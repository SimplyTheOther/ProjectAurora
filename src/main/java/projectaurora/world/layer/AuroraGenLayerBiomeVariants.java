package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerBiomeVariants extends BaseGenLayer {
	public static int RANDOM_MAX = 10000;

	public AuroraGenLayerBiomeVariants(long seed) {
		super(seed);
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);
		
		for(int k1 = 0; k1 < zSize; k1++) {
			for(int i1 = 0; i1 < xSize; i1++) {
				initChunkSeed(x + i1, z + k1);
				ints[(i1 + k1 * xSize)] = nextInt(RANDOM_MAX);
			}
		}
		return ints;
	}
}