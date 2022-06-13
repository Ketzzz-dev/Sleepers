package net.keys.sleepers.init;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.keys.sleepers.client.render.SleeperPrototypeRenderer;
import net.keys.sleepers.entity.ModEntityTypes;

public class Client implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntityTypes.SLEEPER_PROTOTYPE, SleeperPrototypeRenderer::new);
    }
}
