package ladysnake.gens.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityGensMerchant extends EntityGensVillager implements IMerchant {
    private EntityPlayer customer;
    private MerchantRecipeList buyingList;

    public EntityGensMerchant(World worldIn) {
        super(worldIn);
    }

    @Override
    public boolean processInteract(EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = itemstack.getItem() == Items.NAME_TAG;

        if (flag) {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        } else if (!this.holdingSpawnEggOfClass(itemstack, this.getClass()) && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking()) {
            if (this.buyingList == null) {
                this.populateBuyingList();
            }

            if (hand == EnumHand.MAIN_HAND) {
                player.addStat(StatList.TALKED_TO_VILLAGER);
            }

            if (!this.world.isRemote && !this.buyingList.isEmpty()) {
                this.setCustomer(player);
                player.displayVillagerTradeGui(this);
            } else if (this.buyingList.isEmpty()) {
                return super.processInteract(player, hand);
            }
            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    private void populateBuyingList() {
        this.buyingList = new MerchantRecipeList();
        for (EntityVillager.ITradeList tradeList : this.profession.getTrades()) {
            tradeList.addMerchantRecipe(this, buyingList, rand);
        }
    }

    private boolean isTrading() {
        return this.customer != null;
    }

    @Nullable
    @Override
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) {
        EntityGensMerchant desertPerson = new EntityGensMerchant(this.world);
        desertPerson.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(desertPerson)), null);
        return desertPerson;
    }

    @Override
    public void setCustomer(@Nullable EntityPlayer player) {
        this.customer = player;
    }

    @Nullable
    @Override
    public EntityPlayer getCustomer() {
        return customer;
    }

    @Nullable
    @Override
    public MerchantRecipeList getRecipes(@Nonnull EntityPlayer player) {
        return this.buyingList;
    }

    @Override
    public void setRecipes(@Nullable MerchantRecipeList recipeList) {

    }

    @Override
    public void useRecipe(@Nonnull MerchantRecipe recipe) {

    }

    @Override
    public void verifySellingItem(@Nonnull ItemStack stack) {

    }

    @Nonnull
    @Override
    public World getWorld() {
        return world;
    }

    @Nonnull
    @Override
    public BlockPos getPos() {
        return new BlockPos(this);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag("tradeList", this.buyingList.getRecipiesAsTags());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("tradeList")) {
            this.buyingList = new MerchantRecipeList(compound.getCompoundTag("tradeList"));
        }
    }
}
