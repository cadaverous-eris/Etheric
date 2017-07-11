package etheric.client.renderer.entity;

import etheric.Etheric;
import etheric.client.model.entity.ModelLesserCelestial;
import etheric.common.entity.mob.EntityLesserCelestial;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLesserCelestial extends RenderBiped<EntityLesserCelestial> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Etheric.MODID, "textures/mobs/lesser_celestial.png");

	public RenderLesserCelestial(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelLesserCelestial(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLesserCelestial entity) {
		return TEXTURE;
	}
	
	public static class Factory implements IRenderFactory<EntityLesserCelestial> {

		@Override
		public Render<EntityLesserCelestial> createRenderFor(RenderManager manager) {
			return new RenderLesserCelestial(manager);
		}
		
	}

}
