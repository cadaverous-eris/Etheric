package etheric.common.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import etheric.common.capabilty.DefaultQuintessenceCapability;
import etheric.common.capabilty.IQuintessenceCapability;
import etheric.common.capabilty.ISuctionProvider;
import etheric.common.capabilty.QuintessenceCapabilityProvider;
import etheric.common.capabilty.Suction;
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
	public Suction getSuction() {
		return Suction.NO_SUCTION;
	}
	
	private void flow() {
		Suction[] suctions = new Suction[6];
		int maxSuc = 0;
		for (EnumFacing facing : EnumFacing.VALUES) {
			TileEntity te = world.getTileEntity(pos.offset(facing));
			if (te != null && te instanceof ISuctionProvider) {
				Suction suc = ((ISuctionProvider) te).getSuction();
				if (suc.strength > getSuction().strength && suc.minPurity <= internalTank.getPurity() && suc.maxPurity >= internalTank.getPurity()) {
					suctions[facing.getIndex()] = suc.copy();
					if (suc.strength > maxSuc) {
						maxSuc = suc.strength;
					}
				}
			}
		}
		for (int i = 0; i < suctions.length; i++) {
			if ((suctions[i] != null) && (suctions[i].strength == maxSuc) && maxSuc > 0) {
				flow(EnumFacing.getFront(i));
			}
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
