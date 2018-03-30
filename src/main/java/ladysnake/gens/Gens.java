package ladysnake.gens;

import ladylib.LadyLib;
import ladylib.misc.TemplateUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Gens.MOD_ID, name = "Gens", version = "@VERSION@")
public class Gens {
    public static final String MOD_ID = "gens";
    public static LadyLib lib;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        lib = LadyLib.initLib(event);
        lib.makeCreativeTab(() -> new ItemStack(Items.EMERALD));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // These 2 lines can be removed at any time without breaking anything
        TemplateUtil.generateStubModels(lib.getItemRegistrar(), "../src/main/resources");
        TemplateUtil.generateStubBlockstates(lib.getBlockRegistrar(), "../src/main/resources");
    }
}
