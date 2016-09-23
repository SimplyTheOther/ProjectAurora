package projectaurora.core;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import projectaurora.world.biome.PacketBiomeVariantsUnwatch;
import projectaurora.world.biome.PacketBiomeVariantsWatch;

public class PacketHandler {//TODO packethandler stuff
	  public static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("aurora_");
	  
	  public PacketHandler() {
		  int id = 0;
		  
		  networkWrapper.registerMessage(PacketBiomeVariantsWatch.Handler.class, PacketBiomeVariantsWatch.class, id++, Side.CLIENT);
		  networkWrapper.registerMessage(PacketBiomeVariantsUnwatch.Handler.class, PacketBiomeVariantsUnwatch.class, id++, Side.CLIENT);
	  }
}