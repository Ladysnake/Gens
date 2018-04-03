package ladysnake.gens.world;

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

    public static final StructureType TEST = new StructureType(new ResourceLocation("gens", "test/test"), 8, 8, 8);
    public static final StructureType HAR_CAMPFIRE = new StructureType(new ResourceLocation("gens", "har/har_campfire"), 9, 9, 9);
    public static final StructureType HAR_DORM = new StructureType(new ResourceLocation("gens", "har/har_dorm"), 9, 9, 9);
    public static final StructureType HAR_FORGE = new StructureType(new ResourceLocation("gens", "har/har_forge"), 9, 9, 9);
    public static final StructureType HAR_STORAGE = new StructureType(new ResourceLocation("gens", "har/har_storage"), 9, 9, 9);

    public static final List<StructureType> HAR_STRUCTURES = Arrays.asList(HAR_CAMPFIRE, HAR_DORM, HAR_FORGE, HAR_STORAGE);

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
    }

    public static class StructureType {
        private final ResourceLocation id;
        /** The size of the bounding box for this feature in the X axis */
        private final int sizeX;
        /** The size of the bounding box for this feature in the Y axis */
        private final int sizeY;
        /** The size of the bounding box for this feature in the Z axis */
        private final int sizeZ;

        private StructureType(ResourceLocation id, int sizeX, int sizeY, int sizeZ) {
            this.id = id;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.sizeZ = sizeZ;
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
                    this.boundingBox.offset(0, this.horizontalPos - this.boundingBox.minY + yOffset, 0);
                    return true;
                }
            }
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound tagCompound) {
            tagCompound.setInteger("HPos", this.horizontalPos);
            tagCompound.setString("StructureType", this.structureType.id.toString());
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            this.horizontalPos = tagCompound.getInteger("HPos");
            this.structureType = subTypeRegistry.get(new ResourceLocation(tagCompound.getString("StructureType")));
        }

        @Override
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
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
                    return true;
                }

                return false;
            }
        }
    }
}
