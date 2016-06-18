package projectaurora.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import projectaurora.world.block.BlockOre;
import projectaurora.world.block.ItemBlockOre;
import projectaurora.world.item.ItemTeleporter;

public class Content {
	public static Block ore;
	public static Item teleporter;

	public static void preInit() {
		ore = new BlockOre().setBlockName("oreAurora");
		metaRegister(ore, ItemBlockOre.class);
		
		teleporter = new ItemTeleporter().setUnlocalizedName("auroraTeleporter");
		GameRegistry.registerItem(teleporter, Reference.modid + (teleporter.getUnlocalizedName().substring(5)));
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
