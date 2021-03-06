package projectaurora.world.vulcan;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.biome.AuroraBiomeVariant;
import projectaurora.world.biome.AuroraBiomeVariantStorage;
import projectaurora.world.gen.MapGenCaves;
import projectaurora.world.gen.MapGenRavine;
import projectaurora.world.gen.WorldGenLake;

public class VulcanChunkProvider implements IChunkProvider {
	//Kepler-10b, 1833K (~1560C), above iron melting point
	private World worldObj;
	private Random rand;
	private BiomeGenBase[] biomesForGeneration;
	private AuroraBiomeVariant[] variantsForGeneration;
	private static final double COORDINATE_SCALE = 684.41200000000003D;	 
	private static final double HEIGHT_SCALE = 1.0D;
	private static final double MAIN_NOISE_SCALE_XZ = 400.0D;
	private static final double MAIN_NOISE_SCALE_Y = 5000.0D;
	private static final double DEPTH_NOISE_SCALE = 200.0D;
	private static final double DEPTH_NOISE_EXP = 0.5D;
	private static final double HEIGHT_STRETCH = 6.0D;
	private static final double UPPER_LIMIT_SCALE = 512.0D;
	private static final double LOWER_LIMIT_SCALE = 512.0D;
	private int biomeSampleRadius;
	private int biomeSampleWidth;
	private NoiseGeneratorOctaves noiseGen1;
	private NoiseGeneratorOctaves noiseGen2;
	private NoiseGeneratorOctaves noiseGen3;
	private NoiseGeneratorOctaves noiseGen5;
	private NoiseGeneratorOctaves noiseGen6;
	private NoiseGeneratorOctaves stoneNoiseGen;
	private double[] noise1;
	private double[] noise2;
	private double[] noise3;	  
	private double[] noise5;
	private double[] noise6;
	private double[] stoneNoise = new double[256];	  
	private double[] heightNoise;
	private float[] biomeHeightNoise;
	private double[] blockHeightNoiseArray;
	private MapGenCaves caveGenerator = new MapGenCaves();
	private MapGenBase ravineGenerator = new MapGenRavine();
	private static final int seaLevel = 62;
	
	public VulcanChunkProvider(World world, long seed) {
		this.worldObj = world;
		this.rand = new Random(seed);
		
		this.noiseGen1 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen2 = new NoiseGeneratorOctaves(this.rand, 16);
		this.noiseGen3 = new NoiseGeneratorOctaves(this.rand, 8);
		this.stoneNoiseGen = new NoiseGeneratorOctaves(this.rand, 4);
		this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
		this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
		
		this.biomeSampleRadius = 6;
	    this.biomeSampleWidth = (2 * this.biomeSampleRadius + 1);

	    this.biomeHeightNoise = new float[this.biomeSampleWidth * this.biomeSampleWidth];
	    for (int i = -this.biomeSampleRadius; i <= this.biomeSampleRadius; i++) {
	    	for (int k = -this.biomeSampleRadius; k <= this.biomeSampleRadius; k++) {
	    		float f = 10.0F / MathHelper.sqrt_float(i * i + k * k + 0.2F);
	    		this.biomeHeightNoise[(i + this.biomeSampleRadius + (k + this.biomeSampleRadius) * this.biomeSampleWidth)] = f;
	    	}
	    }
	}
	
	private void generateTerrain(int chunkX, int chunkZ, Block[] blocks, byte[] meta, ChunkFlags chunkFlags) {
		VulcanChunkManager chunkManager = (VulcanChunkManager)this.worldObj.getWorldChunkManager();
        byte byte0 = 4;
        byte byte2 = 32;
        int k = byte0 + 1;
        byte byte3 = 33;
        int l = byte0 + 1;
        this.biomesForGeneration = chunkManager.getBiomesForGeneration(this.biomesForGeneration, chunkX * byte0 - this.biomeSampleRadius, chunkZ * byte0 - this.biomeSampleRadius, k + this.biomeSampleWidth, l + this.biomeSampleWidth);
        this.variantsForGeneration = chunkManager.getVariantsChunkGen(this.variantsForGeneration, chunkX * byte0 - this.biomeSampleRadius, chunkZ * byte0 - this.biomeSampleRadius, k + this.biomeSampleWidth, l + this.biomeSampleWidth, this.biomesForGeneration);
        this.heightNoise = this.initializeHeightNoise(this.heightNoise, chunkX * byte0, 0, chunkZ * byte0, k, byte3, l, chunkFlags);
        
        this.blockHeightNoiseArray = new double[blocks.length];
        
        for (int i2 = 0; i2 < byte0; ++i2) {
            for (int j2 = 0; j2 < byte0; ++j2) {
                for (int k2 = 0; k2 < byte2; ++k2) {
                    double d = 0.125;
                    double d2 = this.heightNoise[((i2 + 0) * l + j2 + 0) * byte3 + k2 + 0];
                    double d3 = this.heightNoise[((i2 + 0) * l + j2 + 1) * byte3 + k2 + 0];
                    double d4 = this.heightNoise[((i2 + 1) * l + j2 + 0) * byte3 + k2 + 0];
                    double d5 = this.heightNoise[((i2 + 1) * l + j2 + 1) * byte3 + k2 + 0];
                    double d6 = (this.heightNoise[((i2 + 0) * l + j2 + 0) * byte3 + k2 + 1] - d2) * d;
                    double d7 = (this.heightNoise[((i2 + 0) * l + j2 + 1) * byte3 + k2 + 1] - d3) * d;
                    double d8 = (this.heightNoise[((i2 + 1) * l + j2 + 0) * byte3 + k2 + 1] - d4) * d;
                    double d9 = (this.heightNoise[((i2 + 1) * l + j2 + 1) * byte3 + k2 + 1] - d5) * d;
                    
                    for (int l2 = 0; l2 < 8; ++l2) {
                        double d10 = 0.25;
                        double d11 = d2;
                        double d12 = d3;
                        double d13 = (d4 - d2) * d10;
                        double d14 = (d5 - d3) * d10;
                        
                        for (int i3 = 0; i3 < 4; ++i3) {
                            int j3 = i3 + i2 * 4 << 12 | 0 + j2 * 4 << 8 | k2 * 8 + l2;
                            double d15 = 0.25;
                            double d16 = (d12 - d11) * d15;
                            
                            for (int k3 = 0; k3 < 4; ++k3) {
                                int blockIndex = j3 + k3 * 256;
                                double blockHeightNoise = d11 + d16 * k3;
                                this.blockHeightNoiseArray[blockIndex] = blockHeightNoise;
                                
                            	BiomeGenBase biome = worldObj.getBiomeGenForCoords(chunkX, chunkZ);
                            	
                            	//System.out.println("blockHeightNoise:" + blockHeightNoise);
                            	//System.out.println("lavaHeightGen:" + k2 * 8 + l2);
                            	//System.out.println("blockIndex:" + blockIndex);
                                
                                if (blockHeightNoise > 0.0) {
                                	if(biome instanceof AuroraBiome) {
    									blocks[blockIndex] = ((AuroraBiome)biome).stoneBlock;//TODO meta
    									//meta[blockIndex] = (byte)((AuroraBiome)biome).stoneBlockMeta;
	    							}
                                } else if (k2 * 8 + l2 < 63) {
                                	if(biome instanceof AuroraBiome) {
    									blocks[blockIndex] = ((AuroraBiome)biome).dominantFluidBlock;//TODO meta
    									//meta[blockIndex] = (byte)((AuroraBiome)biome).dominantFluidMeta;
	    							}
                                } else {
                                    blocks[blockIndex] = Blocks.air;
                                }   
                            }
                            d11 += d13;
                            d12 += d14;
                        }
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                        d5 += d9;
                    }
                }
            }
        }
		/*VulcanChunkManager chunkManager = (VulcanChunkManager)this.worldObj.getWorldChunkManager();
	    byte byte0 = 4;
	    byte byte1 = 32;
	    int k = byte0 + 1;
	    byte byte3 = 33;
	    int l = byte0 + 1;
	    this.biomesForGeneration = chunkManager.getBiomesForGeneration(this.biomesForGeneration, chunkX * byte0 - this.biomeSampleRadius, chunkZ * byte0 - this.biomeSampleRadius, k + this.biomeSampleWidth, l + this.biomeSampleWidth);
	    this.variantsForGeneration = chunkManager.getVariantsChunkGen(this.variantsForGeneration, chunkX * byte0 - this.biomeSampleRadius, chunkZ * byte0 - this.biomeSampleRadius, k + this.biomeSampleWidth, l + this.biomeSampleWidth, this.biomesForGeneration);
	    this.heightNoise = initializeHeightNoise(this.heightNoise, chunkX * byte0, 0, chunkZ * byte0, k, byte3, l, chunkFlags);

	    this.blockHeightNoiseArray = new double[blocks.length];
	    
	    for (int x = 0; x < byte0; x++) {
	    	for (int z = 0; z < byte0; z++) {
	    		for (int y = 0; y < byte1; y++) {
	    			double d = 0.125D;
	    			double d1 = this.heightNoise[((x * l + z) * byte3 + y)];
	    			double d2 = this.heightNoise[((x * l + z + 1) * byte3 + y)];
	    			double d3 = this.heightNoise[(((x + 1) * l + z) * byte3 + y)];
	    			double d4 = this.heightNoise[(((x + 1) * l + z + 1) * byte3 + y)];
	    			double d5 = (this.heightNoise[((x * l + z) * byte3 + y + 1)] - d1) * d;
	    			double d6 = (this.heightNoise[((x * l + z + 1) * byte3 + y + 1)] - d2) * d;
	    			double d7 = (this.heightNoise[(((x + 1) * l + z) * byte3 + y + 1)] - d3) * d;
	    			double d8 = (this.heightNoise[(((x + 1) * l + z + 1) * byte3 + y + 1)] - d4) * d;
	    			
	    			for (int l1 = 0; l1 < 8; l1++) {
	    				double d9 = 0.25D;
	    				double d10 = d1;
	    				double d11 = d2;
	    				double d12 = (d3 - d1) * d9;
	    				double d13 = (d4 - d2) * d9;

	    				for (int i2 = 0; i2 < 4; i2++) {
	    					int j2 = i2 + x * 4 << 12 | 0 + z * 4 << 8 | y * 8 + l1;
	    					short s = 256;
	    					j2 -= s;
	    					double d14 = 0.25D;
	    					double d15 = (d11 - d10) * d14;
	    					double d16 = d10 - d15;

	    					for (int k2 = 0; k2 < 4; k2++) {
	    						int blockIndex = j2 + k2 * 256;
                                double blockHeightNoise = d10 + d15 * k2;
                                this.blockHeightNoiseArray[blockIndex] = blockHeightNoise;
                                
                                if (blockHeightNoise > 0.0) {
                                	BiomeGenBase biome = worldObj.getBiomeGenForCoords(chunkX, chunkZ);
	    							
	    							if(biome instanceof AuroraBiome) {
    									blocks[blockIndex] = ((AuroraBiome)biome).stoneBlock;//meta
    									//meta[blockIndex] = (byte)((AuroraBiome)biome).stoneBlockMeta;
	    							}
                                } else if (k2 * 8 + l1 <= 62) {
                                	BiomeGenBase biome = worldObj.getBiomeGenForCoords(chunkX, chunkZ);
	    							
	    							if(biome instanceof AuroraBiome) {
    									blocks[blockIndex] = ((AuroraBiome)biome).dominantFluidBlock;//meta
    									//meta[blockIndex] = (byte)((AuroraBiome)biome).dominantFluidMeta;
	    							}
                                } else {
                                    blocks[blockIndex] = Blocks.air;
                                }
	    					}

	    					d10 += d12;
	    					d11 += d13;
	    				}
	    				d1 += d5;
	    				d2 += d6;
	    				d3 += d7;
	    				d4 += d8;
	    			}
	    		}
	    	}
	    }*/
	}

	private void replaceBlocksForBiome(int originalX, int originalZ, Block[] blocks, byte[] metadata, BiomeGenBase[] biomeArray, AuroraBiomeVariant[] variantArray) {
		double d = 0.03125D;
	    this.stoneNoise = this.stoneNoiseGen.generateNoiseOctaves(this.stoneNoise, originalX * 16, originalZ * 16, 0, 16, 16, 1, d * 2.0D, d * 2.0D, d * 2.0D);
	    int ySize = blocks.length / 256;

	    for (int i1 = 0; i1 < 16; i1++) {
	    	for (int k1 = 0; k1 < 16; k1++) {
	    		int x = originalX * 16 + i1;
	    		int z = originalZ * 16 + k1;
	    		int xzIndex = i1 * 16 + k1;
	    		int xzIndexBiome = i1 + k1 * 16;
	    		AuroraBiome biome = (AuroraBiome)biomeArray[xzIndexBiome];
	    		AuroraBiomeVariant variant = variantArray[xzIndexBiome];

	    		int height = 0;
                
	    		for (int j = ySize - 1; j >= 0; --j) {
                    int index = xzIndex * ySize + j;
                    Block block = blocks[index];
                    
                    if (block.isOpaqueCube()) {
                        height = j;
                        break;
                    }
                }
	    		
                biome.generateBiomeTerrain(this.worldObj, this.rand, blocks, metadata, x, z, this.stoneNoise[xzIndex], height, variant);
                
                /*if (FixedStructures.hasMapFeatures(this.worldObj)) {
                    RoadGenerator.generateRoad(this.worldObj, this.rand, x, z, biome, blocks, metadata, this.blockHeightNoiseArray);
                    int lavaHeight = Mountains.getLavaHeight(x, z);
                    
                    if (lavaHeight > 0) {
                        for (int l = lavaHeight; l >= 0; --l) {
                            int index2 = xzIndex * ySize + l;
                            Block block2 = blocks[index2];
                            
                            if (block2.isOpaqueCube()) {
                                break;
                            }
                            
                            blocks[index2] = Blocks.lava;
                            metadata[index2] = 0;
                        }
                    }
                }*/
	    	}
	    }
	}
	
	@Override
	public Chunk loadChunk(int x, int z) {
		return provideChunk(x, z);
	}

	@Override
	public Chunk provideChunk(int x, int z) {
	    this.rand.setSeed(x * 341873128712L + z * 132897987541L);

	    VulcanChunkManager chunkManager = (VulcanChunkManager)this.worldObj.getWorldChunkManager();
	    Block[] blocks = new Block[65536];
	    byte[] metadata = new byte[65536];
        ChunkFlags chunkFlags = new ChunkFlags();
        
	    generateTerrain(x, z, blocks, metadata, chunkFlags);
	    this.biomesForGeneration = chunkManager.loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
	    this.variantsForGeneration = chunkManager.getBiomeVariants(this.variantsForGeneration, x * 16, z * 16, 16, 16);
	    replaceBlocksForBiome(x, z, blocks, metadata, this.biomesForGeneration, this.variantsForGeneration);

        this.caveGenerator.isVillage = chunkFlags.isVillage;
	    this.caveGenerator.func_151539_a(this, this.worldObj, x, z, blocks);
	    this.ravineGenerator.func_151539_a(this, this.worldObj, x, z, blocks);

	    Chunk chunk = new Chunk(this.worldObj, x, z);

	    ExtendedBlockStorage[] blockStorage = chunk.getBlockStorageArray();
	   
	    for (int i1 = 0; i1 < 16; i1++) {
	    	for (int k1 = 0; k1 < 16; k1++) {
	    		for (int j1 = 0; j1 < 256; j1++) {
	    			int blockIndex = i1 << 12 | k1 << 8 | j1;
	    			Block block = blocks[blockIndex];

	    			if ((block != null) && (block != Blocks.air)) {
	    				byte meta = metadata[blockIndex];

	    				int j2 = j1 >> 4;
	    			
	    				if (blockStorage[j2] == null) {
	    					blockStorage[j2] = new ExtendedBlockStorage(j2 << 4, true);
	    				}

	    				blockStorage[j2].func_150818_a(i1, j1 & 0xF, k1, block);
	    				blockStorage[j2].setExtBlockMetadata(i1, j1 & 0xF, k1, meta & 0xF);
	    			}
	    		}
	    	}
	    }
	    
	    byte[] biomes = chunk.getBiomeArray();
	    
	    for (int l = 0; l < biomes.length; l++) {
	    	biomes[l] = ((byte)this.biomesForGeneration[l].biomeID);
	    }

	    byte[] variants = new byte[256];
	    
	    for (int l = 0; l < variants.length; l++) {
	    	variants[l] = ((byte)this.variantsForGeneration[l].variantID);
	    }
	    
	    AuroraBiomeVariantStorage.setChunkBiomeVariants(this.worldObj, chunk, variants);

	    chunk.generateSkylightMap();
        //FixedStructures.nanoTimeElapsed = 0L;
	    return chunk;
	}
	
	private double[] initializeHeightNoise(double[] noise, int i, int j, int k, int xSize, int ySize, int zSize, ChunkFlags chunkFlags) {
	    if (noise == null) {
	    	noise = new double[xSize * ySize * zSize];
	    }

	    double xzNoiseScale = 400.0D;
	    int noiseCentralIndex = (xSize - 1) / 2 + this.biomeSampleRadius + ((zSize - 1) / 2 + this.biomeSampleRadius) * (xSize + this.biomeSampleWidth);
	    AuroraBiome noiseCentralBiome = (AuroraBiome)this.biomesForGeneration[noiseCentralIndex];
	    
	    if (noiseCentralBiome.biomeTerrain.hasXZScale()) {
	    	xzNoiseScale = noiseCentralBiome.biomeTerrain.getXZScale();
	    }

	    this.noise1 = this.noiseGen1.generateNoiseOctaves(this.noise1, i, j, k, xSize, ySize, zSize, 684.41200000000003D, 1.0D, 684.41200000000003D);
	    this.noise2 = this.noiseGen2.generateNoiseOctaves(this.noise2, i, j, k, xSize, ySize, zSize, 684.41200000000003D, 1.0D, 684.41200000000003D);
	    this.noise3 = this.noiseGen3.generateNoiseOctaves(this.noise3, i, j, k, xSize, ySize, zSize, 684.41200000000003D / xzNoiseScale, 0.0002D, 684.41200000000003D / xzNoiseScale);
	    this.noise5 = this.noiseGen5.generateNoiseOctaves(this.noise5, i, k, xSize, zSize, 1.121D, 1.121D, 0.5D);
	    this.noise6 = this.noiseGen6.generateNoiseOctaves(this.noise6, i, k, xSize, zSize, 200.0D, 200.0D, 0.5D);

	    int noiseIndexXZ = 0;
	    int noiseIndex = 0;

	    for (int i1 = 0; i1 < xSize; i1++) {
	    	for (int k1 = 0; k1 < zSize; k1++) {
	    		float totalBaseHeight = 0.0F;
	    		float totalHeightVariation = 0.0F;
	    		float totalHeightNoise = 0.0F;
	    		
	    		float totalFlatBiomeHeight = 0.0F;
	        	int totalFlatBiomeCount = 0;

	        	int centreBiomeIndex = i1 + this.biomeSampleRadius + (k1 + this.biomeSampleRadius) * (xSize + this.biomeSampleWidth);
	        	BiomeGenBase centreBiome = this.biomesForGeneration[centreBiomeIndex];
	        	AuroraBiomeVariant centreVariant = this.variantsForGeneration[centreBiomeIndex];

	        	float centreHeight = centreBiome.rootHeight + centreVariant.heightBoost;
	        	
	        	if (centreVariant.absoluteHeight) {
	        		centreHeight = centreVariant.heightBoost;
	        	}

	        	for (int i2 = -this.biomeSampleRadius; i2 <= this.biomeSampleRadius; i2++) {
	        		for (int k2 = -this.biomeSampleRadius; k2 <= this.biomeSampleRadius; k2++) {
	        			int biomeIndex = i1 + i2 + this.biomeSampleRadius + (k1 + k2 + this.biomeSampleRadius) * (xSize + this.biomeSampleWidth);
	        			BiomeGenBase biome = this.biomesForGeneration[biomeIndex];
	        			AuroraBiomeVariant variant = this.variantsForGeneration[biomeIndex];

	        			float baseHeight = biome.rootHeight + variant.heightBoost;
	        			float heightVariation = biome.heightVariation * variant.hillFactor;
	        			
	        			if (variant.absoluteHeight) {
	        				baseHeight = variant.heightBoost;
	        				heightVariation = variant.hillFactor;
	        			}

	        			float baseHeightPlus = baseHeight + 2.0F;
	            
	        			if (baseHeightPlus == 0.0F) {
	        				baseHeightPlus = 0.001F;
	        			}

	        			float heightNoise = this.biomeHeightNoise[(i2 + this.biomeSampleRadius + (k2 + this.biomeSampleRadius) * this.biomeSampleWidth)] / baseHeightPlus / 2.0F;
	        			heightNoise = Math.abs(heightNoise);

	        			if (baseHeight > centreHeight) {
	        				heightNoise /= 2.0F;
	        			}

	        			totalBaseHeight += baseHeight * heightNoise;
	        			totalHeightVariation += heightVariation * heightNoise;
	        			totalHeightNoise += heightNoise;

	        			float flatBiomeHeight = biome.rootHeight;
	        			boolean isWater = ((AuroraBiome)biome).rootHeight < 0.0F;
	        			
                        if (variant.absoluteHeight && variant.absoluteHeightLevel < 0.0f) {
                            isWater = true;
                        }
                        
                        if (isWater) {
                            flatBiomeHeight = baseHeight;
                        }
                        
                        totalFlatBiomeHeight += flatBiomeHeight;
                        ++totalFlatBiomeCount;
	        		}
	        	}

	        	float avgBaseHeight = totalBaseHeight / totalHeightNoise;
	        	float avgHeightVariation = totalHeightVariation / totalHeightNoise;
	        	float avgFlatBiomeHeight = totalFlatBiomeHeight / totalFlatBiomeCount;

	        	int xPos = i + i1 << 2;
	        	int zPos = k + k1 << 2;
	        	
                xPos += 2;
                zPos += 2;
	        	
                /*if (FixedStructures.hasMapFeatures(this.worldObj)) {
                    float roadNear = Roads.isRoadNear(xPos, zPos, 32);
                    if (roadNear >= 0.0f) {
                        float interpFactor = roadNear;
                        avgBaseHeight = avgFlatBiomeHeight + (avgBaseHeight - avgFlatBiomeHeight) * interpFactor;
                        avgHeightVariation *= interpFactor;
                    }
                    float mountain = Mountains.getTotalHeightBoost(xPos, zPos);
                    if (mountain > 0.005f) {
                        avgBaseHeight += mountain;
                        float mtnV = 0.2f;
                        float dv = avgHeightVariation - mtnV;
                        avgHeightVariation = mtnV + dv / (1.0f + mountain);
                    }
                }*/
                if (centreBiome instanceof AuroraBiome) {
                    AuroraBiome lb = (AuroraBiome)centreBiome;
                    lb.decorator.checkForVillages(this.worldObj, xPos, zPos, chunkFlags);
                    if (chunkFlags.isFlatVillage) {
                        avgBaseHeight = avgFlatBiomeHeight;
                        avgHeightVariation = 0.0f;
                    }
                }

	        	avgBaseHeight = (avgBaseHeight * 4.0F - 1.0F) / 8.0F;
	        
	        	if (avgHeightVariation == 0.0F) {
	        		avgHeightVariation = 0.001F;
	        	}

	        	double heightNoise = this.noise6[noiseIndexXZ] / 8000.0D;
	        
	        	if (heightNoise < 0.0D) {
	        		heightNoise = -heightNoise * 0.3D;
	        	}
	        
	        	heightNoise = heightNoise * 3.0D - 2.0D;
	        
	        	if (heightNoise < 0.0D) {
	        		heightNoise /= 2.0D;
	        		
	        		if (heightNoise < -1.0D) {
	        			heightNoise = -1.0D;
	        		}
	        		
	        		heightNoise /= 1.4D;
	        		heightNoise /= 2.0D;
	        	} else {
	        		if (heightNoise > 1.0D) {
	        			heightNoise = 1.0D;
	        		}
	        		
	        		heightNoise /= 8.0D;
	        	}
	        	
	        	noiseIndexXZ++;
	        	
	        	for (int j1 = 0; j1 < ySize; j1++) {
	        		double baseHeight = avgBaseHeight;
	        		double heightVariation = avgHeightVariation;

	        		baseHeight += heightNoise * 0.2D * centreVariant.hillFactor;
	        		baseHeight = baseHeight * ySize / 16.0D;
	        		double var28 = ySize / 2.0D + baseHeight * 4.0D;

	        		double totalNoise = 0.0D;
	        		double var32 = (j1 - var28) * 6.0D * 128.0D / 256.0D / heightVariation;
	          
	        		if (var32 < 0.0D) {
	        			var32 *= 4.0D;
	        		}

	        		double var34 = this.noise1[noiseIndex] / 512.0D;
	        		double var36 = this.noise2[noiseIndex] / 512.0D;
	        		double var38 = (this.noise3[noiseIndex] / 10.0D + 1.0D) / 2.0D * centreVariant.hillFactor;
	          
	        		if (var38 < 0.0D) {
	        			totalNoise = var34;
	        		} else if (var38 > 1.0D) {
	        			totalNoise = var36;
	        		} else {
	        			totalNoise = var34 + (var36 - var34) * var38;
	        		}
	        		
	        		totalNoise -= var32;
	        		
	        		if (j1 > ySize - 4) {
	        			double var40 = (j1 - (ySize - 4)) / 3.0F;
	        			totalNoise = totalNoise * (1.0D - var40) + -10.0D * var40;
	        		}

	        		noise[noiseIndex] = totalNoise;
	        		noiseIndex++;
	        	}
	    	}
	    }
	    return noise;
	}
	
	@Override
	public boolean chunkExists(int x, int z) {
		return true;
	}

	@Override
	public void populate(IChunkProvider provider, int x, int z) {
		BlockSand.fallInstantly = true;
		int k = x * 16;
		int l = z * 16;
		AuroraBiome biome = (AuroraBiome)this.worldObj.getBiomeGenForCoords(k + 16, l + 16);
		
	    this.rand.setSeed(this.worldObj.getSeed());
	    long l1 = this.rand.nextLong() / 2L * 2L + 1L;
	    long l2 = this.rand.nextLong() / 2L * 2L + 1L;
	    this.rand.setSeed(x * l1 + z * l2 ^ this.worldObj.getSeed());

	    if (this.rand.nextInt(4) == 0) {
	    	int i1 = k + this.rand.nextInt(16) + 8;
	    	int j1 = this.rand.nextInt(128);
	    	int k1 = l + this.rand.nextInt(16) + 8;
	      
	    	if (j1 < 60) {
	    		new WorldGenLake(Blocks.lava, 0, biome).generate(this.worldObj, this.rand, i1, j1, k1);
    		}
	    }

	    if (this.rand.nextInt(8) == 0) {
	    	int i1 = k + this.rand.nextInt(16) + 8;
	    	int j1 = this.rand.nextInt(this.rand.nextInt(120) + 8);
	    	int k1 = l + this.rand.nextInt(16) + 8;
	    	
	    	if (j1 < 60) {
	    		new WorldGenLake(Blocks.lava, 0, biome).generate(this.worldObj, this.rand, i1, j1, k1);
	    	}
	    }

	    biome.decorate(this.worldObj, this.rand, k, l);

	    if (biome.getChanceToSpawnAnimals() <= 1.0F) {
	    	if (this.rand.nextFloat() < biome.getChanceToSpawnAnimals()) {
	    		SpawnerAnimals.performWorldGenSpawning(this.worldObj, biome, k + 8, l + 8, 16, 16, this.rand);
	    	}
	    } else {
	    	for (int i1 = 0; i1 < MathHelper.floor_double(biome.getChanceToSpawnAnimals()); i1++) {
	    		SpawnerAnimals.performWorldGenSpawning(this.worldObj, biome, k + 8, l + 8, 16, 16, this.rand);
	    	}
	    }

	    k += 8;
	    l += 8;

	    for (int i1 = 0; i1 < 16; i1++) {
	    	for (int k1 = 0; k1 < 16; k1++) {
	    		int j1 = this.worldObj.getPrecipitationHeight(k + i1, l + k1);

	    		/*if (this.worldObj.isBlockFreezable(i1 + k, j1 - 1, k1 + l)) {
	    			this.worldObj.setBlock(i1 + k, j1 - 1, k1 + l, Blocks.ice, 0, 2);
	    		}

	    		if (this.worldObj.func_147478_e(i1 + k, j1, k1 + l, true)) {
	    			this.worldObj.setBlock(i1 + k, j1, k1 + l, Blocks.snow_layer, 0, 2);
	    		}*/
	    	}
	    }
	    BlockSand.fallInstantly = false;
	}

	@Override
	public boolean saveChunks(boolean flag, IProgressUpdate update) {
		return true;
	}
	
	@Override
	public void saveExtraData() {

	}

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public String makeString() {
		return "VulcanLevelSource";
	}

	@Override
	public List getPossibleCreatures(EnumCreatureType creatureType, int x, int y, int z) {
		BiomeGenBase biome = this.worldObj.getBiomeGenForCoords(x, z);
		
		if(biome == null) {
			return null;
		} else {
			return biome.getSpawnableList(creatureType);
		}
	}

	/**
	 * Might be 'find stronghold?'
	 */
	@Override
	public ChunkPosition func_147416_a(World world, String string, int x, int y, int z) {//no idea what this does, but apparently it's 'find stronghold'
		return null;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public void recreateStructures(int x, int z) {

	}
	
	public static class ChunkFlags {
        public boolean isVillage;
        public boolean isFlatVillage;
        
        private ChunkFlags() {
            this.isVillage = false;
            this.isFlatVillage = false;
        }
    }
}