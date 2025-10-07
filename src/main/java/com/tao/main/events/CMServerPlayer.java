package com.tao.main.events;

import com.mojang.authlib.GameProfile;
import com.tao.main.CMConnectionsHelper;
import com.tao.main.CompactCell;
import com.tao.main.PlayerVisitData;
import com.tao.main.dimension.CMHistory;
import com.tao.main.dimension.CMtransfer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class CMServerPlayer extends ServerPlayer {
    public CMServerPlayer(MinecraftServer server, ServerLevel level, GameProfile gameProfile, ClientInformation clientInformation) {
        super(server, level, gameProfile, clientInformation);
    }
    public CMServerPlayer(ServerPlayer player) {
        super(player.server, (ServerLevel) player.level(), player.getGameProfile(), player.clientInformation());
    }
    @SubscribeEvent
    public static void onPlayerDisconnected(PlayerEvent.PlayerLoggedOutEvent evt) {
        ServerLevel overworld = evt.getEntity().getServer().overworld();

        var transferTool = overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(CMtransfer::create, CMtransfer::load), "compactcell_transfer");
        var historyData = overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(CMHistory::create, CMHistory::load), "compactcell_history");
        var genData = overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(CMPlayerChangedDimensionEvent::create, CMPlayerChangedDimensionEvent::load), "compactcell_gen");
        var playerData = overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(PlayerVisitData::create, PlayerVisitData::load), "compactcell_player_visits");
        var connect = overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(CMConnectionsHelper::create, CMConnectionsHelper::load), "compactcell_connect");

        transferTool.setDirty();
        historyData.setDirty();
        genData.setDirty();
        playerData.setDirty();
        connect.setDirty();

    }
    @SubscribeEvent
    public static void onPlayerLogin(final PlayerEvent.PlayerLoggedInEvent evt) {
        ServerLevel overworld = evt.getEntity().getServer().overworld();

        var transferTool = overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(CMtransfer::create, CMtransfer::load), "compactcell_transfer");
        var historyData = overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(CMHistory::create, CMHistory::load), "compactcell_history");
        var genData = overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(CMPlayerChangedDimensionEvent::create, CMPlayerChangedDimensionEvent::load), "compactcell_gen");
        var visitData = overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(PlayerVisitData::create, PlayerVisitData::load), "compactcell_player_visits");
        var connect = overworld.getDataStorage().computeIfAbsent(new SavedData.Factory<>(CMConnectionsHelper::create, CMConnectionsHelper::load), "compactcell_connect");

        transferTool.setDirty();
        historyData.setDirty();
        genData.setDirty();
        visitData.setDirty();
        connect.setDirty();

        if (evt.getEntity() instanceof ServerPlayer player) {
            //PlayerVisitData visitData = PlayerVisitData.create();
            if (visitData.isFirstVisit(player.getUUID())) {
                onFirstTimePlayerJoin(player);
                visitData.markPlayerVisited(player.getUUID());
            } else {
                onReturningPlayerJoin(player);
            }
        }
    }
    private static void onFirstTimePlayerJoin(ServerPlayer player) {
        //player.sendSystemMessage(Component.literal("欢迎你，新冒险者！"));
        //CompactCell.LOG.info("玩家首次进入存档: {}", player.getScoreboardName());
    }

    private static void onReturningPlayerJoin(ServerPlayer player) {
        // 非首次进入时的操作
        //player.sendSystemMessage(Component.literal("欢迎回来，").append(player.getScoreboardName()));
    }
    @SubscribeEvent
    public static void onPlayerRespawn(final PlayerEvent.PlayerRespawnEvent evt) {
        CMHistory.clear();
    }
}
