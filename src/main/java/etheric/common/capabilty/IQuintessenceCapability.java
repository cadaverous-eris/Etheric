package etheric.common.capabilty;

import net.minecraft.nbt.NBTTagCompound;

public interface IQuintessenceCapability {
	
	public int getAmount();
	public int getCapacity();
	public float getPurity();
	public void setAmount(int amount);
	public void setCapacity(int capacity);
	public void setPurity(float purity);
	
	public int addAmount(int amount, boolean doAdd);
	public int addAmount(int amount, float purity, boolean doAdd);
	public int removeAmount(int amount, boolean doRemove);
	
	public void writeToNBT(NBTTagCompound tag);
	public void readFromNBT(NBTTagCompound tag);

}
