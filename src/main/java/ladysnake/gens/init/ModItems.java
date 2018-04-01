package ladysnake.gens.init;

import ladylib.registration.ItemRegistrar;
import ladysnake.gens.Gens;
import ladysnake.gens.ItemScimitar;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSoup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static ladylib.registration.ItemRegistrar.name;

@GameRegistry.ObjectHolder(Gens.MOD_ID)
@Mod.EventBusSubscriber(modid = Gens.MOD_ID)
public class ModItems {
    public static final Item SCIMITAR = Items.AIR;
    public static final Item HAR_SMA = Items.AIR;
    public static final Item CACTUS_STEW = Items.AIR;
    public static final Item SAND_WORM = Items.AIR;

    @SubscribeEvent
    public static void onRegistryRegister(RegistryEvent.Register<Item> event) {
        ItemRegistrar reg = Gens.lib.getItemRegistrar();
        ItemScimitar scimitar = new ItemScimitar(Item.ToolMaterial.IRON);
        reg.addItem(name(scimitar, "scimitar"), true);
        MinecraftForge.EVENT_BUS.register(scimitar);
        reg.addItem(name(new Item(), "har_sma"), true);
        reg.addItem(name(new ItemSoup(4), "cactus_stew"), true);
        reg.addItem(name(new ItemFood(2, true), "sand_worm"), true);
    }
}
