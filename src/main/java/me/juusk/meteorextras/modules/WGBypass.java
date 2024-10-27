package me.juusk.meteorextras.modules;

import me.juusk.meteorextras.MeteorExtras;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class WGBypass extends Module {
    public WGBypass() {
        super(MeteorExtras.CATEGORY, "WGBypass", "Lets you move in WorldGuard protected areas (no idea if this works pls test it)");
    }

    double hspeed = 0.0625D;
    double vspeed = 0.0625D;

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        double[] result = getYaw(mc.player.input.movementForward, mc.player.input.movementSideways);
        float yaw = (float) result[0] + 90;
        double x = 0, y = 0, z = 0;
        mc.player.getAbilities().flying = false;
        mc.player.setVelocity(0, 0, 0);
        if (mc.options.jumpKey.isPressed())
            y = y + vspeed;
        if (mc.options.sneakKey.isPressed())
            y = y - vspeed;
        if (result[1] == 1) {
            x = Math.cos(Math.toRadians(yaw)) * hspeed;
            z = Math.sin(Math.toRadians(yaw)) * hspeed;
        }
        if (mc.options.forwardKey.isPressed() || mc.options.backKey.isPressed() || mc.options.leftKey.isPressed() || mc.options.rightKey.isPressed() || mc.options.jumpKey.isPressed() || mc.options.sneakKey.isPressed()) {
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z, false));
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX() + x, mc.player.getY() - 100, mc.player.getZ() + z, true));
        }
    }
    //Thanks blackout
    private double[] getYaw(double f, double s) {
        double yaw = mc.player.getYaw();
        double move;
        if (f > 0) {
            move = 1;
            yaw += s > 0 ? -45 : s < 0 ? 45 : 0;
        } else if (f < 0) {
            move = 1;
            yaw += s > 0 ? -135 : s < 0 ? 135 : 180;
        } else {
            move = s != 0 ? 1 : 0;
            yaw += s > 0 ? -90 : s < 0 ? 90 : 0;
        }
        return new double[]{yaw, move};
    }
}
