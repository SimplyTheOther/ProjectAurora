package projectaurora.world.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import projectaurora.core.ClientProxy;
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
		System.out.println("Called renderWorldBlock");
		if(this.getRenderId() != modelId) {
			System.out.println("Returned false?");
			return false;
		}
		
		switch(BlockOre.pass) {
			case 0:
				if(Minecraft.getMinecraft().theWorld.provider.dimensionId == 1) {
					renderer.renderStandardBlock(Blocks.end_stone, x, y, z);
				} else if(Minecraft.getMinecraft().theWorld.provider.dimensionId == -1) {
					renderer.renderStandardBlock(Blocks.netherrack, x, y, z);
				} else {
					renderer.renderStandardBlock(Blocks.stone, x, y, z);
				}
			default:
				renderer.renderStandardBlock(block, x, y, z);
				System.out.println("Rendered overlay");
		}
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
}