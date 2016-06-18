package projectaurora.world.biome;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class PacketBiomeVariantsWatch implements IMessage {
	private int chunkX;
	private int chunkZ;
	private byte[] variants;
	
	public PacketBiomeVariantsWatch() {
		
	}
	
	public PacketBiomeVariantsWatch(int x, int z, byte[] v) {
		this.chunkX = x;
		this.chunkZ = z;
		this.variants = v;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.chunkX = buf.readInt();
		this.chunkZ = buf.readInt();
		int length = buf.readInt();
		this.variants = buf.readBytes(length).array();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(chunkX);
		buf.writeInt(chunkZ);
		buf.writeInt(variants.length);
		buf.writeBytes(variants);
	}
	
	public static class Handler implements IMessageHandler<PacketBiomeVariantsWatch, IMessage> {
	    public IMessage onMessage(PacketBiomeVariantsWatch packet, MessageContext context) {
	    	World world = Minecraft.getMinecraft().theWorld;
	    	int chunkX = packet.chunkX;
	    	int chunkZ = packet.chunkZ;
	     
	    	if (world.blockExists(chunkX << 4, 0, chunkZ << 4)) {
	    		AuroraBiomeVariantStorage.setChunkBiomeVariants(world, new ChunkCoordIntPair(chunkX, chunkZ), packet.variants);
	    	} else {
	    		FMLLog.severe("Client received Aurora biome variant data for nonexistent chunk at %d, %d", new Object[] { Integer.valueOf(chunkX << 4), Integer.valueOf(chunkZ << 4) });
	    	}

	    	return null;
	    }
	}
}