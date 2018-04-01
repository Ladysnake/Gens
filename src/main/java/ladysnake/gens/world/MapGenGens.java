package ladysnake.gens.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureStart;

import javax.annotation.Nullable;
import java.util.Random;

public class MapGenGens extends MapGenStructure {
    static {
        registerStructures();
        ComponentGensPieces.registerStructureComponents();
    }

    public static void registerStructures() {
        MapGenStructureIO.registerStructure(Start.class, "gens:Village");
    }

    private final int distance;
    private final int size;

    public MapGenGens(int distance, int size) {
        this.distance = distance;
        this.size = size;
    }

    @Override
    public String getStructureName() {
        return "Gens";
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, distance, 8, 0xFF65AD, false, 100, findUnexplored);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        return true;
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        //return new MapGenNetherBridge.Start(world, rand, chunkX, chunkZ);
        return new Start(world, rand, chunkX, chunkZ, size);
    }

    public static class Start extends StructureStart {
        public Start(World world, Random rand, int x, int z, int size) {
            super(x, z);
            ComponentGensPieces.Test component = new ComponentGensPieces.Test(rand, x * 16, z * 16);
            components.add(component);
            updateBoundingBox();
        }
    }
}
