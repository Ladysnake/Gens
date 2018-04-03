package ladysnake.gens.entity;

import ladysnake.gens.init.ModEthnicities;
import ladysnake.gens.item.ItemScimitar;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityGensSoldier extends EntityGensVillager {
    public EntityGensSoldier(World worldIn) {
        super(worldIn, ModEthnicities.HAR.getProfession("sentinel"));
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
        this.setHeldItem(EnumHand.MAIN_HAND, ItemScimitar.generateScimitar());
        return livingData;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) {
        EntityGensSoldier desertPerson = new EntityGensSoldier(this.world);
        desertPerson.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(desertPerson)), null);
        return desertPerson;
    }
}
