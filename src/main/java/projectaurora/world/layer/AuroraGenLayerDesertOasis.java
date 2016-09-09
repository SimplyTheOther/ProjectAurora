package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;
import projectaurora.world.biome.AuroraBiome;

public class AuroraGenLayerDesertOasis extends BaseGenLayer {

	public AuroraGenLayerDesertOasis(long seed, BaseGenLayer layer) {
		super(seed);
		this.baseParent = layer;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int[] biomes = this.baseParent.getInts(world, x - 1, z - 1, xSize + 2, zSize + 2);
	    int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int k1 = 0; k1 < zSize; k1++) {
	    	for (int i1 = 0; i1 < xSize; i1++) {
	    		initChunkSeed(x + i1, z + k1);
	    		int biomeID = biomes[(i1 + 1 + (k1 + 1) * (xSize + 2))];
	    		AuroraBiome biome = AuroraBiome.auroraBiomeList[biomeID];

	    		int newBiomeID = biomeID;

	    		/*if (((biome instanceof BiomeGenDesert)) && (!(biome instanceof BiomeGenDesertFertile))) {
	    			if (nextInt(200) == 0) {
	    				boolean surrounded = true;

	    				for (int i2 = -1; i2 <= 1; i2++) {
	    					for (int k2 = -1; k2 <= 1; k2++) {
	    						int adjBiomeID = biomes[(i1 + 1 + i2 + (k1 + 1 + k2) * (xSize + 2))];
	    						AuroraBiome adjBiome = AuroraBiome.auroraBiomeList[adjBiomeID];
	    						if ((!(adjBiome instanceof BiomeGenDesert)) || ((adjBiome instanceof BiomeGenDesertFertile))) {
	    							surrounded = false;
	    						}
	    					}
	    				}

	    				if (surrounded) {
	    					newBiomeID = AuroraBiome.desertOasis.biomeID;
	    				}
	    			}
	    		}*///TODO desert oasis
	    		ints[(i1 + k1 * xSize)] = newBiomeID;
	    	}
	    }
	    return ints;
	}
}