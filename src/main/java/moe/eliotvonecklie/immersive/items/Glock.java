package moe.eliotvonecklie.immersive.items;

import java.util.function.Predicate;
import org.joml.Quaternionf;
import org.joml.Vector3f;
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
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
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
				
				int l = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, stack);
				int l2 = l == 0 ? 1 : 3;
				
				for (int l3 = 0; l3 < l2; l3++) {
					PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack, playerEntity);
					
					persistentProjectileEntity.setProperties(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0F, 3.0F, 1.0F);
					persistentProjectileEntity.setCritical(true);
					
					int i = EnchantmentHelper.getLevel(Enchantments.PIERCING, stack);
					if (i > 0) {
						persistentProjectileEntity.setPierceLevel((byte)i);
					}

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
					
					Vec3d vec3d = playerEntity.getOppositeRotationVector(1.0F);
					float simulated = switch(l3) {
						default -> 0F;
						case 1 -> -10F;
						case 2 -> 10F;
					};
					
					Quaternionf quaternionf = new Quaternionf().setAngleAxis((double)(simulated * (float) (Math.PI / 180.0)), vec3d.x, vec3d.y, vec3d.z);
					Vec3d vec3d2 = playerEntity.getRotationVec(1.0F);
					Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
					
					persistentProjectileEntity.setVelocity((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), 1.6F, 1.F);

					world.spawnEntity(persistentProjectileEntity);
				}
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
			
			return TypedActionResult.consume(stack);
		}

		return TypedActionResult.fail(stack);
	}
	
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
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
