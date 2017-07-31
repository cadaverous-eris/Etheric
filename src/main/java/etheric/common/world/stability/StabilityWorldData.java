package etheric.common.world.stability;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;

public class StabilityWorldData {
	
	private int dim;
	private ConcurrentHashMap<ChunkPos, StabilityData> chunkData = new ConcurrentHashMap<ChunkPos, StabilityData>();
	
	public StabilityWorldData(int dim) {
		this.dim = dim;
	}
	
	public StabilityData getStabilityData(ChunkPos inPos) {
		for (ChunkPos cPos : chunkData.keySet()) {
			if (cPos.equals(inPos)) {
				return chunkData.get(cPos);
			}
		}
		return StabilityData.NO_DATA;
	}
	
	public float getBaseStability(ChunkPos inPos) {
		for (ChunkPos cPos : chunkData.keySet()) {
			if (cPos.equals(inPos)) {
				return chunkData.get(cPos).getBaseStability();
			}
		}
		return -1;
	}
	
	public float getStability(ChunkPos inPos) {
		for (ChunkPos cPos : chunkData.keySet()) {
			if (cPos.equals(inPos)) {
				return chunkData.get(cPos).getStability();
			}
		}
		return -1;
	}
	
	public float setStability(ChunkPos inPos, float stability) {
		for (ChunkPos cPos : chunkData.keySet()) {
			if (cPos.equals(inPos)) {
				return chunkData.get(cPos).setStability(stability).getStability();
			}
		}
		return -1F;
	}
	
	public float modifyStability(ChunkPos inPos, float amount) {
		float stability = getStability(inPos);
		if (stability == -1) {
			return -1;
		}
		return setStability(inPos, Math.max(StabilityData.MIN_STABILITY, Math.min(StabilityData.MAX_STABILITY, stability + amount)));
	}
	
	public boolean addChunk(ChunkPos inPos, StabilityData stability) {
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
	
	public Collection<StabilityData> getAllChunkData() {
		return this.chunkData.values();
	}
	
	public KeySetView<ChunkPos, StabilityData> getChunks() {
		return this.chunkData.keySet();
	}
	
	public int getDim() {
		return this.dim;
	}

}
