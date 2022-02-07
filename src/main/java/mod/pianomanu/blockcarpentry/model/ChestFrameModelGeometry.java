package mod.pianomanu.blockcarpentry.model;

import com.mojang.datafixers.util.Pair;
import mod.pianomanu.blockcarpentry.bakedmodels.ChestBakedModel;
import mod.pianomanu.blockcarpentry.bakedmodels.FrameBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class ChestFrameModelGeometry implements IModelGeometry<ChestFrameModelGeometry> {

    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
        return new ChestBakedModel();
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return Collections.singletonList(new Material(InventoryMenu.BLOCK_ATLAS, FrameBakedModel.TEXTURE));
    }
}
