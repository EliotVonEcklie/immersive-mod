package moe.eliotvonecklie.immersive.items;

import java.util.function.Predicate;

import moe.eliotvonecklie.immersive.registry.ItemRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Glock extends RangedWeaponItem {
	public static final Predicate<ItemStack> GLOCK_PROJECTILES = stack -> stack.isOf(ItemRegistry.BULLET_9MM);
	public static final int MAX_AMMO = 9;
	public static final int RANGE = 30;

	public Glock(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
		ItemStack stack = playerEntity.getStackInHand(hand);
		boolean isInfinite = playerEntity.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
		ItemStack itemStack = playerEntity.getArrowType(stack);
		if (!itemStack.isEmpty() || isInfinite) {
			if (itemStack.isEmpty()) {
				itemStack = new ItemStack(ItemRegistry.BULLET_9MM);
			}

			boolean bl2 = isInfinite && itemStack.isOf(ItemRegistry.BULLET_9MM);
			
			if (!world.isClient) {
				ArrowItem arrowItem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
				PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack, playerEntity);
				persistentProjectileEntity.setProperties(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 3.0F, 1.0F);
				persistentProjectileEntity.setCritical(true);

				int j = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
				if (j > 0) {
					persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + (double)j * 0.5 + 0.5);
				}

				int k = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
				if (k > 0) {
					persistentProjectileEntity.setPunch(k);
				}

				if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) {
					persistentProjectileEntity.setOnFireFor(100);
				}

				stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(playerEntity.getActiveHand()));
				if (bl2 || playerEntity.getAbilities().creativeMode) {
					persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
				}

				world.spawnEntity(persistentProjectileEntity);
			}

			world.playSound(
				null,
				playerEntity.getX(),
				playerEntity.getY(),
				playerEntity.getZ(),
				SoundEvents.ENTITY_ARROW_SHOOT,
				SoundCategory.PLAYERS,
				1.0F,
				1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F
			);
			
			if (!bl2 && !playerEntity.getAbilities().creativeMode) {
				itemStack.decrement(1);
				if (itemStack.isEmpty()) {
					playerEntity.getInventory().removeOne(itemStack);
				}
				
			}

			playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
			
			return TypedActionResult.success(stack);
		}

		return TypedActionResult.fail(stack);
	}

	@Override
	public Predicate<ItemStack> getProjectiles() {
		//return GLOCK_PROJECTILES;
		return BOW_PROJECTILES;
	}

	@Override
	public int getRange() {
		return RANGE;
	}
}
