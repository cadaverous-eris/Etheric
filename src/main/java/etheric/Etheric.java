package etheric;
import etheric.common.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
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
	
	public static CreativeTabs tab = new CreativeTabs("etheric") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Items.DRAGON_BREATH);
		}
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
