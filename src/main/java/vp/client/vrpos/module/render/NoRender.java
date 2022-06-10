package vp.client.vrpos.module.render;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import vp.client.vrpos.event.events.PacketEvent;
import vp.client.vrpos.module.Category;
import vp.client.vrpos.module.Module;
import vp.client.vrpos.module.Setting;

public class NoRender extends Module {
    public Setting<Boolean> explosion = register(new Setting("Explosion" , true));
    public Setting<Boolean> experienceOrb = register(new Setting("ExperienceOrb" , true));

    public NoRender(){
        super("NoRender" , Category.RENDER);
    }

    @Override
    public void onPacketReceive(PacketEvent.Receive event) {
        Packet<?> packet = event.packet;
        if ((packet instanceof SPacketSpawnExperienceOrb)
                || (packet instanceof SPacketExplosion)) {
            event.cancel();
        }
    }
}
