package com.tao.main;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;

public class CMDataComponents {
    public static final DeferredRegister.DataComponents DR = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, "compactcell");
    public record PidRecord(int id) {}
    private static final Codec<PidRecord> Pid_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("id").forGetter(PidRecord::id)
            ).apply(instance, PidRecord::new)
    );
    private static final StreamCodec<ByteBuf, PidRecord> Pid_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PidRecord::id,
            PidRecord::new
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> Id = DR.registerComponentType(
            "uuid",
            builder -> builder
                    .persistent(UUIDUtil.CODEC)
                    .networkSynchronized(UUIDUtil.STREAM_CODEC)
    );
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PidRecord>> Pid = DR.registerComponentType(
            "pid",
            builder -> builder
                    .persistent(Pid_CODEC)
                    .networkSynchronized(Pid_STREAM_CODEC)
    );
}
