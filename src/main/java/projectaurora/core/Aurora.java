package projectaurora.core;

import java.util.Calendar;

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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import projectaurora.compat.Compat;
import projectaurora.world.WorldModule;

@Mod(modid = Reference.modidUpperCase, name = Reference.name, version = Reference.version, dependencies = "required-after:Forge;after:TConstruct;after:ImmersiveEngineering")
public class Aurora {

	@Mod.Instance(Reference.modidUpperCase)
	public static Aurora instance;
	
	@SidedProxy(clientSide = "projectaurora.core.ClientProxy", serverSide = "projectaurora.core.CommonProxy")
	public static CommonProxy proxy;
	
	private static PacketHandler packetHandler;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Content.preInit();
		Compat.preInit();
		WorldModule.preInit(event);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		packetHandler = new PacketHandler();
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		
		Content.init();
		Compat.init();
		WorldModule.init();
		proxy.renderCrap();
	}
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Content.postInit();
		Compat.postInit();
	}
	
	public static CreativeTabs tabWorld = new CreativeTabs(Reference.modidLowerCase) {

		@Override
		public Item getTabIconItem() {
			return null;
		}
		
		@Override
		public ItemStack getIconItemStack() {
			return new ItemStack(Content.ore, 1, 0);
		}
	};

	public static boolean isChristmas() {
		Calendar calendar = Calendar.getInstance();
		
		if(calendar.get(2) == 11) {
			int date = calendar.get(5);
			
			if(date == 24 || date == 25 || date == 26) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isNewYears() {
		Calendar calendar = Calendar.getInstance();
		
		if(calendar.get(2) == 0) {
			int date = calendar.get(5);
			
			if(date == 1) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isAprilFools() {
		Calendar calendar = Calendar.getInstance();
		
		if(calendar.get(2) == 3) {
			int date = calendar.get(5);
			
			if(date == 1) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isHalloween() {
		Calendar calendar = Calendar.getInstance();
		
		if(calendar.get(2) == 9) {
			int date = calendar.get(5);
			
			if(date == 31) {
				return true;
			}
		}
		return false;
	}
}