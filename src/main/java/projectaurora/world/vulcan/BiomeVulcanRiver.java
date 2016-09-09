package projectaurora.world.vulcan;

import net.minecraft.init.Blocks;
import projectaurora.core.Content;
import projectaurora.world.biome.AuroraBiome;

public class BiomeVulcanRiver extends AuroraBiome {

	public BiomeVulcanRiver(int biomeID) {
		super(biomeID);
		this.topBlock = Content.dust;
		this.topBlockMeta = 0;
		this.fillerBlock = Content.rock; 
		this.fillerBlockMeta = 0;
		this.stoneBlock = Content.rock;
		this.stoneBlockMeta = 0;
		this.dominantFluidBlock = Blocks.lava;
		this.dominantFluidMeta = 0;
		//this.spawnableWhateverList.clear();
		//setBanditChance
		
		super.vulcanList.add(this);
	}
}