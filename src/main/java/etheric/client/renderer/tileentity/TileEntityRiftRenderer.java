package etheric.client.renderer.tileentity;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import etheric.client.handler.ShaderHandler;
import etheric.common.tileentity.TileEntityRift;
import etheric.common.tileentity.TileEntityStabilityProjector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class TileEntityRiftRenderer extends TileEntitySpecialRenderer<TileEntityRift> {

	private static final ResourceLocation END_SKY_TEXTURE = new ResourceLocation("textures/environment/end_sky.png");
	private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
	private static final Random RANDOM = new Random(31100L);
	private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
	private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
	private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

	@Override
	public void render(TileEntityRift te, double x, double y, double z, float partialTicks, int destroyStage,
			float alpha) {
		GlStateManager.disableLighting();
		RANDOM.setSeed(31100L);

		GlStateManager.getFloat(2982, MODELVIEW);
		GlStateManager.getFloat(2983, PROJECTION);
		double d0 = x * x + y * y + z * z;
		int i = this.getPasses(d0);
		float f = 1F;
		boolean flag = false;
		GlStateManager.pushMatrix();

		for (int j = -1; j < i; ++j) {
			GlStateManager.pushMatrix();
			float f1 = 2.0F / (float) (18 - j);

			if (j == 0) {
				this.bindTexture(END_SKY_TEXTURE);
				f1 = 0.15F;
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			}

			if (j >= 1 && j < i) {
				this.bindTexture(END_PORTAL_TEXTURE);
				flag = true;
				Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
			}

			if (j == 1) {
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
			}

			if (j == i) {

			}

			GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
			GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
			GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
			GlStateManager.texGen(GlStateManager.TexGen.S, 9474, this.getBuffer(1.0F, 0.0F, 0.0F, 0.0F));
			GlStateManager.texGen(GlStateManager.TexGen.T, 9474, this.getBuffer(0.0F, 1.0F, 0.0F, 0.0F));
			GlStateManager.texGen(GlStateManager.TexGen.R, 9474, this.getBuffer(0.0F, 0.0F, 1.0F, 0.0F));
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
			GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.loadIdentity();
			GlStateManager.translate(0.5F, 0.5F, 0.0F);
			GlStateManager.scale(0.5F, 0.5F, 1.0F);
			float f2 = (float) (j + 1);
			GlStateManager.translate(17.0F / f2,
					(2.0F + f2 / 1.5F) * ((float) Minecraft.getSystemTime() % 800000.0F / 800000.0F), 0.0F);
			GlStateManager.rotate((f2 * f2 * 4321.0F + f2 * 9.0F) * 2.0F, 0.0F, 0.0F, 1.0F);
			GlStateManager.scale(4.5F - f2 / 4.0F, 4.5F - f2 / 4.0F, 1.0F);

			GlStateManager.multMatrix(PROJECTION);
			GlStateManager.multMatrix(MODELVIEW);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			float f3 = (RANDOM.nextFloat() * 0.5F + 0.1F) * f1;
			float f4 = (RANDOM.nextFloat() * 0.5F + 0.4F) * f1;
			float f5 = (RANDOM.nextFloat() * 0.5F + 0.5F) * f1;

			float[][] skel = te.getSkeleton();
			int points = skel.length;
			double segHeight = te.getSegmentHeight();
			double midX = x + 0.5, midY = y + 0.5, midZ = z + 0.5;
			double baseY = y + (points * segHeight) - skel[points - 1][1] + skel[0][1] - 0.5;

			for (int k = 0; k < points - 1; k++) {
				// north
				bufferbuilder.pos(midX + skel[k][3] + skel[k][0], baseY - (k * segHeight) + skel[k][1],
						midZ + skel[k][3] + skel[k][2]).color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX - skel[k][3] + skel[k][0], baseY - (k * segHeight) + skel[k][1],
						midZ + skel[k][3] + skel[k][2]).color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX - skel[k + 1][3] + skel[k + 1][0],
						baseY - ((k + 1) * segHeight) + skel[k + 1][1], midZ + skel[k + 1][3] + skel[k + 1][2])
						.color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX + skel[k + 1][3] + skel[k + 1][0],
						baseY - ((k + 1) * segHeight) + skel[k + 1][1], midZ + skel[k + 1][3] + skel[k + 1][2])
						.color(f3, f4, f5, 1.0F).endVertex();
				// south
				bufferbuilder.pos(midX - skel[k][3] + skel[k][0], baseY - (k * segHeight) + skel[k][1],
						midZ - skel[k][3] + skel[k][2]).color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX + skel[k][3] + skel[k][0], baseY - (k * segHeight) + skel[k][1],
						midZ - skel[k][3] + skel[k][2]).color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX + skel[k + 1][3] + skel[k + 1][0],
						baseY - ((k + 1) * segHeight) + skel[k + 1][1], midZ - skel[k + 1][3] + skel[k + 1][2])
						.color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX - skel[k + 1][3] + skel[k + 1][0],
						baseY - ((k + 1) * segHeight) + skel[k + 1][1], midZ - skel[k + 1][3] + skel[k + 1][2])
						.color(f3, f4, f5, 1.0F).endVertex();
				// west
				bufferbuilder.pos(midX - skel[k][3] + skel[k][0], baseY - (k * segHeight) + skel[k][1],
						midZ + skel[k][3] + skel[k][2]).color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX - skel[k][3] + skel[k][0], baseY - (k * segHeight) + skel[k][1],
						midZ - skel[k][3] + skel[k][2]).color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX - skel[k + 1][3] + skel[k + 1][0],
						baseY - ((k + 1) * segHeight) + skel[k + 1][1], midZ - skel[k + 1][3] + skel[k + 1][2])
						.color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX - skel[k + 1][3] + skel[k + 1][0],
						baseY - ((k + 1) * segHeight) + skel[k + 1][1], midZ + skel[k + 1][3] + skel[k + 1][2])
						.color(f3, f4, f5, 1.0F).endVertex();
				// east
				bufferbuilder.pos(midX + skel[k][3] + skel[k][0], baseY - (k * segHeight) + skel[k][1],
						midZ - skel[k][3] + skel[k][2]).color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX + skel[k][3] + skel[k][0], baseY - (k * segHeight) + skel[k][1],
						midZ + skel[k][3] + skel[k][2]).color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX + skel[k + 1][3] + skel[k + 1][0],
						baseY - ((k + 1) * segHeight) + skel[k + 1][1], midZ + skel[k + 1][3] + skel[k + 1][2])
						.color(f3, f4, f5, 1.0F).endVertex();
				bufferbuilder.pos(midX + skel[k + 1][3] + skel[k + 1][0],
						baseY - ((k + 1) * segHeight) + skel[k + 1][1], midZ - skel[k + 1][3] + skel[k + 1][2])
						.color(f3, f4, f5, 1.0F).endVertex();
			}

			tessellator.draw();

			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
			this.bindTexture(END_SKY_TEXTURE);
		}

		GlStateManager.popMatrix();

		GlStateManager.disableBlend();
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
		GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
		GlStateManager.enableLighting();

		if (flag) {
			Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
		}
	}

	protected int getPasses(double p_191286_1_) {
		int i;

		if (p_191286_1_ > 36864.0D) {
			i = 1;
		} else if (p_191286_1_ > 25600.0D) {
			i = 3;
		} else if (p_191286_1_ > 16384.0D) {
			i = 5;
		} else if (p_191286_1_ > 9216.0D) {
			i = 7;
		} else if (p_191286_1_ > 4096.0D) {
			i = 9;
		} else if (p_191286_1_ > 1024.0D) {
			i = 11;
		} else if (p_191286_1_ > 576.0D) {
			i = 13;
		} else if (p_191286_1_ > 256.0D) {
			i = 14;
		} else {
			i = 15;
		}

		return i;
	}

	private FloatBuffer getBuffer(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_) {
		this.buffer.clear();
		this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
		this.buffer.flip();
		return this.buffer;
	}

}
