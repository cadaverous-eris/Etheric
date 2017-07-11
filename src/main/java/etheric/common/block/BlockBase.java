package etheric.common.block;

import etheric.Etheric;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockBase extends Block {

	public BlockBase(String name) {
		super(Material.ROCK);
		
		setRegistryName(Etheric.MODID, name);
		setUnlocalizedName(Etheric.MODID + "." + name);
		
		setHardness(1F);
		setSoundType(SoundType.STONE);
		
		setCreativeTab(Etheric.tab);
	}

}
