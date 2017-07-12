package etheric.common.world.stability;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import etheric.Etheric;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Etheric.MODID)
public class StabilityHandler {

	public static final String STABILITY_KEY = "Etheric Dim Stability";

	private static ConcurrentHashMap<Integer, StabilityWorldData> worldData = new ConcurrentHashMap<Integer, StabilityWorldData>();
	private static ConcurrentHashMap<Integer, CopyOnWriteArrayList<ChunkPos>> dirtyChunks = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<ChunkPos>>();

	public static StabilityWorldData getWorldData(int dim) {
		return worldData.get(dim);
	}

	public static CopyOnWriteArrayList<ChunkPos> getDirtyChunks(int dim) {
		return dirtyChunks.get(dim);
	}

	public static void addWorldData(int dim) {
		if (!worldData.containsKey(dim)) {
			worldData.put(dim, new StabilityWorldData(dim));
			dirtyChunks.put(dim, new CopyOnWriteArrayList<ChunkPos>());
			Etheric.logger.info("Added stability data to handler for dim " + dim);
		}
	}

	public static void removeWorldData(int dim) {
		worldData.remove(dim);
		dirtyChunks.remove(dim);
		Etheric.logger.info("Removed stability data to handler for dim " + dim);
	}

	public static void addChunkData(int dim, ChunkPos pos, float stability) {
		worldData.get(dim).addChunk(pos, stability);
	}

	public static void removeChunkData(int dim, ChunkPos pos) {
		worldData.get(dim).removeChunk(pos);
	}

	public static float getChunkData(int dim, ChunkPos pos) {
		return worldData.get(dim).getStability(pos);
	}

	public static float setStability(int dim, ChunkPos pos, float stability) {
		dirtyChunks.get(dim).add(pos);
		return worldData.get(dim).setStability(pos, stability);
	}

	public static float modifyStability(int dim, ChunkPos pos, float amount) {
		dirtyChunks.get(dim).add(pos);
		return worldData.get(dim).modifyStability(pos, amount);
	}

	public static float generateChunkStability(Random rand) {
		return 0.5F + (rand.nextFloat() * 0.5F);
	}

	/*
	 * ======================================== EVENT HANDLERS
	 * =====================================
	 */

	@SubscribeEvent
	public static void onChunkSave(ChunkDataEvent.Save event) {
		int dim = event.getWorld().provider.getDimension();
		ChunkPos pos = event.getChunk().getPos();

		float stability = getChunkData(dim, pos);
		if (stability != -1) {
			event.getData().setFloat(STABILITY_KEY, stability);
		}

		if (!event.getChunk().isLoaded()) {
			removeChunkData(dim, pos);
		}
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkDataEvent.Load event) {
		int dim = event.getWorld().provider.getDimension();
		ChunkPos pos = event.getChunk().getPos();

		float stability;
		if (event.getData().hasKey(STABILITY_KEY)) {
			stability = event.getData().getFloat(STABILITY_KEY);
		} else {
			stability = generateChunkStability(event.getWorld().rand);
		}
		
		addChunkData(dim, pos, stability);
	}

	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event) {
		if (!event.getWorld().isRemote) {
			removeWorldData(event.getWorld().provider.getDimension());
		}
	}

	@SubscribeEvent
	public static void onWorldLoad(WorldEvent.Load event) {
		if (!event.getWorld().isRemote) {
			addWorldData(event.getWorld().provider.getDimension());
		}
	}

	private static int ticks = 0;

	@SubscribeEvent
	public static void onServerWorldTick(TickEvent.WorldTickEvent event) {
		if (event.side != Side.CLIENT) {

			int dim = event.world.provider.getDimension();
			if (ticks % 20 == 0) {
				for (ChunkPos pos : getDirtyChunks(dim)) {
					event.world.markChunkDirty(new BlockPos(pos.x << 4, 16, pos.z << 4), null);
				}
				getDirtyChunks(dim).clear();
			}

			ticks++;
		}
	}

}
