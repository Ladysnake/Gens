package ladysnake.gens.init;

import ladysnake.gens.Gens;
import ladysnake.gens.entity.GensEthnicity;
import ladysnake.gens.entity.HarTradeList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = Gens.MOD_ID)
public class ModEthnicities {
    @GameRegistry.ObjectHolder("gens:har")
    public static GensEthnicity HAR;

    @SubscribeEvent
    public static void onRegistryRegister(RegistryEvent.Register<GensEthnicity> event) {
        GensEthnicity har = new GensEthnicity("har");
        har.registerProfession("dealer",
                Pair.of("har_female_dealer", "dealer_turban"),
                Pair.of("har_male_dealer", "dealer_turban"))
                .addTrade(new HarTradeList());
        har.registerProfession("sentinel",
                Pair.of("har_female_sentinel", "sentinel_turban"),
                Pair.of("har_male_sentinel", "sentinel_turban"));
        har.registerProfession("member",
                Pair.of("har_female_member", "sentinel_turban"),
                Pair.of("har_male_member", "sentinel_turban"));
        event.getRegistry().register(har.setRegistryName("har"));
    }

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        GensEthnicity.REGISTRY = new RegistryBuilder<GensEthnicity>()
                .setType(GensEthnicity.class)
                .setName(new ResourceLocation(Gens.MOD_ID, "ethnicities"))
                .create();
    }
}
