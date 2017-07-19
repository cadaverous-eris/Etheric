package etheric.client.renderer.tileentity;

import com.google.common.primitives.Ints;

import etheric.Etheric;
import etheric.client.util.QuintessenceRenderUtil;
import etheric.client.util.RenderUtil;
import etheric.common.tileentity.TileEntityPipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.common.property.IExtendedBlockState;

public class TileEntityPipeRenderer extends FastTESR<TileEntityPipe> {
	
	public TileEntityPipeRenderer() {
		super();
	}

	@Override
	public void renderTileEntityFast(TileEntityPipe te, double x, double y, double z, float partialTicks,
			int destroyStage, float partial, net.minecraft.client.renderer.BufferBuilder buffer) {
		BlockPos pos = te.getPos();
		int amount = te.getAmount();
		float purity = te.getPurity();
		TextureAtlasSprite sprite = ModelLoader.defaultTextureGetter().apply(QuintessenceRenderUtil.flow_texture);
		
		int red = QuintessenceRenderUtil.getRed(purity);
		int green = QuintessenceRenderUtil.getGreen(purity);
		int blue = QuintessenceRenderUtil.getBlue(purity);
		
		if (amount > 0) {
			buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
			
			RenderUtil.addCuboid(buffer, pos, 0.40625F, 0.40625F, 0.40625F, 0.59375F, 0.40625F + (amount * 0.046875F), 0.59375F, sprite, red, green, blue, 255);
			if (te.getQuintConnection(EnumFacing.DOWN)) {
				RenderUtil.addCuboid(buffer, pos, 0.5F - (amount * 0.0234375F), 0.0F, 0.5F - (amount * 0.0234375F), 0.5F + (amount * 0.0234375F), 0.40625F, 0.5F + (amount * 0.0234375F), sprite, red, green, blue, 255);
			}
			if (te.getQuintConnection(EnumFacing.UP)) {
				RenderUtil.addCuboid(buffer, pos, 0.5F - (amount * 0.0234375F), 0.40625F + (amount * 0.046875F), 0.5F - (amount * 0.0234375F), 0.5F + (amount * 0.0234375F), 1.0F, 0.5F + (amount * 0.0234375F), sprite, red, green, blue, 255);
			}
			if (te.getQuintConnection(EnumFacing.NORTH)) {
				RenderUtil.addCuboid(buffer, pos, 0.40625F, 0.40625F, 0.0F, 0.59375F, 0.40625F + (amount * 0.046875F), 0.40625F, sprite, red, green, blue, 255);
			}
			if (te.getQuintConnection(EnumFacing.SOUTH)) {
				RenderUtil.addCuboid(buffer, pos, 0.40625F, 0.40625F, 0.5625F, 0.59375F, 0.40625F + (amount * 0.046875F), 1.0F, sprite, red, green, blue, 255);
			}
			if (te.getQuintConnection(EnumFacing.WEST)) {
				RenderUtil.addCuboid(buffer, pos, 0.0F, 0.40625F, 0.40625F, 0.4375F, 0.40625F + (amount * 0.046875F), 0.59375F, sprite, red, green, blue, 255);
			}
			if (te.getQuintConnection(EnumFacing.EAST)) {
				RenderUtil.addCuboid(buffer, pos, 0.59375F, 0.40625F, 0.40625F, 1.0F, 0.40625F + (amount * 0.046875F), 0.59375F, sprite, red, green, blue, 255);
			}
		}
	}

}
