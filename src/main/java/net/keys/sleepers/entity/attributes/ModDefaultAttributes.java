package net.keys.sleepers.entity.attributes;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.keys.sleepers.entity.ModEntityTypes;
import net.keys.sleepers.entity.sleeper.SleeperPrototypeEntity;
import net.keys.sleepers.init.Main;

public class ModDefaultAttributes {

    public static void registerDefaultAttributes() {
        Main.LOGGER.info("Registering default attributes...");

        FabricDefaultAttributeRegistry.register(ModEntityTypes.SLEEPER_PROTOTYPE, SleeperPrototypeEntity.createSleeperPrototypeAttributes());
    }
}
