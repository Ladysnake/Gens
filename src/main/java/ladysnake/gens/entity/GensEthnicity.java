package ladysnake.gens.entity;

import ladysnake.gens.Gens;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.HashMap;
import java.util.Map;

public class GensEthnicity extends IForgeRegistryEntry.Impl<GensEthnicity> {
    public static IForgeRegistry<GensEthnicity> REGISTRY;

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY = new RegistryBuilder<GensEthnicity>()
                .setType(GensEthnicity.class)
                .setName(new ResourceLocation(Gens.MOD_ID, "ethnicities"))
                .create();
    }

    public static final GensEthnicity HAR;

    static {
        HAR = new GensEthnicity();
        HAR.registerProfession("dealer");
        HAR.getProfession("dealer").addTrade(new HarTradeList());
    }

    private Map<String, GensProfession> professions = new HashMap<>();

    public GensEthnicity() {

    }

    public void registerProfession(String professionName) {
        professions.put(professionName, new GensProfession());
    }

    public GensProfession getProfession(String professionName) {
        return professions.get(professionName);
    }

}
