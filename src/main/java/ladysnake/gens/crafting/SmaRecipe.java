package ladysnake.gens.crafting;

import ladysnake.gens.item.ItemSma;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public class SmaRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        ItemStack tool = ItemStack.EMPTY;
        ItemStack sma = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemSma) {
                    if (!sma.isEmpty()) {
                        return false;
                    }
                    sma = stack;
                } else {
                    if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe) {
                        if (!tool.isEmpty()) {
                            return false;
                        }
                        tool = stack;
                    } else {
                        return false;
                    }
                }
            }
        }
        return (!tool.isEmpty() && !sma.isEmpty());
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack tool = ItemStack.EMPTY;
        ItemStack sma = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if(!stack.isEmpty()) {
                if(stack.getItem() instanceof ItemSma) {
                    sma = stack;
                } else {
                    if(stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe) {
                        tool = stack;
                    }
                }
            }
        }
        if(!tool.isEmpty() && !sma.isEmpty()) {
            ItemStack result = tool.copy();
            ItemSma.Types appliedSma = ((ItemSma)sma.getItem()).getType();
            ItemSma.setSmaType(result, appliedSma);
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < remaining.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            remaining.set(i, ForgeHooks.getContainerItem(itemstack));
        }

        return remaining;
    }
}
