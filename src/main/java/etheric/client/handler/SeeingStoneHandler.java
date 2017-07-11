package etheric.client.handler;

import com.google.common.base.MoreObjects;

import etheric.Etheric;
import etheric.RegistryManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Etheric.MODID)
@SideOnly(Side.CLIENT)
public class SeeingStoneHandler {

	@SubscribeEvent
	public static void renderSeeingStone(RenderHandEvent event) {

		EntityPlayer player = Minecraft.getMinecraft().player;

		ItemStack stack;
		EnumHand hand;

		if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == RegistryManager.seeing_stone) {
			if (!player.getHeldItem(EnumHand.OFF_HAND).isEmpty()) {
				return;
			}
			stack = player.getHeldItem(EnumHand.MAIN_HAND);
			hand = EnumHand.MAIN_HAND;
		} else if (player.getHeldItem(EnumHand.OFF_HAND).getItem() == RegistryManager.seeing_stone) {
			if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
				return;
			}
			stack = player.getHeldItem(EnumHand.OFF_HAND);
			hand = EnumHand.OFF_HAND;
		} else {
			return;
		}
		
		if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0 || Minecraft.getMinecraft().gameSettings.hideGUI || Minecraft.getMinecraft().playerController.isSpectator()) {
			return;
		}
		
		event.setCanceled(true);

		Minecraft.getMinecraft().entityRenderer.enableLightmap();
		int i = Minecraft.getMinecraft().world.getCombinedLight(
				new BlockPos(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ), 0);
		float f = (float) (i & 65535);
		float f1 = (float) (i >> 16);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);

		renderExtra(event.getPartialTicks());
		
		RenderHelper.enableStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, 0F, -0.1875F);
		Minecraft.getMinecraft().getRenderItem().renderItem(stack, player,
				ItemCameraTransforms.TransformType.NONE, false);
		double dummy = Math.abs(Math.sin(player.posX) * Math.cos(player.posZ));
		String text = ("" + dummy).substring(0, 4);
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		int length = font.getStringWidth(text);
		float scale = 0.00375F;
		GlStateManager.translate(length / 2F * -scale, font.FONT_HEIGHT * 2 * scale, 0F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.scale(scale, scale, -scale);
		font.drawString(text, 0, 0, 0xFF00F0);

		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		RenderHelper.disableStandardItemLighting();

		GlStateManager.popMatrix();

	}

	private static void renderArms() {
		Minecraft mc = Minecraft.getMinecraft();

		if (!mc.player.isInvisible()) {
			GlStateManager.disableCull();
			GlStateManager.pushMatrix();
			GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
			renderArm(EnumHandSide.RIGHT);
			renderArm(EnumHandSide.LEFT);
			GlStateManager.popMatrix();
			GlStateManager.enableCull();
		}
	}

	private static void renderArm(EnumHandSide p_187455_1_) {
		Minecraft mc = Minecraft.getMinecraft();

		mc.getTextureManager().bindTexture(mc.player.getLocationSkin());
		Render<AbstractClientPlayer> render = mc.getRenderManager()
				.<AbstractClientPlayer>getEntityRenderObject(mc.player);
		RenderPlayer renderplayer = (RenderPlayer) render;
		GlStateManager.pushMatrix();
		float f = p_187455_1_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
		GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(f * -41.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.translate(f * 0.3F, -1.1F, 0.45F);

		if (p_187455_1_ == EnumHandSide.RIGHT) {
			renderplayer.renderRightArm(mc.player);
		} else {
			renderplayer.renderLeftArm(mc.player);
		}

		GlStateManager.popMatrix();
	}

	private static void renderArmsFirstPerson(float p_187463_1_, float p_187463_2_, float p_187463_3_) {
		float f = MathHelper.sqrt(p_187463_3_);
		float f1 = -0.2F * MathHelper.sin(p_187463_3_ * (float) Math.PI);
		float f2 = -0.4F * MathHelper.sin(f * (float) Math.PI);
		GlStateManager.translate(0.0F, -f1 / 2.0F, f2);
		float f3 = getAngleFromPitch(p_187463_1_);
		GlStateManager.translate(0.0F, 0.04F + p_187463_2_ * -1.2F + f3 * -0.5F, -0.72F);
		GlStateManager.rotate(f3 * -85.0F, 1.0F, 0.0F, 0.0F);
		renderArms();
		float f4 = MathHelper.sin(f * (float) Math.PI);
		GlStateManager.rotate(f4 * 20.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
	}

	private static float getAngleFromPitch(float pitch) {
		float f = 1.0F - pitch / 45.0F + 0.1F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		f = -MathHelper.cos(f * (float) Math.PI) * 0.1F - 0.05F;
		return 0.02F;
	}

	private static void renderExtra(float partialTicks) {
		AbstractClientPlayer abstractclientplayer = Minecraft.getMinecraft().player;
		float f = abstractclientplayer.getSwingProgress(partialTicks);
		EnumHand enumhand = (EnumHand) MoreObjects.firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND);
		float f1 = abstractclientplayer.prevRotationPitch
				+ (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
		float f2 = abstractclientplayer.prevRotationYaw
				+ (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
		rotateArroundXAndY(f1, f2);
		setLightmap();
		rotateArm(partialTicks);
		GlStateManager.enableRescaleNormal();
            float f3 = enumhand == EnumHand.MAIN_HAND ? f : 0.0F;
            float f5 = 1.0F - (1F);
            renderArmsFirstPerson(f1, f5, f3);

            //float f4 = enumhand == EnumHand.OFF_HAND ? f : 0.0F;
            //float f6 = 1.0F - (0F);
            //renderArmsFirstPerson(f1, f6, f4);
		GlStateManager.disableRescaleNormal();
	}

	private static void rotateArroundXAndY(float angle, float angleY) {
		GlStateManager.pushMatrix();
		GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(angleY, 0.0F, 1.0F, 0.0F);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	private static void setLightmap() {
		AbstractClientPlayer abstractclientplayer = Minecraft.getMinecraft().player;
		int i = Minecraft.getMinecraft().world.getCombinedLight(new BlockPos(abstractclientplayer.posX,
				abstractclientplayer.posY + (double) abstractclientplayer.getEyeHeight(), abstractclientplayer.posZ),
				0);
		float f = (float) (i & 65535);
		float f1 = (float) (i >> 16);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
	}

	private static void rotateArm(float p_187458_1_) {
		EntityPlayerSP entityplayersp = Minecraft.getMinecraft().player;
		float f = entityplayersp.prevRenderArmPitch
				+ (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_187458_1_;
		float f1 = entityplayersp.prevRenderArmYaw
				+ (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_187458_1_;
		GlStateManager.rotate((entityplayersp.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate((entityplayersp.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
	}

}