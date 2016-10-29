package projectaurora.world.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import projectaurora.core.ClientProxy;
import projectaurora.core.Content;
import projectaurora.world.WorldModule;
import projectaurora.world.block.BlockOre;

public class BlockOreRenderHandler implements ISimpleBlockRenderingHandler {
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		Tessellator tess = Tessellator.instance;
		
		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(Blocks.stone);
		
		GL11.glRotatef(90F, 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		//Render stone, regardless of picked up crap. Ehh, draconic evolution does it. 
		tess.startDrawingQuads();
		tess.setNormal(0F, -1F, 0F);
		renderer.renderFaceYNeg(block, 0D, 0D, 0D, renderer.getBlockIcon(Blocks.stone));
		tess.draw();
		
		tess.startDrawingQuads();
		tess.setNormal(0F, 1F, 0F);
		renderer.renderFaceYPos(block, 0D, 0D, 0D, renderer.getBlockIcon(Blocks.stone));
		tess.draw();
		
		tess.startDrawingQuads();
		tess.setNormal(0F, 0F, -1F);
		renderer.renderFaceZNeg(block, 0D, 0D, 0D, renderer.getBlockIcon(Blocks.stone));
		tess.draw();
		
		tess.startDrawingQuads();
		tess.setNormal(0F, 0F, 1F);
		renderer.renderFaceZPos(block, 0D, 0D, 0D, renderer.getBlockIcon(Blocks.stone));
		tess.draw();
		
		tess.startDrawingQuads();
		tess.setNormal(-1F, 0F, 0F);
		renderer.renderFaceXNeg(block, 0D, 0D, 0D, renderer.getBlockIcon(Blocks.stone));
		tess.draw();
		
		tess.startDrawingQuads();
		tess.setNormal(1F, 0F, 0F);
		renderer.renderFaceXPos(block, 0D, 0D, 0D, renderer.getBlockIcon(Blocks.stone));
		tess.draw();
		
		//Render ore
		tess.startDrawingQuads();
		tess.setNormal(0F, -1F, 0F);
		renderer.renderFaceYNeg(block, 0D, 0D, 0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
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
		
		switch(BlockOre.pass) {
			case 0:
				if(Minecraft.getMinecraft().theWorld.provider.dimensionId == 1) {
					renderer.renderStandardBlock(Blocks.end_stone, x, y, z);
				} else if(Minecraft.getMinecraft().theWorld.provider.dimensionId == -1) {
					renderer.renderStandardBlock(Blocks.netherrack, x, y, z);
				} else if(Minecraft.getMinecraft().theWorld.provider.dimensionId == WorldModule.vulcanID) {
					renderStandardBlockWithBackgroundMetadata(Content.rock, x, y, z, 0, world, renderer);
				} else {
					renderer.renderStandardBlock(Blocks.stone, x, y, z);
				}
			default:
				renderer.renderStandardBlock(block, x, y, z);
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
		return ClientProxy.oreRenderID;
	}

	private IIcon getBlockIcon(Block block, IBlockAccess world, int x, int y, int z, int side, int meta) {
		IIcon icon = block.getIcon(side, meta);
		
		if(icon == null) {
			icon = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
		}
		
		return icon;
	}
	
    public boolean renderStandardBlockWithBackgroundMetadata(Block block, int x, int y, int z, int meta, IBlockAccess world, RenderBlocks renderer) {
        int multiplier = block.colorMultiplier(world, x, y, z);
        float red = (float)(multiplier >> 16 & 255) / 255.0F;
        float green = (float)(multiplier >> 8 & 255) / 255.0F;
        float blue = (float)(multiplier & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable) {
            float f3O = (red * 30.0F + green * 59.0F + blue * 11.0F) / 100.0F;
            float f4O = (red * 30.0F + green * 70.0F) / 100.0F;
            float f5O = (red * 30.0F + green * 70.0F) / 100.0F;
            red = f3O;
            green = f4O;
            blue = f5O;
        }
    	
        renderer.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;

        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;
        
        int l = block.getMixedBrightnessForBlock(world, x, y, z);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        if (renderer.getBlockIcon(block).getIconName().equals("grass_top")) {
            flag1 = false;
        } else if (renderer.hasOverrideBlockTexture()) {
            flag1 = false;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(world, x, y - 1, z, 0)) {
            if (renderer.renderMinY <= 0.0D) {
                --y;
            }

            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(world, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(world, x + 1, y, z);
            renderer.aoLightValueScratchXYNN = world.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = world.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = world.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPN = world.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            flag2 = world.getBlock(x + 1, y - 1, z).getCanBlockGrass();
            flag3 = world.getBlock(x - 1, y - 1, z).getCanBlockGrass();
            flag4 = world.getBlock(x, y - 1, z + 1).getCanBlockGrass();
            flag5 = world.getBlock(x, y - 1, z - 1).getCanBlockGrass();

            if (!flag5 && !flag3) {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXYNN;
            } else {
                renderer.aoLightValueScratchXYZNNN = world.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(world, x - 1, y, z - 1);
            }

            if (!flag4 && !flag3) {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXYNN;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXYNN;
            } else {
                renderer.aoLightValueScratchXYZNNP = world.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(world, x - 1, y, z + 1);
            }

            if (!flag5 && !flag2) {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXYPN;
            } else {
                renderer.aoLightValueScratchXYZPNN = world.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(world, x + 1, y, z - 1);
            }

            if (!flag4 && !flag2) {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXYPN;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXYPN;
            } else {
                renderer.aoLightValueScratchXYZPNP = world.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(world, x + 1, y, z + 1);
            }

            if (renderer.renderMinY <= 0.0D) {
                ++y;
            }

            i1 = l;

            if (renderer.renderMinY <= 0.0D || !world.getBlock(x, y - 1, z).isOpaqueCube()) {
                i1 = block.getMixedBrightnessForBlock(world, x, y - 1, z);
            }

            f7 = world.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            f6 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXYPN) / 4.0F;
            f5 = (f7 + renderer.aoLightValueScratchYZNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNN) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNN + f7 + renderer.aoLightValueScratchYZNN) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXYNN, renderer.aoBrightnessYZNP, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXYPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNN, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNN, renderer.aoBrightnessYZNN, i1);

            if (flag1) {
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
            renderer.renderFaceYNeg(block, (double)x, (double)y, (double)z, this.getBlockIcon(block, world, x, y, z, 0, meta));
            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(world, x, y + 1, z, 1)) {
            if (renderer.renderMaxY >= 1.0D) {
                ++y;
            }

            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(world, x - 1, y, z);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(world, x + 1, y, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
            renderer.aoLightValueScratchXYNP = world.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = world.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = world.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = world.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            flag2 = world.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            flag3 = world.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            flag4 = world.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            flag5 = world.getBlock(x, y + 1, z - 1).getCanBlockGrass();

            if (!flag5 && !flag3) {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXYNP;
            } else {
                renderer.aoLightValueScratchXYZNPN = world.getBlock(x - 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(world, x - 1, y, z - 1);
            }

            if (!flag5 && !flag2) {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXYPP;
            } else {
                renderer.aoLightValueScratchXYZPPN = world.getBlock(x + 1, y, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(world, x + 1, y, z - 1);
            }

            if (!flag4 && !flag3) {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXYNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXYNP;
            } else {
                renderer.aoLightValueScratchXYZNPP = world.getBlock(x - 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(world, x - 1, y, z + 1);
            }

            if (!flag4 && !flag2) {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXYPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXYPP;
            } else {
                renderer.aoLightValueScratchXYZPPP = world.getBlock(x + 1, y, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(world, x + 1, y, z + 1);
            }

            if (renderer.renderMaxY >= 1.0D) {
                --y;
            }

            i1 = l;

            if (renderer.renderMaxY >= 1.0D || !world.getBlock(x, y + 1, z).isOpaqueCube()) {
                i1 = block.getMixedBrightnessForBlock(world, x, y + 1, z);
            }

            f7 = world.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            f6 = (renderer.aoLightValueScratchXYZNPP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchYZPP + f7) / 4.0F;
            f3 = (renderer.aoLightValueScratchYZPP + f7 + renderer.aoLightValueScratchXYZPPP + renderer.aoLightValueScratchXYPP) / 4.0F;
            f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNPP, renderer.aoBrightnessXYNP, renderer.aoBrightnessYZPP, i1);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXYZPPP, renderer.aoBrightnessXYPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
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
            renderer.renderFaceYPos(block, (double)x, (double)y, (double)z, this.getBlockIcon(block, world, x, y, z, 1, meta));
            flag = true;
        }

        IIcon iicon;

        if (renderer.renderAllFaces || block.shouldSideBeRendered(world, x, y, z - 1, 2)) {
            if (renderer.renderMinZ <= 0.0D) {
                --z;
            }

            renderer.aoLightValueScratchXZNN = world.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNN = world.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPN = world.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = world.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(world, x - 1, y, z);
            renderer.aoBrightnessYZNN = block.getMixedBrightnessForBlock(world, x, y - 1, z);
            renderer.aoBrightnessYZPN = block.getMixedBrightnessForBlock(world, x, y + 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(world, x + 1, y, z);
            flag2 = world.getBlock(x + 1, y, z - 1).getCanBlockGrass();
            flag3 = world.getBlock(x - 1, y, z - 1).getCanBlockGrass();
            flag4 = world.getBlock(x, y + 1, z - 1).getCanBlockGrass();
            flag5 = world.getBlock(x, y - 1, z - 1).getCanBlockGrass();

            if (!flag3 && !flag5) {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            } else {
                renderer.aoLightValueScratchXYZNNN = world.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(world, x - 1, y - 1, z);
            }

            if (!flag3 && !flag4) {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            } else {
                renderer.aoLightValueScratchXYZNPN = world.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(world, x - 1, y + 1, z);
            }

            if (!flag2 && !flag5) {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            } else {
                renderer.aoLightValueScratchXYZPNN = world.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(world, x + 1, y - 1, z);
            }

            if (!flag2 && !flag4) {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            } else {
                renderer.aoLightValueScratchXYZPPN = world.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(world, x + 1, y + 1, z);
            }

            if (renderer.renderMinZ <= 0.0D) {
                ++z;
            }

            i1 = l;

            if (renderer.renderMinZ <= 0.0D || !world.getBlock(x, y, z - 1).isOpaqueCube()) {
                i1 = block.getMixedBrightnessForBlock(world, x, y, z - 1);
            }

            f7 = world.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchXYZNPN + f7 + renderer.aoLightValueScratchYZPN) / 4.0F;
            f4 = (f7 + renderer.aoLightValueScratchYZPN + renderer.aoLightValueScratchXZPN + renderer.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (renderer.aoLightValueScratchYZNN + f7 + renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXZPN) / 4.0F;
            f6 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXZNN + renderer.aoLightValueScratchYZNN + f7) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessYZPN, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessYZPN, renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNN, renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXZPN, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXZNN, renderer.aoBrightnessYZNN, i1);

            if (flag1) {
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
            iicon = this.getBlockIcon(block, world, x, y, z, 2, meta);
            renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture()) {
                renderer.colorRedTopLeft *= red;
                renderer.colorRedBottomLeft *= red;
                renderer.colorRedBottomRight *= red;
                renderer.colorRedTopRight *= red;
                renderer.colorGreenTopLeft *= green;
                renderer.colorGreenBottomLeft *= green;
                renderer.colorGreenBottomRight *= green;
                renderer.colorGreenTopRight *= green;
                renderer.colorBlueTopLeft *= blue;
                renderer.colorBlueBottomLeft *= blue;
                renderer.colorBlueBottomRight *= blue;
                renderer.colorBlueTopRight *= blue;
                renderer.renderFaceZNeg(block, (double)x, (double)y, (double)z, BlockGrass.getIconSideOverlay());
            }
            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(world, x, y, z + 1, 3)) {
            if (renderer.renderMaxZ >= 1.0D) {
                ++z;
            }

            renderer.aoLightValueScratchXZNP = world.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = world.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZNP = world.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchYZPP = world.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(world, x - 1, y, z);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(world, x + 1, y, z);
            renderer.aoBrightnessYZNP = block.getMixedBrightnessForBlock(world, x, y - 1, z);
            renderer.aoBrightnessYZPP = block.getMixedBrightnessForBlock(world, x, y + 1, z);
            flag2 = world.getBlock(x + 1, y, z + 1).getCanBlockGrass();
            flag3 = world.getBlock(x - 1, y, z + 1).getCanBlockGrass();
            flag4 = world.getBlock(x, y + 1, z + 1).getCanBlockGrass();
            flag5 = world.getBlock(x, y - 1, z + 1).getCanBlockGrass();

            if (!flag3 && !flag5) {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            } else {
                renderer.aoLightValueScratchXYZNNP = world.getBlock(x - 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(world, x - 1, y - 1, z);
            }

            if (!flag3 && !flag4) {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            } else {
                renderer.aoLightValueScratchXYZNPP = world.getBlock(x - 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(world, x - 1, y + 1, z);
            }

            if (!flag2 && !flag5) {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            } else {
                renderer.aoLightValueScratchXYZPNP = world.getBlock(x + 1, y - 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(world, x + 1, y - 1, z);
            }

            if (!flag2 && !flag4) {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            } else {
                renderer.aoLightValueScratchXYZPPP = world.getBlock(x + 1, y + 1, z).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(world, x + 1, y + 1, z);
            }

            if (renderer.renderMaxZ >= 1.0D) {
                --z;
            }

            i1 = l;

            if (renderer.renderMaxZ >= 1.0D || !world.getBlock(x, y, z + 1).isOpaqueCube()) {
                i1 = block.getMixedBrightnessForBlock(world, x, y, z + 1);
            }

            f7 = world.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYZNPP + f7 + renderer.aoLightValueScratchYZPP) / 4.0F;
            f6 = (f7 + renderer.aoLightValueScratchYZPP + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            f5 = (renderer.aoLightValueScratchYZNP + f7 + renderer.aoLightValueScratchXYZPNP + renderer.aoLightValueScratchXZPP) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYZNNP + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchYZNP + f7) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYZNPP, renderer.aoBrightnessYZPP, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessYZPP, renderer.aoBrightnessXZPP, renderer.aoBrightnessXYZPPP, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessYZNP, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, renderer.aoBrightnessYZNP, i1);

            if (flag1) {
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
            iicon = this.getBlockIcon(block, world, x, y, z, 3, meta);
            renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, this.getBlockIcon(block, world, x, y, z, 3, meta));

            if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture()) {
                renderer.colorRedTopLeft *= red;
                renderer.colorRedBottomLeft *= red;
                renderer.colorRedBottomRight *= red;
                renderer.colorRedTopRight *= red;
                renderer.colorGreenTopLeft *= green;
                renderer.colorGreenBottomLeft *= green;
                renderer.colorGreenBottomRight *= green;
                renderer.colorGreenTopRight *= green;
                renderer.colorBlueTopLeft *= blue;
                renderer.colorBlueBottomLeft *= blue;
                renderer.colorBlueBottomRight *= blue;
                renderer.colorBlueTopRight *= blue;
                renderer.renderFaceZPos(block, (double)x, (double)y, (double)z, BlockGrass.getIconSideOverlay());
            }
            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(world, x - 1, y, z, 4)) {
            if (renderer.renderMinX <= 0.0D) {
                --x;
            }

            renderer.aoLightValueScratchXYNN = world.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNN = world.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZNP = world.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYNP = world.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYNN = block.getMixedBrightnessForBlock(world, x, y - 1, z);
            renderer.aoBrightnessXZNN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
            renderer.aoBrightnessXZNP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
            renderer.aoBrightnessXYNP = block.getMixedBrightnessForBlock(world, x, y + 1, z);
            flag2 = world.getBlock(x - 1, y + 1, z).getCanBlockGrass();
            flag3 = world.getBlock(x - 1, y - 1, z).getCanBlockGrass();
            flag4 = world.getBlock(x - 1, y, z - 1).getCanBlockGrass();
            flag5 = world.getBlock(x - 1, y, z + 1).getCanBlockGrass();

            if (!flag4 && !flag3) {
                renderer.aoLightValueScratchXYZNNN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNNN = renderer.aoBrightnessXZNN;
            } else {
                renderer.aoLightValueScratchXYZNNN = world.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(world, x, y - 1, z - 1);
            }

            if (!flag5 && !flag3) {
                renderer.aoLightValueScratchXYZNNP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNNP = renderer.aoBrightnessXZNP;
            } else {
                renderer.aoLightValueScratchXYZNNP = world.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(world, x, y - 1, z + 1);
            }

            if (!flag4 && !flag2) {
                renderer.aoLightValueScratchXYZNPN = renderer.aoLightValueScratchXZNN;
                renderer.aoBrightnessXYZNPN = renderer.aoBrightnessXZNN;
            } else {
                renderer.aoLightValueScratchXYZNPN = world.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(world, x, y + 1, z - 1);
            }

            if (!flag5 && !flag2) {
                renderer.aoLightValueScratchXYZNPP = renderer.aoLightValueScratchXZNP;
                renderer.aoBrightnessXYZNPP = renderer.aoBrightnessXZNP;
            } else {
                renderer.aoLightValueScratchXYZNPP = world.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(world, x, y + 1, z + 1);
            }

            if (renderer.renderMinX <= 0.0D) {
                ++x;
            }

            i1 = l;

            if (renderer.renderMinX <= 0.0D || !world.getBlock(x - 1, y, z).isOpaqueCube()) {
                i1 = block.getMixedBrightnessForBlock(world, x - 1, y, z);
            }

            f7 = world.getBlock(x - 1, y, z).getAmbientOcclusionLightValue();
            f6 = (renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXYZNNP + f7 + renderer.aoLightValueScratchXZNP) / 4.0F;
            f3 = (f7 + renderer.aoLightValueScratchXZNP + renderer.aoLightValueScratchXYNP + renderer.aoLightValueScratchXYZNPP) / 4.0F;
            f4 = (renderer.aoLightValueScratchXZNN + f7 + renderer.aoLightValueScratchXYZNPN + renderer.aoLightValueScratchXYNP) / 4.0F;
            f5 = (renderer.aoLightValueScratchXYZNNN + renderer.aoLightValueScratchXYNN + renderer.aoLightValueScratchXZNN + f7) / 4.0F;
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXYNN, renderer.aoBrightnessXYZNNP, renderer.aoBrightnessXZNP, i1);
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNP, renderer.aoBrightnessXYNP, renderer.aoBrightnessXYZNPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXZNN, renderer.aoBrightnessXYZNPN, renderer.aoBrightnessXYNP, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXYZNNN, renderer.aoBrightnessXYNN, renderer.aoBrightnessXZNN, i1);

            if (flag1) {
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
            iicon = this.getBlockIcon(block, world, x, y, z, 4, meta);
            renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture()) {
                renderer.colorRedTopLeft *= red;
                renderer.colorRedBottomLeft *= red;
                renderer.colorRedBottomRight *= red;
                renderer.colorRedTopRight *= red;
                renderer.colorGreenTopLeft *= green;
                renderer.colorGreenBottomLeft *= green;
                renderer.colorGreenBottomRight *= green;
                renderer.colorGreenTopRight *= green;
                renderer.colorBlueTopLeft *= blue;
                renderer.colorBlueBottomLeft *= blue;
                renderer.colorBlueBottomRight *= blue;
                renderer.colorBlueTopRight *= blue;
                renderer.renderFaceXNeg(block, (double)x, (double)y, (double)z, BlockGrass.getIconSideOverlay());
            }
            flag = true;
        }

        if (renderer.renderAllFaces || block.shouldSideBeRendered(world, x + 1, y, z, 5)) {
            if (renderer.renderMaxX >= 1.0D) {
                ++x;
            }

            renderer.aoLightValueScratchXYPN = world.getBlock(x, y - 1, z).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPN = world.getBlock(x, y, z - 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXZPP = world.getBlock(x, y, z + 1).getAmbientOcclusionLightValue();
            renderer.aoLightValueScratchXYPP = world.getBlock(x, y + 1, z).getAmbientOcclusionLightValue();
            renderer.aoBrightnessXYPN = block.getMixedBrightnessForBlock(world, x, y - 1, z);
            renderer.aoBrightnessXZPN = block.getMixedBrightnessForBlock(world, x, y, z - 1);
            renderer.aoBrightnessXZPP = block.getMixedBrightnessForBlock(world, x, y, z + 1);
            renderer.aoBrightnessXYPP = block.getMixedBrightnessForBlock(world, x, y + 1, z);
            flag2 = world.getBlock(x + 1, y + 1, z).getCanBlockGrass();
            flag3 = world.getBlock(x + 1, y - 1, z).getCanBlockGrass();
            flag4 = world.getBlock(x + 1, y, z + 1).getCanBlockGrass();
            flag5 = world.getBlock(x + 1, y, z - 1).getCanBlockGrass();

            if (!flag3 && !flag5) {
                renderer.aoLightValueScratchXYZPNN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPNN = renderer.aoBrightnessXZPN;
            } else {
                renderer.aoLightValueScratchXYZPNN = world.getBlock(x, y - 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(world, x, y - 1, z - 1);
            }

            if (!flag3 && !flag4) {
                renderer.aoLightValueScratchXYZPNP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPNP = renderer.aoBrightnessXZPP;
            } else {
                renderer.aoLightValueScratchXYZPNP = world.getBlock(x, y - 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(world, x, y - 1, z + 1);
            }

            if (!flag2 && !flag5) {
                renderer.aoLightValueScratchXYZPPN = renderer.aoLightValueScratchXZPN;
                renderer.aoBrightnessXYZPPN = renderer.aoBrightnessXZPN;
            } else {
                renderer.aoLightValueScratchXYZPPN = world.getBlock(x, y + 1, z - 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(world, x, y + 1, z - 1);
            }

            if (!flag2 && !flag4) {
                renderer.aoLightValueScratchXYZPPP = renderer.aoLightValueScratchXZPP;
                renderer.aoBrightnessXYZPPP = renderer.aoBrightnessXZPP;
            } else {
                renderer.aoLightValueScratchXYZPPP = world.getBlock(x, y + 1, z + 1).getAmbientOcclusionLightValue();
                renderer.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(world, x, y + 1, z + 1);
            }

            if (renderer.renderMaxX >= 1.0D) {
                --x;
            }

            i1 = l;

            if (renderer.renderMaxX >= 1.0D || !world.getBlock(x + 1, y, z).isOpaqueCube()) {
                i1 = block.getMixedBrightnessForBlock(world, x + 1, y, z);
            }

            f7 = world.getBlock(x + 1, y, z).getAmbientOcclusionLightValue();
            f3 = (renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXYZPNP + f7 + renderer.aoLightValueScratchXZPP) / 4.0F;
            f4 = (renderer.aoLightValueScratchXYZPNN + renderer.aoLightValueScratchXYPN + renderer.aoLightValueScratchXZPN + f7) / 4.0F;
            f5 = (renderer.aoLightValueScratchXZPN + f7 + renderer.aoLightValueScratchXYZPPN + renderer.aoLightValueScratchXYPP) / 4.0F;
            f6 = (f7 + renderer.aoLightValueScratchXZPP + renderer.aoLightValueScratchXYPP + renderer.aoLightValueScratchXYZPPP) / 4.0F;
            renderer.brightnessTopLeft = renderer.getAoBrightness(renderer.aoBrightnessXYPN, renderer.aoBrightnessXYZPNP, renderer.aoBrightnessXZPP, i1);
            renderer.brightnessTopRight = renderer.getAoBrightness(renderer.aoBrightnessXZPP, renderer.aoBrightnessXYPP, renderer.aoBrightnessXYZPPP, i1);
            renderer.brightnessBottomRight = renderer.getAoBrightness(renderer.aoBrightnessXZPN, renderer.aoBrightnessXYZPPN, renderer.aoBrightnessXYPP, i1);
            renderer.brightnessBottomLeft = renderer.getAoBrightness(renderer.aoBrightnessXYZPNN, renderer.aoBrightnessXYPN, renderer.aoBrightnessXZPN, i1);

            if (flag1) {
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
            iicon = this.getBlockIcon(block, world, x, y, z, 5, meta);
            renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, iicon);

            if (renderer.fancyGrass && iicon.getIconName().equals("grass_side") && !renderer.hasOverrideBlockTexture()) {
                renderer.colorRedTopLeft *= red;
                renderer.colorRedBottomLeft *= red;
                renderer.colorRedBottomRight *= red;
                renderer.colorRedTopRight *= red;
                renderer.colorGreenTopLeft *= green;
                renderer.colorGreenBottomLeft *= green;
                renderer.colorGreenBottomRight *= green;
                renderer.colorGreenTopRight *= green;
                renderer.colorBlueTopLeft *= blue;
                renderer.colorBlueBottomLeft *= blue;
                renderer.colorBlueBottomRight *= blue;
                renderer.colorBlueTopRight *= blue;
                renderer.renderFaceXPos(block, (double)x, (double)y, (double)z, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        renderer.enableAO = false;
        return flag;
    }
}