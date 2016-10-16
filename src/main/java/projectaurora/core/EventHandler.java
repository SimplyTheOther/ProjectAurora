package projectaurora.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import projectaurora.api.IArmourFreezeResistant;
import projectaurora.api.IArmourHeatResistant;
import projectaurora.api.IArmourOxygenMask;
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
						if(event.entityPlayer.getCurrentEquippedItem() != null) {
							if(event.entityPlayer.getCurrentEquippedItem().getItem() != null) {
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
	
	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent event) {
		if (event.entityLiving == null || event.entityLiving.worldObj.isRemote) {
			return;
		}
		
		EntityLivingBase entity = event.entityLiving;
		int x = MathHelper.floor_double(entity.posX);
		int y = MathHelper.floor_double(entity.posY);
		int z = MathHelper.floor_double(entity.posZ);
		
		boolean isAirBlock = event.entityLiving.worldObj.getBlock(x, y + 1, z) == Content.oxygen;
		boolean isSolidBlockAbove = event.entityLiving.worldObj.getBlock(x, y + 2, z).getMaterial().isSolid();
		
		if(event.entityLiving.worldObj.provider.dimensionId == projectaurora.world.WorldModule.vulcanID) {
			if(isAirBlock) {
				if(entity instanceof EntityPlayer) {
					ItemStack helmet = ((EntityPlayer)entity).getCurrentArmor(3);
					
					if(!isSolidBlockAbove) {
						if(helmet != null) {
							if(helmet.getItem() instanceof IArmourHeatResistant && ((IArmourHeatResistant)helmet.getItem()).isHeatModuleActive()) {
								if(helmet.getItem() instanceof IArmourFreezeResistant && ((IArmourFreezeResistant)helmet.getItem()).isFreezeModuleActive()) {
									//do nothing
								} else {
									if(!event.entityLiving.worldObj.provider.isDaytime()) {
										entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
									}
								} 
							} else if(helmet.getItem() instanceof IArmourFreezeResistant && ((IArmourFreezeResistant)helmet.getItem()).isFreezeModuleActive()) {
								if(event.entityLiving.worldObj.provider.isDaytime()) {
									entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
								}
							} else {
								if(event.entityLiving.worldObj.provider.isDaytime()) {
									entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
								} else {
									entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
								}
							}
						} else {
							if(event.entityLiving.worldObj.provider.isDaytime()) {
								entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
							} else {
								entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
							}
						}
					} //fine
				} else {
					if(!isSolidBlockAbove) {
						if(event.entityLiving.worldObj.provider.isDaytime()) {
							entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
						} else {
							entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
						}
					}
				}
			} else {
				if(entity instanceof EntityPlayer) {
					ItemStack helmet = ((EntityPlayer)entity).getCurrentArmor(3);
					
					if(!isSolidBlockAbove) {
						if(helmet != null) {
							if(helmet.getItem() instanceof IArmourOxygenMask && ((IArmourOxygenMask)helmet.getItem()).isOxygenModuleActive()) {
								if(((IArmourOxygenMask)helmet.getItem()).getAir() > 0) {
									((IArmourOxygenMask)helmet.getItem()).decreaseAir();
									
									if(helmet.getItem() instanceof IArmourHeatResistant && ((IArmourHeatResistant)helmet.getItem()).isHeatModuleActive()) {
										if(helmet.getItem() instanceof IArmourFreezeResistant && ((IArmourFreezeResistant)helmet.getItem()).isFreezeModuleActive()) {
											//do nothing
										} else {
											if(!event.entityLiving.worldObj.provider.isDaytime()) {
												entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
											}
										} 
									} else if(helmet.getItem() instanceof IArmourFreezeResistant && ((IArmourFreezeResistant)helmet.getItem()).isFreezeModuleActive()) {
										if(event.entityLiving.worldObj.provider.isDaytime()) {
											entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
										}
									} else {
										if(event.entityLiving.worldObj.provider.isDaytime()) {
											entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
										} else {
											entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
										}
									}
								} else {
									entity.attackEntityFrom(Aurora.damageSuffocate, 2.0F);
									
									if(helmet.getItem() instanceof IArmourHeatResistant && ((IArmourHeatResistant)helmet.getItem()).isHeatModuleActive()) {
										if(helmet.getItem() instanceof IArmourFreezeResistant && ((IArmourFreezeResistant)helmet.getItem()).isFreezeModuleActive()) {
											//do nothing
										} else {
											if(!event.entityLiving.worldObj.provider.isDaytime()) {
												entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
											}
										} 
									} else if(helmet.getItem() instanceof IArmourFreezeResistant && ((IArmourFreezeResistant)helmet.getItem()).isFreezeModuleActive()) {
										if(event.entityLiving.worldObj.provider.isDaytime()) {
											entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
										}
									} else {
										if(event.entityLiving.worldObj.provider.isDaytime()) {
											entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
										} else {
											entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
										}
									}
								}
							} else {
								entity.attackEntityFrom(Aurora.damageSuffocate, 2.0F);
								
								if(helmet.getItem() instanceof IArmourHeatResistant && ((IArmourHeatResistant)helmet.getItem()).isHeatModuleActive()) {
									if(helmet.getItem() instanceof IArmourFreezeResistant && ((IArmourFreezeResistant)helmet.getItem()).isFreezeModuleActive()) {
										//do nothing
									} else {
										if(!event.entityLiving.worldObj.provider.isDaytime()) {
											entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
										}
									} 
								} else if(helmet.getItem() instanceof IArmourFreezeResistant && ((IArmourFreezeResistant)helmet.getItem()).isFreezeModuleActive()) {
									if(event.entityLiving.worldObj.provider.isDaytime()) {
										entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
									}
								} else {
									if(event.entityLiving.worldObj.provider.isDaytime()) {
										entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
									} else {
										entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
									}
								}
							}
						} else {
							entity.attackEntityFrom(Aurora.damageSuffocate, 2.0F);
							
							if(event.entityLiving.worldObj.provider.isDaytime()) {
								entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
							} else {
								entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
							}
						}
					} else {
						if(helmet != null) {
							if(helmet.getItem() instanceof IArmourOxygenMask && ((IArmourOxygenMask)helmet.getItem()).isOxygenModuleActive()) {
								if(((IArmourOxygenMask)helmet.getItem()).getAir() > 0) {
									((IArmourOxygenMask)helmet.getItem()).decreaseAir();
								} else {
									entity.attackEntityFrom(Aurora.damageSuffocate, 2.0F);
								}
							} else {
								entity.attackEntityFrom(Aurora.damageSuffocate, 2.0F);
							}
						} else {
							entity.attackEntityFrom(Aurora.damageSuffocate, 2.0F);
						}
					}
				} else {
					//if(entity != suffocation resistant) {
					//} else {
					entity.attackEntityFrom(Aurora.damageSuffocate, 2.0F);
					
					if(!isSolidBlockAbove) {
						if(event.entityLiving.worldObj.provider.isDaytime()) {
							entity.attackEntityFrom(Aurora.damageHeat, 2.0F);
						} else {
							entity.attackEntityFrom(Aurora.damageFreeze, 2.0F);
						}
					}
					//}
				}
			}
		}
	}
}