package projectaurora.world.vulcan;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import projectaurora.world.biome.AuroraBiome;

public class BiomeLavaOcean extends AuroraBiome {

	public BiomeLavaOcean(int id) {
		super(id);
		this.topBlock = Blocks.netherrack;//TODO better top block than netherrack?
		this.topBlockMeta = 0;
		this.fillerBlock = Blocks.netherrack; //TODO better filler than netherrack?
		this.fillerBlockMeta = 0;
		
		//TODO this.spawnableWhateverCreatureList.add 
		this.spawnableAmbientList.clear();
		this.spawnableGoodList.clear();
		this.spawnableEvilList.clear();
		
		//TODO this.decorator.WHATEVERPerChunk = 
		
		//TODO this.decorator.addTree(AuroraTreeType.WHATEVER, whatever)
		
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