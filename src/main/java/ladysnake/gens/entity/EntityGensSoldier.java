package ladysnake.gens.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityGensSoldier extends EntityGensVillager {
    public EntityGensSoldier(World worldIn) {
        super(worldIn);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) {
        return null;
    }
}
