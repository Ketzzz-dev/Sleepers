package net.keys.sleepers.init;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final String MOD_ID = "sleepers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Main.LOGGER.info("Initializing Sleepers...");
    }
}
