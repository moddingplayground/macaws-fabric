package me.ninni.macaws.client.model.entity;

import com.google.common.collect.ImmutableList;
import me.ninni.macaws.entity.macaw.MacawEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.util.math.MathHelper;

@SuppressWarnings({ "FieldCanBeLocal", "unused" })
@Environment(EnvType.CLIENT)
public class MacawEntityModel<T extends MacawEntity> extends AnimalModel<T> {
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
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        limbDistance = MathHelper.clamp(limbDistance, -0.35F, 0.35F);
        float speed = 1.5f;
        float degree = 1.25f;
        this.right_wing.roll = 0.0F;
        this.left_wing.roll = 0.0F;
        this.left_wing.yaw = 0.0F;
        this.right_wing.yaw = 0.0F;
        this.body.pitch = 0.4F;
        this.tail.pitch = - 0.8F;
        this.left_leg.pitch = 0.0F;
        this.right_leg.pitch = 0.0F;
        this.head.pivotZ = - 3.5F;
        this.head.pitch = headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;

        if (!entity.isOnGround()) {
            this.right_wing.roll = MathHelper.cos(-1.0F + animationProgress * speed * 1.0F) * degree * 2.0F * 0.25F + 0.5F;
            this.left_wing.roll = MathHelper.cos(-1.0F + animationProgress * speed * 1.0F) * degree * -2.0F * 0.25F - 0.5F;
            this.left_wing.yaw = MathHelper.cos(-1.0F + animationProgress * speed * 0.5F) * degree * -1.0F * 0.25F;
            this.right_wing.yaw = MathHelper.cos(-1.0F + animationProgress * speed * 0.5F) * degree * 1.0F * 0.25F;
            this.body.pitch = MathHelper.cos(animationProgress * speed * 0.25F) * degree * 0.2F * 0.25F + 0.4F;
            this.tail.pitch = MathHelper.cos(-10.0F + animationProgress * speed * 0.25F) * degree * 0.3F * 0.25F - 0.8F;
            this.body.pivotY = MathHelper.cos(limbAngle * speed * 0.5F) * degree * 1.5F * limbDistance + 18.0F;
            this.head.pitch = MathHelper.cos(animationProgress * speed * 0.25F) * degree * 0.2F * 0.25F;
            this.head.pivotY = MathHelper.cos(-1.0F + limbAngle * speed * 0.5F) * degree * 1.5F * limbDistance + 15.0F;
            this.left_leg.pitch = MathHelper.cos(animationProgress * speed * 0.5F) * degree * 0.4F * 0.25F + 0.8F;
            this.right_leg.pitch = MathHelper.cos(-2.0F + animationProgress * speed * 0.5F) * degree * -0.4F * 0.25F + 0.8F;
        }
        if (entity.isInSittingPose()) {
            this.body.pitch = 0.0F;
            this.tail.pitch = 0.8F;
            this.head.pivotY = 14.4F;
            this.head.pivotZ = - 2.0F;
            this.left_leg.pitch = -0.75F;
            this.right_leg.pitch = -0.75F;
            this.body.pivotY = + 18.125F;
        }

        //if (entity.isWhistling()) {
        //this.beak.pitch = MathHelper.cos(limbAngle * speed * 0.1F) * degree * 0.3F * limbDistance - 0.2F;
        //this.jaw.pitch = MathHelper.cos(-1.0F + limbAngle * speed * 0.1F) * degree * 0.4F * limbDistance + 0.1F;
        //this.head.roll = MathHelper.cos(limbAngle * speed * 0.05F) * degree * 0.4F * limbDistance;
        //this.body.roll = MathHelper.cos(-1.0F + limbAngle * speed * 0.05F) * degree * 0.4F * limbDistance;
        //this.tail.roll = MathHelper.cos(-1.0F + limbAngle * speed * 0.05F) * degree * 0.8F * limbDistance;
        //}

    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.left_leg, this.right_leg);
    }
}
