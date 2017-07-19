package etheric.common.block.property;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyInt implements IUnlistedProperty<Integer> {
	
	private String name;
	
	public UnlistedPropertyInt(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(Integer value) {
		return true;
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

	@Override
	public String valueToString(Integer value) {
		return value.toString();
	}

}
