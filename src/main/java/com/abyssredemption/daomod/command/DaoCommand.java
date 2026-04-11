package com.abyssredemption.daomod.command;


import com.abyssredemption.daomod.AbsDaoMod;
import com.abyssredemption.daomod.attachment.CultivationData;
import com.abyssredemption.daomod.network.CultivationPayload;
import com.abyssredemption.daomod.registry.ModAttachments;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;


@EventBusSubscriber(modid = AbsDaoMod.MODID)
public class DaoCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher());
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        // 注册qi命令
        dispatcher.register(Commands.literal("dao").requires(source -> source.hasPermission(2)) // 需要管理员权限
                .then(Commands.literal("qi").then(Commands.literal("set")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("amount", LongArgumentType.longArg(0))
                                                .executes(context -> updateQi(
                                                        context.getSource(),
                                                        EntityArgument.getPlayers(context, "targets"),
                                                        LongArgumentType.getLong(context, "amount"),
                                                        false))
                                        )
                                )
                        )
                        .then(Commands.literal("add")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("amount", LongArgumentType.longArg(0))
                                                .executes(context -> updateQi(
                                                        context.getSource(),
                                                        EntityArgument.getPlayers(context, "targets"),
                                                        LongArgumentType.getLong(context, "amount"),
                                                        true))
                                        )
                                )
                        )
                )
        );

        //
        dispatcher.register(Commands.literal("dao").requires(source -> source.hasPermission(2)) // 需要管理员权限
                .then(Commands.literal("realm").
                        then(Commands.literal("set")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .then(Commands.argument("realm", IntegerArgumentType.integer(0))
                                                        .executes(context -> updateRealm(
                                                                context.getSource(),
                                                                EntityArgument.getPlayers(context, "targets"),
                                                                IntegerArgumentType.getInteger(context, "realm"),
                                                                false))
                                                )
                                        )
                                )
                        .then(Commands.literal("add")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .then(Commands.argument("realm", IntegerArgumentType.integer(0))
                                                        .executes(context -> updateRealm(
                                                                context.getSource(),
                                                                EntityArgument.getPlayers(context, "targets"),
                                                                IntegerArgumentType.getInteger(context, "realm"),
                                                                true))
                                                )
                                        )
                                )
                )
        );
    }

    private static int updateQi(CommandSourceStack source, java.util.Collection<ServerPlayer> targets, long amount, boolean isAdd) {
        for (ServerPlayer player : targets) {
            var data = player.getData(ModAttachments.CULTIVATION);

            long newQi;
            if (isAdd) {
                newQi = data.getQi() + amount;
            } else {
                newQi = amount;
            }

            // 限制不超过最大值
            long maxQi = CultivationData.getMaxQi(data.getRealm(), data.getStage());
            newQi = Math.min(newQi, maxQi);

            // 1. 更新服务端数据
            data.setQi(newQi);
            player.setData(ModAttachments.CULTIVATION, data);

            // 2. 发送同步包给客户端 (必须手动同步，UI 才会变)
            player.connection.send(new CultivationPayload(
                    data.getRealm(),
                    data.getQi(),
                    data.getSectOrthodoxy(),
                    data.getStage()
            ));

            long finalNewQi = newQi;
            source.sendSuccess(() -> Component.literal("已为 " + player.getScoreboardName() + " 设置炁为: " + finalNewQi), true);
        }
        return targets.size();
    }

    private static int updateRealm(CommandSourceStack source, java.util.Collection<ServerPlayer> targets, int amount, boolean isAdd) {
        for (ServerPlayer player : targets) {
            var data = player.getData(ModAttachments.CULTIVATION);

            int newRealm;
            if (isAdd) {
                newRealm = data.getRealm() + amount;
            } else {
                newRealm = amount;
            }

            // 限制不超过最大值

            newRealm = Math.min(newRealm, 8);

            // 1. 更新服务端数据
            data.setRealm(newRealm);
            player.setData(ModAttachments.CULTIVATION, data);

            // 2. 发送同步包给客户端 (必须手动同步，UI 才会变)
            player.connection.send(new CultivationPayload(
                    data.getRealm(),
                    data.getQi(),
                    data.getSectOrthodoxy(),
                    data.getStage()
            ));

            int finalNewRealm = newRealm;
            source.sendSuccess(() -> Component.literal("已为 " + player.getScoreboardName() + " 设置境界为: " + finalNewRealm), true);
        }
        return targets.size();
    }



}
