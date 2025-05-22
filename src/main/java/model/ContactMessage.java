package model;
import java.time.LocalDateTime;

public class ContactMessage {
	private Integer messageId;
	private Integer userId;
	private String name;
	private String email;
	private String subject;
	private String message;
	private LocalDateTime submittedAt;
	private Boolean isDeleted;

	// Reference object
	private User user;

	// Constructors
	public ContactMessage() {
	}

	public ContactMessage(Integer messageId, Integer userId, String name, String email, String subject, String message,
			LocalDateTime submittedAt, Boolean isDeleted) {
		this.messageId = messageId;
		this.userId = userId;
		this.name = name;
		this.email = email;
		this.subject = subject;
		this.message = message;
		this.submittedAt = submittedAt;
		this.isDeleted = isDeleted;
	}

	// Getters and Setters
	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		if (user != null) {
			this.userId = user.getUserId();
		}
	}

	@Override
	public String toString() {
		return "ContactMessage{" + "messageId=" + messageId + ", userId=" + userId + ", name='" + name + '\''
				+ ", email='" + email + '\'' + ", subject='" + subject + '\'' + ", submittedAt=" + submittedAt
				+ ", isDeleted=" + isDeleted + '}';
	}
}