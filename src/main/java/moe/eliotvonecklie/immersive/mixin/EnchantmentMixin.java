package moe.eliotvonecklie.immersive.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import moe.eliotvonecklie.immersive.items.Glock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Redirect(method = "isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentTarget;isAcceptableItem(Lnet/minecraft/item/Item;)Z"))
	private boolean addGlockEnchantments(EnchantmentTarget target, Item item) {
		if (item instanceof Glock && (target == EnchantmentTarget.BOW || target == EnchantmentTarget.CROSSBOW)) {
			return true;
		} else {
			return target.isAcceptableItem(item);
		}
	}
}
