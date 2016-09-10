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

public class BlockOre extends Block { // TODO NOTE TO SELF: Quartz? Tin. Platinum? Cobalt? Ardite? Uranium/Yellorite? Iridium? Apatite? Zinc?

	@SideOnly(Side.CLIENT)
	public IIcon[] icons = new IIcon[texNames.length];

	public static final String[] texNames = new String[] {"Coal", "Iron", "Gold", "Lapis", "Diamond", "Emerald", "Copper", "Aluminium", "Lead", "Silver", "Nickel", "Tin", "Quartz"};

	public static int pass;

	public BlockOre() {
		super(Material.rock);
		this.setCreativeTab(Aurora.tabWorld);
		this.setHardness(3.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeStone);
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
		return ClientProxy.oreRenderID;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
		return true;
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
			this.icons[i] = register.registerIcon(Reference.modidLowerCase + ":ores/ore" + texNames[i]);
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