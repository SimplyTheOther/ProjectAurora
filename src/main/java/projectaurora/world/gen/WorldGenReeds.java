package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenReeds extends WorldGenerator {
	private Block reedsBlock;
	private int reedsMeta;
	
	public WorldGenReeds(Block block, int meta) {
		this.reedsBlock = block;
		this.reedsMeta = meta;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		label210: for(int l = 0; l < 16; l++) {
			int i1 = x + rand.nextInt(8) - rand.nextInt(8);
			int j1 = y + rand.nextInt(4) - rand.nextInt(4);
			int k1 = z + rand.nextInt(8) - rand.nextInt(8);
			
			int maxDepth = 5;
			for(int j2 = j1 - 0; j2 > 0; j2--) {
				if(world.getBlock(i1, j2, k1).getMaterial() != Material.water) {
					break;
				}
				
				if(j2 < j1 - maxDepth) {
					break label210;
				}
			}
			
			if(!world.isBlockFreezable(i1, j1 - 1, k1)) {
				int reedHeight = 1 + rand.nextInt(3);
				for(int j2 = j1; j2 < j1 + reedHeight; j2++) {
					if((world.isAirBlock(i1, j2, k1)) && this.reedsBlock.canBlockStay(world, i1, j2, k1)) {
						world.setBlock(i1, j2, k1, this.reedsBlock, this.reedsMeta, 2);
					}
				}
			}
		}
		return true;
	}
}