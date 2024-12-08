package com.sindercube.mobSoup.client.content.entity.model;

import com.sindercube.mobSoup.client.content.entity.state.CenturionState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import org.joml.Quaternionf;

public class CenturionCapeModel extends BipedEntityModel<CenturionState> {

	private final ModelPart cape;

	public CenturionCapeModel(ModelPart modelPart) {
		super(modelPart);
		this.cape = this.body.getChild("cape");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0F);
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData modelPartData2 = modelPartData.addChild("head");
		modelPartData2.addChild("hat");
		ModelPartData modelPartData3 = modelPartData.addChild("body");
		modelPartData.addChild("left_arm");
		modelPartData.addChild("right_arm");
		modelPartData.addChild("left_leg");
		modelPartData.addChild("right_leg");
		modelPartData3.addChild("cape", ModelPartBuilder.create().uv(0, 0).cuboid(-5.0F, 0.0F, -1.0F, 10.0F, 16.0F, 1.0F, Dilation.NONE, 1.0F, 0.5F), ModelTransform.of(0.0F, 0.0F, 2.0F, 0.0F, 3.1415927F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}

	public void setAngles(CenturionState state) {
		super.setAngles(state);
		this.cape.rotate(
			new Quaternionf()
				// field_53537 == capeLean
				// field_53536 == capeFlap
				// field_53538 == capeLean2
				.rotateY((float)-Math.PI)
				.rotateX((6.0F + state.capeLean / 2.0F + state.capeFlap) * (float) (Math.PI / 180.0))
				.rotateZ(state.capeLean2 / 2.0F * (float) (Math.PI / 180.0))
				.rotateY((180.0F - state.capeLean2 / 2.0F) * (float) (Math.PI / 180.0))
		);
	}

}
