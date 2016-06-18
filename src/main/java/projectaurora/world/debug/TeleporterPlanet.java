package projectaurora.world.debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterPlanet extends Teleporter {
	private final WorldServer worldServer;
	private final Random rand;
	private final LongHashMap destinationCoordinateCache = new LongHashMap();

	private final List destinationCoordinateKeys = new ArrayList();
	
	public TeleporterPlanet(WorldServer world) {
		super(world);
		this.worldServer = world;
		this.rand = new Random(world.getSeed());
	}
	
	@Override
	public void placeInPortal(Entity entity, double x, double y, double z, float f) {
		int i = MathHelper.floor_double(entity.posX);
		int j = MathHelper.floor_double(entity.posY);
		int k = MathHelper.floor_double(entity.posZ);
		this.worldServer.getBlock(i, j, k);
		int height = this.worldServer.getHeightValue(i, k);
		entity.setPosition(i, height, k);
		return;
	}
}