package com.bnmla.advideos.Entities;

/**
 * Created by nay on 2/26/16.
 */
public class Setting {
    private int index, width, height;
    private String name, primary, aspect_ratio;
    private boolean autostart, mute, controls, repeat;

    public Setting(int width, int height, boolean autostart,
                   boolean mute, boolean controls, boolean repeat, String primary) {
        this.width = width;
        this.height = height;
        this.autostart = autostart;
        this.mute = mute;
        this.controls = controls;
        this.repeat = repeat;
        this.primary = primary;
    }

    public Setting(int index, String name, int width, int height, boolean autostart,
                   boolean mute, boolean controls, boolean repeat, String primary,
                   String aspect_ratio) {
        this.index = index;
        this.name = name;
        this.width = width;
        this.height = height;
        this.autostart = autostart;
        this.mute = mute;
        this.controls = controls;
        this.repeat = repeat;
        this.primary = primary;
        this.aspect_ratio = aspect_ratio;
    }

    public int getIndex() {
        return this.index;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getName() {
        return this.name;
    }

    public String getPrimary() {
        return this.primary;
    }

    public boolean isAutostart() {
        return this.autostart;
    }

    public boolean isMute() {
        return this.mute;
    }

    public boolean isControls() {
        return this.controls;
    }

    public boolean isRepeat() {
        return this.repeat;
    }

    public String getAspectRatio() {
        return this.aspect_ratio;
    }
}
