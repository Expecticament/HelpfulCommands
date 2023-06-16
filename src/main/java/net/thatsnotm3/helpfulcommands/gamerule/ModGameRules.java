package net.thatsnotm3.helpfulcommands.gamerule;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.Category;

public class ModGameRules{
    public static GameRules.Key<GameRules.BooleanRule> HC_ENABLED;
    public static GameRules.Key<GameRules.BooleanRule> HC_UNCAP_EXPLOSION_POWER;

    public static void registerGameRules(){
        HC_ENABLED=GameRuleRegistry.register("hcEnabled", Category.CHAT, GameRuleFactory.createBooleanRule(true));
        HC_UNCAP_EXPLOSION_POWER=GameRuleRegistry.register("hcUncapExplosionPower", Category.CHAT, GameRuleFactory.createBooleanRule(false));
    }
}