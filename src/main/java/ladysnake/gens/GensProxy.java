package ladysnake.gens;

import ladysnake.gens.init.ModEntities;
import ladysnake.gens.item.ItemSma;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.awt.*;
import java.util.Random;

public class GensProxy {
    @SidedProxy
    public static GensProxy proxy;

    public void preInit() {

    }

    public void postInit() {

    }

    @SuppressWarnings("unused")
    public static class ClientProxy extends GensProxy {
        @Override
        public void preInit() {
            super.preInit();
            ModEntities.registerRenders();
        }

        @Override
        public void postInit() {
            super.postInit();
            Random rand = new Random();
            for (int i = 1; i <= 4; i++) {
                Minecraft.getMinecraft().getItemColors().registerItemColorHandler((
                                (stack, tintIndex) -> tintIndex == 1 && stack.hasTagCompound() ? stack.getTagCompound().getInteger("gemColor") : Color.WHITE.getRGB()),
                        ForgeRegistries.ITEMS.getValue(new ResourceLocation(Gens.MOD_ID, "scimitar_" + (rand.nextInt(4) + 1)))
                );
            }
            for (ItemSma.Types types : ItemSma.Types.VALUES) {
                Minecraft.getMinecraft().getItemColors().registerItemColorHandler((
                                (stack, tintIndex) -> tintIndex == 1 ? ((ItemSma)stack.getItem()).getType().getColor() : Color.WHITE.getRGB()),
                        ForgeRegistries.ITEMS.getValue(new ResourceLocation(Gens.MOD_ID, "har_sma_" + types))
                );
            }
        }
    }

    @SuppressWarnings("unused")
    public static class ServerProxy extends GensProxy {

    }
}
