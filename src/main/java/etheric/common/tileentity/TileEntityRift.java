package etheric.common.tileentity;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class TileEntityRift extends TEBase implements ITickable {
	private Random rand = new Random();

	public final double MAX_INSTABLITY = 10;
	public final double MIN_INSTABLITY = -10;
	public double instability = 10;
	protected int randTicks = 0;
	protected int ticks = 0;
	protected double size = rand.nextDouble() * 0.5 + 0.5;
	protected float[][] skel = new float[rand.nextInt((int) (size * 2) + 1) + 5 + (int) (size * 3)][4];
	protected float segmentHeight = 0.5F * ((float) (size / 0.75));

	public TileEntityRift() {
		for (int i = 0; i < skel.length; i++) {
			skel[i][0] = (rand.nextFloat() - 0.5F) * 0.375F * ((float) (size / 0.75) * 0.75F);
			skel[i][1] = (rand.nextFloat() - 0.5F) * 0.0625F * ((float) (size / 0.75) * 0.75F);
			skel[i][2] = (rand.nextFloat() - 0.5F) * 0.375F * ((float) (size / 0.75) * 0.75F);
			if (i > 0 && i < skel.length - 1) {
				skel[i][3] = ((float) (size / 0.75)) * 0.125F * ((middleness(i, 0, skel.length - 1) + 0.75F));
			} else {
				skel[i][3] = 0;
			}
		}
	}

	private float middleness(int i, int min, int max) {
		float middle = (max - min) / 2F;
		if (i > (max - min) / 2F) {
			return (max - i) / middle;
		} else {
			return i / middle;
		}

	}

	public float[][] getSkeleton() {
		float[][] ret = new float[skel.length][4];

		for (int i = 1; i < ret.length - 1; i++) {
			for (int j = 0; j < 3; j++) {
				ret[i][j] = skel[i][j];
			}
			ret[i][3] = skel[i][3] * (0.975F + (0.05F * MathHelper.sin(getTicks() * 0.05F)));
		}

		return ret;
	}

	public float getSegmentHeight() {
		return this.segmentHeight;
	}

	public int getTicks() {
		return ticks;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		double minX = pos.getX(), maxX = minX + 1;
		double minY = pos.getY(), maxY = minY + (skel.length * segmentHeight) + skel[skel.length - 1][1] + skel[0][1];
		double minZ = pos.getZ(), maxZ = minZ + 1;

		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	@Override
	public void update() {
		AxisAlignedBB aabb = getRenderBoundingBox().grow(1 + size, (1 + size) / 2, 1 + size);
		double midX = aabb.minX + ((aabb.maxX - aabb.minX) / 2);
		double midY = aabb.minY + ((aabb.maxY - aabb.minY) / 2);
		double midZ = aabb.minZ + ((aabb.maxZ - aabb.minZ) / 2);
		for (Entity e : world.getEntitiesWithinAABB(Entity.class, aabb)) {
			
			double distExp = Math.min(Math.min(e.getDistanceSq(midX, aabb.minY + (1 + size), midZ) * e.getDistanceSq(midX, aabb.minY + (1 + size), midZ), e.getDistanceSq(midX, aabb.maxY - (1 + size), midZ) * e.getDistanceSq(midX, aabb.maxY - (1 + size), midZ)), e.getDistanceSq(midX, midY, midZ) * e.getDistanceSq(midX, midY, midZ));
			// double distExp = Math.min(Math.min(e.getDistanceSq(midX, aabb.minY + (1 + size), midZ), e.getDistanceSq(midX, aabb.maxY - (1 + size), midZ)), e.getDistanceSq(midX, midY, midZ));
			
			double xVel = ((e.posX - midX) / distExp / 1) * 2;
			double yVel = ((e.posY - midY) / distExp / 1) * 2;
			double zVel = ((e.posZ - midZ) / distExp / 1) * 2;
			e.addVelocity(xVel, yVel, zVel);
			
		}
		
		if (randTicks <= 0) {
			randTicks = 10 + rand.nextInt(90);
			int chance = rand.nextInt(10);

			if (chance == 0) {
				if (this.instability - 1 >= this.MIN_INSTABLITY) {
					this.instability -= 1;
				}
			}
			if (chance == 9) {
				if (this.instability + 1 >= this.MAX_INSTABLITY) {
					this.instability += 1;
				}
			}

			if (this.instability == this.MIN_INSTABLITY && rand.nextInt(1000) == 0) {
				// world.createExplosion(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), 5F, true);
			}
		}

		for (int i = 0; i < skel.length; i++) {
			if (rand.nextInt(8) == 0) {
				
				double maxY = this.pos.getY() + (skel.length * segmentHeight) + skel[skel.length - 1][1] - 0.7;
				
				double xOffset = (((0.75 + rand.nextDouble() * 0.25)) * size / 4) * (rand.nextBoolean() ? 1 : -1);
				double zOffset = (((0.75 + rand.nextDouble() * 0.25)) * size / 4) * (rand.nextBoolean() ? 1 : -1);
				
				double x = this.pos.getX() + 0.5 + skel[i][0] + xOffset;
				double y = maxY - (i * segmentHeight) + skel[i][1] - (rand.nextDouble() * segmentHeight);
				double z = this.pos.getZ() + 0.5 + skel[i][0] + zOffset;
				
				world.spawnParticle(EnumParticleTypes.PORTAL, true, x, y, z, -xOffset * 1D, 0, -zOffset * 1D);
				
			}
		}

		randTicks--;
		ticks++;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("instability", 6)) {
			this.instability = tag.getDouble("instability");
		}
		if (tag.hasKey("size", 6)) {
			this.size = tag.getDouble("size");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setDouble("instability", this.instability);
		tag.setDouble("size", this.size);
		return tag;
	}

	@Override
	public boolean receiveClientEvent(int id, int type) {
		if (id == 1) {
			getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
			this.markDirty();
			return true;
		} else {
			return super.receiveClientEvent(id, type);
		}
	}

}
