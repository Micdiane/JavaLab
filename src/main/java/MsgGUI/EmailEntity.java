package MsgGUI;

import java.io.Serializable;

public class EmailEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String recipient;
    private String subject;
    private String content;
    private String id;

    // 构造函数需要传入唯一标识符
    public EmailEntity(String id, String recipient, String subject, String content) {
        this.id = id;
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
    }
    public String getId() {
        return id;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "EmailEntity{" +
                "recipient='" + recipient + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
