package projectaurora.world;

import java.io.File;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.Configuration;
import projectaurora.core.Reference;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.vulcan.VulcanWorldProvider;

public class WorldModule {
	public static WorldType worldTypeAurora;
	
	public static int vulcanID;
	
	public static int[] allDimensionIds;
	
	public static int[] dirtGen;
	public static int[] gravelGen;
	public static int[] coalGen;
	public static int[] ironGen;
	public static int[] goldGen;
	public static int[] lapisGen;
	public static int[] diamondGen;
	public static int[] emeraldGen;
	public static int[] copperGen;
	public static int[] aluminiumGen;
	public static int[] leadGen;
	public static int[] silverGen;
	public static int[] nickelGen;
	public static int[] tinGen;
	public static int[] quartzGen;

	public static void preInit(FMLPreInitializationEvent event) {
		vulcanID = DimensionManager.getNextFreeDimId();
		allDimensionIds = new int[] { vulcanID };
		
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(), Reference.modidUpperCase + "World.cfg"));
		
		config.load();
		dirtGen = config.get("oregen", "dirt", new int[] { 32, 20, 0, 256 }, "Config for underground dirt. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		gravelGen = config.get("oregen", "gravel", new int[] { 32, 10, 0, 256 }, "Config for underground gravel. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		coalGen = config.get("oregen", "coal", new int[] { 16, 20, 0, 256 }, "Config for coal ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		ironGen = config.get("oregen", "iron", new int[] { 8, 20, 0, 64 }, "Config for iron ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		goldGen = config.get("oregen", "gold", new int[] { 8, 2, 0, 32 }, "Config for gold ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		lapisGen = config.get("oregen", "lapis", new int[] { 6, 1, 0, 16 }, "Config for lapis ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		diamondGen = config.get("oregen", "diamond", new int[] { 7, 1, 0, 16 }, "Config for diamond ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		emeraldGen = config.get("oregen", "emerald", new int[] { 1, 7, 0, 16 }, "Config for emerald ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		copperGen = config.get("oregen", "copper", new int[] { 8, 8, 0, 128 }, "Config for copper ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		aluminiumGen = config.get("oregen", "aluminium", new int[] { 4, 8, 0, 128 }, "Config for aluminium ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		leadGen = config.get("oregen", "lead", new int[] { 6, 4, 0, 64 }, "Config for lead ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		silverGen = config.get("oregen", "silver", new int[] { 8, 4, 0, 32 }, "Config for silver ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		nickelGen = config.get("oregen", "nickel", new int[] { 6, 2, 0, 64 }, "Config for nickel ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		tinGen = config.get("oregen", "tin", new int[] { 8, 8, 0, 128 }, "Config for tin ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		quartzGen = config.get("oregen", "quartz", new int[] { 13, 7, 0, 128 }, "Config for quartz ore. Params: blocks per vein, chance, minY, maxY. Note that this is multiplied for each planet.").getIntList();
		
		config.save();
		
		worldTypeAurora = new WorldTypeAurora("aurora");
		AuroraBiome.initBiomes();
	}
	
	public static void init() {
		DimensionManager.registerProviderType(vulcanID, VulcanWorldProvider.class, false);
		DimensionManager.registerDimension(vulcanID, vulcanID);
	}
}