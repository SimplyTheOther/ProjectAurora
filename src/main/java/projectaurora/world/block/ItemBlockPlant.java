package projectaurora.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ForgeEventFactory;
import projectaurora.core.Content;

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
				name = "plantGlowstoneAurora";
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
	
	@Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(stack.getItemDamage() == 0) {
			MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

			if (movingobjectposition == null) {
				return stack;
			}
        
			else {
				if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
					int i = movingobjectposition.blockX;
					int j = movingobjectposition.blockY;
                	int k = movingobjectposition.blockZ;

                	if (!world.canMineBlock(player, i, j, k)) {
                		return stack;
                	}

                	if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack)) {
                		return stack;
                	}

                	if (world.getBlock(i, j, k) == Blocks.lava && world.getBlockMetadata(i, j, k) == 0 && world.isAirBlock(i, j + 1, k)) {
                		BlockSnapshot blocksnapshot = BlockSnapshot.getBlockSnapshot(world, i, j + 1, k);
                		world.setBlock(i, j + 1, k, Content.plant, 0, 2);
                    
                		if (ForgeEventFactory.onPlayerBlockPlace(player, blocksnapshot, ForgeDirection.UP).isCanceled())  {
                			blocksnapshot.restore(true, false);
                        	return stack;
                		}

                		if (!player.capabilities.isCreativeMode) {
                			--stack.stackSize;
                		}
                	}
				}
        	}
        }
        return stack;
    }
}