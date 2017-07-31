package etheric.common.world.stability.event;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class InfertilityEvent extends InstabilityEvent {

	public static final String TAG = "INFERTILE";
	
	public InfertilityEvent(ChunkPos chunkPos) {
		super(chunkPos);
	}

	@Override
	public void affectChunk(World world) {
		
		Chunk chunk = world.getChunkFromChunkCoords(chunkPos.x, chunkPos.z);
		
		for (int i = 0; i < chunk.getEntityLists().length; i++) {
			if (chunk.getEntityLists()[i] != null) {
				for (EntityAnimal e : chunk.getEntityLists()[i].getByClass(EntityAnimal.class)) {
					if (world.rand.nextInt(3) == 0) {
						e.addTag(TAG);
					}
				}
			}
		}

	}

}
