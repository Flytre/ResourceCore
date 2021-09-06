package net.flytre.resource_core.mixin;


import net.flytre.resource_core.RegistryUtils;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(ModelLoader.class)
public class ModelLoaderMixin {

    @Shadow
    @Final
    private ResourceManager resourceManager;

    @Inject(method = "loadModelFromJson", at = @At("HEAD"), cancellable = true)
    public void resource_core$defaultModel(Identifier id, CallbackInfoReturnable<JsonUnbakedModel> cir) {
        if (id.getPath().startsWith("builtin/")) {
            return;
        }

        try {
            this.resourceManager.getResource(new Identifier(id.getNamespace(), "models/" + id.getPath() + ".json"));
        } catch (IOException e) {
            if (!RegistryUtils.getInferredBoilerNamespaces().contains(id.getNamespace()))
                return;

            if (id.getPath().startsWith("item/")) {

                Identifier og = new Identifier(id.getNamespace(), id.getPath().replace("item/", ""));

                if (Registry.BLOCK.containsId(og)) {
                    String json = String.format("""
                            {
                              "parent": "%s"
                            }
                            """, new Identifier(id.getNamespace(), id.getPath().replace("item/", "block/")));
                    JsonUnbakedModel jum = JsonUnbakedModel.deserialize(json);
                    jum.id = id.toString();
                    cir.setReturnValue(jum);
                } else if (Registry.ITEM.containsId(og)) {
                    String json = String.format("""
                            {
                              "parent": "minecraft:item/generated",
                              "textures": {
                                "layer0": "%s"
                              }
                            }
                            """, id);
                    JsonUnbakedModel jum = JsonUnbakedModel.deserialize(json);
                    jum.id = id.toString();
                    cir.setReturnValue(jum);
                }
            } else if (id.getPath().startsWith("block/")) {
                String json = String.format("""
                        {
                          "parent": "minecraft:block/cube_all",
                          "textures": {
                            "all": "%s"
                          }
                        }
                        """, id.toString());
                JsonUnbakedModel jum = JsonUnbakedModel.deserialize(json);
                jum.id = id.toString();
                cir.setReturnValue(jum);
            }
        }
    }
}
