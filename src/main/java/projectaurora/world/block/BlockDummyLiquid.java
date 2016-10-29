package projectaurora.world.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import projectaurora.compat.Compat;
import projectaurora.core.Aurora;
import projectaurora.core.ClientProxy;
import projectaurora.core.Content;
import projectaurora.core.Reference;

public class BlockDummyLiquid extends Block {

	@SideOnly(Side.CLIENT)
	public IIcon[] icons = new IIcon[Reference.oreTexNames.length];

	//public static final String[] oreTexNames = new String[] {"Coal", "Iron", "Gold", "Lapis", "Diamond", "Emerald", "Copper", "Aluminium", "Lead", "Silver", "Nickel", "Tin", "Quartz"};

	public BlockDummyLiquid() {
		super(Material.lava);
		//this.setCreativeTab(Aurora.tabWorld);
		this.setHardness(-1.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeStone);
		this.setBlockBounds(0F, 0F, 0F, 1F, 0.86174509995F, 1F);
		this.setLightLevel(13F);
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
		//if(world.isRemote) {
			//return true;
		//} else {
			ItemStack currentItem = player.getCurrentEquippedItem();
			
			if(currentItem == null) {
				System.out.println("lightLevelOf" + world.getBlock(1799, 11, -1684).getUnlocalizedName() + "=" + world.getBlock(1799, 11, -1684).getLightValue());
				System.out.println("lightLevelOf" + world.getBlock(1799, 10, -1684).getUnlocalizedName() + "=" + world.getBlock(1799, 10, -1684).getLightValue());
				System.out.println("lightLevelOf" + world.getBlock(1799, 9, -1684).getUnlocalizedName() + "=" + world.getBlock(1799, 9, -1684).getLightValue());
				
				return true;
			} else {
				if(currentItem.getItem() == Items.bucket) {
					return putLiquidInBucket(world, x, y, z, player);
				}
			}
		//}
		
		return false;
    }

	private boolean putLiquidInBucket(World world, int x, int y, int z, EntityPlayer player) {
		if(FluidRegistry.getFluid(Reference.oreTexNames[world.getBlockMetadata(x, y, z)].toLowerCase() + ".molten") != null) {
			if(!player.capabilities.isCreativeMode) {
				ItemStack original = player.getCurrentEquippedItem();
				ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(new FluidStack(FluidRegistry.getFluid(Reference.oreTexNames[world.getBlockMetadata(x, y, z)].toLowerCase() + ".molten"), 1000), player.getCurrentEquippedItem());
				
				if(filledBucket != null) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, filledBucket);
				}
			} 
			world.setBlock(x, y, z, Blocks.air);
			return true;
		} else if(FluidRegistry.getFluid("aluminum.molten") != null && world.getBlockMetadata(x, y, z) == 7) {
			if(!player.capabilities.isCreativeMode) {
				ItemStack original = player.getCurrentEquippedItem();
				ItemStack filledBucket = FluidContainerRegistry.fillFluidContainer(new FluidStack(FluidRegistry.getFluid("aluminum.molten"), 1000), player.getCurrentEquippedItem());
					
				if(filledBucket != null) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, filledBucket);
				}
			}
			world.setBlock(x, y, z, Blocks.air);
			return true;
		}		
	
		return false;
	}

	@Override
	public int getRenderType() {
		return ClientProxy.liquidRenderID;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
		return false;
	}
	
/*	@Override
    public boolean canCollideCheck(int blockMeta, boolean rightClickWithBoat) {
        return rightClickWithBoat;
    }*/
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
    public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side) {
        Material material = world.getBlock(x, y, z).getMaterial();
        
        if(material == this.blockMaterial) {
        	return false;
        } else if(side == 1) {
        	return true;
        } else {
        	return super.isBlockSolid(world, x, y, z, side);
        }
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        Material material = world.getBlock(x, y, z).getMaterial();
        
        if(material == this.blockMaterial) {
        	return false;
        } else if(side == 1) {
        	return true;
        } else {
        	return super.shouldSideBeRendered(world, x, y, z, side);
        }
    }
	
	@Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }
	
	@Override
    public Item getItemDropped(int meta, Random rand, int fortune) {
        return null;
    }
	
	@Override
    public int quantityDropped(Random rand) {
        return 0;
    }
	
    protected int func_149804_e(World world, int x, int y, int z) {
    	if(world.getBlock(x, y, z).getMaterial() == this.blockMaterial) {
    		return world.getBlockMetadata(x, y, z);
    	} else {
    		return -1;
    	}
    }
	
    public static float getLiquidHeightPercent(int meta) {
        return 1.0F / 9.0F;
    }
    
    protected int getEffectiveFlowDecay(IBlockAccess world, int x, int y, int z) {
        if (world.getBlock(x, y, z).getMaterial() != this.blockMaterial) {
        	return -1;
        } else {
            return 0;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public static double getFlowDirection(IBlockAccess world, int x, int y, int z, Material material) {
        Vec3 vec3 = ((BlockDummyLiquid)Content.dummyLiquid).getFlowVector(world, x, y, z);

        if(vec3.xCoord == 0.0D && vec3.zCoord == 0.0D) {
        	return -1000.0D;
        } else {
        	return Math.atan2(vec3.zCoord, vec3.xCoord) - (Math.PI / 2D);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public double getFlowDirectionNotStatic(IBlockAccess world, int x, int y, int z, Material material) {
        Vec3 vec3 = this.getFlowVector(world, x, y, z);

        if(vec3.xCoord == 0.0D && vec3.zCoord == 0.0D) {
        	return -1000.0D;
        } else {
        	return Math.atan2(vec3.zCoord, vec3.xCoord) - (Math.PI / 2D);
        }
    }
	
    public Vec3 getFlowVector(IBlockAccess world, int x, int y, int z) {
        Vec3 vec3 = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
        int effectiveFlowDecay = this.getEffectiveFlowDecay(world, x, y, z);

        for (int i1 = 0; i1 < 4; ++i1) {
            int newX = x;
            int newZ = z;

            if (i1 == 0) {
                newX = x - 1;
            }

            if (i1 == 1) {
                newZ = z - 1;
            }

            if (i1 == 2) {
                ++newX;
            }

            if (i1 == 3) {
                ++newZ;
            }

            int newEffectiveFlowDecay = this.getEffectiveFlowDecay(world, newX, y, newZ);
            int totalEffectiveFlowDecay;

            if (newEffectiveFlowDecay < 0) {
                if (!world.getBlock(newX, y, newZ).getMaterial().blocksMovement()) {
                    newEffectiveFlowDecay = this.getEffectiveFlowDecay(world, newX, y - 1, newZ);

                    if (newEffectiveFlowDecay >= 0) {
                        totalEffectiveFlowDecay = newEffectiveFlowDecay - (effectiveFlowDecay - 8);
                        vec3 = vec3.addVector((double)((newX - x) * totalEffectiveFlowDecay), (double)((y - y) * totalEffectiveFlowDecay), (double)((newZ - z) * totalEffectiveFlowDecay));
                    }
                }
            } else if (newEffectiveFlowDecay >= 0) {
                totalEffectiveFlowDecay = newEffectiveFlowDecay - effectiveFlowDecay;
                vec3 = vec3.addVector((double)((newX - x) * totalEffectiveFlowDecay), (double)((y - y) * totalEffectiveFlowDecay), (double)((newZ - z) * totalEffectiveFlowDecay));
            }
        }

        if (world.getBlockMetadata(x, y, z) >= 8) {
            boolean flag = false;

            if (flag || this.isBlockSolid(world, x, y, z - 1, 2)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(world, x, y, z + 1, 3)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(world, x - 1, y, z, 4)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(world, x + 1, y, z, 5)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(world, x, y + 1, z - 1, 2)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(world, x, y + 1, z + 1, 3)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(world, x - 1, y + 1, z, 4)) {
                flag = true;
            }

            if (flag || this.isBlockSolid(world, x + 1, y + 1, z, 5)) {
                flag = true;
            }

            //if (flag) {
                vec3 = vec3.normalize().addVector(0.0D, -6.0D, 0.0D);
            //}
        }

        vec3 = vec3.normalize();
        return vec3;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
        int brightness = world.getLightBrightnessForSkyBlocks(x, y, z, 0);
        int aboveBrightness = world.getLightBrightnessForSkyBlocks(x, y + 1, z, 0);
        int j1 = brightness & 255;
        int k1 = aboveBrightness & 255;
        int l1 = brightness >> 16 & 255;
        int i2 = aboveBrightness >> 16 & 255;
        
        return (j1 > k1 ? j1 : k1) | (l1 > i2 ? l1 : i2) << 16;
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if(Compat.isTinkersLoaded) {
			switch(meta) {
				case 1:
					if(FluidRegistry.getFluid("iron.molten").getIcon() != null) {
						return FluidRegistry.getFluid("iron.molten").getIcon();
					} else {
						System.out.println("Couldn't get tinker texture for " + Reference.oreTexNames[meta]);
					}
					break;
				case 2:
					if(FluidRegistry.getFluid("gold.molten").getIcon() != null) {
						return FluidRegistry.getFluid("gold.molten").getIcon();
					} else {
						System.out.println("Couldn't get tinker texture for " + Reference.oreTexNames[meta]);
					} 
					break;
				case 6:
					if(FluidRegistry.getFluid("copper.molten").getIcon() != null) {
						return FluidRegistry.getFluid("copper.molten").getIcon();
					} else {
						System.out.println("Couldn't get tinker texture for " + Reference.oreTexNames[meta]);
					} 
					break;
				case 7:
					if(FluidRegistry.getFluid("aluminum.molten").getIcon() != null) {
						return FluidRegistry.getFluid("aluminum.molten").getIcon();
					} else {
						System.out.println("Couldn't get tinker texture for " + Reference.oreTexNames[meta]);
					} 
					break;
				case 8:
					if(FluidRegistry.getFluid("lead.molten").getIcon() != null) {
						return FluidRegistry.getFluid("lead.molten").getIcon();
					} else {
						System.out.println("Couldn't get tinker texture for " + Reference.oreTexNames[meta]);
					} 
					break;
				case 9:
					if(FluidRegistry.getFluid("silver.molten").getIcon() != null) {
						return FluidRegistry.getFluid("silver.molten").getIcon();
					} else {
						System.out.println("Couldn't get tinker texture for " + Reference.oreTexNames[meta]);
					} 
					break;
				case 10:
					if(FluidRegistry.getFluid("nickel.molten").getIcon() != null) {
						return FluidRegistry.getFluid("nickel.molten").getIcon();
					} else {
						System.out.println("Couldn't get tinker texture for " + Reference.oreTexNames[meta]);
					} 
					break;
				case 11:
					if(FluidRegistry.getFluid("tin.molten").getIcon() != null) {
						return FluidRegistry.getFluid("tin.molten").getIcon();
					} else {
						System.out.println("Couldn't get tinker texture for " + Reference.oreTexNames[meta]);
					}
					break;
			}
		}
		return icons[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		for(int i = 0; i < Reference.oreTexNames.length; i++) {
			this.icons[i] = register.registerIcon(Reference.modidLowerCase + ":liquids/molten" + Reference.oreTexNames[i]);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < Reference.oreTexNames.length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}
}