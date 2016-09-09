package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerIncludeRivers extends BaseGenLayer {
	private BaseGenLayer riverLayer;
	private BaseGenLayer mapRiverLayer;

	public AuroraGenLayerIncludeRivers(long seed, BaseGenLayer rivers, BaseGenLayer mapRivers) {
		super(seed);
		this.riverLayer = rivers;
		this.mapRiverLayer = mapRivers;
	}
	
	@Override
	public void initWorldGenSeed(long seed) {
	    super.initWorldGenSeed(seed);
	    this.riverLayer.initWorldGenSeed(seed);
	    this.mapRiverLayer.initWorldGenSeed(seed);
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int[] rivers = this.riverLayer.getInts(world, x, z, xSize, zSize);
	    int[] mapRivers = this.mapRiverLayer.getInts(world, x, z, xSize, zSize);
	    int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int k1 = 0; k1 < zSize; k1++) {
	    	for (int i1 = 0; i1 < xSize; i1++) {
	    		initChunkSeed(x + i1, z + k1);

	    		int index = i1 + k1 * xSize;
	    		int isRiver = rivers[index];
	    		int isMapRiver = mapRivers[index];
	        
	    		if (isMapRiver == 2) {
	    			ints[index] = isMapRiver;
	    		} else {
	    			ints[index] = isRiver;
	    		}
	    	}
	    }
	    return ints;
	}
}