package projectaurora.core;

import net.minecraft.util.DamageSource;

public class DamageSourceHeat extends DamageSource {

	public DamageSourceHeat() {
		super("aurora.space.heat");
		setDamageBypassesArmor();
		setDamageIsAbsolute();
	}
}