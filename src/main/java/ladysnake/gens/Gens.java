package ladysnake.gens;

import ladylib.LadyLib;
import ladylib.misc.TemplateUtil;
import ladysnake.gens.entity.HarTradeList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import ladysnake.gens.world.EventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Gens.MOD_ID, name = "Gens", version = "@VERSION@")
public class Gens {
    public static final String MOD_ID = "gens";
    public static LadyLib lib;
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        lib = LadyLib.initLib(event);
        lib.makeCreativeTab(() -> new ItemStack(Items.EMERALD));
        GensProxy.proxy.preInit();
        MinecraftForge.TERRAIN_GEN_BUS.register(new EventHandler());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // These 2 lines can be removed at any time without breaking anything
        TemplateUtil.generateStubModels(lib.getItemRegistrar(), "../src/main/resources");
        TemplateUtil.generateStubBlockstates(lib.getBlockRegistrar(), "../src/main/resources");
        HarTradeList.initTrades();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        GensProxy.proxy.postInit();
    }
}
