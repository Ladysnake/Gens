package ladysnake.gens.init;

import ladylib.registration.ItemRegistrar;
import ladysnake.gens.Gens;
import ladysnake.gens.item.ItemFriedWorms;
import ladysnake.gens.item.ItemScimitar;
import ladysnake.gens.item.ItemSma;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSoup;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Locale;

import static ladylib.registration.ItemRegistrar.name;

@GameRegistry.ObjectHolder(Gens.MOD_ID)
@Mod.EventBusSubscriber(modid = Gens.MOD_ID)
public class ModItems {
    public static final Item HAR_SMA = Items.AIR;
    public static final Item CACTUS_STEW = Items.AIR;
    public static final Item SAND_WORM = Items.AIR;

    @SubscribeEvent
    public static void onRegistryRegister(RegistryEvent.Register<Item> event) {
        ItemRegistrar reg = Gens.lib.getItemRegistrar();
        for (int i = 1; i <= 4; i++) {
            reg.addItem(new ItemScimitar(Item.ToolMaterial.IRON).setRegistryName("scimitar_" + i).setUnlocalizedName("gens.scimitar"), true);
        }
        MinecraftForge.EVENT_BUS.register(ItemScimitar.class);
        for (ItemSma.Types type : ItemSma.Types.VALUES) {
            reg.addItem(name(new ItemSma(type), "har_sma_" + type.toString().toLowerCase(Locale.ENGLISH)), true);
        }
        reg.addItem(name(new ItemSoup(6), "cactus_stew"), true);
        reg.addItem(name(new ItemFriedWorms(4).setPotionEffect(new PotionEffect(MobEffects.NAUSEA, 100), 0.1F), "sand_worm"), true);
    }
}
