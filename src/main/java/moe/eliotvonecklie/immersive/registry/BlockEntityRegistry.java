package moe.eliotvonecklie.immersive.registry;

import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;

import moe.eliotvonecklie.immersive.ImmersiveMod;
import moe.eliotvonecklie.immersive.blockentities.*;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.BlockEntityType.BlockEntityFactory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntityRegistry {
	public static BlockEntityType<SecurityFactionRankReaderBlockEntity> SECURITY_FACTION_RANK_READER_BLOCKENTITY;
	public static BlockEntityType<SecurityIdentityReaderBlockEntity> SECURITY_IDENTITY_READER_BLOCKENTITY;
	
	private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String id, BlockEntityFactory<T> blockEntityFactory, Block block) {
		return Registry.register(Registries.BLOCK_ENTITY_TYPE,
				new Identifier(ImmersiveMod.MODID, id),
				QuiltBlockEntityTypeBuilder.create(blockEntityFactory, block).build()
			);
	}

	public static void init() {
		SECURITY_FACTION_RANK_READER_BLOCKENTITY = registerBlockEntity("security_faction_rank_reader", SecurityFactionRankReaderBlockEntity::new, BlockRegistry.SECURITY_FACTION_RANK_READER);
		SECURITY_IDENTITY_READER_BLOCKENTITY = registerBlockEntity("security_identity_reader", SecurityIdentityReaderBlockEntity::new, BlockRegistry.SECURITY_IDENTITY_READER);
	}

}
