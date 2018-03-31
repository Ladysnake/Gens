package ladysnake.gens.init;

import ladysnake.gens.Gens;
import ladysnake.gens.entity.EntityGensMerchant;
import ladysnake.gens.entity.EntityGensSoldier;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = Gens.MOD_ID)
public class ModEntities {
    private static int id;
    @SubscribeEvent
    public static void onRegistryRegister(RegistryEvent.Register<EntityEntry> event) {
        event.getRegistry().registerAll(
                createEntry(EntityGensMerchant.class, EntityGensMerchant::new, "merchant").egg(0xFFDE66, 0x1354AD).build(),
                createEntry(EntityGensSoldier.class, EntityGensSoldier::new, "soldier").egg(0xFFDE66, 0x354687).build()
        );
    }

    private static <T extends Entity> EntityEntryBuilder<T> createEntry(Class<? extends T> clazz, Function<World, T> entityFactory, String name) {
        EntityEntryBuilder<T> ret = EntityEntryBuilder.create();
        return ret.entity(clazz)
                .factory(entityFactory)
                .id(new ResourceLocation(Gens.MOD_ID, name), id++)
                .name(name)
                .tracker(64, 1, true);
    }

    public static void registerRenders() {
        RenderingRegistry.registerEntityRenderingHandler(EntityGensMerchant.class, renderManager -> new RenderBiped<>(renderManager, new ModelBiped(), 1.0f));
        RenderingRegistry.registerEntityRenderingHandler(EntityGensSoldier.class, renderManager -> new RenderBiped<>(renderManager, new ModelBiped(), 1.0f));
    }
}
