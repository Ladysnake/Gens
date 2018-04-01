package ladysnake.gens.entity;

import ladysnake.gens.init.ModItems;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HarTradeList implements EntityVillager.ITradeList {
    private static final List<Pair<ItemStack, EntityVillager.PriceInfo>> canBuy = new ArrayList<>();
    private static final List<Pair<ItemStack, EntityVillager.PriceInfo>> canSell = new ArrayList<>();

    public static void initTrades() {
        addBuyItem(Items.IRON_INGOT, 6, 8);
        addBuyItem(Items.GOLD_NUGGET, 10, 16);
        addBuyItem(Items.EMERALD, 2, 6);
        addItem(canBuy, new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getMetadata()), 16, 18);
        addBuyItem(Items.DIAMOND, 1, 1);
        addBuyItem(Items.LAVA_BUCKET, 1, 1);
        addBuyItem(Items.WATER_BUCKET, 1, 1);
        addBuyItem(Items.IRON_SWORD, 1, 1);
        addBuyItem(Items.MUTTON, 6, 8);
        addBuyItem(Items.BEEF, 4, 8);
        addBuyItem(Items.STICK, 32, 48);
        addItem(canBuy, new ItemStack(Blocks.LOG), 16, 20);
        addBuyItem(Items.PAPER, 20, 30);
        addItem(canBuy, new ItemStack(Blocks.WOOL), 18, 26);
        addBuyItem(Items.LEATHER, 6, 8);
        addSellItem(ModItems.SCIMITAR, 1, 1);
        addSellItem(Items.ARROW, 18, 26);
        addSellItem(ModItems.HAR_SMA, 4, 6);
        addSellItem(ModItems.CACTUS_STEW, 1, 1);
        addSellItem(ModItems.SAND_WORM, 8, 16);
        addItem(canSell, new ItemStack(Blocks.SAND), 26, 40);
    }

    private static void addBuyItem(Item item, int min, int max) {
        addItem(canBuy, new ItemStack(item), min, max);
    }

    private static void addSellItem(Item item, int min, int max) {
        addItem(canSell, new ItemStack(item), min, max);
    }

    private static void addItem(List<Pair<ItemStack, EntityVillager.PriceInfo>> list, ItemStack stack, int min, int max) {
        list.add(Pair.of(stack, new EntityVillager.PriceInfo(min, max)));
    }

    @Override
    public void addMerchantRecipe(@Nonnull IMerchant merchant, @Nonnull MerchantRecipeList recipeList, @Nonnull Random random) {
        for (int i = 0; i < 3 + random.nextInt(2); i++) {
            ItemStack buy1 = getTradeItemStack(canBuy, random);
            ItemStack buy2 = ItemStack.EMPTY;
            if (buy1.getCount() > 1 && random.nextInt(3) == 0) {
                buy2 = getTradeItemStack(canBuy, random);
                buy1.setCount(buy1.getCount() / 2);
                buy2.setCount(buy2.getCount() / 2);
            }
            ItemStack sell = getTradeItemStack(canSell, random);
            MerchantRecipe recipe = new MerchantRecipe(buy1, buy2, sell);
            recipeList.add(recipe);
        }
    }

    private static ItemStack getTradeItemStack(List<Pair<ItemStack, EntityVillager.PriceInfo>> itemList, Random random) {
        Pair<ItemStack, EntityVillager.PriceInfo> entry = itemList.get(random.nextInt(itemList.size()));
        ItemStack stack = entry.getKey().copy();
        stack.setCount(entry.getValue().getPrice(random));
        return stack;
    }
}
