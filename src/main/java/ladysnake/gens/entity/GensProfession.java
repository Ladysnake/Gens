package ladysnake.gens.entity;

import ladysnake.gens.Gens;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class GensProfession {
    protected List<EntityVillager.ITradeList> trades = new ArrayList<>();
    protected final GensEthnicity parent;
    protected final String name;
    protected ResourceLocation[] textures;
    protected ResourceLocation[] texturesLayer;

    @SafeVarargs
    public GensProfession(GensEthnicity parent, String name, Pair<String, String>... textures) {
        this.parent = parent;
        this.name = name;
        this.textures = new ResourceLocation[textures.length];
        this.texturesLayer = new ResourceLocation[textures.length];
        String prefix = "textures/entity/"  + parent.getName() + "/";
        for (int i = 0; i < textures.length; i++) {
            this.textures[i] = new ResourceLocation(Gens.MOD_ID, prefix + textures[i].getKey() + ".png");
            this.texturesLayer[i] = new ResourceLocation(Gens.MOD_ID, prefix + textures[i].getKey() + ".png");
        }
    }

    public GensEthnicity getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void addTrade(EntityVillager.ITradeList trade) {
        trades.add(trade);
    }

    public List<EntityVillager.ITradeList> getTrades() {
        return trades;
    }

    public ResourceLocation[] getTextures() {
        return textures;
    }

    public ResourceLocation[] getClothesTextures() {
        return texturesLayer;
    }
}
