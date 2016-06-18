package projectaurora.world.layer;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.World;

public abstract class BaseGenLayer extends GenLayer {
	
	protected BaseGenLayer baseParent;

	public BaseGenLayer(long seed) {
		super(seed);
	}
	
	@Override
	public void initWorldGenSeed(long seed) {
		super.initWorldGenSeed(seed);
		
		if(this.baseParent != null) {
			this.baseParent.initWorldGenSeed(seed);
		}
	}
	
	@Override
	public final int[] getInts(int x, int z, int xSize, int zSize) {
		throw new RuntimeException("You screwed up, Jerry!");
	}

	public abstract int[] getInts(World world, int x, int z, int xSize, int zSize);
}