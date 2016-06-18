package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenCorn extends WorldGenerator {
	private Block corn;
	private int cornMeta;
	
	public WorldGenCorn(Block block, int meta) {
		this.corn = block;
		this.cornMeta = meta;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int l = 0; l < 20; l++) {
			int i1 = x + rand.nextInt(4) - rand.nextInt(4);
			int k1 = z + rand.nextInt(4) - rand.nextInt(4);
			
			Block replace = world.getBlock(i1, y, k1);
			if(replace.isReplaceable(world, i1, y, k1) && (!replace.getMaterial().isLiquid())) {
				boolean adjWater = false;
				
				label164: for(int i3 = -1; i3 <= 1; i3++) {
					for(int k3 = -1; k3 <= 1; k3++) {
						if(Math.abs(i3) + Math.abs(k3) == 1 && world.getBlock(i1 + i3, y - 1, k1 + k3).getMaterial() == Material.water) {
							adjWater = true;
							break label164;
						}
					}
				}
				
				if(adjWater) {
					int cornHeight = 2 + rand.nextInt(2);
					
					for(int j2 = 0; j2 < cornHeight; j2++) {
						if(corn.canBlockStay(world, i1, y + j2, k1)) {
							world.setBlock(i1, y + j2, k1, corn, cornMeta, 2);
						}
					}
				}
			}
		}
		return true;
	}
}