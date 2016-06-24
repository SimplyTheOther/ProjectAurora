package projectaurora.world.vulcan;

import net.minecraft.init.Blocks;
import projectaurora.world.biome.AuroraBiome;

public class BiomeVulcan extends AuroraBiome {

	public BiomeVulcan(int biomeID) {
		super(biomeID);
		this.topBlock = Blocks.netherrack;//TODO better top block than netherrack?
		this.topBlockMeta = 0;
		this.fillerBlock = Blocks.netherrack; //TODO better filler than netherrack?
		this.fillerBlockMeta = 0;
		this.stoneBlock = Blocks.sandstone;//TODO for testing purposes, change to netherrack?
		this.stoneBlockMeta = 0;
		this.dominantFluidBlock = Blocks.lava;
		this.dominantFluidMeta = 0;
	}
}