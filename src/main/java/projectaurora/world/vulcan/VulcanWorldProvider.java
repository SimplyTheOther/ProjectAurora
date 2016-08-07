package projectaurora.world.vulcan;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import projectaurora.world.BaseWorldProvider;
import projectaurora.world.WorldModule;

public class VulcanWorldProvider extends BaseWorldProvider {

	@Override
	public String getDimensionName() {
		return "Vulcan";
	}
	
	@Override
	public IChunkProvider createChunkGenerator() {
		return new VulcanChunkProvider(this.worldObj, this.worldObj.getSeed());
		// TODO chunk generator/provider change?
	}
	
	@Override
	public void registerWorldChunkManager() {
		this.worldChunkMgr = new VulcanChunkManager(this.worldObj);
		// TODO world chunk manager change?
	}
	
	@Override
	public String getSaveFolder() {
		return "DIMVulcan";
	}
	
	@Override
	public int getRespawnDimension(EntityPlayerMP player) {
		return WorldModule.vulcanID;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getWelcomeMessage() {
		return StatCollector.translateToLocal("aurora.world.vulcan.enter");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getDepartMessage() {
		return StatCollector.translateToLocal("aurora.world.vulcan.exit");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer() {//TODO custom sky renderer?
		//return new SkyRenderer();
		return super.getSkyRenderer();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getCloudRenderer() {//TODO Will vulcan have clouds? Nope?
		//return new NoCloudRenderer();
		return super.getCloudRenderer();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getWeatherRenderer() {
		//return new NoWeatherRenderer();
		return super.getWeatherRenderer();
		//TODO Will Vulcan have weather?
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Vec3 drawClouds(float partialTicks) {
		return super.drawClouds(partialTicks);
		//TODO Will vulcan have clouds?
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Vec3 getFogColor(float par1, float par2) {
		float f2 = MathHelper.cos(par1 * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        float f3 = 0.7529412F;
        float f4 = 0.84705883F;
        float f5 = 1.0F;
        f3 *= f2 * 0.94F + 0.06F;
        f4 *= f2 * 0.94F + 0.06F;
        f5 *= f2 * 0.91F + 0.09F;
        return Vec3.createVectorHelper((double)f3, (double)f4, (double)f5);
        //TODO Will vulcan have fog?
	}
}