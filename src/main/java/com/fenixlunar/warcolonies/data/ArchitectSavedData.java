package com.fenixlunar.warcolonies.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.core.HolderLookup;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple SavedData for Architect toggles per block position.
 */
public class ArchitectSavedData extends SavedData {
    private static final String DATA_NAME = "warcolonies_architect_data";

    // map packedPos -> flags (bit0 = sawmill)
    private final Map<Long, Integer> flags = new HashMap<>();

    public ArchitectSavedData() {
        super();
    }

    public static ArchitectSavedData get(final ServerLevel level) {
        try {
            final Object storage = level.getDataStorage();
            final Class<?> factoryInterface = Class.forName("net.minecraft.world.level.saveddata.SavedData$Factory");
            final java.lang.reflect.InvocationHandler handler = (proxy, method, args) -> {
                final String name = method.getName();
                if ("load".equals(name) || "create".equals(name) || "apply".equals(name)) {
                    // attempt to match signature: (CompoundTag, HolderLookup$Provider)
                    if (args != null && args.length == 2) {
                        return ArchitectSavedData.load((CompoundTag) args[0], (HolderLookup.Provider) args[1]);
                    }
                    // no-arg factory
                    return new ArchitectSavedData();
                }
                throw new UnsupportedOperationException("Unsupported factory method: " + name);
            };
            final Object factoryProxy = java.lang.reflect.Proxy.newProxyInstance(ArchitectSavedData.class.getClassLoader(), new Class[]{factoryInterface}, handler);
            final java.lang.reflect.Method compute = storage.getClass().getMethod("computeIfAbsent", factoryInterface, String.class);
            final Object result = compute.invoke(storage, factoryProxy, DATA_NAME);
            return (ArchitectSavedData) result;
        } catch (final Throwable t) {
            // Fallback: create a new instance (will not be persisted) but avoid crashing server
            return new ArchitectSavedData();
        }
    }

    // Backwards-compatible factory for SavedData (some mappings expect a single-arg factory)
    public static ArchitectSavedData load(final CompoundTag tag) {
        return load(tag, (HolderLookup.Provider) null);
    }

    public static ArchitectSavedData load(final CompoundTag tag, final HolderLookup.Provider provider) {
        final ArchitectSavedData d = new ArchitectSavedData();
        if (tag.contains("entries", 9)) { // LIST
            final ListTag list = tag.getList("entries", 10); // compound elements
            for (int i = 0; i < list.size(); i++) {
                final CompoundTag e = list.getCompound(i);
                final long pos = e.getLong("pos");
                final int f = e.getInt("flags");
                d.flags.put(pos, f);
            }
        }
        return d;
    }

    @Override
    public CompoundTag save(final CompoundTag tag, final HolderLookup.Provider provider) {
        final ListTag list = new ListTag();
        for (final Map.Entry<Long, Integer> en : flags.entrySet()) {
            final CompoundTag e = new CompoundTag();
            e.putLong("pos", en.getKey());
            e.putInt("flags", en.getValue());
            list.add(e);
        }
        tag.put("entries", list);
        return tag;
    }

    // Backwards-compatible save overload
    public CompoundTag save(final CompoundTag tag) {
        return save(tag, (HolderLookup.Provider) null);
    }

    public boolean getSawmillEnabled(final BlockPos pos) {
        final Integer v = flags.get(pos.asLong());
        return v != null && (v & 0x1) != 0;
    }

    public void setSawmillEnabled(final BlockPos pos, final boolean enabled) {
        final long key = pos.asLong();
        int v = flags.getOrDefault(key, 0);
        if (enabled) v |= 0x1; else v &= ~0x1;
        flags.put(key, v);
        this.setDirty();
    }

    // General helpers for category bits
    public boolean getCategoryEnabled(final BlockPos pos, final int bitIndex) {
        final Integer v = flags.get(pos.asLong());
        if (v == null) return false;
        return (v & (1 << bitIndex)) != 0;
    }

    public void setCategoryEnabled(final BlockPos pos, final int bitIndex, final boolean enabled) {
        final long key = pos.asLong();
        int v = flags.getOrDefault(key, 0);
        if (enabled) v |= (1 << bitIndex); else v &= ~(1 << bitIndex);
        flags.put(key, v);
        this.setDirty();
    }
}
