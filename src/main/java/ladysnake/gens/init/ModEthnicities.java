package ladysnake.gens.init;

import ladylib.registration.AutoRegister;
import ladysnake.gens.Gens;
import ladysnake.gens.entity.GensEthnicity;
import net.minecraftforge.fml.common.registry.GameRegistry;

@AutoRegister(Gens.MOD_ID)
@GameRegistry.ObjectHolder(Gens.MOD_ID)
public class ModEthnicities {
    public static final GensEthnicity HAR = new GensEthnicity();


}
