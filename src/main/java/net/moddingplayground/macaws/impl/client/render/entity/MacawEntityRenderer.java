package net.moddingplayground.macaws.impl.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.api.Macaws;
import net.moddingplayground.macaws.api.entity.MacawEntity;
import net.moddingplayground.macaws.impl.client.model.MacawEntityModelLayers;
import net.moddingplayground.macaws.impl.client.model.entity.MacawEntityModel;

@Environment(EnvType.CLIENT)
public class MacawEntityRenderer<T extends MacawEntity> extends MobEntityRenderer<T, MacawEntityModel<T>> {
    public static final Identifier TEXTURE_EYEPATCH = new Identifier(Macaws.MOD_ID, "textures/entity/macaw/macaw_eyepatch.png");
    public static final Identifier TEXTURE_RING = new Identifier(Macaws.MOD_ID, "textures/entity/macaw/macaw_ring.png");

    public MacawEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new MacawEntityModel<>(ctx.getPart(MacawEntityModelLayers.MACAW)), 0.3f);
        this.addFeature(new MacawEyepatchFeatureRenderer<>(this, new MacawEntityModel<>(ctx.getPart(MacawEntityModelLayers.MACAW_EYEPATCH)), TEXTURE_EYEPATCH));
        this.addFeature(new MacawRingFeatureRenderer<>(this, new MacawEntityModel<>(ctx.getPart(MacawEntityModelLayers.MACAW_RING)), TEXTURE_RING));
    }

    @Override
    protected void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
        super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
        if (entity.isInSittingPose()) {
            matrices.translate(0.0F, -0.075F, 0.0F);
        }
    }

    @Override
    public Identifier getTexture(T entity) {
        return entity.getVariant().getTexture();
    }
}
