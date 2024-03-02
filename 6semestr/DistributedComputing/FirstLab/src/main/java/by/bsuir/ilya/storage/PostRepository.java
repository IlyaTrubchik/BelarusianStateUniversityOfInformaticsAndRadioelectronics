package by.bsuir.ilya.storage;

import by.bsuir.ilya.Entity.Post;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
public class PostRepository implements  InMemoryRepository<Post> {
    private Map<Long,Post> postMemory = new HashMap<>();

    long lastId = 0;
    @Override
    public Post findById(long id) {
        Post post = postMemory.get(id);
        if(post!=null) {
            post.setId(id);
        }
        return post;
    }

    @Override
    public List<Post> findAll() {
        List<Post> postList = new ArrayList<>();
        for (Long key:postMemory.keySet()) {
            Post post = postMemory.get(key);;
            post.setId(key);
            postList.add(post);
        }
        return postList;
    }

    @Override
    public Post deleteById(long id) {
        Post result = postMemory.remove(id);
        if(result!=null) lastId--;
        return result;
    }

    @Override
    public boolean deleteAll() {
        postMemory = new HashMap<>();
        lastId = 0;
        return true;
    }

    @Override
    public Post insert(Post insertObject) {
        postMemory.put(lastId,insertObject);
        lastId++;
        return insertObject;
    }

    @Override
    public boolean updateById(Long id, Post updatingPost) {
        boolean status = postMemory.replace(id,postMemory.get(id),updatingPost);
        return status;
    }

    @Override
    public boolean update(Post updatingValue) {
        boolean status = postMemory.replace(updatingValue.getId(),postMemory.get(updatingValue.getId()),updatingValue);
        return status;
    }


}
