package etheric;

import etheric.client.renderer.entity.RenderLesserCelestial;
import etheric.client.renderer.tileentity.TileEntityPipeRenderer;
import etheric.common.block.BlockBase;
import etheric.common.block.BlockCreativeTank;
import etheric.common.block.BlockLodestoneOre;
import etheric.common.block.BlockPipe;
import etheric.common.block.BlockRift;
import etheric.common.block.BlockTestTank;
import etheric.common.entity.mob.EntityLesserCelestial;
import etheric.common.item.ItemMaterial;
import etheric.common.item.ItemSeeingStone;
import etheric.common.tileentity.TileEntityCreativeTank;
import etheric.common.tileentity.TileEntityPipe;
import etheric.common.tileentity.TileEntityRift;
import etheric.common.tileentity.TileEntityTestTank;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Etheric.MODID)
@ObjectHolder("etheric")
public class RegistryManager {
	
	public static final Block lodestone_ore = null, celestial_stone = null, rift = null, pipe = null;
	public static final Block creative_tank = null, test_tank = null;
	public static final Item material = null, seeing_stone = null;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		
		event.getRegistry().register(new BlockLodestoneOre());
		event.getRegistry().register(new BlockBase("celestial_stone").setHardness(5F).setResistance(1000.0F));
		event.getRegistry().register(new BlockRift("rift"));
		event.getRegistry().register(new BlockPipe("pipe"));
		
		event.getRegistry().register(new BlockCreativeTank());
		event.getRegistry().register(new BlockTestTank());
		
		GameRegistry.registerTileEntity(TileEntityRift.class, Etheric.MODID + ":rift");
		GameRegistry.registerTileEntity(TileEntityPipe.class, Etheric.MODID + ":pipe");
		
		GameRegistry.registerTileEntity(TileEntityCreativeTank.class, Etheric.MODID + ":creative_tank");
		GameRegistry.registerTileEntity(TileEntityTestTank.class, Etheric.MODID + ":test_tank");
		
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
		
		registerItemBlock(event.getRegistry(), lodestone_ore);
		registerItemBlock(event.getRegistry(), celestial_stone);
		registerItemBlock(event.getRegistry(), rift);
		registerItemBlock(event.getRegistry(), pipe);
		
		registerItemBlock(event.getRegistry(), creative_tank);
		registerItemBlock(event.getRegistry(), test_tank);
		
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		
		event.getRegistry().register(new ItemMaterial());
		event.getRegistry().register(new ItemSeeingStone());
		
	}
	
	@SubscribeEvent
    public static void setupModels(ModelRegistryEvent event) {
		
		registerBlockModel(lodestone_ore);
		registerBlockModel(celestial_stone);
		registerBlockModel(rift);
		registerBlockModel(pipe);
		
		((ItemMaterial) material).registerModels();
		registerItemModel(seeing_stone);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPipe.class, new TileEntityPipeRenderer());
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
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerSprites(TextureStitchEvent.Pre event) {
		event.getMap().registerSprite(new ResourceLocation(Etheric.MODID, "blocks/quintessence_flow"));
		event.getMap().registerSprite(new ResourceLocation(Etheric.MODID, "items/lodestone_sliver"));
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
