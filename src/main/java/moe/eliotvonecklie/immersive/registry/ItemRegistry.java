package moe.eliotvonecklie.immersive.registry;

import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import moe.eliotvonecklie.immersive.ImmersiveMod;
import moe.eliotvonecklie.immersive.items.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ItemRegistry {
	public static final Item GLOCK = new Glock(new QuiltItemSettings());
	public static final Item BULLET_9MM = new Item(new QuiltItemSettings());
	public static final Item MOON_ROCK = new Item(new QuiltItemSettings().maxCount(16));
	public static final Item POWERED_MOON_ROCK = new Item(new QuiltItemSettings().rarity(Rarity.RARE).maxCount(16).fireproof());
	
	private static void registerItem(String id, Item item) {
		Registry.register(Registries.ITEM, new Identifier(ImmersiveMod.MODID, id), item);
	}
	
	public static void register() {
		registerItem("glock", GLOCK);
		registerItem("bullet_9mm", BULLET_9MM);
		registerItem("moon_rock", MOON_ROCK);
		registerItem("powered_moon_rock", POWERED_MOON_ROCK);
	}
}
