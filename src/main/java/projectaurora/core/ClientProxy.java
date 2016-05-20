package projectaurora.core;

import cpw.mods.fml.client.registry.RenderingRegistry;
import projectaurora.world.render.BlockOreRenderHandler;

public class ClientProxy extends CommonProxy {

	public static int oreRenderID;
	
	@Override
	public void renderCrap() {
		oreRenderID = RenderingRegistry.getNextAvailableRenderId(); //maybe
		RenderingRegistry.registerBlockHandler(oreRenderID, new BlockOreRenderHandler());
	}
}
