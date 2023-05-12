package moe.eliotvonecklie.immersive.registry;

import java.util.HashMap;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;
import moe.eliotvonecklie.immersive.ImmersiveMod;
import moe.eliotvonecklie.immersive.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class BlockRegistry {
	public static final SecurityFactionRankReader SECURITY_FACTION_RANK_READER = new SecurityFactionRankReader(QuiltBlockSettings.of(Material.STONE).hardness(4.0f).requiresTool());
	public static final SecurityIdentityReader SECURITY_IDENTITY_READER = new SecurityIdentityReader(QuiltBlockSettings.of(Material.STONE).hardness(4.0f).requiresTool());
	public static final Block MOON_BLOCK = new Block(QuiltBlockSettings.of(Material.AGGREGATE).hardness(0.5f).sounds(BlockSoundGroup.SAND));
	public static final NuclearFissionExplosive NUCLEAR_FISSION_EXPLOSIVE = new NuclearFissionExplosive(QuiltBlockSettings.of(Material.TNT).breakInstantly().sounds(BlockSoundGroup.GRASS));
	
	private static final HashMap<Identifier, BlockItem> BLOCK_ITEMS_MAP = new HashMap<Identifier, BlockItem>();
	
	private static <T extends Block> void registerBlock(String id, T block) {
		Registry.register(Registries.BLOCK, new Identifier(ImmersiveMod.MODID, id), block);
	}
	
	private static <T extends Block> void registerBlockWithItemSettings(String id, T block, Item.Settings itemSettings) {
		registerBlock(id, block);
		Identifier identifier =  new Identifier(ImmersiveMod.MODID, id + "_item");
		BlockItem blockItem = new BlockItem(block, itemSettings);
		BLOCK_ITEMS_MAP.put(identifier, blockItem);
		Registry.register(Registries.ITEM, identifier, blockItem);
	}
	
	private static <T extends Block> void registerBlockWithItem(String id, T block) {
		registerBlockWithItemSettings(id, block, new Item.Settings());
	}
	
	public static BlockItem getRegisteredBlockItem(String id) {
		if (!id.endsWith("_item"))
			id += "_item";
		
		return BLOCK_ITEMS_MAP.get(new Identifier(ImmersiveMod.MODID, id));
	}
	
	public static void init() {
		registerBlockWithItem("security_faction_rank_reader", SECURITY_FACTION_RANK_READER);
		registerBlockWithItem("security_identity_reader", SECURITY_IDENTITY_READER);
		registerBlockWithItem("moon_block", MOON_BLOCK);
		registerBlockWithItemSettings("nuclear_fission_explosive", NUCLEAR_FISSION_EXPLOSIVE, new QuiltItemSettings().rarity(Rarity.RARE));
	}
}
