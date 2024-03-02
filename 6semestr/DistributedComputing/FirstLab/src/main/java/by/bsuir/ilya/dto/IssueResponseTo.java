package by.bsuir.ilya.dto;

import java.sql.Date;

public class IssueResponseTo {
    private long id;

    private long userId;

    private String title;
    private String content;
    private Date created;
    private Date modified;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    public long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
