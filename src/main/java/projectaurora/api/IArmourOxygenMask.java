package projectaurora.api;

//@Optional.Interface(iface = "cr0s.warpdrive.api.IBreathingHelmet", modid = "WarpDrive", striprefs = true)
public interface IArmourOxygenMask {
	public int getAir();
	public void decreaseAir();
	public boolean isOxygenModuleActive();
}