package net.keys.sleepers.entity.sleeper;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import net.keys.sleepers.entity.ai.goal.AlertNearbySleepersGoal;
import net.keys.sleepers.entity.ai.goal.ListenForDisturbanceGoal;
import net.keys.sleepers.entity.ai.goal.SleepGoal;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.event.listener.VibrationListener;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.function.BiConsumer;

public class SleeperPrototypeEntity extends HostileEntity implements IAnimatable, VibrationListener.Callback {
    private static final Logger LOGGER = LogUtils.getLogger();

    // entity properties
    public static final int DISTURBANCE_THRESHOLD = 120;

    private boolean asleep = true;
    private boolean listening = false;

    private int disturbanceLevel = 0;

    private static final String ASLEEP_KEY = "asleep";
    private static final String LISTENING_KEY = "listening";
    private static final String DISTURBANCE_LEVEL_KEY = "disturbance_level";

    // animations
    private final AnimationFactory animationFactory = new AnimationFactory(this);

    // handling vibrations
    private final EntityGameEventHandler<VibrationListener> gameEventHandler = new EntityGameEventHandler<>(new VibrationListener(
       new EntityPositionSource(this, this.getStandingEyeHeight()),
       16, this, null, 0f, 0
    ));

    public SleeperPrototypeEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createSleeperPrototypeAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.34f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3f)
                .add(EntityAttributes.GENERIC_ARMOR, 2f);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        this.writeEntityPropertiesToNbt(nbt);

        VibrationListener.createCodec(this)
                .encodeStart(NbtOps.INSTANCE, this.gameEventHandler.getListener())
                .resultOrPartial(LOGGER::error)
                .ifPresent(nbtElement -> nbt.put("listener", nbtElement));

        super.writeCustomDataToNbt(nbt);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.readEntityPropertiesFromNbt(nbt);

        if (nbt.contains("listener", NbtElement.COMPOUND_TYPE))
            VibrationListener.createCodec(this)
                    .parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("listener")))
                    .resultOrPartial(LOGGER::error)
                    .ifPresent(vibrationListener -> this.gameEventHandler.setListener(vibrationListener, this.world));
        super.readCustomDataFromNbt(nbt);
    }
    @Override
    public void tick() {
        super.tick();

        if (this.world instanceof ServerWorld serverWorld)
            this.gameEventHandler.getListener().tick(serverWorld);
    }

    @Override
    public void sendMessage(Text message) {
        MinecraftClient client = MinecraftClient.getInstance();

        client.inGameHud.getChatHud().addMessage(message);
    }

    // goals
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new ListenForDisturbanceGoal(this));
        this.goalSelector.add(2, new SleepGoal(this));
        this.goalSelector.add(3, new AlertNearbySleepersGoal(this));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1d, false));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.25d));
        this.goalSelector.add(6, new LookAroundGoal(this));

        this.targetSelector.add(0, new RevengeGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, false));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));

        super.initGoals();
    }

    // entity properties
    private void writeEntityPropertiesToNbt(NbtCompound nbt) {
        nbt.putBoolean(ASLEEP_KEY, this.asleep);
        nbt.putBoolean(LISTENING_KEY, this.listening);
        nbt.putInt(DISTURBANCE_LEVEL_KEY, this.disturbanceLevel);
    }
    private void readEntityPropertiesFromNbt(NbtCompound nbt) {
        if (nbt.contains(ASLEEP_KEY))
            this.asleep = nbt.getBoolean(ASLEEP_KEY);
        if (nbt.contains(LISTENING_KEY))
            this.listening = nbt.getBoolean(LISTENING_KEY);
        if (nbt.contains(DISTURBANCE_LEVEL_KEY))
            this.disturbanceLevel = nbt.getInt(DISTURBANCE_LEVEL_KEY);
    }

    public boolean isAsleep() {
        return this.asleep;
    }
    public void awaken() {
        if (!this.asleep)
            return;

        this.asleep = false;
        this.listening = false;
    }
    public boolean isListening() {
        return this.listening;
    }
    public void stopListening() {
        if (this.listening)
            this.listening = false;
    }

    public int getDisturbanceLevel() {
        return disturbanceLevel;
    }
    public void increaseDisturbanceLevel(int amount) {
        this.disturbanceLevel += amount;
    }

    // animations
    private <E extends IAnimatable>PlayState animationPredicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "sleeperPrototypeController", 0, this::animationPredicate));
    }
    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    // handling vibrations
    @Override
    public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
        super.updateEventHandler(callback);

        if (this.world instanceof ServerWorld serverWorld)
            callback.accept(this.gameEventHandler, serverWorld);
    }
    @Override
    public boolean accepts(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, GameEvent.Emitter emitter) {
        if (this.world != world || this.isDead() || this.isRemoved() || this.isAiDisabled())
            return false;

        return this.isAsleep() && emitter.sourceEntity() instanceof LivingEntity;
    }
    @Override
    public void accept(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity, @Nullable Entity sourceEntity, float distance) {
        if (!this.listening) {
            this.listening = true;
        } else {
            this.increaseDisturbanceLevel(10 + this.random.nextInt(10));
        }
    }
}
