package projectaurora.world.biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import projectaurora.core.PacketHandler;
import projectaurora.world.biome.PacketBiomeVariantsWatch;

public class AuroraBiomeVariantStorage {
	private static Map<Integer, Map<ChunkCoordIntPair, byte[]>> chunkVariantMap = new HashMap();
	private static Map<Integer, Map<ChunkCoordIntPair, byte[]>> chunkVariantMapClient = new HashMap();

	private static Map<ChunkCoordIntPair, byte[]> getDimensionChunkMap(World world) {
	    Map<Integer, Map<ChunkCoordIntPair, byte[]>> sourcemap;
	    
	    if (!world.isRemote) {
	    	sourcemap = chunkVariantMap;
	    } else {
	    	sourcemap = chunkVariantMapClient;
	    }

	    int dimID = getCurrentDimension(world);
	    
	    Map map = (Map)sourcemap.get(dimID);
	    
	    if (map == null) {
	    	map = new HashMap();
	    	sourcemap.put(dimID, map);
	    }
	    return map;
	}

	private static int getCurrentDimension(World world) {
		if(world != null) {
			return world.provider.dimensionId;
		}
		return 0;
	}

	private static ChunkCoordIntPair getChunkKey(Chunk chunk) {
	    return new ChunkCoordIntPair(chunk.xPosition, chunk.zPosition);
	}

	public static byte[] getChunkBiomeVariants(World world, Chunk chunk) {
	    return getChunkBiomeVariants(world, getChunkKey(chunk));
	}

	public static byte[] getChunkBiomeVariants(World world, ChunkCoordIntPair chunk) {
	    return (byte[])getDimensionChunkMap(world).get(chunk);
	}

	public static void setChunkBiomeVariants(World world, Chunk chunk, byte[] variants) {
	    setChunkBiomeVariants(world, getChunkKey(chunk), variants);
	}

	public static void setChunkBiomeVariants(World world, ChunkCoordIntPair chunk, byte[] variants) {
	    getDimensionChunkMap(world).put(chunk, variants);
	}

	public static void clearChunkBiomeVariants(World world, Chunk chunk) {
	    clearChunkBiomeVariants(world, getChunkKey(chunk));
	}

	public static void clearChunkBiomeVariants(World world, ChunkCoordIntPair chunk) {
	    getDimensionChunkMap(world).remove(chunk);
	}

	public static void loadChunkVariants(World world, Chunk chunk, NBTTagCompound data) {
	    if (getChunkBiomeVariants(world, chunk) == null) {
	    	byte[] variants;
	    	
	    	if (data.hasKey("AuroraBiomeVariants")) {
	    		variants = data.getByteArray("AuroraBiomeVariants");
	    	} else {
	    		variants = new byte[256];
	    	}
	    	setChunkBiomeVariants(world, chunk, variants);
    	}
	}

	public static void saveChunkVariants(World world, Chunk chunk, NBTTagCompound data) {
	    byte[] variants = getChunkBiomeVariants(world, chunk);
	    
	    if (variants != null) {
	    	data.setByteArray("AuroraBiomeVariants", variants);
	    }
	}

	public static void clearAllVariants(World world) {
	    getDimensionChunkMap(world).clear();
	    FMLLog.info("Unloading Aurora biome variants in %s", new Object[] { world.getProviderName().substring(0, world.getProviderName().length() - 11) });
	}

	public static void performCleanup(WorldServer world) {
	    Map<ChunkCoordIntPair, byte[]> dimensionMap = getDimensionChunkMap(world);
	    int loaded = dimensionMap.size();

	    long l = System.nanoTime();

	    List<ChunkCoordIntPair> removalChunks = new ArrayList<ChunkCoordIntPair>();
	    
        for (final ChunkCoordIntPair chunk : dimensionMap.keySet()) {
            if (!world.theChunkProviderServer.chunkExists(chunk.chunkXPos, chunk.chunkZPos)) {
                removalChunks.add(chunk);
            }
        }

	    int removed = 0;
	    
	    for (ChunkCoordIntPair chunk2 : removalChunks) {
	    	dimensionMap.remove(chunk2);
	    	removed++;
	    }
	}
	
	public static void sendChunkVariantsToPlayer(World world, Chunk chunk, EntityPlayerMP entityplayer) {
	    byte[] variants = getChunkBiomeVariants(world, chunk);
	    
	    if (variants != null) {
	    	PacketBiomeVariantsWatch packet = new PacketBiomeVariantsWatch(chunk.xPosition, chunk.zPosition, variants);
	    	PacketHandler.networkWrapper.sendTo(packet, entityplayer);
	    } else {
	    	FMLLog.severe("Could not find Aurora biome variants for chunk at %d, %d; requested by %s", new Object[] { Integer.valueOf(chunk.xPosition << 4), Integer.valueOf(chunk.zPosition << 4), entityplayer.getCommandSenderName() });
	    }
	}

	public static void sendUnwatchToPlayer(World world, Chunk chunk, EntityPlayerMP entityplayer) {
	    PacketBiomeVariantsUnwatch packet = new PacketBiomeVariantsUnwatch(chunk.xPosition, chunk.zPosition);
	    PacketHandler.networkWrapper.sendTo(packet, entityplayer);
	}

	public static int getSize(World world) {
	    Map<ChunkCoordIntPair, byte[]> map = getDimensionChunkMap(world);
	    return map.size();
	}
}