package moe.eliotvonecklie.immersive;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import moe.eliotvonecklie.immersive.registry.BlockEntityRegistry;
import moe.eliotvonecklie.immersive.registry.BlockRegistry;
import moe.eliotvonecklie.immersive.registry.EntityRegistry;
import moe.eliotvonecklie.immersive.registry.ItemRegistry;

public class ImmersiveMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Immersive");
	public static String MODID = "immersive";

	@Override
	public void onInitialize(ModContainer mod) {
		MODID = mod.metadata().id();
		
		BlockRegistry.register();
		BlockEntityRegistry.register();
		ItemRegistry.register();
		EntityRegistry.init();
		
		//DimensionRegistry
		CustomPortalBuilder.beginPortal()
			.frameBlock(Blocks.QUARTZ_BLOCK)
			.destDimID(new Identifier(MODID, "moon"))
			.tintColor(255, 255, 255)
			.onlyLightInOverworld()
			.registerPortal();
	}
}
