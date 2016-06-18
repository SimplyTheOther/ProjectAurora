package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;
import projectaurora.world.biome.AuroraBiome;

public class WorldGenBiomeFlowers extends WorldGenerator {

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		BiomeGenBase.FlowerEntry flower = ((AuroraBiome)world.getBiomeGenForCoords(x, z)).getRandomFlower(rand);
		
		Block block = flower.block;
		int meta = flower.metadata;
		
		for(int l = 0; l < 64; l++) {
			int i1 = x + rand.nextInt(8) - rand.nextInt(8);
			int j1 = y + rand.nextInt(4) - rand.nextInt(4);
			int k1 = z + rand.nextInt(8) - rand.nextInt(8);
			
			if(world.isAirBlock(i1, j1, k1) && ((!world.provider.hasNoSky) || (j1 < 255)) && block.canBlockStay(world, i1, i1, k1)) {
				world.setBlock(i1, j1, k1, block, meta, 2);
			}
		}
		return true;
	}
}