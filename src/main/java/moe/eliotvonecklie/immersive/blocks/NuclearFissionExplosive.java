package moe.eliotvonecklie.immersive.blocks;

import org.jetbrains.annotations.Nullable;
import moe.eliotvonecklie.immersive.entities.NuclearFissionExplosiveEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;

public class NuclearFissionExplosive extends Block {
	public NuclearFissionExplosive(Settings settings) {
		super(settings);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (!oldState.isOf(state.getBlock())) {
			if (world.isReceivingRedstonePower(pos)) {
				primeTnt(world, pos);
				world.removeBlock(pos, false);
			}
		}
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (world.isReceivingRedstonePower(pos)) {
			primeTnt(world, pos);
			world.removeBlock(pos, false);
		}
	}
	
	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient() && !player.isCreative()) {
			primeTnt(world, pos);
		}

		super.onBreak(world, pos, state, player);
	}
	
	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		if (!world.isClient) {
			TntEntity tntEntity = new TntEntity(world, (double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, explosion.getCausingEntity());
			int i = tntEntity.getFuse();
			tntEntity.setFuse((short)(world.random.nextInt(i / 4) + i / 8));
			world.spawnEntity(tntEntity);
		}
	}
	
	public static void primeTnt(World world, BlockPos pos) {
		primeTnt(world, pos, null);
	}

	private static void primeTnt(World world, BlockPos pos, @Nullable LivingEntity igniter) {
		if (!world.isClient) {
			//NuclearFissionExplosiveEntity tntEntity = new NuclearFissionExplosiveEntity(world, (double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, igniter);
			//world.spawnEntity(tntEntity);
			//world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.emitGameEvent(igniter, GameEvent.PRIME_FUSE, pos);
			explode(world, pos);
		}
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {		
		ItemStack itemStack = player.getStackInHand(hand);
		if (!itemStack.isOf(Items.FLINT_AND_STEEL) && !itemStack.isOf(Items.FIRE_CHARGE)) {
			return ActionResult.PASS;
		} else {
			primeTnt(world, pos, player);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
			Item item = itemStack.getItem();
			if (!player.isCreative()) {
				if (itemStack.isOf(Items.FLINT_AND_STEEL)) {
					itemStack.damage(1, player, playerx -> playerx.sendToolBreakStatus(hand));
				} else {
					itemStack.decrement(1);
				}
			}

			player.incrementStat(Stats.USED.getOrCreateStat(item));
			return ActionResult.success(world.isClient);
		}
	}
	
	@Override
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		if (!world.isClient) {
			BlockPos blockPos = hit.getBlockPos();
			Entity entity = projectile.getOwner();
			if (projectile.isOnFire() && projectile.canModifyAt(world, blockPos)) {
				primeTnt(world, blockPos, entity instanceof LivingEntity ? (LivingEntity)entity : null);
				world.removeBlock(blockPos, false);
			}
		}
	}
	
	@Override
	public boolean shouldDropItemsOnExplosion(Explosion explosion) {
		return false;
	}
	
	// TEMPORARY FIX
	private static void explode(World world, BlockPos pos) {
		if (!world.isClient) {
			world.createExplosion(null, (double)pos.getX() + 5, (double)pos.getY() * 0.0625D, (double)pos.getZ() + 5, 128F, World.ExplosionSourceType.BLOCK);
			world.createExplosion(null, (double)pos.getX() + 5, (double)pos.getY() * 0.0625D, (double)pos.getZ() - 5, 128F, World.ExplosionSourceType.BLOCK);
			world.createExplosion(null, (double)pos.getX() - 5, (double)pos.getY() * 0.0625D, (double)pos.getZ() + 5, 128F, World.ExplosionSourceType.BLOCK);
			world.createExplosion(null, (double)pos.getX() - 5, (double)pos.getY() * 0.0625D, (double)pos.getZ() - 5, 128F, World.ExplosionSourceType.BLOCK);
		}
	}
}
