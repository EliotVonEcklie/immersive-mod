package moe.eliotvonecklie.immersive;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import moe.eliotvonecklie.immersive.registry.EntityRegistry;

@ClientOnly
public class ImmersiveModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityRegistry.initClient();
	}

}
