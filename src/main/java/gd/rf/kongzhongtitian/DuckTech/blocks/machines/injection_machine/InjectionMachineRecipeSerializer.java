package gd.rf.kongzhongtitian.DuckTech.blocks.machines.injection_machine;

import gd.rf.kongzhongtitian.DuckTech.api.recipes.InputOutputRecipeSerializer;

public class InjectionMachineRecipeSerializer extends InputOutputRecipeSerializer<InjectionMachineRecipe> {
    public static final InjectionMachineRecipeSerializer INSTANCE = new InjectionMachineRecipeSerializer();

    public InjectionMachineRecipeSerializer() {
        super(data -> new InjectionMachineRecipe(data.inputs, data.outputs, data.id, data.processingTime), 2, 1);
    }

}
