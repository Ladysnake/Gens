package ladysnake.gens.init;

import ladysnake.gens.Gens;
import ladysnake.gens.crafting.SmaRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Gens.MOD_ID)
public class ModRecipes {

    @SubscribeEvent
    public static void onRegistryRegister(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new SmaRecipe().setRegistryName("sma"));
    }
}
