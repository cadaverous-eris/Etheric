package etheric.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import etheric.RegistryManager;
import etheric.common.block.BlockPipe;
import etheric.common.capabilty.DefaultQuintessenceCapability;
import etheric.common.capabilty.IQuintessenceCapability;
import etheric.common.capabilty.ISuctionProvider;
import etheric.common.capabilty.QuintessenceCapabilityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityPipe extends TEBase implements ITickable, ISuctionProvider {

	private DefaultQuintessenceCapability internalTank = new DefaultQuintessenceCapability(4);
	private int suction = 0;
	// d u n s w e
	private boolean[] connections = { false, false, false, false, false, false };
	private int ticks = 0;

	public TileEntityPipe() {
		super();
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == QuintessenceCapabilityProvider.quintessenceCapability
				|| super.hasCapability(capability, facing);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == QuintessenceCapabilityProvider.quintessenceCapability) {
			return (T) internalTank;
		}
		return super.getCapability(capability, facing);
	}

	public void updateConnections() {
		for (EnumFacing facing : EnumFacing.VALUES) {
			TileEntity te = world.getTileEntity(pos.offset(facing));
			boolean connect = te != null
					&& te.hasCapability(QuintessenceCapabilityProvider.quintessenceCapability, facing.getOpposite());
			connections[facing.getIndex()] = connect;
			getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
			this.markDirty();
		}
	}

	public boolean getConnection(int i) {
		return connections[i];
	}

	@Override
	public boolean receiveClientEvent(int id, int type) {
		if (id == 1) {
			getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
			this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), true);
			this.markDirty();
			return true;
		} else {
			return super.receiveClientEvent(id, type);
		}
	}

	public void breakBlock(IBlockState state) {

	}

	// get the S U C C
	@Override
	public int getSuction() {
		return suction;
	}

	public void updateSuction() {
		int suc = 0;
		for (int i = 0; i < connections.length; i++) {
			if (connections[i]) {
				EnumFacing facing = EnumFacing.getFront(i);
				TileEntity te = world.getTileEntity(pos.offset(facing));
				if (te != null && te instanceof ISuctionProvider) {
					int teSuc = ((ISuctionProvider) world.getTileEntity(pos.offset(facing))).getSuction();
					if (teSuc > suc) {
						suc = teSuc;
					}
				}
			}
		}
		suction = suc > 0 ? suc - 1 : 0;
	}

	private void flow() {
		int[] suctions = new int[6];
		int maxSuc = 0;
		for (int i = 0; i < 6; i++) {
			if (connections[i]) {
				EnumFacing facing = EnumFacing.getFront(i);
				TileEntity te = world.getTileEntity(pos.offset(facing));
				if (te != null && te instanceof ISuctionProvider) {
					int suc = ((ISuctionProvider) te).getSuction();
					if (suc > getSuction()) {
						suctions[facing.getIndex()] = suc;
						if (suc > maxSuc) {
							maxSuc = suc;
						}
					}
				}
			}
		}
		List<EnumFacing> flowDirs = new ArrayList<EnumFacing>();
		for (int i = 0; i < suctions.length; i++) {
			if (suctions[i] == maxSuc) {
				flowDirs.add(EnumFacing.getFront(i));
			}
		}
		if (flowDirs.size() > 1) {
			flow(flowDirs.get(world.rand.nextInt(flowDirs.size())));
		} else if (flowDirs.size() > 0) {
			flow(flowDirs.get(0));
		}
	}

	private void flow(EnumFacing dir) {
		TileEntity te = world.getTileEntity(pos.offset(dir));
		if (te == null || !te.hasCapability(QuintessenceCapabilityProvider.quintessenceCapability, dir.getOpposite())) {
			return;
		}
		IQuintessenceCapability flowInto = te.getCapability(QuintessenceCapabilityProvider.quintessenceCapability,
				dir.getOpposite());
		if (flowInto != null) {
			internalTank.removeAmount(flowInto.addAmount(internalTank.getAmount(), internalTank.getPurity(), true),
					true);
		}
	}

	@Override
	public void update() {
		if (ticks % 10 == 0) {
			updateSuction();
			if (!world.isRemote) {
				flow();
			}
		}
		ticks++;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("quintessence")) {
			internalTank.readFromNBT((NBTTagCompound) tag.getTag("quintessence"));
		}
		if (tag.hasKey("suction")) {
			suction = tag.getInteger("suction");
		}
		if (tag.hasKey("connections")) {
			deserializeConnections(tag.getInteger("connections"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		NBTTagCompound qTag = new NBTTagCompound();
		internalTank.writeToNBT(qTag);
		tag.setTag("quintessence", qTag);
		tag.setInteger("suction", suction);
		tag.setInteger("connections", serializeConnections());
		return tag;
	}

	private int serializeConnections() {
		int data = 0;
		for (int i = 0; i < connections.length; i++) {
			if (connections[i]) {
				data |= (1 << i);
			}
		}
		return data;
	}

	private void deserializeConnections(int data) {
		for (int i = 0; i < connections.length; i++) {
			connections[i] = (data & (1 << i)) > 0;
		}
	}

	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		String con = "[";
		for (boolean b : connections) {
			con += (b + ", ");
		}
		if (!world.isRemote) {
			player.sendMessage(new TextComponentString(con + "], Suction: " + getSuction()));
		}
		return true;
	}
}
