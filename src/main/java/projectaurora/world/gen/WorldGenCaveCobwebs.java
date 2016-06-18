package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenCaveCobwebs extends WorldGenerator {
	private Block web;
	private int webMeta;
	private Block stone;
	private int stoneMeta;
	
	public WorldGenCaveCobwebs(Block block, int meta, Block target, int targetMeta) {
		this.web = block;
		this.webMeta = meta;
		this.stone = target;
		this.stoneMeta = targetMeta;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int l = 0; l < 64; l++) {
			int i1 = x - rand.nextInt(6) + rand.nextInt(6);
			int j1 = y - rand.nextInt(4) + rand.nextInt(4);
			int k1 = z - rand.nextInt(6) + rand.nextInt(6);
			
			if(world.isAirBlock(i1, j1, k1)) {
				boolean flag = false;
				
				if(world.getBlock(i1 - 1, j1, k1) == this.stone && world.getBlockMetadata(i1 - 1, j1, k1) == this.stoneMeta) {
					flag = true;
				}
				if(world.getBlock(i1 + 1, j1, k1) == this.stone && world.getBlockMetadata(i1 + 1, j1, k1) == this.stoneMeta) {
					flag = true;
				}
				if(world.getBlock(i1, j1 - 1, k1) == this.stone && world.getBlockMetadata(i1, j1 - 1, k1) == this.stoneMeta) {
					flag = true;
				}
				if(world.getBlock(i1, j1 + 1, k1) == this.stone && world.getBlockMetadata(i1, j1 + 1, k1) == this.stoneMeta) {
					flag = true;
				}
				if(world.getBlock(i1, j1, k1 - 1) == this.stone && world.getBlockMetadata(i1, j1, k1 - 1) == this.stoneMeta) {
					flag = true;
				}
				if(world.getBlock(i1, j1, k1 + 1) == this.stone && world.getBlockMetadata(i1, j1, k1 + 1) == this.stoneMeta) {
					flag = true;
				}
				if(flag) {
					world.setBlock(i1, j1, k1, web, webMeta, 2);
				}
			}
		}
		return true;
	}
}