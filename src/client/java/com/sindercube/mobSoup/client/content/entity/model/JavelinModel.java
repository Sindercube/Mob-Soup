package com.sindercube.mobSoup.client.content.entity.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;

public class JavelinModel extends Model {

	public JavelinModel(ModelPart root) {
		super(root, RenderLayer::getEntityCutout);
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, -6).cuboid(0.0F, -19.5F, -3.0F, 0.0F, 32.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 11.5F, 0.0F, 0.0F, -0.7854F, 0.0F));
		body.addChild("body_r1", ModelPartBuilder.create().uv(0, -6).cuboid(0.0F, -16.0F, -3.0F, 0.0F, 32.0F, 6.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -3.5F, 0.0F, 0.0F, 1.5708F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}

}
