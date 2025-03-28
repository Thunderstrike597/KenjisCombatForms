package net.kenji.kenjiscombatforms.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class VoidRiftParticle extends TextureSheetParticle {
    public VoidRiftParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
        super(level, x, y, z, dx, dy, dz);
        this.lifetime = 100; // Particle lifespan in ticks
        this.hasPhysics = false; // Disable gravity
        this.friction = 0.95F;
        this.xd = dx;
        this.yd = dy;
        this.zd = dz;
        this.quadSize = 0.1F;
    }


    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType>{
        private final SpriteSet sprites;
        public Provider(SpriteSet sprites){
            this.sprites = sprites;
        }


        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            VoidRiftParticle particle = new VoidRiftParticle(level, x, y, z, dx, dy, dz);
            particle.pickSprite(this.sprites);
            return particle;
        }
    }


    public float getQuadSize(float p_107608_) {
        float f = 1.0F - ((float)this.age + p_107608_) / ((float)this.lifetime * 1.5F);
        return this.quadSize * f;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            float f = (float)this.age / (float)this.lifetime;
            this.x += this.xd * (double)f;
            this.y += this.yd * (double)f;
            this.z += this.zd * (double)f;
            this.setPos(this.x, this.y, this.z);
        }

    }
}
