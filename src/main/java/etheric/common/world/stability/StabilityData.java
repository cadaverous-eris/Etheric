package etheric.common.world.stability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public class StabilityData {
	
	public static final float MAX_STABILITY = 1F;
	public static final float MIN_STABILITY = 0F;
	
	public static final StabilityData NO_DATA = new StabilityData(-1F);
	
	private final float baseStability;
	private float stability;
	
	public StabilityData(float base, float amount) {
		this.baseStability = base;
		this.stability = amount;
	}
	
	public StabilityData(float amount) {
		this(amount, amount);
	}
	
	public StabilityData(NBTTagCompound tag) {
		if (tag.hasKey("Base", 5)) {
			this.baseStability = tag.getFloat("Base");
		} else {
			this.baseStability = 0.5F;
		}
		if (tag.hasKey("Stability", 5)) {
			this.stability = tag.getFloat("Stability");
		} else {
			this.stability = 0.5F;
		}
	}
	
	public float getStability() {
		return this.stability;
	}
	
	public float getBaseStability() {
		return this.baseStability;
	}
	
	public StabilityData setStability(float amount) {
		this.stability = MathHelper.clamp(amount, MIN_STABILITY, MAX_STABILITY);
		return this;
	}
	
	public StabilityData modifyStability(float amount) {
		this.stability = MathHelper.clamp(this.stability + amount, MIN_STABILITY, MAX_STABILITY);
		return this;
	}
	
	public NBTTagCompound writeToNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.setFloat("Base", this.baseStability);
		tag.setFloat("Stability", this.stability);
		
		return tag;
	}

}
