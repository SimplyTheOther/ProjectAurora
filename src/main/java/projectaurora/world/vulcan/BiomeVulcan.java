package projectaurora.world.vulcan;

import net.minecraft.init.Blocks;
import projectaurora.core.Content;
import projectaurora.world.biome.AuroraBiome;

public class BiomeVulcan extends AuroraBiome {

	public BiomeVulcan(int biomeID) {
		super(biomeID);
		this.topBlock = Content.dust;
		this.topBlockMeta = 0;
		this.fillerBlock = Content.rock; 
		this.fillerBlockMeta = 0;
		this.stoneBlock = Blocks.stone;//TODO for testing purposes, change to netherrack?
		this.stoneBlockMeta = 0;
		this.dominantFluidBlock = Blocks.lava;
		this.dominantFluidMeta = 0;
	}
}