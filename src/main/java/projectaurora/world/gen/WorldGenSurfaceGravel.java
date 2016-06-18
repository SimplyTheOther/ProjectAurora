package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenSurfaceGravel extends WorldGenerator {
	private Block gravel;
	private int gravelMeta;
	
	public WorldGenSurfaceGravel(Block block, int meta) {
		this.gravel = block;
		this.gravelMeta = meta;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		int r = MathHelper.getRandomIntegerInRange(rand, 2, 8);
		int chance = MathHelper.getRandomIntegerInRange(rand, 3, 9);
		
		for(int i1 = -r; i1 <= r; i1++) {
			for(int k1 = -r; k1 <= r; k1++) {
				int i2 = x + i1;
				int k2 = z + k1;
				
				int d = i1 * i1 + k1 * k1;
				if(d < r * r) {
					if(rand.nextInt(chance) == 0) {
						int j1 = world.getTopSolidOrLiquidBlock(i2, k2) - 1;
						Block block = world.getBlock(i2, j1, k2);
						Material material = block.getMaterial();
						
						if(block.isOpaqueCube() && (material == Material.ground || material == Material.grass || material == Material.sand)) {
							world.setBlock(i2, j1, k2, gravel, gravelMeta, 2);
						}
					}
				}
			}
		}
		return true;
	}
}