package by.bsuir.ilya.dto;

public class StickerRequestTo {
    private long id;
    private String name;
    private  long issueId;

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public long getIssueId() {
        return issueId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
