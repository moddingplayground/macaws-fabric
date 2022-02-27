package net.moddingplayground.macaws.impl.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.moddingplayground.macaws.api.Macaws;
import net.moddingplayground.macaws.impl.client.model.entity.MacawEntityModel;

@Environment(EnvType.CLIENT)
public interface MacawEntityModelLayers {
    EntityModelLayer MACAW = main("macaw", MacawEntityModel::getTexturedModelData);
    EntityModelLayer MACAW_EYEPATCH = main("macaw_eyepatch", MacawEntityModel::getTexturedModelData);
    EntityModelLayer MACAW_RING = main("macaw_ring", MacawEntityModel::getTexturedModelData);

    private static EntityModelLayer register(String id, String layer, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        EntityModelLayer ret = new EntityModelLayer(new Identifier(Macaws.MOD_ID, id), layer);
        EntityModelLayerRegistry.registerModelLayer(ret, provider);
        return ret;
    }

    private static EntityModelLayer main(String id, EntityModelLayerRegistry.TexturedModelDataProvider provider) {
        return register(id, "main", provider);
    }
}
