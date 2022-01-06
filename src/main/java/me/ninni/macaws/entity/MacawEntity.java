package me.ninni.macaws.entity;

import com.google.common.collect.ImmutableSet;
import me.ninni.macaws.entity.data.MacawsTrackedDataHandlerRegistry;
import me.ninni.macaws.entity.data.TrackedDataPackager;
import me.ninni.macaws.sound.MacawsSoundEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.FollowMobGoal;
import net.minecraft.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SitGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import static me.ninni.macaws.client.util.ClientUtil.*;
import static me.ninni.macaws.util.MacawsNbtConstants.*;
import static net.minecraft.nbt.NbtElement.*;

public class MacawEntity extends AbstractTameableHeadEntity implements Flutterer {
    public static final TrackedData<Variant> VARIANT = DataTracker.registerData(MacawEntity.class, MacawsTrackedDataHandlerRegistry.MACAW_VARIANT);
    public static final TrackedData<Personality> PERSONALITY = DataTracker.registerData(MacawEntity.class, MacawsTrackedDataHandlerRegistry.MACAW_PERSONALITY);
    public static final TrackedData<Boolean> HAS_EYEPATCH = DataTracker.registerData(MacawEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public static final TrackedData<Integer> RING_COLOR = DataTracker.registerData(MacawEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public static final ImmutableSet<Item> TAMING_INGREDIENTS = ImmutableSet.of(
        Items.MELON_SLICE,
        Items.APPLE,
        Items.POTATO,
        Items.BEETROOT,
        Items.CARROT,
        Items.MELON_SEEDS,
        Items.PUMPKIN_SEEDS,
        Items.WHEAT_SEEDS,
        Items.BEETROOT_SEEDS
    );

    public static final float PITCH_DEVIANCE = 0.125f;
    public static final Item EYEPATCH_GIVE_ITEM = Items.BLACK_WOOL;

    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    private float flapSpeed = 1.0f;
    private float minFlapSpeed = 1.0f;

    public MacawEntity(EntityType<? extends TameableEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 10, false);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData data, @Nullable NbtCompound nbt) {
        this.setVariant(Variant.random(this.random));
        this.setPersonality(Personality.random(this.random));
        if (data == null) data = new PassiveEntity.PassiveData(false);
        return super.initialize(world, difficulty, spawnReason, data, nbt);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation nav = new BirdNavigation(this, world);
        nav.setCanPathThroughDoors(false);
        nav.setCanSwim(true);
        nav.setCanEnterOpenDoors(true);
        return nav;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25D));
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(2, new SitGoal(this));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.0D, 5.0f, 1.0f, true));
        this.goalSelector.add(2, new WanderGoal(this, 1.0));
        // this.goalSelector.add(3, new SitOnOwnerShoulderGoal(this));
        this.goalSelector.add(3, new FollowMobGoal(this, 1.0, 3.0f, 7.0f));
    }

    // getters/setters
    public Variant getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void setVariant(Variant variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    public Personality getPersonality() {
        return this.dataTracker.get(PERSONALITY);
    }

    public void setPersonality(Personality personality) {
        this.dataTracker.set(PERSONALITY, personality);
    }

    public boolean hasEyepatch() {
        return this.dataTracker.get(HAS_EYEPATCH);
    }

    public void setHasEyepatch(boolean eyepatch) {
        this.dataTracker.set(HAS_EYEPATCH, eyepatch);
    }

    public DyeColor getRingColor() {
        return DyeColor.byId(this.dataTracker.get(RING_COLOR));
    }

    public void setRingColor(DyeColor color){
        this.dataTracker.set(RING_COLOR, color.getId());
    }

    // tick
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        Item item = stack.getItem();

        if (!this.world.isClient) {
            if (this.hasEyepatch()) {
                if (FabricToolTags.SHEARS.contains(stack.getItem())) {
                    if (!player.getAbilities().creativeMode) {
                        ItemEntity itemEntity = this.dropItem(EYEPATCH_GIVE_ITEM, 1);
                        if (itemEntity != null) {
                            itemEntity.addVelocity(
                                (this.random.nextFloat() - this.random.nextFloat()) * 0.1f,
                                this.random.nextFloat() * 0.05f,
                                (this.random.nextFloat() - this.random.nextFloat()) * 0.1f
                            );
                        }
                    }

                    this.world.playSoundFromEntity(null, this, MacawsSoundEvents.ENTITY_MACAW_SHEAR, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    this.emitGameEvent(GameEvent.SHEAR, player);

                    this.setHasEyepatch(false);
                    return ActionResult.SUCCESS;
                }
            } else {
                if (stack.isOf(EYEPATCH_GIVE_ITEM)) {
                    this.setHasEyepatch(true);
                    this.world.playSoundFromEntity(null, this, MacawsSoundEvents.ENTITY_MACAW_EQUIP_EYEPATCH, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    this.emitGameEvent(GameEvent.MOB_INTERACT, player);
                    stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
            }

            if (this.isTamed()) {
                if (TAMING_INGREDIENTS.contains(stack.getItem()) && this.getHealth() < this.getMaxHealth()) {
                    if (!this.isSilent()) {
                        this.world.playSoundFromEntity(null, this, MacawsSoundEvents.ENTITY_MACAW_EAT, this.getSoundCategory(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
                        this.world.sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
                    }

                    if (!player.getAbilities().creativeMode) stack.decrement(1);
                    this.heal(4.0f);
                    return ActionResult.SUCCESS;
                }

                if (!(item instanceof DyeItem)) {
                    ActionResult actionResult = super.interactMob(player, hand);
                    if ((!actionResult.isAccepted() || this.isBaby()) && this.isOwner(player)) {
                        this.setSitting(!this.isSitting());
                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget(null);
                        return ActionResult.SUCCESS;
                    }

                    return actionResult;
                }

                DyeColor dyeColor = ((DyeItem)item).getColor();
                if (dyeColor != this.getRingColor()) {
                    this.setRingColor(dyeColor);
                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }

                    return ActionResult.SUCCESS;
                }
            }
        }

        if (!this.isInAir() && this.isTamed() && this.isOwner(player)) {
            if (!this.world.isClient) this.setSitting(!this.isSitting());
            return ActionResult.success(this.world.isClient);
        }

        if (!this.isTamed() && TAMING_INGREDIENTS.contains(stack.getItem())) {
            if (!player.getAbilities().creativeMode) stack.decrement(1);

            if (!this.isSilent()) {
                this.world.playSound(null, this.getX(), this.getY(), this.getZ(), MacawsSoundEvents.ENTITY_MACAW_EAT, this.getSoundCategory(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
            }

            if (!this.world.isClient) {
                if (this.random.nextInt(5) == 0) {
                    this.setOwner(player);
                    this.world.sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
                } else {
                    this.world.sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
                }
            }

            return ActionResult.success(this.world.isClient);
        }

        return super.interactMob(player, hand);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        this.flapWings();
    }

    public void flapWings() {
        this.prevMaxWingDeviation = this.maxWingDeviation;
        this.maxWingDeviation = (float)((double)this.maxWingDeviation + (double)(this.onGround || this.hasVehicle() ? -1 : 4) * 0.3);
        this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0f, 1.0f);

        if (!this.onGround && this.flapSpeed < 1.0f) this.flapSpeed = 1.0f;
        this.flapSpeed = (float)((double)this.flapSpeed * 0.9);

        this.prevFlapProgress = this.flapProgress;
        this.flapProgress += this.flapSpeed * 2.0f;

        Vec3d vel = this.getVelocity();
        if (!this.onGround && vel.y < 0.0) this.setVelocity(vel.multiply(1.0, 0.6, 1.0));
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        if (this.hasEyepatch()) this.dropItem(EYEPATCH_GIVE_ITEM);
    }

    // misc
    @SuppressWarnings("ConstantConditions")
    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(16.0D);
            this.setHealth(this.getMaxHealth());
        } else {
            this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(8.0D);
        }
    }

    @Override
    protected boolean hasWings() {
        return this.speed > this.minFlapSpeed;
    }

    @Override
    protected void addFlapEffects() {
        this.playSound(MacawsSoundEvents.ENTITY_MACAW_FLY, 0.15f, 1.0f);
        this.minFlapSpeed = this.speed + this.maxWingDeviation / 2.0f;
    }

    @Override
    public float getSoundPitch() {
        return this.getPersonality().pitch() + (this.random.nextFloat(PITCH_DEVIANCE * 2) - PITCH_DEVIANCE);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isTamed() ? MacawsSoundEvents.ENTITY_MACAW_AMBIENT_TAMED : MacawsSoundEvents.ENTITY_MACAW_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTamed() ? MacawsSoundEvents.ENTITY_MACAW_DEATH_TAMED : MacawsSoundEvents.ENTITY_MACAW_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.isTamed() ? MacawsSoundEvents.ENTITY_MACAW_HURT_TAMED : MacawsSoundEvents.ENTITY_MACAW_HURT;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        if (entity instanceof PlayerEntity) return;
        super.pushAwayFrom(entity);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) return false;
        this.setSitting(false);
        return super.damage(source, amount);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.6f;
    }

    @Override
    public Vec3d getLeashOffset() {
        return new Vec3d(0.0D, this.getStandingEyeHeight() / 2, this.getWidth() * 0.4D);
    }

    @Override
    public boolean isInAir() {
        return !this.onGround;
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return MacawsEntities.MACAW.create(world);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {}

    @Override
    public boolean canBreedWith(AnimalEntity other) {
        return false;
    }

    // data
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VARIANT, Variant.SCARLET);
        this.dataTracker.startTracking(HAS_EYEPATCH, false);
        this.dataTracker.startTracking(PERSONALITY, Personality.EMPTY);
        this.dataTracker.startTracking(RING_COLOR, DyeColor.RED.getId());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.getVariant().writeToNbt(nbt);
        this.getPersonality().writeToNbt(nbt);
        nbt.putBoolean(NBT_HAS_EYEPATCH, this.hasEyepatch());
        nbt.putByte(NBT_RING_COLOR, (byte) this.getRingColor().getId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVariant(Variant.readFromNbt(nbt));
        this.setPersonality(Personality.readFromNbt(nbt));
        this.setHasEyepatch(nbt.getBoolean(NBT_HAS_EYEPATCH));
        if (nbt.contains(NBT_RING_COLOR, NUMBER_TYPE)) this.setRingColor(DyeColor.byId(nbt.getInt(NBT_RING_COLOR)));
    }

    public static class WanderGoal extends FlyGoal {
        public WanderGoal(MacawEntity mob, double speed) {
            super(mob, speed);
        }

        @Override
        protected Vec3d getWanderTarget() {
            Optional<Vec3d> pos = Optional.empty();
            if (this.mob.isTouchingWater()) pos = Optional.ofNullable(FuzzyTargeting.find(this.mob, 15, 15));
            if (this.mob.getRandom().nextFloat() >= this.probability) pos = this.locateTree();
            return pos.orElseGet(super::getWanderTarget);
        }

        private Optional<Vec3d> locateTree() {
            BlockPos pos = this.mob.getBlockPos();
            BlockPos.Mutable mut1 = new BlockPos.Mutable();
            BlockPos.Mutable mut2 = new BlockPos.Mutable();
            Iterable<BlockPos> iterable = BlockPos.iterate(
                MathHelper.floor(this.mob.getX() - 3.0),
                MathHelper.floor(this.mob.getY() - 6.0),
                MathHelper.floor(this.mob.getZ() - 3.0),
                MathHelper.floor(this.mob.getX() + 3.0),
                MathHelper.floor(this.mob.getY() + 6.0),
                MathHelper.floor(this.mob.getZ() + 3.0)
            );

            for (BlockPos p : iterable) {
                if (pos.equals(p)) continue;
                BlockState state = this.mob.world.getBlockState(mut1.set(p, Direction.DOWN));
                if (!(state.getBlock() instanceof LeavesBlock || state.isIn(BlockTags.LOGS))) continue;
                if (!this.mob.world.isAir(p)) continue;
                if (!this.mob.world.isAir(mut2.set(p, Direction.UP))) continue;
                return Optional.of(Vec3d.ofBottomCenter(p));
            }

            return Optional.empty();
        }
    }

    public enum Variant {
        BLUE_AND_GOLD, SCARLET, HYACINTH, GREEN;

        @Environment(EnvType.CLIENT)
        private static final Function<String, Identifier> ID_TO_TEXTURE = Util.memoize(id -> entityTexture("macaw/macaw_%s".formatted(id)));

        private static final Variant[] VALUES = Variant.values();
        public static final Variant DEFAULT = VALUES[0];

        private final String id;

        Variant() {
            this.id = this.name().toLowerCase();
        }

        public String getId() {
            return this.id;
        }

        public Identifier getTexture() {
            return ID_TO_TEXTURE.apply(this.getId());
        }

        public void writeToNbt(NbtCompound nbt) {
            nbt.putString(NBT_MACAW_VARIANT, this.name());
        }

        public static Variant readFromNbt(NbtCompound nbt) {
            return Variant.safeValueOf(nbt.getString(NBT_MACAW_VARIANT));
        }

        public static Variant random(Random random) {
            return VALUES[random.nextInt(VALUES.length)];
        }

        public static Variant safeValueOf(String name) {
            try { return Variant.valueOf(name); } catch (IllegalArgumentException ignored) {}
            return DEFAULT;
        }
    }

    public record Personality(float pitch) implements TrackedDataPackager<Personality> {
        public static final Personality EMPTY = new Personality(1.0f);

        public static final float MIN_PITCH = 0.5F;
        public static final float MAX_PITCH = 1.5F;
        public static final int DECIMAL_ACCURACY = 100;

        public void writeToNbt(NbtCompound nbt) {
            NbtCompound personalityNbt = new NbtCompound();
            personalityNbt.putFloat(NBT_PERSONALITY_PITCH, this.pitch());
            nbt.put(NBT_PERSONALITY, personalityNbt);
        }

        public static Personality readFromNbt(NbtCompound nbt) {
            NbtCompound personalityNbt = nbt.getCompound(NBT_PERSONALITY);
            if (personalityNbt != null) {
                float pitch = personalityNbt.getFloat(NBT_PERSONALITY_PITCH);
                return new Personality(pitch);
            }
            return Personality.EMPTY;
        }

        @Override
        public void toPacket(PacketByteBuf buf) {
            buf.writeFloat(this.pitch());
        }

        @Override
        public Personality fromPacket(PacketByteBuf buf) {
            return new Personality(buf.readFloat());
        }

        @Override
        public Personality copyForPackager() {
            return this;
        }

        public static Personality random(Random random) {
            float pitch = (random.nextFloat(MAX_PITCH - MIN_PITCH) + MIN_PITCH);
            float pitchRnd = ((float) (int) (pitch * DECIMAL_ACCURACY)) / DECIMAL_ACCURACY;
            return new Personality(pitchRnd);
        }
    }
}
