package ladysnake.gens.world;

import ladysnake.gens.entity.*;
import ladysnake.gens.init.ModEthnicities;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.*;

public class ComponentGensPieces {
    private static final Map<ResourceLocation, StructureType> subTypeRegistry = new HashMap<>();

    public static final StructureType TEST = new StructureType(new ResourceLocation("gens", "test/test"), 8, 8, 8, 0, Collections.emptyList(), 0, 0);
    public static final StructureType HAR_CAMPFIRE = new StructureType(new ResourceLocation("gens", "har/har_campfire"), 9, 9, 9, 0, Arrays.asList(
        new BlockPos(2, 1, 2), new BlockPos(6, 1, 2), new BlockPos(2, 1, 6), new BlockPos(6, 1, 6)
    ), 0, 1);
    public static final StructureType HAR_DORM = new StructureType(new ResourceLocation("gens", "har/har_dorm"), 9, 9, 9, 0, Arrays.asList(
        new BlockPos(3, 1, 3), new BlockPos(5, 1, 3), new BlockPos(3, 1, 5), new BlockPos(5, 1, 5)
    ), 1, 3);
    public static final StructureType HAR_FORGE = new StructureType(new ResourceLocation("gens", "har/har_forge"), 9, 9, 9, 0, Arrays.asList(
        new BlockPos(3, 1, 3), new BlockPos(5, 1, 3), new BlockPos(3, 1, 5), new BlockPos(5, 1, 5)
    ), 1, 2);
    public static final StructureType HAR_STORAGE = new StructureType(new ResourceLocation("gens", "har/har_storage"), 9, 9, 9, 0, Arrays.asList(
        new BlockPos(3, 1, 3), new BlockPos(5, 1, 3), new BlockPos(3, 1, 5), new BlockPos(5, 1, 5)
    ), 0, 2);
    public static final StructureType HAR_STASH = new StructureType(new ResourceLocation("gens", "har/har_stash"), 3, 6, 3, -2, Collections.emptyList(), 0, 0);
    public static final StructureType HAR_GUARD_TOWER = new StructureType(new ResourceLocation("gens", "har/har_guard_tower"), 13, 7, 13, 0, Arrays.asList(
        new BlockPos(3, 6, 3), new BlockPos(9, 6, 3), new BlockPos(3, 6, 9), new BlockPos(9, 6, 9)
    ), 0, 2);
    public static final StructureType HAR_RABBIT_PEN = new StructureType(new ResourceLocation("gens", "har/har_rabbit_pen"), 13, 5, 13, -3, Collections.emptyList(), 0, 0);

    private static void registerStructureType(StructureType structureType) {
        subTypeRegistry.put(structureType.id, structureType);
    }

    public static void registerStructureComponents() {
        MapGenStructureIO.registerStructureComponent(Feature.class, "gens:feature");
        registerStructureType(TEST);
        registerStructureType(HAR_CAMPFIRE);
        registerStructureType(HAR_DORM);
        registerStructureType(HAR_FORGE);
        registerStructureType(HAR_STORAGE);
        registerStructureType(HAR_STASH);
        registerStructureType(HAR_GUARD_TOWER);
        registerStructureType(HAR_RABBIT_PEN);
    }

    public static class StructureType {
        private final ResourceLocation id;
        private final int sizeX;
        private final int sizeY;
        private final int sizeZ;
        private final int offsetY;
        private final List<BlockPos> spawnPositions;
        private final int minVillagers;
        private final int maxVillagers;

        private StructureType(ResourceLocation id, int sizeX, int sizeY, int sizeZ, int offsetY, List<BlockPos> spawnPositions, int minVillagers, int maxVillagers) {
            this.id = id;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.sizeZ = sizeZ;
            this.offsetY = offsetY;
            this.spawnPositions = spawnPositions;
            this.minVillagers = minVillagers;
            this.maxVillagers = maxVillagers;
        }
    }

    public static class Feature extends StructureComponent {
        protected int horizontalPos = -1;
        protected StructureType structureType;


        public Feature() {}

        public Feature(Random rand, int x, int y, int z, StructureType structureType) {
            super(0);
            this.structureType = structureType;
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));

            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(x, y, z, x + structureType.sizeX - 1, y + structureType.sizeY - 1, z + structureType.sizeZ - 1);
            } else {
                this.boundingBox = new StructureBoundingBox(x, y, z, x + structureType.sizeZ - 1, y + structureType.sizeY - 1, z + structureType.sizeX - 1);
            }
        }

        /**
         * Calculates and offsets this structure boundingbox to average ground level
         */
        protected boolean offsetToAverageGroundLevel(World worldIn, StructureBoundingBox structurebb, int yOffset) {
            if (this.horizontalPos >= 0) {
                return true;
            } else {
                int i = 0;
                int j = 0;
                BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

                for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k) {
                    for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l) {
                        pos.setPos(l, 64, k);

                        if (structurebb.isVecInside(pos)) {
                            i += Math.max(worldIn.getTopSolidOrLiquidBlock(pos).getY(), worldIn.provider.getAverageGroundLevel());
                            ++j;
                        }
                    }
                }

                if (j == 0) {
                    return false;
                } else {
                    this.horizontalPos = i / j;
                    this.boundingBox.offset(0, this.horizontalPos - this.boundingBox.minY + yOffset + structureType.offsetY, 0);
                    return true;
                }
            }
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound tagCompound) {
            tagCompound.setInteger("HPos", this.horizontalPos);
            if (structureType != null) {
                tagCompound.setString("StructureType", structureType.id.toString());
            }
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            this.horizontalPos = tagCompound.getInteger("HPos");
            this.structureType = subTypeRegistry.get(new ResourceLocation(tagCompound.getString("StructureType")));
        }

        @Override
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            if (structureType == null) return false; // Not sure what would cause this to be null at the moment.
            if (!this.offsetToAverageGroundLevel(worldIn, structureBoundingBoxIn, -1)) {
                return false;
            } else {
                StructureBoundingBox box = this.getBoundingBox();
                BlockPos middle = new BlockPos((box.minX + box.maxX) / 2, (box.minY + box.maxY) / 2, (box.minZ + box.maxZ) / 2);
                if (structureBoundingBoxIn.isVecInside(middle)) {
                    PlacementSettings placementsettings = new PlacementSettings().setRotation(rotation).setMirror(mirror).setReplacedBlock(Blocks.STRUCTURE_VOID).setBoundingBox(box);

                    BlockPos minPos = new BlockPos(box.minX, box.minY, box.minZ);
                    BlockPos zeroPos = Template.getZeroPositionWithTransform(minPos, mirror, rotation, box.getXSize(), box.getZSize());
                    MinecraftServer minecraftserver = worldIn.getMinecraftServer();
                    TemplateManager templatemanager = worldIn.getSaveHandler().getStructureTemplateManager();
                    Template template = templatemanager.getTemplate(minecraftserver, structureType.id);
                    template.addBlocksToWorldChunk(worldIn, zeroPos, placementsettings);

                    for (BlockPos.MutableBlockPos pos : BlockPos.getAllInBoxMutable(box.minX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ)) {
                        int initialY = pos.getY();
                        for (int y = box.minY - 1; y >= 0; y--) {
                            pos.setY(y);
                            if (worldIn.isBlockNormalCube(pos, true)) break;
                            worldIn.setBlockState(pos, Blocks.SAND.getDefaultState());
                        }
                        pos.setY(initialY);
                    }
                    /* worldIn.setBlockState(new BlockPos(box.minX, box.minY, box.minZ), Blocks.BEDROCK.getDefaultState());
                    worldIn.setBlockState(new BlockPos(box.maxX, box.minY, box.minZ), Blocks.BEDROCK.getDefaultState());
                    worldIn.setBlockState(new BlockPos(box.minX, box.maxY, box.minZ), Blocks.BEDROCK.getDefaultState());
                    worldIn.setBlockState(new BlockPos(box.maxX, box.maxY, box.minZ), Blocks.BEDROCK.getDefaultState());
                    worldIn.setBlockState(new BlockPos(box.minX, box.minY, box.maxZ), Blocks.BEDROCK.getDefaultState());
                    worldIn.setBlockState(new BlockPos(box.maxX, box.minY, box.maxZ), Blocks.BEDROCK.getDefaultState());
                    worldIn.setBlockState(new BlockPos(box.minX, box.maxY, box.maxZ), Blocks.BEDROCK.getDefaultState());
                    worldIn.setBlockState(new BlockPos(box.maxX, box.maxY, box.maxZ), Blocks.BEDROCK.getDefaultState()); */

                    spawnVillagers(worldIn, randomIn, structureType.minVillagers + randomIn.nextInt(1 + structureType.maxVillagers - structureType.minVillagers));
                    return true;
                }

                return false;
            }
        }

        protected void spawnVillagers(World worldIn, Random rand, int count)
        {
            for (int i = 0; i < count; ++i)
            {
                BlockPos pos = structureType.spawnPositions.get(rand.nextInt(structureType.spawnPositions.size()));
                int x = this.getXWithOffset(pos.getX(), pos.getZ());
                int y = this.getYWithOffset(pos.getY());
                int z = this.getZWithOffset(pos.getX(), pos.getZ());

                /*GensProfession[] professions = ModEthnicities.HAR.getProfessions().values().toArray(new GensProfession[0]);
                GensProfession profession = professions[rand.nextInt(professions.length)];
                EntityGensVillager villager;
                switch (profession.getName()) {
                    case "sentinel":
                        villager = new EntityGensSoldier(worldIn);
                        break;
                    case "dealer":
                        villager = new EntityGensMerchant(worldIn);
                } */
                EntityGensVillager villager;
                switch (rand.nextInt(3)) {
                    case 0:
                        villager = new EntityGensMerchant(worldIn, ModEthnicities.HAR.getProfession("dealer"));
                        break;
                    case 1:
                        villager = new EntityGensMerchant(worldIn, ModEthnicities.HAR.getProfession("member"));
                        break;
                    default:
                        villager = new EntityGensSoldier(worldIn);
                }

                villager.setLocationAndAngles(x + 0.5D, y, z + 0.5D, 0.0F, 0.0F);
                worldIn.spawnEntity(villager);
            }
        }
    }
}
