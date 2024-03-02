package by.bsuir.ilya.dto;

import java.sql.Date;

public class IssueRequestTo {
    private long id;

    private long userId;

    private String title;
    private String content;
    private Date created;
    private Date modified;

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getModified() {
        return modified;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public long getUserId() {
        return userId;
    }

    public Date getCreated() {
        return created;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
