package com.ssomar.score.nofalldamage;

import com.ssomar.score.SCore;
import com.ssomar.score.utils.Couple;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;


public class NoFallDamageManager {

    private static NoFallDamageManager instance;
    private final Map<Player, List<Couple<UUID, BukkitTask>>> noFallDamageMap = new HashMap<>();

    public static NoFallDamageManager getInstance() {
        if (instance == null) instance = new NoFallDamageManager();
        return instance;
    }

    public void addNoFallDamage(Player p) {
        UUID uuid = UUID.randomUUID();

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                NoFallDamageManager.getInstance().removeNoFallDamage(p, uuid);
            }
        };
        BukkitTask task = runnable.runTaskLater(SCore.plugin, 300);

        NoFallDamageManager.getInstance().addNoFallDamage(p, new Couple<>(uuid, task));
    }

    public void addNoFallDamage(Player p, Couple<UUID, BukkitTask> c) {
        if (noFallDamageMap.containsKey(p)) {
            noFallDamageMap.get(p).add(c);
        } else {
            List<Couple<UUID, BukkitTask>> newList = new ArrayList<>();
            newList.add(c);
            noFallDamageMap.put(p, newList);
        }
    }

    public void removeNoFallDamage(Player p, UUID uuid) {
        boolean emptyList = false;
        for (Player player : noFallDamageMap.keySet()) {

            if (p.equals(player)) {
                List<Couple<UUID, BukkitTask>> tasks = noFallDamageMap.get(p);

                Couple<UUID, BukkitTask> toRemove = null;
                for (Couple<UUID, BukkitTask> c : tasks) {
                    if (c.getElem1().equals(uuid)) {
                        c.getElem2().cancel();
                        toRemove = c;
                    }
                }
                if (toRemove != null) tasks.remove(toRemove);

                emptyList = tasks.size() == 0;

                break;
            }
        }
        if (emptyList) noFallDamageMap.remove(p);

    }

    public void removeAllNoFallDamage(Player p) {
        noFallDamageMap.remove(p);
    }

    public boolean contains(Player p) {
        for (Player player : noFallDamageMap.keySet()) {
            if (p.equals(player)) {
                return true;
            }
        }
        return false;
    }

}
