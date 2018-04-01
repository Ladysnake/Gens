package ladysnake.gens.entity;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

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
