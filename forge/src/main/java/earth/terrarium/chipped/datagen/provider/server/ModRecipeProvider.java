package earth.terrarium.chipped.datagen.provider.server;

import earth.terrarium.chipped.common.recipe.ChippedRecipe;
import earth.terrarium.chipped.datagen.builder.ChippedRecipeBuilder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

@MethodsReturnNonnullByDefault
public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pGenerator) {
        super(pGenerator);
    }

    public static void createSimpleChippedRecipe(Consumer<FinishedRecipe> consumer, RecipeSerializer<ChippedRecipe> serializer, Item workbench, List<String> tags) {
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(workbench.asItem()));
        ChippedRecipeBuilder builder = new ChippedRecipeBuilder(serializer, workbench, tags)
            .unlockedBy("has_" + id.getPath(), has(workbench));
        builder.save(consumer, new ResourceLocation(id.getNamespace(), id.getPath()));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        Map<TagKey<Item>, List<String>> workbenchTags = new HashMap<>();

        ModBlockTagProvider.registerTags((block, registry, name, workbench) -> {
            List<String> tags = workbenchTags.getOrDefault(workbench, new ArrayList<>());
            tags.add(name);
            workbenchTags.put(workbench, tags);
        });
        workbenchTags.forEach((tag, blocks) -> createSimpleChippedRecipe(consumer, (RecipeSerializer<ChippedRecipe>) ForgeRegistries.RECIPE_SERIALIZERS.getValue(tag.location()), Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(tag.location())), blocks));
    }
}
