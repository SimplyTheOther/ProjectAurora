package projectaurora.world.layer;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import projectaurora.world.AuroraIntCache;
import projectaurora.world.biome.AuroraBiome;

public class AuroraGenLayerRiverVariants extends BaseGenLayer {
	private BaseGenLayer biomeGenLayer;
	private BaseGenLayer riverGenLayer;

	public AuroraGenLayerRiverVariants(long seed, BaseGenLayer biomes, BaseGenLayer rivers) {
		super(seed);
		this.biomeGenLayer = biomes;
		this.riverGenLayer = rivers;
	}
	
	@Override
	public void initWorldGenSeed(long seed) {
		this.biomeGenLayer.initWorldGenSeed(seed);
		this.riverGenLayer.initWorldGenSeed(seed);
		super.initWorldGenSeed(seed);
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
		int[] biomes = this.biomeGenLayer.getInts(world, x, z, xSize, zSize);
	    int[] rivers = this.riverGenLayer.getInts(world, x, z, xSize, zSize);
	    int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);

	    for (int l = 0; l < xSize * zSize; l++) {
	    	int biomeID = biomes[l];
	    	int riverCode = rivers[l];

	    	if (riverCode >= 0) {
		        BiomeGenBase biome = AuroraBiome.auroraBiomeList[biomeID];
		        
		        if (((biome instanceof AuroraBiome)) && (!((AuroraBiome)biome).getEnableRiver())) {
		        	ints[l] = biomeID;
		        //} else if ((biome instanceof BiomeGenCorrupted)) { TODO custom river variant biomes
		        	/*if ((biome instanceof BiomeGenCorruptSub2)) {
		        		ints[l] = AuroraBiome.corruptRiver2.biomeID;
		        	} else if ((biome instanceof BiomeGenCorruptSub3)) {
		        		ints[l] = AuroraBiome.corruptRiver3.biomeID;
		        	} else {
		        		ints[l] = AuroraBiome.corruptRiver.biomeID;
		        	}*/
		        } else {
		        	ints[l] = AuroraBiome.lavaRiver.biomeID;
		        }
	        } else {
	        	ints[l] = biomeID;
	        }
	    }
	    return ints;
	}
}