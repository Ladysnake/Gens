package ladysnake.gens.entity;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GensEthnicity {
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

    public class GensProfession {
        private List<EntityVillager.ITradeList> trades = new ArrayList<>();
        private ResourceLocation texture;

        public void addTrade(EntityVillager.ITradeList trade) {
            trades.add(trade);
        }

        public List<EntityVillager.ITradeList> getTrades() {
            return trades;
        }
    }
}
