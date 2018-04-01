package ladysnake.gens.world;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class ComponentGensPieces {
    public static void registerStructureComponents() {
        MapGenStructureIO.registerStructureComponent(Test.class, "gens:test");
    }

    public static abstract class Feature extends StructureComponent {
        /** The size of the bounding box for this feature in the X axis */
        protected int width;
        /** The size of the bounding box for this feature in the Y axis */
        protected int height;
        /** The size of the bounding box for this feature in the Z axis */
        protected int depth;
        protected int horizontalPos = -1;

        protected Feature() {}

        protected Feature(Random rand, int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
            super(0);
            this.width = sizeX;
            this.height = sizeY;
            this.depth = sizeZ;
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));

            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z) {
                this.boundingBox = new StructureBoundingBox(x, y, z, x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1);
            } else {
                this.boundingBox = new StructureBoundingBox(x, y, z, x + sizeZ - 1, y + sizeY - 1, z + sizeX - 1);
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
            tagCompound.setInteger("Width", this.width);
            tagCompound.setInteger("Height", this.height);
            tagCompound.setInteger("Depth", this.depth);
            tagCompound.setInteger("HPos", this.horizontalPos);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
            this.width = tagCompound.getInteger("Width");
            this.height = tagCompound.getInteger("Height");
            this.depth = tagCompound.getInteger("Depth");
            this.horizontalPos = tagCompound.getInteger("HPos");
        }
    }

    public static class Test extends Feature {
        private static final ResourceLocation TEST_ID = new ResourceLocation("gens", "test/test");

        public Test() {}

        public Test(Random rand, int x, int z)
        {
            super(rand, x, 64, z, 8, 8, 8);
        }

        @Override
        public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
            if (!this.offsetToAverageGroundLevel(worldIn, structureBoundingBoxIn, -1)) {
                return false;
            } else {
                StructureBoundingBox box = this.getBoundingBox();
                BlockPos pos = new BlockPos(box.minX, box.minY, box.minZ);
                Rotation[] rotations = Rotation.values();
                MinecraftServer minecraftserver = worldIn.getMinecraftServer();
                TemplateManager templatemanager = worldIn.getSaveHandler().getStructureTemplateManager();
                PlacementSettings placementsettings = new PlacementSettings().setRotation(rotations[randomIn.nextInt(rotations.length)]).setReplacedBlock(Blocks.STRUCTURE_VOID).setBoundingBox(box);
                Template template = templatemanager.getTemplate(minecraftserver, TEST_ID);
                template.addBlocksToWorldChunk(worldIn, pos, placementsettings);
                return true;
            }
        }
    }
}
