package ladysnake.gens;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ItemScimitar extends ItemSword {
    public ItemScimitar(ToolMaterial material) {
        super(material);
    }

    @Override
    public float getAttackDamage() {
        return super.getAttackDamage() + 2;
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.getEntityPlayer().getHeldItemMainhand().getItem() == this) {
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
