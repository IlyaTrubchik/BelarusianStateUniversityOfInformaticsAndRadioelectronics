package by.bsuir.ilya.Service;

import by.bsuir.ilya.Entity.Issue;
import by.bsuir.ilya.dto.*;
import by.bsuir.ilya.storage.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class IssueService implements IService<IssueResponseTo, IssueRequestTo> {
    @Autowired
    private IssueRepository issueRepository;

    public IssueService(){

    }
    public List<IssueResponseTo> getAll(){
        List<Issue> issueList = issueRepository.findAll();
        List<IssueResponseTo> resultList = new ArrayList<>();
        for(int i = 0;i<issueList.size();i++)
        {
            resultList.add(IssueMapper.INSTANCE.issueToIssueResponseTo(issueList.get(i)));
        }
        return resultList;
    }
    public IssueResponseTo update(IssueRequestTo updatingIssue){
        Issue issue = IssueMapper.INSTANCE.issueRequestToToIssue(updatingIssue);
        if(validateIssue(issue)) {
            boolean result = issueRepository.update(issue);
            IssueResponseTo responseTo = result ? IssueMapper.INSTANCE.issueToIssueResponseTo(issue) : null;
            return  responseTo;
        }
        else return new IssueResponseTo();
        //return responseTo;
    }
    public IssueResponseTo getById(long id){
        return IssueMapper.INSTANCE.issueToIssueResponseTo(issueRepository.findById(id));
    }
    public IssueResponseTo deleteById(long id)
    {
        return IssueMapper.INSTANCE.issueToIssueResponseTo(issueRepository.deleteById(id));
    }
    public IssueResponseTo add(IssueRequestTo issueRequestTo)
    {
        Issue issue = IssueMapper.INSTANCE.issueRequestToToIssue(issueRequestTo);
        return IssueMapper.INSTANCE.issueToIssueResponseTo(issueRepository.insert(issue));
    }
    private boolean validateIssue(Issue issue)
    {
        String title = issue.getTitle();
        String content = issue.getContent();

        if((content.length()>=4 && content.length()<=2048)&& (title.length()>=2 && title.length()<=64))
        {
            return true;
        }
        return false;
    }
}
