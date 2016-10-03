package projectaurora.world.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import projectaurora.core.Reference;

public class ItemBlockDummyLiquid extends ItemBlock {

	public ItemBlockDummyLiquid(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = "dummyLiquid" + Reference.oreTexNames[stack.getItemDamage()] + "Aurora";
		
		return this.getUnlocalizedName() + "." + name;
	}

	@Override
	public int getMetadata(int itemBlockMeta) {
		return itemBlockMeta;
	}
}