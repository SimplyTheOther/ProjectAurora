package projectaurora.world.vulcan;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import projectaurora.core.Content;
import projectaurora.world.AuroraTreeType;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.biome.AuroraBiomeVariant;

public class BiomeLavaOcean extends BiomeVulcan {

	public BiomeLavaOcean(int id) {
		super(id);
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
		
		this.clearBiomeVariants();
		this.addBiomeVariant(AuroraBiomeVariant.ISLAND);
		
		this.decorator.glowstonePerChunk = 1;
		
		this.decorator.clearTrees();
		this.decorator.addTree(AuroraTreeType.GLOWSTONE, 1000000);
		this.decorator.generateWater = false;
		
		this.isOcean = true;
		
		this.setBanditChance(0);
		
		super.vulcanList.add(this);
	}
	
	@Override
	public boolean getEnableRiver() {
		return false;
	}
	
	@Override
	public float getChanceToSpawnAnimals() {
		return 0.25F;
	}
}