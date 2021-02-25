/*
 * Copyright (c) 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.lambdaurora.aurorasdeco.client;

import dev.lambdaurora.aurorasdeco.AurorasDeco;
import dev.lambdaurora.aurorasdeco.block.BlackboardBlock;
import dev.lambdaurora.aurorasdeco.client.renderer.BlackboardBlockEntityRenderer;
import dev.lambdaurora.aurorasdeco.client.renderer.BlackboardItemRenderer;
import dev.lambdaurora.aurorasdeco.client.renderer.LanternBlockEntityRenderer;
import dev.lambdaurora.aurorasdeco.client.renderer.WindChimeBlockEntityRenderer;
import dev.lambdaurora.aurorasdeco.registry.AurorasDecoRegistry;
import dev.lambdaurora.aurorasdeco.resource.AurorasDecoPack;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Represents the Aurora's Decorations client mod.
 *
 * @author LambdAurora
 * @version 1.0.0
 * @since 1.0.0
 */
@Environment(EnvType.CLIENT)
public class AurorasDecoClient implements ClientModInitializer {
    public static final AurorasDecoPack RESOURCE_PACK = new AurorasDecoPack();
    public static final ModelIdentifier BLACKBOARD_MASK = new ModelIdentifier(AurorasDeco.id("blackboard_mask"),
            "inventory");

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(AurorasDecoRegistry.BLACKBOARD_BLOCK_ENTITY_TYPE,
                BlackboardBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(AurorasDecoRegistry.LANTERN_BLOCK_ENTITY_TYPE,
                LanternBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(AurorasDecoRegistry.WIND_CHIME_BLOCK_ENTITY_TYPE,
                WindChimeBlockEntityRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(),
                AurorasDecoRegistry.BURNT_VINE_BLOCK);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                AurorasDecoRegistry.WALL_LANTERN_BLOCK,
                AurorasDecoRegistry.WIND_CHIME_BLOCK);

        this.registerBlackboardItemRenderer(AurorasDecoRegistry.BLACKBOARD_BLOCK);
        this.registerBlackboardItemRenderer(AurorasDecoRegistry.CHALKBOARD_BLOCK);
        this.registerBlackboardItemRenderer(AurorasDecoRegistry.WAXED_BLACKBOARD_BLOCK);
        this.registerBlackboardItemRenderer(AurorasDecoRegistry.WAXED_CHALKBOARD_BLOCK);

        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) ->
                        world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor(),
                AurorasDecoRegistry.BURNT_VINE_BLOCK);

        EntityModelLayerRegistry.registerModelLayer(WindChimeBlockEntityRenderer.WIND_CHIME_MODEL_LAYER,
                WindChimeBlockEntityRenderer::getTexturedModelData);
    }

    private void registerBlackboardItemRenderer(BlackboardBlock blackboard) {
        Identifier id = Registry.BLOCK.getId(blackboard);
        ModelIdentifier modelId = new ModelIdentifier(new Identifier(id.getNamespace(), id.getPath() + "_base"),
                "inventory");
        BuiltinItemRendererRegistry.INSTANCE.register(blackboard, new BlackboardItemRenderer(modelId));
        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(modelId);
            out.accept(BLACKBOARD_MASK);
        });
    }
}
