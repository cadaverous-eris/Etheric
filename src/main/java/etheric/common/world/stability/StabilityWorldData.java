package etheric.common.world.stability;

import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

public class StabilityWorldData {
	
	public static final float MAX_STABILITY = 1F;
	public static final float MIN_STABILITY = 0F;
	
	private int dim;
	private ConcurrentHashMap<ChunkPos, Float> chunkData = new ConcurrentHashMap<ChunkPos, Float>();
	
	public StabilityWorldData(int dim) {
		this.dim = dim;
	}
	
	public float getStability(ChunkPos inPos) {
		for (ChunkPos cPos : chunkData.keySet()) {
			if (cPos.equals(inPos)) {
				return chunkData.get(cPos);
			}
		}
		return -1;
	}
	
	public float setStability(ChunkPos inPos, float stability) {
		ChunkPos pos = inPos;
		for (ChunkPos cPos : chunkData.keySet()) {
			if (cPos.equals(inPos)) {
				pos = cPos;
				break;
			}
		}
		chunkData.replace(pos, stability);
		return stability;
	}
	
	public float modifyStability(ChunkPos inPos, float amount) {
		float stability = getStability(inPos);
		if (stability == -1) {
			return -1;
		}
		return setStability(inPos, Math.max(MIN_STABILITY, Math.min(MAX_STABILITY, stability + amount)));
	}
	
	public boolean addChunk(ChunkPos inPos, float stability) {
		for (ChunkPos cPos : chunkData.keySet()) {
			if (cPos.equals(inPos)) {
				return false;
			}
		}
		chunkData.put(inPos, stability);
		return true;
	}
	
	public boolean removeChunk(ChunkPos inPos) {
		for (ChunkPos cPos : chunkData.keySet()) {
			if (cPos.equals(inPos)) {
				chunkData.remove(cPos);
				return true;
			}
		}
		return false;
	}
	
	public int getDim() {
		return this.dim;
	}

}
