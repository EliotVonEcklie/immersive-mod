package moe.eliotvonecklie.immersive.registry;

import static moe.eliotvonecklie.immersive.registry.ItemRegistry.*;
import static moe.eliotvonecklie.immersive.registry.BlockRegistry.getRegisteredBlockItem;
import moe.eliotvonecklie.immersive.ImmersiveMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ItemGroupRegistry {
	public static ItemGroup SECURITY, WEAPONS, PLANETS;

	public static void init() {
		SECURITY = FabricItemGroup.builder(new Identifier(ImmersiveMod.MODID, "security"))
			.icon(() -> new ItemStack(getRegisteredBlockItem("security_identity_reader")))
			.build();
		
	    ItemGroupEvents.modifyEntriesEvent(SECURITY).register(content -> {
	    	content.prepend(getRegisteredBlockItem("security_faction_rank_reader"));
	    	content.prepend(getRegisteredBlockItem("security_identity_reader"));
	    });
		
		WEAPONS = FabricItemGroup.builder(new Identifier(ImmersiveMod.MODID, "weapons"))
			.icon(() -> new ItemStack(GLOCK))
			.build();
		
		ItemGroupEvents.modifyEntriesEvent(WEAPONS).register(content -> {
	    	content.prepend(GLOCK);
	    });
		
		PLANETS = FabricItemGroup.builder(new Identifier(ImmersiveMod.MODID, "planets"))
			.icon(() -> new ItemStack(getRegisteredBlockItem("moon_block")))
			.build();
		
		ItemGroupEvents.modifyEntriesEvent(PLANETS).register(content -> {
	    	content.prepend(MOON_ROCK);
	    	content.prepend(POWERED_MOON_ROCK);
	    	content.prepend(getRegisteredBlockItem("moon_block"));
	    });
	}
}
