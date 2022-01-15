package net.moddingplayground.macaws.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.client.model.entity.MacawEntityModel;
import net.moddingplayground.macaws.entity.MacawEntity;

@Environment(EnvType.CLIENT)
public class MacawRingFeatureRenderer<T extends MacawEntity, M extends MacawEntityModel<T>> extends FeatureRenderer<T, M> {
    private final M model;
    private final Identifier texture;

    public MacawRingFeatureRenderer(FeatureRendererContext<T, M> context, M model, Identifier texture) {
        super(context);
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertices, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.isTamed()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (entity.isInvisibleTo(client.player)) return;

        boolean invisible = entity.isInvisible();
        RenderLayer layer = invisible ? RenderLayer.getEntityTranslucent(this.texture) : RenderLayer.getEntityCutoutNoCull(this.texture);
        float[] col = entity.getRingColor().getColorComponents();

        this.getContextModel().copyStateTo(this.model);
        this.model.animateModel(entity, limbAngle, limbDistance, tickDelta);
        this.model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        VertexConsumer vertex = vertices.getBuffer(layer);
        this.model.render(matrices, vertex, light, OverlayTexture.DEFAULT_UV, col[0], col[1], col[2], invisible ? 0.15f : 1.0f);
    }
}
