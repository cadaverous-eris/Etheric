package etheric.common.capabilty;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class QuintessenceCapabilityStorage implements IStorage<IQuintessenceCapability> {

	@Override
	public NBTBase writeNBT(Capability<IQuintessenceCapability> capability, IQuintessenceCapability instance,
			EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<IQuintessenceCapability> capability, IQuintessenceCapability instance,
			EnumFacing side, NBTBase nbt) {
		
	}

}
