package etheric.client.renderer.tileentity;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.primitives.Ints;

import etheric.Etheric;
import etheric.RegistryManager;
import etheric.client.util.QuintessenceRenderUtil;
import etheric.client.util.RenderUtil;
import etheric.common.tileentity.TileEntityStabilityProjector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.client.model.ModelLoader;

public class TileEntityStabilityProjectorRenderer extends TileEntitySpecialRenderer<TileEntityStabilityProjector> {

	private static final ResourceLocation lodestone_sliver = new ResourceLocation(Etheric.MODID, "items/lodestone_sliver");
	
	@Override
	public void render(TileEntityStabilityProjector te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) {

		float[][] displayVals = te.getDisplays();
		
		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();

		GlStateManager.translate((x + 0.125F), (y + 0.921875F), (z + 0.125F));

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		TextureAtlasSprite sprite = ModelLoader.defaultTextureGetter().apply(lodestone_sliver);
		float tile = (1.5F / 16);
		int i = getWorld().getCombinedLight(te.getPos(), 0);
		
		int a = getWorld().getLightFromNeighborsFor(EnumSkyBlock.SKY, te.getPos());
		int b = getWorld().getLightFromNeighborsFor(EnumSkyBlock.BLOCK, te.getPos());
		i = a << 20 | b << 4;
		int lightx = (int) (((i >> 0x10) & 0xFFFF) * 1F);
		int lighty = i & 0xFFFF;

		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		for (int chunkZ = 0; chunkZ < displayVals.length; chunkZ++) {
			for (int chunkX = 0; chunkX < displayVals[chunkZ].length; chunkX++) {
				float yOffset = (0.25F * displayVals[chunkZ][chunkX]);
				// yOffset = 0.125F;
				if (te.display()) {
					int xz = Math.abs(chunkX - 3) + Math.abs(chunkZ - 3);
					int offset = (xz * xz);
					float amp = 0.01F;
					yOffset += amp + (amp * MathHelper.sin((te.getTicks() - offset) * 0.05F));
				}
				
				addBitToBuffer(buffer, tile + (chunkX * tile), yOffset, tile + (chunkZ * tile), sprite, lightx, lighty);
			}
		}
		tessellator.draw();

		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

	}

	private void addBitToBuffer(BufferBuilder buffer, float x, float y, float z, TextureAtlasSprite sprite, int lightx, int lighty) {
		float x1 = x - (1F / 32);
		float y1 = y - (1.5F / 32);
		float z1 = z - (1F / 32);
		float x2 = x + (1F / 32);
		float y2 = y + (1.5F / 32);
		float z2 = z + (1F / 32);
		
		double diffU = sprite.getMaxU() - sprite.getMinU();
		double diffV = sprite.getMaxV() - sprite.getMinV();
		double minU = sprite.getMinU() + diffU * 0;
		double maxU = sprite.getMaxU() - diffU * (1 - 0.125);
		double minV = sprite.getMinV() + diffV * 0.125;
		double maxV = sprite.getMaxV() - diffV * (1 - 0.3125);

		// north
		buffer.pos(x2, y2, z1).tex(minU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x2, y1, z1).tex(minU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x1, y1, z1).tex(maxU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x1, y2, z1).tex(maxU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		// south
		buffer.pos(x1, y2, z2).tex(minU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x1, y1, z2).tex(minU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x2, y1, z2).tex(maxU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x2, y2, z2).tex(maxU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		// west
		buffer.pos(x1, y2, z1).tex(minU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x1, y1, z1).tex(minU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x1, y1, z2).tex(maxU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x1, y2, z2).tex(maxU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		// east
		buffer.pos(x2, y2, z2).tex(minU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x2, y1, z2).tex(minU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x2, y1, z1).tex(maxU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x2, y2, z1).tex(maxU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		// down
		minV = sprite.getMinV() + diffV * 0.3125;
		maxV = sprite.getMaxV() - diffV * (1 - 0.4375);
		buffer.pos(x2, y1, z1).tex(maxU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x2, y1, z2).tex(maxU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x1, y1, z2).tex(minU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x1, y1, z1).tex(minU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		// up
		minV = sprite.getMinV() + diffV * 0;
		maxV = sprite.getMaxV() - diffV * (1 - 0.125);
		buffer.pos(x1, y2, z1).tex(minU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x1, y2, z2).tex(minU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x2, y2, z2).tex(maxU, maxV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
		buffer.pos(x2, y2, z1).tex(maxU, minV).lightmap(lightx, lighty).color(1F, 1F, 1F, 1F).endVertex();
	}

}
