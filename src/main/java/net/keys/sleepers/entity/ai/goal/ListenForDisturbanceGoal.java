package net.keys.sleepers.entity.ai.goal;

import net.keys.sleepers.entity.sleeper.SleeperPrototypeEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.text.Text;

import java.util.EnumSet;

public class ListenForDisturbanceGoal extends Goal {
    private final SleeperPrototypeEntity sleeper;

    private int listenTime;
    private int disturbanceLevel;

    public ListenForDisturbanceGoal(SleeperPrototypeEntity sleeper) {
        this.sleeper = sleeper;

        this.setControls(EnumSet.of(Control.JUMP, Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.sleeper.isAsleep() && this.sleeper.isListening();
    }
    @Override
    public boolean shouldContinue() {
        return this.listenTime > 0;
    }

    @Override
    public void start() {
        this.listenTime = this.getTickCount(this.getListenTimeByDifficulty());
        this.disturbanceLevel = this.sleeper.getDisturbanceLevel();
    }
    @Override
    public void stop() {
        this.sleeper.stopListening();
    }

    @Override
    public void tick() {
        if (this.disturbanceLevel != this.sleeper.getDisturbanceLevel()) {
            this.increaseListenTime();

            this.disturbanceLevel = this.sleeper.getDisturbanceLevel();
        }
        if (sleeper.getDisturbanceLevel() > SleeperPrototypeEntity.DISTURBANCE_THRESHOLD) {
            this.sleeper.awaken();
        }

        --this.listenTime;
    }

    private int getListenTimeByDifficulty() {
        return switch (this.sleeper.world.getDifficulty()) {
            case PEACEFUL -> 0;
            case EASY -> 62;
            case NORMAL -> 98;
            case HARD -> 184;
        };
    }
    private void increaseListenTime() {
        this.listenTime += 20 + this.sleeper.getRandom().nextInt(20);
    }
}
