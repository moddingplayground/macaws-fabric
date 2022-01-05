package me.ninni.macaws.client.model.entity;

import me.ninni.macaws.entity.macaw.MacawEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

@SuppressWarnings({ "FieldCanBeLocal", "unused" })
@Environment(EnvType.CLIENT)
public class MacawEntityModel<T extends MacawEntity> extends SinglePartEntityModel<T> {
    private final ModelPart root;

    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart left_wing;
    private final ModelPart right_wing;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart beak;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public MacawEntityModel(ModelPart root) {
        this.root = root;

        this.body = this.root.getChild("body");
        this.tail = this.body.getChild("tail");
        this.left_wing = this.body.getChild("left_wing");
        this.right_wing = this.body.getChild("right_wing");

        this.head = this.root.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.beak = this.head.getChild("beak");

        this.left_leg = this.root.getChild("left_leg");
        this.right_leg = this.root.getChild("right_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        ModelPartData body = root.addChild(
            "body",
            ModelPartBuilder.create()
                            .uv(0, 11)
                            .mirrored(false)
                            .cuboid(-2.5F, -4.0F, -2.5F, 5.0F, 8.0F, 5.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, 18.0F, -1.5F, 0.3927F, 0.0F, 0.0F)
        );

        ModelPartData tail = body.addChild(
            "tail",
            ModelPartBuilder.create()
                            .uv(0, 0)
                            .mirrored(false)
                            .cuboid(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 10.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, 3.0F, 2.5F, -0.829F, 0.0F, 0.0F)
        );

        ModelPartData left_wing = body.addChild(
            "left_wing",
            ModelPartBuilder.create()
                            .uv(20, 11)
                            .mirrored(false)
                            .cuboid(0.0F, 0.0F, -2.0F, 1.0F, 8.0F, 4.0F, new Dilation(0.0F))
                            .uv(26, 10)
                            .mirrored(false)
                            .cuboid(0.0F, 8.0F, -1.0F, 0.0F, 2.0F, 3.0F, new Dilation(0.0F)),
            ModelTransform.of(2.5F, -3.0F, 0.0F, 0.3927F, 0.0F, 0.0F)
        );

        ModelPartData right_wing = body.addChild(
            "right_wing",
            ModelPartBuilder.create()
                            .uv(20, 11)
                            .mirrored(true)
                            .cuboid(-1.0F, 0.0F, -2.0F, 1.0F, 8.0F, 4.0F, new Dilation(0.0F))
                            .uv(26, 10)
                            .mirrored(false)
                            .cuboid(0.0F, 8.0F, -1.0F, 0.0F, 2.0F, 3.0F, new Dilation(0.0F)),
            ModelTransform.of(-2.5F, -3.0F, 0.0F, 0.3927F, 0.0F, 0.0F)
        );

        ModelPartData head = root.addChild(
            "head",
            ModelPartBuilder.create()
                            .uv(16, 0)
                            .mirrored(false)
                            .cuboid(-2.0F, -5.0F, -3.0F, 4.0F, 5.0F, 5.0F, new Dilation(0.0F))
                            .uv(30, 22)
                            .mirrored(false)
                            .cuboid(-2.0F, -5.0F, -3.0F, 4.0F, 5.0F, 5.0F, new Dilation(0.25F)),
            ModelTransform.of(0.0F, 15.0F, -3.5F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData jaw = head.addChild(
            "jaw",
            ModelPartBuilder.create()
                            .uv(15, 11)
                            .mirrored(false)
                            .cuboid(-1.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, -2.0F, -3.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData beak = head.addChild(
            "beak",
            ModelPartBuilder.create()
                            .uv(4, 6)
                            .mirrored(false)
                            .cuboid(-1.0F, 0.25F, -3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                            .uv(0, 0)
                            .mirrored(false)
                            .cuboid(-1.0F, -2.75F, -3.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, -2.25F, -3.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData right_leg = root.addChild(
            "right_leg",
            ModelPartBuilder.create()
                            .uv(0, 24)
                            .mirrored(false)
                            .cuboid(-0.5F, 0.0F, -2.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)),
            ModelTransform.of(-1.0F, 21.0F, 0.5F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData left_leg = root.addChild(
            "left_leg",
            ModelPartBuilder.create()
                            .uv(0, 24)
                            .mirrored(false)
                            .cuboid(-0.5F, 0.0F, -2.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)),
            ModelTransform.of(1.0F, 21.0F, 0.5F, 0.0F, 0.0F, 0.0F)
        );

        return TexturedModelData.of(data, 48, 32);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {}

    @Override
    public ModelPart getPart() {
        return this.root;
    }
}
