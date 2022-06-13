package net.keys.sleepers.client.model;

import net.keys.sleepers.entity.sleeper.SleeperPrototypeEntity;
import net.keys.sleepers.util.id.IdentifierHelper;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SleeperPrototypeModel extends AnimatedGeoModel<SleeperPrototypeEntity> {
    @Override
    public Identifier getModelResource(SleeperPrototypeEntity object) {
        return IdentifierHelper.createId("geo/sleeper_prototype.geo.json");
    }

    @Override
    public Identifier getTextureResource(SleeperPrototypeEntity object) {
        return IdentifierHelper.createId("textures/entity/sleeper_prototype.png");
    }

    @Override
    public Identifier getAnimationResource(SleeperPrototypeEntity animatable) {
        return IdentifierHelper.createId("animations/sleeper_prototype.animation.json");
    }
}