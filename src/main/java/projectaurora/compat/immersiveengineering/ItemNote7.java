package projectaurora.compat.immersiveengineering;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import projectaurora.compat.Compat;
import projectaurora.core.Aurora;
import projectaurora.core.Reference;

@Interface(modid="ImmersiveEngineering", iface="cofh.api.energy.IEnergyContainerItem")
public class ItemNote7 extends Item implements IEnergyContainerItem {
	
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;
	
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;
	
	public ItemNote7() {
		//TODO
		this.capacity = 0;
		this.maxReceive = 0;
		this.maxExtract = 0;
		this.maxStackSize = 1;
		setCreativeTab(Aurora.tabWorld);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		icons = new IIcon[2];
		
		for(int i = 0; i < 1/*blowingup*/; i++) {
			icons[i] = register.registerIcon(Reference.modidLowerCase + ":phone" + i);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icons[0];
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(!player.capabilities.isCreativeMode) {
			--stack.stackSize;
		}
		
		if(!world.isRemote) {
			if(Compat.isImmersiveEngineeringLoaded) {
				world.spawnEntityInWorld(new EntityNote7(world, player, /*getEnergyStored(stack)*/10));
			} else {
				world.spawnEntityInWorld(new EntityNote7(world, player, 10));
			}
			
			System.out.println("Tried to spawn entity");
		}
		
		return stack;
	}

	//IEnergyContainerItem
	@Method(modid="ImmersiveEngineering")
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		if (container.stackTagCompound == null) {
			container.stackTagCompound = new NBTTagCompound();
		}
		
		int energy = container.stackTagCompound.getInteger("Energy");
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) {
			energy += energyReceived;
			container.stackTagCompound.setInteger("Energy", energy);
		}
		
		return energyReceived;
	}

	@Method(modid="ImmersiveEngineering")
	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
			return 0;
		}
		
		int energy = container.stackTagCompound.getInteger("Energy");
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
			container.stackTagCompound.setInteger("Energy", energy);
		}
		
		return energyExtracted;
	}

	@Method(modid="ImmersiveEngineering")
	@Override
	public int getEnergyStored(ItemStack container) {
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
			return 0;
		}
		
		return container.stackTagCompound.getInteger("Energy");
	}

	@Method(modid="ImmersiveEngineering")
	@Override
	public int getMaxEnergyStored(ItemStack container) {
		return capacity;
	}
}