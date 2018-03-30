package ladysnake.gens.world;

import net.minecraft.world.gen.structure.MapGenVillage;

public class MapGenGensVillage extends MapGenVillage {
    public MapGenGensVillage(MapGenVillage original) {
        this.size = original.size;
        this.distance = original.distance;
    }
}
