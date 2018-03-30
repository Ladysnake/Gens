package ladysnake.gens.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.MapGenVillage;

import java.util.Random;

public class MapGenGensVillage extends MapGenVillage {
    private final MapGenVillage original;

    public MapGenGensVillage(MapGenVillage original) {
        this.original = original;
        this.size = original.size;
        this.distance = original.distance;
    }

    @Override
    public void generate(World worldIn, int x, int z, ChunkPrimer primer) {
        original.generate(worldIn, x, z, primer);
    }

    @Override
    public String getStructureName() {
        return original.getStructureName();
    }

    @Override
    public synchronized boolean generateStructure(World worldIn, Random randomIn, ChunkPos chunkCoord) {
        return original.generateStructure(worldIn, randomIn, chunkCoord);
    }

    @Override
    public boolean isInsideStructure(BlockPos pos) {
        return original.isInsideStructure(pos);
    }

    @Override
    public boolean isPositionInStructure(World worldIn, BlockPos pos) {
        return original.isPositionInStructure(worldIn, pos);
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
        return original.getNearestStructurePos(worldIn, pos, findUnexplored);
    }
}
