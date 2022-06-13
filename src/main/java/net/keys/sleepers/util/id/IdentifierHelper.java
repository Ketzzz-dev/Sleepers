package net.keys.sleepers.util.id;

import net.keys.sleepers.init.Main;
import net.minecraft.util.Identifier;

public class IdentifierHelper {
    public static Identifier createId(String path) {
        return new Identifier(Main.MOD_ID, path);
    }
}
