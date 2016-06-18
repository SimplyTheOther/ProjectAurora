package projectaurora.world.layer;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;

public class AuroraGenLayerRiverInit extends BaseGenLayer {

	public AuroraGenLayerRiverInit(long seed) {
		super(seed);
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);
		
		for(int k1 = 0; k1 < zSize; k1++) {
			for(int i1 = 0; i1 < xSize; i1++) {
				initChunkSeed(x + i1, z + k1);
				//ints[(i1 + i1 * xSize)] = (2 + nextInt(299999));
				/*if(ints[i1 + i1 * xSize] > 0) {
					ints[i1 + i1 * xSize] = this.nextInt(299999) + 2;
				} else {
					ints[i1 + i1 * xSize] = 0;//TODO make the river i1nit work properly
				}*/
				ints[i1 + k1 * xSize] = 0;
                //ints[i1 + i1 * xSize] = i1nts[i1 + i1 * xSize] > 0 ? this.nextInt(299999) + 2 : 0;
			}
		}
		return ints;
	}
}
