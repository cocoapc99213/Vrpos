package vp.client.vrpos.module.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import vp.client.vrpos.event.events.PacketEvent;
import vp.client.vrpos.module.Category;
import vp.client.vrpos.module.Module;
import vp.client.vrpos.module.Setting;
import vp.client.vrpos.util.CrystalUtil;
import vp.client.vrpos.util.InventoryUtil;
import vp.client.vrpos.util.PlayerUtil;
import vp.client.vrpos.util.Timer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CrystalAura extends Module {
    public Setting<Boolean> place = register(new Setting("Place" , true));
    public Setting<Boolean> predict = register(new Setting("Predict" , true));
    public Setting<Double> placeDelay = register(new Setting("PlaceDelay" , 2.0 , 30.0 , 0.0));
    public Setting<Double> placeRange = register(new Setting("PlaceRange" , 5.0 , 11.0 , 3.0));
    public Setting<Double> wallRangePlace = register(new Setting("WallRangePlace" , 4.0 , 15.0 , 0.0));
    public Setting<Boolean> placeSwing = register(new Setting("PlaceSwing" , false));
    public Setting<Boolean> opPlace = register(new Setting("1.13 Place" , false));
    public Setting<AutoSwitch> autoSwitch = register(new Setting("Switch" , AutoSwitch.AUTO));

    public Setting<Boolean> explode = register(new Setting("Break" , true));
    public Setting<Boolean> predictHit = register(new Setting("PredictHit" , true));
    public Setting<Integer> amount = register(new Setting("Amount" , 5 , 20 , 1 , v -> predictHit.getValue()));
    public Setting<Double> breakDelay = register(new Setting("BreakDelay" , 2.0 , 30.0 , 0.0));
    public Setting<Double> breakRange = register(new Setting("BreakRange" , 5.0 , 15.0 , 3.0));
    public Setting<Double> wallRangeBreak = register(new Setting("WallRangeBreak" , 4.0 , 15.0 , 0.0));
    public Setting<Boolean> packetBreak = register(new Setting("PacketBreak" , true));
    public Setting<Arm> swingArm = register(new Setting("SwingArm" , Arm.Offhand));
    public Setting<Boolean> packetSwing = register(new Setting("PacketSwing" , true , v -> swingArm.getValue() != Arm.None));
    public Setting<Boolean> antiWeakness = register(new Setting("AntiWeakness" , true));

    public Setting<Boolean> ignoreSelf = register(new Setting("IgnoreSelfDamage" , false));
    public Setting<Double> maxSelfDamage = register(new Setting("MaxSelfDamage" , 5.0 , 36.0 , 1.0 , v -> !ignoreSelf.getValue()));
    public Setting<Double> minDamage = register(new Setting("MinDamage" , 5.0 , 36.0 , 0.0));
    public Setting<Boolean> noSuicide = register(new Setting("AntiSuicide" , true));
    public Setting<Double> pauseHealth = register(new Setting("RequireHealth" , 3.0 , 36.0 , 0.0));

    public enum AutoSwitch{
        AUTO ,
        SILENT ,
        NONE
    }

    public enum Arm{
        Mainhand ,
        Offhand ,
        None
    }

    public Timer placeTimer , breakTimer = new Timer();
    public EntityPlayer target = null;
    public int lastedEntityID = -1;

    public CrystalAura(){
        super("CrystalAura" , Category.COMBAT);
    }

    @Override
    public void onTick(){
        doCrystalAura();
    }

    public void doCrystalAura(){
        if(nullCheck()) return;

        target = PlayerUtil.getNearestPlayer(20);
        if(target == null) return;

        if(placeTimer == null) placeTimer = new Timer();
        if(breakTimer == null) breakTimer = new Timer();

        if(pauseHealth.getValue() > mc.player.getHealth()) return;
        if(place.getValue()) placeCrystal();
        if(explode.getValue()) breakCrystal();
    }

    public void placeCrystal(){
        if(placeTimer.passedD(placeDelay.getValue())){
            boolean offhand = mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
            int slot = InventoryUtil.getItemHotbar(Items.END_CRYSTAL);
            if(slot == -1 && !offhand) return;

            List<BlockPos> posList = CrystalUtil.placePostions(placeRange.getValue() , opPlace.getValue());
            List<CrystalPos> crystalPosList = new ArrayList<>();
            for(BlockPos pos : posList){
                double range = PlayerUtil.getDistance(pos);
                if(range > placeRange.getValue()) continue;
                if(!PlayerUtil.canSeePos(pos) && range > wallRangePlace.getValue()) continue;
                double mydamage = CrystalUtil.calculateDamage(pos.getX() + 0.5 , pos.getY() + 1 , pos.getZ() + 0.5 , mc.player);
                if(mydamage > maxSelfDamage.getValue() && !ignoreSelf.getValue()) continue;
                double enemyDamage = CrystalUtil.calculateDamage(pos.getX() + 0.5 , pos.getY() + 1 , pos.getZ() + 0.5 , target);
                if(enemyDamage < minDamage.getValue()) continue;
                if(mydamage > enemyDamage && noSuicide.getValue()) continue;
                CrystalPos cp = new CrystalPos(enemyDamage , pos);
                crystalPosList.add(cp);
            }
            CrystalPos finallyCrystalPos = crystalPosList.stream().max(Comparator.comparing(c -> c.damage)).orElse(null);
            if(finallyCrystalPos == null) return;
            BlockPos placePos = finallyCrystalPos.pos;
            if(!offhand) setItem(slot);
            EnumHand hand = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
            CrystalUtil.placeCrystalOnBlock(placePos , hand , placeSwing.getValue());
            restoreItem();
            //predict hit
            if(predictHit.getValue() && packetBreak.getValue() && lastedEntityID != -1){
                if(mc.player.isPotionActive(MobEffects.WEAKNESS) && antiWeakness.getValue()){
                    int sword = InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD);
                    if(sword == -1) return;
                    setItem(sword);
                }
                for(int i = 0 ; i < amount.getValue(); i++){
                    CPacketUseEntity p = new CPacketUseEntity();
                    p.entityId = lastedEntityID + i + 1;;
                    p.action = CPacketUseEntity.Action.ATTACK;
                    mc.player.connection.sendPacket(p);
                }
            }
        }
    }

    public void breakCrystal(){
        if(breakTimer.passedD(breakDelay.getValue())){
            List<CrystalPos> crystalList = new ArrayList<>();
            for(Entity entity : new ArrayList<>(mc.world.loadedEntityList)){
                if(!(entity instanceof EntityEnderCrystal)) continue;
                double range = PlayerUtil.getDistance(entity);
                if(range > breakRange.getValue()) continue;
                if(!PlayerUtil.canSeePos(entity.getPosition()) && range > wallRangeBreak.getValue()) continue;
                double mydamage = CrystalUtil.calculateDamage(entity.posX , entity.posY , entity.posZ , mc.player);
                if(mc.player.getHealth() - mydamage <= 0 && noSuicide.getValue()) continue;
                if(mydamage > maxSelfDamage.getValue() && !ignoreSelf.getValue()) continue;
                double enemyDamage = CrystalUtil.calculateDamage(entity.posX , entity.posY, entity.posZ , target);
                if(enemyDamage < minDamage.getValue()) continue;
                if(mydamage > enemyDamage && noSuicide.getValue()) continue;
                crystalList.add(new CrystalPos(enemyDamage , entity));
            }
            CrystalPos finallyCrystal = crystalList.stream().max(Comparator.comparing(c -> c.damage)).orElse(null);
            if(finallyCrystal == null) return;
            if(mc.player.isPotionActive(MobEffects.WEAKNESS) && antiWeakness.getValue()){
                int sword = InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD);
                if(sword == -1) return;
                setItem(sword);
            }
            Entity crystal = finallyCrystal.crystal;
            if(packetBreak.getValue()){
                mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
            }
            else{
                mc.playerController.attackEntity(mc.player , crystal);
            }
            swing();
        }
    }

    //predict
    @Override
    public void onPacketReceive(PacketEvent.Receive event){
        if(event.getPacket() instanceof SPacketSpawnObject
                && ((SPacketSpawnObject) event.getPacket()).getType() == 51 && explode.getValue() && predict.getValue() && packetBreak.getValue()){
            SPacketSpawnObject packet = ((SPacketSpawnObject)event.getPacket());
            lastedEntityID = packet.getEntityID();
            BlockPos pos = new BlockPos(packet.getX() , packet.getY() , packet.getZ());
            double range = PlayerUtil.getDistance(pos);
            if(range > breakRange.getValue()) return;
            if(!PlayerUtil.canSeePos(pos) && range > wallRangeBreak.getValue()) return;
            double mydamage = CrystalUtil.calculateDamage(pos.getX() , pos.getY() , pos.getZ() , mc.player);
            if(mc.player.getHealth() - mydamage <= 0 && noSuicide.getValue()) return;
            if(mydamage > maxSelfDamage.getValue() && !ignoreSelf.getValue()) return;
            double enemyDamage = CrystalUtil.calculateDamage(pos.getX() , pos.getY(), pos.getZ(), target);
            if(enemyDamage < minDamage.getValue()) return;
            if(mydamage > enemyDamage && noSuicide.getValue()) return;
            //break
            CPacketUseEntity p = new CPacketUseEntity();
            p.entityId = packet.getEntityID();
            p.action = CPacketUseEntity.Action.ATTACK;
            mc.player.connection.sendPacket(p);
            swing();
        }
        if(event.getPacket() instanceof SPacketSpawnExperienceOrb){
            lastedEntityID = ((SPacketSpawnExperienceOrb)event.getPacket()).getEntityID();
        }
        if(event.getPacket() instanceof SPacketSpawnMob){
            lastedEntityID = ((SPacketSpawnMob)event.getPacket()).getEntityID();
        }
        if(event.getPacket() instanceof SPacketSpawnPainting){
            lastedEntityID = ((SPacketSpawnPainting)event.getPacket()).getEntityID();
        }
        if(event.getPacket() instanceof SPacketSpawnPlayer){
            lastedEntityID = ((SPacketSpawnPlayer)event.getPacket()).getEntityID();
        }
    }

    public void swing(){
        EnumHand hand = swingArm.getValue() == Arm.Mainhand ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
        if(swingArm.getValue() != Arm.None) {
            if(packetSwing.getValue())
                mc.player.connection.sendPacket(new CPacketAnimation(hand));
            else
                mc.player.swingArm(hand);
        }
    }

    public int oldslot = -1;
    public EnumHand oldhand = null;
    public void setItem(int slot)
    {
        if(autoSwitch.getValue() == AutoSwitch.SILENT) {
            oldhand = null;
            if(mc.player.isHandActive()) {
                oldhand = mc.player.getActiveHand();
            }
            oldslot = mc.player.inventory.currentItem;
            mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        }
        else if(autoSwitch.getValue() == AutoSwitch.AUTO) {
            mc.player.inventory.currentItem = slot;
            mc.playerController.updateController();
        }
    }
    public void restoreItem()
    {
        if(oldslot != -1 && autoSwitch.getValue() == AutoSwitch.SILENT)
        {
            if(oldhand != null) {
                mc.player.setActiveHand(oldhand);
            }
            mc.player.connection.sendPacket(new CPacketHeldItemChange(oldslot));
            oldslot = -1;
            oldhand = null;
        }
    }

    public class CrystalPos{
        public double damage;
        public Entity crystal;
        public BlockPos pos;

        public CrystalPos(double damage , BlockPos pos){
            this.damage = damage;
            this.pos = pos;
        }

        public CrystalPos(double damage , Entity crystal) {
            this.damage = damage;
            this.crystal = crystal;
        }
    }
}
