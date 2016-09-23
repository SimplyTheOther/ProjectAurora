package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import projectaurora.world.biome.AuroraBiome;

public class WorldGenStalactites extends WorldGenerator {

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int l = 0; l < 64; l++) {
			int i1 = x - rand.nextInt(8) + rand.nextInt(8);
			int j1 = y - rand.nextInt(4) + rand.nextInt(4);
			int k1 = z - rand.nextInt(8) + rand.nextInt(8);
			
			if(world.getBiomeGenForCoords(x, z) instanceof AuroraBiome) {
				AuroraBiome biome = (AuroraBiome)world.getBiomeGenForCoords(x, z);
				
				if(world.isAirBlock(i1, j1, k1)) {
					if(world.getBlock(i1, j1 + 1, k1) == biome.stoneBlock) {
						world.setBlock(i1, j1, k1, Blocks.brick_block, 0, 2);//TODO placeholder stalactites
					} else if(world.getBlock(i1, j1 - 1, k1) == biome.stoneBlock) {
						world.setBlock(i1, j1, k1, Blocks.stonebrick, 0, 2);
					}
				}
			}
		}
		return true;
	}
}