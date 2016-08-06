package projectaurora.world.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPlant extends ItemBlock {

	public ItemBlockPlant(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = "";
		switch(stack.getItemDamage()) {
			case 0: {
				name = "plantGlowstone";
				break;
			}
			default: {
				name = "congratsYouBrokeTheGamePlantVersion";
				break;
			}
		}
		return this.getUnlocalizedName() + "." + name;
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}
}