package projectaurora.compat.immersiveengineering;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import projectaurora.core.Content;
import projectaurora.core.Reference;

@SideOnly(Side.CLIENT)
public class RenderEntityNote7 extends Render {
	
	public RenderEntityNote7() {
		
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_,
			float p_76986_9_) {
		IIcon note7 = Content.note7.getIconFromDamage(0);
		
		if(note7 != null) {
			GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, (float)z);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.bindEntityTexture(entity);
            
            Tessellator tessellator = Tessellator.instance;
            
    		float minU = note7.getMinU();
            float maxU = note7.getMaxU();
            float minV = note7.getMinV();
            float maxV = note7.getMaxV();
            
            GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            tessellator.addVertexWithUV((double)(-0.5F), (double)(-0.25F), 0.0D, (double)minU, (double)maxV);
            tessellator.addVertexWithUV((double)(0.5F), (double)(-0.25F), 0.0D, (double)maxU, (double)maxV);
            tessellator.addVertexWithUV((double)(0.5F), (double)(0.75F), 0.0D, (double)maxU, (double)minV);
            tessellator.addVertexWithUV((double)(-0.5F), (double)(0.75F), 0.0D, (double)minU, (double)minV);
            tessellator.draw();
            
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		if(new ResourceLocation(Reference.modidLowerCase + ":textures/items/phone0.png") != null) {
			System.out.println("not null resourcelocation");
		}
		return new ResourceLocation(Reference.modidLowerCase + ":textures/items/phone0.png");
	}

}
