package net.keys.sleepers.client.render;

import net.keys.sleepers.client.model.SleeperPrototypeModel;
import net.keys.sleepers.entity.sleeper.SleeperPrototypeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class SleeperPrototypeRenderer  extends GeoEntityRenderer<SleeperPrototypeEntity> {
    public SleeperPrototypeRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new SleeperPrototypeModel());
    }
}