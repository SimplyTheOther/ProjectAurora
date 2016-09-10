package projectaurora.world.vulcan;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;
import projectaurora.core.Reference;

public class VulcanSkyRenderer extends IRenderHandler {
	//NOTES: Sun = 10 times larger in this, 100 in real life
	//No moon
	//20 hour year...
	//Basically no atmosphere
	private static final ResourceLocation sunTexture = new ResourceLocation(Reference.modidLowerCase, "textures/environment/vulcanSun.png");
	private static final ResourceLocation moonTexture = new ResourceLocation(Reference.modidLowerCase, "textures/environment/blank.png");

	public int starGLCallList = GLAllocation.generateDisplayLists(3);
	public int glSkyList;
	public int glSkyList2;
	
	public VulcanSkyRenderer() {
		GL11.glPushMatrix();
        GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
        this.renderStars();
        GL11.glEndList();
        GL11.glPopMatrix();
        final Tessellator tessellator = Tessellator.instance;
        this.glSkyList = this.starGLCallList + 1;
        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
        final byte byte2 = 64;
        final int i = 256 / byte2 + 2;
        float f = 16F;

        for (int j = -byte2 * i; j <= byte2 * i; j += byte2) {
            for (int l = -byte2 * i; l <= byte2 * i; l += byte2) {
                tessellator.startDrawingQuads();
                tessellator.addVertex(j + 0, f, l + 0);
                tessellator.addVertex(j + byte2, f, l + 0);
                tessellator.addVertex(j + byte2, f, l + byte2);
                tessellator.addVertex(j + 0, f, l + byte2);
                tessellator.draw();
            }
        }

        GL11.glEndList();
        this.glSkyList2 = this.starGLCallList + 2;
        GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
        f = -16F;
        tessellator.startDrawingQuads();

        for (int k = -byte2 * i; k <= byte2 * i; k += byte2) {
            for (int i1 = -byte2 * i; i1 <= byte2 * i; i1 += byte2) {
                tessellator.addVertex(k + byte2, f, i1 + 0);
                tessellator.addVertex(k + 0, f, i1 + 0);
                tessellator.addVertex(k + 0, f, i1 + byte2);
                tessellator.addVertex(k + byte2, f, i1 + byte2);
            }
        }

        tessellator.draw();
        GL11.glEndList();
	}

	private void renderStars() {
		final Random var1 = new Random(10842L);
        final Tessellator var2 = Tessellator.instance;
        var2.startDrawingQuads();

        for (int var3 = 0; var3 < (Minecraft.getMinecraft().isFancyGraphicsEnabled() ? 35000 : 6000); ++var3) {
            double var4 = var1.nextFloat() * 2.0F - 1.0F;
            double var6 = var1.nextFloat() * 2.0F - 1.0F;
            double var8 = var1.nextFloat() * 2.0F - 1.0F;
            final double var10 = 0.08F + var1.nextFloat() * 0.07F;
            double var12 = var4 * var4 + var6 * var6 + var8 * var8;

            if (var12 < 1.0D && var12 > 0.01D) {
                var12 = 1.0D / Math.sqrt(var12);
                var4 *= var12;
                var6 *= var12;
                var8 *= var12;
                final double pX = var4 * (Minecraft.getMinecraft().isFancyGraphicsEnabled() ? var1.nextDouble() * 75D + 65D : 80.0D);
                final double pY = var6 * (Minecraft.getMinecraft().isFancyGraphicsEnabled() ? var1.nextDouble() * 75D + 65D : 80.0D);
                final double pZ = var8 * (Minecraft.getMinecraft().isFancyGraphicsEnabled() ? var1.nextDouble() * 75D + 65D : 80.0D);
                final double var20 = Math.atan2(var4, var8);
                final double var22 = Math.sin(var20);
                final double var24 = Math.cos(var20);
                final double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
                final double var28 = Math.sin(var26);
                final double var30 = Math.cos(var26);
                final double var32 = var1.nextDouble() * Math.PI * 2.0D;
                final double var34 = Math.sin(var32);
                final double var36 = Math.cos(var32);

                for (int i = 0; i < 4; ++i) {
                    final double i1 = ((i & 2) - 1) * var10;
                    final double i2 = ((i + 1 & 2) - 1) * var10;
                    final double var47 = i1 * var36 - i2 * var34;
                    final double var49 = i2 * var36 + i1 * var34;
                    final double var55 = -var47 * var30;
                    final double dX = var55 * var22 - var49 * var24;
                    final double dZ = var49 * var22 + var55 * var24;
                    final double dY = var47 * var28;
                    var2.addVertex(pX + dX, pY + dY, pZ + dZ);
                }
            }
        }
        var2.draw();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		Vec3 vec3 = world.getSkyColor(mc.renderViewEntity, partialTicks);
		float f1 = (float) vec3.xCoord;
		float f2 = (float) vec3.yCoord;
		float f3 = (float) vec3.zCoord;
		float f4;
		
		if (mc.gameSettings.anaglyph) {
			float f5 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float f6 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			f4 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = f5;
			f2 = f6;
			f3 = f4;
		}
		
		GL11.glColor3f(f1, f2, f3);
		Tessellator tessellator1 = Tessellator.instance;
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glColor3f(f1, f2, f3);
		GL11.glCallList(this.glSkyList);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		RenderHelper.disableStandardItemLighting();
		float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
		float f7;
		float f8;
		float f9;
		float f10;
		
		if (afloat != null) {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glPushMatrix();
			GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
			f4 = afloat[0];
			f7 = afloat[1];
			f8 = afloat[2];
			float f11;
			
			if (mc.gameSettings.anaglyph) {
				f9 = (f4 * 30.0F + f7 * 59.0F + f8 * 11.0F) / 100.0F;
				f10 = (f4 * 30.0F + f7 * 70.0F) / 100.0F;
				f11 = (f4 * 30.0F + f8 * 70.0F) / 100.0F;
				f4 = f9;
				f7 = f10;
				f8 = f11;
			}
			
			tessellator1.startDrawing(6);
			tessellator1.setColorRGBA_F(f4, f7, f8, afloat[3]);
			tessellator1.addVertex(0.0D, 100.0D, 0.0D);
			byte b0 = 16;
			tessellator1.setColorRGBA_F(afloat[0], afloat[1], afloat[2], 0.0F);
			
			for (int j = 0; j <= b0; ++j) {
				f11 = (float) j * (float) Math.PI * 2.0F / (float) b0;
				float f12 = MathHelper.sin(f11);
				float f13 = MathHelper.cos(f11);
				tessellator1.addVertex((double) (f12 * 120.0F), (double) (f13 * 120.0F), (double) (-f13 * 40.0F * afloat[3]));
			}
			
			tessellator1.draw();
			GL11.glPopMatrix();
			GL11.glShadeModel(GL11.GL_FLAT);
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glPushMatrix();
		f4 = 1.0F - world.getRainStrength(partialTicks);
		f7 = 0.0F;
		f8 = 0.0F;
		f9 = 0.0F;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, f4);
		GL11.glTranslatef(f7, f8, f9);
		GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
		
		f10 = 75.0F; // Size of sun from center
		mc.renderEngine.bindTexture(sunTexture); 
		
		tessellator1.startDrawingQuads();
		tessellator1.addVertexWithUV((double) (-f10), 100.0D, (double) (-f10), 0.0D, 0.0D);
		tessellator1.addVertexWithUV((double) f10, 100.0D, (double) (-f10), 1.0D, 0.0D);
		tessellator1.addVertexWithUV((double) f10, 100.0D, (double) f10, 1.0D, 1.0D);
		tessellator1.addVertexWithUV((double) (-f10), 100.0D, (double) f10, 0.0D, 1.0D);
		
		tessellator1.draw(); // Draw sun
		f10 = 15.0F; // Size of moon from center
		mc.renderEngine.bindTexture(moonTexture);
		
		int k = world.getMoonPhase();
		int l = k % 4;
		int i1 = k / 4 % 2;
		float f14 = (float) (l + 0) / 4.0F;
		float f15 = (float) (i1 + 0) / 2.0F;
		float f16 = (float) (l + 1) / 4.0F;
		float f17 = (float) (i1 + 1) / 2.0F;
		tessellator1.startDrawingQuads();
		tessellator1.addVertexWithUV((double) (-f10), -100.0D, (double) f10, (double) f16, (double) f17);
		tessellator1.addVertexWithUV((double) f10, -100.0D, (double) f10, (double) f14, (double) f17);
		tessellator1.addVertexWithUV((double) f10, -100.0D, (double) (-f10), (double) f14, (double) f15);
		tessellator1.addVertexWithUV((double) (-f10), -100.0D, (double) (-f10), (double) f16, (double) f15);
		tessellator1.draw();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		float f18 = world.getStarBrightness(partialTicks) * f4;
		
		if (f18 > 0.0F) {
			GL11.glColor4f(f18, f18, f18, f18);
			GL11.glCallList(this.starGLCallList);
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor3f(0.0F, 0.0F, 0.0F);
		double d0 = mc.thePlayer.getPosition(partialTicks).yCoord - world.getHorizon();
		
		if (d0 < 0.0D) {
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 12.0F, 0.0F);
			GL11.glCallList(this.glSkyList2);
			GL11.glPopMatrix();
			f8 = 1.0F;
			f9 = -((float) (d0 + 65.0D));
			f10 = -f8;
			tessellator1.startDrawingQuads();
			tessellator1.setColorRGBA_I(0, 255);
			tessellator1.addVertex((double) (-f8), (double) f9, (double) f8);
			tessellator1.addVertex((double) f8, (double) f9, (double) f8);
			tessellator1.addVertex((double) f8, (double) f10, (double) f8);
			tessellator1.addVertex((double) (-f8), (double) f10, (double) f8);
			tessellator1.addVertex((double) (-f8), (double) f10, (double) (-f8));
			tessellator1.addVertex((double) f8, (double) f10, (double) (-f8));
			tessellator1.addVertex((double) f8, (double) f9, (double) (-f8));
			tessellator1.addVertex((double) (-f8), (double) f9, (double) (-f8));
			tessellator1.addVertex((double) f8, (double) f10, (double) (-f8));
			tessellator1.addVertex((double) f8, (double) f10, (double) f8);
			tessellator1.addVertex((double) f8, (double) f9, (double) f8);
			tessellator1.addVertex((double) f8, (double) f9, (double) (-f8));
			tessellator1.addVertex((double) (-f8), (double) f9, (double) (-f8));
			tessellator1.addVertex((double) (-f8), (double) f9, (double) f8);
			tessellator1.addVertex((double) (-f8), (double) f10, (double) f8);
			tessellator1.addVertex((double) (-f8), (double) f10, (double) (-f8));
			tessellator1.addVertex((double) (-f8), (double) f10, (double) (-f8));
			tessellator1.addVertex((double) (-f8), (double) f10, (double) f8);
			tessellator1.addVertex((double) f8, (double) f10, (double) f8);
			tessellator1.addVertex((double) f8, (double) f10, (double) (-f8));
			tessellator1.draw();
		}
		
		if (world.provider.isSkyColored()) {
			GL11.glColor3f(0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
		} else {
			GL11.glColor3f(f1, f2, f3);
		}
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, -((float) (d0 - 16.0D)), 0.0F);
		GL11.glCallList(this.glSkyList2);
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(true);
	}
}