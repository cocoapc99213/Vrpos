package vp.client.vrpos.event.events;

import net.minecraft.network.Packet;
import vp.client.vrpos.event.VrposEvent;

public class PacketEvent extends VrposEvent {
    public Packet<?> packet;
    public PacketEvent(Packet<?> packet){
        this.packet = packet;
    }

    public Packet<?> getPacket(){
        return packet;
    }

    public static class Send extends PacketEvent{
        public Send(Packet<?> packet){
            super(packet);
        }
    }

    public static class Receive extends PacketEvent{
        public Receive(Packet<?> packet){
            super(packet);
        }
    }
}
