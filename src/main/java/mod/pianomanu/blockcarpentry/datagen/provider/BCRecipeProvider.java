package mod.pianomanu.blockcarpentry.datagen.provider;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.data.*;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class BCRecipeProvider extends RecipeProvider {
    public BCRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    //TODO if FALLING_FRAMEBLOCK is fixed or removed, remove here too!
    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        System.out.println("HELLLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
        ShapedRecipeBuilder.shapedRecipe(Registration.FRAMEBLOCK.get(),8).key('#', Items.SCAFFOLDING).patternLine("###").patternLine("# #").patternLine("###").addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Registration.SLAB_FRAMEBLOCK.get(),3).key('#', Registration.FRAMEBLOCK.get()).patternLine("###").addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Registration.STAIRS_FRAMEBLOCK.get(),4).key('#', Registration.FRAMEBLOCK.get()).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Registration.BUTTON_FRAMEBLOCK.get(),1).key('#', Registration.FRAMEBLOCK.get()).patternLine("#").addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING)).build(consumer);
        ShapedRecipeBuilder.shapedRecipe(Registration.PRESSURE_PLATE_FRAMEBLOCK.get(),1).key('#', Registration.FRAMEBLOCK.get()).patternLine("##").addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING)).build(consumer);
        //ShapedRecipeBuilder.shapedRecipe(Registration.FALLING_FRAMEBLOCK.get(),1).key('#', Registration.FRAMEBLOCK.get()).key('S', Items.SAND).patternLine("#").addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING));
        //ShapelessRecipeBuilder.shapelessRecipe(Registration.FALLING_FRAMEBLOCK.get()).addIngredient(Registration.FRAMEBLOCK.get()).addIngredient(Items.SAND).addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(Registration.FRAMEBLOCK.get()).addIngredient(Registration.BUTTON_FRAMEBLOCK.get()).addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(Registration.FRAMEBLOCK.get()).addIngredient(Registration.SLAB_FRAMEBLOCK.get(),2).addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(Registration.FRAMEBLOCK.get(),3).addIngredient(Registration.STAIRS_FRAMEBLOCK.get(),2).addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING)).build(consumer);
        ShapelessRecipeBuilder.shapelessRecipe(Registration.FRAMEBLOCK.get(),2).addIngredient(Registration.PRESSURE_PLATE_FRAMEBLOCK.get()).addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING)).build(consumer);
        //ShapelessRecipeBuilder.shapelessRecipe(Registration.FRAMEBLOCK.get()).addIngredient(Registration.FALLING_FRAMEBLOCK.get()).addCriterion("has_scaffolding", this.hasItem(Items.SCAFFOLDING)).build(consumer);
    }
}
