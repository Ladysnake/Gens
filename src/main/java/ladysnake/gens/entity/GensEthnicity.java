package ladysnake.gens.entity;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class GensEthnicity extends IForgeRegistryEntry.Impl<GensEthnicity> {
    public static IForgeRegistry<GensEthnicity> REGISTRY;

    protected Map<String, GensProfession> professions = new HashMap<>();
    protected String name;

    public GensEthnicity(String name) {
        this.name = name;
    }

    @SafeVarargs
    public final GensProfession registerProfession(String professionName, Pair<String, String>... textures) {
        GensProfession profession = new GensProfession(this, professionName, textures);
        professions.put(professionName, profession);
        return profession;
    }

    public GensProfession getProfession(String professionName) {
        return professions.get(professionName);
    }

    public String getName() {
        return name;
    }
}
