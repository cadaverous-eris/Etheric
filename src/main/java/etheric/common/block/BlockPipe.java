package etheric.common.block;

import etheric.common.capabilty.QuintessenceCapabilityProvider;
import etheric.common.tileentity.TileEntityPipe;
import etheric.common.tileentity.TileEntityTestTank;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

public class BlockPipe extends BlockBase implements ITileEntityProvider {
	
	public static final PropertyBool[] CONNECTIONS = {
			PropertyBool.create("down"),
			PropertyBool.create("up"),
			PropertyBool.create("north"),
			PropertyBool.create("south"),
			PropertyBool.create("west"),
			PropertyBool.create("east")
		};

	public BlockPipe(String name) {
		super(name, Material.ROCK);
		
		setSoundType(SoundType.GLASS);
		setHardness(2F);
		
		IBlockState state = this.blockState.getBaseState();
		for (int i = 0; i < 6; i++) {
			state = state.withProperty(CONNECTIONS[i], false);
		}
		setDefaultState(state);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPipe();
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityPipe) {
			((TileEntityPipe) worldIn.getTileEntity(pos)).breakBlock(state);
		}
		worldIn.removeTileEntity(pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityPipe) {
			TileEntityPipe tep = (TileEntityPipe) te;
			for (int i = 0; i < 6; i++) {
				boolean connected = tep.getConnection(i);
				state = state.withProperty(CONNECTIONS[i], connected);
			}
		}
		return state;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos){
		TileEntityPipe te = (TileEntityPipe)world.getTileEntity(pos);
		te.updateConnections();
		te.markDirty();
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state){
		if (world.getTileEntity(pos) instanceof TileEntityPipe){
			((TileEntityPipe) world.getTileEntity(pos)).updateConnections();
			world.getTileEntity(pos).markDirty();
		}
	}
	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState();
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CONNECTIONS);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return ((TileEntityPipe) world.getTileEntity(pos)).activate(world, pos, state, player, hand, side, hitX,
				hitY, hitZ);
	}

}
