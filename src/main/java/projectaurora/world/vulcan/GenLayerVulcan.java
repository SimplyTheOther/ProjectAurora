package projectaurora.world.vulcan;

import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.layer.AuroraGenLayerBeach;
import projectaurora.world.layer.AuroraGenLayerBiomeSubTypes;
import projectaurora.world.layer.AuroraGenLayerBiomeSubTypesInit;
import projectaurora.world.layer.AuroraGenLayerBiomeVariants;
import projectaurora.world.layer.AuroraGenLayerBiomeVariantsLake;
import projectaurora.world.layer.AuroraGenLayerBiomes;
import projectaurora.world.layer.AuroraGenLayerDesertOasis;
import projectaurora.world.layer.AuroraGenLayerDesertRiverbanks;
import projectaurora.world.layer.AuroraGenLayerExtractRivers;
import projectaurora.world.layer.AuroraGenLayerIncludeRivers;
import projectaurora.world.layer.AuroraGenLayerNarrowRivers;
import projectaurora.world.layer.AuroraGenLayerOasisLake;
import projectaurora.world.layer.AuroraGenLayerOcean;
import projectaurora.world.layer.AuroraGenLayerRemoveOcean;
import projectaurora.world.layer.AuroraGenLayerRemoveRivers;
import projectaurora.world.layer.AuroraGenLayerRiver;
import projectaurora.world.layer.AuroraGenLayerRiverInit;
import projectaurora.world.layer.AuroraGenLayerRiverZoom;
import projectaurora.world.layer.AuroraGenLayerSmooth;
import projectaurora.world.layer.AuroraGenLayerZoom;
import projectaurora.world.layer.BaseGenLayer;

public class GenLayerVulcan extends BaseGenLayer {

	public GenLayerVulcan(long seed) {
		super(seed);
	}

	@Override
	public int[] getInts(World world, int x, int z, int width, int length) {
		int[] inputBiomeIDs = noBaseGetInts(world, x, z, width, length);
		int[] outputBiomeIDs = AuroraIntCache.get(world).getIntArray(width * length);
		
		for(int i1 = 0; i1 < length; i1++) {
			for(int j1 = 0; j1 < width; j1++) {
				this.initChunkSeed(j1 + x, i1 + z);
				int currentBiomeID = inputBiomeIDs[j1 + i1 * width];
				
				int l1 = (currentBiomeID & 3840) >> 8;
				currentBiomeID &= -3841;
				
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
		return outputBiomeIDs; 
    }

	private int[] noBaseGetInts(World world, int x, int z, int width, int length) {
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

	/*public static BaseGenLayer[] createWorld(long seed) {
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
	}*/
	
	public static BaseGenLayer[] createWorld(long seed) {
		BaseGenLayer rivers = new AuroraGenLayerRiverInit(100L);
	    rivers = AuroraGenLayerZoom.magnify(1000L, rivers, /*10*/1);
	    rivers = new AuroraGenLayerRiver(1L, rivers);
	    rivers = new AuroraGenLayerSmooth(1000L, rivers);
	    rivers = AuroraGenLayerZoom.magnify(1000L, rivers, 1);

	    BaseGenLayer biomeSubtypes = new AuroraGenLayerBiomeSubTypesInit(3000L);
	    biomeSubtypes = AuroraGenLayerZoom.magnify(3000L, biomeSubtypes, 2);

	    BaseGenLayer biomes = new GenLayerVulcan(seed);
	    
	    BaseGenLayer oceans = new AuroraGenLayerOcean(2012L);
	    oceans = AuroraGenLayerZoom.magnify(200L, oceans, 3);
	    oceans = new AuroraGenLayerRemoveOcean(400L, oceans);

	    biomes = new AuroraGenLayerBiomes(2013L, oceans);
	    biomes = AuroraGenLayerZoom.magnify(300L, biomes, 2);

	    BaseGenLayer mapRivers = new AuroraGenLayerExtractRivers(5000L, biomes);
	    
	    biomes = new AuroraGenLayerRemoveRivers(1000L, biomes);
	    biomes = new AuroraGenLayerBiomeSubTypes(1000L, biomes, biomeSubtypes);

	    biomes = new AuroraGenLayerDesertRiverbanks(200L, biomes, mapRivers);
	    biomes = new AuroraGenLayerDesertOasis(500L, biomes);
	    biomes = AuroraGenLayerZoom.magnify(1000L, biomes, 1);
	    biomes = new AuroraGenLayerBeach(1000L, biomes, AuroraBiome.lavaOcean);
	    biomes = AuroraGenLayerZoom.magnify(1000L, biomes, 2);
	    biomes = new AuroraGenLayerOasisLake(600L, biomes);
	    biomes = AuroraGenLayerZoom.magnify(1000L, biomes, 2);
	    biomes = new AuroraGenLayerSmooth(1000L, biomes);

	    BaseGenLayer variants = new AuroraGenLayerBiomeVariants(200L);
	    variants = AuroraGenLayerZoom.magnify(200L, variants, 8);
	    BaseGenLayer variantsSmall = new AuroraGenLayerBiomeVariants(300L);
	    variantsSmall = AuroraGenLayerZoom.magnify(300L, variantsSmall, 6);

	    BaseGenLayer lakes = new AuroraGenLayerBiomeVariantsLake(100L, null, 0).setLakeFlags(new int[] { 1 });
	    
	    for (int i = 1; i <= 5; i++) {
	    	lakes = new AuroraGenLayerZoom(200L + i, lakes);

	    	if (i <= 2) {
	    		lakes = new AuroraGenLayerBiomeVariantsLake(300L + i, lakes, i).setLakeFlags(new int[] { 1 });
	    	}

	    	if (i == 3) {
	    		lakes = new AuroraGenLayerBiomeVariantsLake(500L, lakes, i).setLakeFlags(new int[] { 2, 4 });
	    	}
	    }

	    for (int i = 0; i < 4; i++) {
	    	mapRivers = new AuroraGenLayerRiverZoom(4000L + i, mapRivers);
	    }
	    
	    mapRivers = new AuroraGenLayerNarrowRivers(3000L, mapRivers, 6);
	    mapRivers = AuroraGenLayerZoom.magnify(4000L, mapRivers, 1);
	    rivers = new AuroraGenLayerIncludeRivers(5000L, rivers, mapRivers);

	    return new BaseGenLayer[] { biomes, variants, variantsSmall, lakes, rivers };
	}
}