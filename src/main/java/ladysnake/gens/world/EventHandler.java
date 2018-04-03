package ladysnake.gens.world;

import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
    @SubscribeEvent
    public void onInitMapGen(InitMapGenEvent event) {
        if (event.getType() == InitMapGenEvent.EventType.VILLAGE) {
            //TODO: Reduce the grid unit! 4 is ridiculous
            event.setNewGen(new MapGenVillageWrapper((MapGenVillage) event.getNewGen(), new MapGenGens(8, 0)));
        }
    }
}
