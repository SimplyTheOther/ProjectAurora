package projectaurora.world.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockOre extends ItemBlock {

	public ItemBlockOre(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = "";
		switch(stack.getItemDamage()) {
			case 0: {
				name = "oreCoalAurora";
				break;
			}
			case 1: {
				name = "oreIronAurora";
				break;
			}
			case 2: {
				name = "oreGoldAurora";
				break;
			}
			case 3: {
				name = "oreLapisAurora";
				break;
			}
			case 4: {
				name = "oreDiamondAurora";
				break;
			}
			case 5: {
				name = "oreEmeraldAurora";
				break;
			}
			case 6: {
				name = "oreCopperAurora";
				break;
			}
			case 7: {
				name = "oreAluminiumAurora";
				break;
			}
			case 8: {
				name = "oreLeadAurora";
				break;
			}
			case 9: {
				name = "oreSilverAurora";
				break;
			}
			case 10: {
				name = "oreNickelAurora";
				break;
			}
			case 11: {
				name = "oreTinAurora";
				break;
			}
			case 12: {
				name = "oreQuartzAurora";
				break;
			}
			default: {
				name = "congratsYouBrokeTheGame";
				break;
			}
		}
		return this.getUnlocalizedName() + "." + name;
	}

	@Override
	public int getMetadata(int par1) {
		return par1;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		String lore = "";
		switch(stack.getItemDamage()) {
			case 0: //Bituminous coal
				lore = "C\u2081\u2083\u2087H\u2089\u2087O\u2089NS";
				break;
			case 1: //Nickeliferous Limonite
				lore = "FeO(OH)\u00B7nH\u2082O";
				break;
			case 2://Calaverite
				lore = "AuTe\u2082";
				break;
			case 3://Lapis bearing stone, technically lazurite formula
				lore = "";//= "(Na,Ca)\u2088[(S,Cl,SO\u2084,OH)\u2082|(Al\u2086Si\u2086O\u2082\u2084)";
				break;
			case 4://Kimberlite - not a compound
				lore = "";
				break;
			case 5: //Pegmatite?
				lore = "";
				break;
			case 6: //Chalcopyrite can be 'auriferous' - i thought the term was aurumiferous
				lore = "CuFeS\u2082";
				break;
			case 7: //Gibbsite, what bauxite is mainly? made out of
				lore = "Al(OH)\u2083";
				break;
			case 8://Galena
				lore = "PbS";
				break;
			case 9://Argentite can have lead in it. Maybe. Well, argentite can replace Galena, with lead in it.
				lore = "Ag\u2082S";
				break;
			case 10://Millerite; platinum sulfide has an almost identical formula.
				lore = "NiS";
				break;
			case 11://Cassiterite can be found with iron. Probs. 
				lore = "SnO\u2082";
				break;
			case 12://Technically quartz is the mineral
				lore = "";
				break;
			default: 
				lore = "Not done, or broken";
				break;
		}
		list.add(lore);
	}
}