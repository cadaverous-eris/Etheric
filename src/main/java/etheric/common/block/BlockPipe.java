package etheric.common.block;

import java.util.List;

import javax.annotation.Nullable;

import etheric.common.block.property.UnlistedPropertyBool;
import etheric.common.block.property.UnlistedPropertyFloat;
import etheric.common.block.property.UnlistedPropertyInt;
import etheric.common.capabilty.IQuintessenceCapability;
import etheric.common.capabilty.QuintessenceCapabilityProvider;
import etheric.common.tileentity.TileEntityPipe;
import etheric.common.tileentity.TileEntityTestTank;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPipe extends BlockBase implements ITileEntityProvider {

	public static final UnlistedPropertyInt[] CONNECTIONS = { new UnlistedPropertyInt("down"),
			new UnlistedPropertyInt("up"), new UnlistedPropertyInt("north"), new UnlistedPropertyInt("south"),
			new UnlistedPropertyInt("west"), new UnlistedPropertyInt("east") };

	public BlockPipe(String name) {
		super(name, Material.ROCK);

		setSoundType(SoundType.GLASS);
		setHardness(2F);

		setDefaultState(this.blockState.getBaseState());
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPipe();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedState = (IExtendedBlockState) getExtendedState(state, world, pos);

		double min = 0.3125, max = 0.6875;

		double x1 = min, y1 = min, z1 = min;
		double x2 = max, y2 = max, z2 = max;
		if (extendedState.getValue(CONNECTIONS[0]) > 0) {
			y1 = 0;
		}
		if (extendedState.getValue(CONNECTIONS[1]) > 0) {
			y2 = 1;
		}
		if (extendedState.getValue(CONNECTIONS[2]) > 0) {
			z1 = 0;
		}
		if (extendedState.getValue(CONNECTIONS[3]) > 0) {
			z2 = 1;
		}
		if (extendedState.getValue(CONNECTIONS[4]) > 0) {
			x1 = 0;
		}
		if (extendedState.getValue(CONNECTIONS[5]) > 0) {
			x2 = 1;
		}

		return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
	}

	public static final AxisAlignedBB NODE_AABB = new AxisAlignedBB(0.375, 0.375, 0.375, 0.625, 0.625, 0.625);
	public static final AxisAlignedBB[] CONNECTION_AABB = new AxisAlignedBB[] {
			new AxisAlignedBB(0.375, 0.0, 0.375, 0.625, 0.375, 0.625),
			new AxisAlignedBB(0.375, 0.625, 0.375, 0.625, 1.0, 0.625),
			new AxisAlignedBB(0.375, 0.375, 0.0, 0.625, 0.625, 0.375),
			new AxisAlignedBB(0.375, 0.375, 0.625, 0.625, 0.625, 1.0),
			new AxisAlignedBB(0.0, 0.375, 0.375, 0.375, 0.625, 0.625),
			new AxisAlignedBB(0.625, 0.375, 0.375, 1.0, 0.625, 0.625) };

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, NODE_AABB);
		IExtendedBlockState extendedState = (IExtendedBlockState) getExtendedState(state, worldIn, pos);

		for (int i = 0; i < CONNECTIONS.length; i++) {
			if (extendedState.getValue(CONNECTIONS[i]) > 0) {
				addCollisionBoxToList(pos, entityBox, collidingBoxes, CONNECTION_AABB[i]);
			}
		}

	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		return true;
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
		IExtendedBlockState extendedState = (IExtendedBlockState) state;
		for (int i = 0; i < CONNECTIONS.length; i++) {
			int connected = getConnection(world, pos, EnumFacing.getFront(i));
			extendedState = extendedState.withProperty(CONNECTIONS[i], connected);
		}
		return extendedState;
	}

	private int getConnection(IBlockAccess world, BlockPos pos, EnumFacing dir) {
		TileEntity te = world.getTileEntity(pos.offset(dir));
		if (world.getBlockState(pos.offset(dir)).getBlock() == this) {
			return 1;
		}
		if (te != null && te.hasCapability(QuintessenceCapabilityProvider.quintessenceCapability, dir)) {
			return 2;
		}

		return 0;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		super.neighborChanged(state, world, pos, block, fromPos);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
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
		IUnlistedProperty[] unlisted = new IUnlistedProperty[] { CONNECTIONS[0], CONNECTIONS[1], CONNECTIONS[2],
				CONNECTIONS[3], CONNECTIONS[4], CONNECTIONS[5] };
		return new ExtendedBlockState(this, new IProperty[] {}, unlisted);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return ((TileEntityPipe) world.getTileEntity(pos)).activate(world, pos, state, player, hand, side, hitX, hitY,
				hitZ);
	}

}
