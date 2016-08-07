package projectaurora.world.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import projectaurora.core.ClientProxy;
import projectaurora.world.block.BlockPlant;

public class BlockPlantRenderHandler implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
		
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
				}
		}
		
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.plantRenderID;
	}
	
    public boolean renderBlockAlgaeGlow(Block block, int x, int y, int z, IBlockAccess world, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
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
        
        tessellator.setBrightness(15728848/*block.getMixedBrightnessForBlock(world, x, y + 1, z)*/);
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
        return true;
    }
}