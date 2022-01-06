package me.ninni.macaws.client.render.entity;

import me.ninni.macaws.client.model.entity.MacawEntityModel;
import me.ninni.macaws.entity.macaw.MacawEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static me.ninni.macaws.client.util.ClientUtil.*;

@Environment(EnvType.CLIENT)
public class MacawRingFeatureRenderer<T extends MacawEntity, M extends MacawEntityModel<T>> extends FeatureRenderer<T, M> {
    private static final Identifier RING = entityTexture("macaw/macaw_ring");

    @SuppressWarnings("unchecked")
    public MacawRingFeatureRenderer(MacawEntityRenderer<T> featureRendererContext) {
        super((FeatureRendererContext<T, M>) featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T macaw, float f, float g, float h, float j, float k, float l) {
        if (macaw.isTamed() && !macaw.isInvisible()) {
            float[] fs = macaw.getRingColor().getColorComponents();
            renderModel(this.getContextModel(), RING, matrixStack, vertexConsumerProvider, i, macaw, fs[0], fs[1], fs[2]);
        }
    }
}
