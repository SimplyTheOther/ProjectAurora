package projectaurora.world;

import java.util.List;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.biome.AuroraBiomeVariant;
import projectaurora.world.biome.AuroraBiomeVariantStorage;
import projectaurora.world.layer.AuroraGenLayerBiomeVariants;
import projectaurora.world.layer.AuroraGenLayerBiomeVariantsLake;
import projectaurora.world.layer.BaseGenLayer;

public abstract class BaseChunkManager extends WorldChunkManager {
	protected BaseGenLayer[] chunkGenLayers;
	protected BaseGenLayer[] worldLayers;
	protected static int LAYER_VARIANTS_LARGE = 1;
	protected static int LAYER_VARIANTS_SMALL = 2;
	protected static int LAYER_VARIANTS_LAKES = 3;
	protected World worldObj;
	protected BiomeCache biomeCacheAurora;
	
	@Override
	public BiomeGenBase getBiomeGenAt(int x, int z) {
		return this.biomeCacheAurora.getBiomeGenAt(x, z);
	}
	
	@Override
	public void cleanupCache() {
	    this.biomeCacheAurora.cleanupCache();
	}
	
	public AuroraBiomeVariant[] getVariantsChunkGen(AuroraBiomeVariant[] variants, int i, int k, int xSize, int zSize, BiomeGenBase[] biomeSource) {
	    return getBiomeVariantsFromLayers(variants, i, k, xSize, zSize, biomeSource, true);
  	}

	public AuroraBiomeVariant[] getBiomeVariants(AuroraBiomeVariant[] variants, int i, int k, int xSize, int zSize) {
	    return getBiomeVariantsFromLayers(variants, i, k, xSize, zSize, null, false);
	}

	private AuroraBiomeVariant[] getBiomeVariantsFromLayers(AuroraBiomeVariant[] variants, int i, int k, int xSize, int zSize, BiomeGenBase[] biomeSource, boolean isChunkGeneration) {
	    AuroraIntCache.get(this.worldObj).resetIntCache();

	    BiomeGenBase[] biomes = new BiomeGenBase[xSize * zSize];
	    if (biomeSource != null) {
	    	biomes = biomeSource;
	    } else {
	    	for (int k1 = 0; k1 < zSize; k1++) {
	    		for (int i1 = 0; i1 < xSize; i1++) {
	    			int index = i1 + k1 * xSize;
	    			biomes[index] = this.worldObj.getBiomeGenForCoords(i + i1, k + k1);
	    		}
	    	}
	    }

	    if ((variants == null) || (variants.length < xSize * zSize)) {
	      variants = new AuroraBiomeVariant[xSize * zSize];
	    }

	    BaseGenLayer[] sourceGenLayers = isChunkGeneration ? this.chunkGenLayers : this.worldLayers;
	    BaseGenLayer variantsLarge = sourceGenLayers[LAYER_VARIANTS_LARGE];
	    BaseGenLayer variantsSmall = sourceGenLayers[LAYER_VARIANTS_SMALL];
	    BaseGenLayer variantsLakes = sourceGenLayers[LAYER_VARIANTS_LAKES];

	    int[] variantsLargeInts = variantsLarge.getInts(this.worldObj, i, k, xSize, zSize);
	    int[] variantsSmallInts = variantsSmall.getInts(this.worldObj, i, k, xSize, zSize);
	    int[] variantsLakesInts = variantsLakes.getInts(this.worldObj, i, k, xSize, zSize);

	    for (int k1 = 0; k1 < zSize; k1++) {
	    	for (int i1 = 0; i1 < xSize; i1++) {
	    		int index = i1 + k1 * xSize;
	    		AuroraBiome biome = (AuroraBiome)biomes[index];
	        	AuroraBiomeVariant variant = AuroraBiomeVariant.STANDARD;

	        	int xPos = i + i1;
	        	int zPos = k + k1;
	        	if (isChunkGeneration) {
	        		xPos <<= 2;
	        		zPos <<= 2;
	        	}

	        	//TODO boolean[] flags = AuroraFixedStructures._mountainNear_structureNear(xPos, zPos);
	        	boolean mountainNear = false;//flags[0];
	        	boolean structureNear = false;//flags[1];
	        
	        	if (!mountainNear) {
	        		float variantChance = biome.variantChance;
	        		if (variantChance > 0.0F) {
	        			for (int pass = 0; pass <= 1; pass++) {
	        				List biomeVariants = pass == 0 ? biome.getBiomeVariantsLarge() : biome.getBiomeVariantsSmall();
	        				int variantCount = biomeVariants.size();
	        				if (variantCount > 0) {
	        					int[] sourceInts = pass == 0 ? variantsLargeInts : variantsSmallInts;
	        					int variantCode = sourceInts[index];

	        					float variantF = variantCode / AuroraGenLayerBiomeVariants.RANDOM_MAX;
	        					if (variantF < variantChance) {
	        						variantF /= variantChance;
	        						float increment = 1.0F / variantCount;
	        						int listIndex = MathHelper.floor_float(variantF / increment);
	        						variant = (AuroraBiomeVariant)biomeVariants.get(listIndex);
	        						break;
	        					}

	        					variant = AuroraBiomeVariant.STANDARD;
	        				}

	        			}

	        		}

	        		if (!structureNear) {
	        			if (biome.getEnableRiver()) {
	        				int variantCode = variantsLakesInts[index];
	        				if (AuroraGenLayerBiomeVariantsLake.getFlag(variantCode, 1)) {
	        					variant = AuroraBiomeVariant.LAKE;
	        				}
	        				if (AuroraGenLayerBiomeVariantsLake.getFlag(variantCode, 2)) {
	        					//if (((biome instanceof AuroraBiomeGenJungle)) && (((AuroraBiomeGenJungle)biome).hasJungleLakes())) {
	        						variant = AuroraBiomeVariant.LAKE;
	        					//}
	        				}
	        				if (AuroraGenLayerBiomeVariantsLake.getFlag(variantCode, 4)) {
	        					//if ((biome instanceof AuroraBiomeGenMangrove)) {
	        						variant = AuroraBiomeVariant.LAKE;
	        					//}
	        				}
	        			}
	        		}
	        	}

	        	variants[index] = variant;
	    	}
	    }

	    return variants;
  	}
	
	public AuroraBiomeVariant getBiomeVariantAt(int i, int k) {
	    Chunk chunk = this.worldObj.getChunkFromBlockCoords(i, k);
	    
	    if (chunk != null) {
	    	byte[] variants = AuroraBiomeVariantStorage.getChunkBiomeVariants(this.worldObj, chunk);
	    	
	    	if (variants != null) {
	    		
	    		if (variants.length == 256) {
	    			int chunkX = i & 0xF;
	    			int chunkZ = k & 0xF;
	    			byte variantID = variants[(chunkX + chunkZ * 16)];
	    			return AuroraBiomeVariant.getVariantForID(variantID);
	    		}
	    		FMLLog.severe("Found chunk biome variant array of unexpected length " + variants.length, new Object[0]);
	    	}
	    }

	    if (!this.worldObj.isRemote) {
	      return getBiomeVariants(null, i, k, 1, 1)[0];
	    }

	    return AuroraBiomeVariant.STANDARD;
    }
}