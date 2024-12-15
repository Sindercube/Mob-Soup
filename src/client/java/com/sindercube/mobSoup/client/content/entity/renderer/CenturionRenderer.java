package com.sindercube.mobSoup.client.content.entity.renderer;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.client.content.entity.model.CenturionModel;
import com.sindercube.mobSoup.client.content.entity.state.CenturionState;
import com.sindercube.mobSoup.content.entity.CenturionEntity;
import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CenturionRenderer extends BipedEntityRenderer<CenturionEntity, CenturionState, CenturionModel> {

	public static final Identifier DEFAULT_TEXTURE = MobSoup.of("textures/entity/centurion/default.png");
	public static final Identifier ENRAGED_TEXTURE = MobSoup.of("textures/entity/centurion/enraged.png");

	public CenturionRenderer(EntityRendererFactory.Context context, EntityModelLayer layer, EntityModelLayer armorInnerLayer, EntityModelLayer armorOuterLayer) {
		super(context, new CenturionModel(context.getPart(layer)), 0.5F);
		this.addFeature(new ArmorFeatureRenderer(this, new SkeletonEntityModel(context.getPart(armorInnerLayer)), new SkeletonEntityModel(context.getPart(armorOuterLayer)), context.getEquipmentRenderer()));
		this.addFeature(new CenturionCapeFeatureRenderer(this, context.getEntityModels(), context.getEquipmentModelLoader()));
	}

	@Override
	public void updateRenderState(CenturionEntity entity, CenturionState state, float f) {
		super.updateRenderState(entity, state, f);
		updateCape(entity, state, f);
		state.enraged = entity.isEnraged();
	}

	private static void updateCape(CenturionEntity player, CenturionState state, float tickDelta) {
		double d = MathHelper.lerp(tickDelta, player.prevCapeX, player.capeX) - MathHelper.lerp(tickDelta, player.prevX, player.getX());
		double e = MathHelper.lerp(tickDelta, player.prevCapeY, player.capeY) - MathHelper.lerp(tickDelta, player.prevY, player.getY());
		double f = MathHelper.lerp(tickDelta, player.prevCapeZ, player.capeZ) - MathHelper.lerp(tickDelta, player.prevZ, player.getZ());
		float g = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
		double h = MathHelper.sin(g * 0.017453292F);
		double i = -MathHelper.cos(g * 0.017453292F);
		state.capeFlap = (float)e * 10.0F;
		state.capeFlap = MathHelper.clamp(state.capeFlap, -6.0F, 32.0F);
		state.capeLean = (float)(d * h + f * i) * 100.0F;
		state.capeLean = MathHelper.clamp(state.capeLean, 0.0F, 150.0F);
		state.capeLean2 = (float)(d * i - f * h) * 100.0F;
		state.capeLean2 = MathHelper.clamp(state.capeLean2, -20.0F, 20.0F);
		float j = MathHelper.lerp(tickDelta, player.prevStrideDistance, player.strideDistance);
		state.capeFlap += MathHelper.sin(6.0F) * 32.0F * j;
	}

	@Override
	protected BipedEntityModel.ArmPose getArmPose(CenturionEntity entity, Arm arm) {
		ItemStack stack = entity.getStackInArm(arm);
		return entity.getMainArm() == arm && entity.isAttacking() && stack.isOf(ModItems.JAVELIN) ? BipedEntityModel.ArmPose.THROW_SPEAR : BipedEntityModel.ArmPose.EMPTY;
	}

	@Override
	public CenturionState createRenderState() {
		return new CenturionState();
	}

	@Override
	public Identifier getTexture(CenturionState state) {
		return state.enraged ? ENRAGED_TEXTURE : DEFAULT_TEXTURE;
	}

}
