package projectaurora.world;

import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import projectaurora.core.Content;
import projectaurora.world.gen.WorldGenAlgae;
import projectaurora.world.gen.WorldGenBigTree;
import projectaurora.world.gen.WorldGenDeadTree;
import projectaurora.world.gen.WorldGenDesertTree;
import projectaurora.world.gen.WorldGenHugeTree;
import projectaurora.world.gen.WorldGenShrub;
import projectaurora.world.gen.WorldGenSimpleTree;

public enum AuroraTreeType {
	OAK(new ITreeFactory() {
		public WorldGenAbstractTree createTree(boolean flag) {//NOTE: MERGED WITH TALL AND TALLER
			return new WorldGenSimpleTree(flag, 4, 16, Blocks.log, 0, Blocks.leaves, 0);//TODO Other types of equivalent sized trees
		}
	}),
	OAK_LARGE(new ITreeFactory() {
		public WorldGenAbstractTree createTree(boolean flag) {
			return new WorldGenBigTree(flag, Blocks.log, 0, Blocks.leaves, 0);
		}
	}),
	OAK_HUGE(new ITreeFactory() {
		public WorldGenAbstractTree createTree(boolean flag) {
			return new WorldGenHugeTree(Blocks.log, 0, Blocks.leaves, 0);
		}
	}),
	OAK_SWAMP(new ITreeFactory() {
		public WorldGenAbstractTree createTree(boolean flag) {
			return new WorldGenSwamp();
		}
	}),
	OAK_DEAD(new ITreeFactory() {
		public WorldGenAbstractTree createTree(boolean flag) {
			return new WorldGenDeadTree(Blocks.log, 0);
		}
	}),
	OAK_DESERT(new ITreeFactory() {
		public WorldGenAbstractTree createTree(boolean flag) {
			return new WorldGenDesertTree(flag, Blocks.log, 0, Blocks.leaves, 0);
		}
	}),
	OAK_SHRUB(new ITreeFactory() {
		public WorldGenAbstractTree createTree(boolean flag) {
			return new WorldGenShrub(Blocks.log, 0, Blocks.leaves, 0);
		}
	}),
	GLOWSTONE(new ITreeFactory() {
		public WorldGenAbstractTree createTree(boolean flag) {
			return new WorldGenAlgae(flag, Content.plant, 0, Blocks.lava, 0);
		}
	}),//TODO more custom trees
	NULL((ITreeFactory)null);
	
	public static AuroraTreeType[] SET_OAK = { OAK, OAK_LARGE, OAK_HUGE, OAK_SWAMP, OAK_DEAD, OAK_DESERT, OAK_SHRUB };
	
	private ITreeFactory treeFactory;
	
	private AuroraTreeType(ITreeFactory factory) {
		this.treeFactory = factory;
	}
	
	public WorldGenAbstractTree create(boolean flag) {
		return this.treeFactory.createTree(flag);
	}
	
	public static class WeightedTreeType extends WeightedRandom.Item {
		public AuroraTreeType treeType;
		
		public WeightedTreeType(AuroraTreeType tree, int i) {
			super(i);
			this.treeType = tree;
		}
	}
	
	private interface ITreeFactory {
		public abstract WorldGenAbstractTree createTree(boolean param);
	}
}