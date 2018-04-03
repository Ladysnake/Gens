package ladysnake.gens.entity.ai;

import ladysnake.gens.entity.EntityGensMerchant;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityGensAILookAtTradePlayer extends EntityAIWatchClosest {
    private final EntityGensMerchant villager;

    public EntityGensAILookAtTradePlayer(EntityGensMerchant villagerIn) {
        super(villagerIn, EntityPlayer.class, 8.0F);
        this.villager = villagerIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (this.villager.isTrading()) {
            this.closestEntity = this.villager.getCustomer();
            return true;
        } else {
            return false;
        }
    }
}
