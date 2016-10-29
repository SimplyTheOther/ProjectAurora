package projectaurora.api;

/**
 * Implement on any armour that has the capability to be freeze resistant. Use isFreezeModuleActive for checking whether the armour is currently freeze resistant. 
 * 
 * @author The Other
 */
public interface IArmourFreezeResistant {
	
	/**
	 * Used to see whether the freeze resistance capability is active at the time. 
	 * 
	 * @return Whether the freeze resistance capability is currently active. 
	 */
	public boolean isFreezeModuleActive();
}
