package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
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
 * @version 1.7 05/30/22
 */
public class EdgedSlopeBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            if (state != null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                return getMimicQuads(state, side, rand, extraData, model);
            }
        }
        return Collections.emptyList();
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, IModelData extraData, IBakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && state != null) {
            List<TextureAtlasSprite> texture = TextureHelper.getTextureFromModel(model, extraData, rand);
            int index = extraData.getData(FrameBlockTile.TEXTURE);
            if (index >= texture.size()) {
                index = 0;
            }
            if (texture.size() == 0) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.block_not_available"), true);
                }
                return Collections.emptyList();
            }
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            List<BakedQuad> quads = new ArrayList<>();
            Half half = state.get(StairsBlock.HALF);
            Half lower = Half.BOTTOM;
            Half upper = Half.TOP;
            Direction direction = state.get(StairsBlock.FACING);
            Direction north = Direction.NORTH;
            Direction east = Direction.EAST;
            Direction south = Direction.SOUTH;
            Direction west = Direction.WEST;
            StairsShape shape = state.get(StairsBlock.SHAPE);
            StairsShape straight = StairsShape.STRAIGHT;
            StairsShape innerLeft = StairsShape.INNER_LEFT;
            StairsShape innerRight = StairsShape.INNER_RIGHT;
            StairsShape outerLeft = StairsShape.OUTER_LEFT;
            StairsShape outerRight = StairsShape.OUTER_RIGHT;

            //Don't even try to understand what's happening here
            //If any of it broke, it's easier to completely rewrite it than to find the problem
            //All the boolean expressions are used to determine whether a certain face of the voxel is visible or not
            //e.g.: "z == 0" for parameter "north" means:
            //      All voxels at z == 0 will have a visible north face
            //e.g.: "z == 15 - y" for parameter "up" means:
            //      All voxels with a z-value equal to "15 minus the current y-value" will have a visible top face
            if (half == lower) {
                //iterate through each of the 4096 voxels and check, which sides are visible, then add to quads
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            if (shape == straight) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 - y, x == 15 && z <= 15 - y, x == 0 && z <= 15 - y, z == 15 - y, y == 0));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= y, z == 15 && x >= y, x == 15, x == y, x == y, y == 0));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y, z == 15, x == 15 && z >= y, x == 0 && z >= y, z == y, y == 0));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= 15 - y, z == 15 && x <= 15 - y, x == 15 - y, x == 0, x == 15 - y, y == 0));
                                }
                            }
                            if (shape == innerLeft) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 - y && z < x || z == 15 && x <= 15 - y, x == 15 && z <= 15 - y || x == 15 - y && x < z, x == 0, z == 15 - y && z <= x || x == 15 - y && x <= z, y == 0));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 && x >= y || z == 15 - y && z < 15 - x, x == 15, x == y && x > 15 - z || x == 0 && z <= 15 - y, z == 15 - y && z <= 15 - x || x == y && x >= 15 - z, y == 0));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && z > x || z == 0 && x >= y, z == 15, x == 15, x == 0 && z >= y || x == y && x > z, z == y && z >= x || x == y && x >= z, y == 0));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= 15 - y || z == y && z > 15 - x, z == 15, x == 15 - y && x < 15 - z || x == 15 && z >= y, x == 0, x == 15 - y && x <= 15 - z || z == y && z > 15 - x, y == 0));
                                }
                            }
                            if (shape == innerRight) {
                                if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 - y && z < x || z == 15 && x <= 15 - y, x == 15 && z <= 15 - y || x == 15 - y && x < z, x == 0, z == 15 - y && z <= x || x == 15 - y && x <= z, y == 0));
                                } else if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 && x >= y || z == 15 - y && z < 15 - x, x == 15, x == y && x > 15 - z || x == 0 && z <= 15 - y, z == 15 - y && z <= 15 - x || x == y && x >= 15 - z, y == 0));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && z > x || z == 0 && x >= y, z == 15, x == 15, x == 0 && z >= y || x == y && x > z, z == y && z >= x || x == y && x >= z, y == 0));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= 15 - y || z == y && z > 15 - x, z == 15, x == 15 - y && x < 15 - z || x == 15 && z >= y, x == 0, x == 15 - y && x <= 15 - z || z == y && z > 15 - x, y == 0));
                                }
                            }
                            if (shape == outerLeft) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= 15 - y, z == 15 - y && x <= 15 - y, x == 15 - y && z <= 15 - y, x == 0 && z <= 15 - y, z == 15 - y && z >= x || x == 15 - y && x >= z, y == 0));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= y, z == 15 - y && x >= y, x == 15 && z <= 15 - y, x == y && x <= 15 - z, x == y && x <= 15 - z || z == 15 - y && z >= 15 - x, y == 0));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && z <= x, z == 15 && x >= y, x == 15 && z >= y, x == y && z >= y, z == y && z <= x || x == y && x <= z, y == 0));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && x <= 15 - y, z == 15 && x <= 15 - y, x == 15 - y && x >= 15 - z, x == 0 && z >= y, x == 15 - y && x >= 15 - z || z == y && z < 15 - x, y == 0));
                                }
                            }
                            if (shape == outerRight) {
                                if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= 15 - y, z == 15 - y && x <= 15 - y, x == 15 - y && z <= 15 - y, x == 0 && z <= 15 - y, z == 15 - y && z >= x || x == 15 - y && x >= z, y == 0));
                                } else if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= y, z == 15 - y && x >= y, x == 15 && z <= 15 - y, x == y && x <= 15 - z, x == y && x <= 15 - z || z == 15 - y && z >= 15 - x, y == 0));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && z <= x, z == 15 && x >= y, x == 15 && z >= y, x == y && z >= y, z == y && z <= x || x == y && x <= z, y == 0));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && x <= 15 - y, z == 15 && x <= 15 - y, x == 15 - y && x >= 15 - z, x == 0 && z >= y, x == 15 - y && x >= 15 - z || z == y && z < 15 - x, y == 0));
                                }
                            }
                        }
                    }
                }
            }


            if (half == upper) {
                //iterate through each of the 4096 voxels and check, which sides are visible, then add to quads
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            if (shape == straight) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == y, x == 15 && z <= y, x == 0 && z <= y, y == 15, z == y));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= 15 - y, z == 15 && x >= 15 - y, x == 15, x == 15 - y, y == 15, x == 15 - y));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y, z == 15, x == 15 && z >= 15 - y, x == 0 && z >= 15 - y, y == 15, z == 15 - y));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= y, z == 15 && x <= y, x == y, x == 0, y == 15, x == y));
                                }
                            }
                            if (shape == innerLeft) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == y && z < x || z == 15 && x <= y, x == 15 && z <= y || x == y && x < z, x == 0, y == 15, z == y && z <= x || x == y && x <= z));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 && x >= 15 - y || z == y && z < 15 - x, x == 15, x == 15 - y && x > 15 - z || x == 0 && z <= y, y == 15, z == y && z <= 15 - x || x == 15 - y && x >= 15 - z));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && z > x || z == 0 && x >= 15 - y, z == 15, x == 15, x == 0 && z >= 15 - y || x == 15 - y && x > z, y == 15, z == 15 - y && z >= x || x == 15 - y && x >= z));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= y || z == 15 - y && z > 15 - x, z == 15, x == y && x < 15 - z || x == 15 && z >= 15 - y, x == 0, y == 15, x == y && x <= 15 - z || z == 15 - y && z > 15 - x));
                                }
                            }
                            if (shape == innerRight) {
                                if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == y && z < x || z == 15 && x <= y, x == 15 && z <= y || x == y && x < z, x == 0, y == 15, z == y && z <= x || x == y && x <= z));
                                } else if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 && x >= 15 - y || z == y && z < 15 - x, x == 15, x == 15 - y && x > 15 - z || x == 0 && z <= y, y == 15, z == y && z <= 15 - x || x == 15 - y && x >= 15 - z));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && z > x || z == 0 && x >= 15 - y, z == 15, x == 15, x == 0 && z >= 15 - y || x == 15 - y && x > z, y == 15, z == 15 - y && z >= x || x == 15 - y && x >= z));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= y || z == 15 - y && z > 15 - x, z == 15, x == y && x < 15 - z || x == 15 && z >= 15 - y, x == 0, y == 15, x == y && x <= 15 - z || z == 15 - y && z > 15 - x));
                                }
                            }
                            if (shape == outerLeft) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= y, z == y && x <= y, x == y && z <= y, x == 0 && z <= y, y == 15, z == y && z >= x || x == y && x >= z));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= 15 - y, z == y && x >= 15 - y, x == 15 && z <= y, x == 15 - y && x <= 15 - z, y == 15, x == 15 - y && x <= 15 - z || z == y && z >= 15 - x));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && z <= x, z == 15 && x >= 15 - y, x == 15 && z >= 15 - y, x == 15 - y && z >= 15 - y, y == 15, z == 15 - y && z <= x || x == 15 - y && x <= z));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && x <= y, z == 15 && x <= y, x == y && x >= 15 - z, x == 0 && z >= 15 - y, y == 15, x == y && x >= 15 - z || z == 15 - y && z < 15 - x));
                                }
                            }
                            if (shape == outerRight) {
                                if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= y, z == y && x <= y, x == y && z <= y, x == 0 && z <= y, y == 15, z == y && z >= x || x == y && x >= z));
                                } else if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= 15 - y, z == y && x >= 15 - y, x == 15 && z <= y, x == 15 - y && x <= 15 - z, y == 15, x == 15 - y && x <= 15 - z || z == y && z >= 15 - x));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && z <= x, z == 15 && x >= 15 - y, x == 15 && z >= 15 - y, x == 15 - y && z >= 15 - y, y == 15, z == 15 - y && z <= x || x == 15 - y && x <= z));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && x <= y, z == 15 && x <= y, x == y && x >= 15 - z, x == 0 && z >= 15 - y, y == 15, x == y && x >= 15 - z || z == 15 - y && z < 15 - x));
                                }
                            }
                        }
                    }
                }
            }
            return quads;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean func_230044_c_() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return getTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }
}
//========SOLI DEO GLORIA========//