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
		
		this.spawnableAmbientList.clear();
		this.spawnableGoodList.clear();
		this.spawnableEvilList.clear();
		
		this.addBiomeVariantSet(AuroraBiomeVariant.SET_NORMAL);
		
		this.decorator.clearTrees();
		this.decorator.addTree(AuroraTreeType.GLOWSTONE, 50);
		this.decorator.generateWater = false;
		this.decorator.cactiPerChunk = 0;
		this.decorator.canePerChunk = 0;
		this.decorator.clayPerChunk = 0;
		this.decorator.cornPerChunk = 0;
		this.decorator.deadBushPerChunk = 0;
		this.decorator.doubleFlowersPerChunk = 0;
		this.decorator.doubleGrassPerChunk = 0;
		this.decorator.dryReedChance = 0.0F;
		this.decorator.enableFern = false;
		this.decorator.enableRandomMushroom = false;
		this.decorator.flowersPerChunk = 0;
		this.decorator.generateCobwebs = false;
		this.decorator.generateLava = true;
		this.decorator.grassPerChunk = 0;
		this.decorator.logsPerChunk = 0;
		this.decorator.melonPerChunk = 0;
		this.decorator.mushroomsPerChunk = 0;
		this.decorator.quicksandPerChunk = 0;
		this.decorator.reedPerChunk = 0;
		this.decorator.sandPerChunk = 0;
		this.decorator.vinesPerChunk = 0;
		this.decorator.willowPerChunk = 0;
		this.decorator.generateSurfaceGravel = false;
	    this.decorator.generateDirt = false;
	    this.decorator.generateGravel = false;
	    this.decorator.biomeOreFactor = 2.0F;
	    this.decorator.generateCoal = false;
	    this.decorator.moltenOres = true;
		
		super.vulcanList.add(this);
	}
}