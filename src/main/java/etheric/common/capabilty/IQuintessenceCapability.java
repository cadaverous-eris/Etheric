package etheric.common.capabilty;

import net.minecraft.nbt.NBTTagCompound;

public interface IQuintessenceCapability {
	
	public float getAmount();
	public float getCapacity();
	public float getPurity();
	public void setAmount(float amount);
	public void setCapacity(float capacity);
	public void setPurity(float purity);
	
	public float addAmount(float amount, boolean doAdd);
	public float addAmount(float amount, float purity, boolean doAdd);
	public float removeAmount(float amount, boolean doRemove);
	
	public void writeToNBT(NBTTagCompound tag);
	public void readFromNBT(NBTTagCompound tag);

}
