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
import projectaurora.core.Aurora;
import projectaurora.core.Reference;

public class BlockSoft extends Block {
	@SideOnly(Side.CLIENT)
	public IIcon[] icons = new IIcon[texNames.length];

	public static final String[] texNames = new String[] {"Ash"};

	public BlockSoft() {
		super(Material.sand);
		this.setCreativeTab(Aurora.tabWorld);
		this.setHardness(0.6F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeGravel);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return icons[meta];
		//return icons[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister register) {
		for(int i = 0; i < texNames.length; i++) {
			this.icons[i] = register.registerIcon(Reference.modidLowerCase + ":dusts/dust" + texNames[i]);
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