package vp.client.vrpos.mixin.mixins;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vp.client.vrpos.Vrpos;

@Mixin(value = Minecraft.class)
public class MixinMinecraft {
    @Inject(method = {"shutdownMinecraftApplet"}, at = {@At(value = "HEAD")})
    private void stopClient(CallbackInfo callbackInfo) {
        Vrpos.INSTANCE.unload();
    }
}
