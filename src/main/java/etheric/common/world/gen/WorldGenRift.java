package etheric.common.world.gen;

import java.util.Random;

import etheric.RegistryManager;
import etheric.common.Config;
import etheric.common.world.stability.StabilityHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenRift extends WorldGenerator {

	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) {

		if (!worldIn.isRemote && StabilityHandler.getChunkStability(worldIn.provider.getDimension(),
				new ChunkPos(position)) < 0.65F) {
			if (Config.RIFT_FREQUENCY > 0 && rand.nextInt(Config.RIFT_FREQUENCY) == 0) {
				int x = position.getX() + rand.nextInt(16);
				int z = position.getZ() + rand.nextInt(16);
				BlockPos genPos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).up(rand.nextInt(3) + 2);

				for (int i = 0; i < 5; i++) {
					if (!worldIn.isAirBlock(genPos.up(i))) {
						return false;
					}
				}
				worldIn.setBlockState(genPos, RegistryManager.rift.getDefaultState());
				return true;
			}
		}

		return false;
	}

}
