package projectaurora.world.vulcan;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import projectaurora.world.AuroraIntCache;
import projectaurora.world.BaseChunkManager;
import projectaurora.world.WorldModule;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.biome.AuroraBiomeVariant;
import projectaurora.world.biome.AuroraBiomeVariantStorage;
import projectaurora.world.layer.AuroraGenLayerBiomeVariants;
import projectaurora.world.layer.AuroraGenLayerBiomeVariantsLake;
import projectaurora.world.layer.AuroraGenLayerZoomVoronoi;
import projectaurora.world.layer.BaseGenLayer;

public class VulcanChunkManager extends BaseChunkManager {
	private static int LAYER_BIOME = 0;
	
	public VulcanChunkManager(World world) {
		this.worldObj = world;
		this.biomeCacheAurora = new BiomeCache(this);
		setupGenLayers();
	}

	private void setupGenLayers() {
		long seed = this.worldObj.getSeed() + 1954L;
	    this.chunkGenLayers = GenLayerVulcan.createWorld(seed);
	    this.worldLayers = new BaseGenLayer[this.chunkGenLayers.length];

	    BaseGenLayer biomes = this.chunkGenLayers[LAYER_BIOME];
	    this.worldLayers[LAYER_BIOME] = new AuroraGenLayerZoomVoronoi(10L, biomes);

	    BaseGenLayer variantsLarge = this.chunkGenLayers[LAYER_VARIANTS_LARGE];
	    this.worldLayers[LAYER_VARIANTS_LARGE] = new AuroraGenLayerZoomVoronoi(10L, variantsLarge);
	    BaseGenLayer variantsSmall = this.chunkGenLayers[LAYER_VARIANTS_SMALL];
	    this.worldLayers[LAYER_VARIANTS_SMALL] = new AuroraGenLayerZoomVoronoi(10L, variantsSmall);
	    BaseGenLayer variantsLakes = this.chunkGenLayers[LAYER_VARIANTS_LAKES];
	    this.worldLayers[LAYER_VARIANTS_LAKES] = new AuroraGenLayerZoomVoronoi(10L, variantsLakes);

	    for (int i = 0; i < this.worldLayers.length; i++) {
	    	this.chunkGenLayers[i].initWorldGenSeed(seed);
	    	this.worldLayers[i].initWorldGenSeed(seed);
	    }
    }
	
	@Override
	public float[] getRainfall(float[] arrayToReuse, int x, int z, int width, int length) {
		AuroraIntCache.get(this.worldObj).resetIntCache();

	    if ((arrayToReuse == null) || (arrayToReuse.length < width * length)) {
	    	arrayToReuse = new float[width * length];
	    }

	    int[] ints = this.worldLayers[LAYER_BIOME].getInts(this.worldObj, x, z, width, length);
	    
	    for (int l = 0; l < width * length; l++) {
	    	int biomeID = ints[l];
	    	float f = AuroraBiome.auroraBiomeList[biomeID].getIntRainfall() / 65536.0F;
	      
	    	if (f > 1.0F) {
	    		f = 1.0F;
	    	}
	    	arrayToReuse[l] = f;
	    }

	    return arrayToReuse;
	}

  	@SideOnly(Side.CLIENT)
  	@Override
  	public float getTemperatureAtHeight(float f, int height) {
  		return f;
  	}

  	@Override
  	public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int i, int k, int xSize, int zSize) {
  		AuroraIntCache.get(this.worldObj).resetIntCache();

	    if ((biomes == null) || (biomes.length < xSize * zSize)) {
	    	biomes = new BiomeGenBase[xSize * zSize];
	    }

	    int[] ints = this.chunkGenLayers[LAYER_BIOME].getInts(this.worldObj, i, k, xSize, zSize);
	    
	    for (int l = 0; l < xSize * zSize; l++) {
	    	biomes[l] = AuroraBiome.auroraBiomeList[ints[l]];
	    }

	    return biomes;
    }

	@Override  
  	public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomes, int i, int k, int xSize, int zSize) {
	    return getBiomeGenAt(biomes, i, k, xSize, zSize, true);
	}

	@Override  
	public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomes, int i, int k, int xSize, int zSize, boolean useCache) {
		AuroraIntCache.get(this.worldObj).resetIntCache();

	    if ((biomes == null) || (biomes.length < xSize * zSize)) {
	    	biomes = new BiomeGenBase[xSize * zSize];
	    }

	    if ((useCache) && (xSize == 16) && (zSize == 16) && ((i & 0xF) == 0) && ((k & 0xF) == 0)) {
	    	BiomeGenBase[] cachedBiomes = this.biomeCacheAurora.getCachedBiomes(i, k);
	    	System.arraycopy(cachedBiomes, 0, biomes, 0, xSize * zSize);
	    	return biomes;
	    }

	    int[] ints = this.worldLayers[LAYER_BIOME].getInts(this.worldObj, i, k, xSize, zSize);
	    for (int l = 0; l < xSize * zSize; l++) {
	    	biomes[l] = AuroraBiome.auroraBiomeList[ints[l]];
	    }
	    return biomes;
    }

	@Override
	public boolean areBiomesViable(int i, int k, int range, List list) {
	    AuroraIntCache.get(this.worldObj).resetIntCache();

	    int i1 = i - range >> 2;
	    int k1 = k - range >> 2;
	    int i2 = i + range >> 2;
	    int k2 = k + range >> 2;
	    int i3 = i2 - i1 + 1;
	    int k3 = k2 - k1 + 1;

	    int[] ints = this.chunkGenLayers[LAYER_BIOME].getInts(this.worldObj, i1, k1, i3, k3);
	    
	    for (int l = 0; l < i3 * k3; l++) {
	    	BiomeGenBase biome = AuroraBiome.auroraBiomeList[ints[l]];
	    	if (!list.contains(biome)) {
	    		return false;
	    	}
	    }

	    return true;
	  }
	
	@Override
	public ChunkPosition findBiomePosition(int i, int k, int range, List list, Random random) {
	    AuroraIntCache.get(this.worldObj).resetIntCache();

	    int i1 = i - range >> 2;
	    int k1 = k - range >> 2;
	    int i2 = i + range >> 2;
	    int k2 = k + range >> 2;
	    int i3 = i2 - i1 + 1;
	    int k3 = k2 - k1 + 1;

	    int[] ints = this.chunkGenLayers[LAYER_BIOME].getInts(this.worldObj, i1, k1, i3, k3);
	    ChunkPosition chunkpos = null;
	    int j = 0;
	    
	    for (int l = 0; l < i3 * k3; l++) {
	    	int xPos = i1 + l % i3 << 2;
	    	int zPos = k1 + l / i3 << 2;
	    	BiomeGenBase biome = AuroraBiome.auroraBiomeList[ints[l]];
	    	if ((list.contains(biome)) && ((chunkpos == null) || (random.nextInt(j + 1) == 0))) {
	    		chunkpos = new ChunkPosition(xPos, 0, zPos);
	    		j++;
	    	}
	    }

	    return chunkpos;
    }
}