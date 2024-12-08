package com.sindercube.mobSoup.client.content.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

public class ConfusedParticle extends AnimatedParticle {

	protected ConfusedParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
		super(world, x, y, z, spriteProvider, 0);
		this.scale = 0.5F;
		this.maxAge = 80;
		this.setSpriteForAge(spriteProvider);
	}

	@Override
	public void tick() {
		super.tick();
		this.setAlpha(1);
	}

	public static class Factory implements ParticleFactory<SimpleParticleType> {

		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double x, double y, double z, double g, double h, double i) {
			return new ConfusedParticle(clientWorld, x, y, z, this.spriteProvider);
		}

	}

}
