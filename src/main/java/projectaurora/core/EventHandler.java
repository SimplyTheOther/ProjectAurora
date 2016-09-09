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
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class EventHandler {//TODO make the correct fluid, make water work
	
	@SubscribeEvent
	public void blockPlace(PlaceEvent event) {
		if(!event.world.isRemote) {
			if(event.world.provider.dimensionId == projectaurora.world.WorldModule.vulcanID) {
				//vulcanBlockPlace(event);
				meltBlock("Iron", event);
				meltBlock("Gold", event);
				meltBlock("Tin", event);
				meltBlock("Lead", event);
				meltBlock("Aluminium", event);
				meltBlock("Aluminum", event);
				meltBlock("Silver", event);
				meltBlock("Copper", event);
				meltBlock("Nickel", event);
				meltBlock("Brass", event);
				meltBlock("Bronze", event);
				meltBlock("Cobalt", event);
				meltBlock("Cupronickel", event);
				meltBlock("Constantan", event);
				meltBlock("Electrum", event);
				meltBlock("Invar", event);
				meltBlock("Mithril", event);
				meltBlock("Plutonium", event);
				meltBlock("Steel", event);
				meltBlock("Terrasteel", event);
				meltBlock("Thaumium", event);
				meltBlock("Uranium", event);
				meltBlock("Zinc", event);
			}
		}
	}
	
	/*private void vulcanBlockPlace(PlaceEvent event) {
		//TODO debugging variants
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
					if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
						System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());
						
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
}