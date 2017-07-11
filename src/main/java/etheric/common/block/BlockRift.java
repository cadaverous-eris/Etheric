package etheric.common.block;

import etheric.common.tileentity.TileEntityRift;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRift extends BlockBase implements ITileEntityProvider{

	public BlockRift(String name) {
		super(name);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityRift();
	}

}
