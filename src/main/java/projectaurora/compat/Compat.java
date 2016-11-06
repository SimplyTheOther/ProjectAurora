package projectaurora.compat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional.Method;
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
	
	public static boolean isWarpDriveLoaded = false;
	
	public static int[] otherModSpaceDimensions = null;
	private static List<Integer> otherModSpaceDimensionsList = new ArrayList();

	public static void preInit() {
		
	}

	public static void init() {
		if(Loader.isModLoaded("ImmersiveEngineering")) {
			isImmersiveEngineeringLoaded = true;
			iEStorage = GameRegistry.findBlock("ImmersiveEngineering", "storage");
			
			putDimensionsInIEOreBlacklist();
		}
		
		if(Loader.isModLoaded("TConstruct")) {
			isTinkersLoaded = true;
			tinkerStorage = GameRegistry.findBlock("TConstruct", "MetalBlock");
			
			putDimensionsInSlimeIslandBlacklist();
		}
		
		if(Loader.isModLoaded("ThermalFoundation")) {
			isThermalFoundationLoaded = true;
			tFStorage = GameRegistry.findBlock("ThermalFoundation", "Storage");
		}
		
		if(Loader.isModLoaded("WarpDrive")) {
			isWarpDriveLoaded = true;
			
			addWarpDriveDimensions();
			editWarpDrivePlanets();
		}
		//More space dimensions from other mods here
		if(otherModSpaceDimensionsList != null) {
			otherModSpaceDimensions = ArrayUtils.toPrimitive(otherModSpaceDimensionsList.toArray(new Integer[otherModSpaceDimensionsList.size()]));
		}
	}

	public static void postInit() {
		// Auto-generated method stub
		
	}

	private static void editWarpDrivePlanets() {
		String location = Loader.instance().getConfigDir().getPath();
		File warpdriveConfig = new File(location + File.separator + "WarpDrive" + File.separator + "WarpDrive.cfg");
		
		File worldConfig = new File(location + File.separator + "ProjectAuroraWorld.cfg");
		
		int[] configVulcan;
	
		try {
			if(warpdriveConfig.exists() && worldConfig.exists()) {
				Configuration warpDriveConfig = new Configuration(warpdriveConfig);
				Configuration auroraWorldConfig = new Configuration(worldConfig);
				
				auroraWorldConfig.load();
				
				configVulcan = auroraWorldConfig.get("planets", "vulcan", new int[] { 1000, 1000, 1000, 100, 100, 0, 0}, "WarpDrive use - dimensionId, dimensionCenterX, dimensionCenterZ, radiusX, radiusZ, spaceCenterX, spaceCenterZ").getIntList();
				
				auroraWorldConfig.save();
				
				warpDriveConfig.load();
				
				warpDriveConfig.get("planets", "overworld", new int[] { 0, 0, 0, 100000, 100000, 0, 0 }, "dimensionId, dimensionCenterX, dimensionCenterZ, radiusX, radiusZ, spaceCenterX, spaceCenterZ").set(new int[] { 0, 0, 0, 100, 100, 0, 0 });
				warpDriveConfig.get("planets", "vulcan", new int[] { 1000, 1000, 1000, 100, 100, 0, 0}, "dimensionId, dimensionCenterX, dimensionCenterZ, radiusX, radiusZ, spaceCenterX, spaceCenterZ").set(configVulcan);
			
				warpDriveConfig.save();
				
				System.out.println("editWarpDrivePlanets");
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("editWarpDrivePlanetsFailed");
		}
	}
	
	private static void addWarpDriveDimensions() {
		String location = Loader.instance().getConfigDir().getPath();
		File warpdriveConfig = new File(location + File.separator + "WarpDrive" + File.separator + "WarpDrive.cfg");
	
		try {
			if(warpdriveConfig.exists()) {
				Configuration config = new Configuration(warpdriveConfig);
				int spaceDimId;
				int hyperspaceDimId;
				
				config.load();
				
				spaceDimId = config.get("general", "space_dimension_id", -2, "Space dimension world ID").getInt();
				hyperspaceDimId = config.get("general", "hyperspace_dimension_id", -3, "Hyperspace dimension world ID").getInt();
			
				config.save();
				
				otherModSpaceDimensionsList.add(spaceDimId);
				otherModSpaceDimensionsList.add(hyperspaceDimId);
				
				System.out.println("addWarpDriveDimensions");
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("addWarpDriveDimensionsFailed");
		}
	}

	private static void putDimensionsInSlimeIslandBlacklist() {
		String location = Loader.instance().getConfigDir().getPath();
		File tinkerConfig = new File(location + File.separator + "TinkersConstruct.cfg");
		
		try {
			if(tinkerConfig.exists()) {
				Configuration config = new Configuration(tinkerConfig);
			
				config.load();
			
				config.get("DimBlacklist", "GenerateSlimeIslandInDim0Only", true, "True: slime islands wont generate in any ages other than overworld(if enabled); False: will generate in all non-blackisted ages").set(true);
				
				config.save();
				
				System.out.println("dimensionInTiCBlacklist");
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("dimensionInTiCBlacklistFailed");
		}
	}
	
	private static void putDimensionsInIEOreBlacklist() {
		String location = Loader.instance().getConfigDir().getPath();
		File ieConfig = new File(location + File.separator + "ImmersiveEngineering.cfg");
		
		try {
			if(ieConfig.exists()) {
				Configuration config = new Configuration(ieConfig);
				int[] currentOreGenBlacklist;
			
				config.load();
			
				currentOreGenBlacklist = config.get("oregen", "DimensionBlacklist", new int[] { -1, 1 }, "A blacklist of dimensions in which IE ores won't spawn. By default this is Nether (-1) and End (1)").getIntList();
				
				int[] combinedIntArray = new int[currentOreGenBlacklist.length + projectaurora.world.WorldModule.allDimensionIds.length];
				System.arraycopy(currentOreGenBlacklist, 0, combinedIntArray, 0, currentOreGenBlacklist.length);
				System.arraycopy(projectaurora.world.WorldModule.allDimensionIds, 0, combinedIntArray, currentOreGenBlacklist.length, projectaurora.world.WorldModule.allDimensionIds.length);

				for(int i = 0; i < projectaurora.world.WorldModule.allDimensionIds.length; i++) {
					if(ArrayUtils.contains(currentOreGenBlacklist, projectaurora.world.WorldModule.allDimensionIds[i])) {
						System.out.println("dimensionAlreadyInIEBlacklist");
					} else {
						config.get("oregen", "DimensionBlacklist", currentOreGenBlacklist, "A blacklist of dimensions in which IE ores won't spawn. By default this is Nether (-1) and End (1)").set(combinedIntArray);
					
						System.out.println("dimensionInIEBlacklist");
					}
				}
				System.out.println("CombinedIntArray=" + combinedIntArray.length + "," + combinedIntArray[0] + "," + combinedIntArray[1] + "," + combinedIntArray[2]);
				
				config.save();
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("dimensionInIEBlacklistFailed");
		}
	}
}