package projectaurora.world;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import projectaurora.world.biome.AuroraBiome;

public abstract class BaseWorldProvider extends WorldProvider {
	public long dayLength = 24000L;
	public long cloudColor = 16777215L;
	
    /** RIP, just copied out array for sunrise/sunset colors (RGBA) */
    public float[] colorsSunriseSunset = new float[4];
	
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
				} else {
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
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isSkyColored() {
		return true;
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
	}
	
	@Override
	protected void generateLightBrightnessTable() {
		float f = 0F;
		
		for(int i = 0; i <= 15; i++) {
			float f1 = 1 - (float)i / 15F;
			this.lightBrightnessTable[i] = (1F - f1) / (f1 * 3F + 1F) * (1F - f) + f;
		}
	}
	
	@Override
	public ChunkCoordinates getEntrancePortalLocation() {
		return new ChunkCoordinates(0, 0, 0);
	}
	
	@Override
	public float calculateCelestialAngle(long par1, float par2) {
		int j = (int) (par1 % this.dayLength);
        float f1 = (j + par2) / this.dayLength - 0.25F;

        if (f1 < 0.0F) {
            ++f1;
        }

        if (f1 > 1.0F) {
            --f1;
        }

        float f2 = f1;
        f1 = 0.5F - MathHelper.cos(f1 * 3.1415927F) / 2.0F;
        return f2 + (f1 - f2) / 3.0F;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public float[] calcSunriseSunsetColors(float par1, float par2) {
        float f2 = 0.4F;
        float f3 = MathHelper.cos(par1 * (float)Math.PI * 2.0F) - 0.0F;
        float f4 = -0.0F;

        if (f3 >= f4 - f2 && f3 <= f4 + f2) {
            float f5 = (f3 - f4) / f2 * 0.5F + 0.5F;
            float f6 = 1.0F - (1.0F - MathHelper.sin(f5 * (float)Math.PI)) * 0.99F;
            f6 *= f6;
            this.colorsSunriseSunset[0] = f5 * 0.3F + 0.7F;
            this.colorsSunriseSunset[1] = f5 * f5 * 0.7F + 0.2F;
            this.colorsSunriseSunset[2] = f5 * f5 * 0.0F + 0.2F;
            this.colorsSunriseSunset[3] = f6;
            return this.colorsSunriseSunset;
        } else {
            return null;
        }
	}
}