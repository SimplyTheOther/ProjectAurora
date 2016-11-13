package projectaurora.compat.immersiveengineering;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.Optional.Interface;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
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
		capacity = 0;
		maxReceive = 0;
		maxExtract = 0;
		setCreativeTab(Aurora.tabWorld);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		icons = new IIcon[2];
		
		for(int i = 0; i < 1/*blowingup*/; i++) {
			icons[i] = register.registerIcon(Reference.modidLowerCase + ":note7" + i);
		}
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