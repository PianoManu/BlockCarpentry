package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.bakedmodels.helper.HandleBakedModel;
import mod.pianomanu.blockcarpentry.block.DoorFrameBlock;
import mod.pianomanu.blockcarpentry.block.TrapdoorFrameBlock;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains all information for the block model
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.1 06/11/22
 */
public class IllusionTrapdoorBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getIllusionQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    private List<BakedQuad> getIllusionQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull IModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && state != null) {
            //get texture from block in tile entity and apply it to the quads
            List<TextureAtlasSprite> glassBlockList = TextureHelper.getGlassTextures();
            TextureAtlasSprite glass = glassBlockList.get(extraData.getData(FrameBlockTile.GLASS_COLOR));
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            int rotation = extraData.getData(FrameBlockTile.ROTATION);
            List<BakedQuad> quads = new ArrayList<>();
            Direction dir = state.getValue(DoorFrameBlock.FACING);
            boolean open = state.getValue(TrapdoorFrameBlock.OPEN);
            Half half = state.getValue(TrapDoorBlock.HALF);
            Half top = Half.TOP;
            Half bottom = Half.BOTTOM;
            Direction west = Direction.WEST;
            Direction east = Direction.EAST;
            Direction north = Direction.NORTH;
            Direction south = Direction.SOUTH;
            int design = extraData.getData(FrameBlockTile.DESIGN);//int design = state.getValue(DoorFrameBlock.DESIGN);
            int desTex = extraData.getData(FrameBlockTile.DESIGN_TEXTURE); //state.getValue(DoorFrameBlock.DESIGN_TEXTURE);

            if (design == 0 || design == 1) {
                if (dir == north && open) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                } else if (dir == west && open) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                } else if (dir == east && open) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                } else if (dir == south && open) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                } else if (half == bottom) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 3 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                } else if (half == top) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 13 / 16f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                }
            }
            if (design == 1) {
                /*DIFFERENT DIRECTIONS = DIFFERENT QUADS
                MATCHING PAIRS:
                        rotationFlag == 0: WEST/UP/CLOSED EAST/UP/CLOSED WEST/DOWN/CLOSED EAST/DOWN/CLOSED
                        rotationFlag == 1: NORTH/UP/CLOSED SOUTH/UP/CLOSED NORTH/DOWN/CLOSED SOUTH/DOWN/CLOSED
                        rotationFlag == 2: NORTH/UP/OPEN NORTH/DOWN/OPEN SOUTH/UP/OPEN SOUTH/DOWN/OPEN
                        rotationFlag == 3: EAST/UP/OPEN EAST/DOWN/OPEN WEST/UP/OPEN WEST/DOWN/OPEN
                 */
                //int rotationFlag = 0;
                if (open && half == bottom) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 13 / 16f, 14 / 16f, 12 / 16f, 17 / 16f, 2, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(-1 / 16f, 4 / 16f, 13 / 16f, 14 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 13 / 16f, 14 / 16f, -1 / 16f, 4 / 16f, 2, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(12 / 16f, 17 / 16f, 13 / 16f, 14 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                    }
                } else if (open && half == top) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 2 / 16f, 3 / 16f, 12 / 16f, 17 / 16f, 2, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(-1 / 16f, 4 / 16f, 2 / 16f, 3 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 2 / 16f, 3 / 16f, -1 / 16f, 4 / 16f, 2, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(12 / 16f, 17 / 16f, 2 / 16f, 3 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                    }
                }
                if (half == bottom && !open) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, -1 / 16f, 4 / 16f, 2 / 16f, 3 / 16f, 1, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(13 / 16f, 14 / 16f, -1 / 16f, 4 / 16f, 6 / 16f, 10 / 16f, 0, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, -1 / 16f, 4 / 16f, 13 / 16f, 14 / 16f, 1, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(2 / 16f, 3 / 16f, -1 / 16f, 4 / 16f, 6 / 16f, 10 / 16f, 0, desTex));
                    }
                } else if (half == top && !open) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 12 / 16f, 17 / 16f, 2 / 16f, 3 / 16f, 1, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(2 / 16f, 3 / 16f, 12 / 16f, 17 / 16f, 6 / 16f, 10 / 16f, 0, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 12 / 16f, 17 / 16f, 13 / 16f, 14 / 16f, 1, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(13 / 16f, 14 / 16f, 12 / 16f, 17 / 16f, 6 / 16f, 10 / 16f, 0, desTex));
                    }
                }
            }
            if (design == 2 || design == 3 || design == 4) {
                if (dir == north && open) {
                    //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 13 / 16f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 3 / 16f, 13 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 3 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 3 / 16f, 13 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 13 / 16f, 1f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, 14 / 16f, 15 / 16f, glass, -1));
                } else if (dir == west && open) {
                    //quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 1f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 1f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 1f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 13 / 16f, 1f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 3 / 16f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createCuboid(14 / 16f, 15 / 16f, 3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, glass, -1));
                } else if (dir == east && open) {
                    //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 1f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 3 / 16f, 13 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 13 / 16f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 3 / 16f, 13 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 3 / 16f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 2 / 16f, 3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, glass, -1));
                } else if (dir == south && open) {
                    //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 3 / 16f, mimic,model,extraData,rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 3 / 16f, 13 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 3 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 3 / 16f, 13 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 13 / 16f, 1f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, 1 / 16f, 2 / 16f, glass, -1));
                } else if (half == bottom) {
                    //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 3 / 16f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 3 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 3 / 16f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 3 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 3 / 16f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 1 / 16f, 2 / 16f, 3 / 16f, 13 / 16f, glass, -1));
                } else if (half == top) {
                    //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 13 / 16f, 1f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 13 / 16f, 1f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 13 / 16f, 1f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 13 / 16f, 1f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 13 / 16f, 1f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 13 / 16f, 14 / 16f, 15 / 16f, 3 / 16f, 13 / 16f, glass, -1));
                }
                if (design == 3) {
                    if (dir == north && open) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 13 / 16f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                    } else if (dir == west && open) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 1f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    } else if (dir == east && open) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 1f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    } else if (dir == south && open) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 3 / 16f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 3 / 16f, 13 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 13 / 16f, 7 / 16f, 9 / 16f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    } else if (half == bottom) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 3 / 16f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 0f, 3 / 16f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 13 / 16f, 0f, 3 / 16f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    } else if (half == top) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 13 / 16f, 1f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(7 / 16f, 9 / 16f, 13 / 16f, 1f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 13 / 16f, 13 / 16f, 1f, 7 / 16f, 9 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    }
                }
                if (design == 4) {
                    if (dir == north && open) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 13 / 16f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, 14 / 16f, 15 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    } else if (dir == west && open) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 1f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 15 / 16f, 3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    } else if (dir == east && open) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 1f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(1 / 16f, 2 / 16f, 3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    } else if (dir == south && open) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 3 / 16f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 13 / 16f, 3 / 16f, 13 / 16f, 1 / 16f, 2 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    } else if (half == bottom) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 3 / 16f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 13 / 16f, 1 / 16f, 2 / 16f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    } else if (half == top) {
                        //quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 13 / 16f, 1f, 0f, 1f, mimic,model,extraData,rand, tintIndex, rotation));
                        quads.addAll(ModelHelper.createSixFaceCuboid(3 / 16f, 13 / 16f, 14 / 16f, 15 / 16f, 3 / 16f, 13 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                    }
                }
                if (open && half == bottom) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 13 / 16f, 14 / 16f, 12 / 16f, 13 / 16f, 2, desTex));
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 13 / 16f, 14 / 16f, 1f, 17 / 16f, 2, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(-1 / 16f, 0 / 16f, 13 / 16f, 14 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                        quads.addAll(HandleBakedModel.createHandle(3 / 16f, 4 / 16f, 13 / 16f, 14 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 13 / 16f, 14 / 16f, -1 / 16f, 0 / 16f, 2, desTex));
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 13 / 16f, 14 / 16f, 3 / 16f, 4 / 16f, 2, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(12 / 16f, 13 / 16f, 13 / 16f, 14 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                        quads.addAll(HandleBakedModel.createHandle(1f, 17 / 16f, 13 / 16f, 14 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                    }
                } else if (open && half == top) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 2 / 16f, 3 / 16f, 12 / 16f, 13 / 16f, 2, desTex));
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 2 / 16f, 3 / 16f, 1f, 17 / 16f, 2, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(-1 / 16f, 0f, 2 / 16f, 3 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                        quads.addAll(HandleBakedModel.createHandle(3 / 16f, 4 / 16f, 2 / 16f, 3 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 2 / 16f, 3 / 16f, -1 / 16f, 0, 2, desTex));
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 2 / 16f, 3 / 16f, 3 / 16f, 4 / 16f, 2, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(12 / 16f, 13 / 16f, 2 / 16f, 3 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                        quads.addAll(HandleBakedModel.createHandle(1f, 17 / 16f, 2 / 16f, 3 / 16f, 6 / 16f, 10 / 16f, 3, desTex));
                    }
                }
                if (half == bottom && !open) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, -1 / 16f, 0f, 2 / 16f, 3 / 16f, 1, desTex));
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 3 / 16f, 4 / 16f, 2 / 16f, 3 / 16f, 1, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(13 / 16f, 14 / 16f, -1 / 16f, 0f, 6 / 16f, 10 / 16f, 0, desTex));
                        quads.addAll(HandleBakedModel.createHandle(13 / 16f, 14 / 16f, 3 / 16f, 4 / 16f, 6 / 16f, 10 / 16f, 0, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, -1 / 16f, 0f, 13 / 16f, 14 / 16f, 1, desTex));
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 3 / 16f, 4 / 16f, 13 / 16f, 14 / 16f, 1, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(2 / 16f, 3 / 16f, -1 / 16f, 0f, 6 / 16f, 10 / 16f, 0, desTex));
                        quads.addAll(HandleBakedModel.createHandle(2 / 16f, 3 / 16f, 3 / 16f, 4 / 16f, 6 / 16f, 10 / 16f, 0, desTex));
                    }
                } else if (half == top && !open) {
                    if (dir == north) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 12 / 16f, 13 / 16f, 2 / 16f, 3 / 16f, 1, desTex));
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 1f, 17 / 16f, 2 / 16f, 3 / 16f, 1, desTex));
                    }
                    if (dir == west) {
                        quads.addAll(HandleBakedModel.createHandle(2 / 16f, 3 / 16f, 12 / 16f, 13 / 16f, 6 / 16f, 10 / 16f, 0, desTex));
                        quads.addAll(HandleBakedModel.createHandle(2 / 16f, 3 / 16f, 1f, 17 / 16f, 6 / 16f, 10 / 16f, 0, desTex));
                    }
                    if (dir == south) {
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 12 / 16f, 13 / 16f, 13 / 16f, 14 / 16f, 1, desTex));
                        quads.addAll(HandleBakedModel.createHandle(6 / 16f, 10 / 16f, 1f, 17 / 16f, 13 / 16f, 14 / 16f, 1, desTex));
                    }
                    if (dir == east) {
                        quads.addAll(HandleBakedModel.createHandle(13 / 16f, 14 / 16f, 12 / 16f, 13 / 16f, 6 / 16f, 10 / 16f, 0, desTex));
                        quads.addAll(HandleBakedModel.createHandle(13 / 16f, 14 / 16f, 1f, 17 / 16f, 6 / 16f, 10 / 16f, 0, desTex));
                    }
                }
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                if (dir == north && open) {
                    quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 13 / 16f, 1f, overlayIndex));
                } else if (dir == west && open) {
                    quads.addAll(ModelHelper.createOverlay(13 / 16f, 1f, 0f, 1f, 0f, 1f, overlayIndex));
                } else if (dir == east && open) {
                    quads.addAll(ModelHelper.createOverlay(0f, 3 / 16f, 0f, 1f, 0f, 1f, overlayIndex));
                } else if (dir == south && open) {
                    quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 3 / 16f, overlayIndex));
                } else if (half == bottom) {
                    quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 3 / 16f, 0f, 1f, overlayIndex));
                } else if (half == top) {
                    quads.addAll(ModelHelper.createOverlay(0f, 1f, 13 / 16f, 1f, 0f, 1f, overlayIndex, true, true, true, true, true, true, false));
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
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/oak_planks"));
    }

    @Override
    @Nonnull
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
//========SOLI DEO GLORIA========//