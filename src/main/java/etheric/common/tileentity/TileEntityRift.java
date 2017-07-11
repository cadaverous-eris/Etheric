package etheric.common.tileentity;

import java.util.Random;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityRift extends TEBase implements ITickable{
	private Random rand = new Random();
	
	public final double MAX_INSTABLITY = 10;
	public final double MIN_INSTABLITY = -10;
	public double instability = 10;
	
	
	@Override
	public void update() {
		int chance = rand.nextInt(10);
		
		if(chance == 0){
			if(this.instability - 1 >= this.MIN_INSTABLITY){
				this.instability -= 1;
			}
		}
		if(chance == 10){
			if(this.instability + 1 >= this.MAX_INSTABLITY){
				this.instability += 1;
			}
		}
		
		if(this.instability == this.MIN_INSTABLITY && rand.nextInt(100) == 50){
			world.createExplosion(null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), 5F, true);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound){
		super.readFromNBT(compound);
		this.instability = compound.getDouble("instability");
    }

	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound){
        super.writeToNBT(compound);
        compound.setDouble("instability", this.instability);
        return compound;
    }

	
}
