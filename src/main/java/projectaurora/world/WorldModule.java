package projectaurora.world;

import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.vulcan.VulcanWorldProvider;

public class WorldModule {
	public static WorldType worldTypeAurora;
	
	public static int vulcanID;

	public static void preInit() {
		worldTypeAurora = new WorldTypeAurora("aurora");
		AuroraBiome.initBiomes();
	}
	
	public static void init() {
		vulcanID = DimensionManager.getNextFreeDimId();
		DimensionManager.registerProviderType(vulcanID, VulcanWorldProvider.class, false);
		DimensionManager.registerDimension(vulcanID, vulcanID);
	}
}