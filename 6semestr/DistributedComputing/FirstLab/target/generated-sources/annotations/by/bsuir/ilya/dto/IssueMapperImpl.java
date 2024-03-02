package by.bsuir.ilya.dto;

import by.bsuir.ilya.Entity.Issue;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-28T20:43:09+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
public class IssueMapperImpl implements IssueMapper {

    @Override
    public IssueRequestTo issueToIssueRequestTo(Issue issue) {
        if ( issue == null ) {
            return null;
        }

        IssueRequestTo issueRequestTo = new IssueRequestTo();

        issueRequestTo.setModified( issue.getModified() );
        issueRequestTo.setUserId( issue.getUserId() );
        issueRequestTo.setTitle( issue.getTitle() );
        issueRequestTo.setCreated( issue.getCreated() );
        issueRequestTo.setId( issue.getId() );
        issueRequestTo.setContent( issue.getContent() );

        return issueRequestTo;
    }

    @Override
    public IssueResponseTo issueToIssueResponseTo(Issue issue) {
        if ( issue == null ) {
            return null;
        }

        IssueResponseTo issueResponseTo = new IssueResponseTo();

        issueResponseTo.setId( issue.getId() );
        issueResponseTo.setContent( issue.getContent() );
        issueResponseTo.setCreated( issue.getCreated() );
        issueResponseTo.setModified( issue.getModified() );
        issueResponseTo.setTitle( issue.getTitle() );
        issueResponseTo.setUserId( issue.getUserId() );

        return issueResponseTo;
    }

    @Override
    public Issue issueResponseToToIssue(IssueResponseTo responseTo) {
        if ( responseTo == null ) {
            return null;
        }

        Issue issue = new Issue();

        issue.setUserId( responseTo.getUserId() );
        issue.setId( responseTo.getId() );
        issue.setModified( responseTo.getModified() );
        issue.setContent( responseTo.getContent() );
        issue.setCreated( responseTo.getCreated() );
        issue.setTitle( responseTo.getTitle() );

        return issue;
    }

    @Override
    public Issue issueRequestToToIssue(IssueRequestTo requestTo) {
        if ( requestTo == null ) {
            return null;
        }

        Issue issue = new Issue();

        issue.setUserId( requestTo.getUserId() );
        issue.setId( requestTo.getId() );
        issue.setModified( requestTo.getModified() );
        issue.setContent( requestTo.getContent() );
        issue.setCreated( requestTo.getCreated() );
        issue.setTitle( requestTo.getTitle() );

        return issue;
    }
}
