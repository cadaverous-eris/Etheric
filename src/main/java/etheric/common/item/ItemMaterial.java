package etheric.common.item;

import etheric.Etheric;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMaterial extends ItemBase {
	
	public final String[] subItems = new String[] {"lodestone_chunk"};

	public ItemMaterial() {
		super("material");
		setHasSubtypes(true);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list) {
		if (isInCreativeTab(tab)) {
			for (int i = 0; i < subItems.length; i++) {
				list.add(new ItemStack(this, 1, i));
			}
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (stack.getMetadata() < subItems.length) {
			return super.getUnlocalizedName() + "." + subItems[stack.getMetadata()];
		}
		return super.getUnlocalizedName();
	}
	
	public void registerModels() {
		for (int i = 0; i < subItems.length; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(new ResourceLocation(Etheric.MODID, subItems[i]), "inventory"));
		}
	}

}
