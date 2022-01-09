package me.ninni.macaws.client.render.entity;

import me.ninni.macaws.client.model.MacawEntityModelLayers;
import me.ninni.macaws.client.model.entity.MacawEntityModel;
import me.ninni.macaws.entity.MacawEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import static me.ninni.macaws.client.util.ClientUtil.*;

@Environment(EnvType.CLIENT)
public class MacawEntityRenderer<T extends MacawEntity> extends MobEntityRenderer<T, MacawEntityModel<T>> {
    public static final Identifier TEXTURE_EYEPATCH = entityTexture("macaw/macaw_eyepatch");
    public static final Identifier TEXTURE_RING = entityTexture("macaw/macaw_ring");

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
