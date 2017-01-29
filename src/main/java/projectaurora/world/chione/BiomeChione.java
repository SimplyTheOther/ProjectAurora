package projectaurora.world.chione;

import net.minecraft.init.Blocks;
import projectaurora.core.Content;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.biome.AuroraBiomeVariant;

public class BiomeChione extends AuroraBiome {

	public BiomeChione(int biomeID) {//TODO chione, unimplemented
		super(biomeID);
		
		this.topBlock = Blocks.dirt;//TODO infertile dirt? ice?
		this.topBlockMeta = 0;
		this.fillerBlock = Blocks.dirt; 
		this.fillerBlockMeta = 0;
		this.stoneBlock = Blocks.stone;
		this.stoneBlockMeta = 0;
		this.dominantFluidBlock = Blocks.ice;//TODO salt water ice?
		this.dominantFluidMeta = 0;
		
		this.spawnableAmbientList.clear();
		this.spawnableGoodList.clear();
		this.spawnableEvilList.clear();
		
		this.addBiomeVariantSet(AuroraBiomeVariant.SET_NORMAL);
		this.clearBiomeVariants();
		
		this.decorator.clearTrees();
		this.decorator.generateWater = true;
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
		this.decorator.generateLava = false;
		this.decorator.grassPerChunk = 0;
		this.decorator.logsPerChunk = 0;
		this.decorator.melonPerChunk = 0;
		this.decorator.mushroomsPerChunk = 0;
		this.decorator.quicksandPerChunk = 0;
		this.decorator.reedPerChunk = 0;
		this.decorator.sandPerChunk = 0;
		this.decorator.vinesPerChunk = 0;
		this.decorator.willowPerChunk = 0;
		this.decorator.generateSurfaceGravel = true;
	    this.decorator.generateDirt = true;
	    this.decorator.generateGravel = true;
	    this.decorator.biomeOreFactor = 1.2F;
	    this.decorator.generateCoal = true;
	    this.decorator.moltenOres = false;
		
		super.chioneList.add(this);
	}
}