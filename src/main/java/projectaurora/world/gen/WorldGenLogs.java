package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenLogs extends WorldGenerator {
	private Block log;
	private int logMeta;
	private boolean isVanillaLog = false;
	
	public WorldGenLogs(Block logBlock, int logBlockMeta) {
		super(false);
		this.log = logBlock;
		this.logMeta = logBlockMeta;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		if(log instanceof BlockLog) {
			this.isVanillaLog = true;
		}
		
		if(!isSuitablePositionForLog(world, x, y, z)) {
			return false;
		}
		
		int logType = rand.nextInt(5);
		
		if(logType == 0) {
			int length = 2 + rand.nextInt(6);
			
			for(int i1 = x; (i1 < x + length) && (isSuitablePositionForLog(world, i1, y, z)); i1++) {
				if(isVanillaLog) {
					setBlockAndNotifyAdequately(world, i1, y, z, this.log, 4);
				} else {
					setBlockAndNotifyAdequately(world, i1, y, z, this.log, this.logMeta);
				}

				world.getBlock(i1, y - 1, z).onPlantGrow(world, i1, y - 1, z, i1, y, z);
			}
			
			return true;
		}
		
		if(logType == 1) {
			int length = 2 + rand.nextInt(6);
			
			for(int k1 = z; (k1 < z + length) && (isSuitablePositionForLog(world, x, y, k1)); k1++) {
				if(isVanillaLog) {
					setBlockAndNotifyAdequately(world, x, y, k1, this.log, 8);
				} else {
					setBlockAndNotifyAdequately(world, x, y, k1, this.log, this.logMeta);
				}
				
				world.getBlock(x, y - 1, k1).onPlantGrow(world, x, y - 1, k1, x, y, k1);
			}
			
			return true;
		}
		
		if(isVanillaLog) {
			setBlockAndNotifyAdequately(world, x, y, z, this.log, 0); 
		} else {
			setBlockAndNotifyAdequately(world, x, y, z, this.log, this.logMeta); 
		}
		
		world.getBlock(x, y - 1, z).onPlantGrow(world, x, y - 1, z, x, y, z);
		return true;
	}

	private boolean isSuitablePositionForLog(World world, int x, int y, int z) {
		if(log instanceof BlockLog) {
			this.isVanillaLog = true;
		}
		
		if(isVanillaLog) {
			if(!world.getBlock(x, y - 1, z).canSustainPlant(world, x, y, z, ForgeDirection.UP, (IPlantable)Blocks.sapling)) {
				return false;
			}
		} 
		
		if(world.getBlock(x, y, z).isReplaceable(world, x, y, z)) {
			return true;
		}
		
		return false;
	}
}