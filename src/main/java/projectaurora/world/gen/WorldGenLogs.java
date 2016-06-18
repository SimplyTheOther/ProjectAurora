package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenLogs extends WorldGenerator {
	
	public WorldGenLogs() {
		super(false);
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		if(!isSuitablePositionForLog(world, x, y, z)) {
			return false;
		}
		
		int logType = rand.nextInt(5);
		if(logType == 0) {
			int length = 2 + rand.nextInt(6);
			for(int i1 = x; (i1 < x + length) && (isSuitablePositionForLog(world, i1, y, z)); i1++) {
				setBlockAndNotifyAdequately(world, i1, y, z, Blocks.log, 4);//TODO custom logs?
				world.getBlock(i1, y - 1, z).onPlantGrow(world, i1, y - 1, z, i1, y, z);
			}
			return true;
		}
		
		if(logType == 1) {
			int length = 2 + rand.nextInt(6);
			for(int k1 = z; (k1 < z + length) && (isSuitablePositionForLog(world, x, y, k1)); k1++) {
				setBlockAndNotifyAdequately(world, x, y, k1, Blocks.log, 8);//TODO custom logs?
				world.getBlock(x, y - 1, k1).onPlantGrow(world, x, y - 1, k1, x, y, k1);
			}
			return true;
		}
		
		setBlockAndNotifyAdequately(world, x, y, z, Blocks.log, 0); //TODO custom logs?
		world.getBlock(x, y - 1, z).onPlantGrow(world, x, y - 1, z, x, y, z);
		return true;
	}

	private boolean isSuitablePositionForLog(World world, int x, int y, int z) {
		if(!world.getBlock(x, y - 1, z).canSustainPlant(world, x, y, z, ForgeDirection.UP, (IPlantable)Blocks.sapling)) {//TODO sapling not plantable necessarily
			return false;
		} else if(world.getBlock(x, y, z).isReplaceable(world, x, y, z)) {
			return true;
		}
		return false;
	}
}