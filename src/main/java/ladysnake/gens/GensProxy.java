package ladysnake.gens;

import ladysnake.gens.init.ModEntities;
import net.minecraftforge.fml.common.SidedProxy;

public class GensProxy {
    @SidedProxy
    public static GensProxy proxy;

    public void preInit() {

    }

    @SuppressWarnings("unused")
    public static class ClientProxy extends GensProxy {
        @Override
        public void preInit() {
            super.preInit();
            ModEntities.registerRenders();
        }
    }

    @SuppressWarnings("unused")
    public static class ServerProxy extends GensProxy {

    }
}
