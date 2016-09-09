package projectaurora.world.layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.world.World;
import projectaurora.world.AuroraIntCache;
import projectaurora.world.biome.AuroraBiome;

public class AuroraGenLayerRemoveRivers extends BaseGenLayer {

	public AuroraGenLayerRemoveRivers(long seed, BaseGenLayer layer) {
		super(seed);
		this.baseParent = layer;
	}

	@Override
	public int[] getInts(World world, int x, int z, int xSize, int zSize) {
        int maxRange = 4;
        int[] biomes = this.baseParent.getInts(world, x - maxRange, z - maxRange, xSize + maxRange * 2, zSize + maxRange * 2);
        int[] ints = AuroraIntCache.get(world).getIntArray(xSize * zSize);
        
        for (int k2 = 0; k2 < zSize; ++k2) {
            for (int i2 = 0; i2 < xSize; ++i2) {
                this.initChunkSeed((long)(x + i2), (long)(z + k2));
                int biomeID = biomes[i2 + maxRange + (k2 + maxRange) * (xSize + maxRange * 2)];
                
                if (biomeID == AuroraBiome.lavaRiver.biomeID) {//TODO replace with generic river
                    int replaceID = -1;
                    
                    for (int range = 1; range <= maxRange; ++range) {
                        Map<Integer, Integer> viableBiomes = new HashMap<Integer, Integer>();
                        Map<Integer, Integer> viableBiomesWateryAdjacent = new HashMap<Integer, Integer>();
                        
                        for (int k3 = k2 - range; k3 <= k2 + range; ++k3) {
                            for (int i3 = i2 - range; i3 <= i2 + range; ++i3) {
                                if (Math.abs(i3 - i2) == range || Math.abs(k3 - k2) == range) {
                                    int subIndex = i3 + maxRange + (k3 + maxRange) * (xSize + maxRange * 2);
                                    int subBiomeID = biomes[subIndex];
                                    AuroraBiome subBiome = AuroraBiome.auroraBiomeList[subBiomeID];
                                    
                                    if (subBiome != AuroraBiome.lavaRiver) {//TODO replace with generic river
                                        boolean wateryAdjacent = subBiome.heightBaseParameter < 0.0F && range == 1;
                                        Map<Integer, Integer> srcMap = wateryAdjacent ? viableBiomesWateryAdjacent : viableBiomes;
                                        int count = 0;
                                        
                                        if (srcMap.containsKey(subBiomeID)) {
                                            count = srcMap.get(subBiomeID);
                                        }
                                        
                                        ++count;
                                        srcMap.put(subBiomeID, count);
                                    }
                                }
                            }
                        }
                        
                        Map<Integer, Integer> priorityMap = viableBiomes;
                        
                        if (!viableBiomesWateryAdjacent.isEmpty()) {
                            priorityMap = viableBiomesWateryAdjacent;
                        }
                        
                        if (!priorityMap.isEmpty()) {
                            List<Integer> maxCountBiomes = new ArrayList<Integer>();
                            int maxCount = 0;
                            
                            for (Map.Entry<Integer, Integer> e : priorityMap.entrySet()) {
                                int id = e.getKey();
                                int count2 = e.getValue();
                                
                                if (count2 > maxCount) {
                                    maxCount = count2;
                                }
                            }
                           
                            for (Map.Entry<Integer, Integer> e : priorityMap.entrySet()) {
                                int id = e.getKey();
                                int count2 = e.getValue();
                                
                                if (count2 == maxCount) {
                                    maxCountBiomes.add(id);
                                }
                            }
                            replaceID = maxCountBiomes.get(this.nextInt(maxCountBiomes.size()));
                            break;
                        }
                    }
                    
                    if (replaceID == -1) {
                        FMLLog.warning("WARNING! Map generation failed to replace map river at %d, %d", new Object[] { x, z });
                        ints[i2 + k2 * xSize] = 0;
                    } else {
                        ints[i2 + k2 * xSize] = replaceID;
                    }
                } else {
                    ints[i2 + k2 * xSize] = biomeID;
                }
            }
        }
        return ints;
	}
}