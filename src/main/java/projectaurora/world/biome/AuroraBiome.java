package projectaurora.world.biome;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenDoublePlant;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.EnumHelper;
import projectaurora.core.Aurora;
import projectaurora.core.Content;
import projectaurora.core.MusicType;
import projectaurora.entity.AmbientCreature;
import projectaurora.world.AuroraTreeType;
import projectaurora.world.BaseChunkManager;
import projectaurora.world.WorldModule;
import projectaurora.world.gen.WorldGenAlgae;
import projectaurora.world.vulcan.BiomeLavaOcean;
import projectaurora.world.vulcan.BiomeVulcan;
import projectaurora.world.vulcan.BiomeVulcanRiver;

public class AuroraBiome extends BiomeGenBase {
	private static Class[][] correctCreatureTypeParams = { { EnumCreatureType.class, Class.class, Integer.TYPE, Material.class, Boolean.TYPE, Boolean.TYPE } };

	public static EnumCreatureType creatureType_Ambient = (EnumCreatureType)EnumHelper.addEnum(correctCreatureTypeParams, EnumCreatureType.class, "AuroraAmbient", new Object[] { AmbientCreature.class, Integer.valueOf(50), Material.air, Boolean.valueOf(true), Boolean.valueOf(false)});
	
	public static AuroraBiome[] auroraBiomeList = new AuroraBiome[256];
	public static List<AuroraBiome> vulcanList = new ArrayList();
	
	public static BiomeGenBase lavaOcean;
	public static BiomeGenBase lavaRiver;
	public static BiomeGenBase vulcan;
	
	public static final float deepOcean = -2.9F;
	public static final float river = -0.5F;
	public static final float deepSwamp = -0.3F;
	public static final float shores = -0.025F;
	public static final float shoresTop = 0.025F;
	public static final float lowPlains = 0.075F;
	public static final float highPlains = 0.4F;
	public static final float hill = 1.5F;
	
	public static final float testDeepOcean = -1.0F;
	public static final float testIslands = 0.3F;
	public static final float testNormalBottom = 0.15F;
	public static final float testNormalTop = 0.3F;
	
	public AuroraBiomeDecorator decorator;
	public int topBlockMeta = 0;
	public int fillerBlockMeta = 0;
	public Block stoneBlock = Blocks.stone;
	public int stoneBlockMeta = 0;
	public Block dominantFluidBlock = Blocks.water;
	public int dominantFluidMeta = 0;
	private List<AuroraBiomeVariant> biomeVariantsSmall = new ArrayList();
	private List<AuroraBiomeVariant> biomeVariantsLarge = new ArrayList();
	public float variantChance = 0.4F;
	public float heightBaseParameter;
	protected static NoiseGeneratorPerlin biomeTerrainNoise = new NoiseGeneratorPerlin(new Random(1955L), 1);
	protected static Random terrainRand = new Random();
	protected boolean enablePodzol = true;
	protected boolean enableRocky = true;

	public List spawnableGoodList = new ArrayList();
	public List spawnableEvilList = new ArrayList();
	private int goodWeight;
	private int evilWeight;
	protected List spawnableAmbientList = new ArrayList();
	private List spawnableTraders = new ArrayList();
	private int banditChance;
	//private Class<? extends EntityBandit> banditEntityClass;//TODO own bandits
	public List invasionSpawns = new ArrayList();

	public BiomeColors biomeColors = new BiomeColors(this);
	
	public BiomeTerrain biomeTerrain = new BiomeTerrain(this);

	public boolean isRiver = false;

	public boolean isOcean = false;
	
	private static Color waterColourCold = new Color(602979); //water colours?
	private static Color waterColourTropical = new Color(4973293);
	
	public AuroraBiome(int biomeID) {
		super(biomeID, false);
		
		auroraBiomeList[biomeID] = this;
		
		this.waterColorMultiplier = BiomeColors.DEFAULT_WATER;
		
		this.decorator = new AuroraBiomeDecorator(this);
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableCaveCreatureList.clear();
		
		//this.spawnableWhateverList.add
	}
	
	public static void initBiomes() {
		lavaOcean = new BiomeLavaOcean(0).setTemperatureRainfall(2F, 0F).setMinMaxHeight(testDeepOcean, testNormalTop/*deepOcean, shoresTop*/).setColor(1).setBiomeName("lavaOcean").setDisableRain();
		lavaRiver = new BiomeVulcanRiver(1).setTemperatureRainfall(2F, 0F).setMinMaxHeight(0F, 0F).setColor(32234).setBiomeName("vulcanRiver").setDisableRain();
		vulcan = new BiomeVulcan(2).setTemperatureRainfall(2F, 0F).setMinMaxHeight(testNormalBottom, testNormalTop/*shoresTop, highPlains*/).setColor(0).setBiomeName("vulcanMain").setDisableRain();
	}
	
	protected void addBiomeVariant(AuroraBiomeVariant variant) {
		if(variant.variantScale == AuroraBiomeVariant.VariantScale.ALL) {
			this.biomeVariantsLarge.add(variant);
			this.biomeVariantsSmall.add(variant);
		} else if(variant.variantScale == AuroraBiomeVariant.VariantScale.LARGE) {
			this.biomeVariantsLarge.add(variant);
		} else if(variant.variantScale == AuroraBiomeVariant.VariantScale.SMALL) {
			this.biomeVariantsSmall.add(variant);
		}
	}
	
	protected void addBiomeVariantSet(AuroraBiomeVariant[] set) {
		for(AuroraBiomeVariant variant : set) {
			addBiomeVariant(variant);
		}
	}
	
	protected void clearBiomeVariants() {
	    this.biomeVariantsLarge.clear();
	    this.biomeVariantsSmall.clear();
	    this.variantChance = 0.4F;
	}

	public List<AuroraBiomeVariant> getBiomeVariantsLarge() {
	    return this.biomeVariantsLarge;
	}

	public List<AuroraBiomeVariant> getBiomeVariantsSmall() {
	    return this.biomeVariantsSmall;
	}

	@Override
	public AuroraBiome setTemperatureRainfall(float min, float max) {
	    return (AuroraBiome)super.setTemperatureRainfall(min, max);
	}

	public boolean hasSeasonalGrass() {
	    return (this.temperature > 0.3F) && (this.temperature < 1.0F);
	}

	public AuroraBiome setMinMaxHeight(float min, float max) {
	    this.heightBaseParameter = min;
	    min -= 2.0F;
	    min += 0.2F;
	    this.rootHeight = min;
	    this.heightVariation = (max / 2.0F);
	    return this;
	}

	@Override
	public BiomeGenBase setColor(int color) {
	    color |= -16777216;
	    Map<Integer, Integer> colorsToBiomeIDs = new HashMap();

	    Integer existingBiomeID = (Integer)colorsToBiomeIDs.get(Integer.valueOf(color));
	   
	    if (existingBiomeID != null) {
	    	throw new RuntimeException("Biome (ID " + this.biomeID + ") is duplicating the color of another biome (ID " + existingBiomeID.intValue() + ")");
	    }

	    colorsToBiomeIDs.put(Integer.valueOf(color), Integer.valueOf(this.biomeID));
	    return super.setColor(color);
	}

	public final String getBiomeDisplayName() {
	    return StatCollector.translateToLocal("aurora.biome." + this.biomeName + ".name");
	}

	public BiomeGenBase.FlowerEntry getRandomFlower(Random random) {
	    return (BiomeGenBase.FlowerEntry)WeightedRandom.getRandomItem(random, this.flowers);
	}

	protected void registerPlainsFlowers() {
	    this.flowers.clear();
	    addFlower(Blocks.red_flower, 4, 3);
	    addFlower(Blocks.red_flower, 5, 3);
	    addFlower(Blocks.red_flower, 6, 3);
	    addFlower(Blocks.red_flower, 7, 3);
	    addFlower(Blocks.red_flower, 0, 20);
	    addFlower(Blocks.red_flower, 3, 20);
	    addFlower(Blocks.red_flower, 8, 20);
	    addFlower(Blocks.yellow_flower, 0, 30);
	    //addFlower(bluebell, 0, 5);
	}

	protected void registerForestFlowers() {
	    this.flowers.clear();
	    addDefaultFlowers();
	    //addFlower(bluebell, 0, 5);
	}

	protected void registerJungleFlowers() {
	    this.flowers.clear();
	    addDefaultFlowers();
	}

	protected void registerMountainsFlowers() {
	    this.flowers.clear();
	    addDefaultFlowers();
	    addFlower(Blocks.red_flower, 1, 10);
	    //addFlower(bluebell, 0, 5);
	}

	protected void registerTaigaFlowers() {
	    this.flowers.clear();
	    addDefaultFlowers();
	    addFlower(Blocks.red_flower, 1, 10);
	    //addFlower(bluebell, 0, 5); 
	}

	protected void registerSavannaFlowers() {
	    this.flowers.clear();
	    addDefaultFlowers();
	}

	protected void registerSwampFlowers() {
	    this.flowers.clear();
	    addDefaultFlowers();
	}

	protected void registerTravellingTrader(Class entityClass) {
	    this.spawnableTraders.add(entityClass);
	}

	protected final void clearTravellingTraders() {
	    this.spawnableTraders.clear();
	}

	public final boolean canSpawnTravellingTrader(Class entityClass) {
	    return this.spawnableTraders.contains(entityClass);
	}

	protected final void setBanditChance(int i) {
	    this.banditChance = i;
	}

	public final int getBanditChance() {
	    return this.banditChance;
	}

	/*protected final void setBanditEntityClass(Class<? extends EntityBandit> c) {
	    this.banditEntityClass = c;
	}//TODO own bandits

	public final Class<? extends EntityBandit> getBanditEntityClass() {
	    return this.banditEntityClass;
	}*/

	public void addBiomeF3(List info, World world, AuroraBiomeVariant variant, int i, int j, int k) {
	    int colorRGB = this.color & 0xFFFFFF;
	    String colorString = Integer.toHexString(colorRGB);
	    
	    while (colorString.length() < 6) {
	      colorString = "0" + colorString;
	    }

	    info.add("Biome: " + getBiomeDisplayName() + ", ID: " + this.biomeID + ", c: #" + colorString);
	    info.add("Variant: " + variant.variantName + ", loaded: " + AuroraBiomeVariantStorage.getSize(world));
  	}

	protected boolean hasDomesticAnimals() {
	    return false;
	}

	public boolean hasSky() {
	    return true;
	}

	public boolean getEnableRiver() {
	    return true;
	}
	
	@Override
    public void genTerrainBlocks(World world, Random random, Block[] blocks, byte[] meta, int x, int z, double stoneNoise) {
        this.generateBiomeTerrain(world, random, blocks, meta, x, z, stoneNoise, 63, AuroraBiomeVariant.RIVER);
    }
	
	public MusicType getMusicForBiome() {
		return MusicType.DEFAULT;
	}

	public void generateBiomeTerrain(World world, Random random, Block[] blocks, byte[] meta, int i, int k, double stoneNoise, int height, AuroraBiomeVariant variant) {
        int chunkX = i & 0xF;
        int chunkZ = k & 0xF;
        int xzIndex = chunkX * 16 + chunkZ;
        int ySize = blocks.length / 256;
		int seaLevel = 63;

	    int fillerDepthBase = (int)(stoneNoise / 4.0D + 5.0D + random.nextDouble() * 0.25D);
	    int fillerDepth = -1;
	    Block top = this.topBlock;
	    byte topMeta = (byte)this.topBlockMeta;
	    Block filler = this.fillerBlock;
	    byte fillerMeta = (byte)this.fillerBlockMeta;
	    
        if (this.enableRocky && height >= 90) {
            float hFactor = (height - 90) / 10.0f;
            float thresh = 1.2f - hFactor * 0.2f;
            thresh = Math.max(thresh, 0.0f);
            double d1 = AuroraBiome.biomeTerrainNoise.func_151601_a(i * 0.03, k * 0.03);
            double d2 = AuroraBiome.biomeTerrainNoise.func_151601_a(i * 0.3, k * 0.3);
            if (d1 + d2 > thresh) {
                if (random.nextInt(5) == 0) {
                    top = Blocks.gravel;
                    topMeta = 0;
                }
                else {
                    top = this.stoneBlock;
                    topMeta = (byte)this.stoneBlockMeta;
                }
                filler = this.stoneBlock;
                fillerMeta = (byte)this.stoneBlockMeta;
                int prevHeight = height;
                if (random.nextInt(20) == 0) {
                    ++height;
                }
                for (int j = height; j >= prevHeight; --j) {
                    int index = xzIndex * ySize + j;
                    
                    if(index >= 65536) {
                    	index = 65535;
                    	System.out.println("Over 65535");
                    }
                    
                    blocks[index] = this.stoneBlock;
                    meta[index] = (byte)this.stoneBlockMeta;
                    //System.out.println("index=" + index + ",blocks=" + blocks[index].getUnlocalizedName());
                }
            }
        }

	    if (this.enablePodzol) {
	    	boolean podzol = false;
	    	
	    	if (this.topBlock == Blocks.grass) {
	    		float trees = this.decorator.treesPerChunk + getTreeIncreaseChance();
	    		trees = Math.max(trees, variant.treeFactor * 0.5F);
	    		
	    		if (trees >= 1.0F)  {
	    			float thresh = 0.8F;
	    			thresh -= trees * 0.15F;
	    			thresh = Math.max(thresh, 0.0F);

	    			double d = 0.06D;
	    			double randNoise = biomeTerrainNoise.func_151601_a(i * d, k * d);
	          
	    			if (randNoise > thresh) {
	    				podzol = true;
	    			}
	    		}
	    	}
	    	
	    	if (podzol) {
	    		terrainRand.setSeed(world.getSeed());
	    		terrainRand.setSeed(terrainRand.nextLong() + i * 4668095025L + k * 1387590552L ^ world.getSeed());

	    		float pdzRand = terrainRand.nextFloat();
	        
	    		if (pdzRand < 0.35F) {
	    			top = Blocks.dirt;
	    			topMeta = 2;
	    		} else if (pdzRand < 0.5F) {
	    			top = Blocks.dirt;
	    			topMeta = 1;
	    		} else if (pdzRand < 0.51F) {
	    			top = Blocks.gravel;
	    			topMeta = 0;
	    		}
	    	}
	    }

	    boolean marsh = variant.hasMarsh;
	    
	    if (marsh) {
	    	double d1 = AuroraBiomeVariant.marshNoise.func_151601_a(i * 0.1D, k * 0.1D);

	    	if (d1 > -0.1) {
                int l = ySize - 1;
                
                while (l >= 0) {
                    int index2 = xzIndex * ySize + l;
                    
                    if (blocks[index2] == null || blocks[index2].getMaterial() != Material.air) {
                        if (l == seaLevel - 1 && blocks[index2] != Blocks.water) {
                            blocks[index2] = Blocks.water;
                            break;
                        }
                        break;
                    } else {
                        --l;
                    }
                }
            }
	    }

	    for (int j = ySize - 1; j >= 0; j--) {
	    	int index = xzIndex * ySize + j;
	      
	    	if (j <= 0 + random.nextInt(1)) {
	    		blocks[index] = Blocks.bedrock;//TODO replace bedrock?
	    	} else {
	    		Block block = blocks[index];
	    		int metadata = meta[index];
	        
	    		if (block == Blocks.air) {
	    			fillerDepth = -1;
	    		} else if (block == this.stoneBlock/*Blocks.stone*/ && metadata == this.stoneBlockMeta) {
	    			if (fillerDepth == -1) {
	    				if (fillerDepthBase <= 0) {
	    					top = Blocks.air;
	    					topMeta = 0;
	    					filler = this.stoneBlock/*Blocks.stone*/;
	    					fillerMeta = (byte)this.stoneBlockMeta;
	    				} else if ((j >= seaLevel - 4) && (j <= seaLevel + 1)) {
	    					top = this.topBlock;
	    					topMeta = (byte)this.topBlockMeta;
	    					filler = this.fillerBlock;
	    					fillerMeta = (byte)this.fillerBlockMeta;
	    				}

	    				if ((j < seaLevel) && (top == Blocks.air)) {
	    					top = this.dominantFluidBlock;
	    					topMeta = (byte)this.dominantFluidMeta;
	    				}

	    				fillerDepth = fillerDepthBase;

	    				if (j >= seaLevel - 1) {
	    					blocks[index] = top;
	    					meta[index] = topMeta;
	    				} else {
	    					blocks[index] = filler;
	    					meta[index] = fillerMeta;
	    				}
	    			} else if (fillerDepth > 0) {
	    				fillerDepth--;
	    				blocks[index] = filler;
	    				meta[index] = fillerMeta;

	    				/*if ((fillerDepth == 0) && (filler == Blocks.sand)) {
	    					if (fillerMeta == 1) {
	    						filler = Blocks.hardened_clay;
	    						fillerMeta = 0;
	    					} else {
	    						filler = Blocks.sandstone;
	    						fillerMeta = 0;
	    						System.out.println("purposefully coded sandstone inclusion");
	    					}
	    					
	    					fillerDepth = 10 + random.nextInt(4);
	    				}*/
	    				
	    				/*if ((((this instanceof BiomeGenWhiteRock)) || ((this instanceof BiomeGenWhiteRock2)) || ((this instanceof BiomeGenWhiteRock3))) && (fillerDepth == 0) && (filler == this.fillerBlock)) {
	    					fillerDepth = 8 + random.nextInt(3);
	    					filler = whiteRock;
	    					fillerMeta = 1;//TODO Example of rock fillers at certain depths
	    				}*/
	    			}
	    		}
	    	}
	    }

	    int mountainRockDepth = (int)(stoneNoise * 6.0D + 2.0D + random.nextDouble() * 0.25D);

	    /*if (this instanceof BiomeMountains && ((BiomeMountains)this).isSnowCovered() && this != Biome.mountainsFoothills) {
            int snowHeight = 120 - mountainRockDepth;
            
            for (int stoneHeight = snowHeight - 30, j2 = 255; j2 >= stoneHeight; --j2) {
                int index4 = xzIndex * ySize + j2;
                Block block2 = blocks[index4];
                
                if (j2 >= snowHeight && block2 == this.topBlock) {
                    blocks[index4] = Blocks.snow;
                    meta[index4] = 0;
                } else if (block2 == this.topBlock || block2 == this.fillerBlock) {
                    blocks[index4] = Blocks.stone;
                    meta[index4] = 0;
                }
            }
        }*/

	    /*if (this instanceof BiomeMountains2 && this != Biome.mountains2Foothills) {
            int stoneHeight2 = 110 - mountainRockDepth;
            
            for (int sandHeight = stoneHeight2 - 6, j2 = 255; j2 >= sandHeight; --j2) {
                int index4 = xzIndex * ySize + j2;
                Block block2 = blocks[index4];
                
                if (block2 == this.topBlock || block2 == this.fillerBlock) {
                    if (j2 >= stoneHeight2) {
                        blocks[index4] = Content.rock;
                        meta[index4] = 4;
                    } else {
                        blocks[index4] = Blocks.sand;
                        meta[index4] = 1;
                    }
                }
            }
        }*/
        variant.generateVariantTerrain(world, random, blocks, meta, i, k, height, this);
    }

	@Override
	public void decorate(World world, Random random, int i, int k) {
	    this.decorator.decorate(world, random, i, k);
	}

	@Override
	public List getSpawnableList(EnumCreatureType creatureType) {
		if (creatureType == creatureType_Ambient) {
			return this.spawnableAmbientList;
	    }
	    return super.getSpawnableList(creatureType); 
	}
	
	public float getChanceToSpawnAnimals() {
	    return 1.0F;
	}

	/**
	 * Means get random tree?
	 */
	@Override
	public final WorldGenAbstractTree func_150567_a(Random random) {
	    AuroraTreeType tree = this.decorator.getRandomTree(random);
	    return tree.create(false);
	}

	public final WorldGenAbstractTree getTreeGen(World world, Random random, int x, int y, int z) {
	    BaseChunkManager chunkManager = (BaseChunkManager)world.getWorldChunkManager();
	    AuroraBiomeVariant variant = chunkManager.getBiomeVariantAt(x, z);
	    AuroraTreeType tree = this.decorator.getRandomTreeForVariant(random, variant);
	    return tree.create(false);
	}

	public float getTreeIncreaseChance() {
	    return 0.1F;
	}

	@Override
	public final WorldGenerator getRandomWorldGenForGrass(Random random) {
	    GrassBlockAndMeta obj = getRandomGrass(random);
	    return new WorldGenTallGrass(obj.block, obj.meta);
	}

	public GrassBlockAndMeta getRandomGrass(Random random) {
	    boolean fern = this.decorator.enableFern;
	    boolean special = this.decorator.enableSpecialGrasses;

	    switch(AuroraBiome.auroraBiomeList.length) {
	    	case 0:
	    		return new GrassBlockAndMeta(Content.plant, 0);
	    	case 1:
	    		return new GrassBlockAndMeta(Content.plant, 0);
	    	case 2:
	    		return new GrassBlockAndMeta(Content.plant, 0);
    		default:
    			if ((fern) && (random.nextInt(3) == 0)) {
    		    	return new GrassBlockAndMeta(Blocks.tallgrass, 2);
    		    }

    		    if ((special) && (random.nextInt(500) == 0)) {
    		    	return new GrassBlockAndMeta(/*flax*/Blocks.tallgrass, 0);
    		    }
    		    
    		    if (random.nextInt(4) > 0) {
    		    	if (special) {
    		    		if (random.nextInt(200) == 0) {
    		    			return new GrassBlockAndMeta(Blocks.tallgrass, 0);//tallGrass, 3);
    		    		}
    		    		
    		    		if (random.nextInt(16) == 0) {
    		    			return new GrassBlockAndMeta(Blocks.tallgrass, 0);//tallGrass, 1);
    		    		}
    		    		
    		    		if (random.nextInt(10) == 0) {
    		    			return new GrassBlockAndMeta(Blocks.tallgrass, 0);//tallGrass, 2);
    		    		}
    		    	}
    		    	return new GrassBlockAndMeta(Blocks.tallgrass, 0);//tallGrass, 0);
    		    }
    		    
    		    if (random.nextInt(3) == 0) {
    		    	return new GrassBlockAndMeta(Blocks.yellow_flower, 0);//clover, 0);
    		    }

    		    return new GrassBlockAndMeta(Blocks.tallgrass, 1);
	    } 
	}

	public WorldGenerator getRandomWorldGenForDoubleGrass(Random random) {
	    WorldGenDoublePlant generator = new WorldGenDoublePlant();
	    WorldGenAlgae algae = new WorldGenAlgae(true, Content.plant, 0, Blocks.lava, 0);
		
		switch(AuroraBiome.auroraBiomeList.length) {
			case 0:
				return algae;
			case 1:
				return algae;
			case 2:
				return algae;
			default:			    
			    if ((this.decorator.enableFern) && (random.nextInt(4) == 0)) {
			    	generator.func_150548_a(3);
			    } else {
			    	generator.func_150548_a(2);
			    }
			    
			    return generator;
		}	
	}

	public WorldGenerator getRandomWorldGenForDoubleFlower(Random random) {
	    WorldGenDoublePlant doubleFlowerGen = new WorldGenDoublePlant();
	    WorldGenAlgae algae = new WorldGenAlgae(true, Content.plant, 0, Blocks.lava, 0);
	    
	    switch(AuroraBiome.auroraBiomeList.length) {
	    	case 0:
	    		return algae;
	    	case 1:
	    		return algae;
	    	case 2:
	    		return algae;
	    	default:		    
	    	    switch (random.nextInt(3)) {
	    	    	case 0:
	    	    		doubleFlowerGen.func_150548_a(1);
	    	    		break;
	    	    	case 1:
	    	    		doubleFlowerGen.func_150548_a(4);
	    	    		break;
	    	    	case 2:
	    	    		doubleFlowerGen.func_150548_a(5);
	    	    }  
	    	   
	    	    return doubleFlowerGen;
	    }

	}

	public int spawnCountMultiplier() {
	    return 1;
	}

	@Override
	public BiomeGenBase createMutation() {
	    return this;
	}

	@Override
	public boolean canSpawnLightningBolt() {
	    return (!getEnableSnow()) && (super.canSpawnLightningBolt());
	}

	@Override
	public boolean getEnableSnow() {
    	if((Aurora.isChristmas()) && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
	    	return true;
	    }
	    
	    return super.getEnableSnow();
	}

	public int getSnowHeight() {
	    return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public final int getBiomeGrassColor(int i, int j, int k) {
	    int color;
	    
	    if (this.biomeColors.grass != null){
	    	color = this.biomeColors.grass.getRGB();
	    } else {
	    	AuroraBiomeVariant variant = ((BaseChunkManager)Minecraft.getMinecraft().theWorld.getWorldChunkManager()).getBiomeVariantAt(i, k);
	    	float temp = getFloatTemperature(i, j, k) + variant.tempBoost;
	    	float rain = this.rainfall + variant.rainBoost;
	    	temp = MathHelper.clamp_float(temp, 0.0F, 1.0F);
	    	rain = MathHelper.clamp_float(rain, 0.0F, 1.0F);
	    	color = ColorizerGrass.getGrassColor(temp, rain);
	    }

	    return color;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public final int getBiomeFoliageColor(int i, int j, int k) {
	    int color;
	    
	    if (this.biomeColors.foliage != null) {
	    	color = this.biomeColors.foliage.getRGB();
	    } else {
	    	AuroraBiomeVariant variant = ((BaseChunkManager)Minecraft.getMinecraft().theWorld.getWorldChunkManager()).getBiomeVariantAt(i, k);
	      	float temp = getFloatTemperature(i, j, k) + variant.tempBoost;
	      	float rain = this.rainfall + variant.rainBoost;
	      	temp = MathHelper.clamp_float(temp, 0.0F, 1.0F);
	      	rain = MathHelper.clamp_float(rain, 0.0F, 1.0F);
	      	color = ColorizerFoliage.getFoliageColor(temp, rain);
	    }

	    return color;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public final int getSkyColorByTemp(float temp){
	    if (this.biomeColors.sky != null) {
	    	return this.biomeColors.sky.getRGB();
	    }

	    return super.getSkyColorByTemp(temp);
	}

	public final Vec3 getCloudColor(Vec3 clouds) {
	    if (this.biomeColors.clouds != null) {
	    	float[] colors = this.biomeColors.clouds.getColorComponents(null);
	    	clouds.xCoord *= colors[0];
	    	clouds.yCoord *= colors[1];
	    	clouds.zCoord *= colors[2];
	    }
	    return clouds;
	}

	public final Vec3 getFogColor(Vec3 fog) {
	    if (this.biomeColors.fog != null) {
	    	float[] colors = this.biomeColors.fog.getColorComponents(null);
	    	fog.xCoord *= colors[0];
	    	fog.yCoord *= colors[1];
	    	fog.zCoord *= colors[2];
	    }
	    return fog;
	}

	public final boolean hasFog() {
	    return this.biomeColors.foggy;
	}

	public static void updateWaterColor(int i, int j, int k) {
	    int min = 0;

	    float[] coldColors = waterColourCold.getColorComponents(null);
	    float[] tropicalColors = waterColourTropical.getColorComponents(null);

	    float dR = tropicalColors[0] - coldColors[0];
	    float dG = tropicalColors[1] - coldColors[1];
	    float dB = tropicalColors[2] - coldColors[2];

	    float r = dR;
	    float g = dG;
	    float b = dB;

	    r += coldColors[0];
	    g += coldColors[1];
	    b += coldColors[2];
	    Color water = new Color(r, g, b);
	    int waterRGB = water.getRGB();


    	for (AuroraBiome biome : auroraBiomeList) {
    		if ((biome != null) && (!biome.biomeColors.hasCustomWater())) {
    			biome.biomeColors.updateWater(waterRGB);
    		}
    	}
	}

	public static class GrassBlockAndMeta {
	    public final Block block;
	    public final int meta;

	    public GrassBlockAndMeta(Block b, int i) {
	    	this.block = b;
	    	this.meta = i;
	    }
	}

	public static class BiomeTerrain {
	    private AuroraBiome theBiome;
	    private double xzScale = -1.0D;

	    public BiomeTerrain(AuroraBiome biome) {
	    	this.theBiome = biome;
	    }

	    public void setXZScale(double d) {
	    	this.xzScale = d;
	    }

	    public void resetXZScale() {
	    	setXZScale(-1.0D);
	    }

	    public boolean hasXZScale() {
	    	return this.xzScale != -1.0D;
	    }

	    public double getXZScale() {
	    	return this.xzScale;
	    }
	}

	public static class BiomeColors {
	    private AuroraBiome theBiome;
	    private Color grass;
	    private Color foliage;
	    private Color sky;
	    private Color clouds;
	    private Color fog;
	    private boolean foggy;
	    private boolean hasCustomWater = false;

	    private static int DEFAULT_WATER = 7186907;

	    public BiomeColors(AuroraBiome biome) {
	    	this.theBiome = biome;
	    }

	    public void setGrass(int rgb) {
	    	this.grass = new Color(rgb);
	    }

	    public void resetGrass() {
	    	this.grass = null;
	    }

	    public void setFoliage(int rgb) {
	    	this.foliage = new Color(rgb);
	    }

	    public void resetFoliage() {
	    	this.foliage = null;
	    }

	    public void setSky(int rgb) {
	    	this.sky = new Color(rgb);
	    }

	    public void resetSky() {
	    	this.sky = null;
	    }

	    public void setClouds(int rgb) {
	    	this.clouds = new Color(rgb);
	    }

	    public void resetClouds() {
	    	this.clouds = null;
	    }

	    public void setFog(int rgb) {
	    	this.fog = new Color(rgb);
	    }

	    public void resetFog() {
	    	this.fog = null;
	    }

	    public void setFoggy(boolean flag) {
	    	this.foggy = flag;
	    }

	    public void setWater(int rgb) {
	    	this.theBiome.waterColorMultiplier = rgb;
	      
	    	if (rgb != DEFAULT_WATER) {
	    		this.hasCustomWater = true;
	    	}
	    }

	    public void resetWater() {
	    	setWater(DEFAULT_WATER);
	    }

	    public boolean hasCustomWater() {
	    	return this.hasCustomWater;
	    }

	    public void updateWater(int rgb) {
	    	this.theBiome.waterColorMultiplier = rgb;
	    }
	}

	public static List getBiomesForCurrentDimension(World world) {
		int dimensionID = world.provider.dimensionId;
		
		if(dimensionID == WorldModule.vulcanID) {
			return vulcanList;
		}
		System.out.println("Couldn't get biomeList, defaulting to vulcan");
		return vulcanList;
	}
}