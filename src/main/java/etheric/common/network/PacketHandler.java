package etheric.common.network;

import etheric.Etheric;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {
	
	public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Etheric.MODID);
	
	private static int id = 0;
	
	public static void registerMessages() {
		
	}

}
