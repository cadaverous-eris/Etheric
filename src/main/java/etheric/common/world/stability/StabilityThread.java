package etheric.common.world.stability;

import java.util.Random;

import net.minecraft.util.math.ChunkPos;

public class StabilityThread implements Runnable {

	private boolean stop = false;
	private Random rand = new Random();
	
	@Override
	public void run() {
		while (!stop) {
			// To-do: Balance all the numbers, add a method for selecting a random instability event based on how unstable a chunk is
			if (StabilityHandler.worldData.isEmpty()) {
				break;
			}
			
			for (StabilityWorldData worldData : StabilityHandler.worldData.values()) {
				int numChunks = worldData.getChunks().size();
				
				// To-do: scale chance with number of chunks
				if (numChunks > 0 && rand.nextInt(64) == 0) {
					int chunkIndex = rand.nextInt(numChunks);
					ChunkPos chunkPos = worldData.getChunks().toArray(new ChunkPos[numChunks])[chunkIndex];
					StabilityData data = worldData.getStabilityData(chunkPos);
					
					if (data.getStability() > data.getBaseStability()) {
						data.modifyStability(-0.01F);
						StabilityHandler.getDirtyChunks(worldData.getDim()).add(chunkPos);
					} else if (data.getStability() < data.getBaseStability()) {
						data.modifyStability(0.01F);
						StabilityHandler.getDirtyChunks(worldData.getDim()).add(chunkPos);
					}
				}
				
				// To-do: scale chance with number of chunks
				if (numChunks > 0 && rand.nextInt(256) == 0) {
					for (ChunkPos chunkPos : worldData.getChunks()) {
						if (worldData.getStability(chunkPos) < 0.75F && rand.nextInt((int) (4096 * worldData.getStability(chunkPos))) == 0) {
							// To-do: add a stability event to the list in the handler
						}
					}
				}
			}
			
		}
		// To-do: set thread reference to null
		
	}
	
	public void stopThread() {
		this.stop = true;
	}

}
