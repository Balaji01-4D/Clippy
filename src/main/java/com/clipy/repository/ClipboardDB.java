package com.clipy.repository;

import com.clipy.model.ClipboardItem;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClipboardDB {
    private static final String DB_FOLDER = System.getProperty("user.home") + "/.clipboard";
    private static final String DB_PATH = DB_FOLDER + "/clipboard.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    static {
        File dir = new File(DB_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL);
    }

    public void closeConnection() {
        try {
            Connection conn = ClipboardDB.getConnection();
            conn.close();
        }
        catch(Exception e){
                System.out.println("error in closing db");
            }
    }

    public ClipboardDB(){
        ClipboardDB.createTableIfNotExists();
        System.out.println("created successfully");
    }
    public static void createTableIfNotExists() {
        String sql = """
        CREATE TABLE IF NOT EXISTS clipboard (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            content TEXT NOT NULL,
            type TEXT,
            timestamp TEXT
        );
    """;

        try (Connection conn = ClipboardDB.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with logger in real project
        }
    }

    public List<ClipboardItem> getAllItems() {
        List<ClipboardItem> items = new ArrayList<>();
        String sql = "SELECT * FROM clipboard ORDER BY timestamp DESC LIMIT 50";

        try (Connection conn = ClipboardDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String content = rs.getString("content");
                String type = rs.getString("type");
                LocalDateTime timestamp = LocalDateTime.parse(rs.getString("timestamp"));
                ClipboardItem item = new ClipboardItem(content, type, timestamp);
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public void addItem(ClipboardItem item) {

        String insertSQL = "INSERT INTO clipboard(content, type, timestamp) VALUES (?, ?, ?)";
        String trimSQL = """
        DELETE FROM clipboard
        WHERE id NOT IN (
            SELECT id FROM clipboard ORDER BY timestamp DESC LIMIT 50
        );
    """;

        try (Connection conn = ClipboardDB.getConnection()) {
            conn.setAutoCommit(false); // ensure both actions happen together

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
                 Statement trimStmt = conn.createStatement()) {

                insertStmt.setString(1, item.getContent());
                insertStmt.setString(2, item.getType());
                insertStmt.setString(3, item.getTimestamp().toString());
                insertStmt.executeUpdate();

                trimStmt.executeUpdate(trimSQL);
                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void truncateTable() {
        String sql = "DELETE FROM clipboard";

        try (Connection conn = ClipboardDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.executeUpdate();
            System.out.println("Clipboard table cleared.");
        } catch (SQLException e) {
            System.out.println("Error truncating clipboard table: " + e.getMessage());
        }
    }

    public List<ClipboardItem> getByType(String type) {
        List<ClipboardItem> items = new ArrayList<>();

        String sql = "SELECT content, type, timestamp FROM clipboard WHERE type = ? ORDER BY timestamp DESC";

        try (Connection conn = ClipboardDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                String content = rs.getString("content");
                String itemType = rs.getString("type");
                String timestamp = rs.getString("timestamp");

                ClipboardItem item = new ClipboardItem(content, itemType, LocalDateTime.parse(timestamp));
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }



}
