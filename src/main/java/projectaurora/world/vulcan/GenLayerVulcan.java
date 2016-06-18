package projectaurora.world.vulcan;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.layer.AuroraGenLayerBeach;
import projectaurora.world.layer.AuroraGenLayerBiomeSubTypes;
import projectaurora.world.layer.AuroraGenLayerBiomeSubTypesInit;
import projectaurora.world.layer.AuroraGenLayerBiomeSubTypesSmall;
import projectaurora.world.layer.AuroraGenLayerBiomeVariants;
import projectaurora.world.layer.AuroraGenLayerBiomeVariantsLake;
import projectaurora.world.layer.AuroraGenLayerRiver;
import projectaurora.world.layer.AuroraGenLayerRiverInit;
import projectaurora.world.layer.AuroraGenLayerRiverVariants;
import projectaurora.world.layer.AuroraGenLayerSmooth;
import projectaurora.world.layer.AuroraGenLayerZoom;
import projectaurora.world.layer.BaseGenLayer;

public class GenLayerVulcan extends BaseGenLayer {

	public GenLayerVulcan(long seed) {
		super(seed);
	}

	@Override
	public int[] getInts(World world, int x, int z, int width, int length) {
		/*int[] inputBiomeIDs = null;
		if(this.baseParent != null) {
			System.out.println("baseParent exists");
			inputBiomeIDs = this.baseParent.getInts(world, x, z, width, length);
		} else if(this.baseParent == null) {
			System.out.println("baseParent does not exist");
		}*///TODO make the original baseParent thing work
		int[] inputBiomeIDs = noBaseGetInts(world, x, z, width, length);
		int[] outputBiomeIDs = AuroraIntCache.get(world).getIntArray(width * length);
		
		for(int i1 = 0; i1 < length; i1++) {
			for(int j1 = 0; j1 < width; j1++) {
				this.initChunkSeed(j1 + x, i1 + z);
				int currentBiomeID = inputBiomeIDs[j1 + i1 * width];
				
				int l1 = (currentBiomeID & 3840) >> 8;
				currentBiomeID &= -3841;
				
				System.out.println("currentBiomeID = " + currentBiomeID);
				System.out.println("ocean biome" + AuroraBiome.lavaOcean);
				System.out.println("ocean biome id = " + AuroraBiome.lavaOcean.biomeID);
				
				if(currentBiomeID == AuroraBiome.lavaOcean.biomeID) {
					outputBiomeIDs[j1 + i1 * width] = currentBiomeID;
				} else if(currentBiomeID == 1) {
					outputBiomeIDs[j1 + i1 * width] = AuroraBiome.vulcan.biomeID;
					//if(j1 > 0) {
						//special biomes, whatever
					//}
				}
			}
		}
		/*int[] outputBiomeIDs = AuroraIntCache.get(world).getoutputBiomeIDs(width * length);
	   
		for (int k1 = 0; k1 < length; k1++) {
			for (int i1 = 0; i1 < width; i1++) {
				int i2 = x + i1 + originX;
				int k2 = z + k1 + originZ;
				
				if ((i2 < 0) || (i2 >= imageWidth) || (k2 < 0) || (k2 >= imageHeight)) {
					outputBiomeIDs[(i1 + k1 * width)] = AuroraBiome.ocean.biomeID;
				} else {
					outputBiomeIDs[(i1 + k1 * width)] = biomeImageData[(i2 + k2 * imageWidth)];
				}
			}
		}
	    return outputBiomeIDs;*/
		return outputBiomeIDs; //TODO Fix getInts for rainfall or whatever
    }

	private int[] noBaseGetInts(World world, int x, int z, int width, int length) {//TODO see if this even works...
		int[] outputBiomeIDs = AuroraIntCache.get(world).getIntArray(width * length);
	
		for(int i1 = 0; i1 < length; i1++) {
			for(int j1 = 0; j1 < width; j1++) {
				this.initChunkSeed(j1 + x, i1 + z);
				
				if(this.nextInt(100) < 50) {
					outputBiomeIDs[j1 + i1 * width] = 1;
				} else {
					outputBiomeIDs[j1 + i1 * width] = 0;
				}
			}
		}
		if(x > -length && x <= 0 && z > -width && z <= 0) {
			outputBiomeIDs[-x + -z * width] = 1;
		}
		return outputBiomeIDs;
	}

	public static BaseGenLayer[] createWorld(long seed) {
		BaseGenLayer rivers = new AuroraGenLayerRiverInit(100L);
		rivers = AuroraGenLayerZoom.magnify(1000L, rivers, 8);
		rivers = new AuroraGenLayerRiver(1L, rivers);
		rivers = new AuroraGenLayerSmooth(1000L, rivers);
		rivers = AuroraGenLayerZoom.magnify(1000L, rivers, 1);
		
		BaseGenLayer biomeSubtypes = new AuroraGenLayerBiomeSubTypesInit(3000L);
		biomeSubtypes = AuroraGenLayerZoom.magnify(3000L, biomeSubtypes, 2);
		
		BaseGenLayer biomes = new GenLayerVulcan(seed);
		biomes = new AuroraGenLayerBiomeSubTypes(1000L, biomes, biomeSubtypes);
		biomes = new AuroraGenLayerBiomeSubTypesSmall(500L, biomes);
		
		biomes = AuroraGenLayerZoom.magnify(1000L, biomes, 1);
		biomes = new AuroraGenLayerBeach(1000L, biomes, AuroraBiome.lavaOcean);
		biomes = AuroraGenLayerZoom.magnify(1000L, biomes, 4);
		biomes = new AuroraGenLayerSmooth(1000L, biomes);
		biomes = new AuroraGenLayerRiverVariants(100L, biomes, rivers);
		
		BaseGenLayer variants = new AuroraGenLayerBiomeVariants(200L);
		variants = AuroraGenLayerZoom.magnify(200L, variants, 8);
		BaseGenLayer variantsSmall = new AuroraGenLayerBiomeVariants(300L);
		variantsSmall = AuroraGenLayerZoom.magnify(300L, variants, 6);
		
		BaseGenLayer lakes = new AuroraGenLayerBiomeVariantsLake(100L, null, 0).setLakeFlags(new int[] { 1 });
		for(int i = 1; i <= 5; i++) {
			lakes = new AuroraGenLayerZoom(200L + i, lakes);
			
			if(i <= 2) {
				lakes = new AuroraGenLayerBiomeVariantsLake(300L + i, lakes, i).setLakeFlags(new int[] { 1 });
			}
			
			if(i == 3) {
				lakes = new AuroraGenLayerBiomeVariantsLake(500L, lakes, i).setLakeFlags(new int[] { 2, 4 });
			}
		}
		return new BaseGenLayer[] { biomes, variants, variantsSmall, lakes};
	}
}