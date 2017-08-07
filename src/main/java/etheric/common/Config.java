package etheric.common;

import java.util.ArrayList;
import java.util.List;

import etheric.Etheric;
import net.minecraftforge.common.config.Configuration;

public class Config {
	
	private final static String CATEGORY_GENERAL = "all.general";
	private final static String CATEGORY_WORLD = "all.world";
	
	private final static List<String> PROPERTY_ORDER_GENERAL = new ArrayList<String>();
	private final static List<String> PROPERTY_ORDER_WORLD = new ArrayList<String>();
	
	public static int LODESTONE_FREQUENCY = 0, LODESTONE_VEIN_SIZE = 0, LODESTONE_MIN_Y = 0, LODESTONE_MAX_Y = 0;
	public static int RIFT_FREQUENCY = 0;
	
	public static void readConfig() {
		Configuration cfg = Etheric.config;
		try {
			cfg.load();
			initGeneralConfig(cfg);
		} catch (Exception e1) {

		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}
	
	private static void initGeneralConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General Options");
		cfg.addCustomCategoryComment(CATEGORY_WORLD, "World Generation Options");
		
		LODESTONE_FREQUENCY = cfg.getInt("Lodestone Frequency", CATEGORY_WORLD, 4, 0, 64, "Number of lodestone veins per chunk");
		LODESTONE_VEIN_SIZE = cfg.getInt("Lodestone Vein Size", CATEGORY_WORLD, 5, 0, 64, "Blocks per lodestone vein");
		LODESTONE_MIN_Y = cfg.getInt("Lodestone Min Y", CATEGORY_WORLD, 0, 0, 256, "Minimum y value where lodestone veins can spawn");
		LODESTONE_MAX_Y = cfg.getInt("Lodestone Max Y", CATEGORY_WORLD, 40, 0, 256, "Maximum y value where lodestone veins can spawn");
		
		RIFT_FREQUENCY = cfg.getInt("Rift Frequency", CATEGORY_WORLD, 16, 0, 1024, "How common rifts are in worldgen.\nA rift will spawn in 1 in every x chunks with a low enough stability value.");
		
		PROPERTY_ORDER_WORLD.add("Lodestone Frequency");
		PROPERTY_ORDER_WORLD.add("Lodestone Vein Size");
		PROPERTY_ORDER_WORLD.add("Lodestone Min Y");
		PROPERTY_ORDER_WORLD.add("Lodestone Max Y");
		PROPERTY_ORDER_WORLD.add("Rift Frequency");
		
		cfg.setCategoryPropertyOrder(CATEGORY_GENERAL, PROPERTY_ORDER_GENERAL);
		cfg.setCategoryPropertyOrder(CATEGORY_WORLD, PROPERTY_ORDER_WORLD);
	}

}
