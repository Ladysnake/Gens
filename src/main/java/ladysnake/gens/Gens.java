package ladysnake.gens;

import ladylib.LadyLib;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
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
}
