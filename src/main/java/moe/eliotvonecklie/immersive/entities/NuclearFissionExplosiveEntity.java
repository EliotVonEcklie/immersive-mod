package moe.eliotvonecklie.immersive.entities;

import org.jetbrains.annotations.Nullable;

import moe.eliotvonecklie.immersive.registry.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class NuclearFissionExplosiveEntity extends TntEntity {
	private static final TrackedData<Integer> FUSE = DataTracker.registerData(NuclearFissionExplosiveEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final int DEFAULT_FUSE = 160;
	@Nullable
	private LivingEntity causingEntity;

	public NuclearFissionExplosiveEntity(EntityType<? extends NuclearFissionExplosiveEntity> variant, World world) {
		super(variant, world);
		this.inanimate = true;
	}
	
	public NuclearFissionExplosiveEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
		this(EntityRegistry.NUCLEAR_FISSION_EXPLOSIVE_ENTITY, world);
		this.setPosition(x, y, z);
		double d = world.random.nextDouble() * (float) (Math.PI * 2);
		this.setVelocity(-Math.sin(d) * 0.02, 0.2F, -Math.cos(d) * 0.02);
		this.setFuse(DEFAULT_FUSE);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
		this.causingEntity = igniter;
	}

	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(FUSE, DEFAULT_FUSE);
	}

	@Override
	protected Entity.MoveEffect getMoveEffect() {
		return Entity.MoveEffect.NONE;
	}

	@Override
	public boolean collides() {
		return !this.isRemoved();
	}

	@Override
	public void tick() {
		if (!this.hasNoGravity()) {
			this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
		}

		this.move(MovementType.SELF, this.getVelocity());
		this.setVelocity(this.getVelocity().multiply(0.98));
		if (this.onGround) {
			this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
		}

		int i = this.getFuse() - 1;
		this.setFuse(i);
		if (i <= 0) {
			this.discard();
			if (!this.world.isClient) {
				this.explode();
			}
		} else {
			this.updateWaterState();
			if (this.world.isClient) {
				this.world.addParticle(ParticleTypes.SMOKE, this.getX() + 5, this.getY() + 0.5, this.getZ() + 5, 0.0, 0.0, 0.0);
				this.world.addParticle(ParticleTypes.SMOKE, this.getX() + 5, this.getY() + 0.5, this.getZ() - 5, 0.0, 0.0, 0.0);
				this.world.addParticle(ParticleTypes.SMOKE, this.getX() - 5, this.getY() + 0.5, this.getZ() + 5, 0.0, 0.0, 0.0);
				this.world.addParticle(ParticleTypes.SMOKE, this.getX() - 5, this.getY() + 0.5, this.getZ() - 5, 0.0, 0.0, 0.0);
			}
		}
	}

	private void explode() {
		if (!world.isClient) {
			world.createExplosion(this, this.getX() + 5, this.getBodyY(0.0625), this.getZ() + 5, 128F, World.ExplosionSourceType.BLOCK);
			world.createExplosion(this, this.getX() + 5, this.getBodyY(0.0625), this.getZ() - 5, 128F, World.ExplosionSourceType.BLOCK);
			world.createExplosion(this, this.getX() - 5, this.getBodyY(0.0625), this.getZ() + 5, 128F, World.ExplosionSourceType.BLOCK);
			world.createExplosion(this, this.getX() - 5, this.getBodyY(0.0625), this.getZ() - 5, 128F, World.ExplosionSourceType.BLOCK);
		}
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		nbt.putShort("Fuse", (short)this.getFuse());
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		this.setFuse(nbt.getShort("Fuse"));
	}

	@Nullable
	public LivingEntity getOwner() {
		return this.causingEntity;
	}

	@Override
	protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.15F;
	}

	public void setFuse(int fuse) {
		this.dataTracker.set(FUSE, fuse);
	}

	public int getFuse() {
		return this.dataTracker.get(FUSE);
	}
}
