package etheric.common.capabilty;

import etheric.common.block.DefaultQuintessenceCapability;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class QuintessenceCapabilityProvider implements ICapabilityProvider {

	private IQuintessenceCapability capability = null;

	public QuintessenceCapabilityProvider() {
		this.capability = new DefaultQuintessenceCapability();
	}

	public QuintessenceCapabilityProvider(IQuintessenceCapability capability) {
		this.capability = capability;
	}

	@CapabilityInject(IQuintessenceCapability.class)
	public static final Capability<IQuintessenceCapability> quintessenceCapability = null;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == quintessenceCapability;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (quintessenceCapability != null && capability == quintessenceCapability) {
			return (T) capability;
		}
		return null;
	}

}
