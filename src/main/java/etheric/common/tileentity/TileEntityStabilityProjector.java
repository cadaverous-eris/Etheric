package etheric.common.tileentity;

import etheric.common.capabilty.Suction;
import etheric.common.world.stability.StabilityHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.ChunkPos;

public class TileEntityStabilityProjector extends TEBase implements ITickable {

	private static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	private static final double BB_EXPAND_XZ = 4D, BB_EXPAND_Y = 2D;
	
	private float[][] chunkStabilities = new float[7][7];
	private float[][] displayVals = new float[7][7];
	private boolean display = false;
	private int ticks = 0;
	private AxisAlignedBB detectionBB = BLOCK_AABB.expand(BB_EXPAND_XZ, BB_EXPAND_Y, BB_EXPAND_XZ).expand(-BB_EXPAND_XZ, -BB_EXPAND_Y, -BB_EXPAND_XZ);
	
	
	@Override
	public void update() {
		if (!world.isRemote) {
			if (ticks % 20 == 0) {
				int dim = this.world.provider.getDimension();
				if (StabilityHandler.getWorldData(dim) != null) {
					ChunkPos chunkPos = new ChunkPos(pos);
					int cX = chunkPos.x;
					int cZ = chunkPos.z;
					for (int z = 0; z < chunkStabilities.length; z++) {
						for (int x = 0; x < chunkStabilities[z].length; x++) {
							chunkStabilities[z][x] = StabilityHandler.getChunkStability(dim, new ChunkPos(cX + x - 3, cZ + z - 3));
						}
					}
					AxisAlignedBB aabb = detectionBB.offset(pos.getX(), pos.getY(), pos.getZ());
					this.display = !world.getEntitiesWithinAABB(EntityPlayer.class, aabb).isEmpty() && world.isBlockIndirectlyGettingPowered(pos) <= 0;
					// System.out.println(aabb);
					getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 2);
					this.markDirty();
				}
			}
		} else {
			for (int z = 0; z < chunkStabilities.length; z++) {
				for (int x = 0; x < chunkStabilities[z].length; x++) {
					if (display) {
						displayVals[z][x] += (chunkStabilities[z][x] - displayVals[z][x]) * 0.05F;
					} else if (displayVals[z][x] != 0) {
						displayVals[z][x] -= displayVals[z][x] * 0.3F;
					}
				}
			}
		}
		ticks++;
	}
	
	public int getTicks() {
		return this.ticks;
	}
	
	public float[][] getDisplays() {
		return this.displayVals.clone();
	}
	
	public boolean display() {
		return this.display;
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
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("display")) {
			display = tag.getBoolean("display");
		}
		if (tag.hasKey("stabilities", 10)) {
			NBTTagCompound stabilitiesTag = tag.getCompoundTag("stabilities");
			for (int z = 0; z < chunkStabilities.length; z++) {
				for (int x = 0; x < chunkStabilities[z].length; x++) {
					chunkStabilities[z][x] = stabilitiesTag.getFloat("(" + x + "," + z + ")");
				}
			}
		}
		//if (tag.hasKey("displays", 10)) {
		//	NBTTagCompound displaysTag = tag.getCompoundTag("displays");
		//	for (int z = 0; z < displayVals.length; z++) {
		//		for (int x = 0; x < displayVals[z].length; x++) {
		//			displayVals[z][x] = displaysTag.getFloat("(" + x + "," + z + ")");
		//		}
		//	}
		//}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.setBoolean("display", display);
		NBTTagCompound stabilitiesTag = new NBTTagCompound();
		for (int z = 0; z < chunkStabilities.length; z++) {
			for (int x = 0; x < chunkStabilities[z].length; x++) {
				stabilitiesTag.setFloat("(" + x + "," + z + ")", chunkStabilities[z][x]);
			}
		}
		tag.setTag("stabilities", stabilitiesTag);
		//NBTTagCompound displaysTag = new NBTTagCompound();
		//for (int z = 0; z < displayVals.length; z++) {
		//	for (int x = 0; x < displayVals[z].length; x++) {
		//		displaysTag.setFloat("(" + x + "," + z + ")", displayVals[z][x]);
		//	}
		//}
		//tag.setTag("displays", displaysTag);
		return tag;
	}

}
