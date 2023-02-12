package com.wjbaker.ccm.crosshair.property;

public final class StringProperty extends ICrosshairProperty<String> {

    public StringProperty(final String alias, final String value) {
        super(alias, value);
    }

    @Override
    public String forConfig() {
        return this.get();
    }

    @Override
    public void setFromConfig(final String value) {
        this.set(value);
    }
}
