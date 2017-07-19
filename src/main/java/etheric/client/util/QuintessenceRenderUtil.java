package etheric.client.util;

import etheric.Etheric;
import net.minecraft.util.ResourceLocation;

public class QuintessenceRenderUtil {
	
	public static final ResourceLocation flow_texture = new ResourceLocation(Etheric.MODID, "blocks/quintessence_flow");
	public static final int pure_red = 229, pure_green = 126, pure_blue = 229;
	public static final int impure_red = 86, impure_green = 52, impure_blue = 86;
	
	public static int getRed(float purity) {
		return (int) ((((purity + 1F) / 2F) * (pure_red - impure_red)) + impure_red);
	}
	
	public static int getGreen(float purity) {
		return (int) ((((purity + 1F) / 2F) * (pure_green - impure_green)) + impure_green);
	}

	public static int getBlue(float purity) {
		return (int) ((((purity + 1F) / 2F) * (pure_blue - impure_blue)) + impure_blue);
	}

}
