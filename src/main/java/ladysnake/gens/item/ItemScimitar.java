package ladysnake.gens.item;

import ladylib.misc.ItemUtil;
import ladysnake.gens.Gens;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Random;

public class ItemScimitar extends ItemSword {
    private static Random rand = new Random();

    public ItemScimitar(ToolMaterial material) {
        super(material);
    }

    public static ItemStack generateScimitar() {
        Item scimitar = ForgeRegistries.ITEMS.getValue(new ResourceLocation(Gens.MOD_ID, "scimitar_" + (rand.nextInt(4) + 1)));
        if (scimitar == null) return ItemStack.EMPTY;
        ItemStack ret = new ItemStack(scimitar);
        ItemUtil.getOrCreateCompound(ret).setInteger("gemColor", rand.nextInt(0xFF) << 4 | rand.nextInt(0xFF) << 2 | rand.nextInt(0xFF));
        return ret;
    }

    @Override
    public float getAttackDamage() {
        return super.getAttackDamage() + 2;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemScimitar) {
            if (event.getEntityPlayer().getDistance(event.getTarget()) > 2)
                event.setCanceled(true);
        }
    }

    @Mod.EventBusSubscriber(modid = Gens.MOD_ID, value = Side.CLIENT)
    public static class ScimitarClientHandler {
        @SubscribeEvent
        public static void onEntityViewRenderFogDensity(EntityViewRenderEvent.FogDensity event) {
            if (event.getEntity() instanceof EntityLivingBase) {
                EntityLivingBase renderEnt = ((EntityLivingBase) event.getEntity());
                if (renderEnt.getHeldItemMainhand().getItem() instanceof ItemScimitar) {
                    Minecraft mc = Minecraft.getMinecraft();
                    // prevent the player from targeting entities outside of the scimitar's range
                    if (mc.pointedEntity != null && renderEnt.getDistanceSq(mc.pointedEntity) > 4) {
                        Vec3d vec = Minecraft.getMinecraft().objectMouseOver.hitVec;
                        Minecraft.getMinecraft().objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec, null, new BlockPos(vec));
                        Minecraft.getMinecraft().pointedEntity = null;
                    }
                }
            }
        }
    }
}
