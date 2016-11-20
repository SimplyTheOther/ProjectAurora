package projectaurora.compat.immersiveengineering;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityNote7 extends EntityThrowable {
	private int energy = 0;

	public EntityNote7(World world) {
		super(world);
		System.out.println("World " + this.posX + "," + this.posY + "," + this.posZ + ",");
	}
	
	public EntityNote7(World world, EntityLivingBase entity) {
		super(world, entity);
		System.out.println("World EntityLivingBase " + this.posX + "," + this.posY + "," + this.posZ + ",");
	}
	
	public EntityNote7(World world, double x, double y, double z) {
		super(world, x, y, z);
		System.out.println("World, x,y,z " + this.posX + "," + this.posY + "," + this.posZ + ",");
	}
	
	public EntityNote7(World world, EntityLivingBase entity, int energyStored) {
		super(world, entity);
		this.energy = energyStored;
		System.out.println("World EntityLivingBaseEnergy " + this.posX + "," + this.posY + "," + this.posZ + ",");
	}

	@Override
	protected void onImpact(MovingObjectPosition position) {
		// TODO Auto-generated method stub
		System.out.println("onImpact at " + this.posX + "," + this.posY + "," + this.posZ + ",");
        for (int i = 0; i < 8; ++i) {
            this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }

        if (!this.worldObj.isRemote) {
            this.setDead();
        }
        
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, energy + 0.1F, true);
	}
}
