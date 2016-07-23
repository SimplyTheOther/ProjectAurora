package projectaurora.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import projectaurora.world.WorldModule;

public class EventHandler {
	
	@SubscribeEvent
	public void blockPlace(PlaceEvent event) {
		if(event.world.provider.dimensionId == WorldModule.vulcanID) {
			System.out.println("event.block=" + event.block + ", event.meta=" + event.blockMetadata);
			
			if(event.block == Blocks.iron_block && event.blockMetadata == 0) {
				if(FluidRegistry.getFluid("iron.molten") != null) {
					event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("iron.molten").getBlock());
				}
			}
			
			if(event.block == Blocks.gold_block && event.blockMetadata == 0) {
				if(FluidRegistry.getFluid("gold.molten") != null) {
					event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("gold.molten").getBlock());
				}
			}
			
			if(OreDictionary.doesOreNameExist("blockTin")) {
				for(ItemStack ore : OreDictionary.getOres("blockTin")) {
					if(ore != null) {
						if(event.block == Block.getBlockFromItem(ore.getItem()) && event.blockMetadata == ore.getItemDamage()) {
							System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
							System.out.println("event.block=" + event.block + ", event.meta=" + event.blockMetadata);
							if(FluidRegistry.getFluid("tin.molten") != null) {
								event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("tin.molten").getBlock());
							}
						}
					}
				}
			}
			
			if(OreDictionary.doesOreNameExist("blockLead")) {
				for(ItemStack ore : OreDictionary.getOres("blockLead")) {
					if(ore != null) {
						if(event.block == Block.getBlockFromItem(ore.getItem()) && event.blockMetadata == ore.getItemDamage()) {
							System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
							System.out.println("event.block=" + event.block + ", event.meta=" + event.blockMetadata);
							if(FluidRegistry.getFluid("lead.molten") != null) {
								event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("lead.molten").getBlock());
							}
						}
					}
				}
			}
			
			if(OreDictionary.doesOreNameExist("blockAluminium")) {
				for(ItemStack ore : OreDictionary.getOres("blockAluminium")) {
					if(ore != null) {
						if(event.block == Block.getBlockFromItem(ore.getItem()) && event.blockMetadata == ore.getItemDamage()) {
							System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
							System.out.println("event.block=" + event.block + ", event.meta=" + event.blockMetadata);
							if(FluidRegistry.getFluid("aluminium.molten") != null) {
								event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminium.molten").getBlock());
							}
						}
					}
				}
			}
			
			if(OreDictionary.doesOreNameExist("blockSilver")) {
				for(ItemStack ore : OreDictionary.getOres("blockSilver")) {
					if(ore != null) {
						if(event.block == Block.getBlockFromItem(ore.getItem()) && event.blockMetadata == ore.getItemDamage()) {
							System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
							System.out.println("event.block=" + event.block + ", event.meta=" + event.blockMetadata);
							if(FluidRegistry.getFluid("silver.molten") != null) {
								event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("silver.molten").getBlock());
							}
						}
					}
				}
			}
			
			if(OreDictionary.doesOreNameExist("blockCopper")) {
				for(ItemStack ore : OreDictionary.getOres("blockCopper")) {
					if(ore != null) {
						if(event.block == Block.getBlockFromItem(ore.getItem()) && event.blockMetadata == ore.getItemDamage()) {
							System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
							System.out.println("event.block=" + event.block + ", event.meta=" + event.blockMetadata);
							if(FluidRegistry.getFluid("copper.molten") != null) {
								event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("copper.molten").getBlock());
							}
						}
					}
				}
			}
			
			if(OreDictionary.doesOreNameExist("blockNickel")) {
				for(ItemStack ore : OreDictionary.getOres("blockNickel")) {
					if(ore != null) {
						if(event.block == Block.getBlockFromItem(ore.getItem()) && event.blockMetadata == ore.getItemDamage()) {
							System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
							System.out.println("event.block=" + event.block + ", event.meta=" + event.blockMetadata);
							if(FluidRegistry.getFluid("nickel.molten") != null) {
								event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("nickel.molten").getBlock());
							}
						}
					}
				}
			}
		}
	}
}