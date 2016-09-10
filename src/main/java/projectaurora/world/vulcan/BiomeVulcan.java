package projectaurora.world.vulcan;

import net.minecraft.init.Blocks;
import projectaurora.core.Content;
import projectaurora.world.AuroraTreeType;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.biome.AuroraBiomeVariant;

public class BiomeVulcan extends AuroraBiome {

	public BiomeVulcan(int biomeID) {
		super(biomeID);
		this.topBlock = Content.dust;
		this.topBlockMeta = 0;
		this.fillerBlock = Content.rock; 
		this.fillerBlockMeta = 0;
		this.stoneBlock = Content.rock;
		this.stoneBlockMeta = 0;
		this.dominantFluidBlock = Blocks.lava;
		this.dominantFluidMeta = 0;
		
		this.addBiomeVariantSet(AuroraBiomeVariant.SET_NORMAL);
		
		this.decorator.clearTrees();
		this.decorator.addTree(AuroraTreeType.GLOWSTONE, 50);
		this.decorator.generateWater = false;
		
		super.vulcanList.add(this);
	}
}