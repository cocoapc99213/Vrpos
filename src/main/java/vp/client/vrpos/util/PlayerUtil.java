package vp.client.vrpos.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

public class PlayerUtil implements Util{
    public static EntityPlayer getNearestPlayer(double range)
    {
        //search nearest player
        return mc.world.playerEntities.stream().filter(p -> mc.player.getDistance(p) <= range)
                .filter(p -> mc.player.getEntityId() != p.getEntityId())
                .min(Comparator.comparing(p -> mc.player.getDistance(p)))
                .orElse(null);
    }

    public static double getDistance(Entity entity)
    {
        return mc.player.getDistance(entity);
    }

    public static double getDistance(BlockPos pos)
    {
        return mc.player.getDistance(pos.getX() , pos.getY() , pos.getZ());
    }

    public static boolean canSeePos(final BlockPos pos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) == null;
    }
}
