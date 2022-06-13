package net.keys.sleepers.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.keys.sleepers.entity.sleeper.SleeperPrototypeEntity;
import net.keys.sleepers.util.id.IdentifierHelper;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

public class ModEntityTypes {
    public static final EntityType<SleeperPrototypeEntity> SLEEPER_PROTOTYPE = Registry.register(
            Registry.ENTITY_TYPE, IdentifierHelper.createId("sleeper_prototype"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SleeperPrototypeEntity::new)
                    .dimensions(EntityDimensions.fixed(0.8f, 1.62f))
                    .build()
    );
}

