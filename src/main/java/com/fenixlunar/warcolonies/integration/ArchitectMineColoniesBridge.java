package com.fenixlunar.warcolonies.integration;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Reflection-based bridge to MineColonies to teach recipes and list huts.
 * This class attempts to be tolerant if MineColonies is not present.
 */
public final class ArchitectMineColoniesBridge {
    private ArchitectMineColoniesBridge() {}

    public static List<String> findNearbyHuts(ServerLevel level, BlockPos pos) {
        final List<String> result = new ArrayList<>();
        try {
            Class<?> mgrClass = Class.forName("com.minecolonies.api.colony.IColonyManager");
            Method getInstance = mgrClass.getMethod("getInstance");
            Object mgr = getInstance.invoke(null);
            Method getColony = mgrClass.getMethod("getColonyByPosFromWorld", net.minecraft.world.level.Level.class, net.minecraft.core.BlockPos.class);
            Object colony = getColony.invoke(mgr, level, pos);
            if (colony == null) return result;
            // Try to get building manager and list building views or building names
            Class<?> colonyClass = Class.forName("com.minecolonies.api.colony.IColony");
            Method getBuildingManager = colonyClass.getMethod("getBuildingManager");
            Object buildingManager = getBuildingManager.invoke(colony);
            if (buildingManager == null) return result;
            // fall back: try to get list of buildings via reflection on colony
            // Many MineColonies versions expose building views manager - attempt common methods
            try {
                Class<?> managerClass = buildingManager.getClass();
                Method getAll = null;
                for (Method m : managerClass.getMethods()) {
                    if (m.getReturnType().isAssignableFrom(java.util.List.class) && m.getParameterCount() == 0) {
                        getAll = m; break;
                    }
                }
                if (getAll != null) {
                    Object list = getAll.invoke(buildingManager);
                    if (list instanceof java.util.List) {
                        for (Object b : (java.util.List<?>) list) {
                            result.add(String.valueOf(b));
                        }
                    }
                }
            } catch (final Throwable ignored) {}
        } catch (final ClassNotFoundException e) {
            // MineColonies not present
        } catch (final Throwable t) {
            // ignore other reflection errors
        }
        return result;
    }

    public static void teachSawmillPlaceholder(ServerLevel level, BlockPos pos, ServerPlayer player) {
        // Try to build a simple RecipeStorage and add it to the MineColonies recipe manager.
        try {
            // Load RecipeStorage builder
            Class<?> rsClass = Class.forName("com.minecolonies.api.crafting.RecipeStorage");
            Method builderMethod = null;
            for (Method m : rsClass.getMethods()) {
                if (m.getName().equals("builder") && (m.getParameterCount() == 0)) {
                    builderMethod = m;
                    break;
                }
            }
            if (builderMethod == null) {
                player.sendSystemMessage(Component.literal("MineColonies RecipeStorage.builder() not found."));
                return;
            }

            Object builder = builderMethod.invoke(null);

            // Try to find a 'primaryOutput' or similar method to set the output
            Class<?> builderClass = builder.getClass();
            Method outputSetter = null;
            for (Method m : builderClass.getMethods()) {
                if (m.getReturnType().equals(builderClass) && m.getParameterCount() == 1) {
                    Class<?> p = m.getParameterTypes()[0];
                    if (p.getName().equals("net.minecraft.world.item.ItemStack")) {
                        outputSetter = m; break;
                    }
                }
            }

            // Create a simple output ItemStack (oak planks)
            net.minecraft.world.item.ItemStack output = new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.OAK_PLANKS, 1);

            if (outputSetter != null) {
                outputSetter.invoke(builder, output);
            } else {
                // fallback: try method that accepts ItemStack and returns something
                boolean set = false;
                for (Method m : builderClass.getMethods()) {
                    if (m.getParameterCount() == 1 && m.getParameterTypes()[0].getName().equals("net.minecraft.world.item.ItemStack")) {
                        m.invoke(builder, output);
                        set = true; break;
                    }
                }
                if (!set) {
                    player.sendSystemMessage(Component.literal("Could not find builder method to set primary output."));
                }
            }

            // Try to build recipe storage object
            Method buildMethod = null;
            for (Method m : builderClass.getMethods()) {
                if ((m.getName().equals("build") || m.getName().equals("create") || m.getName().equals("buildRecipe")) && m.getParameterCount() == 0) {
                    buildMethod = m; break;
                }
            }
            Object recipeStorage = null;
            if (buildMethod != null) {
                recipeStorage = buildMethod.invoke(builder);
            } else {
                // maybe the builder has a method that returns IRecipeStorage via other name
                for (Method m : builderClass.getMethods()) {
                    if (m.getReturnType().getName().contains("IRecipeStorage") && m.getParameterCount() == 0) {
                        recipeStorage = m.invoke(builder); break;
                    }
                }
            }

            if (recipeStorage == null) {
                player.sendSystemMessage(Component.literal("Failed to build RecipeStorage via reflection."));
                return;
            }

            // Locate colony manager and recipe manager
            Class<?> mgrClass = Class.forName("com.minecolonies.api.colony.IColonyManager");
            Method getInstance = mgrClass.getMethod("getInstance");
            Object mgr = getInstance.invoke(null);

            // Get recipe manager from manager (IColonyManager.getRecipeManager())
            Method getRecipeManager = null;
            for (Method m : mgrClass.getMethods()) {
                if (m.getName().equals("getRecipeManager") && m.getParameterCount() == 0) {
                    getRecipeManager = m; break;
                }
            }
            if (getRecipeManager == null) {
                player.sendSystemMessage(Component.literal("IColonyManager.getRecipeManager() not found."));
                return;
            }

            Object recipeManager = getRecipeManager.invoke(mgr);
            if (recipeManager == null) {
                player.sendSystemMessage(Component.literal("Recipe manager is null."));
                return;
            }

            // Try checkOrAddRecipe or addRecipe
            Method checkOrAdd = null;
            for (Method m : recipeManager.getClass().getMethods()) {
                if ((m.getName().equals("checkOrAddRecipe") || m.getName().equals("addRecipe")) && m.getParameterCount() == 1) {
                    checkOrAdd = m; break;
                }
            }
            if (checkOrAdd == null) {
                player.sendSystemMessage(Component.literal("No suitable add/check method found on recipe manager."));
                return;
            }

            Object token = checkOrAdd.invoke(recipeManager, recipeStorage);
            player.sendSystemMessage(Component.literal("Teach completed: token=" + String.valueOf(token)));
        } catch (ClassNotFoundException e) {
            player.sendSystemMessage(Component.literal("MineColonies API not available on the server."));
        } catch (Throwable t) {
            player.sendSystemMessage(Component.literal("Error during teachSawmill: " + t.toString()));
        }
    }
}
