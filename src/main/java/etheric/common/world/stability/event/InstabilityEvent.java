package etheric.common.world.stability.event;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public abstract class InstabilityEvent {
	
	protected ChunkPos chunkPos;
	
	public InstabilityEvent(ChunkPos chunkPos) {
		this.chunkPos = chunkPos;
	}
	
	public abstract void affectChunk(World world);

}
