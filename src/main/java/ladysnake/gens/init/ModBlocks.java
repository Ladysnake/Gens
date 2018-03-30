package ladysnake.gens.init;

import ladylib.registration.BlockRegistrar;
import ladysnake.gens.Gens;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static ladylib.registration.BlockRegistrar.name;

@GameRegistry.ObjectHolder(Gens.MOD_ID)
@Mod.EventBusSubscriber(modid = Gens.MOD_ID)
public class ModBlocks {

    @SubscribeEvent
    public static void onRegistryRegister(RegistryEvent.Register<Block> event) {
        BlockRegistrar reg = Gens.lib.getBlockRegistrar();
        // Example registration. This will register a block, set its registry and unlocalized names,
        // add it to the mod's creative tab and create the corresponding itemblock
        reg.addBlock(name(new Block(Material.CLOTH), "fabric"), true, true);
    }
}
