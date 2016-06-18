package projectaurora.world.vulcan;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import projectaurora.world.WorldModule;
import projectaurora.world.biome.AuroraBiome;

public class VulcanWorldProvider extends WorldProvider {
	
	@Override
	public BiomeGenBase getBiomeGenForCoords(int x, int z) {
		if(this.worldObj.blockExists(x, 0, z)) {
			Chunk chunk = this.worldObj.getChunkFromBlockCoords(x, z);
			if(chunk != null) {
				int chunkX = x & 0xF;
				int chunkZ = z & 0xF;
				int biomeID = chunk.getBiomeArray()[(chunkZ << 4 | chunkX)] & 0xFF;
				
				if(biomeID == 255) {
					BiomeGenBase bgb = this.worldChunkMgr.getBiomeGenAt((chunk.xPosition << 4) + chunkX, (chunk.zPosition << 4) + chunkZ);
					biomeID = bgb.biomeID;
					chunk.getBiomeArray()[(chunkZ << 4 | chunkX)] = ((byte)(biomeID & 0xFF));
				}
				
				if(AuroraBiome.auroraBiomeList[biomeID] == null) {
					return AuroraBiome.auroraBiomeList[0];
				} else {//TODO how does it calculate biome?
					return AuroraBiome.auroraBiomeList[biomeID];
				}
			}
		}
		return this.worldChunkMgr.getBiomeGenAt(x, z);
	}

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
		return "DIM" + WorldModule.vulcanID;
		//TODO save folder change to name?
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public double getMovementFactor() {
		return 0.1;
		//TODO compared to space?
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
		return worldObj.getSkyColorBody(cameraEntity, partialTicks);
		//TODO Get sky colour change
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isSkyColored() {
		return true;
		//TODO Change sky colour
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean canRespawnHere() {
		return true;
		//TODO Can respawn here, yep
	}
	
	@Override
	public int getRespawnDimension(EntityPlayerMP player) {
		return WorldModule.vulcanID;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isSurfaceWorld() {
		return false;
		//TODO What does 'isSurfaceWorld' even mean?
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public float getCloudHeight() {
		return this.terrainType.getCloudHeight();
		//TODO Will vulcan have clouds?
	}
	
	@Override
	public ChunkCoordinates getEntrancePortalLocation() {
		return new ChunkCoordinates(0, 0, 0);//TODO Would Vulcan have a portal?
	}
	
	@Override
	protected void generateLightBrightnessTable() {
		float f = 0F;//TODO I don't even know how light brightness works.
		
		for(int i = 0; i <= 15; i++) {
			float f1 = 1 - (float)i / 15F;
			this.lightBrightnessTable[i] = (1F - f1) / (f1 * 3F + 1F) * (1F - f) + f;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getWelcomeMessage() {
		return StatCollector.translateToLocal("aurora.world.vulcan.enter");
		//return "Computer: Entering the gravitational well of Vulcan";
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getDepartMessage() {
		return StatCollector.translateToLocal("aurora.world.vulcan.exit");
		//return "Computer: Leaving the gravitational well of Vulcan";
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