package projectaurora.world.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
						
				}
			case 1://alpha
				switch(world.getBlockMetadata(x, y, z)) {
					case 0:
						renderBlockAlgaeGlow(block, x, y, z, world, renderer);
						//renderInventoryBlock(block, world.getBlockMetadata(x, y, z), modelId, renderer);
				}
		}
		
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
            int meta = renderer.blockAccess.getBlockMetadata(x, y, z);
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
                IIcon iconToRender = renderer.getBlockIconFromSideAndMetadata(block, 1, meta);
                float flowDirection = (float)getFlowDirection(world, x, y, z);

                if (flowDirection > -999.0F) {
                    iconToRender = renderer.getBlockIconFromSideAndMetadata(block, 2, meta);
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
                renderer.renderFaceYNeg(block, (double)x, (double)y + longDouble, (double)z, renderer.getBlockIconFromSideAndMetadata(block, 0, meta));
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

                IIcon iicon1 = renderer.getBlockIconFromSideAndMetadata(block, side + 2, meta);

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
        
        tessellator.setBrightness(15728848/*block.getMixedBrightnessForBlock(world, x, y + 1, z));
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
}