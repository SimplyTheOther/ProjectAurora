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
		if(event.world.provider.dimensionId == projectaurora.world.WorldModule.vulcanID) {
			//TODO debugging variants
			System.out.println("biome=" + event.world.getBiomeGenForCoords(event.x, event.z));
			System.out.println("variant=" + ((projectaurora.world.vulcan.VulcanChunkManager)event.world.getWorldChunkManager()).getBiomeVariantAt(event.x, event.z));
			
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
							} else if(FluidRegistry.getFluid("aluminum.molten") != null) {
								event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("aluminum.molten").getBlock());
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
							} else {
								event.world.setBlock(event.x, event.y, event.z, Blocks.lava);
							}
						}
					}
				}
			}
			
			if(OreDictionary.doesOreNameExist("blockCopper")) {
				for(ItemStack ore : OreDictionary.getOres("blockCopper")) {
					if(ore != null) {
						if(event.block == Block.getBlockFromItem(ore.getItem()) && event.world.getBlockMetadata(event.x, event.y, event.z) == ore.getItemDamage()) {
							System.out.println("block=" + Block.getBlockFromItem(ore.getItem()) + ", meta=" + ore.getItemDamage());

							if(FluidRegistry.getFluid("copper.molten") != null) {
								event.world.setBlock(event.x, event.y, event.z, FluidRegistry.getFluid("copper.molten").getBlock());
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
}