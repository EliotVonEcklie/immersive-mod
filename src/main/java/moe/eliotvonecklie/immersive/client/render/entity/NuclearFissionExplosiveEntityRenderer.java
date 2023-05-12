package moe.eliotvonecklie.immersive.client.render.entity;

import moe.eliotvonecklie.immersive.entities.NuclearFissionExplosiveEntity;
import moe.eliotvonecklie.immersive.registry.BlockRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;

public class NuclearFissionExplosiveEntityRenderer extends EntityRenderer<NuclearFissionExplosiveEntity> {
	private final BlockRenderManager blockRenderManager;

	public NuclearFissionExplosiveEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
		this.blockRenderManager = context.getBlockRenderManager();
	}

	public void render(NuclearFissionExplosiveEntity nuclearFissionExplosiveEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		matrixStack.translate(0.0F, 0.5F, 0.0F);
		int j = nuclearFissionExplosiveEntity.getFuse();
		if ((float)j - g + 1.0F < 10.0F) {
			float h = 1.0F - ((float)j - g + 1.0F) / 10.0F;
			h = MathHelper.clamp(h, 0.0F, 1.0F);
			h *= h;
			h *= h;
			float k = 1.0F + h * 0.3F;
			matrixStack.scale(k, k, k);
		}

		matrixStack.multiply(Axis.Y_POSITIVE.rotationDegrees(-90.0F));
		matrixStack.translate(-0.5F, -0.5F, 0.5F);
		matrixStack.multiply(Axis.Y_POSITIVE.rotationDegrees(90.0F));
		TntMinecartEntityRenderer.renderFlashingBlock(this.blockRenderManager, BlockRegistry.NUCLEAR_FISSION_EXPLOSIVE.getDefaultState(), matrixStack, vertexConsumerProvider, i, j / 5 % 2 == 0);
		matrixStack.pop();
		super.render(nuclearFissionExplosiveEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	public Identifier getTexture(NuclearFissionExplosiveEntity tntEntity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
}
