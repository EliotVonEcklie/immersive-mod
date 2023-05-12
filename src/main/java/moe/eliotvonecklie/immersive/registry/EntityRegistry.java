package moe.eliotvonecklie.immersive.registry;

import org.quiltmc.loader.api.minecraft.ClientOnly;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

import moe.eliotvonecklie.immersive.ImmersiveMod;
import moe.eliotvonecklie.immersive.client.render.entity.NuclearFissionExplosiveEntityRenderer;
import moe.eliotvonecklie.immersive.entities.NuclearFissionExplosiveEntity;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.TntEntityRenderer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.EntityType.EntityFactory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class EntityRegistry {
	public static final EntityType<NuclearFissionExplosiveEntity> NUCLEAR_FISSION_EXPLOSIVE_ENTITY = 
			Registry.register(Registries.ENTITY_TYPE, new Identifier(ImmersiveMod.MODID, "nuclear_fission_explosive_entity"),
					QuiltEntityTypeBuilder.create(SpawnGroup.MISC, (EntityFactory<NuclearFissionExplosiveEntity>)NuclearFissionExplosiveEntity::new).setDimensions(EntityDimensions.fixed(1, 1)).build()
			);
			
	@ClientOnly
	public static void initClient() {
        /*
         * Registers our Cube Entity's renderer, which provides a model and texture for the entity.
         *
         * Entity Renderers can also manipulate the model before it renders based on entity context (EndermanEntityRenderer#render).
         */
        EntityRendererRegistry.register(NUCLEAR_FISSION_EXPLOSIVE_ENTITY, (context) -> {
            return new NuclearFissionExplosiveEntityRenderer(context);
        });
 
        //EntityModelLayerRegistry.registerModelLayer(MODEL_CUBE_LAYER, TntEntityModel::getTexturedModelData);
	}
	
	public static void init() {
		
	}
}
