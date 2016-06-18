package projectaurora.world.layer;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import projectaurora.world.AuroraIntCache;
import projectaurora.world.biome.AuroraBiome;

public class AuroraGenLayerBeach extends BaseGenLayer {
	private BiomeGenBase targetBiome;

	public AuroraGenLayerBeach(long seed, BaseGenLayer layer, BiomeGenBase target) {
		super(seed);
		this.baseParent = layer;
		this.targetBiome = target;
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

	    		if ((biomeID != this.targetBiome.biomeID) && (biome.heightBaseParameter >= 0.0F)) {
	    			int biome1 = biomes[(i1 + 1 + (k1 + 1 - 1) * (xSize + 2))];
	    			int biome2 = biomes[(i1 + 1 + 1 + (k1 + 1) * (xSize + 2))];
	    			int biome3 = biomes[(i1 + 1 - 1 + (k1 + 1) * (xSize + 2))];
	    			int biome4 = biomes[(i1 + 1 + (k1 + 1 + 1) * (xSize + 2))];

	    			if ((biome1 == this.targetBiome.biomeID) || (biome2 == this.targetBiome.biomeID) || (biome3 == this.targetBiome.biomeID) || (biome4 == this.targetBiome.biomeID)) {
	    				//if (((biome instanceof AuroraBiomeGenCliff))) {
	    					//newBiomeID = AuroraBiome.beachStone.biomeID; TODO custom beach biomes
	    				//} else if (!(biome instanceof AuroraBiomeGenBeach)) {
	    					//if (nextInt(20) == 0) {
	    						//newBiomeID = AuroraBiome.beachGravel.biomeID;
    						//} else {
    							//newBiomeID = AuroraBiome.beach.biomeID;
    						//}
	    				//}
	    			}
	    		}
	    		ints[(i1 + k1 * xSize)] = newBiomeID;
	    	}
	    }
	    return ints;
	}
}