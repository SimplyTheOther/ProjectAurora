package projectaurora.core;

import cpw.mods.fml.client.registry.RenderingRegistry;
import projectaurora.world.render.BlockOreRenderHandler;
import projectaurora.world.render.BlockPlantRenderHandler;

public class ClientProxy extends CommonProxy {

	public static int oreRenderID;
	public static int plantRenderID;
	
	@Override
	public void renderCrap() {
		oreRenderID = RenderingRegistry.getNextAvailableRenderId(); //maybe
		RenderingRegistry.registerBlockHandler(oreRenderID, new BlockOreRenderHandler());
		plantRenderID = RenderingRegistry.getNextAvailableRenderId(); //maybe
		RenderingRegistry.registerBlockHandler(plantRenderID, new BlockPlantRenderHandler());
	}
}
