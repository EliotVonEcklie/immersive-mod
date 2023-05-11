package moe.eliotvonecklie.immersive.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NuclearFissionExplosive extends Block {
	public NuclearFissionExplosive(Settings settings) {
		super(settings);
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
	
	private void primeTnt(World world, BlockPos pos, PlayerEntity player) {
		TntEntity tntEntity = new TntEntity(world, pos.getX() - 0.5, pos.getY(), pos.getZ() - 0.5, player);
		
		for (int i = 0; i < 255; i++) {
			TntMinecartEntity tntMinecartEntity = new TntMinecartEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			world.spawnEntity(tntMinecartEntity);
			if (world instanceof ClientWorld clientWorld)
				clientWorld.tickEntity(tntMinecartEntity);
		}
			
		world.spawnEntity(tntEntity);
	}
}
