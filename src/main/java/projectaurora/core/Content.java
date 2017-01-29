package projectaurora.core;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import projectaurora.compat.immersiveengineering.EntityNote7;
import projectaurora.compat.immersiveengineering.ItemNote7;
import projectaurora.core.item.ItemUniversalBucket;
import projectaurora.world.block.BlockDummyLiquid;
import projectaurora.api.BlockFiniteFluid;
import projectaurora.world.block.BlockOxygen;
import projectaurora.world.block.BlockOre;
import projectaurora.world.block.BlockOxygen;
import projectaurora.world.block.BlockPlant;
import projectaurora.world.block.BlockRock;
import projectaurora.world.block.BlockSoft;
import projectaurora.world.block.ItemBlockDummyLiquid;
import projectaurora.world.block.ItemBlockOre;
import projectaurora.world.block.ItemBlockOxygen;
import projectaurora.world.block.ItemBlockPlant;
import projectaurora.world.block.ItemBlockRock;
import projectaurora.world.block.ItemBlockSoft;
import projectaurora.world.item.ItemTeleporter;

public class Content {
	public static Block ore;
	public static Block rock;
	public static Block dust;
	public static Block plant;
	public static Block dummyLiquid;
	public static Block oxygen;
	public static Block saltWater;
	
	public static Item universalBucket;
	public static Item note7;
	public static Item teleporter;
	
	public static Fluid fluidOxygen;
	public static Fluid fluidSaltWater;

	public static void preInit() {
		fluidOxygen = FluidRegistry.getFluid("oxygen");
		if(fluidOxygen == null) {
			fluidOxygen = new Fluid("oxygen").setBlock(oxygen).setGaseous(true).setDensity(500).setViscosity(1000);
			FluidRegistry.registerFluid(fluidOxygen);
		}
		
		//TFC compat required lowercase
		fluidSaltWater = FluidRegistry.getFluid("saltwater");
		if(fluidSaltWater == null) {
			fluidSaltWater = new Fluid("saltwater").setBlock(saltWater);
			FluidRegistry.registerFluid(fluidSaltWater);
		}
		
		ore = new BlockOre().setBlockName(Reference.modidLowerCase + ".ore");
		metaRegister(ore, ItemBlockOre.class);
		
		rock = new BlockRock().setBlockName(Reference.modidLowerCase + ".rock");
		metaRegister(rock, ItemBlockRock.class);
		
		dust = new BlockSoft().setBlockName(Reference.modidLowerCase + ".dust");
		metaRegister(dust, ItemBlockSoft.class);
		
		plant = new BlockPlant().setBlockName(Reference.modidLowerCase + ".plant");
		metaRegister(plant, ItemBlockPlant.class);
		
		dummyLiquid = new BlockDummyLiquid().setBlockName(Reference.modidLowerCase + ".dummyliquid");
		metaRegister(dummyLiquid, ItemBlockDummyLiquid.class);
		
		oxygen = new BlockOxygen(fluidOxygen).setCreativeTab(Aurora.tabWorld).setBlockName(Reference.modidLowerCase + ".oxygen");
		metaRegister(oxygen, ItemBlockOxygen.class);
		
		saltWater = new BlockFiniteFluid(fluidSaltWater, Material.water).setCreativeTab(Aurora.tabWorld).setBlockName(Reference.modidLowerCase + ".saltwater");
		GameRegistry.registerBlock(saltWater, saltWater.getUnlocalizedName().substring(5));

		universalBucket = new ItemUniversalBucket().setUnlocalizedName(Reference.modidLowerCase + ".bucket").setCreativeTab(Aurora.tabWorld);
		GameRegistry.registerItem(universalBucket, (universalBucket.getUnlocalizedName().substring(5)));
		
		note7 = new ItemNote7().setUnlocalizedName(Reference.modidLowerCase + ".note7");
		GameRegistry.registerItem(note7, (note7.getUnlocalizedName().substring(5)));
		
		teleporter = new ItemTeleporter().setUnlocalizedName(Reference.modidLowerCase + ".teleporter");
		GameRegistry.registerItem(teleporter, (teleporter.getUnlocalizedName().substring(5)));
		
		registerEntity(EntityNote7.class, "noteseven", 0, 0);
	}

	public static void metaRegister(Block block, Class<? extends ItemBlock> itemclass) {
		GameRegistry.registerBlock(block, itemclass, (block.getUnlocalizedName().substring(5)));
	}
	
	public static void registerEntity(Class entity, String name, int primaryColour, int secondaryColour) {
		int id = EntityRegistry.findGlobalUniqueEntityId();
		
		EntityRegistry.registerGlobalEntityID(entity, Reference.modidLowerCase + "." + name, id, primaryColour, secondaryColour);
		
		EntityRegistry.registerModEntity(entity, Reference.modidLowerCase + "." + name, id, Aurora.instance, 5, 3, false);
	}

	public static void init() {//recipes?
		// Auto-generated method stub
		
	}

	public static void postInit() {
		// Auto-generated method stub
		
	}
}