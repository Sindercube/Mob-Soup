package com.sindercube.mobSoup.client.content.entity.renderer;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.client.content.entity.state.JavelinState;
import com.sindercube.mobSoup.content.entity.JavelinEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class JavelinRenderer extends EntityRenderer<JavelinEntity, JavelinState> {

	public static final Identifier TEXTURE = MobSoup.of("textures/entity/javelin.png");
	public TridentEntityModel model;

	public JavelinRenderer(EntityRendererFactory.Context context, EntityModelLayer layer) {
		super(context);
		this.model = new TridentEntityModel(context.getPart(layer));
	}

	public void render(JavelinState tridentEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(tridentEntityRenderState.yaw - 90.0F));
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(tridentEntityRenderState.pitch + 90.0F));
		VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(TEXTURE), false, tridentEntityRenderState.enchanted);
		this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
		matrixStack.pop();
		super.render(tridentEntityRenderState, matrixStack, vertexConsumerProvider, i);
	}

	public JavelinState createRenderState() {
		return new JavelinState();
	}

	public void updateRenderState(JavelinEntity entity, JavelinState state, float f) {
		super.updateRenderState(entity, state, f);
		state.yaw = entity.getLerpedYaw(f);
		state.pitch = entity.getLerpedPitch(f);
		state.enchanted = entity.isEnchanted();
	}

}
