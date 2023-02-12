package com.wjbaker.ccm.crosshair.render;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.wjbaker.ccm.crosshair.CustomCrosshair;
import com.wjbaker.ccm.type.RGBA;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;
import java.util.Set;

public final class ComputedProperties {

    private final Minecraft mc;
    private final CustomCrosshair crosshair;

    private final int gap;
    private final RGBA colour;
    private final boolean isVisible;

    private final Set<Item> rangedWeapons = ImmutableSet.<Item>of(
        Items.bow
    );

    private final Set<Item> throwableItems = ImmutableSet.of(
        Items.ender_pearl,
        Items.ender_eye
    );

    private final Map<Item, Float> usageItemsDurations = ImmutableMap.<Item, Float>of(
        Items.bow, 20.0F
    );

    private final Set<Item> attackableItems = ImmutableSet.of(
        Items.wooden_sword,
        Items.golden_sword,
        Items.stone_sword,
        Items.iron_sword,
        Items.diamond_sword,

        Items.wooden_axe,
        Items.golden_axe,
        Items.stone_axe,
        Items.iron_axe,
        Items.diamond_axe,

        Items.wooden_shovel,
        Items.golden_shovel,
        Items.stone_shovel,
        Items.iron_shovel,
        Items.diamond_shovel,

        Items.wooden_pickaxe,
        Items.golden_pickaxe,
        Items.stone_pickaxe,
        Items.iron_pickaxe,
        Items.diamond_pickaxe,

        Items.wooden_hoe,
        Items.golden_hoe,
        Items.stone_hoe,
        Items.iron_hoe,
        Items.diamond_hoe
    );

    public ComputedProperties(final CustomCrosshair crosshair) {
        this.mc = Minecraft.getMinecraft();
        this.crosshair = crosshair;

        this.gap = this.calculateGap();
        this.colour = this.calculateColour();
        this.isVisible = this.calculateIsVisible();
    }

    public int gap() {
        return this.gap;
    }

    public RGBA colour() {
        return this.colour;
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    private int calculateGap() {
        int baseGap = this.crosshair.gap.get();

        if (this.mc.thePlayer == null)
            return baseGap;

        boolean isSpectator = this.mc.thePlayer.isSpectator();

        boolean isHoldingItem = this.mc.thePlayer.getHeldItem() != null;

        boolean isDynamicBowEnabled = this.crosshair.isDynamicBowEnabled.get();

        if (!isSpectator && isHoldingItem && isDynamicBowEnabled) {
            float gapModifier = 2;
            Float usageItem = this.usageItemsDurations.get(this.mc.thePlayer.getHeldItem().getItem());
            float currentUsage = this.mc.thePlayer.getItemInUseDuration();

            if (usageItem != null && currentUsage > 0.0F) {
                float progress = Math.max(0.0F, usageItem - currentUsage);

                return baseGap + Math.round(progress * gapModifier);
            }
        }

        return baseGap;
    }

    private RGBA calculateColour() {
        Entity target = this.mc.pointedEntity;

        boolean isHighlightPlayersEnabled = this.crosshair.isHighlightPlayersEnabled.get();
        if (isHighlightPlayersEnabled && target instanceof EntityPlayer) {
            return this.crosshair.highlightPlayersColour.get();
        }

        boolean isHighlightHostilesEnabled = this.crosshair.isHighlightHostilesEnabled.get();
        if (isHighlightHostilesEnabled && target instanceof IMob) {
            return this.crosshair.highlightHostilesColour.get();
        }

        boolean isHighlightPassivesEnabled = this.crosshair.isHighlightPassivesEnabled.get();
        if (isHighlightPassivesEnabled && target instanceof IAnimals) {
            return this.crosshair.highlightPassivesColour.get();
        }

        if (this.crosshair.isRainbowEnabled.get())
            return this.getRainbowColour();

        return this.crosshair.colour.get();
    }

    private RGBA getRainbowColour() {
        int ticks = this.crosshair.rainbowTicks.get() + 1;

        if (ticks > 125000)
            ticks = 0;

        this.crosshair.rainbowTicks.set(ticks);

        int opacity = this.crosshair.colour.get().getOpacity();
        int speed = this.crosshair.rainbowSpeed.get();

        return new RGBA(255, 255, 255, opacity)
            .setRed(this.getRainbowColourComponent(ticks, 0.0F, speed))
            .setGreen(this.getRainbowColourComponent(ticks, 2.0F, speed))
            .setBlue(this.getRainbowColourComponent(ticks, 4.0F, speed));
    }

    private int getRainbowColourComponent(final int ticks, final float offset, final int speed) {
        return (int)(Math.sin((ticks * speed / 20000.0F) + offset) * 127 + 128);
    }

    private boolean calculateIsVisible() {
        if (this.mc.thePlayer == null)
            return false;

        if (!this.crosshair.isVisibleDefault.get())
            return false;

        if (!this.crosshair.isVisibleHiddenGui.get() && this.mc.gameSettings.hideGUI)
            return false;

        int pov = this.mc.gameSettings.thirdPersonView;
        boolean isThirdPerson = pov == 1 || pov == 2;
        if (!this.crosshair.isVisibleThirdPerson.get() && isThirdPerson)
            return false;

        if (!this.crosshair.isVisibleDebug.get() && this.mc.gameSettings.showDebugInfo)
            return false;

        if (!this.crosshair.isVisibleSpectator.get() && this.mc.thePlayer.isSpectator())
            return false;

        if (!this.crosshair.isVisibleHoldingRangedWeapon.get() && this.isHoldingItem(this.mc.thePlayer, this.rangedWeapons))
            return false;

        if (!this.crosshair.isVisibleHoldingThrowableItem.get() && this.isHoldingItem(this.mc.thePlayer, this.throwableItems))
            return false;

        return true;
    }

    private boolean isHoldingItem(final EntityPlayerSP player, final Set<Item> items) {
    	ItemStack heldItem = this.mc.thePlayer.getHeldItem();
    	if (heldItem == null)
    		return false;
    	
        return items.contains(this.mc.thePlayer.getHeldItem().getItem());
    }
}
