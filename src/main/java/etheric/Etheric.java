package etheric;
import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import etheric.common.CommonProxy;
import etheric.common.Config;
import etheric.common.block.DefaultQuintessenceCapability;
import etheric.common.capabilty.IQuintessenceCapability;
import etheric.common.capabilty.QuintessenceCapabilityStorage;
import etheric.common.network.PacketHandler;
import etheric.common.tileentity.TileEntityRift;
import etheric.common.world.gen.EthericWorldGenerator;
import etheric.common.world.stability.StabilityHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Etheric.MODID, name = Etheric.MOD_NAME, version = Etheric.VERSION, dependencies = Etheric.DEPENDENCIES)
public class Etheric {
	
	public static final String MODID = "etheric";
	public static final String MOD_NAME = "Etheric";
	public static final String VERSION = "0.100";
	public static final String DEPENDENCIES = "";
	
	@Mod.Instance
	public static Etheric instance;
	
	@SidedProxy(clientSide = "etheric.client.ClientProxy", serverSide = "etheric.common.CommonProxy")
	public static CommonProxy proxy;
	
	public static final Logger logger = LogManager.getLogger(MODID);
	
	public static Configuration config;
	
	public static CreativeTabs tab = new CreativeTabs("etheric") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(RegistryManager.seeing_stone);
		}
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		PacketHandler.registerMessages();
		
		File directory = event.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "etheric.cfg"));
        Config.readConfig();
		
        CapabilityManager.INSTANCE.register(IQuintessenceCapability.class, new QuintessenceCapabilityStorage(), DefaultQuintessenceCapability.class);
        
		proxy.preInit(event);
		
		GameRegistry.registerWorldGenerator(new EthericWorldGenerator(), 0);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
		
		if (config.hasChanged()) {
            config.save();
        }
	}

}
