package com.wjbaker.ccm.helper;

import java.awt.Desktop;
import java.net.URI;

import net.minecraft.util.Util;

public final class ExternalHelper {

    public boolean openInBrowser(final String url) {
    	if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
    	    try {
				Desktop.getDesktop().browse(new URI(url));
				
		    	return true;
			}
    	    catch (Exception e) {}
    	}
    	
    	return false;
    }
}
