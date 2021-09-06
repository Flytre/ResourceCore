package net.flytre.resource_core.mixin;


import net.flytre.resource_core.FakeBlockStateResource;
import net.flytre.resource_core.RegistryUtils;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(NamespaceResourceManager.class)
public class NamespaceResourceManagerMixin {

    @Inject(method = "getAllResources", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    public void resource_core$createBlockstate(Identifier id, CallbackInfoReturnable<List<Resource>> cir, List<Resource> list) {
        if (list.isEmpty() && id.getPath().startsWith("blockstates") && RegistryUtils.getInferredBoilerNamespaces().contains(id.getNamespace())) {
            list.add(new FakeBlockStateResource(id));
        }
    }
}
