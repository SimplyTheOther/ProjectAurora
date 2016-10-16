package projectaurora.world.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockOxygen extends ItemBlock {

	public ItemBlockOxygen(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = "concentration." + stack.getItemDamage();
		
		return this.getUnlocalizedName() + "." + name;
	}

	@Override
	public int getMetadata(int itemBlockMeta) {
		return itemBlockMeta;
	}
}