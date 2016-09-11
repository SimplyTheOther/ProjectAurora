package projectaurora.compat;

import java.io.File;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class Compat {
	
	public static boolean isImmersiveEngineeringLoaded = false;
	public static Block iEStorage = null;
	
	public static boolean isTinkersLoaded = false;
	public static Block tinkerStorage = null;
	
	public static boolean isThermalFoundationLoaded = false;
	public static Block tFStorage = null;

	public static void preInit() {
		// TODO Auto-generated method stub
		
	}

	public static void init() {
		if(Loader.isModLoaded("ImmersiveEngineering")) {
			isImmersiveEngineeringLoaded = true;
			iEStorage = GameRegistry.findBlock("ImmersiveEngineering", "storage");
		}
		
		if(Loader.isModLoaded("TConstruct")) {
			isTinkersLoaded = true;
			tinkerStorage = GameRegistry.findBlock("TConstruct", "MetalBlock");
			
			putDimensionsInBlacklist();
		}
		
		if(Loader.isModLoaded("ThermalFoundation")) {
			isThermalFoundationLoaded = true;
			tFStorage = GameRegistry.findBlock("ThermalFoundation", "Storage");
		}
	}

	public static void postInit() {
		// TODO Auto-generated method stub
		
	}


	private static void putDimensionsInBlacklist() {
		String location = Loader.instance().getConfigDir().getPath();
		File tinkerConfig = new File(location + File.separator + "TinkersConstruct.cfg");
		
		try {
			if(tinkerConfig.exists()) {
				Configuration config = new Configuration(tinkerConfig);
			
				config.load();
			
				config.get("DimBlacklist", "GenerateSlimeIslandInDim0Only", true, "True: slime islands wont generate in any ages other than overworld(if enabled); False: will generate in all non-blackisted ages").set(true);
			
				System.out.println(config.get("DimBlacklist", "GenerateSlimeIslandInDim0Only", true, "True: slime islands wont generate in any ages other than overworld(if enabled); False: will generate in all non-blackisted ages").getBoolean());
				
				config.save();
				
				System.out.println("dimensionInBlacklist");
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("dimensionInBlacklistFailed");
		}
	}
}