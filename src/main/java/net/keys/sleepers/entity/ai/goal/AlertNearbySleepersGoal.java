package net.keys.sleepers.entity.ai.goal;

import net.keys.sleepers.entity.sleeper.SleeperPrototypeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.Box;

import java.util.List;

public class AlertNearbySleepersGoal extends Goal {

    private final SleeperPrototypeEntity sleeper;

    public AlertNearbySleepersGoal(SleeperPrototypeEntity sleeper) {
        this.sleeper = sleeper;
    }

    @Override
    public boolean canStart() {
        return !this.sleeper.isAsleep();
    }

    @Override
    public void start() {
        this.getOthersInRange()
                .forEach(SleeperPrototypeEntity::awaken);
    }

    private List<? extends SleeperPrototypeEntity> getOthersInRange() {
        double range = this.sleeper.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
        Box box = Box.from(this.sleeper.getPos()).expand(range, 10.0, range);

        return this.sleeper.world.getEntitiesByClass(SleeperPrototypeEntity.class, box, EntityPredicates.EXCEPT_SPECTATOR);
    }
}
