package ladysnake.gens.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class MapGenGens extends MapGenStructure {
    //public static List<Biome> HAR_SPAWN_BIOMES = Arrays.asList(Biomes.DESERT, Biomes.DESERT_HILLS);
    public static List<Biome> HAR_SPAWN_BIOMES = ForgeRegistries.BIOMES.getValues(); //TODO: Limit this before release!

    static {
        registerStructures();
        ComponentGensPieces.registerStructureComponents();
    }

    public static void registerStructures() {
        MapGenStructureIO.registerStructure(Start.class, "gens:Village");
    }

    private final int gridUnit;
    private final int size;

    public MapGenGens(int gridUnit, int size) {
        this.gridUnit = gridUnit;
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
        return findNearestStructurePosBySpacing(worldIn, this, pos, gridUnit, 8, 0xFF65AD, false, 100, findUnexplored);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        //return (chunkX & 0b111) == 0 && (chunkZ & 0b111) == 0;

        int gridX = Math.floorDiv(chunkX, gridUnit);
        int gridZ = Math.floorDiv(chunkZ, gridUnit);
        Random random = world.setRandomSeed(gridX, gridZ, 0xFF65AD);
        int gridChunkX = gridX * gridUnit + random.nextInt(3 * gridUnit / 4);
        int gridChunkZ = gridZ * gridUnit + random.nextInt(3 * gridUnit / 4);

        return chunkX == gridChunkX && chunkZ == gridChunkZ && this.world.getBiomeProvider().areBiomesViable(chunkX * 16 + 8, chunkZ * 16 + 8, 0, HAR_SPAWN_BIOMES);

    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        //return new MapGenNetherBridge.Start(world, rand, chunkX, chunkZ);
        return new Start(world, rand, chunkX, chunkZ, size);
    }

    public static class Start extends StructureStart {
        public Start() {}

        public Start(World world, Random rand, int x, int z, int size) {
            super(x, z);
            ComponentGensPieces.Feature component = new ComponentGensPieces.Feature(rand, x * 16, 64, z * 16, ComponentGensPieces.HAR_FORGE);
            components.add(component);
            updateBoundingBox();
        }
    }
}
