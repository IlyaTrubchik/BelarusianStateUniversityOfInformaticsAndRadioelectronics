package by.bsuir.ilya.dto;

public class PostResponseTo {
    private long id;
    private long issueId;
    private String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIssueId() {
        return issueId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }
}
