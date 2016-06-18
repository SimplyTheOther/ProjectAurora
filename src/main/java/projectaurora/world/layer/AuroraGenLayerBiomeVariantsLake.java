package projectaurora.world.layer;

import com.google.common.math.IntMath;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerBiomeVariantsLake extends BaseGenLayer {
	public static final int FLAG_LAKE = 1;
	public static final int FLAG_JUNGLE = 2;
	public static final int FLAG_MANGROVE = 4;
	private int zoomScale;
	private int lakeFlags = 0;

	public AuroraGenLayerBiomeVariantsLake(long seed, BaseGenLayer layer, int zoom) {
		super(seed);
		this.baseParent = layer;
		this.zoomScale = IntMath.pow(2, zoom);
	}
	
	public AuroraGenLayerBiomeVariantsLake setLakeFlags(int[] flags) {
		for(int f : flags) {
			this.lakeFlags = setFlag(this.lakeFlags, f);
		}
		return this;
	}
	
	public static int setFlag(int param, int flag) {
		param |= flag;
		return param;
	}
	
	public static boolean getFlag(int param, int flag) {
		return (param & flag) == flag;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int[] baseInts = this.baseParent == null ? null : this.baseParent.getInts(world, x, z, xSize, zSize);
	    int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int k1 = 0; k1 < zSize; k1++) {
	    	for (int i1 = 0; i1 < xSize; i1++) {
	    		initChunkSeed(x + i1, z + k1);
	    		int baseInt = baseInts == null ? 0 : baseInts[(i1 + k1 * xSize)];

	    		if (getFlag(this.lakeFlags, 1)) {
	    			if (nextInt(20 * this.zoomScale * this.zoomScale * this.zoomScale) == 2) {
	    				baseInt = setFlag(baseInt, 1);
	    			}
	    		}
	    		
	    		if (getFlag(this.lakeFlags, 2)) {
	    			if (nextInt(12) == 3) {
	    				baseInt = setFlag(baseInt, 2);
	    			}
	    		}
	    		
	    		if (getFlag(this.lakeFlags, 4)) {
	    			if (nextInt(10) == 1) {
	    				baseInt = setFlag(baseInt, 4);
	    			}
	    		}
	    		
	    		ints[(i1 + k1 * xSize)] = baseInt;
	    	}
	    }
	    return ints;
	}
}