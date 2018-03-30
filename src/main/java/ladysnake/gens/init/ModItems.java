package ladysnake.gens.init;

import ladylib.registration.ItemRegistrar;
import ladysnake.gens.Gens;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static ladylib.registration.ItemRegistrar.name;

@GameRegistry.ObjectHolder(Gens.MOD_ID)
@Mod.EventBusSubscriber(modid = Gens.MOD_ID)
public class ModItems {

    @SubscribeEvent
    public static void onRegistryRegister(RegistryEvent.Register<Item> event) {
        ItemRegistrar reg = Gens.lib.getItemRegistrar();
        reg.addItem(name(new ItemSword(Item.ToolMaterial.IRON), "scimitar"), true);
    }
}
