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
import etheric.common.capabilty.Suction;
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

	private DefaultQuintessenceCapability internalTank = new DefaultQuintessenceCapability(4) {
		@Override
		public void onContentsChanged() {
			TileEntityPipe.this.markDirty();
			TileEntityPipe.this.world.notifyNeighborsOfStateChange(TileEntityPipe.this.pos, TileEntityPipe.this.getBlockType(), true);
			TileEntityPipe.this.world.notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
		}
	};
	private Suction suction = Suction.NO_SUCTION;
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
		boolean update = false;
		for (EnumFacing facing : EnumFacing.VALUES) {
			TileEntity te = world.getTileEntity(pos.offset(facing));
			boolean connect = te != null
					&& te.hasCapability(QuintessenceCapabilityProvider.quintessenceCapability, facing.getOpposite());
			if (connections[facing.getIndex()] != connect) {
				update = true;
			}
			connections[facing.getIndex()] = connect;
		}
		if (update) {
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

	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}

	public void breakBlock(IBlockState state) {

	}

	// get the S U C C
	@Override
	public Suction getSuction() {
		return suction.copy();
	}

	public void updateSuction() {
		Suction suc = Suction.NO_SUCTION;
		for (int i = 0; i < connections.length; i++) {
			if (connections[i]) {
				EnumFacing facing = EnumFacing.getFront(i);
				TileEntity te = world.getTileEntity(pos.offset(facing));
				if (te != null && te instanceof ISuctionProvider) {
					Suction teSuc = ((ISuctionProvider) world.getTileEntity(pos.offset(facing))).getSuction();
					if (teSuc.strength > suc.strength) {
						suc = teSuc.copy();
					}
				}
			}
		}
		suction = suc.strength > 0 ? new Suction(suc.strength - 1, suc.minPurity, suc.maxPurity) : Suction.NO_SUCTION;
	}

	private void flow() {
		Suction[] suctions = new Suction[6];
		int maxSuc = 0;
		for (int i = 0; i < 6; i++) {
			if (connections[i]) {
				EnumFacing facing = EnumFacing.getFront(i);
				TileEntity te = world.getTileEntity(pos.offset(facing));
				if (te != null && te instanceof ISuctionProvider) {
					Suction suc = ((ISuctionProvider) te).getSuction();
					if (suc.strength > suction.strength && suc.minPurity <= internalTank.getPurity()
							&& suc.maxPurity >= internalTank.getPurity()) {
						suctions[facing.getIndex()] = suc.copy();
						if (suc.strength > maxSuc) {
							maxSuc = suc.strength;
						}
					}
				}
			}
		}
		List<EnumFacing> flowDirs = new ArrayList<EnumFacing>();
		for (int i = 0; i < suctions.length; i++) {
			if (suctions[i] != null && suctions[i].strength == maxSuc) {
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
			internalTank.removeAmount(flowInto.addAmount(Math.min(1, internalTank.getAmount()), internalTank.getPurity(), true),
					true);
		}
	}

	@Override
	public void update() {
		if (ticks % 10 == 0) {
			updateConnections();
			updateSuction();
			if (!world.isRemote) {
				flow();
			}
			getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
			this.markDirty();
		}
		ticks++;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("quintessence")) {
			internalTank.readFromNBT((NBTTagCompound) tag.getTag("quintessence"));
		}
		suction = Suction.readFromNBT(tag);
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
		suction.writeToNBT(tag);
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
		//String con = "[";
		//for (boolean b : connections) {
		//	con += (b + ", ");
		//}
		//if (!world.isRemote) {
		//	player.sendMessage(new TextComponentString(con + "], Suction: " + getSuction()));
		//}
		//return true;
		if (!world.isRemote) {
			player.sendMessage(new TextComponentString(
					"Quintessence: " + internalTank.getAmount() + ", Purity: " + internalTank.getPurity()));
		}
		return true;
	}
}
