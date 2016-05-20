package projectaurora.core;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;
import projectaurora.compat.Compat;
import projectaurora.world.WorldTypeAurora;

@Mod(modid = Reference.modid, name = Reference.name, version = Reference.version, dependencies = "required-after:Forge;")
public class Aurora {

	@Mod.Instance(Reference.modid)
	public static Aurora instance;
	
	@SidedProxy(clientSide = "projectaurora.core.ClientProxy", serverSide = "projectaurora.core.CommonProxy")
	public static CommonProxy proxy;
	
	public static WorldType worldTypeAurora;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		worldTypeAurora = new WorldTypeAurora("aurora");
		Content.preInit();
		Compat.preInit();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		Content.init();
		Compat.init();
		proxy.renderCrap();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Content.postInit();
		Compat.postInit();
	}
	
	public static CreativeTabs tabWorld = new CreativeTabs(Reference.modid) {

		@Override
		public Item getTabIconItem() {
			return null;
		}
		
		@Override
		public ItemStack getIconItemStack() {
			return new ItemStack(Content.ore, 1, 0);
		}
	};
}