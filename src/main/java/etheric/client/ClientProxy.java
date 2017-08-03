package etheric.client;

import etheric.client.handler.ShaderHandler;
import etheric.client.model.block.PipeModel;
import etheric.client.renderer.entity.RenderLesserCelestial;
import etheric.common.CommonProxy;
import etheric.common.entity.mob.EntityLesserCelestial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		ModelLoaderRegistry.registerLoader(new PipeModel.PipeModelLoader());
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(ShaderHandler.instance);
		ShaderHandler.instance.init();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

}
