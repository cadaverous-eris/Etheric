package etheric.common.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import etheric.common.capabilty.DefaultQuintessenceCapability;
import etheric.common.capabilty.ISuctionProvider;
import etheric.common.capabilty.QuintessenceCapabilityProvider;
import etheric.common.capabilty.Suction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityTestTank extends TEBase implements ISuctionProvider {

	private DefaultQuintessenceCapability internalTank = new DefaultQuintessenceCapability(64);

	@Override
	public Suction getSuction() {
		return new Suction(50);
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

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if (tag.hasKey("quintessence")) {
			internalTank.readFromNBT((NBTTagCompound) tag.getTag("quintessence"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		NBTTagCompound qTag = new NBTTagCompound();
		internalTank.writeToNBT(qTag);
		tag.setTag("quintessence", qTag);
		return tag;
	}

	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			player.sendMessage(new TextComponentString(
					"Quintessence: " + internalTank.getAmount() + ", Purity: " + internalTank.getPurity()));
		}
		return true;
	}

}
