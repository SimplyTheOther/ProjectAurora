package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import projectaurora.world.biome.AuroraBiome;

public class WorldGenAlgae extends WorldGenAbstractTree {
	Block growable;
	int growableMeta;
	Block growableBase;
	int baseMeta;
	
	public WorldGenAlgae(boolean flag, Block algae, int meta, Block base, int metaBase) {
		super(flag);
		this.growable = algae;
		this.growableMeta = meta;
		this.growableBase = base;
		this.baseMeta = metaBase;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		for(int amount = 0; amount < 3; amount++) {
			int i = x + rand.nextInt(8) - rand.nextInt(8);
			int j = y + rand.nextInt(4) - rand.nextInt(4);
			int k = z + rand.nextInt(8) - rand.nextInt(8);
			
			if(world.isAirBlock(i, j, k) && world.getBlock(i, j - 1, k) == growableBase && world.getBlockMetadata(i, j - 1, k) == baseMeta) {
				world.setBlock(i, j, k, growable, growableMeta, 2);
				
				if(world.getBiomeGenForCoords(x, z) == AuroraBiome.lavaOcean) {
					System.out.println("setBlock in ocean at " + i + "," + j + "," + k);
				}
			}
		}
		return true;
	}
}