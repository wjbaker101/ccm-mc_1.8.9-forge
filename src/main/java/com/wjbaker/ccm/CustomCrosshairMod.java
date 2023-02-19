package com.wjbaker.ccm;

import static org.apache.logging.log4j.LogManager.getLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.wjbaker.ccm.config.ConfigManager;
import com.wjbaker.ccm.config.GlobalProperties;
import com.wjbaker.ccm.crosshair.property.ICrosshairProperty;
import com.wjbaker.ccm.crosshair.render.CrosshairRenderManager;
import com.wjbaker.ccm.helper.RequestHelper;
import com.wjbaker.ccm.render.gui.screen.screens.editCrosshair.EditCrosshairGuiScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = "custom-crosshair-mod", clientSideOnly = true)
@SideOnly(value = Side.CLIENT)
public final class CustomCrosshairMod {

    public static CustomCrosshairMod INSTANCE;

    public static final String TITLE = "Custom Crosshair Mod";
    public static final String VERSION = "1.3.0-forge";
    public static final String MC_VERSION = "1.8.9-forge";
    public static final String CURSEFORGE_PAGE = "https://www.curseforge.com/projects/242995/";
    public static final String MC_FORUMS_PAGE = "https://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2637819/";
    public static final String PATREON_PAGE = "https://www.patreon.com/bePatron?u=66431720";
    public static final String PAYPAL_PAGE = "https://www.paypal.com/cgi-bin/webscr?return=https://www.curseforge.com/projects/242995&cn=Add+special+instructions+to+the+addon+author()&business=sparkless101%40gmail.com&bn=PP-DonationsBF:btn_donateCC_LG.gif:NonHosted&cancel_return=https://www.curseforge.com/projects/242995&lc=US&item_name=Custom+Crosshair+Mod+(from+curseforge.com)&cmd=_donations&rm=1&no_shipping=1&currency_code=USD";

    private static final KeyBinding EDIT_CROSSHAIR_KEY_BINDING = new KeyBinding("Open GUI", Keyboard.KEY_GRAVE, "Custom Crosshair Mod");

    private final Logger logger;
    private final GlobalProperties properties;
    private final CrosshairRenderManager crosshairRenderManager;

    private ConfigManager configManager;

    public CustomCrosshairMod() {
        this.logger = getLogger(CustomCrosshairMod.class);
        this.properties = new GlobalProperties();
        this.crosshairRenderManager = new CrosshairRenderManager(this.properties.getCrosshair());

        INSTANCE = this;

        this.loadConfig();
        this.checkVersionAsync();

        ClientRegistry.registerKeyBinding(EDIT_CROSSHAIR_KEY_BINDING);
    }
    
    @EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(this);
	}

    private void loadConfig() {
        List<ICrosshairProperty<?>> configProperties = this.properties.getCrosshair().propertiesAsList;
        configProperties.add(this.properties.getIsModEnabled());
        
        Map<String, ICrosshairProperty<?>> propertiesAsMap = new HashMap<String, ICrosshairProperty<?>>();
        for (ICrosshairProperty<?> property : configProperties) {
        	propertiesAsMap.put(property.alias(), property);
        }
        
        this.configManager = new ConfigManager("crosshair_config.ccmcfg", propertiesAsMap);

        if (!this.configManager.read()) {
            if (!this.configManager.write()) {
                this.error("Config Manager (Load)", "Unable to load or write config.");
            }
        }
    }
    
    private void checkVersionAsync() {
        Executors.newSingleThreadExecutor().submit(new Callable<Boolean>() {
        	@Override
        	public Boolean call() throws Exception {
        		checkVersion();
        		return true;
        	}
		});
    }

    private void checkVersion() {
    	BufferedReader reader = null;
        try {
        	reader = new RequestHelper().get("https://pastebin.com/raw/B2sL8QCh");
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] lineSplit = currentLine.split(" ");

                if (lineSplit.length != 2)
                    continue;

                String mcVersion = lineSplit[0];
                String expectedModVersion = lineSplit[1];

                if (mcVersion.equals(MC_VERSION) && !expectedModVersion.equals(VERSION)) {
                    this.log("Version Checker", "Not using latest version of Customer Crosshair Mod.");
                    this.properties.setLatestVersion(false);
                    this.properties.getNewVersion().set(expectedModVersion);
                }
            }
        }
        catch (final IOException e) {
            this.error("Version Checker", "Unable to check the version.");
        }
        finally {
        	if (reader != null) {
				try {
					reader.close();
				}
        		catch (Exception e) {}
        	}
        }
    }

    @SubscribeEvent
    public void onClientTickEvent(final TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().currentScreen == null && EDIT_CROSSHAIR_KEY_BINDING.isPressed())
            Minecraft.getMinecraft().displayGuiScreen(new EditCrosshairGuiScreen());
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onRenderCrosshair(final RenderGameOverlayEvent.Pre event) {
    	if (event.type != ElementType.CROSSHAIRS)
    		return;
    	
    	event.setCanceled(this.properties.getIsModEnabled().get());
    }

    @SubscribeEvent
    public void onRenderTickEvent(final TickEvent.RenderTickEvent event) {
        if (!this.properties.getIsModEnabled().get()) {
            return;
        }

        if (Minecraft.getMinecraft().currentScreen != null && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat))
            return;

        ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
        int width = resolution.getScaledWidth();
        int height = resolution.getScaledHeight();

        int x = Math.round(width / 2.0F);
        int y = Math.round(height / 2.0F);

        this.crosshairRenderManager.draw(x, y);
    }
    
    @SubscribeEvent
    public void onPlayerLoggedInEvent(final PlayerEvent.PlayerLoggedInEvent event) {
    	if (this.properties.isLatestVersion().get() || this.properties.getIsNewVersionNotified().get() || this.properties.getNewVersion().get() == null)
    		return;
    	
		String pre = "\u00A79" + "[Custom Crosshair Mod] " + "\u00A7r";
		
    	event.player.addChatMessage(new ChatComponentTranslation(pre + "New version available: " + this.properties.getNewVersion().get() + ".", new Object[0]));
    	
    	this.properties.getIsNewVersionNotified().set(true);
    }

    public GlobalProperties properties() {
        return this.properties;
    }

    public ConfigManager configManager() {
        return this.configManager;
    }

    public void log(final String action, final String message, final Object... values) {
        this.logger.info(String.format("[%s] %s", action, message), values);
    }

    public void error(final String action, final String message, final Object... values) {
        this.logger.error(String.format("[%s] %s", action, message), values);
    }
}
