package projectaurora.world.chione;

import projectaurora.world.biome.AuroraBiomeVariant;

public class BiomeChioneOcean extends BiomeChione {

	public BiomeChioneOcean(int biomeID) { //TODO chione ocean, unimplemented
		super(biomeID);
		
		this.spawnableAmbientList.clear();
		this.spawnableGoodList.clear();
		this.spawnableEvilList.clear();
		
		this.clearBiomeVariants();
		this.addBiomeVariant(AuroraBiomeVariant.ISLAND);
		
		this.isOcean = true;
		
		super.chioneList.add(this);
	}

	@Override
	public boolean getEnableRiver() {
		return false;
	}
}