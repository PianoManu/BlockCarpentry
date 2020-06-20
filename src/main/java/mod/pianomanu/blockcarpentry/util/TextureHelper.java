package mod.pianomanu.blockcarpentry.util;

import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class TextureHelper {
    public static TextureAtlasSprite getTextureFromBlock(Block blockIn) {
        ResourceLocation location;
        List<ResourceLocation> locationList = new ArrayList<>();
        String m = "minecraft";
        String n = blockIn.getRegistryName().getNamespace();
        String p = blockIn.getRegistryName().getPath();
        String b = "block/";
        locationList.add(loc(n,b+p));
        if(blockIn instanceof GrassBlock) {
            locationList.set(0,loc(m,b+"dirt"));
            locationList.add(loc(m,b+"grass_block_top"));
        }
        if(blockIn instanceof RotatedPillarBlock) {
            locationList.set(0,loc(n, b+p));
            locationList.add(loc(n, b+p+"_top"));
        }
        if(blockIn instanceof CraftingTableBlock) {
            locationList.set(0,loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side"));
            locationList.add(loc(m,b+p+"_front"));
        }
        System.out.println(locationList.toString());
        location=locationList.get(0);
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(location);
    }

    private static ResourceLocation loc(String nameSpace, String path) {
        return new ResourceLocation(nameSpace, path);
    }

    /**
     * creates ResourceLocations and gets TextureAtlasSprites for blocks with different textures on each side (e.g. grass block with dirt sides/bottom, grass overlay on sides and grass_top texture on top
     * A typical ResourceLocation for textures looks like this: <namespace>:block/<registry blockname>, e.g. for oak planks: "minecraft:block/oak_planks"
     * @param blockIn Block in frame, to get the texture from
     * @return TextureAtlasSprite from Atlas with the texture of the given block (hopefully)
     */
    public static List<TextureAtlasSprite> getTextureListFromBlock(Block blockIn) {
        List<ResourceLocation> locationList = new ArrayList<>();
        String m = "minecraft";
        String n = blockIn.getRegistryName().getNamespace();
        String p = blockIn.getRegistryName().getPath();
        String b = "block/";
        locationList.add(loc(n,b+p));
        if(blockIn instanceof GrassBlock) {
            locationList.set(0,loc(m,b+"dirt"));
            locationList.add(loc(m,b+"grass_block_top"));
        }
        if(blockIn instanceof RotatedPillarBlock) {
            locationList.set(0,loc(n, b+p));
            locationList.add(loc(n, b+p+"_top"));
        }
        if(blockIn instanceof BarrelBlock) {
            locationList.set(0,loc(n, b+p+"_side"));
            locationList.add(loc(n, b+p+"_bottom"));
            locationList.add(loc(n, b+p+"_top"));
            locationList.add(loc(n, b+p+"_top_open"));
        }
        if(blockIn==Blocks.SANDSTONE) {
            locationList.set(0,loc(n,b+p));
            locationList.add(loc(n,b+"cut_sandstone"));
            locationList.add(loc(n,b+"sandstone_top"));
        }
        if(blockIn==Blocks.SMOOTH_SANDSTONE) {
            locationList.set(0,loc(n,b+"sandstone_top"));
            locationList.add(loc(n,b+"cut_sandstone"));
        }
        if(blockIn==Blocks.CUT_SANDSTONE) {
            locationList.set(0,loc(n,b+"sandstone_top"));
        }
        if(blockIn==Blocks.CHISELED_SANDSTONE) {
            locationList.set(0,loc(n,b+"sandstone_top"));
            locationList.add(loc(n,b+"chiseled_sandstone"));
        }
        if(blockIn==Blocks.CARVED_PUMPKIN) {
            locationList.set(0,loc(n,b+p));
            locationList.add(loc(n,b+"pumpkin_side"));
            locationList.add(loc(n,b+"pumpkin_top"));
        }
        if(blockIn==Blocks.PUMPKIN || blockIn==Blocks.MELON) {
            locationList.set(0,loc(n,b+p+"_side"));
            locationList.add(loc(n,b+p+"_top"));
        }
        if(blockIn==Blocks.JACK_O_LANTERN) {
            locationList.set(0,loc(n,b+"jack_o_lantern"));
            locationList.add(loc(n,b+"pumpkin_side"));
            locationList.add(loc(n,b+"pumpkin_top"));
        }
        if(blockIn==Blocks.MYCELIUM) {
            locationList.set(0,loc(n,b+"dirt"));
            locationList.add(loc(n,b+"mycelium_top"));
        }
        if(blockIn==Blocks.DRIED_KELP_BLOCK) {
            locationList.set(0,loc(n,b+"dried_kelp_bottom"));
            locationList.add(loc(n,b+"dried_kelp_side"));
            locationList.add(loc(n,b+"dried_kelp_top"));
        }
        if(blockIn instanceof HayBlock) {
            locationList.set(0,loc(n,b+p+"_side"));
            locationList.add(loc(n,b+p+"_top"));
        }
        if(blockIn==Blocks.CRAFTING_TABLE) {
            locationList.set(0,loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side"));
            locationList.add(loc(m,b+p+"_front"));
        }
        if(blockIn instanceof JukeboxBlock) {
            locationList.set(0,loc(m,b+p+"_side"));
            locationList.add(loc(m,b+p+"_top"));
        }
        if(blockIn instanceof FurnaceBlock) {
            locationList.set(0,loc(m,b+p+"_front"));
            locationList.add(loc(m,b+p+"_front_on"));
            locationList.add(loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side"));
        }
        if(blockIn instanceof EnchantingTableBlock) {
            locationList.set(0,loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_bottom"));
            locationList.add(loc(m,b+p+"_side"));
        }
        if(blockIn instanceof EndPortalFrameBlock) {
            locationList.set(0,loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side"));
            locationList.add(loc(m,b+"end_stone"));
        }
        if(blockIn instanceof GrassPathBlock) {
            locationList.set(0,loc(m,b+"dirt"));
            locationList.add(loc(m,b+p+"_top"));
        }
        if(blockIn instanceof LoomBlock) {
            locationList.set(0,loc(m,b+p+"_front"));
            locationList.add(loc(m,b+p+"_bottom"));
            locationList.add(loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side"));
        }
        if(blockIn instanceof SmokerBlock) {
            locationList.set(0,loc(m,b+p+"_front"));
            locationList.add(loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side"));
            locationList.add(loc(m,b+p+"_bottom"));
        }
        if(blockIn instanceof BlastFurnaceBlock) {
            locationList.set(0,loc(m,b+p+"_front"));
            locationList.add(loc(m,b+p+"_front_on"));
            locationList.add(loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side"));
        }
        if(blockIn instanceof CartographyTableBlock) {
            locationList.set(0,loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side1"));
            locationList.add(loc(m,b+p+"_side2"));
            locationList.add(loc(m,b+p+"_side3"));
        }
        if(blockIn instanceof FletchingTableBlock) {
            locationList.set(0,loc(m,b+p+"_front"));
            locationList.add(loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side"));
        }
        if(blockIn instanceof SmithingTableBlock) {
            locationList.set(0,loc(m,b+p+"_front"));
            locationList.add(loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side"));
            locationList.add(loc(m,b+p+"_bottom"));
        }
        if(blockIn==Blocks.BEEHIVE) {
            locationList.set(0,loc(m,b+p+"_front"));
            locationList.add(loc(m,b+p+"_front_honey"));
            locationList.add(loc(m,b+p+"_side"));
            locationList.add(loc(m,b+p+"_end"));
        }
        if(blockIn==Blocks.BEE_NEST) {
            locationList.set(0,loc(m,b+p+"_front"));
            locationList.add(loc(m,b+p+"_front_honey"));
            locationList.add(loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side"));
        }
        if(blockIn instanceof DispenserBlock) {
            locationList.set(0,loc(m,b+p+"_front"));
            locationList.add(loc(m,b+p+"_front_vertical"));
            locationList.add(loc(m,b+"furnace_top"));
        }
        if(blockIn==Blocks.PISTON) {
            locationList.set(0,loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_inner"));
            locationList.add(loc(m,b+p+"_bottom"));
            locationList.add(loc(m,b+p+"_side"));
        }
        if(blockIn==Blocks.STICKY_PISTON) {
            locationList.set(0,loc(m,b+"piston_top_sticky"));
            locationList.add(loc(m,b+"piston_inner"));
            locationList.add(loc(m,b+"piston_bottom"));
            locationList.add(loc(m,b+"piston_side"));
        }
        if(blockIn instanceof TNTBlock) {
            locationList.set(0,loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_bottom"));
            locationList.add(loc(m,b+p+"_side"));
        }
        if(blockIn instanceof RedstoneLampBlock) {
            locationList.set(0,loc(m,b+p));
            locationList.add(loc(m,b+p+"_on"));
        }
        if(blockIn instanceof ObserverBlock) {
            locationList.set(0,loc(m,b+p+"_front"));
            locationList.add(loc(m,b+p+"_top"));
            locationList.add(loc(m,b+p+"_side"));
            locationList.add(loc(m,b+p+"_back_on"));
        }
        return getTextureFromLocation(locationList);
    }

    private static List<TextureAtlasSprite> getTextureFromLocation(List<ResourceLocation> locationList) {
        List<TextureAtlasSprite> textureList = new ArrayList<>();
        for(ResourceLocation location:locationList) {
            textureList.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(location));
        }
        return textureList;
    }
}
