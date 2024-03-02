package by.bsuir.ilya.Entity;

public class Sticker {
    private long id;
    private String name;
    private long issueId;
    public Sticker()
    {

    }

    public long getIssueId() {
        return issueId;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

