package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerBiomeSubTypes extends BaseGenLayer {
	private BaseGenLayer biomeLayer;
	private BaseGenLayer variantsLayer;
	
	public AuroraGenLayerBiomeSubTypes(long seed, BaseGenLayer layer, BaseGenLayer subtypes) {
		super(seed);
		this.biomeLayer = layer;
		this.variantsLayer = subtypes;
	}
	
	@Override
	public void initWorldGenSeed(long seed) {
		this.biomeLayer.initWorldGenSeed(seed);
		this.variantsLayer.initWorldGenSeed(seed);
		super.initWorldGenSeed(seed);
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
	    int[] biomes = this.biomeLayer.getInts(world, x, z, xSize, zSize);
	    int[] variants = this.variantsLayer.getInts(world, x, z, xSize, zSize);
	    int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int k1 = 0; k1 < zSize; k1++) {
	    	for (int i1 = 0; i1 < xSize; i1++) {
	    		initChunkSeed(x + i1, z + k1);
	    		int biome = biomes[(i1 + k1 * xSize)];
	    		int variant = variants[(i1 + k1 * xSize)];

	    		int newBiome = biome;
	        
	    		/*if ((biome == AuroraBiome.ontarine.biomeID) && (variant < 15) && (variant != 0)) { TODO custom genlayer biome replacements
	    			newBiome = AuroraBiome.ontarineCoral.biomeID;
	    		} else if ((biome == AuroraBiome.ocean.biomeID) && (variant < 2)) {
	    			newBiome = AuroraBiome.island.biomeID;
	    		}*/

	    		if (newBiome != biome) {
	    			ints[(i1 + k1 * xSize)] = newBiome;
	    		} else {
	    			ints[(i1 + k1 * xSize)] = biome;
	    		}
	    	}
	    }
	    return ints;
	}
}