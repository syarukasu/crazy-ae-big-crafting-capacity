package com.syaru.crazycapacity;

import appeng.block.crafting.AbstractCraftingUnitBlock;
import appeng.block.crafting.ICraftingUnitType;
import appeng.blockentity.crafting.CraftingBlockEntity;
import dev.beecube31.crazyae2.common.tile.networking.TileCraftingUnitsCombiner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.EntityBlock;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;

public final class StorageCapacity {
    private StorageCapacity() {}

    private static final ClassValue<Access> ACCESS = new ClassValue<>() {
        protected Access computeValue(Class<?> type) {
            return new Access(findMethod(type, BigInteger.class, "exactStorageBytes", "getBigIntegerStorageBytes"),
                    findMethod(type, long.class, "getStorageBytes"),
                    findMethod(type, int.class, "getAcceleratorThreads"), findUnitType(type));
        }
    };

    public static ICraftingUnitType advancedType(ItemStack stack) {
        if (!(stack.getItem() instanceof BlockItem item)
                || item.getBlock() instanceof AbstractCraftingUnitBlock
                || !(item.getBlock() instanceof EntityBlock block)) return null;
        Field field = ACCESS.get(item.getBlock().getClass()).unitType();
        if (field == null) return null;
        ICraftingUnitType type = (ICraftingUnitType) invokeField(field, item.getBlock());
        var entity = block.newBlockEntity(BlockPos.ZERO, item.getBlock().defaultBlockState());
        if (entity == null) return null;
        Access access = ACCESS.get(entity.getClass());
        BigInteger capacity = exact(entity);
        if (capacity == null) {
            long bytes = access.ordinary() == null ? type.getStorageBytes()
                    : (long) invoke(access.ordinary(), entity);
            capacity = BigInteger.valueOf(Math.max(0L, bytes));
        }
        int threads = access.threads() == null ? type.getAcceleratorThreads()
                : (int) invoke(access.threads(), entity);
        return new AdvancedStorageType(capacity, Math.max(0, threads), stack.getItem());
    }

    public static BigInteger stackCapacity(ItemStack stack) {
        if (stack.isEmpty()) return BigInteger.ZERO;
        ICraftingUnitType type = TileCraftingUnitsCombiner.unitTypeOf(stack);
        if (type == null) return BigInteger.ZERO;
        BigInteger capacity = exact(type);
        return capacity != null ? capacity : BigInteger.valueOf(Math.max(0L, type.getStorageBytes()));
    }

    public static BigInteger exactContribution(CraftingBlockEntity entity) {
        BigInteger capacity = exact(entity);
        if (capacity == null) capacity = exact(entity.getUnitBlock().type);
        return capacity != null ? capacity : BigInteger.valueOf(Math.max(0L, entity.getStorageBytes()));
    }

    private static BigInteger exact(Object provider) {
        if (provider instanceof ExactStorageProvider own) return checked(own.getBigIntegerStorageBytes());
        Method method = ACCESS.get(provider.getClass()).exact();
        return method == null ? null : checked((BigInteger) invoke(method, provider));
    }

    private static BigInteger checked(BigInteger capacity) {
        if (capacity == null || capacity.signum() < 0)
            throw new IllegalStateException("Storage provider returned an invalid capacity");
        return capacity;
    }

    private static Method findMethod(Class<?> type, Class<?> result, String... names) {
        for (String name : names) {
            try {
                Method method = type.getMethod(name);
                if (method.getReturnType() == result && method.trySetAccessible()) return method;
            } catch (NoSuchMethodException ignored) {}
        }
        return null;
    }

    private static Field findUnitType(Class<?> type) {
        try {
            Field field = type.getField("type");
            return ICraftingUnitType.class.isAssignableFrom(field.getType()) && field.trySetAccessible() ? field : null;
        } catch (NoSuchFieldException ignored) { return null; }
    }

    private static Object invoke(Method method, Object target) {
        try { return method.invoke(target); }
        catch (IllegalAccessException | InvocationTargetException failure) {
            throw new IllegalStateException("Cannot read exact crafting storage capacity", failure);
        }
    }

    private static Object invokeField(Field field, Object target) {
        try { return field.get(target); }
        catch (IllegalAccessException failure) {
            throw new IllegalStateException("Cannot read crafting unit type", failure);
        }
    }

    private record Access(Method exact, Method ordinary, Method threads, Field unitType) {}

    private record AdvancedStorageType(BigInteger capacity, int threads, Item item)
            implements ICraftingUnitType, ExactStorageProvider {
        public BigInteger getBigIntegerStorageBytes() { return capacity; }
        public long getStorageBytes() { return CapacityMath.longFacade(capacity); }
        public int getAcceleratorThreads() { return threads; }
        public Item getItemFromType() { return item; }
    }
}
