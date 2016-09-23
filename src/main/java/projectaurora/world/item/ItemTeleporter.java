package projectaurora.world.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import projectaurora.core.Aurora;
import projectaurora.core.Reference;
import projectaurora.world.WorldModule;
import projectaurora.world.biome.AuroraBiome;
import projectaurora.world.debug.TeleporterPlanet;

public class ItemTeleporter extends Item {
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	
	public ItemTeleporter() {
		setHasSubtypes(true);
		setMaxDamage(0);
		setCreativeTab(Aurora.tabWorld);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		icons = new IIcon[2];
		
		for(int i = 0; i < 2; i++) {
			icons[i] = register.registerIcon(Reference.modidLowerCase + ":teleporter" + i);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icons[meta];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = "";
		switch(stack.getItemDamage()) {
			case 0: {
				name = "overworld";
				break;
			}
			case 1: {
				name = "vulcan";
				break;
			}
			default: {
				name = "broken";
				break;
			}
		}
		return super.getUnlocalizedName() + "." + name;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < 2; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			System.out.println("lshiftdown");
			
			for(int x = 0; x < 15; x++) {
				for(int y = 0; y < 31; y++) {
					for(int z = 0; z < 15; z++) {
						
						switch(stack.getItemDamage()) {
							case 0: {
								int playerX = (int) player.posX;
								int playerY = (int) player.posY;
								int playerZ = (int) player.posZ;
								
								if(world.provider.dimensionId == WorldModule.vulcanID) {
									AuroraBiome biome = (AuroraBiome)world.getBiomeGenForCoords(playerX, playerZ);
									
									if(world.getBlock(playerX - 8 + x, playerY + 16 - y, playerZ - 8 + z) == biome.stoneBlock) {
										world.setBlock(playerX - 8 + x, playerY + 16 - y, playerZ - 8 + z, Blocks.air, 0, 3);
										System.out.println("deleted" + x + y + 8 + z);
									}
								}
								
								if(world.getBlock(playerX - 8 + x, playerY + 16 - y, playerZ - 8 + z) == world.getBiomeGenForCoords(playerX, playerZ).topBlock || world.getBlock(playerX - 8 + x, playerY + 16 - y, playerZ - 8 + z) == world.getBiomeGenForCoords(playerX, playerZ).fillerBlock || world.getBlock(playerX - 8 + x, playerY + 16 - y, playerZ - 8 + z) == Blocks.stone || world.getBlock(playerX - 8 + x, playerY + 16 - y, playerZ - 8 + z) == Blocks.gravel) {
									world.setBlock(playerX - 8 + x, playerY + 16 - y, playerZ - 8 + z, Blocks.air, 0, 3);
									System.out.println("deleted" + x + y + 8 + z);
								}
								break;
							}
							case 1: {
								if(world.getBlock((int)player.posX - 8 + x, (int)player.posY + 16 - y, (int)player.posZ - 8 + z).getMaterial() == Material.lava) {
									world.setBlock((int)player.posX - 8 + x, (int)player.posY + 16 - y, (int)player.posZ - 8 + z, Blocks.air, 0, 3);
									
									System.out.println("deleted" + x + y + 8 + z);
								}
								break;
							}
							default: {
								if(world.getBlock((int)player.posX - 8 + x, (int)player.posY + 16 - y, (int)player.posZ - 8 + z).getMaterial() == Material.water) {
									world.setBlock((int)player.posX - 8 + x, (int)player.posY + 16 - y, (int)player.posZ - 8 + z, Blocks.air, 0, 3);
									
									System.out.println("deleted" + x + y + 8 + z);
								}
								break;
							}
						}
					}
				}
			}
		} else {
		
			int dimension = 0;
		
			switch(this.getDamage(stack)) {
				case 0: {
					dimension = 0;
					System.out.println("onItemRightClick overworld");
					break;
				}
				case 1: {
					dimension = WorldModule.vulcanID;
					System.out.println("onItemRightClick vulcan");
					break;
				}
				default: {
					dimension = 0;
					System.out.println("onItemRightClick default");
					break;
				}
			}
		
			Side side = FMLCommonHandler.instance().getEffectiveSide();
	
			if(side == Side.SERVER) {
				if(player instanceof EntityPlayerMP) {
					if(player.ridingEntity == null && player.riddenByEntity == null && player instanceof EntityPlayer) {
						((EntityPlayerMP)player).mcServer.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)player, dimension, new TeleporterPlanet((WorldServer)world));
						System.out.println("teleported");
					}
				}
			}
		}
		return stack;
	}
}