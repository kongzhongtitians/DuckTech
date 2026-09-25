package gd.rf.kongzhongtitian.DuckTech.blocks.machines.essence_conversion_machine;

import gd.rf.kongzhongtitian.DuckTech.api.recipes.InputOutputRecipeSerializer;

public class EssenceConversionMachineRecipeSerializer extends InputOutputRecipeSerializer<EssenceConversionMachineRecipe> {
    public static final EssenceConversionMachineRecipeSerializer INSTANCE = new EssenceConversionMachineRecipeSerializer();

    public EssenceConversionMachineRecipeSerializer() {
        super(data -> new EssenceConversionMachineRecipe(data.inputs, data.outputs, data.id, data.processingTime), 2, 1);
    }

}
