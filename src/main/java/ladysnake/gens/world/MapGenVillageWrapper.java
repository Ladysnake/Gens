package ladysnake.gens.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenVillage;

import java.util.Random;

public class MapGenVillageWrapper extends MapGenVillage {
    private final MapGenVillage original;
    private final MapGenGens gens;

    public MapGenVillageWrapper(MapGenVillage original) {
        this.original = original;
        size = original.size;
        distance = original.distance;
        gens = new MapGenGens(distance, size);
    }

    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer primer) {
        original.generate(worldIn, x, z, primer);
        gens.generate(worldIn, x, z, primer);
    }

    @Override
    public synchronized boolean generateStructure(World worldIn, Random randomIn, ChunkPos chunkCoord) {
        return original.generateStructure(worldIn, randomIn, chunkCoord) | gens.generateStructure(worldIn, randomIn, chunkCoord);
    }

    @Override
    public boolean isInsideStructure(BlockPos pos) {
        return original.isInsideStructure(pos) || gens.isInsideStructure(pos);
    }

    @Override
    public boolean isPositionInStructure(World worldIn, BlockPos pos) {
        return original.isPositionInStructure(worldIn, pos) || gens.isPositionInStructure(worldIn, pos);
    }

    @Override
    public String getStructureName() {
        return original.getStructureName();
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
        BlockPos originalNearest = original.getNearestStructurePos(worldIn, pos, findUnexplored);
        BlockPos gensNearest = gens.getNearestStructurePos(worldIn, pos, findUnexplored);
        if (originalNearest == null) return gensNearest;
        if (gensNearest == null) return originalNearest;
        return pos.distanceSq(originalNearest) <= pos.distanceSq(gensNearest) ? originalNearest : gensNearest;
    }
}
