package projectaurora.world.gen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenSand extends WorldGenerator {
	private Block sand;
	private int sandMeta;
	private int radius;
	private int heightRadius;
	
	public WorldGenSand(Block sandC, int sandMetaC, int radiusC, int heightRadiusC) {
		this.sand = sandC;
		this.sandMeta = sandMetaC;
		this.radius = radiusC;
		this.heightRadius = heightRadiusC;
	}

	@Override
	public boolean generate(World world, Random rand, int x, int y, int z) {
		boolean mid = false;
		boolean adj = false;
		
		int waterCheck = 1;
		for(int i1 = -waterCheck; i1 <= waterCheck; i1++) {
			for(int k1 = -waterCheck; k1 <= waterCheck; k1++) {
				int i2 = x + i1;
				int k2 = z + k1;
				
				if(world.getBlock(i2, y, k2).getMaterial() == Material.water) {
					if(i1 == 0 && k1 == 0) {
						mid = true;
					} else {
						adj = true;
					}
				}
			}
		}
		
		if((!mid) || (!adj)) {
			return false;
		}
		
		BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
		int r = rand.nextInt(this.radius - 2) + 2;
		int hr = this.heightRadius;
		
		for(int i1 = x - r; i1 <= x + r; i1++) {
			for(int k1 = z - r; k1 <= z + r; k1++) {
				int i2 = i1 - x;
				int k2 = k1 - z;
				
				if(i2 * i2 + k2 * k2 <= r * r) {
					for(int j1 = y - hr; j1 <= y + hr; j1++) {
						Block block = world.getBlock(i1, j1, k1);
						
						if(block == biome.topBlock || block == biome.fillerBlock) {
							if(rand.nextInt(3) != 0) {
								world.setBlock(i1, j1, k1, this.sand, this.sandMeta, 2);
							}
						}
					}
				}
			}
		}
		// TODO Change the params?
		return false;
	}
}