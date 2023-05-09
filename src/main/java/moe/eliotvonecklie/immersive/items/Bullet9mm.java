package moe.eliotvonecklie.immersive.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Bullet9mm extends ArrowItem {
	public Bullet9mm(Settings settings) {
		super(settings);
	}

	@Override
	public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
		ArrowEntity arrowEntity = new ArrowEntity(world, shooter);
		arrowEntity.initFromStack(stack);
		return arrowEntity;
	}
}
