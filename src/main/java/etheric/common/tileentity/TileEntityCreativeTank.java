package etheric.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import etheric.common.capabilty.DefaultQuintessenceCapability;
import etheric.common.capabilty.IQuintessenceCapability;
import etheric.common.capabilty.ISuctionProvider;
import etheric.common.capabilty.QuintessenceCapabilityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityCreativeTank extends TEBase implements ITickable, ISuctionProvider {
	
	private int ticks = 0;
	private DefaultQuintessenceCapability internalTank = new DefaultQuintessenceCapability(64);

	@Override
	public void update() {
		
		internalTank.setAmount(64);
		
		if (ticks % 10 == 0) {
			flow();
		}
		
		ticks++;
	}

	@Override
	public int getSuction() {
		return 0;
	}
	
	private void flow() {
		int[] suctions = new int[6];
		int maxSuc = 0;
		for (EnumFacing facing : EnumFacing.VALUES) {
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
		List<EnumFacing> flowDirs = new ArrayList<EnumFacing>();
		for (int i = 0; i < suctions.length; i++) {
			if (suctions[i] == maxSuc && maxSuc > 0) {
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
		IQuintessenceCapability flowInto = world.getTileEntity(pos.offset(dir)).getCapability(QuintessenceCapabilityProvider.quintessenceCapability, dir.getOpposite());
		if (flowInto != null) {
			internalTank.removeAmount(flowInto.addAmount(internalTank.getAmount(), internalTank.getPurity(), true), false);
		}
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

}
