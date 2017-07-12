package etheric.common.network;

import etheric.Etheric;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Etheric.MODID);
	
	public static void registerMessages() {
		int id = 0;
		
		INSTANCE.registerMessage(MessageStabilityRequest.MessageHolder.class, MessageStabilityRequest.class, id++, Side.SERVER);
		INSTANCE.registerMessage(MessageStabilityData.MessageHolder.class, MessageStabilityData.class, id++, Side.CLIENT);
	}

}
