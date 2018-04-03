package ladysnake.gens.world;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.*;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

public class MapGenGens extends MapGenStructure {
    public static List<Biome> HAR_SPAWN_BIOMES = Arrays.asList(Biomes.DESERT, Biomes.MESA);
    //public static List<Biome> HAR_SPAWN_BIOMES = ForgeRegistries.BIOMES.getValues(); //TODO: Limit this before release!

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
        return findNearestStructurePosBySpacing(worldIn, this, pos, gridUnit, gridUnit / 4, 0xFF65AD, false, 100, findUnexplored);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        //return (chunkX & 0b111) == 0 && (chunkZ & 0b111) == 0;

        int gridX = Math.floorDiv(chunkX, gridUnit);
        int gridZ = Math.floorDiv(chunkZ, gridUnit);
        Random random = world.setRandomSeed(gridX, gridZ, 0xFF65AD);
        int gridChunkX = gridX * gridUnit + random.nextInt(3 * gridUnit / 4);
        int gridChunkZ = gridZ * gridUnit + random.nextInt(3 * gridUnit / 4);

        return chunkX == gridChunkX && chunkZ == gridChunkZ && this.world.getBiomeProvider().areBiomesViable(chunkX * 16 + 8, chunkZ * 16 + 8, 16, HAR_SPAWN_BIOMES);

    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new Start(world, rand, chunkX, chunkZ, size);
    }

    public static class Start extends StructureStart {
        public Start() {}

        public Start(World world, Random rand, int x, int z, int size) {
            super(x, z);

            List<ComponentGensPieces.StructureType> plans = new ArrayList<>();
            plans.add(ComponentGensPieces.HAR_CAMPFIRE);
            if (rand.nextInt(2) == 0) {
                plans.add(ComponentGensPieces.HAR_FORGE);
            }
            for (int i = 1 + rand.nextInt(3); i >= 0; i--) {
                plans.add(ComponentGensPieces.HAR_DORM);
            }
            for (int i = 1 + rand.nextInt(2); i >= 0; i--) {
                plans.add(ComponentGensPieces.HAR_STORAGE);
            }

            Collections.shuffle(plans, rand);

            //TODO: Components go missing. Why?
            for (ComponentGensPieces.StructureType plan : plans) {
                StructureComponent component = null;
                if (components.isEmpty()) {
                    component = new ComponentGensPieces.Feature(rand, x * 16, 64, z * 16, plan);
                } else {
                    while (component == null) {
                        StructureComponent otherComponent = components.get(rand.nextInt(components.size()));
                        double r = 9.0D + 5.0D * rand.nextDouble();
                        double theta = rand.nextDouble() * Math.PI * 2.0D;

                        StructureBoundingBox box = otherComponent.getBoundingBox();
                        int candidateX = MathHelper.floor(box.minX + Math.sin(theta) * r);
                        int candidateY = MathHelper.floor(box.minZ + Math.cos(theta) * r);
                        StructureComponent candidateComponent = new ComponentGensPieces.Feature(rand, candidateX, 64, candidateY, plan);
                        if (components.stream().noneMatch(collidingComponent -> candidateComponent.getBoundingBox().intersectsWith(collidingComponent.getBoundingBox()))) {
                            component = candidateComponent;
                        }
                    }
                }
                components.add(component);
            }
            updateBoundingBox();
        }
    }
}
