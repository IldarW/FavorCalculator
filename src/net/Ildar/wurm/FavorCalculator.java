package net.Ildar.wurm;

import com.wurmonline.client.game.inventory.InventoryMetaItem;
import com.wurmonline.client.game.inventory.InventoryMetaWindowView;
import com.wurmonline.client.renderer.gui.*;
import javassist.ClassPool;
import javassist.CtClass;
import org.gotti.wurmunlimited.modloader.ReflectionUtil;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Initable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmClientMod;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FavorCalculator implements WurmClientMod, Initable {
    private static Logger logger = Logger.getLogger("FavorCalculator");
    private static HeadsUpDisplay hud;

    @Override
    public void init() {
        try {
            final ClassPool classPool = HookManager.getInstance().getClassPool();
            final CtClass ctWurmConsole = classPool.getCtClass("com.wurmonline.client.console.WurmConsole");
            ctWurmConsole.getMethod("handleDevInput", "(Ljava/lang/String;[Ljava/lang/String;)Z").insertBefore("if (net.Ildar.wurm.FavorCalculator.handleInput($1,$2)) return true;");
            HookManager.getInstance().registerHook("com.wurmonline.client.renderer.gui.HeadsUpDisplay", "init", "(II)V", () -> (proxy, method, args) -> {
                method.invoke(proxy, args);
                hud = (HeadsUpDisplay) proxy;
                return null;
            });
        } catch (Exception e) {
            if (FavorCalculator.logger != null) {
                FavorCalculator.logger.log(Level.SEVERE, "Error loading mod", e);
                FavorCalculator.logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    public static float itemFavor(InventoryMetaItem item, float c) {
        float quality = item.getQuality() * (1 - item.getDamage() / 100);
        return quality * quality / 500 * c;
    }

    private static void calculateFavor(String[] params) {
        float factor = 1.0f;
        if (params.length == 2) {
            try {
                factor = Float.parseFloat(params[1]);
            } catch (NumberFormatException e) {
                hud.consoleOutput("Invalid value");
                return;
            }
        }
        try {
            int x = hud.getWorld().getClient().getXMouse();
            int y = hud.getWorld().getClient().getYMouse();
            List<WurmComponent> components = hud.getComponents();
            for (int i = 0; i < components.size(); i++) {
                WurmComponent wurmComponent = components.get(i);
                if ((wurmComponent instanceof InventoryWindow || wurmComponent instanceof ItemListWindow) && wurmComponent.contains(x, y)) {
                    InventoryListComponent inventoryListComponent = ReflectionUtil.getPrivateField(wurmComponent,
                            ReflectionUtil.getField(wurmComponent.getClass(), "component"));
                    InventoryMetaWindowView inventoryWindow = ReflectionUtil.getPrivateField(inventoryListComponent,
                            ReflectionUtil.getField(inventoryListComponent.getClass(), "inventoryWindow"));
                    long[] targets = inventoryListComponent.getSelectedCommandTargets();
                    if (targets == null || targets.length == 0)
                        break;
                    float favor = 0f;
                    for (long target : targets) {
                        favor += itemFavor(inventoryWindow.getItem(target), factor);
                    }
                    String info = String.format("You would probably get %.2f favor for this", favor);
                    hud.addOnscreenMessage(info, 1, 1, 1, (byte) 1);
                    break;
                }
            }
        } catch (Exception e) {
            hud.consoleOutput("Unexpected error while calculating favor - " + e.getMessage());
            hud.consoleOutput(e.toString());
        }
    }

    public static boolean handleInput(final String cmd, final String[] data) {
        switch (cmd) {
            case "favor":
                calculateFavor(data);
                return true;
            default:
                return false;
        }
    }
}
