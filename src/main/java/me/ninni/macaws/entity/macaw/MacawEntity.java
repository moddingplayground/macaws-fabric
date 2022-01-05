package me.ninni.macaws.entity.macaw;

import com.google.common.collect.Sets;
import me.ninni.macaws.entity.AbstractTameableHeadEntity;
import me.ninni.macaws.entity.MacawsEntities;
import me.ninni.macaws.entity.data.MacawsTrackedDataHandlerRegistry;
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
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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
import java.util.Set;

import static me.ninni.macaws.util.MacawsNbtConstants.*;

public class MacawEntity extends AbstractTameableHeadEntity implements Flutterer {
    private static final TrackedData<MacawVariant> VARIANT = DataTracker.registerData(MacawEntity.class, MacawsTrackedDataHandlerRegistry.MACAW_VARIANT);
    private static final TrackedData<Boolean> HAS_EYEPATCH = DataTracker.registerData(MacawEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private static final Set<Item> TAMING_INGREDIENTS = Sets.newHashSet(Items.MELON_SLICE, Items.GLISTERING_MELON_SLICE, Items.APPLE);
    private static final Item EYEPATCH_GIVE_ITEM = Items.BLACK_WOOL;

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
        this.setVariant(MacawVariant.random(this.random));
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
    public MacawVariant getVariant() {
        return this.dataTracker.get(VARIANT);
    }

    public void setVariant(MacawVariant variant) {
        this.dataTracker.set(VARIANT, variant);
    }

    public boolean hasEyepatch() {
        return this.dataTracker.get(HAS_EYEPATCH);
    }

    public void setHasEyepatch(boolean eyepatch) {
        this.dataTracker.set(HAS_EYEPATCH, eyepatch);
    }

    // tick
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!this.isTamed() && TAMING_INGREDIENTS.contains(stack.getItem())) {
            if (!player.getAbilities().creativeMode) stack.decrement(1);

            if (!this.isSilent()) {
                this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_PARROT_EAT, this.getSoundCategory(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f); // TODO
            }

            if (!this.world.isClient) {
                if (this.random.nextInt(10) == 0) {
                    this.setOwner(player);
                    this.world.sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
                } else {
                    this.world.sendEntityStatus(this, EntityStatuses.ADD_NEGATIVE_PLAYER_REACTION_PARTICLES);
                }
            }

            return ActionResult.success(this.world.isClient);
        }

        if (!this.isInAir() && this.isTamed() && this.isOwner(player)) {
            if (!this.world.isClient) this.setSitting(!this.isSitting());
            return ActionResult.success(this.world.isClient);
        }

        if (!this.world.isClient) {
            if (this.hasEyepatch()) {
                if (FabricToolTags.SHEARS.contains(stack.getItem())) {
                    ItemEntity itemEntity = this.dropItem(EYEPATCH_GIVE_ITEM, 1);
                    if (itemEntity != null) {
                        itemEntity.addVelocity(
                            (this.random.nextFloat() - this.random.nextFloat()) * 0.1f,
                            this.random.nextFloat() * 0.05f,
                            (this.random.nextFloat() - this.random.nextFloat()) * 0.1f
                        );
                    }

                    this.world.playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.PLAYERS, 1.0f, 1.0f); // TODO
                    this.emitGameEvent(GameEvent.SHEAR, player);

                    this.setHasEyepatch(false);
                    return ActionResult.SUCCESS;
                }
            } else {
                if (stack.isOf(EYEPATCH_GIVE_ITEM)) {
                    this.setHasEyepatch(true);
                    this.world.playSoundFromEntity(null, this, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.PLAYERS, 1.0f, 1.0f); // TODO
                    this.emitGameEvent(GameEvent.MOB_INTERACT, player);
                    stack.decrement(1);
                    return ActionResult.SUCCESS;
                }
            }
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
    @Override
    protected boolean hasWings() {
        return this.speed > this.minFlapSpeed;
    }

    @Override
    protected void addFlapEffects() {
        this.playSound(SoundEvents.ENTITY_PARROT_FLY, 0.15f, 1.0f); // TODO
        this.minFlapSpeed = this.speed + this.maxWingDeviation / 2.0f;
    }

    @Override
    public float getSoundPitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
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
        this.dataTracker.startTracking(VARIANT, MacawVariant.SCARLET);
        this.dataTracker.startTracking(HAS_EYEPATCH, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.getVariant().writeToNbt(nbt);
        nbt.putBoolean(NBT_HAS_EYEPATCH, this.hasEyepatch());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setVariant(MacawVariant.readFromNbt(nbt));
        this.setHasEyepatch(nbt.getBoolean(NBT_HAS_EYEPATCH));
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
}
