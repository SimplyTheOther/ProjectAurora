package projectaurora.world.render;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import projectaurora.core.ClientProxy;
import projectaurora.world.block.BlockDummyLiquid;

public class BlockDummyLiquidRenderHandler implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		renderer.renderBlockAsItem(block, metadata, 1F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
			RenderBlocks renderer) {
		if(this.getRenderId() != modelId) {
			return false;
		}
		
		this.renderBlockLiquid(block, x, y, z, renderer);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.liquidRenderID;
	}
	
    public boolean renderBlockLiquid(Block liquid, int x, int y, int z, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        int colorMultiplier = liquid.colorMultiplier(renderer.blockAccess, x, y, z);
        float f = (float)(colorMultiplier >> 16 & 255) / 255.0F;
        float f1 = (float)(colorMultiplier >> 8 & 255) / 255.0F;
        float f2 = (float)(colorMultiplier & 255) / 255.0F;
        boolean shouldBlockOnTopBottomBeRendered = liquid.shouldSideBeRendered(renderer.blockAccess, x, y + 1, z, 1);
        boolean shouldBlockUnderTopBeRendered = liquid.shouldSideBeRendered(renderer.blockAccess, x, y - 1, z, 0);
        boolean[] shouldSidesBeRendered = new boolean[] { liquid.shouldSideBeRendered(renderer.blockAccess, x, y, z - 1, 2), liquid.shouldSideBeRendered(renderer.blockAccess, x, y, z + 1, 3), liquid.shouldSideBeRendered(renderer.blockAccess, x - 1, y, z, 4), liquid.shouldSideBeRendered(renderer.blockAccess, x + 1, y, z, 5) };

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
            
            Material material = liquid.getMaterial();
            int meta = renderer.blockAccess.getBlockMetadata(x, y, z);
            double liquidHeight = (double)0.8627451F;
            double liquidHeightPlusZ = (double)0.8627451F;
            double liquidHeightPlusXPlusZ = (double)0.8627451F;
            double liquidHeightPlusX = (double)0.8627451F;
            double longDouble = 0.0010000000474974513D;
            float f9;
            float f10;
            float f11;

            if (renderer.renderAllFaces || shouldBlockOnTopBottomBeRendered) {
                newRenderAllFaces = true;
                IIcon iconToRender = renderer.getBlockIconFromSideAndMetadata(liquid, 1, meta);
                float flowDirection = (float)((BlockDummyLiquid)liquid).getFlowDirection(renderer.blockAccess, x, y, z, material);

                if (flowDirection > -999.0F) {
                    iconToRender = renderer.getBlockIconFromSideAndMetadata(liquid, 2, meta);
                }

                liquidHeight -= longDouble;
                liquidHeightPlusZ -= longDouble;
                liquidHeightPlusXPlusZ -= longDouble;
                liquidHeightPlusX -= longDouble;
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

                tessellator.setBrightness(liquid.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
                tessellator.setColorOpaque_F(f4 * f, f4 * f1, f4 * f2);
                tessellator.addVertexWithUV((double)(x + 0), (double)y + liquidHeight, (double)(z + 0), d7, d14);
                tessellator.addVertexWithUV((double)(x + 0), (double)y + liquidHeightPlusZ, (double)(z + 1), d8, d16);
                tessellator.addVertexWithUV((double)(x + 1), (double)y + liquidHeightPlusXPlusZ, (double)(z + 1), d10, d18);
                tessellator.addVertexWithUV((double)(x + 1), (double)y + liquidHeightPlusX, (double)(z + 0), d12, d20);
                tessellator.addVertexWithUV((double)(x + 0), (double)y + liquidHeight, (double)(z + 0), d7, d14);
                tessellator.addVertexWithUV((double)(x + 1), (double)y + liquidHeightPlusX, (double)(z + 0), d12, d20);
                tessellator.addVertexWithUV((double)(x + 1), (double)y + liquidHeightPlusXPlusZ, (double)(z + 1), d10, d18);
                tessellator.addVertexWithUV((double)(x + 0), (double)y + liquidHeightPlusZ, (double)(z + 1), d8, d16);
            }

            if (renderer.renderAllFaces || shouldBlockUnderTopBeRendered) {
                tessellator.setBrightness(liquid.getMixedBrightnessForBlock(renderer.blockAccess, x, y - 1, z));
                tessellator.setColorOpaque_F(f3, f3, f3);
                renderer.renderFaceYNeg(liquid, (double)x, (double)y + longDouble, (double)z, renderer.getBlockIconFromSide(liquid, 0));
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

                IIcon iicon1 = renderer.getBlockIconFromSideAndMetadata(liquid, side + 2, meta);

                if (renderer.renderAllFaces || shouldSidesBeRendered[side]) {
                    double newLiquidHeight;
                    double anotherNewLiquidHeight;
                    double d13;
                    double d15;
                    double d17;
                    double d19;

                    if (side == 0) {
                        newLiquidHeight = liquidHeight;
                        anotherNewLiquidHeight = liquidHeightPlusX;
                        d13 = (double)x;
                        d17 = (double)(x + 1);
                        d15 = (double)z + longDouble;
                        d19 = (double)z + longDouble;
                    } else if (side == 1) {
                        newLiquidHeight = liquidHeightPlusXPlusZ;
                        anotherNewLiquidHeight = liquidHeightPlusZ;
                        d13 = (double)(x + 1);
                        d17 = (double)x;
                        d15 = (double)(z + 1) - longDouble;
                        d19 = (double)(z + 1) - longDouble;
                    } else if (side == 2) {
                        newLiquidHeight = liquidHeightPlusZ;
                        anotherNewLiquidHeight = liquidHeight;
                        d13 = (double)x + longDouble;
                        d17 = (double)x + longDouble;
                        d15 = (double)(z + 1);
                        d19 = (double)z;
                    } else {
                        newLiquidHeight = liquidHeightPlusX;
                        anotherNewLiquidHeight = liquidHeightPlusXPlusZ;
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
                    tessellator.setBrightness(liquid.getMixedBrightnessForBlock(renderer.blockAccess, newX, y, newZ));
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
    }
}