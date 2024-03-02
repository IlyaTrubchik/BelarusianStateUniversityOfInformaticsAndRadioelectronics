package by.bsuir.ilya.dto;

public class PostRequestTo {
    private long id;
    private long issueId;
    private String content;

    public String getContent() {
        return content;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getIssueId() {
        return issueId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
