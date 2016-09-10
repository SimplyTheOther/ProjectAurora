package projectaurora.compat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

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
		}
		
		if(Loader.isModLoaded("ThermalFoundation")) {
			isThermalFoundationLoaded = true;
			tFStorage = GameRegistry.findBlock("ThermalFoundation", "Storage");
		}
	}

	public static void postInit() {
		// TODO Auto-generated method stub
		
	}

}
