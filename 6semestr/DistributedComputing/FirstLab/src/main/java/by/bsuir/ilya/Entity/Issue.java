package by.bsuir.ilya.Entity;

import java.sql.Date;

public class Issue {
    private long id;

    private long userId;

    private String title;
    private String content;
    private Date created;
    private Date modified;

    public String getTitle() {
        return title;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    public String getContent() {
        return content;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
