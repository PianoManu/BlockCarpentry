package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.bakedmodels.helper.DoorKnobBakedModel;
import mod.pianomanu.blockcarpentry.block.DoorFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Contains all information for the block model
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.0 05/23/22
 */
public class DoorBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        //get block saved in frame tile
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getMimicQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }

        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        int tex = extraData.getData(FrameBlockTile.TEXTURE);
        if (mimic != null && state != null && extraData.getData(FrameBlockTile.DESIGN) != null && extraData.getData(FrameBlockTile.DESIGN_TEXTURE) != null) {
            //get texture from block in tile entity and apply it to the quads
            List<TextureAtlasSprite> glassBlockList = TextureHelper.getGlassTextures();
            TextureAtlasSprite glass = glassBlockList.get(extraData.getData(FrameBlockTile.GLASS_COLOR));
            List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
            TextureAtlasSprite texture;
            if (textureList.size() <= tex) {
                //texture = textureList.get(0);
                extraData.setData(FrameBlockTile.TEXTURE, 0);
                tex = 0;
            }
            if (textureList.size() == 0) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.displayClientMessage(new TranslatableComponent("message.blockcarpentry.block_not_available"), true);
                }
                return Collections.emptyList();
            }
            texture = textureList.get(tex);
            List<BakedQuad> quads = new ArrayList<>();
            Direction dir = state.getValue(DoorFrameBlock.FACING);
            boolean open = state.getValue(DoorFrameBlock.OPEN);
            DoorHingeSide hinge = state.getValue(DoorFrameBlock.HINGE);
            Direction west = Direction.WEST;
            Direction east = Direction.EAST;
            Direction north = Direction.NORTH;
            Direction south = Direction.SOUTH;
            DoorHingeSide left = DoorHingeSide.LEFT;
            DoorHingeSide right = DoorHingeSide.RIGHT;
            int design = extraData.getData(FrameBlockTile.DESIGN);//int design = state.getValue(DoorFrameBlock.DESIGN);
            int desTex = extraData.getData(FrameBlockTile.DESIGN_TEXTURE); //state.getValue(DoorFrameBlock.DESIGN_TEXTURE);
            DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
            DoubleBlockHalf lower = DoubleBlockHalf.LOWER;
            DoubleBlockHalf upper = DoubleBlockHalf.UPPER;
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            boolean northSide = (dir == north && !open && hinge == right) || (dir == east && open && hinge == right) || (dir == west && open && hinge == left) || (dir == north && !open && hinge == left);
            boolean westSide = (dir == west && !open && hinge == right) || (dir == north && open && hinge == right) || (dir == south && open && hinge == left) || (dir == west && !open && hinge == left);
            boolean eastSide = (dir == south && open && hinge == right) || (dir == east && !open && hinge == right) || (dir == east && !open && hinge == left) || (dir == north && open && hinge == left);
            if (design == 0) {
                if (northSide) {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 4 / 16f, 1f, 14 / 16f, 15 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 4 / 16f, 13 / 16f, 1f, texture, tintIndex));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 12 / 16f, 14 / 16f, 15 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 12 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex));
                    }
                } else if (westSide) {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 12 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 0f, 4 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(14 / 16f, 15 / 16f, 4 / 16f, 1f, 4 / 16f, 12 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0f, 4 / 16f, 4 / 16f, 12 / 16f, texture, tintIndex));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 12 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 0f, 4 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(14 / 16f, 15 / 16f, 0f, 12 / 16f, 4 / 16f, 12 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 12 / 16f, 1f, 4 / 16f, 12 / 16f, texture, tintIndex));
                    }
                } else if (eastSide) {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 12 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 0f, 4 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(1 / 16f, 2 / 16f, 4 / 16f, 1f, 4 / 16f, 12 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0f, 4 / 16f, 4 / 16f, 12 / 16f, texture, tintIndex));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 12 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 0f, 4 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(1 / 16f, 2 / 16f, 0f, 12 / 16f, 4 / 16f, 12 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 12 / 16f, 1f, 4 / 16f, 12 / 16f, texture, tintIndex));
                    }
                } else {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 4 / 16f, 1f, 1 / 16f, 2 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 4 / 16f, 0f, 3 / 16f, texture, tintIndex));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 12 / 16f, 1 / 16f, 2 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 12 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex));
                    }
                }
            }
            if (design == 3) {
                if (northSide) {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, 14 / 16f, 15 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 4 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 12 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, 14 / 16f, 15 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 12 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 4 / 16f, 13 / 16f, 1f, texture, tintIndex));
                    }
                } else if (westSide) {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 12 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 0f, 4 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(14 / 16f, 15 / 16f, 4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0f, 4 / 16f, 4 / 16f, 12 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 12 / 16f, 1f, 4 / 16f, 12 / 16f, texture, tintIndex));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 12 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 0f, 4 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(14 / 16f, 15 / 16f, 4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 12 / 16f, 1f, 4 / 16f, 12 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(13 / 16f, 1f, 0f, 4 / 16f, 4 / 16f, 12 / 16f, texture, tintIndex));
                    }
                } else if (eastSide) {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 12 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 0f, 4 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(1 / 16f, 2 / 16f, 4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0f, 4 / 16f, 4 / 16f, 12 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 12 / 16f, 1f, 4 / 16f, 12 / 16f, texture, tintIndex));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 12 / 16f, 1f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 0f, 4 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(1 / 16f, 2 / 16f, 4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 12 / 16f, 1f, 4 / 16f, 12 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 3 / 16f, 0f, 4 / 16f, 4 / 16f, 12 / 16f, texture, tintIndex));
                    }
                } else {
                    if (half == lower) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, 1 / 16f, 2 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 4 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 12 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex));
                    }
                    if (half == upper) {
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(0f, 4 / 16f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(12 / 16f, 1f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 4 / 16f, 12 / 16f, 1 / 16f, 2 / 16f, glass, -1));
                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createCuboid(4 / 16f, 12 / 16f, 12 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 4 / 16f, 0f, 3 / 16f, texture, tintIndex));
                    }
                }
            }
            if (design == 1 || design == 2) {
                int flag = 0;
                if (northSide) {
                    flag = 1;
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 13 / 16f, 1f, texture, tintIndex));
                } else if (westSide) {
                    flag = 2;
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0f, 1f, 0f, 1f, texture, tintIndex));
                } else if (eastSide) {
                    flag = 3;
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0f, 1f, 0f, 1f, texture, tintIndex));
                } else {
                    quads.addAll(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 3 / 16f, texture, tintIndex));
                }
                if (design == 1) {
                    if (half == lower) {
                        if ((dir == south && hinge == left && !open) || (dir == west && hinge == right && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2 / 16f, 4 / 16f, 15 / 16f, 17 / 16f, -1 / 16f, 1 / 16f, flag, desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2 / 16f, 4 / 16f, 15 / 16f, 17 / 16f, 2 / 16f, 4 / 16f, flag, desTex));
                        } else if ((dir == south && hinge == right && !open) || (dir == east && hinge == left && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12 / 16f, 14 / 16f, 15 / 16f, 17 / 16f, -1 / 16f, 1 / 16f, flag, desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12 / 16f, 14 / 16f, 15 / 16f, 17 / 16f, 2 / 16f, 4 / 16f, flag, desTex));
                        }
                    }
                    if (half == lower) {
                        if ((dir == north && hinge == right && !open) || (dir == west && hinge == left && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2 / 16f, 4 / 16f, 15 / 16f, 17 / 16f, 15 / 16f, 17 / 16f, flag, desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2 / 16f, 4 / 16f, 15 / 16f, 17 / 16f, 12 / 16f, 14 / 16f, flag, desTex));
                        } else if ((dir == north && hinge == left && !open) || (dir == east && hinge == right && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12 / 16f, 14 / 16f, 15 / 16f, 17 / 16f, 15 / 16f, 17 / 16f, flag, desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12 / 16f, 14 / 16f, 15 / 16f, 17 / 16f, 12 / 16f, 14 / 16f, flag, desTex));
                        }
                    }
                    if (half == lower) {
                        if ((dir == west && hinge == left && !open) || (dir == north && hinge == right && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(15 / 16f, 17 / 16f, 15 / 16f, 17 / 16f, 2 / 16f, 4 / 16f, flag, desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12 / 16f, 14 / 16f, 15 / 16f, 17 / 16f, 2 / 16f, 4 / 16f, flag, desTex));
                        } else if ((dir == west && hinge == right && !open) || (dir == south && hinge == left && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(15 / 16f, 17 / 16f, 15 / 16f, 17 / 16f, 12 / 16f, 14 / 16f, flag, desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(12 / 16f, 14 / 16f, 15 / 16f, 17 / 16f, 12 / 16f, 14 / 16f, flag, desTex));
                        }
                    }
                    if (half == lower) {
                        if ((dir == east && hinge == right && !open) || (dir == north && hinge == left && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(-1 / 16f, 1 / 16f, 15 / 16f, 17 / 16f, 2 / 16f, 4 / 16f, flag, desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2 / 16f, 4 / 16f, 15 / 16f, 17 / 16f, 2 / 16f, 4 / 16f, flag, desTex));
                        } else if ((dir == east && hinge == left && !open) || (dir == south && hinge == right && open)) {
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(-1 / 16f, 1 / 16f, 15 / 16f, 17 / 16f, 12 / 16f, 14 / 16f, flag, desTex));
                            quads.addAll(DoorKnobBakedModel.createDoorKnob(2 / 16f, 4 / 16f, 15 / 16f, 17 / 16f, 12 / 16f, 14 / 16f, flag, desTex));
                        }
                    }
                }
            }
            if (design == 4) {
                if (northSide) {
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, 14 / 16f, 15 / 16f, glass, -1));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 0f, 3 / 16f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 13 / 16f, 1f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, 13 / 16f, 1f, texture, tintIndex));
                } else if (westSide) {
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(14 / 16f, 15 / 16f, 3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, glass, -1));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0f, 3 / 16f, 3 / 16f, 13 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 13 / 16f, 1f, 3 / 16f, 13 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, texture, tintIndex));
                } else if (eastSide) {
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 13 / 16f, 1f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 2 / 16f, 3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, glass, -1));
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0f, 3 / 16f, 3 / 16f, 13 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 13 / 16f, 1f, 3 / 16f, 13 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, texture, tintIndex));
                } else {
                    quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0, 1f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, 1 / 16f, 2 / 16f, glass, -1));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 0f, 3 / 16f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 13 / 16f, 1f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, 0f, 3 / 16f, texture, tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, 0f, 3 / 16f, texture, tintIndex));
                }
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                if (northSide) {
                    quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 13 / 16f, 1f, overlayIndex));
                } else if (westSide) {
                    quads.addAll(ModelHelper.createOverlay(13 / 16f, 1f, 0f, 1f, 0f, 1f, overlayIndex));
                } else if (eastSide) {
                    quads.addAll(ModelHelper.createOverlay(0f, 3 / 16f, 0f, 1f, 0f, 1f, overlayIndex));
                } else {
                    quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 3 / 16f, overlayIndex));
                }
            }

            return quads;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    @Nonnull
    public TextureAtlasSprite getParticleIcon() {
        return getTexture();
    }

    @Override
    @Nonnull
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
//========SOLI DEO GLORIA========//