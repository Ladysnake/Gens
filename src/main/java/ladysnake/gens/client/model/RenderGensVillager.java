package ladysnake.gens.client.model;

import ladysnake.gens.entity.EntityGensVillager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class RenderGensVillager extends RenderBiped<EntityGensVillager> {
    public RenderGensVillager(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelBiped(0, 0, 64, 64), 0.5F);
        this.addLayer(new LayerGensClothing(this));
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
            protected void initArmor() {
                this.modelLeggings = new ModelBiped(0.5F, 0.0F, 64, 32);
                this.modelArmor = new ModelBiped(1.0F, 0.0F, 64, 32);
            }
        };
        this.addLayer(layerbipedarmor);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityGensVillager entity) {
        return entity.getTexture();
    }
}
