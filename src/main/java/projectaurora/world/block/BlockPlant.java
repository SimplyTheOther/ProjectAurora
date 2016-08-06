package projectaurora.world.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import projectaurora.core.Aurora;
import projectaurora.core.ClientProxy;
import projectaurora.core.Reference;

public class BlockPlant extends Block {

	@SideOnly(Side.CLIENT)
	public IIcon[] icons = new IIcon[texNames.length];
	
	public static final String[] texNames = new String[] {"Glowstone"};
	
	public static int pass;
	
	public BlockPlant() {
		super(Material.plants);
		this.setCreativeTab(Aurora.tabWorld);
		this.setHardness(0.1F);
		this.setResistance(0.1F);
		this.setStepSound(soundTypeGrass);
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
		return icons[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		for(int i = 0; i < texNames.length; i++) {
			this.icons[i] = register.registerIcon(Reference.modid + ":plants/plant" + texNames[i]);
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