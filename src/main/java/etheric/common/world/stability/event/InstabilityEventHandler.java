package etheric.common.world.stability.event;

import etheric.Etheric;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Etheric.MODID)
public class InstabilityEventHandler {
	
	@SubscribeEvent
	public static void onAnimalBreedEvent(BabyEntitySpawnEvent event) {
		if (event.getParentA().getTags().contains(InfertilityEvent.TAG) || event.getParentB().getTags().contains(InfertilityEvent.TAG)) {
			event.getChild().setDead();
		}
	}

}
