package net.moddingplayground.macaws.impl.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.moddingplayground.macaws.api.entity.MacawEntity;

import static net.minecraft.client.render.entity.model.EntityModelPartNames.*;

@SuppressWarnings({ "FieldCanBeLocal", "unused" })
@Environment(EnvType.CLIENT)
public class MacawEntityModel<T extends MacawEntity> extends AnimalModel<T> {
    private final ModelPart root;

    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart beak;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public MacawEntityModel(ModelPart root) {
        this.root = root;

        this.body = this.root.getChild(BODY);
        this.tail = this.body.getChild(TAIL);
        this.leftWing = this.body.getChild(LEFT_WING);
        this.rightWing = this.body.getChild(RIGHT_WING);

        this.head = this.root.getChild(HEAD);
        this.jaw = this.head.getChild(JAW);
        this.beak = this.head.getChild(BEAK);

        this.leftLeg = this.root.getChild(LEFT_LEG);
        this.rightLeg = this.root.getChild(RIGHT_LEG);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData data = new ModelData();
        ModelPartData root = data.getRoot();

        ModelPartData body = root.addChild(
            BODY,
            ModelPartBuilder.create()
                            .uv(0, 11)
                            .mirrored(false)
                            .cuboid(-2.5F, -4.0F, -2.5F, 5.0F, 8.0F, 5.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, 18.0F, -1.5F, 0.3927F, 0.0F, 0.0F)
        );

        ModelPartData tail = body.addChild(
            TAIL,
            ModelPartBuilder.create()
                            .uv(0, 0)
                            .mirrored(false)
                            .cuboid(-1.5F, 0.0F, 0.0F, 3.0F, 1.0F, 10.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, 3.0F, 2.5F, -0.829F, 0.0F, 0.0F)
        );

        ModelPartData left_wing = body.addChild(
            LEFT_WING,
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
            RIGHT_WING,
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
            HEAD,
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
            JAW,
            ModelPartBuilder.create()
                            .uv(15, 11)
                            .mirrored(false)
                            .cuboid(-1.0F, 0.0F, -2.0F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, -2.0F, -3.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData beak = head.addChild(
            BEAK,
            ModelPartBuilder.create()
                            .uv(4, 6)
                            .mirrored(false)
                            .cuboid(-1.0F, 0.25F, -3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
                            .uv(0, 0)
                            .mirrored(false)
                            .cuboid(-1.0F, -2.75F, -3.0F, 2.0F, 3.0F, 3.0F, new Dilation(0.0F)),
            ModelTransform.of(0.0F, -2.25F, -3.0F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData left_leg = root.addChild(
            LEFT_LEG,
            ModelPartBuilder.create()
                            .uv(0, 24)
                            .mirrored(false)
                            .cuboid(-0.5F, 0.0F, -2.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)),
            ModelTransform.of(1.0F, 21.0F, 0.5F, 0.0F, 0.0F, 0.0F)
        );

        ModelPartData right_leg = root.addChild(
            RIGHT_LEG,
            ModelPartBuilder.create()
                            .uv(0, 24)
                            .mirrored(false)
                            .cuboid(-0.5F, 0.0F, -2.0F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F)),
            ModelTransform.of(-1.0F, 21.0F, 0.5F, 0.0F, 0.0F, 0.0F)
        );

        return TexturedModelData.of(data, 48, 32);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.setAngles(entity.getMacawPose(), 0, limbAngle, limbDistance, animationProgress, headYaw, headPitch, false);
    }

    public void setAngles(MacawEntity.Pose pose, float air, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch, boolean mounted) {
        limbDistance = MathHelper.clamp(limbDistance, -0.35F, 0.35F);

        float speed = 1.5f;
        float degree = 1.25f;

        this.rightWing.roll = 0.0F;
        this.leftWing.roll = 0.0F;
        this.leftWing.yaw = 0.0F;
        this.rightWing.yaw = 0.0F;
        this.body.pitch = 0.4F;
        this.body.pivotY = 18.0F;
        this.tail.pitch = - 0.8F;
        this.leftLeg.pitch = 0.0F;
        this.rightLeg.pitch = 0.0F;
        this.head.pivotZ = - 3.5F;
        this.head.pitch = headPitch * (float) (Math.PI / 180);
        this.head.yaw = headYaw * (float) (Math.PI / 180);
        this.head.pivotY = 15.0F;

        switch (pose) {
            case AIR -> {
                this.rightWing.roll = MathHelper.cos(-1.0F + animationProgress * speed * 1.0F) * degree * 2.0F * 0.25F + 0.5F;
                this.leftWing.roll = MathHelper.cos(-1.0F + animationProgress * speed * 1.0F) * degree * -2.0F * 0.25F - 0.5F;
                this.leftWing.yaw = MathHelper.cos(-1.0F + animationProgress * speed * 0.5F) * degree * -1.0F * 0.25F;
                this.rightWing.yaw = MathHelper.cos(-1.0F + animationProgress * speed * 0.5F) * degree * 1.0F * 0.25F;
                this.body.pitch = MathHelper.cos(animationProgress * speed * 0.25F) * degree * 0.2F * 0.25F + 0.4F;
                this.tail.pitch = MathHelper.cos(-10.0F + animationProgress * speed * 0.25F) * degree * 0.3F * 0.25F - 0.8F;
                this.body.pivotY = MathHelper.cos(limbAngle * speed * 0.5F) * degree * 1.5F * limbDistance + 18.0F;
                this.head.pitch = MathHelper.cos(animationProgress * speed * 0.25F) * degree * 0.2F * 0.25F;
                this.head.pivotY = MathHelper.cos(-1.0F + limbAngle * speed * 0.5F) * degree * 1.5F * limbDistance + 15.0F;
                this.leftLeg.pitch = MathHelper.cos(animationProgress * speed * 0.5F) * degree * 0.4F * 0.25F + 0.8F;
                this.rightLeg.pitch = MathHelper.cos(-2.0F + animationProgress * speed * 0.5F) * degree * -0.4F * 0.25F + 0.8F;
            }
            case SITTING -> {
                this.body.pitch = 0.0F;
                this.tail.pitch = 0.8F;
                this.head.pivotY = 14.4F;
                this.head.pivotZ = - 2.0F;
                this.leftLeg.pitch = -0.75F;
                this.rightLeg.pitch = -0.75F;
                this.body.pivotY = 18.125F;
            }
            case WHISTLING -> {
                this.beak.pitch = MathHelper.cos(limbAngle * speed * 0.1F) * degree * 0.3F * limbDistance - 0.2F;
                this.jaw.pitch = MathHelper.cos(-1.0F + limbAngle * speed * 0.1F) * degree * 0.4F * limbDistance + 0.1F;
                this.head.roll = MathHelper.cos(limbAngle * speed * 0.05F) * degree * 0.4F * limbDistance;
                this.body.roll = MathHelper.cos(-1.0F + limbAngle * speed * 0.05F) * degree * 0.4F * limbDistance;
                this.tail.roll = MathHelper.cos(-1.0F + limbAngle * speed * 0.05F) * degree * 0.8F * limbDistance;
            }
        }

        if (mounted) {
            this.tail.pitch = -0.225F;
            this.head.pitch = 0.225F;
            this.tail.pitch += MathHelper.cos(animationProgress * speed * 0.05F) * degree * 0.15F * 0.25F;
            this.rightWing.roll = MathHelper.cos(-1.0F + animationProgress * speed * 0.05F) * degree * 0.1F * 0.25F + 0.05F;
            this.leftWing.roll = MathHelper.cos(-1.0F + animationProgress * speed * 0.05F) * degree * -0.1F * 0.25F - 0.05F;

            if (pose == MacawEntity.Pose.AIR) {
                float deg = degree / air;
                float width = 0.55F / air;
                this.leftLeg.pitch = 0.0F;
                this.rightLeg.pitch = 0.0F;
                this.body.pivotY = MathHelper.cos(limbAngle * speed * 0.5F) * degree * 1.5F * limbDistance + 18.0F;
                this.head.pivotY = MathHelper.cos(-1.0F + limbAngle * speed * 0.5F) * degree * 1.5F * limbDistance + 15.0F;
                this.head.pitch += MathHelper.cos(animationProgress * speed * 0.25F) * degree * 0.2F * 0.25F;
                this.rightWing.roll = MathHelper.cos(-1.0F + animationProgress * speed * 1.0F) * deg * 2.0F * 0.25F + width;
                this.leftWing.roll = MathHelper.cos(-1.0F + animationProgress * speed * 1.0F) * deg * -2.0F * 0.25F - width;
                this.leftWing.yaw = MathHelper.cos(-1.0F + animationProgress * speed * 0.5F) * deg * -1.0F * 0.25F;
                this.rightWing.yaw = MathHelper.cos(-1.0F + animationProgress * speed * 0.5F) * deg * 1.0F * 0.25F;
            }
        }
    }

    public void renderOnHead(MatrixStack matrices, VertexConsumer vertex, int light, int overlay, float animationProgress, float air, float red, float green, float blue) {
        this.setAngles(air > 0 ? MacawEntity.Pose.AIR : MacawEntity.Pose.SITTING, air, 0.0f, 0.0f, animationProgress, 0.0f, 0.0f, true);
        this.root.render(matrices, vertex, light, overlay, red, green, blue, 1.0F);
    }

    public void renderOnHead(MatrixStack matrices, VertexConsumer vertex, int light, int overlay, float animationProgress, float air) {
        this.renderOnHead(matrices, vertex, light, overlay, animationProgress, air, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.leftLeg, this.rightLeg);
    }
}
