package ladysnake.gens.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemFriedWorms extends ItemFood {
    public ItemFriedWorms(int amount) {
        super(amount, true);
    }

    @Nonnull
    public ItemStack onItemUseFinish(ItemStack stack, @Nonnull World worldIn, EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        return new ItemStack(Items.BOWL);
    }
}
