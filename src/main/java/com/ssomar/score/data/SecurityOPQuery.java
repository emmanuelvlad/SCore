package com.ssomar.score.data;

import com.ssomar.score.SCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SecurityOPQuery {

    private final static String TABLE_SECURITYOP = "securityop";
    private final static String TABLE_SECURITYOP_NAME = "SecurityOP";

    private final static String COL_PLAYER = "player";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SECURITYOP + " (" + COL_PLAYER + " TEXT NOT NULL);";


    public static void createNewTable(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            SCore.plugin.getLogger().info(SCore.NAME_2 + " Verification of the table " + TABLE_SECURITYOP_NAME + "...");
            stmt.execute(CREATE_TABLE);
        } catch (SQLException e) {
            System.out.println(SCore.NAME_2 + " " + e.getMessage());
        }
    }

    public static boolean insertPlayerOP(Connection conn, List<Player> players) {
        String sql = "INSERT INTO " + TABLE_SECURITYOP + " (" + COL_PLAYER + ") VALUES(?)";

        PreparedStatement pstmt = null;
        int i = 0;

        try {
            pstmt = conn.prepareStatement(sql);

            for (Player player : players) {
                i++;
                pstmt.setString(1, player.getUniqueId().toString());
                pstmt.addBatch();

                if (i % 15 == 0 || i == players.size()) {
                    pstmt.executeBatch(); // Execute every 1000 items.
                }
            }
        } catch (SQLException e) {
            System.out.println(SCore.NAME_2 + " " + e.getMessage());
            return false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


    public static void deletePlayerOP(Connection conn, Player p, boolean async) {

        if (async) {
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    deletePlayerOP(conn, p);
                }
            };
            runnable.runTaskAsynchronously(SCore.plugin);
        } else deletePlayerOP(conn, p);
    }

    public static void deletePlayerOP(Connection conn, Player p) {
        String sql = "DELETE FROM " + TABLE_SECURITYOP + " where " + COL_PLAYER + "=?";

        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, p.getUniqueId().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(SCore.NAME_2 + " " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static boolean selectIfSecurityOPcontains(Connection conn, Player p) {
        String sql = "SELECT " + COL_PLAYER + " FROM " + TABLE_SECURITYOP + " where " + COL_PLAYER + "=?";

        PreparedStatement pstmt = null;

        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, p.getUniqueId().toString());
            rs = pstmt.executeQuery();

            if (rs.next()) return true;
        } catch (SQLException e) {
            System.out.println(SCore.NAME_2 + " " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static List<UUID> loadUsersOp(Connection conn) {
        String sql = "SELECT * FROM " + TABLE_SECURITYOP;

        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List<UUID> list = new ArrayList<>();

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String player = rs.getString(COL_PLAYER);
                try {
                    list.add(UUID.fromString(player));
                } catch (Exception e) {
                    /* In the old version I was saving the name of the player */
                    try {
                        list.add(Bukkit.getServer().getOfflinePlayer(player).getUniqueId());
                    } catch (Exception ignore) {
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("[ExecutableItems] " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

}
