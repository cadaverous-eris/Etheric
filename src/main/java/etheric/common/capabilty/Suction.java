package etheric.common.capabilty;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public class Suction {
	
	public static final Suction NO_SUCTION = new Suction(0);
	
	public int strength;
	public float minPurity, maxPurity;
	
	public Suction(int str, float min, float max) {
		this.strength = str;
		this.minPurity = min;
		this.maxPurity = max;
	}
	
	public Suction(int str) {
		this(str, -1F, 1F);
	}
	
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagCompound sucTag = new NBTTagCompound();
		
		sucTag.setInteger("strength", this.strength);
		sucTag.setFloat("minPurity", this.minPurity);
		sucTag.setFloat("maxPurity", this.maxPurity);
		
		tag.setTag("Suction", sucTag);
	}
	
	public static Suction readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey("Suction", Constants.NBT.TAG_COMPOUND)) {
			NBTTagCompound sucTag = tag.getCompoundTag("Suction");
			
			int str = sucTag.getInteger("strength");
			float min = sucTag.getFloat("minPurity");
			float max = sucTag.getFloat("maxPurity");
			
			return new Suction(str, min, max);
		}
		return NO_SUCTION;
	}
	
	public String toString() {
		return "Strength=" + strength + ", Range=[" + minPurity + ", " + maxPurity + "]";
	}
	
	public Suction copy() {
		return new Suction(this.strength, this.minPurity, this.maxPurity);
	}

}
