package etheric;

import etheric.client.renderer.entity.RenderLesserCelestial;
import etheric.common.block.BlockBase;
import etheric.common.entity.mob.EntityLesserCelestial;
import etheric.common.item.ItemSeeingStone;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Etheric.MODID)
@ObjectHolder("etheric")
public class RegistryManager {
	
	public static final Block celestial_stone = null;
	public static final Item seeing_stone = null;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		
		event.getRegistry().register(new BlockBase("celestial_stone").setHardness(5F).setResistance(1000.0F));
		
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		
		registerItemBlock(event.getRegistry(), celestial_stone);
		
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		
		event.getRegistry().register(new ItemSeeingStone());
		
	}
	
	@SubscribeEvent
    public static void setupModels(ModelRegistryEvent event) {
		
		registerBlockModel(celestial_stone);
		registerItemModel(seeing_stone);
		
	}
	
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		int id = 0;
		
		EntityRegistry.registerModEntity(new ResourceLocation(Etheric.MODID, "lesser_celestial"), EntityLesserCelestial.class, "lesser_celestial", id++, Etheric.MODID, 96, 1, true, 16777215, 13158600);
		
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerEntityRenderers(RegistryEvent.Register<EntityEntry> event) {
		
		RenderingRegistry.registerEntityRenderingHandler(EntityLesserCelestial.class, new RenderLesserCelestial.Factory());
		
	}
	
	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
	
	private static void registerBlockModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName().toString(), "inventory"));
	}
	
	private static void registerItemModel(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString(), "inventory"));
	}

}
