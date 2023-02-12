package com.wjbaker.ccm.config;

import com.wjbaker.ccm.crosshair.CustomCrosshair;
import com.wjbaker.ccm.crosshair.property.BooleanProperty;
import com.wjbaker.ccm.crosshair.property.StringProperty;

public final class GlobalProperties {

    private final CustomCrosshair crosshair;
    private final BooleanProperty isModEnabled;
    
    private final BooleanProperty isLatestVersion;
    private final BooleanProperty isNewVersionNotified;
    private final StringProperty newVersion;

    public GlobalProperties() {
        this.crosshair = new CustomCrosshair();
        this.isModEnabled = new BooleanProperty("mod_enabled", true);
        this.isLatestVersion = new BooleanProperty("is_latest_version", true);
        this.isNewVersionNotified = new BooleanProperty("is_new_version_notified", false);
        this.newVersion = new StringProperty("new_version", null);
    }

    public CustomCrosshair getCrosshair() {
        return this.crosshair;
    }

    public BooleanProperty getIsModEnabled() {
        return this.isModEnabled;
    }

    public BooleanProperty isLatestVersion() {
        return this.isLatestVersion;
    }

    public void setLatestVersion(final boolean isLatestVersion) {
        this.isLatestVersion.set(isLatestVersion);
    }

    public BooleanProperty getIsNewVersionNotified() {
        return this.isNewVersionNotified;
    }

    public StringProperty getNewVersion() {
        return this.newVersion;
    }
}
