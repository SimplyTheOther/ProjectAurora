package projectaurora.world.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockDummyLiquid extends ItemBlock {

	public ItemBlockDummyLiquid(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = "";
		switch(stack.getItemDamage()) {
			case 0: {
				name = "dummyLiquidCoalAurora";
				break;
			}
			case 1: {
				name = "dummyLiquidIronAurora";
				break;
			}
			case 2: {
				name = "dummyLiquidGoldAurora";
				break;
			}
			case 3: {
				name = "dummyLiquidLapisAurora";
				break;
			}
			case 4: {
				name = "dummyLiquidDiamondAurora";
				break;
			}
			case 5: {
				name = "dummyLiquidEmeraldAurora";
				break;
			}
			case 6: {
				name = "dummyLiquidCopperAurora";
				break;
			}
			case 7: {
				name = "dummyLiquidAluminiumAurora";
				break;
			}
			case 8: {
				name = "dummyLiquidLeadAurora";
				break;
			}
			case 9: {
				name = "dummyLiquidSilverAurora";
				break;
			}
			case 10: {
				name = "dummyLiquidNickelAurora";
				break;
			}
			case 11: {
				name = "dummyLiquidTinAurora";
				break;
			}
			case 12: {
				name = "dummyLiquidQuartzAurora";
				break;
			}
			default: {
				name = "congratsYouBrokeTheGameL";
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