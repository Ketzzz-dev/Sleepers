package net.keys.sleepers.init;

import net.fabricmc.api.ModInitializer;
import net.keys.sleepers.entity.attributes.ModDefaultAttributes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {
    public static final String MOD_ID = "sleepers";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        Main.LOGGER.info("Initializing Sleepers...");

        ModDefaultAttributes.registerDefaultAttributes();
    }
}
