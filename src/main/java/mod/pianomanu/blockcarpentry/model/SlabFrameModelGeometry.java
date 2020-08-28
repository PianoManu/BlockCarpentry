package mod.pianomanu.blockcarpentry.model;

import mod.pianomanu.blockcarpentry.bakedmodels.SlabFrameBakedModel;
import mod.pianomanu.blockcarpentry.bakedmodels.SlabFrameTopBakedModel;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class SlabFrameModelGeometry implements IModelGeometry<SlabFrameModelGeometry> {
    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {
        return new SlabFrameBakedModel();
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        return Collections.singletonList(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, SlabFrameTopBakedModel.TEXTURE));
    }
}
