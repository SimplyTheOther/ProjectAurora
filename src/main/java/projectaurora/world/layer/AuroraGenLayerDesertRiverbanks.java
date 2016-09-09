package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;
import projectaurora.world.biome.AuroraBiome;

public class AuroraGenLayerDesertRiverbanks extends BaseGenLayer {
	private BaseGenLayer biomeLayer;
	private BaseGenLayer mapRiverLayer;

	public AuroraGenLayerDesertRiverbanks(long seed, BaseGenLayer biomes, BaseGenLayer rivers) {
		super(seed);
		this.biomeLayer = biomes;
		this.mapRiverLayer = rivers;
	}
	
	@Override
	public void initWorldGenSeed(long seed) {
	    super.initWorldGenSeed(seed);
	    this.biomeLayer.initWorldGenSeed(seed);
	    this.mapRiverLayer.initWorldGenSeed(seed);
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int[] biomes = this.biomeLayer.getInts(world, x - 2, z - 2, xSize + 3, zSize + 3);
	    int[] mapRivers = this.mapRiverLayer.getInts(world, x - 2, z - 2, xSize + 3, zSize + 3);
	    int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int k1 = 0; k1 < zSize; k1++) {
	    	for (int i1 = 0; i1 < xSize; i1++) {
	    		initChunkSeed(x + i1, z + k1);
	    		int biomeID = biomes[(i1 + 2 + (k1 + 2) * (xSize + 3))];
	    		AuroraBiome biome = AuroraBiome.auroraBiomeList[biomeID];

	    		int newBiomeID = biomeID;

	    		/*if (((biome instanceof BiomeGenDesert)) && (!(biome instanceof BiomeGenDesertFertile))) {
	    			boolean adjRiver = false;

	    			for (int i2 = -2; i2 <= 1; i2++) {
	    				for (int k2 = -2; k2 <= 1; k2++) {
	    					if ((i2 != -2) && (k2 != -2) && (i2 != 1) && (k2 == 1));
	    					int adjRiverCode = mapRivers[(i1 + 2 + i2 + (k1 + 2 + k2) * (xSize + 3))];
	    					
	    					if (adjRiverCode == 2) {
	    						adjRiver = true;
	    					}
	    				}
	    			}
	    			if (adjRiver) {
	    				newBiomeID = AuroraBiome.desertRiverbank.biomeID;
	    			}
	    		}*///TODO put deserts here
	    		ints[(i1 + k1 * xSize)] = newBiomeID;
	    	}
	    }
	    return ints;
	}
}