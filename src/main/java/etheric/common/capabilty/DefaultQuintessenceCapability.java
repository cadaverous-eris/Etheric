package etheric.common.capabilty;

import etheric.Etheric;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public class DefaultQuintessenceCapability implements IQuintessenceCapability {
	
	public static final String AMOUNT_TAG = Etheric.MODID + ":quintessence_amount";
	public static final String CAPACITY_TAG = Etheric.MODID + ":quintessence_capacity";
	public static final String PURITY_TAG = Etheric.MODID + ":quintessence_purity";
	
	private float amount = 0;
	private float capacity = 0;
	private float purity = 0;

	@Override
	public float getAmount() {
		return amount;
	}

	@Override
	public float getCapacity() {
		return capacity;
	}

	@Override
	public float getPurity() {
		return purity;
	}

	@Override
	public void setAmount(float amount) {
		if (amount <= 0) {
			amount = 0;
			this.purity = 0;
		}
		this.amount = amount;
	}

	@Override
	public void setCapacity(float capacity) {
		this.capacity = capacity;
	}

	@Override
	public void setPurity(float purity) {
		this.purity = MathHelper.clamp(purity, -1F, 1F);
	}

	@Override
	public float addAmount(float amount, boolean doAdd) {
		float added = amount;
		if (this.amount + amount > this.capacity) {
			added = this.capacity - this.amount;
		}
		if (doAdd) {
			this.amount += added;
			if (this.amount <= 0) {
				this.purity = 0;
			}
		}
		return added;
	}

	@Override
	public float addAmount(float amount, float purity, boolean doAdd) {
		if (doAdd && this.purity != purity) {
			float added = addAmount(amount, false);
			float total = this.amount + added;
			
			float p = ((this.amount / total) * this.purity) + ((added / total) * purity) / 2F;
			this.purity = MathHelper.clamp(p, -1F, 1F);
		}
		return addAmount(purity, doAdd);
	}

	@Override
	public float removeAmount(float amount, boolean doRemove) {
		float removed = amount;
		if (this.amount - amount < 0) {
			removed = this.amount;
		}
		if (doRemove) {
			this.amount -= removed;
			if (this.amount <= 0) {
				this.purity = 0;
			}
		}
		return removed;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setFloat(AMOUNT_TAG, this.amount);
		tag.setFloat(CAPACITY_TAG, this.capacity);
		tag.setFloat(PURITY_TAG, this.purity);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(AMOUNT_TAG)) {
			this.amount = tag.getFloat(AMOUNT_TAG);
		}
		if (tag.hasKey(CAPACITY_TAG)) {
			this.capacity = tag.getFloat(CAPACITY_TAG);
		}
		if (tag.hasKey(PURITY_TAG)) {
			this.purity = tag.getFloat(PURITY_TAG);
		}
	}

}
