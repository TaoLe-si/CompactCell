package com.tao.main;

import com.tao.main.dimension.CMtransfer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class CMSavedData extends SavedData {
    private static final Logger LOG = LoggerFactory.getLogger(CMSavedData.class);
    @Override
    public CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        return compoundTag;
    }
}
