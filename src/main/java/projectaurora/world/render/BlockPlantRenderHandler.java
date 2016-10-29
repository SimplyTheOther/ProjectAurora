package projectaurora.world.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import projectaurora.core.ClientProxy;
import projectaurora.core.Content;
import projectaurora.world.block.BlockDummyLiquid;
import projectaurora.world.block.BlockPlant;

public class BlockPlantRenderHandler implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator tess = Tessellator.instance;
		
		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(Content.plant);
		
		GL11.glRotatef(90F, 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		//Render plant
		tess.startDrawingQuads();
		tess.setNormal(0F, -1F, 0F);
		renderer.renderFaceYNeg(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        //renderer.renderFaceYNeg(block, (double)x, (double)y + longDouble, (double)z, renderer.getBlockIconFromSide(block, 0));
		tess.draw();
		
		tess.startDrawingQuads();
		tess.setNormal(0F, 1F, 0F);
		renderer.renderFaceYPos(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		tess.draw();
		
		tess.startDrawingQuads();
		tess.setNormal(0F, 0F, -1F);
		renderer.renderFaceZNeg(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		tess.draw();
		
		tess.startDrawingQuads();
		tess.setNormal(0F, 0F, 1F);
		renderer.renderFaceZPos(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		tess.draw();
		
		tess.startDrawingQuads();
		tess.setNormal(-1F, 0F, 0F);
		renderer.renderFaceXNeg(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		tess.draw();
		
		tess.startDrawingQuads();
		tess.setNormal(1F, 0F, 0F);
		renderer.renderFaceXPos(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		tess.draw();
		
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		if(this.getRenderId() != modelId) {
			return false;
		}
		
		switch(BlockPlant.pass) {
			case 0://opaque
				switch(world.getBlockMetadata(x, y, z)) {
					case 0:
						break;
				}
			case 1://alpha
				switch(world.getBlockMetadata(x, y, z)) {
					case 0:
						renderStandardBlockWithAmbientOcclusionPartial/*BlockAlgaeGlow*/(block, x, y, z, world, renderer);
						//renderInventoryBlock(block, world.getBlockMetadata(x, y, z), modelId, renderer);
						break;
				}
		}
		
		Tessellator.instance.addVertex(0, 0, 0);
		Tessellator.instance.addVertex(0, 0, 0);
		Tessellator.instance.addVertex(0, 0, 0);
		Tessellator.instance.addVertex(0, 0, 0);
		
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.plantRenderID;
	}
	
    public boolean renderBlockAlgaeGlow(Block block, int x, int y, int z, IBlockAccess world, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        int colorMultiplier = block.colorMultiplier(renderer.blockAccess, x, y, z);
        float f = (float)(colorMultiplier >> 16 & 255) / 255.0F;
        float f1 = (float)(colorMultiplier >> 8 & 255) / 255.0F;
        float f2 = (float)(colorMultiplier & 255) / 255.0F;
        boolean shouldBlockOnTopBottomBeRendered = block.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1);
        boolean shouldBlockUnderTopBeRendered = block.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0);
        boolean[] shouldSidesBeRendered = new boolean[] { block.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2), block.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3), block.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4), block.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5) };

        if (!shouldBlockOnTopBottomBeRendered && !shouldBlockUnderTopBeRendered && !shouldSidesBeRendered[0] && !shouldSidesBeRendered[1] && !shouldSidesBeRendered[2] && !shouldSidesBeRendered[3]) {
            return false;
        } else {
            boolean newRenderAllFaces = false;
            float f3 = 0.5F;
            float f4 = 1.0F;
            float f5 = 0.8F;
            float f6 = 0.6F;
            double newRenderMinY = 0.0D;
            double newRenderMaxY = 1.0D;
            
            Material material = block.getMaterial();
            double blockHeight = (double)0.015625F;
            double blockHeightPlusZ = (double)0.015625F;
            double blockHeightPlusXPlusZ = (double)0.015625F;
            double blockHeightPlusX = (double)0.015625F;
            double longDouble = 0.0010000000474974513D;
            float f9;
            float f10;
            float f11;

            if (renderer.renderAllFaces || shouldBlockOnTopBottomBeRendered) {
                newRenderAllFaces = true;
                IIcon iconToRender = renderer.getBlockIconFromSide(block, 1);
                float flowDirection = (float)getFlowDirection(world, x, y, z);

                if (flowDirection > -999.0F) {
                    iconToRender = renderer.getBlockIconFromSide(block, 2);
                }

                blockHeight -= longDouble;
                blockHeightPlusZ -= longDouble;
                blockHeightPlusXPlusZ -= longDouble;
                blockHeightPlusX -= longDouble;
                double d7;
                double d8;
                double d10;
                double d12;
                double d14;
                double d16;
                double d18;
                double d20;

                if (flowDirection < -999.0F) {
                    d7 = (double)iconToRender.getInterpolatedU(0.0D);
                    d14 = (double)iconToRender.getInterpolatedV(0.0D);
                    d8 = d7;
                    d16 = (double)iconToRender.getInterpolatedV(16.0D);
                    d10 = (double)iconToRender.getInterpolatedU(16.0D);
                    d18 = d16;
                    d12 = d10;
                    d20 = d14;
                } else {
                    f9 = MathHelper.sin(flowDirection) * 0.25F;
                    f10 = MathHelper.cos(flowDirection) * 0.25F;
                    f11 = 8.0F;
                    d7 = (double)iconToRender.getInterpolatedU((double)(8.0F + (-f10 - f9) * 16.0F));
                    d14 = (double)iconToRender.getInterpolatedV((double)(8.0F + (-f10 + f9) * 16.0F));
                    d8 = (double)iconToRender.getInterpolatedU((double)(8.0F + (-f10 + f9) * 16.0F));
                    d16 = (double)iconToRender.getInterpolatedV((double)(8.0F + (f10 + f9) * 16.0F));
                    d10 = (double)iconToRender.getInterpolatedU((double)(8.0F + (f10 + f9) * 16.0F));
                    d18 = (double)iconToRender.getInterpolatedV((double)(8.0F + (f10 - f9) * 16.0F));
                    d12 = (double)iconToRender.getInterpolatedU((double)(8.0F + (f10 - f9) * 16.0F));
                    d20 = (double)iconToRender.getInterpolatedV((double)(8.0F + (-f10 - f9) * 16.0F));
                }

                tessellator.setBrightness(15728848);
                tessellator.setColorOpaque_F(f4 * f, f4 * f1, f4 * f2);
                tessellator.addVertexWithUV((double)(x + 0), (double)y + blockHeight, (double)(z + 0), d7, d14);
                tessellator.addVertexWithUV((double)(x + 0), (double)y + blockHeightPlusZ, (double)(z + 1), d8, d16);
                tessellator.addVertexWithUV((double)(x + 1), (double)y + blockHeightPlusXPlusZ, (double)(z + 1), d10, d18);
                tessellator.addVertexWithUV((double)(x + 1), (double)y + blockHeightPlusX, (double)(z + 0), d12, d20);
                tessellator.addVertexWithUV((double)(x + 0), (double)y + blockHeight, (double)(z + 0), d7, d14);
                tessellator.addVertexWithUV((double)(x + 1), (double)y + blockHeightPlusX, (double)(z + 0), d12, d20);
                tessellator.addVertexWithUV((double)(x + 1), (double)y + blockHeightPlusXPlusZ, (double)(z + 1), d10, d18);
                tessellator.addVertexWithUV((double)(x + 0), (double)y + blockHeightPlusZ, (double)(z + 1), d8, d16);
            }

            if (renderer.renderAllFaces || shouldBlockUnderTopBeRendered) {
                tessellator.setBrightness(15728848);
                tessellator.setColorOpaque_F(f3, f3, f3);
                renderer.renderFaceYNeg(block, (double)x, (double)y + longDouble, (double)z, renderer.getBlockIconFromSide(block, 0));
                newRenderAllFaces = true;
            }

            for (int side = 0; side < 4; ++side) {
                int newX = x;
                int newZ = z;

                if (side == 0) {
                    newZ = z - 1;
                }

                if (side == 1) {
                    ++newZ;
                }

                if (side == 2) {
                    newX = x - 1;
                }

                if (side == 3) {
                    ++newX;
                }

                IIcon iicon1 = renderer.getBlockIconFromSide(block, side + 2);

                if (renderer.renderAllFaces || shouldSidesBeRendered[side]) {
                    double newLiquidHeight;
                    double anotherNewLiquidHeight;
                    double d13;
                    double d15;
                    double d17;
                    double d19;

                    if (side == 0) {
                        newLiquidHeight = blockHeight;
                        anotherNewLiquidHeight = blockHeightPlusX;
                        d13 = (double)x;
                        d17 = (double)(x + 1);
                        d15 = (double)z + longDouble;
                        d19 = (double)z + longDouble;
                    } else if (side == 1) {
                        newLiquidHeight = blockHeightPlusXPlusZ;
                        anotherNewLiquidHeight = blockHeightPlusZ;
                        d13 = (double)(x + 1);
                        d17 = (double)x;
                        d15 = (double)(z + 1) - longDouble;
                        d19 = (double)(z + 1) - longDouble;
                    } else if (side == 2) {
                        newLiquidHeight = blockHeightPlusZ;
                        anotherNewLiquidHeight = blockHeight;
                        d13 = (double)x + longDouble;
                        d17 = (double)x + longDouble;
                        d15 = (double)(z + 1);
                        d19 = (double)z;
                    } else {
                        newLiquidHeight = blockHeightPlusX;
                        anotherNewLiquidHeight = blockHeightPlusXPlusZ;
                        d13 = (double)(x + 1) - longDouble;
                        d17 = (double)(x + 1) - longDouble;
                        d15 = (double)z;
                        d19 = (double)(z + 1);
                    }

                    newRenderAllFaces = true;
                    float f8 = iicon1.getInterpolatedU(0.0D);
                    f9 = iicon1.getInterpolatedU(8.0D);
                    f10 = iicon1.getInterpolatedV((1.0D - newLiquidHeight) * 16.0D * 0.5D);
                    f11 = iicon1.getInterpolatedV((1.0D - anotherNewLiquidHeight) * 16.0D * 0.5D);
                    float f12 = iicon1.getInterpolatedV(8.0D);
                    tessellator.setBrightness(15728848);
                    float f13 = 1.0F;
                    f13 *= side < 2 ? f5 : f6;
                    tessellator.setColorOpaque_F(f4 * f13 * f, f4 * f13 * f1, f4 * f13 * f2);
                    tessellator.addVertexWithUV(d13, (double)y + newLiquidHeight, d15, (double)f8, (double)f10);
                    tessellator.addVertexWithUV(d17, (double)y + anotherNewLiquidHeight, d19, (double)f9, (double)f11);
                    tessellator.addVertexWithUV(d17, (double)(y + 0), d19, (double)f9, (double)f12);
                    tessellator.addVertexWithUV(d13, (double)(y + 0), d15, (double)f8, (double)f12);
                    tessellator.addVertexWithUV(d13, (double)(y + 0), d15, (double)f8, (double)f12);
                    tessellator.addVertexWithUV(d17, (double)(y + 0), d19, (double)f9, (double)f12);
                    tessellator.addVertexWithUV(d17, (double)y + anotherNewLiquidHeight, d19, (double)f9, (double)f11);
                    tessellator.addVertexWithUV(d13, (double)y + newLiquidHeight, d15, (double)f8, (double)f10);
                }
            }

            renderer.renderMinY = newRenderMinY;
            renderer.renderMaxY = newRenderMaxY;
            return newRenderAllFaces;
        }
        /*Tessellator tessellator = Tessellator.instance;
        IIcon icon = renderer.getBlockIconFromSide(block, 1);

        if (renderer.hasOverrideBlockTexture()) {
            icon = renderer.overrideBlockTexture;
        }

        float f = 0.015625F;
        double minU = icon.getMinU();
        double minV = icon.getMinV();
        double maxU = icon.getMaxU();
        double maxV = icon.getMaxV();
        long l = x * 3129871 ^ z * 116129781L ^ y;
        l = l * l * 42317861L + l * 11L;
        int i1 = (int)(l >> 16 & 0x3);
        
        tessellator.setBrightness(15728848);
        float newX = (float)x + 0.5F;
        float newZ = (float)z + 0.5F;
        float f3 = (float)(i1 & 1) * 0.5F * (float)(1 - i1 / 2 % 2 * 2);
        float f4 = (float)(i1 + 1 & 1) * 0.5F * (float)(1 - (i1 + 1) / 2 % 2 * 2);
        tessellator.setColorOpaque_I(block.getBlockColor());
        tessellator.addVertexWithUV((double)(newX + f3 - f4), (double)((float)y + f), (double)(newZ + f3 + f4), minU, minV);
        tessellator.addVertexWithUV((double)(newX + f3 + f4), (double)((float)y + f), (double)(newZ - f3 + f4), maxU, minV);
        tessellator.addVertexWithUV((double)(newX - f3 + f4), (double)((float)y + f), (double)(newZ - f3 - f4), maxU, maxV);
        tessellator.addVertexWithUV((double)(newX - f3 - f4), (double)((float)y + f), (double)(newZ + f3 - f4), minU, maxV);
        tessellator.setColorOpaque_I((block.getBlockColor() & 16711422) >> 1);
        tessellator.addVertexWithUV((double)(newX - f3 - f4), (double)((float)y + f), (double)(newZ + f3 - f4), minU, maxV);
        tessellator.addVertexWithUV((double)(newX - f3 + f4), (double)((float)y + f), (double)(newZ - f3 - f4), maxU, maxV);
        tessellator.addVertexWithUV((double)(newX + f3 + f4), (double)((float)y + f), (double)(newZ - f3 + f4), maxU, minV);
        tessellator.addVertexWithUV((double)(newX + f3 - f4), (double)((float)y + f), (double)(newZ + f3 + f4), minU, minV);
        return true;*/
    }

	private double getFlowDirection(IBlockAccess world, int x, int y, int z) {
        Vec3 vec3 = getFlowVector(world, x, y, z);

        if(vec3.xCoord == 0.0D && vec3.zCoord == 0.0D) {
        	return -1000.0D;
        } else {
        	return Math.atan2(vec3.zCoord, vec3.xCoord) - (Math.PI / 2D);
        }
	}

	private Vec3 getFlowVector(IBlockAccess world, int x, int y, int z) {
        Vec3 vec3 = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
        int effectiveFlowDecay = -1;

        for (int i1 = 0; i1 < 4; ++i1) {
            int newX = x;
            int newZ = z;

            if (i1 == 0) {
                newX = x - 1;
            }

            if (i1 == 1) {
                newZ = z - 1;
            }

            if (i1 == 2) {
                ++newX;
            }

            if (i1 == 3) {
                ++newZ;
            }

            int newEffectiveFlowDecay = -1;
            int totalEffectiveFlowDecay;

            if (newEffectiveFlowDecay < 0) {
                if (!world.getBlock(newX, y, newZ).getMaterial().blocksMovement()) {
                    newEffectiveFlowDecay = -1;

                    if (newEffectiveFlowDecay >= 0) {
                        totalEffectiveFlowDecay = newEffectiveFlowDecay - (effectiveFlowDecay - 8);
                        vec3 = vec3.addVector((double)((newX - x) * totalEffectiveFlowDecay), (double)((y - y) * totalEffectiveFlowDecay), (double)((newZ - z) * totalEffectiveFlowDecay));
                    }
                }
            } else if (newEffectiveFlowDecay >= 0) {
                totalEffectiveFlowDecay = newEffectiveFlowDecay - effectiveFlowDecay;
                vec3 = vec3.addVector((double)((newX - x) * totalEffectiveFlowDecay), (double)((y - y) * totalEffectiveFlowDecay), (double)((newZ - z) * totalEffectiveFlowDecay));
            }
        }

        if (world.getBlockMetadata(x, y, z) >= 8) {
            boolean flag = false;

            if (flag || world.getBlock(x, y, z).isBlockSolid(world, x, y, z - 1, 2)) {
                flag = true;
            }

            if (flag || world.getBlock(x, y, z).isBlockSolid(world, x, y, z + 1, 3)) {
                flag = true;
            }

            if (flag || world.getBlock(x, y, z).isBlockSolid(world, x - 1, y, z, 4)) {
                flag = true;
            }

            if (flag || world.getBlock(x, y, z).isBlockSolid(world, x + 1, y, z, 5)) {
                flag = true;
            }

            if (flag || world.getBlock(x, y, z).isBlockSolid(world, x, y + 1, z - 1, 2)) {
                flag = true;
            }

            if (flag || world.getBlock(x, y, z).isBlockSolid(world, x, y + 1, z + 1, 3)) {
                flag = true;
            }

            if (flag || world.getBlock(x, y, z).isBlockSolid(world, x - 1, y + 1, z, 4)) {
                flag = true;
            }

            if (flag || world.getBlock(x, y, z).isBlockSolid(world, x + 1, y + 1, z, 5)) {
                flag = true;
            }

            //if (flag) {
                vec3 = vec3.normalize().addVector(0.0D, -6.0D, 0.0D);
            //}
        }

        vec3 = vec3.normalize();
        return vec3;
	}
	
    public boolean renderStandardBlockWithAmbientOcclusionPartial(Block block, int x, int y, int z, IBlockAccess world, RenderBlocks renderer) {
    	int colourMultiplier = block.colorMultiplier(renderer.blockAccess, x, y, z);
        float red = (float)(colourMultiplier >> 16 & 255) / 255.0F;
        float green = (float)(colourMultiplier >> 8 & 255) / 255.0F;
        float blue = (float)(colourMultiplier & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable) {
            float tempRed = (red * 30.0F + green * 59.0F + blue * 11.0F) / 100.0F;
            float tempGreen = (red * 30.0F + green * 70.0F) / 100.0F;
            float tempBlue = (red * 30.0F + blue * 70.0F) / 100.0F;
            red = tempRed;
            green = tempGreen;
            blue = tempBlue;
        }
    	
        renderer.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean doesntHaveOverrideTexture = true;
        int brightness = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(/*983055*/15728848);

        if (renderer.hasOverrideBlockTexture()) {
            doesntHaveOverrideTexture = false;
        }

        boolean blocksGrassXPlusYMinus;
        boolean blocksGrassXMinusYMinus;
        boolean blocksGrassYMinusZPlus;
        boolean blocksGrassYMinusZMinus;
        int newBrightness;
        float ambientOcclusionMinusY;

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0)) {
            if (renderer.renderMinY <= 0.0D) {
                --y;
            }

            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            blocksGrassXPlusYMinus = renderer.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
            blocksGrassXMinusYMinus = renderer.blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
            blocksGrassYMinusZPlus = renderer.blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();
            blocksGrassYMinusZMinus = renderer.blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();

            if (!blocksGrassYMinusZMinus && !blocksGrassXMinusYMinus) {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
            } else {
                renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z - 1);
            }

            if (!blocksGrassYMinusZPlus && !blocksGrassXMinusYMinus) {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
            } else {
                renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z + 1);
            }

            if (!blocksGrassYMinusZMinus && !blocksGrassXPlusYMinus) {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
            } else {
                renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z - 1);
            }

            if (!blocksGrassYMinusZPlus && !blocksGrassXPlusYMinus) {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
            } else {
                renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z + 1);
            }

            if (renderer.renderMinY <= 0.0D) {
                ++y;
            }

            newBrightness = brightness;

            if (renderer.renderMinY <= 0.0D || !renderer.blockAccess.getBlock(x, y - 1, z).isOpaqueCube()) {
            	newBrightness = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            }

            ambientOcclusionMinusY = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + ambientOcclusionMinusY) / 4.0F;
            f6 = (renderer.aoLightValueScratchYZNP + ambientOcclusionMinusY + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
            f5 = (ambientOcclusionMinusY + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + ambientOcclusionMinusY + renderer.aoLightValueScratchYZNN) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, newBrightness);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, newBrightness);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, newBrightness);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, newBrightness);

            if (doesntHaveOverrideTexture) {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = red * 0.5F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = green * 0.5F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = blue * 0.5F;
            } else {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.5F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.5F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.5F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 0));
            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1)) {
            if (renderer.renderMaxY >= 1.0D) {
                ++y;
            }

            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            blocksGrassXPlusYMinus = renderer.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            blocksGrassXMinusYMinus = renderer.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            blocksGrassYMinusZPlus = renderer.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            blocksGrassYMinusZMinus = renderer.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();

            if (!blocksGrassYMinusZMinus && !blocksGrassXMinusYMinus) {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
            } else {
                renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z - 1);
            }

            if (!blocksGrassYMinusZMinus && !blocksGrassXPlusYMinus) {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
            } else {
                renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z - 1);
            }

            if (!blocksGrassYMinusZPlus && !blocksGrassXMinusYMinus) {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
            } else {
                renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z + 1);
            }

            if (!blocksGrassYMinusZPlus && !blocksGrassXPlusYMinus) {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
            } else {
                renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z + 1);
            }

            if (renderer.renderMaxY >= 1.0D) {
                --y;
            }

            newBrightness = brightness;

            if (renderer.renderMaxY >= 1.0D || !renderer.blockAccess.getBlock(x, y + 1, z).isOpaqueCube()) {
            	newBrightness = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            }

            ambientOcclusionMinusY = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + ambientOcclusionMinusY) / 4.0F;
            f3 = (renderer.aoLightValueScratchYZPP + ambientOcclusionMinusY + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
            f4 = (ambientOcclusionMinusY + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + ambientOcclusionMinusY + renderer.aoLightValueScratchYZPN) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, newBrightness);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, newBrightness);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, newBrightness);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, newBrightness);
            renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = red;
            renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = green;
            renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = blue;
            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 1));
            flag = true;
        }

        float f8;
        float f9;
        float f10;
        float f11;
        int j1;
        int k1;
        int l1;
        int i2;
        IIcon iicon;

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2)) {
            if (renderer.renderMinZ <= 0.0D) {
                --z;
            }

            renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            blocksGrassXPlusYMinus = renderer.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();
            blocksGrassXMinusYMinus = renderer.blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
            blocksGrassYMinusZPlus = renderer.blockAccess.getBlock(x, y + 1, z - 1).getCanBlockGrass();
            blocksGrassYMinusZMinus = renderer.blockAccess.getBlock(x, y - 1, z - 1).getCanBlockGrass();

            if (!blocksGrassXMinusYMinus && !blocksGrassYMinusZMinus) {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            } else {
                renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y - 1, z);
            }

            if (!blocksGrassXMinusYMinus && !blocksGrassYMinusZPlus) {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            } else {
                renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y + 1, z);
            }

            if (!blocksGrassXPlusYMinus && !blocksGrassYMinusZMinus) {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            } else {
                renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y - 1, z);
            }

            if (!blocksGrassXPlusYMinus && !blocksGrassYMinusZPlus) {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            } else {
                renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y + 1, z);
            }

            if (renderer.renderMinZ <= 0.0D) {
                ++z;
            }

            newBrightness = brightness;

            if (renderer.renderMinZ <= 0.0D || !renderer.blockAccess.getBlock(x, y, z - 1).isOpaqueCube()) {
            	newBrightness = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            }

            ambientOcclusionMinusY = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + ambientOcclusionMinusY + renderer.aoLightValueScratchYZPN) / 4.0F;
            f9 = (ambientOcclusionMinusY + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f10 = (renderer.aoLightValueScratchYZNN + ambientOcclusionMinusY + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + ambientOcclusionMinusY) / 4.0F;
            f3 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMaxY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            f4 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            f5 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            f6 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, newBrightness);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, newBrightness);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, newBrightness);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, newBrightness);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMinX), renderer.renderMaxY * renderer.renderMinX, (1.0D - renderer.renderMaxY) * renderer.renderMinX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMaxY * (1.0D - renderer.renderMaxX), renderer.renderMaxY * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * renderer.renderMaxX, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMaxX), renderer.renderMinY * renderer.renderMaxX, (1.0D - renderer.renderMinY) * renderer.renderMaxX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, k1, l1, i2, renderer.renderMinY * (1.0D - renderer.renderMinX), renderer.renderMinY * renderer.renderMinX, (1.0D - renderer.renderMinY) * renderer.renderMinX, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));

            if (doesntHaveOverrideTexture) {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = red * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = green * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = blue * 0.8F;
            } else {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 2);
            renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, iicon);

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3)) {
            if (renderer.renderMaxZ >= 1.0D) {
                ++z;
            }

            renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            blocksGrassXPlusYMinus = renderer.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
            blocksGrassXMinusYMinus = renderer.blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();
            blocksGrassYMinusZPlus = renderer.blockAccess.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            blocksGrassYMinusZMinus = renderer.blockAccess.getBlock(x, y - 1, z + 1).getCanBlockGrass();

            if (!blocksGrassXMinusYMinus && !blocksGrassYMinusZMinus) {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            } else {
                renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y - 1, z);
            }

            if (!blocksGrassXMinusYMinus && !blocksGrassYMinusZPlus) {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            } else {
                renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y + 1, z);
            }

            if (!blocksGrassXPlusYMinus && !blocksGrassYMinusZMinus) {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            } else {
                renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y - 1, z);
            }

            if (!blocksGrassXPlusYMinus && !blocksGrassYMinusZPlus) {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            } else {
                renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y + 1, z);
            }

            if (renderer.renderMaxZ >= 1.0D) {
                --z;
            }

            newBrightness = brightness;

            if (renderer.renderMaxZ >= 1.0D || !renderer.blockAccess.getBlock(x, y, z + 1).isOpaqueCube()){
            	newBrightness = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            }

            ambientOcclusionMinusY = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + ambientOcclusionMinusY + renderer.aoLightValueScratchYZPP) / 4.0F;
            f9 = (ambientOcclusionMinusY + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f10 = (renderer.aoLightValueScratchYZNP + ambientOcclusionMinusY + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + ambientOcclusionMinusY) / 4.0F;
            f3 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMaxY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX));
            f4 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMinX) + (double)f9 * renderer.renderMinY * renderer.renderMinX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMinX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX));
            f5 = (float)((double)f8 * renderer.renderMinY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMinY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMinY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX));
            f6 = (float)((double)f8 * renderer.renderMaxY * (1.0D - renderer.renderMaxX) + (double)f9 * renderer.renderMaxY * renderer.renderMaxX + (double)f10 * (1.0D - renderer.renderMaxY) * renderer.renderMaxX + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX));
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, newBrightness);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, newBrightness);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, newBrightness);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, newBrightness);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMaxY) * renderer.renderMinX, renderer.renderMaxY * renderer.renderMinX);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinX), (1.0D - renderer.renderMinY) * renderer.renderMinX, renderer.renderMinY * renderer.renderMinX);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMinY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMinY) * renderer.renderMaxX, renderer.renderMinY * renderer.renderMaxX);
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, renderer.renderMaxY * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxX), (1.0D - renderer.renderMaxY) * renderer.renderMaxX, renderer.renderMaxY * renderer.renderMaxX);

            if (doesntHaveOverrideTexture) {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = red * 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = green * 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = blue * 0.8F;
            } else {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.8F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.8F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.8F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 3);
            renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, iicon);
            
            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4)) {
            if (renderer.renderMinX <= 0.0D) {
                --x;
            }

            renderer.aoLightValueScratchXYNN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYNP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            blocksGrassXPlusYMinus = renderer.blockAccess.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            blocksGrassXMinusYMinus = renderer.blockAccess.getBlock(x - 1, y - 1, z).getCanBlockGrass();
            blocksGrassYMinusZPlus = renderer.blockAccess.getBlock(x - 1, y, z - 1).getCanBlockGrass();
            blocksGrassYMinusZMinus = renderer.blockAccess.getBlock(x - 1, y, z + 1).getCanBlockGrass();

            if (!blocksGrassYMinusZPlus && !blocksGrassXMinusYMinus) {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            } else {
                renderer.aoLightValueScratchXYZNNN = renderer.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z - 1);
            }

            if (!blocksGrassYMinusZMinus && !blocksGrassXMinusYMinus) {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            } else {
                renderer.aoLightValueScratchXYZNNP = renderer.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z + 1);
            }

            if (!blocksGrassYMinusZPlus && !blocksGrassXPlusYMinus)  {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            } else {
                renderer.aoLightValueScratchXYZNPN = renderer.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z - 1);
            }

            if (!blocksGrassYMinusZMinus && !blocksGrassXPlusYMinus) {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            } else {
                renderer.aoLightValueScratchXYZNPP = renderer.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z + 1);
            }

            if (renderer.renderMinX <= 0.0D) {
                ++x;
            }

            newBrightness = brightness;

            if (renderer.renderMinX <= 0.0D || !renderer.blockAccess.getBlock(x - 1, y, z).isOpaqueCube()) {
            	newBrightness = block.getMixedBrightnessForBlock(renderer.blockAccess, x - 1, y, z);
            }

            ambientOcclusionMinusY = renderer.blockAccess.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + ambientOcclusionMinusY + renderer.aoLightValueScratchXZNP) / 4.0F;
            f9 = (ambientOcclusionMinusY + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
            f10 = (renderer.aoLightValueScratchXZNN + ambientOcclusionMinusY + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
            f11 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + ambientOcclusionMinusY) / 4.0F;
            f3 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMaxZ + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            f4 = (float)((double)f9 * renderer.renderMaxY * renderer.renderMinZ + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f11 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            f5 = (float)((double)f9 * renderer.renderMinY * renderer.renderMinZ + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            f6 = (float)((double)f9 * renderer.renderMinY * renderer.renderMaxZ + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f11 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, newBrightness);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, newBrightness);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, newBrightness);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, newBrightness);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMaxZ, renderer.renderMaxY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMaxY) * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMaxY * renderer.renderMinZ, renderer.renderMaxY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMaxY) * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMinZ, renderer.renderMinY * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), (1.0D - renderer.renderMinY) * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(k1, l1, i2, j1, renderer.renderMinY * renderer.renderMaxZ, renderer.renderMinY * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), (1.0D - renderer.renderMinY) * renderer.renderMaxZ);

            if (doesntHaveOverrideTexture) {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = red * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = green * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = blue * 0.6F;
            } else {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 4);
            renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, iicon);

            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5)) {
            if (renderer.renderMaxX >= 1.0D) {
                ++x;
            }

            renderer.aoLightValueScratchXYPN = renderer.blockAccess.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = renderer.blockAccess.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = renderer.blockAccess.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = renderer.blockAccess.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z - 1);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z + 1);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z);
            blocksGrassXPlusYMinus = renderer.blockAccess.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            blocksGrassXMinusYMinus = renderer.blockAccess.getBlock(x + 1, y - 1, z).getCanBlockGrass();
            blocksGrassYMinusZPlus = renderer.blockAccess.getBlock(x + 1, y, z + 1).getCanBlockGrass();
            blocksGrassYMinusZMinus = renderer.blockAccess.getBlock(x + 1, y, z - 1).getCanBlockGrass();

            if (!blocksGrassXMinusYMinus && !blocksGrassYMinusZMinus) {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            } else {
                renderer.aoLightValueScratchXYZPNN = renderer.blockAccess.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z - 1);
            }

            if (!blocksGrassXMinusYMinus && !blocksGrassYMinusZPlus) {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            } else {
                renderer.aoLightValueScratchXYZPNP = renderer.blockAccess.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z + 1);
            }

            if (!blocksGrassXPlusYMinus && !blocksGrassYMinusZMinus) {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            } else {
                renderer.aoLightValueScratchXYZPPN = renderer.blockAccess.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z - 1);
            }

            if (!blocksGrassXPlusYMinus && !blocksGrassYMinusZPlus) {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            } else {
                renderer.aoLightValueScratchXYZPPP = renderer.blockAccess.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(renderer.blockAccess, x, y + 1, z + 1);
            }

            if (renderer.renderMaxX >= 1.0D) {
                --x;
            }

            newBrightness = brightness;

            if (renderer.renderMaxX >= 1.0D || !renderer.blockAccess.getBlock(x + 1, y, z).isOpaqueCube()) {
            	newBrightness = block.getMixedBrightnessForBlock(renderer.blockAccess, x + 1, y, z);
            }

            ambientOcclusionMinusY = renderer.blockAccess.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            f8 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + ambientOcclusionMinusY + renderer.aoLightValueScratchXZPP) / 4.0F;
            f9 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + ambientOcclusionMinusY) / 4.0F;
            f10 = (renderer.aoLightValueScratchXZPN + ambientOcclusionMinusY + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
            f11 = (ambientOcclusionMinusY + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f3 = (float)((double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMinY * renderer.renderMaxZ);
            f4 = (float)((double)f8 * (1.0D - renderer.renderMinY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMinY * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMinY * renderer.renderMinZ);
            f5 = (float)((double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMinZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ) + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMinZ) + (double)f11 * renderer.renderMaxY * renderer.renderMinZ);
            f6 = (float)((double)f8 * (1.0D - renderer.renderMaxY) * renderer.renderMaxZ + (double)f9 * (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ) + (double)f10 * renderer.renderMaxY * (1.0D - renderer.renderMaxZ) + (double)f11 * renderer.renderMaxY * renderer.renderMaxZ);
            j1 = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, newBrightness);
            k1 = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, newBrightness);
            l1 = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, newBrightness);
            i2 = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, newBrightness);
            renderer.brightnessTopLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMaxZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMaxZ), renderer.renderMinY * (1.0D - renderer.renderMaxZ), renderer.renderMinY * renderer.renderMaxZ);
            renderer.brightnessBottomLeft = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMinY) * renderer.renderMinZ, (1.0D - renderer.renderMinY) * (1.0D - renderer.renderMinZ), renderer.renderMinY * (1.0D - renderer.renderMinZ), renderer.renderMinY * renderer.renderMinZ);
            renderer.brightnessBottomRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMinZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMinZ), renderer.renderMaxY * (1.0D - renderer.renderMinZ), renderer.renderMaxY * renderer.renderMinZ);
            renderer.brightnessTopRight = renderer.mixAoBrightness(j1, i2, l1, k1, (1.0D - renderer.renderMaxY) * renderer.renderMaxZ, (1.0D - renderer.renderMaxY) * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * (1.0D - renderer.renderMaxZ), renderer.renderMaxY * renderer.renderMaxZ);

            if (doesntHaveOverrideTexture) {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = red * 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = green * 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = blue * 0.6F;
            } else {
                renderer.colorRedTopLeft = renderer.colorRedBottomLeft = renderer.colorRedBottomRight = renderer.colorRedTopRight = 0.6F;
                renderer.colorGreenTopLeft = renderer.colorGreenBottomLeft = renderer.colorGreenBottomRight = renderer.colorGreenTopRight = 0.6F;
                renderer.colorBlueTopLeft = renderer.colorBlueBottomLeft = renderer.colorBlueBottomRight = renderer.colorBlueTopRight = 0.6F;
            }

            renderer.colorRedTopLeft *= f3;
            renderer.colorGreenTopLeft *= f3;
            renderer.colorBlueTopLeft *= f3;
            renderer.colorRedBottomLeft *= f4;
            renderer.colorGreenBottomLeft *= f4;
            renderer.colorBlueBottomLeft *= f4;
            renderer.colorRedBottomRight *= f5;
            renderer.colorGreenBottomRight *= f5;
            renderer.colorBlueBottomRight *= f5;
            renderer.colorRedTopRight *= f6;
            renderer.colorGreenTopRight *= f6;
            renderer.colorBlueTopRight *= f6;
            iicon = renderer.getBlockIcon(block, renderer.blockAccess, x, y, z, 5);
            renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, iicon);

            flag = true;
        }

        renderer.enableAO = false;
        return flag;
    }
}