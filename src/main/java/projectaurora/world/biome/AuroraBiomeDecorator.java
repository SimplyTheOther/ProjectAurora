package projectaurora.world.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenCactus;
import net.minecraft.world.gen.feature.WorldGenDeadBush;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenPumpkin;
import net.minecraft.world.gen.feature.WorldGenReed;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fluids.FluidRegistry;
import projectaurora.compat.Compat;
import projectaurora.core.Content;
import projectaurora.world.AuroraTreeType;
import projectaurora.world.BaseChunkManager;
import projectaurora.world.WorldModule;
import projectaurora.world.gen.WorldGenAlgae;
import projectaurora.world.gen.WorldGenBiomeFlowers;
import projectaurora.world.gen.WorldGenCaveCobwebs;
import projectaurora.world.gen.WorldGenCorn;
import projectaurora.world.gen.WorldGenLogs;
import projectaurora.world.gen.WorldGenMineable;
import projectaurora.world.gen.WorldGenReeds;
import projectaurora.world.gen.WorldGenSand;
import projectaurora.world.gen.WorldGenStalactites;
import projectaurora.world.gen.WorldGenStreams;
import projectaurora.world.gen.WorldGenSurfaceGravel;
import projectaurora.world.village.AuroraVillageGen;
import projectaurora.world.vulcan.BiomeVulcan;
import projectaurora.world.vulcan.VulcanChunkProvider.ChunkFlags;

public class AuroraBiomeDecorator {//TODO readd generation
	private World worldObj;
	private Random rand;
	private int chunkX;
	private int chunkZ;
	private AuroraBiome biome;
	private List<GenerateOre> biomeOres = new ArrayList();
	public float biomeOreFactor = 1.0F;
	private WorldGenerator clayGen;
	private WorldGenerator sandGen;
	private WorldGenerator quicksandGen;
	private WorldGenerator surfaceGravelGen;
	private WorldGenerator flowerGen;
	private WorldGenerator logGen;
	private WorldGenerator mushroomBrownGen;
	private WorldGenerator mushroomRedGen;
	private WorldGenerator caneGen;
	private WorldGenerator reedGen;
	private WorldGenerator dryReedGen;
	private WorldGenerator cornGen;
	private WorldGenerator pumpkinGen;
	private WorldGenerator glowstoneGen;
	private WorldGenerator cobwebGen;
	private WorldGenerator stalactiteGen;
	private WorldGenerator vinesGen;
	private WorldGenerator cactusGen;
	private WorldGenerator melonGen;
	public int sandPerChunk;
	public int clayPerChunk;
	public int quicksandPerChunk;
	public int treesPerChunk;
	public int willowPerChunk;
	public int logsPerChunk;
	public int vinesPerChunk;
	public int flowersPerChunk;
	public int doubleFlowersPerChunk;
	public int grassPerChunk;
	public int doubleGrassPerChunk;
	public boolean enableFern;
	public boolean enableSpecialGrasses;
	public int deadBushPerChunk;
	public int glowstonePerChunk;
	public int mushroomsPerChunk;
	public boolean enableRandomMushroom;
	public int canePerChunk;
	public int reedPerChunk;
	public float dryReedChance;
	public int cornPerChunk;
	public int cactiPerChunk;
	public float melonPerChunk;
	public boolean generateWater;
	public boolean generateLava;
	public boolean generateCobwebs;
	public boolean generateSurfaceGravel;
	private int treeClusterSize;
	private int treeClusterChance;
	private List<AuroraTreeType.WeightedTreeType> treeTypes;
	private Random structureRand;
	private List<RandomStructure> randomStructures;
	//private List<AuroraVillageGen> villages; TODO own villages/towns/settlements
	public boolean generateDirt;
	public boolean generateGravel;
	public boolean generateCoal;
	public boolean generateIron;
	public boolean generateGold;
	public boolean generateLapis;
	public boolean generateDiamond;
	public boolean generateEmerald;
	public boolean generateCopper;
	public boolean generateAluminium;
	public boolean generateLead;
	public boolean generateSilver;
	public boolean generateNickel;
	public boolean generateTin;
	public boolean generateQuartz;
	public boolean moltenOres;

	public void addOre(WorldGenerator generator, float chance, int min, int max) {
	    this.biomeOres.add(new GenerateOre(generator, chance, min, max));
	}
	
	public void addOre(WorldGenerator generator, int[] config) {
	    this.biomeOres.add(new GenerateOre(generator, (float)config[1], config[2], config[3]));
	}

	public void clearOres() {
	    this.biomeOres.clear();
	}

	public AuroraBiomeDecorator(AuroraBiome biome) {
	    this.clayGen = new WorldGenSand(Blocks.clay, 0, 5, 1);
	    this.sandGen = new WorldGenSand(Blocks.sand, 0, 7, 2);
	    //this.quicksandGen = new WorldGenSand(Block.quicksand, 0, 7, 2);
	    this.surfaceGravelGen = new WorldGenSurfaceGravel(Blocks.gravel, 0);

	    this.flowerGen = new WorldGenBiomeFlowers();
	    this.logGen = new WorldGenLogs();
	    this.mushroomBrownGen = new WorldGenFlowers(Blocks.brown_mushroom);
	    this.mushroomRedGen = new WorldGenFlowers(Blocks.red_mushroom);
	    this.caneGen = new WorldGenReed();
	    this.reedGen = new WorldGenReeds(Blocks.reeds, 0);
	    this.dryReedGen = new WorldGenReeds(Blocks.reeds, 0);
	    this.cornGen = new WorldGenCorn(Blocks.reeds, 0);
	    this.pumpkinGen = new WorldGenPumpkin();
	    this.glowstoneGen = new WorldGenAlgae(false, Content.plant, 0, Blocks.lava, 0);
	    this.cobwebGen = new WorldGenCaveCobwebs(Blocks.web, 0, biome.stoneBlock, biome.stoneBlockMeta);
	    this.stalactiteGen = new WorldGenStalactites();
	    this.vinesGen = new WorldGenVines();
	    this.cactusGen = new WorldGenCactus();
	    this.melonGen = new WorldGenMelon();

	    this.sandPerChunk = 4;
	    this.clayPerChunk = 3;
	    this.quicksandPerChunk = 0;
	    this.treesPerChunk = 0;
	    this.willowPerChunk = 0;
	    this.logsPerChunk = 0;
	    this.vinesPerChunk = 0;
	    this.flowersPerChunk = 2;
	    this.doubleFlowersPerChunk = 0;
	    this.grassPerChunk = 1;
	    this.doubleGrassPerChunk = 0;
	    this.enableFern = false;
	    this.enableSpecialGrasses = true;
	    this.deadBushPerChunk = 0;
	    this.glowstonePerChunk = 0;
	    this.mushroomsPerChunk = 0;
	    this.enableRandomMushroom = true;
	    this.canePerChunk = 0;
	    this.reedPerChunk = 1;
	    this.dryReedChance = 0.1F;
	    this.cornPerChunk = 0;
	    this.cactiPerChunk = 0;
	    this.melonPerChunk = 0.0F;
	    this.generateWater = true;
	    this.generateLava = true;
	    this.generateCobwebs = true;
	    this.generateSurfaceGravel = true;
	    this.generateDirt = true;
	    this.generateGravel = true;
		
		this.generateCoal = true;
		this.generateIron = true;
		this.generateGold = true;
		this.generateLapis = true;
		this.generateDiamond = true;
		this.generateEmerald = true;
		this.generateCopper = true;
		this.generateAluminium = true;
		this.generateLead = true;
		this.generateSilver = true;
		this.generateNickel = true;
		this.generateTin = true;
		this.generateQuartz = true;
		
		this.moltenOres = false;

	    this.treeClusterChance = -1;

	    this.treeTypes = new ArrayList();

	    this.structureRand = new Random();
	    this.randomStructures = new ArrayList();
	    //this.villages = new ArrayList(); TODO villages

	    this.biome = biome;
  	}

	public void addTree(AuroraTreeType type, int weight) {
	    this.treeTypes.add(new AuroraTreeType.WeightedTreeType(type, weight));
	}

	public void clearTrees() {
	    this.treeTypes.clear();
	}

	public AuroraTreeType getRandomTree(Random random) {
	    if (this.treeTypes.isEmpty()) {
	    	return AuroraTreeType.OAK;
	    }

	    WeightedRandom.Item item = WeightedRandom.getRandomItem(random, this.treeTypes);
	    return ((AuroraTreeType.WeightedTreeType)item).treeType;
	}

	public AuroraTreeType getRandomTreeForVariant(Random random, AuroraBiomeVariant variant) {
	    if (variant.treeTypes.isEmpty()) {
	    	return getRandomTree(random);
	    }

	    float f = variant.variantTreeChance;
	    
	    if (random.nextFloat() < f) {
	    	WeightedRandom.Item item = WeightedRandom.getRandomItem(random, variant.treeTypes);
	    	return ((AuroraTreeType.WeightedTreeType)item).treeType;
	    }

	    return getRandomTree(random);
	}

	public void genTree(World world, Random random, int i, int j, int k) {
	    WorldGenerator treeGen = this.biome.getTreeGen(world, random, i, j, k);
	    treeGen.generate(world, random, i, j, k);
	}

	public void setTreeCluster(int size, int chance) {
	    this.treeClusterSize = size;
	    this.treeClusterChance = chance;
	}

	public void resetTreeCluster() {
	    setTreeCluster(0, -1);
	}

	public void addRandomStructure(WorldGenerator structure, int chunkChance) {
	    this.randomStructures.add(new RandomStructure(structure, chunkChance));
	}

	public void clearRandomStructures() {
	    this.randomStructures.clear();
	}

	public void addVillage(AuroraVillageGen village) {
	   // this.villages.add(village); TODO villages
	}

	public void clearVillages() {
	    //this.villages.clear(); TODO villages
	}

	public void decorate(World world, Random random, int i, int j) {
	    this.worldObj = world;
	    this.rand = random;
	    this.chunkX = i;
	    this.chunkZ = j;
	    decorate();
	}

	private void decorate() {
	    AuroraBiomeVariant biomeVariant = ((BaseChunkManager)this.worldObj.getWorldChunkManager()).getBiomeVariantAt(this.chunkX + 8, this.chunkZ + 8);
	    AuroraBiome biome = (AuroraBiome)((BaseChunkManager)this.worldObj.getWorldChunkManager()).getBiomeGenAt(chunkX, chunkZ);

	    if(this.generateDirt) {
	    	addOre(new WorldGenMineable(Blocks.dirt, 0, WorldModule.dirtGen, biome), WorldModule.dirtGen);
	    }
	    
	    if(this.generateGravel) {
		    addOre(new WorldGenMineable(Blocks.gravel, 0, WorldModule.gravelGen, biome), WorldModule.gravelGen);
	    }
	    
	    if(this.generateCoal) {
	    	if(this.moltenOres && FluidRegistry.getFluid("coal.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 0, WorldModule.coalGen, biome), WorldModule.coalGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 0, WorldModule.coalGen, biome), WorldModule.coalGen);	
	    	}
	    }
	    
	    if(this.generateIron) {
	    	if(this.moltenOres && FluidRegistry.getFluid("iron.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 1, WorldModule.ironGen, biome), WorldModule.ironGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 1, WorldModule.ironGen, biome), WorldModule.ironGen);	
	    	}
	    }
	    
	    if(this.generateGold) {
	    	if(this.moltenOres && FluidRegistry.getFluid("gold.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 2, WorldModule.goldGen, biome), WorldModule.goldGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 2, WorldModule.goldGen, biome), WorldModule.goldGen);	
	    	}
	    }
	    
	    if(this.generateLapis) {
	    	if(this.moltenOres && FluidRegistry.getFluid("lapis.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 3, WorldModule.lapisGen, biome), WorldModule.lapisGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 3, WorldModule.lapisGen, biome), WorldModule.lapisGen);	
	    	}
	    }
	    
	    if(this.generateDiamond) {
	    	if(this.moltenOres && FluidRegistry.getFluid("diamond.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 4, WorldModule.diamondGen, biome), WorldModule.diamondGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 4, WorldModule.diamondGen, biome), WorldModule.diamondGen);	
	    	}
	    }
	    
	    if(this.generateEmerald) {
	    	if(this.moltenOres && FluidRegistry.getFluid("emerald.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 5, WorldModule.emeraldGen, biome), WorldModule.emeraldGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 5, WorldModule.emeraldGen, biome), WorldModule.emeraldGen);	
	    	}
	    }
	    
	    if(this.generateCopper) {
	    	if(this.moltenOres && FluidRegistry.getFluid("copper.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 6, WorldModule.copperGen, biome), WorldModule.copperGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 6, WorldModule.copperGen, biome), WorldModule.copperGen);	
	    	}
	    }
	    
	    if(this.generateAluminium) {
	    	if(this.moltenOres && FluidRegistry.getFluid("aluminium.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 7, WorldModule.aluminiumGen, biome), WorldModule.aluminiumGen);
	    	} else if(this.moltenOres && FluidRegistry.getFluid("aluminum.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 7, WorldModule.aluminiumGen, biome), WorldModule.aluminiumGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 7, WorldModule.aluminiumGen, biome), WorldModule.aluminiumGen);	
	    	}
	    }
	    
	    if(this.generateLead) {
	    	if(this.moltenOres && FluidRegistry.getFluid("lead.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 8, WorldModule.leadGen, biome), WorldModule.leadGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 8, WorldModule.leadGen, biome), WorldModule.leadGen);	
	    	}
	    }
	    
	    if(this.generateSilver) {
	    	if(this.moltenOres && FluidRegistry.getFluid("silver.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 9, WorldModule.silverGen, biome), WorldModule.silverGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 9, WorldModule.silverGen, biome), WorldModule.silverGen);	
	    	}
	    }
	    
	    if(this.generateNickel) {
	    	if(this.moltenOres && FluidRegistry.getFluid("nickel.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 10, WorldModule.nickelGen, biome), WorldModule.nickelGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 10, WorldModule.nickelGen, biome), WorldModule.nickelGen);	
	    	}
	    }
	    
	    if(this.generateTin) {
	    	if(this.moltenOres && FluidRegistry.getFluid("tin.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 11, WorldModule.tinGen, biome), WorldModule.tinGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 11, WorldModule.tinGen, biome), WorldModule.tinGen);	
	    	}
	    }
	    
	    if(this.generateQuartz) {
	    	if(this.moltenOres && FluidRegistry.getFluid("quartz.molten") != null) {
	    		addOre(new WorldGenMineable(Content.dummyLiquid, 12, WorldModule.quartzGen, biome), WorldModule.quartzGen);
	    	} else {
	    		addOre(new WorldGenMineable(Content.ore, 12, WorldModule.quartzGen, biome), WorldModule.quartzGen);	
	    	}
	    }
	    
	    generateOres();

	    if ((this.rand.nextBoolean()) && (this.generateCobwebs)) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(60);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.cobwebGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    for (int l = 0; l < 3; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(60);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.stalactiteGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    for (int l = 0; l < this.quicksandPerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.quicksandGen.generate(this.worldObj, this.rand, i, this.worldObj.getTopSolidOrLiquidBlock(i, k), k);
	    }

	    for (int l = 0; l < this.sandPerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.sandGen.generate(this.worldObj, this.rand, i, this.worldObj.getTopSolidOrLiquidBlock(i, k), k);
	    }
	    
	    for (int l = 0; l < this.clayPerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.clayGen.generate(this.worldObj, this.rand, i, this.worldObj.getTopSolidOrLiquidBlock(i, k), k);
	    }

	    if (this.rand.nextInt(120) == 0 && this.generateSurfaceGravel) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.surfaceGravelGen.generate(this.worldObj, this.rand, i, 0, k);
	    }	

	    if ((Math.abs(this.chunkX) > 4) && (Math.abs(this.chunkZ) > 4)) {
	    	long seed = this.chunkX * 1879267 ^ this.chunkZ * 67209689L;
	    	seed = seed * seed * 5829687L + seed * 2876L;
	    	this.structureRand.setSeed(seed);

	    	for (RandomStructure randomstructure : this.randomStructures) {
	    		if (this.structureRand.nextInt(randomstructure.chunkChance) == 0) {
	    			int i = this.chunkX + this.rand.nextInt(16) + 8;
	    			int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    			int j = this.worldObj.getTopSolidOrLiquidBlock(i, k);
	    			randomstructure.structureGen.generate(this.worldObj, this.rand, i, j, k);
	    		}
	    	}

	    	/*for (AuroraVillageGen village : this.villages) {
	    		village.generateInChunk(this.worldObj, this.rand, this.chunkX, this.chunkZ);
	    	}*///TODO villages
	    }

	    int trees = this.treesPerChunk;
	    trees = Math.round((trees + 1) * biomeVariant.treeFactor - 1.0F);

	    if (this.rand.nextFloat() < this.biome.getTreeIncreaseChance() * biomeVariant.treeFactor) {
	    	trees++;
	    }

	    float reciprocalTreeFactor = 1.0F / Math.max(biomeVariant.treeFactor, 0.001F);
	    int cluster = Math.round(this.treeClusterChance * reciprocalTreeFactor);
	   
	    if (cluster > 0) {
	    	Random chunkRand = new Random();
	    	long seed = this.chunkX / this.treeClusterSize * 3129871 ^ this.chunkZ / this.treeClusterSize * 116129781L;
	    	seed = seed * seed * 42317861L + seed * 11L;
	    	chunkRand.setSeed(seed);
	     
	    	if (chunkRand.nextInt(cluster) == 0) {
	    		trees += 6 + this.rand.nextInt(5);
	    	}
	    }

	    for (int l = 0; l < trees; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	      	WorldGenerator treeGen = getRandomTreeForVariant(this.rand, biomeVariant).create(false);
	      	treeGen.generate(this.worldObj, this.rand, i, this.worldObj.getHeightValue(i, k), k);
	    }

	    for (int l = 0; l < this.willowPerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	//WorldGenerator treeGen = AuroraTreeType.WILLOW_WATER.create(false);
	    	//treeGen.generate(this.worldObj, this.rand, i, this.worldObj.getHeightValue(i, k), k);
	    }

	    int fallenLeaves = trees / 2;
	    
	    if ((fallenLeaves <= 0) && (trees > 0)) {
	    	fallenLeaves = 1;
	    }
	    
	    for (int l = 0; l < fallenLeaves; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	//new WorldGenFallenLeaves().generate(this.worldObj, this.rand, i, this.worldObj.getTopSolidOrLiquidBlock(i, k), k);
	    }

	    for (int l = 0; l < this.logsPerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.logGen.generate(this.worldObj, this.rand, i, this.worldObj.getHeightValue(i, k), k);
	    }

	    for (int l = 0; l < this.vinesPerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = 64;
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.vinesGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    int flowers = this.flowersPerChunk;
	    flowers = Math.round(flowers * biomeVariant.flowerFactor);
	   
	    for (int l = 0; l < flowers; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(128);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.flowerGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    int doubleFlowers = this.doubleFlowersPerChunk;
	    doubleFlowers = Math.round(doubleFlowers * biomeVariant.flowerFactor);
	    
	    for (int l = 0; l < doubleFlowers; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(128);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	      	WorldGenerator doubleFlowerGen = this.biome.getRandomWorldGenForDoubleFlower(this.rand);
	      	doubleFlowerGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    int grasses = this.grassPerChunk;
	    grasses = Math.round(grasses * biomeVariant.grassFactor);
	    
	    for (int l = 0; l < grasses; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(128);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	WorldGenerator grassGen = this.biome.getRandomWorldGenForGrass(this.rand);
	    	grassGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    int doubleGrasses = this.doubleGrassPerChunk;
	    doubleGrasses = Math.round(doubleGrasses * biomeVariant.grassFactor);
	    
	    for (int l = 0; l < doubleGrasses; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(128);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	WorldGenerator grassGen = this.biome.getRandomWorldGenForDoubleGrass(this.rand);
	    	grassGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    for (int l = 0; l < this.deadBushPerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(128);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	new WorldGenDeadBush(Blocks.deadbush).generate(this.worldObj, this.rand, i, j, k);
	    }

	    for (int l = 0; l < this.glowstonePerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;

	    	for (int j = this.rand.nextInt(128); (j > 0) && (this.worldObj.getBlock(i, j + 1, k) == Blocks.air); j--) {
	    		this.glowstoneGen.generate(this.worldObj, this.rand, i, j, k);
	    	}
	    }

	    for (int l = 0; l < this.mushroomsPerChunk; l++) {
	    	if (this.rand.nextInt(4) == 0) {
	    		int i = this.chunkX + this.rand.nextInt(16) + 8;
	    		int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    		int j = this.worldObj.getHeightValue(i, k);
	    		this.mushroomBrownGen.generate(this.worldObj, this.rand, i, j, k);
	    	}

	    	if (this.rand.nextInt(8) == 0) {
	    		int i = this.chunkX + this.rand.nextInt(16) + 8;
	    		int k = this.chunkZ + this.rand.nextInt(16) + 8;
	        	int j = this.worldObj.getHeightValue(i, k);
	        	this.mushroomRedGen.generate(this.worldObj, this.rand, i, j, k);
	    	}
	    }

	    if (this.enableRandomMushroom) {
	    	if (this.rand.nextInt(4) == 0) {
	    		int i = this.chunkX + this.rand.nextInt(16) + 8;
	    		int j = this.rand.nextInt(128);
	    		int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    		this.mushroomBrownGen.generate(this.worldObj, this.rand, i, j, k);
	    	}

	    	if (this.rand.nextInt(8) == 0) {
	    		int i = this.chunkX + this.rand.nextInt(16) + 8;
	    		int j = this.rand.nextInt(128);
	    		int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    		this.mushroomRedGen.generate(this.worldObj, this.rand, i, j, k);
	    	}
	    }

	    for (int l = 0; l < this.canePerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(128);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.caneGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    for (int l = 0; l < 10; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(128);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.caneGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    for (int l = 0; l < this.reedPerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;

	    	for (int j = this.rand.nextInt(128); (j > 0) && (this.worldObj.getBlock(i, j - 1, k) == Blocks.air); j--) {
	    		if (this.rand.nextFloat() < this.dryReedChance) {
	    			this.dryReedGen.generate(this.worldObj, this.rand, i, j, k);
	    		} else {
	    			this.reedGen.generate(this.worldObj, this.rand, i, j, k);
	    		}
	    	}
	    }

	    for (int l = 0; l < this.cornPerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(128);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.cornGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    for (int l = 0; l < this.cactiPerChunk; l++) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(128);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.cactusGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    if (this.melonPerChunk > 0.0F) {
	    	int melonInt = MathHelper.floor_double(this.melonPerChunk);
	    	float melonF = this.melonPerChunk - melonInt;
	      
	    	for (int l = 0; l < melonInt; l++) {
	    		int i = this.chunkX + this.rand.nextInt(16) + 8;
	    		int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    		int j = this.worldObj.getHeightValue(i, k);
	    		this.melonGen.generate(this.worldObj, this.rand, i, j, k);
	    	}
	    	
	    	if (this.rand.nextFloat() < melonF) {
	    		int i = this.chunkX + this.rand.nextInt(16) + 8;
	        	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	        	int j = this.worldObj.getHeightValue(i, k);
	        	this.melonGen.generate(this.worldObj, this.rand, i, j, k);
	    	}
	    }

	    if ((this.flowersPerChunk > 0) && (this.rand.nextInt(32) == 0)) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(128);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	this.pumpkinGen.generate(this.worldObj, this.rand, i, j, k);
	    }

	    if ((this.flowersPerChunk > 0) && (this.rand.nextInt(4) == 0)) {
	    	int i = this.chunkX + this.rand.nextInt(16) + 8;
	    	int j = this.rand.nextInt(128);
	    	int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    	//new WorldGenBerryBush().generate(this.worldObj, this.rand, i, j, k);
	    }

	    if (this.generateWater) {
	    	WorldGenerator waterGen = new WorldGenStreams(Blocks.flowing_water);

	    	for (int l = 0; l < 50; l++) {
	    		int i = this.chunkX + this.rand.nextInt(16) + 8;
	    		int j = this.rand.nextInt(this.rand.nextInt(120) + 8);
	    		int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    		waterGen.generate(this.worldObj, this.rand, i, j, k);
	    	}

	    	if (this.biome.rootHeight > 1.0F) {
	    		for (int l = 0; l < 50; l++) {
	    			int i = this.chunkX + this.rand.nextInt(16) + 8;
	    			int j = 100 + this.rand.nextInt(150);
	    			int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    			waterGen.generate(this.worldObj, this.rand, i, j, k);
	    		}
	    	}
	    }

	    if (this.generateLava) {
	    	WorldGenerator lavaGen = new WorldGenStreams(Blocks.flowing_lava);

	    	int lava = 20;
	    	
	    	if ((this.biome instanceof BiomeVulcan)) {
	    		lava = 50;
	    	}

	    	for (int l = 0; l < lava; l++) {
	    		int i = this.chunkX + this.rand.nextInt(16) + 8;
	    		int j = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
	    		int k = this.chunkZ + this.rand.nextInt(16) + 8;
	        	lavaGen.generate(this.worldObj, this.rand, i, j, k);
	    	}
	    }

	    if (biomeVariant.boulderGen != null) {
	    	if (this.rand.nextInt(biomeVariant.boulderChance) == 0) {
	    		int boulders = MathHelper.getRandomIntegerInRange(this.rand, 1, biomeVariant.boulderMax);
	    		
	    		for (int l = 0; l < boulders; l++) {
	    			int i = this.chunkX + this.rand.nextInt(16) + 8;
	    			int k = this.chunkZ + this.rand.nextInt(16) + 8;
	    			biomeVariant.boulderGen.generate(this.worldObj, this.rand, i, this.worldObj.getHeightValue(i, k), k);
	    		}
	    	}
	    }
	}

	private void generateOres() {
		for (GenerateOre ore : this.biomeOres) {
			float oresToGenerate = ore.oreChance * this.biomeOreFactor * 0.01F;
			genStandardOre(oresToGenerate, ore.oreGen, ore.minHeight, ore.maxHeight);
	    }
	}

	private void genStandardOre(float oresToGenerate, WorldGenerator oreGen, int minHeight, int maxHeight) {
		while (oresToGenerate > 0.0F) {
			boolean generate = oresToGenerate >= 1.0F || this.rand.nextFloat() < oresToGenerate;
			--oresToGenerate;
			
			//System.out.println("oresToGenerate=" + Float.toString(oresToGenerate));
			
/*			if (oresToGenerate >= 1.0F) {
				generate = true;
			} else {
				generate = this.rand.nextFloat() < oresToGenerate;
			}
	     
			oresToGenerate -= 1.0F;*/

			if (generate) {
				int x = this.chunkX + this.rand.nextInt(16);
				int y = MathHelper.getRandomIntegerInRange(this.rand, minHeight, maxHeight);
				int z = this.chunkZ + this.rand.nextInt(16);
				oreGen.generate(this.worldObj, this.rand, x, y, z);
			}
	    }
	}

	private class RandomStructure {
		public WorldGenerator structureGen;
	    public int chunkChance;

	    public RandomStructure(WorldGenerator generator, int chance) {
	    	this.structureGen = generator;
	    	this.chunkChance = chance;
	    }
	}

	private class GenerateOre {
	    private WorldGenerator oreGen;
	    private float oreChance;
	    private int minHeight;
	    private int maxHeight;

	    public GenerateOre(WorldGenerator generator, float chance, int minH, int maxH) {
	    	this.oreGen = generator;
	    	this.oreChance = chance;
	    	this.minHeight = minH;
	    	this.maxHeight = maxH;
	    }
	}

    public void checkForVillages(World world, int i, int k, ChunkFlags chunkFlags) {
        chunkFlags.isVillage = false;
        chunkFlags.isFlatVillage = false;
        
        /*for (AuroraVillageGen village : this.villages) {
            List<AuroraVillageGen.AbstractInstance> instances = village.getNearbyVillagesAtPosition(world, i, k);
            
            if (!instances.isEmpty()) {
                chunkFlags.isVillage = true;
                
                for (AuroraVillageGen.AbstractInstance inst : instances) {
                    if (inst.isFlat()) {
                        chunkFlags.isFlatVillage = true;
                    }
                }
            } TODO villages
        }*/
    }
}