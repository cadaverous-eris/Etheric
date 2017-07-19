package etheric.common.world.gen;

import java.util.Random;

import etheric.RegistryManager;
import etheric.common.Config;
import etheric.common.world.stability.StabilityHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class EthericWorldGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		
		StabilityHandler.addChunkData(world.provider.getDimension(), new ChunkPos(chunkX, chunkZ), StabilityHandler.generateChunkStability(random));

		if (world.provider.getDimensionType() == DimensionType.OVERWORLD) {
			WorldGenMinable lodestone = new WorldGenMinable(RegistryManager.lodestone_ore.getDefaultState(), Config.LODESTONE_VEIN_SIZE);
			for (int i = 0; i < Config.LODESTONE_FREQUENCY; i++) {
				int x = chunkX * 16 + random.nextInt(16);
				int y = random.nextInt(Config.LODESTONE_MAX_Y - Config.LODESTONE_MIN_Y) + Config.LODESTONE_MIN_Y;
				int z = chunkZ * 16 + random.nextInt(16);
				lodestone.generate(world, random, new BlockPos(x, y, z));
			}
		}
		
	}

}
