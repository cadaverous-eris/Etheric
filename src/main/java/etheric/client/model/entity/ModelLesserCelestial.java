package etheric.client.model.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLesserCelestial extends ModelBiped {
	
	public ModelLesserCelestial() {
		this(0.0F);
	}
	
	public ModelLesserCelestial(float modelSize) {
		super(modelSize, 0F, 64, 32);
	}

}
