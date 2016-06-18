package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenStreams extends WorldGenerator {
	private Block liquid;
	
	public WorldGenStreams(Block block) {
		this.liquid = block;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		if(!isRock(world, x, y + 1, z)) {
			return false;
		}
		
		if(!isRock(world, x, y - 1, z)) {
			return false;
		}
		
		if((!world.isAirBlock(x, y, z)) && (!isRock(world, x, y, z))) {
			return false;
		}
		
		int sides = 0;
		if(isRock(world, x - 1, y, z)) {
			sides++;
		}
		
		if(isRock(world, x + 1, y, z)) {
			sides++;
		}
		
		if(isRock(world, x, y, z - 1)) {
			sides++;
		}
		
		if(isRock(world, x, y, z + 1)) {
			sides++;
		}
		
		int openAir = 0;
		if(world.isAirBlock(x - 1, y, z)) {
			openAir++;
		}
		
		if(world.isAirBlock(x + 1, y, z)) {
			openAir++;
		}
		
		if(world.isAirBlock(x, y, z - 1)) {
			openAir++;
		}
		
		if(world.isAirBlock(x, y, z + 1)) {
			openAir++;
		}
		
		if(sides == 3 && openAir == 1) {
			world.setBlock(x, y, z, liquid, 0, 2);
			world.scheduledUpdatesAreImmediate = true;
			this.liquid.updateTick(world, x, y, z, rand);
			world.scheduledUpdatesAreImmediate = false;
		}
		
		return true;
	}

	private boolean isRock(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		
		if(block == Blocks.stone/*TODO other rocks*/) {
			return true;
		}
		return false;
	}
}