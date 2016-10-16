package projectaurora.core;

import net.minecraft.util.DamageSource;

public class DamageSourceSuffocation extends DamageSource {

	public DamageSourceSuffocation() {
		super("aurora.space.suffocate");
		setDamageBypassesArmor();
		setDamageIsAbsolute();
	}
}