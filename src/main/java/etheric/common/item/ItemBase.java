package etheric.common.item;

import etheric.Etheric;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBase extends Item {
	
	public ItemBase(String name) {
		super();
		
		setRegistryName(Etheric.MODID, name);
		setUnlocalizedName(Etheric.MODID + "." + name);
		
		setCreativeTab(Etheric.tab);
	}

}
