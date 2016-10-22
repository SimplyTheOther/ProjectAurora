package projectaurora.world.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import projectaurora.compat.Compat;
import projectaurora.core.Reference;
import projectaurora.world.WorldModule;

public class BlockOxygen extends Block implements IFluidBlock {
	private String fluidName;
	
	@SideOnly(Side.CLIENT)
	public IIcon icon;

	public BlockOxygen(Fluid fluid) {
		super(Material.fire);
		this.setHardness(0.0F);
		this.fluidName = fluid.getName();
		fluid.setBlock(this);
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public int getRenderBlockPass() {
		return 1;
	}
	
	@Override
	public boolean isAir(IBlockAccess world, int x, int y, int z) {
		return true;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}
	
	@Override
	public boolean isReplaceable(IBlockAccess world, int x, int y, int z) {
		return true;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return true;
	}
	
	@Override
	public boolean canCollideCheck(int meta, boolean hasBoatInHand) {
		return false;
	}
	
	@Override
	public int getMobilityFlag() {
		return 1;
	}
	
	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return null;
	}
	
	@Override
	public int quantityDropped(Random rand) {
		return 0;
	}
	
	@Override
	public boolean isCollidable() {
		return false;
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		boolean isInSpace = false;
		
		for(int i = 0; i < WorldModule.allDimensionIds.length; i++) {
			for(int j = 0; j < Compat.otherModSpaceDimensions.length; j++) {
				/*if(world.provider.dimensionId == WorldModule.allDimensionIds[i]) {
					//if(dimensionId == planet with atmosphere) {
					//world.setBlock(x, y, z, Blocks.air, 0, 3);
					//} else {
					world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
					System.out.println("updated" + i + "," + j);
					System.out.println(WorldModule.allDimensionIds[i]);
					//}
				} else if(world.provider.dimensionId == Compat.otherModSpaceDimensions[j]) {
					world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
					System.out.println("updated" + i + "," + j);
					System.out.println(Compat.otherModSpaceDimensions[j]);*/
				if(world.provider.dimensionId == WorldModule.allDimensionIds[i] || world.provider.dimensionId == Compat.otherModSpaceDimensions[j]) {
					//if(world.provider.dimensionId == planets with atmospheres) {
					//isInSpace = false;
					//} else {
					isInSpace = true;
					//}
				}
				/*} else {
					System.out.println("currentdim=" +world.provider.dimensionId);
					world.setBlock(x, y, z, Blocks.air, 0, 3);
					System.out.println("aired");
				}*/
			}
		}
		
		if(isInSpace) {
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
		} else {
			world.setBlock(x, y, z, Blocks.air, 0, 3);
		}
	}
	
	@Override
	public int tickRate(World world) {
		return 40;
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (world.isRemote) {
			return;
		}
		
		boolean isInSpace = false;
		
		for(int i = 0; i < WorldModule.allDimensionIds.length; i++) {
			for(int j = 0; j < Compat.otherModSpaceDimensions.length; j++) {
				System.out.println("Spacedim=" + WorldModule.allDimensionIds[i]);
				System.out.println("Spacedim=" + Compat.otherModSpaceDimensions[j]);
				System.out.println("currentdim=" + world.provider.dimensionId);
				
				if(world.provider.dimensionId == WorldModule.allDimensionIds[i] || world.provider.dimensionId == Compat.otherModSpaceDimensions[j]) {
					//if(world.provider.dimensionId == planets with atmospheres) {
					//isInSpace = false;
					//} else {
					isInSpace = true;
					//}
				}
			}
		}
		
		System.out.println("isInSpace=" + isInSpace);
		
		int meta = world.getBlockMetadata(x, y, z);

		if (meta <= 0 || !isInSpace) {
			world.setBlock(x, y, z, Blocks.air, 0, 3); 
		} else {
			spreadAir(world, x, y, z);
		}
		
		world.scheduleBlockUpdate(x, y, z, this, 30 + 2 * meta);
	}
	
	private void spreadAir(World world, int x, int y, int z) {
		System.out.println("spreadedAir at " + x + "," + y + "," + z);
		
		int meta = world.getBlockMetadata(x, y, z);
		
		int airCount = 1;
		int emptyCount = 0;
		int sumConcentration = meta + 1;
		int maxConcentration = meta + 1;
		int minConcentration = meta + 1;
		
		Block plusX = world.getBlock(x + 1, y, z);
		boolean plusXIsAir = world.isAirBlock(x + 1, y, z);
		int plusXConcentration = (plusX != this) ? -1 : world.getBlockMetadata(x + 1, y, z);
		
		if (plusXIsAir) {
			airCount++;
			
			if (plusXConcentration >= 0) {
				sumConcentration += plusXConcentration + 1;
				maxConcentration = Math.max(maxConcentration, plusXConcentration + 1);
				minConcentration = Math.min(minConcentration, plusXConcentration + 1);
			} else {
				emptyCount++;
			}
		}
		
		Block minusX = world.getBlock(x - 1, y, z);
		boolean minusXIsAir = world.isAirBlock(x - 1, y, z);
		int minusXConcentration = (minusX != this) ? -1 : world.getBlockMetadata(x - 1, y, z);
		
		if (minusXIsAir) {
			airCount++;
			
			if (minusXConcentration >= 0) {
				sumConcentration += minusXConcentration + 1;
				maxConcentration = Math.max(maxConcentration, minusXConcentration + 1);
				minConcentration = Math.min(minConcentration, minusXConcentration + 1);
			} else {
				emptyCount++;
			}
		}
		
		Block plusY = world.getBlock(x, y + 1, z);
		boolean plusYIsAir = world.isAirBlock(x, y + 1, z);
		int plusYConcentration = (plusY != this) ? -1 : world.getBlockMetadata(x, y + 1, z);
		
		if (plusYIsAir) {
			airCount++;
			
			if (plusYConcentration >= 0) {
				sumConcentration += plusYConcentration + 1;
				maxConcentration = Math.max(maxConcentration, plusYConcentration + 1);
				minConcentration = Math.min(minConcentration, plusYConcentration + 1);
			} else {
				emptyCount++;
			}
		}
		
		Block minusY = world.getBlock(x, y - 1, z);
		boolean minusYIsAir = world.isAirBlock(x, y - 1, z);
		int minusYConcentration = (minusY != this) ? -1 : world.getBlockMetadata(x, y - 1, z);
		
		if (minusYIsAir) {
			airCount++;
			
			if (minusYConcentration >= 0) {
				sumConcentration += minusYConcentration + 1;
				maxConcentration = Math.max(maxConcentration, minusYConcentration + 1);
				minConcentration = Math.min(minConcentration, minusYConcentration + 1);
			} else {
				emptyCount++;
			}
		}
		
		Block plusZ = world.getBlock(x, y, z + 1);
		boolean plusZIsAir = world.isAirBlock(x, y, z + 1);
		int plusZConcentration = (plusZ != this) ? -1 : world.getBlockMetadata(x, y, z + 1);
		
		if (plusZIsAir) {
			airCount++;
			
			if (plusZConcentration >= 0) {
				sumConcentration += plusZConcentration + 1;
				maxConcentration = Math.max(maxConcentration, plusZConcentration + 1);
				minConcentration = Math.min(minConcentration, plusZConcentration + 1);
			} else {
				emptyCount++;
			}
		}
		
		Block minusZ = world.getBlock(x, y, z - 1);
		boolean minusZIsAir = world.isAirBlock(x, y, z - 1);
		int minusZConcentration = (minusZ != this) ? -1 : world.getBlockMetadata(x, y, z - 1);
		
		if (minusZIsAir) {
			airCount++;
			
			if (minusZConcentration >= 0) {
				sumConcentration += minusZConcentration + 1;
				maxConcentration = Math.max(maxConcentration, minusZConcentration + 1);
				minConcentration = Math.min(minConcentration, minusZConcentration + 1);
			} else {
				emptyCount++;
			}
		}
		
		// air leaks means penalty plus some randomization for visual effects
		if (emptyCount > 0) {
			if (meta < 8) {
				sumConcentration -= emptyCount;
			} else if (meta < 4) {
				sumConcentration -= emptyCount + (world.rand.nextBoolean() ? 0 : emptyCount);
			} else {
				sumConcentration -= airCount;
			}
		}
		if (sumConcentration < 0) sumConcentration = 0;
		
		// compute new concentration, buffing closed space
		int mid_concentration;
		int new_concentration;
		boolean isGrowth = (maxConcentration > 8 && (maxConcentration - minConcentration < 9)) || (maxConcentration > 5 && (maxConcentration - minConcentration < 4));
		
		if (isGrowth) {
			mid_concentration = Math.round(sumConcentration / (float)airCount) - 1;
			new_concentration = sumConcentration - mid_concentration * (airCount - 1);
			new_concentration = Math.max(Math.max(meta + 1, maxConcentration - 1), new_concentration - 20);
		} else {
			mid_concentration = (int) Math.floor(sumConcentration / (float)airCount);
			new_concentration = sumConcentration - mid_concentration * (airCount - 1);
			
			if (emptyCount > 0) {
				new_concentration = Math.max(0, new_concentration - 5);
			}
		}
		
		// apply scale and clamp
		if (mid_concentration < 1) {
			mid_concentration = 0;
		} else if (mid_concentration > 14) {
			mid_concentration = 14;
		} else if (mid_concentration > 0) {
			mid_concentration--;
		}
		
		if (new_concentration < 1) {
			new_concentration = 0;
		} else if (new_concentration > maxConcentration - 2) {
			new_concentration = Math.max(0, maxConcentration - 2);
		} else {
			new_concentration--;
		}
		
		if ((new_concentration < 0 || mid_concentration < 0 || new_concentration > 14 || mid_concentration > 14)) {
			System.out.println("Invalid concentration at step B " + isGrowth + " " + meta + " + "
					+ plusXConcentration + " " + minusXConcentration + " "
					+ plusYConcentration + " " + minusYConcentration + " "
					+ plusZConcentration + " " + minusZConcentration + " = " + sumConcentration + " total, " + emptyCount + " empty / " + airCount
					+ " -> " + new_concentration + " + " + (airCount - 1) + " * " + mid_concentration);
		}
		
		// new_concentration = mid_concentration = 0;
		
		// protect air generator
		if (meta != new_concentration) {
			if (meta == 15) {
				if (plusX != Blocks.brick_block && minusX != Blocks.brick_block
				  && plusY != Blocks.brick_block && minusY != Blocks.brick_block
				  && plusZ != Blocks.brick_block && minusZ != Blocks.brick_block) {
					System.out.println("AirGenerator not found, removing air block at " + x + ", " + y + ", " + z);
					
					world.setBlockMetadataWithNotify(x, y, z, 1, 0);
				} else {
					// keep the block as a source
				}
			} else {
				world.setBlockMetadataWithNotify(x, y, z, new_concentration, 0);
			}
		}
		
		// Check and setup air to adjacent blocks
		// (do not overwrite source block, do not decrease neighbors if we're growing)
		if (plusXIsAir) {
			if (plusX == this) {
				if (plusXConcentration != mid_concentration && plusXConcentration != 15 && (!isGrowth || plusXConcentration < mid_concentration)) {
					world.setBlockMetadataWithNotify(x + 1, y, z, mid_concentration, 0);
				}
			} else {
				world.setBlock(x + 1, y, z, this, mid_concentration, 2);
			}
		}
		
		if (minusXIsAir) {
			if (minusX == this) {
				if (minusXConcentration != mid_concentration && minusXConcentration != 15 && (!isGrowth || minusXConcentration < mid_concentration)) {
					world.setBlockMetadataWithNotify(x - 1, y, z, mid_concentration, 0);
				}
			} else {
				world.setBlock(x - 1, y, z, this, mid_concentration, 2);
			}
		}
		
		if (plusYIsAir) {
			if (plusY == this) {
				if (plusYConcentration != mid_concentration && plusYConcentration != 15 && (!isGrowth || plusYConcentration < mid_concentration)) {
					world.setBlockMetadataWithNotify(x, y + 1, z, mid_concentration, 0);
				}
			} else {
				world.setBlock(x, y + 1, z, this, mid_concentration, 2);
			}
		}
		
		if (minusYIsAir) {
			if (minusY == this) {
				if (minusYConcentration != mid_concentration && minusYConcentration != 15 && (!isGrowth || minusYConcentration < mid_concentration)) {
					world.setBlockMetadataWithNotify(x, y - 1, z, mid_concentration, 0);
				}
			} else {
				world.setBlock(x, y - 1, z, this, mid_concentration, 2);
			}
		}
		
		if (plusZIsAir) {
			if (plusZ == this) {
				if (plusZConcentration != mid_concentration && plusZConcentration != 15 && (!isGrowth || plusZConcentration < mid_concentration)) {
					world.setBlockMetadataWithNotify(x, y, z + 1, mid_concentration, 0);
				}
			} else {
				world.setBlock(x, y, z + 1, this, mid_concentration, 2);
			}
		}
		
		if (minusZIsAir) {
			if (minusZ == this) {
				if (minusZConcentration != mid_concentration && minusZConcentration != 15 && (!isGrowth || minusZConcentration < mid_concentration)) {
					world.setBlockMetadataWithNotify(x, y, z - 1, mid_concentration, 0);
				}
			} else {
				world.setBlock(x, y, z - 1, this, mid_concentration, 2);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
		Block sideBlock = world.getBlock(x, y, z);
		
		if (sideBlock == this) {
			return false;
		}
		
		return world.isAirBlock(x, y, z);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < 16; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		this.icon = register.registerIcon(Reference.modidLowerCase + ":gases/oxygen");
	}

	@Override
	public Fluid getFluid() {
		return FluidRegistry.getFluid(fluidName);
	}

	@Override
	public FluidStack drain(World world, int x, int y, int z, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canDrain(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public float getFilledPercentage(World world, int x, int y, int z) {
		return 1.0F;
	}
}