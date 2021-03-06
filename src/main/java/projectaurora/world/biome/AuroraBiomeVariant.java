package projectaurora.world.biome;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenerator;
import projectaurora.core.Content;
import projectaurora.world.AuroraTreeType;
import projectaurora.world.AuroraTreeType.WeightedTreeType;
import projectaurora.world.gen.WorldGenBoulder;

public class AuroraBiomeVariant {
	private static AuroraBiomeVariant[] allVariants = new AuroraBiomeVariant[256];

	public static AuroraBiomeVariant STANDARD = new AuroraBiomeVariant(0, "standard", VariantScale.ALL);
	public static AuroraBiomeVariant FLOWERS = new AuroraBiomeVariant(1, "flowers", VariantScale.SMALL).setFlowers(10.0F);
	public static AuroraBiomeVariant FOREST = new AuroraBiomeVariant(2, "forest", VariantScale.ALL).setTemperatureRainfall(0.0F, 0.3F).setTrees(8.0F).setGrass(2.0F);
	public static AuroraBiomeVariant FOREST_LIGHT = new AuroraBiomeVariant(3, "forest_light", VariantScale.ALL).setTemperatureRainfall(0.0F, 0.2F).setTrees(3.0F).setGrass(2.0F);
	public static AuroraBiomeVariant STEPPE = new AuroraBiomeVariant(4, "steppe", VariantScale.LARGE).setTemperatureRainfall(0.0F, -0.1F).setHeight(0.0F, 0.1F).setTrees(0.01F).setGrass(3.0F);
	public static AuroraBiomeVariant STEPPE_BARREN = new AuroraBiomeVariant(5, "steppe_barren", VariantScale.LARGE).setTemperatureRainfall(0.1F, -0.2F).setHeight(0.0F, 0.1F).setTrees(0.01F).setGrass(0.2F);
	public static AuroraBiomeVariant HILLS = new AuroraBiomeVariant(6, "hills", VariantScale.ALL).setTemperatureRainfall(-0.1F, -0.1F).setHeight(0.5F, 1.5F).setGrass(0.5F);
	public static AuroraBiomeVariant HILLS_FOREST = new AuroraBiomeVariant(7, "hills_forest", VariantScale.ALL).setTemperatureRainfall(-0.1F, 0.0F).setHeight(0.5F, 1.5F).setTrees(3.0F);
	public static AuroraBiomeVariant MOUNTAIN = new AuroraBiomeVariant(8, "mountain", VariantScale.ALL).setTemperatureRainfall(-0.1F, -0.2F).setHeight(1.2F, 3.0F);
	public static AuroraBiomeVariant CLEARING = new AuroraBiomeVariant(9, "clearing", VariantScale.SMALL).setHeight(0.0F, 0.5F).setTrees(0.0F).setGrass(2.0F).setFlowers(3.0F);
	public static AuroraBiomeVariant DENSEFOREST = new AuroraBiomeVariant(10, "denseForest", VariantScale.LARGE).setTemperatureRainfall(0.1F, 0.3F).setHeight(0.5F, 2.0F).setTrees(8.0F).setGrass(2.0F).setLargeTrees(600).setHugeTrees(100)/*.addTreeTypes(0.5F, new Object[] { AuroraTreeType.OAK_LARGE, Integer.valueOf(600), AuroraTreeType.OAK_HUGE, Integer.valueOf(100) })*/;
  	public static AuroraBiomeVariant DEADFOREST = new AuroraBiomeVariant(11, "deadForest", VariantScale.ALL).setTemperatureRainfall(0.0F, -0.3F).setTrees(3.0F).setGrass(0.5F).setDeadTrees(100)/*.addTreeTypes(0.5F, new Object[] { AuroraTreeType.OAK_DEAD, Integer.valueOf(100) })*/;
  	public static AuroraBiomeVariant SHRUBLAND = new AuroraBiomeVariant(12, "shrubland", VariantScale.ALL).setTemperatureRainfall(0.0F, 0.3F).setTrees(6.0F).setShrubs(100)/*.addTreeTypes(0.7F, new Object[] { AuroraTreeType.OAK_SHRUB, Integer.valueOf(100) })*/;
  	public static AuroraBiomeVariant SWAMP_LOWLAND = new AuroraBiomeVariant(13, "swampLowland", VariantScale.SMALL).setHeight(-0.12F, 0.2F).setTrees(0.5F).setGrass(5.0F).setMarsh();
  	public static AuroraBiomeVariant SWAMP_UPLAND = new AuroraBiomeVariant(14, "swampUpland", VariantScale.SMALL).setHeight(0.12F, 1.0F).setTrees(6.0F).setGrass(5.0F);
  	public static AuroraBiomeVariant LAKE = new AuroraBiomeVariant(15, "lake", VariantScale.NONE).setHeight(-2.1F, 0.05F).setAbsoluteHeight(-0.5f, 0.05f);
  	public static AuroraBiomeVariant BOULDERS = new AuroraBiomeVariant(16, "boulders", VariantScale.LARGE).setBoulders(new WorldGenBoulder(/*Content.redSandstone*/Blocks.bookshelf, 1, 1, 3), 2, 4);
	public static AuroraBiomeVariant ISLAND = new AuroraBiomeVariant(17, "island", VariantScale.SMALL).setHeight(0.12F, 1.0F);
	public static AuroraBiomeVariant RIVER = new AuroraBiomeVariant(18, "river", VariantScale.NONE).setHeight(-0.5F, 0.05F).setAbsoluteHeight(-0.5f, 0.05f);
  	
  	public static AuroraBiomeVariant[] SET_NORMAL = { FLOWERS, FOREST, FOREST_LIGHT, STEPPE, STEPPE_BARREN, HILLS, HILLS_FOREST, DENSEFOREST, DEADFOREST, SHRUBLAND };
  	public static AuroraBiomeVariant[] SET_FOREST = { FLOWERS, HILLS, CLEARING };
  	public static AuroraBiomeVariant[] SET_MOUNTAINS = { FOREST, FOREST_LIGHT };
  	public static AuroraBiomeVariant[] SET_SWAMP = { SWAMP_LOWLAND, SWAMP_LOWLAND, SWAMP_LOWLAND, SWAMP_UPLAND };
	  
	public static NoiseGeneratorPerlin marshNoise = new NoiseGeneratorPerlin(new Random(444L), 1);
	public static NoiseGeneratorPerlin podzolNoise = new NoiseGeneratorPerlin(new Random(58052L), 1);
	public final int variantID;
	public final String variantName;
	public final VariantScale variantScale;
	public float tempBoost = 0.0F;
	public float rainBoost = 0.0F;

	public boolean absoluteHeight = false;
	public float absoluteHeightLevel = 0.0F;
	public float heightBoost = 0.0F;
	public float hillFactor = 1.0F;
	public float treeFactor = 1.0F;
	public float grassFactor = 1.0F;
	public float flowerFactor = 1.0F;
	
	public float largeTreeFactor = 0.0F;
	public float hugeTreeFactor = 0.0F;
	public float deadTreeFactor = 0.0F;
	public float shrubFactor = 0.0F;

	public boolean hasMarsh = false;
    public boolean disableStructures;

	public List<AuroraTreeType.WeightedTreeType> treeTypes = new ArrayList();
	public float variantTreeChance = 0.0F;
	public WorldGenerator boulderGen;
	public int boulderChance = 0;
	public int boulderMax = 1;

	public AuroraBiomeVariant(int id, String name, VariantScale scale) {
		if(allVariants[id] != null) {
			throw new IllegalArgumentException("Biome Variant already exists at " + id + ", goddammit!");
		}
		
		this.variantID = id;
		allVariants[id] = this;
		this.variantName = name;
		this.variantScale = scale;
	}
	
	public static AuroraBiomeVariant getVariantForID(int id) {
		AuroraBiomeVariant variant = allVariants[id];
	    
		if (variant == null) {
			return STANDARD;
	    }

	    return variant;
    }
	
	private AuroraBiomeVariant setTemperatureRainfall(float temp, float rain) {
    	this.tempBoost = temp;
    	this.rainBoost = rain;
	    return this;
	}

	private AuroraBiomeVariant setHeight(float height, float hills) {
	    this.heightBoost = height;
	    this.hillFactor = hills;
	    return this;
  	}

	private AuroraBiomeVariant setAbsoluteHeight(float height, float hills) {
        this.absoluteHeight = true;
        this.absoluteHeightLevel = height;
        float f = height - 2.0f;
        f += 0.2f;
        this.heightBoost = f;
        this.hillFactor = hills;
        return this;
	}

	private AuroraBiomeVariant setTrees(float factor) {
	    this.treeFactor = factor;
	    return this;
	}
	
	private AuroraBiomeVariant setLargeTrees(float factor) {
		this.largeTreeFactor  = factor * 0.5F;
		return this;
	}
	
	private AuroraBiomeVariant setHugeTrees(float factor) {
		this.hugeTreeFactor  = factor * 0.5F;
		return this;
	}
	
	private AuroraBiomeVariant setDeadTrees(float factor) {
		this.deadTreeFactor  = factor * 0.5F;
		return this;
	}
	
	private AuroraBiomeVariant setShrubs(float factor) {
		this.shrubFactor  = factor * 0.7F;
		return this;
	}

	private AuroraBiomeVariant setGrass(float factor) {
	    this.grassFactor = factor;
	    return this;
	}

	private AuroraBiomeVariant setFlowers(float factor) {
	    this.flowerFactor = factor;
	    return this;
	}

  	private AuroraBiomeVariant addTreeTypes(float factor, Object... trees) {
	    this.variantTreeChance = factor;
	    
	    for (int i = 0; i < trees.length / 2; i++) {
	    	Object obj1 = trees[(i * 2)];
	    	Object obj2 = trees[(i * 2 + 1)];
	    	this.treeTypes.add(new AuroraTreeType.WeightedTreeType((AuroraTreeType)obj1, ((Integer)obj2).intValue()));
	    }
	    
	    return this;
  	}
  	
    public AuroraTreeType getRandomTree(Random random) {
        WeightedRandom.Item item = WeightedRandom.getRandomItem(random, (Collection)this.treeTypes);
        return ((AuroraTreeType.WeightedTreeType)item).treeType;
    }
    
    public AuroraTreeType getRandomLargeTree(Random random) {
        WeightedRandom.Item item = WeightedRandom.getRandomItem(random, (Collection)this.treeTypes);
        
        if(((AuroraTreeType.WeightedTreeType)item).treeType.toString().contains("LARGE") || ((AuroraTreeType.WeightedTreeType)item).treeType.toString().contains("HUGE")) {
            return ((AuroraTreeType.WeightedTreeType)item).treeType;
        } else {
        	return AuroraTreeType.OAK_LARGE;
        }
    }

    public AuroraTreeType getRandomDeadTree(Random random) {
        WeightedRandom.Item item = WeightedRandom.getRandomItem(random, (Collection)this.treeTypes);
        
        if(((AuroraTreeType.WeightedTreeType)item).treeType.toString().contains("DEAD")) {
            return ((AuroraTreeType.WeightedTreeType)item).treeType;
        } else {
        	return AuroraTreeType.OAK_DEAD;
        }
    }
    
    public AuroraTreeType getRandomShrub(Random random) {
        WeightedRandom.Item item = WeightedRandom.getRandomItem(random, (Collection)this.treeTypes);
        
        if(((AuroraTreeType.WeightedTreeType)item).treeType.toString().contains("SHRUB")) {
            return ((AuroraTreeType.WeightedTreeType)item).treeType;
        } else {
        	return AuroraTreeType.OAK_SHRUB;
        }
    }

  	private AuroraBiomeVariant setMarsh() {
	    this.hasMarsh = true;
	    return this;
  	}
  	
    protected AuroraBiomeVariant disableStructures() {
        this.disableStructures = true;
        return this;
    }

  	private AuroraBiomeVariant setBoulders(WorldGenerator boulder, int chance, int num) {
	    if (num < 1) {
	      throw new IllegalArgumentException("n must be > 1");
	    }

	    this.boulderGen = boulder;
	    this.boulderChance = chance;
	    this.boulderMax = num;
	    return this;
  	}
	
	public static enum VariantScale {
		LARGE,
		SMALL,
		ALL,
		NONE;
	}

	public void generateVariantTerrain(World world, Random random, Block[] blocks, byte[] meta, int x, int z, int height, AuroraBiome auroraBiome) {
		
	}
	
	public void decorateVariant(World world, Random random, int x, int z, AuroraBiome biome) {
		
	}
}