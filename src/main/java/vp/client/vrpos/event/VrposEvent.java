package vp.client.vrpos.event;

import net.minecraftforge.fml.common.eventhandler.Event;

public class VrposEvent extends Event {
    public boolean canceled = false;

    @Override
    public void setCanceled(boolean cancel) {
        canceled = cancel;
    }

    public void cancel(){
        canceled = true;
    }

    @Override
    public boolean isCanceled(){
        return canceled;
    }
}
