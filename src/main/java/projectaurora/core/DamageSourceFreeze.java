package projectaurora.core;

import net.minecraft.util.DamageSource;

public class DamageSourceFreeze extends DamageSource {

	public DamageSourceFreeze() {
		super("aurora.space.freeze");
		setDamageBypassesArmor();
		setDamageIsAbsolute();
	}
}