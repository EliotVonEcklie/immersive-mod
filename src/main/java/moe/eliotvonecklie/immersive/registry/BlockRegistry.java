package moe.eliotvonecklie.immersive.registry;

import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import moe.eliotvonecklie.immersive.ImmersiveMod;
import moe.eliotvonecklie.immersive.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockRegistry {
	public static final SecurityFactionRankReader SECURITY_FACTION_RANK_READER = new SecurityFactionRankReader(QuiltBlockSettings.of(Material.STONE).hardness(4.0f));
	public static final SecurityIdentityReader SECURITY_IDENTITY_READER = new SecurityIdentityReader(QuiltBlockSettings.of(Material.STONE).hardness(4.0f));
	
	private static <T extends Block> void registerBlock(String id, T block) {
		Registry.register(Registries.BLOCK, new Identifier(ImmersiveMod.MODID, id), block);
	}
	
	private static <T extends Block> void registerBlockWithItem(String id, T block) {
		registerBlock(id, block);
		Registry.register(Registries.ITEM, new Identifier(ImmersiveMod.MODID, id + "_item"), new BlockItem(block, new Item.Settings()));
	}
	
	public static void register() {
		registerBlockWithItem("security_faction_rank_reader", SECURITY_FACTION_RANK_READER);
		registerBlockWithItem("security_identity_reader", SECURITY_IDENTITY_READER);
	}
}
