package by.bsuir.ilya.dto;

import by.bsuir.ilya.Entity.Issue;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IssueMapper {
    IssueMapper INSTANCE = Mappers.getMapper( IssueMapper.class );


    IssueRequestTo issueToIssueRequestTo(Issue issue);
    IssueResponseTo issueToIssueResponseTo(Issue issue);

    Issue issueResponseToToIssue(IssueResponseTo responseTo);

    Issue issueRequestToToIssue(IssueRequestTo requestTo);
}
