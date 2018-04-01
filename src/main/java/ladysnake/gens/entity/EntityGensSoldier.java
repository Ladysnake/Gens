package ladysnake.gens.entity;

import ladysnake.gens.init.ModItems;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityGensSoldier extends EntityGensVillager {
    public EntityGensSoldier(World worldIn) {
        super(worldIn);
    }

    protected void applyEntityAI() {
        super.applyEntityAI();
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityZombie.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityEvoker.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityVindicator.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityVex.class, true));
    }

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingData) {
        super.onInitialSpawn(difficulty, livingData);
        this.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(ModItems.SCIMITAR));
        return livingData;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) {
        return null;
    }
}
