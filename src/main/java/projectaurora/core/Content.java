package projectaurora.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import projectaurora.world.block.BlockDummyLiquid;
import projectaurora.world.block.BlockOre;
import projectaurora.world.block.BlockPlant;
import projectaurora.world.block.BlockRock;
import projectaurora.world.block.BlockSoft;
import projectaurora.world.block.ItemBlockDummyLiquid;
import projectaurora.world.block.ItemBlockOre;
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
	public static Item teleporter;

	public static void preInit() {
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
		
		teleporter = new ItemTeleporter().setUnlocalizedName(Reference.modidLowerCase + ".teleporter");
		GameRegistry.registerItem(teleporter, (teleporter.getUnlocalizedName().substring(5)));
	}

	public static void metaRegister(Block block, Class<? extends ItemBlock> itemclass) {
		GameRegistry.registerBlock(block, itemclass, (block.getUnlocalizedName().substring(5)));
	}

	public static void init() {//recipes?
		// Auto-generated method stub
		
	}

	public static void postInit() {
		// Auto-generated method stub
		
	}
}