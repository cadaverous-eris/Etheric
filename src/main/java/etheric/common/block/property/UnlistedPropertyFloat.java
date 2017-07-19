package etheric.common.block.property;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyFloat implements IUnlistedProperty<Float> {
	
	String name;
	
	public UnlistedPropertyFloat(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(Float value) {
		return true;
	}

	@Override
	public Class<Float> getType() {
		return Float.class;
	}

	@Override
	public String valueToString(Float value) {
		return value.toString();
	}

}
