package projectaurora.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import projectaurora.world.biome.AuroraBiome;

public abstract class BaseWorldProvider extends WorldProvider {

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
	
	@SideOnly(Side.CLIENT)
	@Override
	public double getMovementFactor() {
		return 0.1;
		//TODO compared to space?
	}
	
	@Override
	public boolean shouldMapSpin(String entity, double x, double y, double z) {
		return false;
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
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isSurfaceWorld() {
		return true;
		//TODO What does 'isSurfaceWorld' even mean?
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public float getCloudHeight() {
		return this.terrainType.getCloudHeight();
		//TODO Will vulcan have clouds?
	}
	
	@Override
	protected void generateLightBrightnessTable() {
		float f = 0F;//TODO I don't even know how light brightness works.
		
		for(int i = 0; i <= 15; i++) {
			float f1 = 1 - (float)i / 15F;
			this.lightBrightnessTable[i] = (1F - f1) / (f1 * 3F + 1F) * (1F - f) + f;
		}
	}
	
	@Override
	public ChunkCoordinates getEntrancePortalLocation() {
		return new ChunkCoordinates(0, 0, 0);
	}
}