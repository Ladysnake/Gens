package ladysnake.gens.entity.ai;

import ladysnake.gens.entity.EntityGensMerchant;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityGensAITradePlayer extends EntityAIBase {

    private final EntityGensMerchant villager;

    public EntityGensAITradePlayer(EntityGensMerchant villagerIn) {
        this.villager = villagerIn;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (!this.villager.isEntityAlive()) {
            return false;
        } else if (this.villager.isInWater()) {
            return false;
        } else if (!this.villager.onGround) {
            return false;
        } else if (this.villager.velocityChanged) {
            return false;
        } else {
            EntityPlayer entityplayer = this.villager.getCustomer();

            return entityplayer != null && !(this.villager.getDistanceSq(entityplayer) > 16.0D) && entityplayer.openContainer != null;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.villager.getNavigator().clearPath();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.villager.setCustomer(null);
    }
}
