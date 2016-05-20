package projectaurora.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import projectaurora.world.block.BlockOre;
import projectaurora.world.block.ItemBlockOre;

public class Content {
	public static Block ore;

	public static void preInit() {
		ore = new BlockOre().setBlockName("oreAurora");
		metaRegister(ore, ItemBlockOre.class);
	}

	public static void metaRegister(Block block, Class<? extends ItemBlock> itemclass) {
		GameRegistry.registerBlock(block, itemclass, Reference.modid + (block.getUnlocalizedName().substring(5)));
	}

	public static void init() {
		// TODO Auto-generated method stub
		
	}

	public static void postInit() {
		// TODO Auto-generated method stub
		
	}
}
