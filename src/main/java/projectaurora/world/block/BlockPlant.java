package projectaurora.world.block;

import java.util.List;

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
import projectaurora.core.Aurora;
import projectaurora.core.ClientProxy;
import projectaurora.core.Reference;

public class BlockPlant extends Block {

	@SideOnly(Side.CLIENT)
	public IIcon[] icons = new IIcon[texNames.length];
	
	@SideOnly(Side.CLIENT)
	public IIcon[] glowstoneIcons = new IIcon[sideNames.length];
	
	public static final String[] texNames = new String[] {"Glowstone"};
	
	public static final String[] sideNames = new String[] {"B", "T", "L", "R"};
	
	public static int pass;
	
	public BlockPlant() {
		super(Material.plants);
		this.setCreativeTab(Aurora.tabWorld);
		this.setHardness(0.1F);
		this.setResistance(0.1F);
		this.setStepSound(soundTypeGrass);
		this.setTickRandomly(true);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		
		switch(meta) {
			case 0:
				return false;
			default:
				return false;
		}
	}
	
	public boolean canSustainBlock(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		
		switch(meta) {
			case 0:
				if(world.getBlock(x, y, z) == Blocks.lava) {
					return true;
				}
			default:
				return false;
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbourBlock) {
		if(!this.canSustainBlock(world, x, y, z)) {
			this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
			world.setBlock(x, y, z, Blocks.air, 0, 2);
		}
	}
	
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		
		switch(meta) {
			case 0:
				setBlockBounds(0F, -0.1F, 0F, 1F, 0.015625F, 1F);
				break;
			default:
				setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
		}
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        float f = 0;
		
		int meta = world.getBlockMetadata(x, y, z);
		
		switch(meta) {
			case 0:
		        f = 0.015625F;
		        break;
		}
		
		byte b0 = 0;
        return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)((float)y + (float)b0 * f), (double)z + this.maxZ);
    }

	@Override
	public int getRenderBlockPass() {
		return this.pass;
	}

	@Override
	public boolean canRenderInPass(int pass) {
		this.pass = pass;
		return true;
	}

	@Override
	public int getRenderType() {
		return ClientProxy.plantRenderID;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		switch(meta) {
			case 0: {
				switch(side) {
					case 0:
					case 1:
						return icons[meta];
					case 2:
						return glowstoneIcons[1];
					case 3:
						return glowstoneIcons[0];
					case 4:
						return glowstoneIcons[2];
					case 5:
						return glowstoneIcons[3];
				}
				break;
			}
		}
		
		return icons[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		for(int i = 0; i < texNames.length; i++) {
			this.icons[i] = register.registerIcon(Reference.modidLowerCase + ":plants/plant" + texNames[i]);
		}
		
		for(int j = 0; j < sideNames.length; j++) {
			this.glowstoneIcons[j] = register.registerIcon(Reference.modidLowerCase + ":plants/plant" + texNames[0] + sideNames[j]);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < texNames.length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}
}