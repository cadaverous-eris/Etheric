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
	
	
	public static boolean PLACE_HOLDER;
	
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
		
		// cfg.getBoolean("Place Holder", CATEGORY_GENERAL, true, "Place holder until actual options are added");
		
		cfg.setCategoryPropertyOrder(CATEGORY_GENERAL, PROPERTY_ORDER_GENERAL);
		cfg.setCategoryPropertyOrder(CATEGORY_WORLD, PROPERTY_ORDER_WORLD);
	}

}
