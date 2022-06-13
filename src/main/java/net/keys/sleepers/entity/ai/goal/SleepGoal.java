package net.keys.sleepers.entity.ai.goal;

import net.keys.sleepers.entity.sleeper.SleeperPrototypeEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.text.Text;

import java.util.EnumSet;

public class SleepGoal extends Goal {
    private final SleeperPrototypeEntity sleeper;

    public SleepGoal(SleeperPrototypeEntity sleeper) {
        this.sleeper = sleeper;

        this.setControls(EnumSet.of(Control.JUMP, Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return this.sleeper.isAsleep();
    }
    @Override
    public boolean shouldContinue() {
        return this.sleeper.isAsleep();
    }

    @Override
    public void start() {
        this.sleeper.getNavigation().stop();
    }
}
