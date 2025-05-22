package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.ContactMessage;
import util.DbConnectionUtil;

public class ContactMessageDAO {

    // Save new contact message
    public boolean saveMessage(ContactMessage message) throws Exception {
        String sql = "INSERT INTO ContactMessages (user_id, name, email, subject, message) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // User ID can be null for non-logged in users
            if (message.getUserId() > 0) {
                ps.setInt(1, message.getUserId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }

            ps.setString(2, message.getName());
            ps.setString(3, message.getEmail());
            ps.setString(4, message.getSubject());
            ps.setString(5, message.getMessage());

            int result = ps.executeUpdate();

            if (result > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        message.setMessageId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    // Get all messages for admin panel
    public List<ContactMessage> getAllMessages() throws Exception {
        String sql = "SELECT * FROM ContactMessages ORDER BY submitted_at DESC";
        List<ContactMessage> messages = new ArrayList<>();

        try (Connection conn = DbConnectionUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ContactMessage message = new ContactMessage();
                message.setMessageId(rs.getInt("message_id"));
                message.setUserId(rs.getInt("user_id"));
                message.setName(rs.getString("name"));
                message.setEmail(rs.getString("email"));
                message.setSubject(rs.getString("subject"));
                message.setMessage(rs.getString("message"));
                message.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());

                messages.add(message);
            }
        }
        return messages;
    }

    // Get paginated messages
    public List<ContactMessage> getMessages(int offset, int limit) throws Exception {
        String sql = "SELECT * FROM ContactMessages ORDER BY submitted_at DESC LIMIT ? OFFSET ?";
        List<ContactMessage> messages = new ArrayList<>();

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ContactMessage message = new ContactMessage();
                    message.setMessageId(rs.getInt("message_id"));
                    message.setUserId(rs.getInt("user_id"));
                    message.setName(rs.getString("name"));
                    message.setEmail(rs.getString("email"));
                    message.setSubject(rs.getString("subject"));
                    message.setMessage(rs.getString("message"));
                    message.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());

                    messages.add(message);
                }
            }
        }
        return messages;
    }

    // Get single message by ID
    public ContactMessage getMessageById(int messageId) throws Exception {
        String sql = "SELECT * FROM ContactMessages WHERE message_id = ?";

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, messageId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ContactMessage message = new ContactMessage();
                    message.setMessageId(rs.getInt("message_id"));
                    message.setUserId(rs.getInt("user_id"));
                    message.setName(rs.getString("name"));
                    message.setEmail(rs.getString("email"));
                    message.setSubject(rs.getString("subject"));
                    message.setMessage(rs.getString("message"));
                    message.setSubmittedAt(rs.getTimestamp("submitted_at").toLocalDateTime());

                    return message;
                }
            }
        }
        return null;
    }

    // Delete a message
    public boolean deleteMessage(int messageId) throws Exception {
        String sql = "DELETE FROM ContactMessages WHERE message_id = ?";

        try (Connection conn = DbConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, messageId);

            int result = ps.executeUpdate();
            return result > 0;
        }
    }

    // Count total messages for pagination
    public int getTotalMessageCount() throws Exception {
        String sql = "SELECT COUNT(*) FROM ContactMessages";

        try (Connection conn = DbConnectionUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    // Get unread messages count (could add a 'read' column to the schema)
    public int getUnreadMessageCount() throws Exception {
        // Assuming you add a 'read' column to the schema
        // String sql = "SELECT COUNT(*) FROM ContactMessages WHERE read = 0";

        // Currently just returns all messages as "unread"
        String sql = "SELECT COUNT(*) FROM ContactMessages";

        try (Connection conn = DbConnectionUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}