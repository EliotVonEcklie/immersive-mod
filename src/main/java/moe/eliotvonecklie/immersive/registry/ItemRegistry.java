package moe.eliotvonecklie.immersive.registry;

import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import moe.eliotvonecklie.immersive.ImmersiveMod;
import moe.eliotvonecklie.immersive.items.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ItemRegistry {
	//public static final Item FUEL_PUMP = new Item(new QuiltItemSettings());
	public static final Item GLOCK = new Glock(new QuiltItemSettings());
	public static final Item BULLET_9MM = new Item(new QuiltItemSettings());
	
	private static void registerItem(String id, Item item) {
		Registry.register(Registries.ITEM, new Identifier(ImmersiveMod.MODID, id), item);
	}
	
	public static void register() {
		//registerItem("fuel_pump", FUEL_PUMP);
		registerItem("glock", GLOCK);
		registerItem("bullet_9mm", BULLET_9MM);
	}
}
