package projectaurora.world.vulcan;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import projectaurora.core.Content;
import projectaurora.world.AuroraTreeType;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.biome.AuroraBiomeVariant;

public class BiomeLavaOcean extends AuroraBiome {

	public BiomeLavaOcean(int id) {
		super(id);
		this.topBlock = Content.dust;
		this.topBlockMeta = 0;
		this.fillerBlock = Content.rock; 
		this.fillerBlockMeta = 0;
		this.stoneBlock = Content.rock;//TODO decide on type of stone, for now, it's basalt
		this.stoneBlockMeta = 0;
		this.dominantFluidBlock = Blocks.lava;
		this.dominantFluidMeta = 0;
		
		//TODO this.spawnableWhateverCreatureList.add 
		this.spawnableAmbientList.clear();
		this.spawnableGoodList.clear();
		this.spawnableEvilList.clear();
		
		//TODO this.decorator.WHATEVERPerChunk = 
		
		this.addBiomeVariant(AuroraBiomeVariant.STANDARD);
		this.addBiomeVariant(AuroraBiomeVariant.ISLAND);
		
		this.decorator.addTree(AuroraTreeType.GLOWSTONE, 10);
		
		this.setBanditChance(0);
	}
	
	@Override
	public boolean getEnableRiver() {
		return false;
	}
	
	@Override
	public void decorate(World world, Random rand, int x, int z) {
		super.decorate(world, rand, x, z);
	}
	
	@Override
	public float getChanceToSpawnAnimals() {
		return 0.25F;
	}
}