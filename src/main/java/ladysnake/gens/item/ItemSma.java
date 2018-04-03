package ladysnake.gens.item;

import ladylib.client.ICustomLocation;
import ladylib.misc.ItemUtil;
import ladysnake.gens.Gens;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

@Mod.EventBusSubscriber(modid = Gens.MOD_ID)
public class ItemSma extends Item implements ICustomLocation {

    public enum Types {
        SMA(MobEffects.POISON, 1), ZIL(MobEffects.WITHER, 0), WAQ(MobEffects.SLOWNESS, 1), JUE(MobEffects.HUNGER, 1);
        public static final Types[] VALUES = values();
        private final int color;
        private final Potion potion;
        private final int amplifier;

        Types(Potion potion, int amplifier) {
            this.color = potion.getLiquidColor();
            this.potion = potion;
            this.amplifier = amplifier;
        }

        public int getColor() {
            return color;
        }

        public void applyEffect(EntityLivingBase entityIn, int potencyModifier) {
            entityIn.addPotionEffect(new PotionEffect(potion, 200, (amplifier + 1) * potencyModifier - 1));
        }
    }

    public static final String NBT_SMA = "gens:sma";
    private static final Random rand = new Random();

    private Types type;

    public ItemSma(Types type) {
        this.type = type;
        this.setContainerItem(Items.GLASS_BOTTLE);
    }

    @SubscribeEvent
    public static void onPlayerInteractRightClickItem(PlayerInteractEvent.RightClickBlock event) {
        if (getSmaType(event.getItemStack()).isPresent()) {
            IBlockState block = event.getWorld().getBlockState(event.getPos());
            if (block.getBlock() instanceof BlockCauldron) {
                int level = block.getValue(BlockCauldron.LEVEL);
                if (level > 0) {
                    setSmaType(event.getItemStack(), null);
                    ((BlockCauldron) block.getBlock()).setWaterLevel(event.getWorld(), event.getPos(), block, level - 1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
        NBTTagCompound smaCompound = stack.getSubCompound(NBT_SMA);
        Types smaType = getSmaType(smaCompound);
        if (smaType != null && event.getTarget() instanceof EntityLivingBase) {
            smaType.applyEffect((EntityLivingBase) event.getTarget(), stack.getItem() instanceof ItemScimitar ? 2 : 1);
            if (!event.getEntityPlayer().isCreative())
                smaCompound.setInteger("uses", smaCompound.getInteger("uses") - 1);
        }
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        getType().applyEffect(entityLiving, 2);
        return new ItemStack(Items.GLASS_BOTTLE);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    @Nonnull
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    public static ItemStack getRandomSmaBottle() {
        Item smaBottle = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Gens.MOD_ID, "har_sma_" + Types.VALUES[rand.nextInt(Types.VALUES.length)]));
        if (smaBottle == null) return ItemStack.EMPTY;
        return new ItemStack(smaBottle);
    }

    public Types getType() {
        return type;
    }

    public static Optional<Types> getSmaType(ItemStack stack) {
        NBTTagCompound compound = stack.getSubCompound(NBT_SMA);
        return Optional.ofNullable(getSmaType(compound));
    }

    @Nullable
    private static Types getSmaType(NBTTagCompound smaNBT) {
        if (smaNBT != null) {
            try {
                return Types.valueOf(smaNBT.getString("type"));
            } catch (IllegalArgumentException e) {
                Gens.LOGGER.error("Unrecognized sma type", e);
            }
        }
        return null;
    }

    public static void setSmaType(ItemStack stack, Types smaType) {
        if (smaType == null && stack.hasTagCompound()) {
            stack.getTagCompound().removeTag(NBT_SMA);
        } else {
            NBTTagCompound compound = ItemUtil.getOrCreateCompound(stack);
            NBTTagCompound smaNBT = new NBTTagCompound();
            smaNBT.setString("type", smaType.name());
            smaNBT.setInteger("uses", 30);
            compound.setTag(NBT_SMA, smaNBT);
        }
    }

    @Override
    public ModelResourceLocation getModelLocation() {
        return new ModelResourceLocation("gens:har_sma");
    }

    @Mod.EventBusSubscriber(modid = Gens.MOD_ID, value = Side.CLIENT)
    public static class SmaClientHandler {
        @SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            ItemStack stack = event.getItemStack();
            NBTTagCompound smaCompound = stack.getSubCompound(NBT_SMA);
            Types smaType = getSmaType(smaCompound);
            if (smaType != null) {
                event.getToolTip().add(I18n.format("gens.tooltip.sma", smaType, smaCompound.getInteger("uses")));
            }
        }
    }
}
