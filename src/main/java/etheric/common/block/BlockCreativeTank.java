package etheric.common.block;

import etheric.common.tileentity.TileEntityCreativeTank;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCreativeTank extends BlockBase implements ITileEntityProvider {

	public BlockCreativeTank() {
		super("creative_tank");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityCreativeTank();
	}

}
