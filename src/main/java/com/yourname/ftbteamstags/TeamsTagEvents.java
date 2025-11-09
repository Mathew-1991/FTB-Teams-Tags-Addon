package com.yourname.ftbteamstags;

import dev.ftb.mods.ftbteams.api.FTBTeamsAPI;
import dev.ftb.mods.ftbteams.api.Team;
import net.minecraft.network.chat.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/**
 * Event subscriber to add [Team] prefixes from FTB Teams to player names in the tablist, nametags, and chat.
 */
@Mod.EventBusSubscriber(modid = FTBTeamsTagsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TeamsTagEvents {

    // Event: Player logs in to update scoreboard and prefix
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            updateScoreboardTeamPrefix(player);
        }
    }

    // Event: Player respawns to update scoreboard and prefix
    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            updateScoreboardTeamPrefix(player);
        }
    }

    // Event: Player changes dimension to update scoreboard and prefix
    @SubscribeEvent
    public static void onChangeDim(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            updateScoreboardTeamPrefix(player);
        }
    }

    // Event: Chat message to prepend team tag
    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        Team team = getPlayerTeam(player);
        if (team == null) return;

        String tag = getTeamTag(team);

        Component prefix = Component.literal("[" + tag + "] ")
                .withStyle(ChatFormatting.GOLD);

        Component wrapped = prefix.append(event.getComponent());
        event.setComponent(wrapped);
    }

    // Update scoreboard team prefix and nametag
    private static void updateScoreboardTeamPrefix(ServerPlayer player) {
        Team team = getPlayerTeam(player);
        if (team == null) return;

        String tag = getTeamTag(team);

        Scoreboard scoreboard = player.getServer().getScoreboard();

        // unique but short id for scoreboard team
        String scoreTeamId = "ftb_" + teamIdShort(team);

        PlayerTeam sbTeam = scoreboard.getPlayerTeam(scoreTeamId);
        if (sbTeam == null) {
            sbTeam = scoreboard.addPlayerTeam(scoreTeamId);
        }

        // Set prefix for tablist and nametag
        sbTeam.setPlayerPrefix(
                Component.literal("[" + tag + "] ")
                        .withStyle(ChatFormatting.GOLD)
        );

        scoreboard.addPlayerToTeam(player.getScoreboardName(), sbTeam);
    }

    private static String teamIdShort(Team team) {
        // Generate short unique id from team's UUID
        UUID id = team.getId();
        return id.toString().replace("-", "").substring(0, 10);
    }

    private static Team getPlayerTeam(ServerPlayer player) {
        return FTBTeamsAPI.api()
                .getManager()
                .getTeamForPlayerID(player.getUUID());
    }

    private static String getTeamTag(Team team) {
        return team.getName().getString();
    }
}
