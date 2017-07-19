package etheric.client.model.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import etheric.Etheric;
import etheric.RegistryManager;
import etheric.client.util.RenderUtil;
import etheric.client.util.QuintessenceRenderUtil;
import etheric.common.block.BlockPipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

public class PipeModel implements IModel {

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		IBakedModel[] connections = new IBakedModel[6];
		IBakedModel[] endings = new IBakedModel[6];
		IBakedModel[] nodeSides = new IBakedModel[6];
		//IBakedModel node = null;

		// d u n s w e
		ModelRotation[] rotations = new ModelRotation[] { ModelRotation.X90_Y0, ModelRotation.X270_Y0,
				ModelRotation.X0_Y0, ModelRotation.X0_Y180, ModelRotation.X0_Y270, ModelRotation.X0_Y90 };

		try {
			IModel nodeSideModel = ModelLoaderRegistry.getModel(new ResourceLocation(Etheric.MODID, "block/pipe_node_side"));
			IModel connectionModel = ModelLoaderRegistry
					.getModel(new ResourceLocation(Etheric.MODID, "block/pipe_connection"));
			IModel endingModel = ModelLoaderRegistry.getModel(new ResourceLocation(Etheric.MODID, "block/pipe_end"));

			//node = nodeModel.bake(new TRSRTransformation(ModelRotation.X0_Y0), DefaultVertexFormats.BLOCK,
			//		ModelLoader.defaultTextureGetter());
			for (int i = 0; i < connections.length; i++) {
				connections[i] = connectionModel.bake(new TRSRTransformation(rotations[i]), DefaultVertexFormats.BLOCK,
						ModelLoader.defaultTextureGetter());
				endings[i] = endingModel.bake(new TRSRTransformation(rotations[i]), DefaultVertexFormats.BLOCK,
						ModelLoader.defaultTextureGetter());
				nodeSides[i] = nodeSideModel.bake(new TRSRTransformation(rotations[i]), DefaultVertexFormats.BLOCK,
						ModelLoader.defaultTextureGetter());
			}
		} catch (Exception e) {
			Etheric.logger.warn(e.getMessage());
		}

		if (connections[0] == null) {
			return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
		}
		return new BakedPipeModel(nodeSides, connections, endings);
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of(new ResourceLocation(Etheric.MODID, "block/pipe_node_side"),
				new ResourceLocation(Etheric.MODID, "block/pipe_connection"),
				new ResourceLocation(Etheric.MODID, "block/pipe_ending"));
	}

	public static class PipeModelLoader implements ICustomModelLoader {

		private static PipeModel model;

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager) {
			model = new PipeModel();
		}

		@Override
		public boolean accepts(ResourceLocation modelLocation) {
			return modelLocation.getResourceDomain().equals(Etheric.MODID)
					&& modelLocation.getResourcePath().equals("pipe");
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception {
			if (model == null) {
				model = new PipeModel();
			}
			return model;
		}

	}

	public static class BakedPipeModel implements IBakedModel {

		private IBakedModel[] connections;
		private IBakedModel[] endings;
		private IBakedModel[] nodeSides;
		//private IBakedModel node;

		public BakedPipeModel(IBakedModel[] nodeSides, IBakedModel[] connections, IBakedModel[] endings) {
			this.nodeSides = nodeSides;
			this.connections = connections;
			this.endings = endings;
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
			if (state instanceof IExtendedBlockState) {
				List<IBakedModel> subModels = getSubModels((IExtendedBlockState) state);
				List<BakedQuad> quads = new ArrayList<BakedQuad>();

				for (IBakedModel model : subModels) {
					quads.addAll(model.getQuads(state, side, rand));
				}

				return quads;
			}
			return Collections.EMPTY_LIST;
		}

		private List<IBakedModel> getSubModels(IExtendedBlockState state) {
			List<IBakedModel> subModels = new ArrayList<IBakedModel>();

			for (int i = 0; i < 6; i++) {
				if (state.getValue(BlockPipe.CONNECTIONS[i]) > 0) {
					subModels.add(connections[i]);
				} else {
					subModels.add(nodeSides[i]);
				}
				if (state.getValue(BlockPipe.CONNECTIONS[i]) > 1) {
					subModels.add(endings[i]);
				}
			}
			
			return subModels;
		}

		@Override
		public boolean isAmbientOcclusion() {
			return true;
		}

		@Override
		public boolean isGui3d() {
			return true;
		}

		@Override
		public boolean isBuiltInRenderer() {
			return false;
		}

		@Override
		public TextureAtlasSprite getParticleTexture() {
			return nodeSides[0].getParticleTexture();
		}

		@Override
		public ItemOverrideList getOverrides() {
			return ItemOverrideList.NONE;
		}

	}

}
