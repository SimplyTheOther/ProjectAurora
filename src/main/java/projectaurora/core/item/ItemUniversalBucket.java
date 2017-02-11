package projectaurora.core.item;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ItemUniversalBucket extends Item {//TODO
	
	public ItemUniversalBucket() {
		this.maxStackSize = 1;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        boolean flag = /*this.isFull == Blocks.air TODO */false;
        
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, flag);

        if (movingobjectposition == null) {
            return stack;
        } else {
            FillBucketEvent event = new FillBucketEvent(player, stack, world, movingobjectposition);
            
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return stack;
            }

            if (event.getResult() == Event.Result.ALLOW) {
                if (player.capabilities.isCreativeMode) {
                    return stack;
                }

                if (--stack.stackSize <= 0) {
                    return event.result;
                }

                if (!player.inventory.addItemStackToInventory(event.result)) {
                    player.dropPlayerItemWithRandomChoice(event.result, false);
                }

                return stack;
            }
            
            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int i = movingobjectposition.blockX;
                int j = movingobjectposition.blockY;
                int k = movingobjectposition.blockZ;

                if (!world.canMineBlock(player, i, j, k)) {
                    return stack;
                }

                if (flag) {
                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack)) {
                        return stack;
                    }

                    Block block = world.getBlock(i, j, k);
                    int meta = world.getBlockMetadata(i, j, k);

                    //Put fluid in general below
                    if (block == Blocks.water && meta == 0) {
                        world.setBlockToAir(i, j, k);
                        //return addItemToInventory(stack, player, Items.water_bucket); TODO
                    }

                    if (block == Blocks.lava && meta == 0) {
                        world.setBlockToAir(i, j, k);
                        //return addItemToInventory(stack, player, Items.lava_bucket); TODO
                    }
                    
                    
                } else {
                    /*if (this.isFull == Blocks.air) {
                        return new ItemStack(Items.bucket);
                    }*///TODO

                    if (movingobjectposition.sideHit == 0) {
                        --j;
                    }

                    if (movingobjectposition.sideHit == 1) {
                        ++j;
                    }

                    if (movingobjectposition.sideHit == 2) {
                        --k;
                    }

                    if (movingobjectposition.sideHit == 3) {
                        ++k;
                    }

                    if (movingobjectposition.sideHit == 4) {
                        --i;
                    }

                    if (movingobjectposition.sideHit == 5) {
                        ++i;
                    }

                    if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack)) {
                        return stack;
                    }

                    /*if (this.tryPlaceContainedLiquid(world, i, j, k) && !player.capabilities.isCreativeMode) {
                        return new ItemStack(Items.bucket);
                    }*///TODO
                }
            }
            return stack;
        }
	}
	
	/**
	 * Gets fluid from the NBT tag. 
	 * 
	 * @return Fluid from NBT tag
	 */
	protected Fluid getContainedFluid(ItemStack bucket, EntityPlayer player) {
		NBTTagCompound nbt = bucket.getTagCompound();
		
		if(nbt == null) {
			System.out.println("(gCT) No NBT for ItemUniversalBucket belonging to " + player.getCommandSenderName());
			return null;
		} else {
			NBTTagCompound tag = nbt.getCompoundTag("fluidstorage");
			
			if(tag == null) {
				System.out.println("(gCT) No fluidstorage tag for ItemUniversalBucket belonging to " + player.getCommandSenderName());
				return null;
			} else {
				if(tag.hasKey("fluidtype")) {
					return FluidRegistry.getFluid(tag.getString("fluidtype"));
				} else {
					System.out.println("(gCT) No fluidtype tag for ItemUniversalBucket belonging to " + player.getCommandSenderName());
					return null;
				}
			}
		}
	}
	
	/**
	 * Removes fluidtype/fluidstorage NBT tag (used to store fluids) from bucket.
	 * Designed to be used in onItemRightClick.
	 * 
	 * @return True if successful
	 */
	protected boolean removeFluidNBTFromBucket(ItemStack bucket, EntityPlayer player) {
		NBTTagCompound nbt = bucket.getTagCompound();
		
		if(nbt == null) {
			System.out.println("(rFNBTFB) No NBT for ItemUniversalBucket belonging to " + player.getCommandSenderName());
			return false;
		} else {
			NBTTagCompound tag = nbt.getCompoundTag("fluidstorage");
			
			if(tag == null) {
				System.out.println("(rFNBTFB) No fluidstorage tag for ItemUniversalBucket belonging to " + player.getCommandSenderName());
				return false;
			} else {
				if(tag.hasKey("fluidtype")) {
					tag.removeTag("fluidtype");
					return true;
				} else {
					System.out.println("(rFNBTFB) No fluidtype tag for ItemUniversalBucket belonging to " + player.getCommandSenderName());
					return false;
				}
			}
		}
	}
	
	protected boolean addFluidNBTToBucket(ItemStack bucket, EntityPlayer player, String fluidName) {
		NBTTagCompound nbt = bucket.getTagCompound();
		
		if(nbt == null) {
			nbt = new NBTTagCompound();
			bucket.setTagCompound(nbt);
		}
		
		NBTTagCompound tag = nbt.getCompoundTag("fluidstorage");

        if (!nbt.hasKey("fluidstorage", 10)) {
            nbt.setTag("fluidstorage", tag);
        }

        tag.setString("fluidtype", fluidName);
        
		return false;
	}
}