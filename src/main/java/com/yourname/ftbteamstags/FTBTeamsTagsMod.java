package com.yourname.ftbteamstags;

import net.minecraftforge.fml.common.Mod;

/**
 * Main mod class for FTB Teams Tags. This class is used by Forge to identify the mod and set up the
 * mod container. All heavy lifting happens in the event subscriber (TeamsTagEvents).
 */
@Mod(FTBTeamsTagsMod.MOD_ID)
public class FTBTeamsTagsMod {
    /**
     * The mod ID used throughout the mod. Must match the modId in mods.toml.
     */
    public static final String MOD_ID = "ftbteamstags";

    public FTBTeamsTagsMod() {
        // Intentionally left blank. All registration is handled via annotations in TeamsTagEvents.
    }
}
