package me.juusk.meteorextras.utils;

import net.minecraft.text.Text;

import java.util.UUID;

public class BossBarExtension {
    public UUID uuid;
    public Text name;

    public BossBarExtension(UUID uuid, Text name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }


    public Text getName() {
        return name;
    }

}
