package projectaurora.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import projectaurora.compat.Compat;

public class EventHandler {
	
	@SubscribeEvent
	public void blockPlace(PlaceEvent event) {
		if(!event.world.isRemote) {
			if(event.world.provider.dimensionId == projectaurora.world.WorldModule.vulcanID) {
				evaporateWater(event);
				
				meltBlock("Iron", event);
				meltBlock("Gold", event);
				
				if(!Compat.isImmersiveEngineeringLoaded) {
					meltBlock("Lead", event);
					meltBlock("Aluminium", event);
					meltBlock("Aluminum", event);
					meltBlock("Silver", event);
					meltBlock("Copper", event);
					meltBlock("Nickel", event);
					meltBlock("Cupronickel", event);
					meltBlock("Constantan", event);
					meltBlock("Electrum", event);
					meltBlock("Steel", event);
				} else {
					meltBlockIE(Compat.iEStorage, event);
				}
				
				if(!Compat.isTinkersLoaded) {
					meltBlock("Cobalt", event);
					meltBlock("Ardite", event);
					meltBlock("Manyullyn", event);
					meltBlock("Copper", event);
					meltBlock("Bronze", event);
					meltBlock("Tin", event);
					meltBlock("Aluminium", event);
					meltBlock("Aluminum", event);
					meltBlock("AluminiumBrass", event);
					meltBlock("AluminumBrass", event);
					meltBlock("Alumite", event);
					meltBlock("Steel", event);
				} else {
					meltBlockTiC(Compat.tinkerStorage, event);
				}
				
				if(!Compat.isThermalFoundationLoaded) {
					meltBlock("Copper", event);
					meltBlock("Tin", event);
					meltBlock("Silver", event);
					meltBlock("Lead", event);
					meltBlock("Nickel", event);
					meltBlock("Mithril", event);
					meltBlock("Electrum", event);
					meltBlock("Invar", event);
					meltBlock("Bronze", event);
					meltBlock("Signalum", event);
				} else {
					meltBlockTF(Compat.tFStorage, event);
				}
				
				meltBlock("Brass", event);
				meltBlock("Plutonium", event);
				meltBlock("Terrasteel", event);
				meltBlock("Thaumium", event);
				meltBlock("Uranium", event);
				meltBlock("Zinc", event);
			}
		}
	}

	private void evaporateWater(PlaceEvent event) {
		if(event.block.getMaterial() == Material.water) {
			System.out.println("found water!");
			event.world.setBlock(event.x, event.y, event.z, Blocks.air);
		}
	}

	private void meltBlockTiC(Block tinkerStorage, PlaceEvent event) {
		if(event.block == tinkerStorage) {
			switch(event.world.getBlockMetadata(event.x, event.y, event.z)) {
				case 0:
					placeFluid(event, "Cobalt");
					break;
				case 1:
					placeFluid(event, "Ardite");
					break;
				case 2:
					placeFluid(event, "Manyullyn");
					break;
				case 3:
					placeFluid(event, "Copper");
					break;
				case 4:
					placeFluid(event, "Bronze");
					break;
				case 5:
					placeFluid(event, "Tin");
					break;
				case 6:
					if(FluidRegistry.getFluid("aluminum.molten") != null) {
						event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminum.molten").getBlock());
						System.out.println("block=" + FluidRegistry.getFluid("aluminum.molten").getBlock());
					} else if(FluidRegistry.getFluid("aluminium.molten") != null) {
						event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminium.molten").getBlock());
						System.out.println("block=" + FluidRegistry.getFluid("aluminium.molten").getBlock());
					} else {
						event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
					}
					break;
				case 7:
					if(FluidRegistry.getFluid("aluminumbrass.molten") != null) {
						event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminumbrass.molten").getBlock());
						System.out.println("block=" + FluidRegistry.getFluid("aluminumbrass.molten").getBlock());
					} else if(FluidRegistry.getFluid("aluminiumbrass.molten") != null) {
						event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminiumbrass.molten").getBlock());
						System.out.println("block=" + FluidRegistry.getFluid("aluminiumbrass.molten").getBlock());
					} else {
						event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
					}
					break;
				case 8:
					placeFluid(event, "Alumite");
					break;
				case 9:
					placeFluid(event, "Steel");
					break;
				default:
					break;
			}
		}
	}

	private void meltBlockIE(Block iEStorage, PlaceEvent event) {
		//Copper, Aluminum, Lead, Silver, Nickel, Constantan, Electrum, Steel
		if(event.block == iEStorage) {
			switch(event.world.getBlockMetadata(event.x, event.y, event.z)) {
				case 0:
					placeFluid(event, "Copper");
					break;
				case 1:
					if(FluidRegistry.getFluid("aluminum.molten") != null) {
						event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminum.molten").getBlock());
						System.out.println("block=" + FluidRegistry.getFluid("aluminum.molten").getBlock());
					} else if(FluidRegistry.getFluid("aluminium.molten") != null) {
						event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminium.molten").getBlock());
						System.out.println("block=" + FluidRegistry.getFluid("aluminium.molten").getBlock());
					} else {
						event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
					}
					break;
				case 2:
					placeFluid(event, "Lead");
					break;
				case 3:
					placeFluid(event, "Silver");
					break;
				case 4:
					placeFluid(event, "Nickel");
					break;
				case 5:
					if(FluidRegistry.getFluid("constantan.molten") != null) {
						event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("constantan.molten").getBlock());
						System.out.println("block=" + FluidRegistry.getFluid("constantan.molten").getBlock());
					} else if(FluidRegistry.getFluid("cupronickel.molten") != null) {
						event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("cupronickel.molten").getBlock());
						System.out.println("block=" + FluidRegistry.getFluid("cupronickel.molten").getBlock());
					} else {
						event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
					}
					break;
				case 6:
					placeFluid(event, "Electrum");
					break;
				case 7:
					placeFluid(event, "Steel");
					break;
				default:
					break;
			}
		}
	}
	
	private void meltBlockTF(Block tFStorage, PlaceEvent event) {
		if(event.block == tFStorage) {
			switch(event.world.getBlockMetadata(event.x, event.y, event.z)) {
				case 0:
					placeFluid(event, "Copper");
					break;
				case 1:
					placeFluid(event, "Tin");
					break;
				case 2:
					placeFluid(event, "Silver");
					break;
				case 3:
					placeFluid(event, "Lead");
					break;
				case 4:
					placeFluid(event, "Nickel");
					break;
				case 6:
					placeFluid(event, "Mithril");
					break;
				case 7:
					placeFluid(event, "Electrum");
					break;
				case 8:
					placeFluid(event, "Invar");
					break;
				case 9:
					placeFluid(event, "Bronze");
					break;
				case 10:
					placeFluid(event, "Signalum");
					break;
				default:
					break;
			}
		}
	}

	private void placeFluid(PlaceEvent event, String name) {
		if(FluidRegistry.getFluid(name.toLowerCase() + ".molten") != null) {
			event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid(name.toLowerCase() + ".molten").getBlock());
			System.out.println("block=" + FluidRegistry.getFluid(name.toLowerCase() + ".molten").getBlock());
		} else {
			event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
		}
	}

	/*private void vulcanBlockPlace(PlaceEvent event) {
		//System.out.println("biome=" + event.world.getBiomeGenForCoords(event.x, event.z));
		//System.out.println("variant=" + ((projectaurora.world.vulcan.VulcanChunkManager)event.world.getWorldChunkManager()).getBiomeVariantAt(event.x, event.z));
		System.out.println("block=" + event.block + ", meta=" + event.world.getBlockMetadata(event.x, event.y, event.z));
		
		if(event.block.getMaterial() == Material.water) {
			event.world.setBlock(event.x, event.y, event.z, Blocks.air);
			event.world.spawnParticle("largesmoke", (double)event.x + Math.random(), (double)event.y + 1.2D, (double)event.z + Math.random(), 0.0D, 0.0D, 0.0D);
		}
		
		if(event.block == Blocks.iron_block && event.world.getBlockMetadata(event.x, event.y, event.z) == 0) {
			if(FluidRegistry.getFluid("iron.molten") != null) {
				event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("iron.molten").getBlock());
			} else {
				event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
			}
		}
		
		if(event.block == Blocks.gold_block && event.world.getBlockMetadata(event.x, event.y, event.z) == 0) {
			if(FluidRegistry.getFluid("gold.molten") != null) {
				event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("gold.molten").getBlock());
			} else {
				event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockTin")) {
			for(ItemStack ore : OreDictionary.getOres("blockTin")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("tin.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("tin.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("tin.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockLead")) {
			for(ItemStack ore : OreDictionary.getOres("blockLead")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("lead.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("lead.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("lead.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockAluminium")) {
			for(ItemStack ore : OreDictionary.getOres("blockAluminium")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());

						if(FluidRegistry.getFluid("aluminium.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminium.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("aluminium.molten").getBlock());
						} else if(FluidRegistry.getFluid("aluminum.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminum.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("aluminum.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockAluminum")) {
			for(ItemStack ore : OreDictionary.getOres("blockAluminum")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());

						if(FluidRegistry.getFluid("aluminium.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminium.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("aluminium.molten").getBlock());
						} else if(FluidRegistry.getFluid("aluminum.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminum.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("aluminum.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockSilver")) {
			for(ItemStack ore : OreDictionary.getOres("blockSilver")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());

						if(FluidRegistry.getFluid("silver.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("silver.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("silver.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		else if(OreDictionary.doesOreNameExist("blockCopper")) {
			for(ItemStack ore : OreDictionary.getOres("blockCopper")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());

						if(FluidRegistry.getFluid("copper.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("copper.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("copper.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockNickel")) {
			for(ItemStack ore : OreDictionary.getOres("blockNickel")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());

						if(FluidRegistry.getFluid("nickel.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("nickel.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("nickel.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockBrass")) {
			for(ItemStack ore : OreDictionary.getOres("blockBrass")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("brass.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("brass.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("brass.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockBronze")) {
			for(ItemStack ore : OreDictionary.getOres("blockBronze")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("bronze.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("bronze.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("bronze.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		else if(OreDictionary.doesOreNameExist("blockCobalt")) {
			for(ItemStack ore : OreDictionary.getOres("blockCobalt")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("cobalt.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("cobalt.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("cobalt.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockCupronickel")) {
			for(ItemStack ore : OreDictionary.getOres("blockCupronickel")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("cupronickel.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("cupronickel.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("cupronickel.molten").getBlock());
						} else if(FluidRegistry.getFluid("constantan.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("constantan.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("constantan.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockConstantan")) {
			for(ItemStack ore : OreDictionary.getOres("blockConstantan")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("cupronickel.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("cupronickel.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("cupronickel.molten").getBlock());
						} else if(FluidRegistry.getFluid("constantan.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("constantan.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("constantan.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockElectrum")) {
			for(ItemStack ore : OreDictionary.getOres("blockElectrum")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("electrum.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("electrum.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("electrum.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockInvar")) {
			for(ItemStack ore : OreDictionary.getOres("blockInvar")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("invar.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("invar.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("invar.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockMithril")) {
			for(ItemStack ore : OreDictionary.getOres("blockMithril")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("mithril.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("mithril.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("mithril.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockPlutonium")) {
			for(ItemStack ore : OreDictionary.getOres("blockPlutonium")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("plutonium.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("plutonium.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("plutonium.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockSteel")) {
			for(ItemStack ore : OreDictionary.getOres("blockSteel")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("steel.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("steel.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("steel.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockTerrasteel")) {
			for(ItemStack ore : OreDictionary.getOres("blockTerrasteel")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("terrasteel.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("terrasteel.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("terrasteel.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockThaumium")) {
			for(ItemStack ore : OreDictionary.getOres("blockThaumium")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("thaumium.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("thaumium.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("thaumium.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockUranium")) {
			for(ItemStack ore : OreDictionary.getOres("blockUranium")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("uranium.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("uranium.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("uranium.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
		
		if(OreDictionary.doesOreNameExist("blockZinc")) {
			for(ItemStack ore : OreDictionary.getOres("blockZinc")) {
				if(ore != null) {
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
						if(FluidRegistry.getFluid("zinc.molten") != null) {
							event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("zinc.molten").getBlock());
							System.out.println("block=" + FluidRegistry.getFluid("zinc.molten").getBlock());
						} else {
							event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
						}
					}
				}
			}
		}
	}*/
	
	private void meltBlock(String blockName, PlaceEvent event) {
		if(OreDictionary.doesOreNameExist("block" + blockName)) {
			for(ItemStack ore : OreDictionary.getOres("block" + blockName)) {
				if(ore != null) {
					if((Block.getBlockFromItem(ore.getItem()).getUnlocalizedName().substring(5).contains("ImmersiveEngineering")) || (Block.getBlockFromItem(ore.getItem()).getUnlocalizedName().substring(5).contains("TConstruct")) || (Block.getBlockFromItem(ore.getItem()).getUnlocalizedName().substring(5).contains("ThermalFoundation"))) {
						
					} else {
						if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
							System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
							
							System.out.println("block=" + Block.getBlockFromItem(ore.getItem()).getUnlocalizedName().substring(5) + Block.getBlockFromItem(ore.getItem()).getUnlocalizedName().substring(5).contains("ImmersiveEngineering") + Block.getBlockFromItem(ore.getItem()).getUnlocalizedName().substring(5).contains("TConstruct") + Block.getBlockFromItem(ore.getItem()).getUnlocalizedName().substring(5).contains("ThermalFoundation"));
							
							if(FluidRegistry.getFluid(blockName.toLowerCase() + ".molten") != null) {
								event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid(blockName.toLowerCase() + ".molten").getBlock());
								System.out.println("block=" + FluidRegistry.getFluid(blockName.toLowerCase() + ".molten").getBlock());
							} else {
								event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderDebugText(RenderGameOverlayEvent.Text event) {
		Minecraft minecraft = Minecraft.getMinecraft();
		
		if(minecraft.gameSettings.showDebugInfo && minecraft.theWorld != null && minecraft.thePlayer != null && minecraft.theWorld.getWorldChunkManager() instanceof projectaurora.world.BaseChunkManager) {
			minecraft.theWorld.theProfiler.startSection("auroraBiomeDisplay");
			
			projectaurora.world.BaseChunkManager manager = (projectaurora.world.BaseChunkManager)minecraft.theWorld.getWorldChunkManager();
			int x = MathHelper.floor_double(minecraft.thePlayer.posX);
			int y = MathHelper.floor_double(minecraft.thePlayer.boundingBox.minY);
			int z = MathHelper.floor_double(minecraft.thePlayer.posZ);
			projectaurora.world.biome.AuroraBiome biome = (projectaurora.world.biome.AuroraBiome)minecraft.theWorld.getBiomeGenForCoords(x, z);
			projectaurora.world.biome.AuroraBiomeVariant variant = manager.getBiomeVariantAt(x, z);
			
			event.left.add(null);
			
			biome.addBiomeF3(event.left, minecraft.theWorld, variant, x, y, z);
			
			minecraft.theWorld.theProfiler.endSection();
		}
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.world.provider.dimensionId == projectaurora.world.WorldModule.vulcanID) {
			if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
				try {
					if(event.entityPlayer != null) {
						System.out.println("entityplayernotnull");
						
						if(event.entityPlayer.getCurrentEquippedItem() != null) {
							System.out.println("itemstacknotnull");
							
							if(event.entityPlayer.getCurrentEquippedItem().getItem() != null) {
								System.out.println("itemnotnull");
								
								if(event.entityPlayer.getCurrentEquippedItem().getItem() == Items.water_bucket) {
									if(!event.entityPlayer.capabilities.isCreativeMode) {
										ItemStack prevEquipped = event.entityPlayer.getCurrentEquippedItem();
								
										event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.currentItem, new ItemStack(Items.bucket));
										MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(event.entityPlayer, prevEquipped));
							
										event.world.playSoundEffect((double)((float)event.x + 0.5F), (double)((float)event.y + 0.5F), (double)((float)event.z + 0.5F), "random.fizz", 0.5F, 2.6F + (event.world.rand.nextFloat() - event.world.rand.nextFloat()) * 0.8F);
							
										event.world.spawnParticle("largesmoke", (double)event.x + Math.random(), (double)event.y + 1.2D, (double)event.z + Math.random(), 0.0D, 0.0D, 0.0D);
										event.world.spawnParticle("largesmoke", (double)event.x + Math.random(), (double)event.y + 1.2D, (double)event.z + Math.random(), 0.0D, 0.0D, 0.0D);
										event.world.spawnParticle("largesmoke", (double)event.x + Math.random(), (double)event.y + 1.2D, (double)event.z + Math.random(), 0.0D, 0.0D, 0.0D);
										event.world.spawnParticle("largesmoke", (double)event.x + Math.random(), (double)event.y + 1.2D, (double)event.z + Math.random(), 0.0D, 0.0D, 0.0D);
										event.world.spawnParticle("largesmoke", (double)event.x + Math.random(), (double)event.y + 1.2D, (double)event.z + Math.random(), 0.0D, 0.0D, 0.0D);
										event.world.spawnParticle("largesmoke", (double)event.x + Math.random(), (double)event.y + 1.2D, (double)event.z + Math.random(), 0.0D, 0.0D, 0.0D);
										event.world.spawnParticle("largesmoke", (double)event.x + Math.random(), (double)event.y + 1.2D, (double)event.z + Math.random(), 0.0D, 0.0D, 0.0D);
										event.world.spawnParticle("largesmoke", (double)event.x + Math.random(), (double)event.y + 1.2D, (double)event.z + Math.random(), 0.0D, 0.0D, 0.0D);
									} else {
										event.setCanceled(true);
										System.out.println("cancelled");
									}
								}
							}
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*@SubscribeEvent
	public void onFillBucket(FillBucketEvent event) {
		if(event.world.provider.dimensionId == projectaurora.world.WorldModule.vulcanID) {
			System.out.println("block at targetBW=" + event.world.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ).getUnlocalizedName());
			System.out.println("current=" + event.current.getItem().getUnlocalizedName());
			if(event.current.getItem() == Items.water_bucket) {
				System.out.println("block at target=" + event.world.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ).getUnlocalizedName());
		
				event.result = new ItemStack(Items.bucket);
				event.setCanceled(true);
				event.result = new ItemStack(Items.bucket);
				/*switch(event.target.sideHit) {
					case 0:
						evaporateWater(event, 0, -1, 0);
						break;
					case 1:
						evaporateWater(event, 0, 1, 0);
						break;
					case 2:
						evaporateWater(event, 0, 0, -1);
						break;
					case 3:
						evaporateWater(event, 0, 0, 1);
						break;
					case 4:
						evaporateWater(event, -1, 0, 0);
						break;
					case 5:
						evaporateWater(event, 1, 0, 0);
						break;
					default:
						break;
				}
			}
		}
	}*/

	/*private void evaporateWater(FillBucketEvent event, int x, int y, int z) {
		System.out.println("sidehit=" + event.target.sideHit);
		System.out.println("block at target2=" + event.world.getBlock(event.target.blockX + x, event.target.blockY + y, event.target.blockZ + z).getUnlocalizedName());
		if(event.world.getBlock(event.target.blockX + x, event.target.blockY + y, event.target.blockZ + z).getMaterial() == Material.water) {
			event.world.setBlock(event.target.blockX + x, event.target.blockY + y, event.target.blockZ + z, Blocks.air, 0, 3);
			System.out.println("Aired");
		}
	}*/
}