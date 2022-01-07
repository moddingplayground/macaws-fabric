package me.ninni.macaws.client.model;

import com.google.common.collect.ImmutableMap;
import me.ninni.macaws.Macaws;
import me.ninni.macaws.client.model.entity.MacawEntityModel;
import me.ninni.macaws.mixin.client.EntityModelLayersInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MacawEntityModelLayers {
    public static final EntityModelLayer MACAW = registerMain("macaw");
    public static final EntityModelLayer MACAW_EYEPATCH = registerMain("macaw_eyepatch");
    public static final EntityModelLayer MACAW_RING = registerMain("macaw_ring");

    static {
        new ImmutableMap.Builder<EntityModelLayer, EntityModelLayerRegistry.TexturedModelDataProvider>()
            .put(MACAW, MacawEntityModel::getTexturedModelData)
            .put(MACAW_EYEPATCH, MacawEntityModel::getTexturedModelData)
            .put(MACAW_RING, MacawEntityModel::getTexturedModelData)
        .build().forEach(EntityModelLayerRegistry::registerModelLayer);
    }

    private static EntityModelLayer registerMain(String id) {
        return EntityModelLayersInvoker.invokeRegister(new Identifier(Macaws.MOD_ID, id).toString(), "main");
    }
}
