package com.sindercube.mobSoup.client.content.entity.renderer;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.client.content.entity.model.CenturionCapeModel;
import com.sindercube.mobSoup.client.content.entity.model.CenturionModel;
import com.sindercube.mobSoup.client.content.entity.state.CenturionState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentModelLoader;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.ItemStack;

public class CenturionCapeFeatureRenderer extends FeatureRenderer<CenturionState, CenturionModel> {

	private final BipedEntityModel<CenturionState> model;
	private final EquipmentModelLoader equipmentModelLoader;

	public CenturionCapeFeatureRenderer(FeatureRendererContext<CenturionState, CenturionModel> context, LoadedEntityModels modelLoader, EquipmentModelLoader equipmentModelLoader) {
		super(context);
		this.model = new CenturionCapeModel(modelLoader.getModelPart(EntityModelLayers.PLAYER_CAPE));
		this.equipmentModelLoader = equipmentModelLoader;
	}

	private boolean hasCustomModelForLayer(ItemStack stack) {
		EquippableComponent equippableComponent = stack.get(DataComponentTypes.EQUIPPABLE);
		if (equippableComponent != null && equippableComponent.assetId().isPresent()) {
			EquipmentModel equipmentModel = this.equipmentModelLoader.get(equippableComponent.assetId().get());
			return !equipmentModel.getLayers(EquipmentModel.LayerType.HUMANOID).isEmpty();
		} else {
			return false;
		}
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertices, int light, CenturionState state, float limbAngle, float limbDistance) {
		if (state.invisible) return;
		matrices.push();
		if (this.hasCustomModelForLayer(state.equippedChestStack)) matrices.translate(0.0F, -0.053125F, 0.06875F);
		VertexConsumer vertexConsumer = vertices.getBuffer(RenderLayer.getEntitySolid(MobSoup.of("capes/centurion.json")));
		this.getContextModel().copyTransforms(this.model);
		this.model.setAngles(state);
		this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}

}
