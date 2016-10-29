package projectaurora.api;

//@Optional.Interface(iface = "cr0s.warpdrive.api.IBreathingHelmet", modid = "WarpDrive", striprefs = true)
/**
 * Implement on any armour (preferably helmets) that has the capability to be supply oxygen to the player. Use isOxygenModuleActive for checking whether the armour is currently able to supply oxygen. 
 * 
 * @author The Other
 */
public interface IArmourOxygenMask {
	/**
	 * Used to see the amount of air currently in the tank. 
	 * 
	 * @return The amount of air currently in the tank. 
	 */
	public int getAir();
	
	/**
	 * Reduces the current amount of air in the tank by 1. 
	 */
	public void decreaseAir();
	
	/**
	 * Used to see whether the oxygen-supplying capability is active at the time. 
	 * 
	 * @return Whether the oxygen supplying capability is currently active. 
	 */
	public boolean isOxygenModuleActive();
}