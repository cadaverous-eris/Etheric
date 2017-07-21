package etheric.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

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
import scala.actors.threadpool.Arrays;

public class TileEntityPipe extends TEBase implements ITickable, ISuctionProvider {

	private DefaultQuintessenceCapability internalTank = new DefaultQuintessenceCapability(4) {
		@Override
		public void onContentsChanged() {
			getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 2);
			markDirty();
		}
	};
	private Suction suction = Suction.NO_SUCTION;
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

	public boolean getConnection(EnumFacing dir) {
		TileEntity te = world.getTileEntity(pos.offset(dir));
		return te != null && te.hasCapability(QuintessenceCapabilityProvider.quintessenceCapability, dir.getOpposite());
	}

	public boolean getQuintConnection(EnumFacing dir) {
		TileEntity te = world.getTileEntity(pos.offset(dir));
		if (te != null && world.getBlockState(pos.offset(dir)).getBlock() == RegistryManager.pipe) {
			return ((TileEntityPipe) te).getAmount() > 0 || (dir != EnumFacing.UP && ((TileEntityPipe) te).getSuction().strength <= 0 && getSuction().strength <= 0);
		}
		if (te != null && te.hasCapability(QuintessenceCapabilityProvider.quintessenceCapability, dir.getOpposite())) {
			return true;
		}
		return false;
	}
	
	public int getAmount() {
		return this.internalTank.getAmount();
	}
	
	public int getCapacity() {
		return this.internalTank.getCapacity();
	}
	
	public float getPurity() {
		return this.internalTank.getPurity();
	}
	
	public int getAdjacentPipeAmount(EnumFacing facing) {
		TileEntity te = world.getTileEntity(pos.offset(facing));
		if (te != null && te instanceof TileEntityPipe) {
			return ((TileEntityPipe) te).getAmount();
		}
		return 0;
	}
	
	@Override
	public boolean hasFastRenderer() {
		return true;
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
		for (EnumFacing facing : EnumFacing.VALUES) {
			if (getConnection(facing)) {
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
	
	private void distribute() {
		TileEntity te = world.getTileEntity(pos.down());
		if (te != null && te instanceof TileEntityPipe) {
			TileEntityPipe tep = (TileEntityPipe) te;
			if (tep.getSuction().strength == getSuction().strength && tep.getAmount() < tep.getCapacity()) {
				internalTank.removeAmount(tep.internalTank.addAmount(Math.min(1, this.internalTank.getAmount()), this.internalTank.getPurity(), true), true);
			}
		}
		List<EnumFacing> horizontals = Lists.newArrayList(EnumFacing.HORIZONTALS);
		while (getAmount() > 1 && horizontals.size() > 0) {
			int i = world.rand.nextInt(horizontals.size());
			if (i < 0) {
				return;
			}
			EnumFacing dir = horizontals.remove(i);
			te = world.getTileEntity(pos.offset(dir));
			if (te != null && te instanceof TileEntityPipe) {
				TileEntityPipe tep = (TileEntityPipe) te;
				if (tep.getSuction().strength == getSuction().strength && tep.getAmount() < getAmount() - 1) {
					internalTank.removeAmount(tep.internalTank.addAmount(Math.min(1, this.internalTank.getAmount()), this.internalTank.getPurity(), true), true);
				}
			}
		}
	}

	private void flow() {
		Suction[] suctions = new Suction[6];
		int maxSuc = 0;
		for (EnumFacing facing : EnumFacing.VALUES) {
			if (getConnection(facing)) {
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
			internalTank.removeAmount(
					flowInto.addAmount(Math.min(2, internalTank.getAmount()), internalTank.getPurity(), true), true);
		}
	}

	@Override
	public void update() {
		if (ticks % 2 == 0) {
			updateSuction();
		}
		if (ticks <= 0) {
			ticks = world.rand.nextInt(12) + 3;
			updateSuction();
			if (!world.isRemote) {
				flow();
			}
			if (getSuction().strength <= 0 && getAmount() > 1) {
				distribute();
			}
		}
		ticks--;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("quintessence")) {
			internalTank.readFromNBT((NBTTagCompound) tag.getTag("quintessence"));
		}
		suction = Suction.readFromNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		NBTTagCompound qTag = new NBTTagCompound();
		internalTank.writeToNBT(qTag);
		tag.setTag("quintessence", qTag);
		suction.writeToNBT(tag);
		return tag;
	}

	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		// debug
		if ((player.getHeldItem(EnumHand.MAIN_HAND).getItem() == RegistryManager.seeing_stone || player.getHeldItem(EnumHand.OFF_HAND).getItem() == RegistryManager.seeing_stone) && !world.isRemote) {
			player.sendMessage(new TextComponentString("Suction: " + getSuction()));
			return true;
		}
		return false;
	}
}
