package projectaurora.world.vulcan;

import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeCache;
import projectaurora.world.BaseChunkManager;
import projectaurora.world.layer.AuroraGenLayerZoomVoronoi;
import projectaurora.world.layer.BaseGenLayer;

public class VulcanChunkManager extends BaseChunkManager {
	public VulcanChunkManager(World world) {
		this.worldObj = world;
		this.biomeCacheAurora = new BiomeCache(this);
		setupGenLayers();
	}

	@Override
	public void setupGenLayers() {
		long seed = this.worldObj.getSeed() + 1954L;
	    this.chunkGenLayers = GenLayerVulcan.createWorld(seed);
	    this.worldLayers = new BaseGenLayer[this.chunkGenLayers.length];

	    //BaseGenLayer biomes = this.chunkGenLayers[LAYER_BIOME];
	    //this.worldLayers[LAYER_BIOME] = new AuroraGenLayerZoomVoronoi(10L, biomes);

	    //BaseGenLayer variantsLarge = this.chunkGenLayers[LAYER_VARIANTS_LARGE];
	    //this.worldLayers[LAYER_VARIANTS_LARGE] = new AuroraGenLayerZoomVoronoi(10L, variantsLarge);
	    //BaseGenLayer variantsSmall = this.chunkGenLayers[LAYER_VARIANTS_SMALL];
	    //this.worldLayers[LAYER_VARIANTS_SMALL] = new AuroraGenLayerZoomVoronoi(10L, variantsSmall);
	    //BaseGenLayer variantsLakes = this.chunkGenLayers[LAYER_VARIANTS_LAKES];
	    //this.worldLayers[LAYER_VARIANTS_LAKES] = new AuroraGenLayerZoomVoronoi(10L, variantsLakes);

	    for(int i = 0; i < this.worldLayers.length; i++) {
	    	BaseGenLayer layer = this.chunkGenLayers[i];
	    	this.worldLayers[i] = new AuroraGenLayerZoomVoronoi(10L, layer);
	    }
	    
	    for(int i = 0; i < this.worldLayers.length; i++) {
	    	this.chunkGenLayers[i].initWorldGenSeed(seed);
	    	this.worldLayers[i].initWorldGenSeed(seed);
	    }
    }
}