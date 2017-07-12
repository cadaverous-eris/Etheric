package etheric.common.world.gen;

import java.util.Random;

import etheric.common.world.stability.StabilityHandler;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class EthericWorldGenerator implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		
		StabilityHandler.addChunkData(world.provider.getDimension(), new ChunkPos(chunkX, chunkZ), StabilityHandler.generateChunkStability(random));

	}

}
