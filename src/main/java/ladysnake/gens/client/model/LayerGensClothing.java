package ladysnake.gens.client.model;

import ladysnake.gens.entity.EntityGensVillager;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

import javax.annotation.Nonnull;

public class LayerGensClothing implements LayerRenderer<EntityGensVillager> {
    private final RenderLivingBase<?> renderer;
    private final ModelBiped layerModel = new ModelBiped(0.25F, 0.0F, 64, 64);

    public LayerGensClothing(RenderLivingBase<?> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityGensVillager gensVillager, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.layerModel.setModelAttributes(this.renderer.getMainModel());
        this.layerModel.setLivingAnimations(gensVillager, limbSwing, limbSwingAmount, partialTicks);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.renderer.bindTexture(gensVillager.getClothesTexture());
        this.layerModel.render(gensVillager, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
