package projectaurora.api;

/**
 * Implement on any armour that has the capability to be heat resistant. Use isHeatModuleActive for checking whether the armour is currently heat resistant. 
 * 
 * @author The Other
 */
public interface IArmourHeatResistant {
	
	/**
	 * Used to see whether the heat resistance capability is active at the time. 
	 * 
	 * @return Whether the heat resistance capability is currently active. 
	 */
	public boolean isHeatModuleActive();
}
