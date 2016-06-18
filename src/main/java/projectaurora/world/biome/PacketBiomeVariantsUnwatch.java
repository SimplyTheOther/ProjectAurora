package projectaurora.world.biome;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;

public class PacketBiomeVariantsUnwatch implements IMessage {
	private int chunkX;
	private int chunkZ;
	
	public PacketBiomeVariantsUnwatch() {
		
	}
	
	public PacketBiomeVariantsUnwatch(int x, int z) {
		this.chunkX = x;
		this.chunkZ = z;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.chunkX = buf.readInt();
		this.chunkZ = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(chunkX);
		buf.writeInt(chunkZ);
	}
	
	public static class Handler implements IMessageHandler<PacketBiomeVariantsUnwatch, IMessage> {
	    public IMessage onMessage(PacketBiomeVariantsUnwatch packet, MessageContext context) {
	    	World world = Minecraft.getMinecraft().theWorld;
	    	int chunkX = packet.chunkX;
	    	int chunkZ = packet.chunkZ;
	     
	    	if (world.blockExists(chunkX << 4, 0, chunkZ << 4)) {
	    		AuroraBiomeVariantStorage.clearChunkBiomeVariants(world, new ChunkCoordIntPair(chunkX, chunkZ));
	    	} else {
	    		FMLLog.severe("Client received Aurora biome variant unwatch packet for nonexistent chunk at %d, %d", new Object[] { Integer.valueOf(chunkX << 4), Integer.valueOf(chunkZ << 4) });
	    	}

	    	return null;
	    }
	}
}