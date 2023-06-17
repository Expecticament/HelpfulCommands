package net.thatsnotm3.helpfulcommands.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;

public class CMD_Abilities{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment){
        dispatcher.register(CommandManager.literal("abilities")
            .then(CommandManager.literal("get").executes(CMD_Abilities::getAbilities))
            .then(CommandManager.literal("set")
                .then(CommandManager.literal("allowFlying")
                    .then(CommandManager.argument("value", BoolArgumentType.bool()).executes(ctx -> toggleAbility(ctx, 0, BoolArgumentType.getBool(ctx, "value"))))
                )
                /*.then(CommandManager.literal("allowModifyWorld")
                    .then(CommandManager.argument("value", BoolArgumentType.bool()).executes(ctx -> toggleAbility(ctx, 0, BoolArgumentType.getBool(ctx, "value"))))
                )*/
                .then(CommandManager.literal("invulnerable")
                    .then(CommandManager.argument("value", BoolArgumentType.bool()).executes(ctx -> toggleAbility(ctx, 0, BoolArgumentType.getBool(ctx, "value"))))
                )
                .then(CommandManager.literal("flySpeed")
                    .then(CommandManager.argument("value", FloatArgumentType.floatArg()).executes(ctx -> setAbilityValue(ctx, 0, FloatArgumentType.getFloat(ctx, "value"))))
                )
                /*.then(CommandManager.literal("walkSpeed")
                    .then(CommandManager.argument("value", FloatArgumentType.floatArg()).executes(ctx -> setAbilityValue(ctx, 1, FloatArgumentType.getFloat(ctx, "value"))))
                )*/
            )
        );
    }

    public static int toggleAbility(CommandContext<ServerCommandSource> ctx, int ability, boolean state) throws CommandSyntaxException{ // 0 - allowFlying; 1 - allowModifyWorld; 2 - invulnerable;
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!net.thatsnotm3.helpfulcommands.command.CommandManager.RunChecks("abilities",player)) return -1;

        PlayerAbilities abilities=player.getAbilities();
        String abilityName="UNKNOWN";
        String abilityState;
        if(state) abilityState="\u00A7atrue";
        else abilityState="\u00A7cfalse";
        switch(ability){
            case 0:
                abilities.allowFlying=state;
                if(!state) abilities.flying=false;
                abilityName="allowFlying";
                break;
            case 1:
                abilities.allowModifyWorld=state;
                abilityName="allowModifyWorld";
                break;
            case 2:
                abilities.invulnerable=state;
                abilityName="invulnerable";
                break;
        }
        player.sendAbilitiesUpdate();
        player.sendMessage(Text.literal("Set \u00A76"+abilityName+"\u00A7r ability state to: "+abilityState));
        
        return 1;
    }
    public static int setAbilityValue(CommandContext<ServerCommandSource> ctx, int ability, float value){ // 0 - flySpeed; 1 - walkSpeed
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!net.thatsnotm3.helpfulcommands.command.CommandManager.RunChecks("abilities",player)) return -1;

        PlayerAbilities abilities=player.getAbilities();
        String abilityName="UNKNOWN";
        switch(ability){
            case 0:
                abilityName="flySpeed";
                abilities.setFlySpeed(value);
                break;
            case 1:
                abilityName="walkSpeed";
                abilities.setWalkSpeed(value);
                break;
        }
        player.sendAbilitiesUpdate();
        player.sendMessage(Text.literal("Set \u00A76"+abilityName+"\u00A7r value to: \u00A7b"+value));

        return 1;
    }
    public static int getAbilities(CommandContext<ServerCommandSource> ctx){
        ServerPlayerEntity player=ctx.getSource().getPlayer();

        if(!net.thatsnotm3.helpfulcommands.command.CommandManager.RunChecks("abilities",player)) return -1;

        PlayerAbilities abilities=player.getAbilities();
        String text="\u00A7bYour abilities list:\u00A7r\n";

        if(abilities.allowFlying) text=text+"allowFlying: "+"\u00A7atrue\u00A7r\n";
        else text=text+"allowFlying: "+"\u00A7cfalse\u00A7r\n";
        if(abilities.allowModifyWorld) text=text+"allowModifyWorld: "+"\u00A7atrue\u00A7r\n";
        else text=text+"allowModifyWorld: "+"\u00A7cfalse\u00A7r\n";
        if(abilities.invulnerable) text=text+"invulnerable: "+"\u00A7atrue\u00A7r\n";
        else text=text+"invulnerable: "+"\u00A7cfalse\u00A7r\n";
        text=text+"flySpeed: "+"\u00A7b"+abilities.getFlySpeed()+"\u00A7r\n";
        text=text+"walkSpeed: "+"\u00A7b"+abilities.getWalkSpeed()+"\u00A7r";

        player.sendMessage(Text.literal(text));

        return 1;
    }
}