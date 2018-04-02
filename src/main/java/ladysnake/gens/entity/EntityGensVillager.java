package ladysnake.gens.entity;

import ladysnake.gens.init.ModEthnicities;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public abstract class EntityGensVillager extends EntityAgeable implements INpc {
    protected GensProfession profession;
    protected int skin;

    @SuppressWarnings("unused")
    public EntityGensVillager(World worldIn) {
        this(worldIn, ModEthnicities.HAR.getProfession("dealer"));
    }

    public EntityGensVillager(World worldIn, GensProfession profession) {
        super(worldIn);
        this.profession = profession;
        this.skin = rand.nextInt(profession.getTextures().length);
        this.setSize(0.6F, 1.95F);
    }

    public ResourceLocation getTexture() {
        ResourceLocation[] professionTextures = profession.getTextures();
        if (skin > professionTextures.length)
            skin = 0;
        return professionTextures[skin];
    }

    public GensProfession getProfession() {
        return profession;
    }

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
//        this.tasks.addTask(1, new EntityAITradePlayer(this));
//        this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
        this.tasks.addTask(2, new EntityAIMoveIndoors(this));
        this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
//        this.tasks.addTask(6, new EntityAIVillagerMate(this));
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
//        this.tasks.addTask(9, new EntityAIVillagerInteract(this));
        this.tasks.addTask(9, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
    }

    @Override
    public boolean attackEntityAsMob(@Nonnull Entity entityIn) {
        float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag) {
            if (i > 0) {
                ((EntityLivingBase) entityIn).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0) {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) entityIn;
                ItemStack heldItem = this.getHeldItemMainhand();
                ItemStack playerHeldItem = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!heldItem.isEmpty() && !playerHeldItem.isEmpty() && heldItem.getItem().canDisableShield(heldItem, playerHeldItem, entityplayer, this) && playerHeldItem.getItem().isShield(playerHeldItem, entityplayer)) {
                    float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1) {
                        entityplayer.getCooldownTracker().setCooldown(playerHeldItem.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte) 30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("skin", skin);
        compound.setString("ethnicity", profession.getParent().getRegistryName().toString());
        compound.setString("profession", profession.getName());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        skin = compound.getInteger("skin");
        GensEthnicity ethnicity = GensEthnicity.REGISTRY.getValue(new ResourceLocation(compound.getString("ethnicity")));
        if (ethnicity != null)
            this.profession = ethnicity.getProfession(compound.getString("profession"));
    }
}
