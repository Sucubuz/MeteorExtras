package me.juusk.meteorextras.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.orbit.EventHandler;
import me.juusk.meteorextras.MeteorExtras;
import meteordevelopment.meteorclient.settings.*;


public class FlightPlus extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();


    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("Speed")
        .description("How fast it should go")
        .defaultValue(1)
        .min(0)
        .sliderMax(100)
        .build()
    );
    private final Setting<Double> ySpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("Y Speed")
        .description("How fast it should go on y axis")
        .defaultValue(1)
        .min(0)
        .sliderMax(100)
        .build()
    );
    private final Setting<Double> antiKickDelay = sgGeneral.add(new DoubleSetting.Builder()
        .name("anti-kick-delay")
        .description("Amount of time to wait before doing an anti-kick")
        .defaultValue(10)
        .min(0)
        .sliderMax(1000)
        .build()
    );
    private final Setting<Double> antiKickAmount = sgGeneral.add(new DoubleSetting.Builder()
        .name("anti-kick-amount")
        .description("How it should move with an anti-kick")
        .defaultValue(1)
        .min(0)
        .sliderMax(100)
        .build()
    );

    public FlightPlus() {
        super(MeteorExtras.CATEGORY, "Flight+", "Better meteor flight");
    }
    public int tick = 0;

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if(Utils.canUpdate()) {
            double[] result = getYaw(mc.player.input.movementForward, mc.player.input.movementSideways);
            float yaw = (float) result[0] + 90;
            double x = 0, y = tick % antiKickDelay.get() == 0 ? antiKickAmount.get() * -0.04 : 0, z = 0;
            mc.player.getAbilities().flying = false;
            mc.player.setVelocity(0, 0, 0);
            if (mc.options.jumpKey.isPressed())
                y = y + ySpeed.get();
            if (mc.options.sneakKey.isPressed())
                y = y - ySpeed.get();
            if (result[1] == 1) {
                x = Math.cos(Math.toRadians(yaw)) * speed.get();
                z = Math.sin(Math.toRadians(yaw)) * speed.get();
            }

            mc.player.setVelocity(x, y, z);
        }
        tick++;

    }

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
