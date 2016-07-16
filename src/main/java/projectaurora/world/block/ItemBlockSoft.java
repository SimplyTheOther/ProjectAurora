package projectaurora.world.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockSoft extends ItemBlock {

	public ItemBlockSoft(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = "";
		switch(stack.getItemDamage()) {
			case 0: {
				name = "dustAshAurora";
				break;
			}
			default: {
				name = "congratsYouBrokeTheGame";
				break;
			}
		}
		return this.getUnlocalizedName() + "." + name;
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}//TODO do addInformation for ash compositions? Probs not
}