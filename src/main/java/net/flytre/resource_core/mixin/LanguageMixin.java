package net.flytre.resource_core.mixin;

import com.google.gson.JsonObject;
import net.flytre.resource_core.RegistryUtils;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Language;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.text.WordUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.InputStream;
import java.util.function.BiConsumer;

@Mixin(Language.class)
public class LanguageMixin {


    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonObject;entrySet()Ljava/util/Set;"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void resource_core$defaultTranslation(InputStream inputStream, BiConsumer<String, String> entryConsumer, CallbackInfo ci, JsonObject jsonObject) {
        var set = RegistryUtils.getInferredBoilerNamespaces();
        Registry.ITEM.getIds().stream().filter(i -> set.contains(i.getNamespace())).forEach(i -> {
            String key = "item." + i.getNamespace() + "." + i.getPath();
            if (!JsonHelper.hasString(jsonObject, key)) {
                jsonObject.addProperty(key, WordUtils.capitalize(i.getPath().replace("_", " ")));
            }

        });

        Registry.BLOCK.getIds().stream().filter(i -> set.contains(i.getNamespace())).forEach(i -> {
            String key = "block." + i.getNamespace() + "." + i.getPath();
            if (!JsonHelper.hasString(jsonObject, key)) {
                jsonObject.addProperty(key, WordUtils.capitalize(i.getPath().replace("_", " ")));
            }

        });

        Registry.ENTITY_TYPE.getIds().stream().filter(i -> set.contains(i.getNamespace())).forEach(i -> {
            String key = "entity." + i.getNamespace() + "." + i.getPath();
            if (!JsonHelper.hasString(jsonObject, key)) {
                jsonObject.addProperty(key, WordUtils.capitalize(i.getPath().replace("_", " ")));
            }

        });
    }
}
