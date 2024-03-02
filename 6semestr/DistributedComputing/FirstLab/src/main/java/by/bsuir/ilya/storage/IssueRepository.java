package by.bsuir.ilya.storage;

import by.bsuir.ilya.Entity.Issue;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class IssueRepository implements  InMemoryRepository<Issue>{


    private Map<Long, Issue> issueMemory = new HashMap<>();

    long lastId = 0;
    @Override
    public Issue findById(long id) {
        Issue issue = issueMemory.get(id);
        if(issue!=null) {
            issue.setId(id);
        }
        return issue;
    }

    @Override
    public List<Issue> findAll() {
        List<Issue> issueList = new ArrayList<>();
        for (Long key:issueMemory.keySet()) {
            Issue issue = issueMemory.get(key);;
            issue.setId(key);
            issueList.add(issue);
        }
        return issueList;
    }

    @Override
    public Issue deleteById(long id) {
        Issue result = issueMemory.remove(id);
        if(result!=null) lastId--;
        return result;
    }

    @Override
    public boolean deleteAll() {
        issueMemory = new HashMap<>();
        lastId = 0;
        return true;
    }

    @Override
    public Issue insert(Issue insertObject) {
        issueMemory.put(lastId,insertObject);
        lastId++;
        return insertObject;
    }

    @Override
    public boolean updateById(Long id, Issue updatingIssue) {
        boolean status = issueMemory.replace(id,issueMemory.get(id),updatingIssue);
        return status;
    }

    @Override
    public boolean update(Issue updatingValue) {
        boolean status = issueMemory.replace(updatingValue.getId(),issueMemory.get(updatingValue.getId()),updatingValue);
        return status;
    }

}
