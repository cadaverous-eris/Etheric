package etheric.common.capabilty;

import etheric.Etheric;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;

public class DefaultQuintessenceCapability implements IQuintessenceCapability {
	
	public static final String AMOUNT_TAG = Etheric.MODID + ":quintessence_amount";
	public static final String CAPACITY_TAG = Etheric.MODID + ":quintessence_capacity";
	public static final String PURITY_TAG = Etheric.MODID + ":quintessence_purity";
	
	private int amount = 0;
	private int capacity = 0;
	private float purity = 0;
	
	public DefaultQuintessenceCapability(int amount, int capacity) {
		this.amount = amount;
		this.capacity = capacity;
	}
	
	public DefaultQuintessenceCapability(int capacity) {
		this(0, capacity);
	}
	
	public DefaultQuintessenceCapability() {
		this(1);
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public float getPurity() {
		return purity;
	}

	@Override
	public void setAmount(int amount) {
		if (amount <= 0) {
			amount = 0;
			this.purity = 0;
		}
		this.amount = amount;
		onContentsChanged();
	}

	@Override
	public void setCapacity(int capacity) {
		this.capacity = capacity;
		onContentsChanged();
	}

	@Override
	public void setPurity(float purity) {
		this.purity = MathHelper.clamp(purity, -1F, 1F);
		onContentsChanged();
	}

	@Override
	public int addAmount(int amount, boolean doAdd) {
		int added = amount;
		if (this.amount + amount > this.capacity) {
			added = this.capacity - this.amount;
		}
		if (doAdd) {
			this.amount += added;
			if (this.amount <= 0) {
				this.purity = 0;
			}
			onContentsChanged();
		}
		return added;
	}

	@Override
	public int addAmount(int amount, float purity, boolean doAdd) {
		if (doAdd && this.purity != purity) {
			int added = addAmount(amount, false);
			float total = this.amount + added;
			
			float p = ((this.amount / total) * this.purity) + ((added / total) * purity) / 2F;
			this.purity = MathHelper.clamp(p, -1F, 1F);
		}
		return addAmount(amount, doAdd);
	}

	@Override
	public int removeAmount(int amount, boolean doRemove) {
		int removed = amount;
		if (this.amount - amount < 0) {
			removed = this.amount;
		}
		if (doRemove) {
			this.amount -= removed;
			if (this.amount <= 0) {
				this.purity = 0;
			}
			onContentsChanged();
		}
		return removed;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		tag.setInteger(AMOUNT_TAG, this.amount);
		tag.setInteger(CAPACITY_TAG, this.capacity);
		tag.setFloat(PURITY_TAG, this.purity);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey(AMOUNT_TAG)) {
			this.amount = tag.getInteger(AMOUNT_TAG);
		}
		if (tag.hasKey(CAPACITY_TAG)) {
			this.capacity = tag.getInteger(CAPACITY_TAG);
		}
		if (tag.hasKey(PURITY_TAG)) {
			this.purity = tag.getFloat(PURITY_TAG);
		}
	}
	
	public void onContentsChanged() {
		
	}

}
