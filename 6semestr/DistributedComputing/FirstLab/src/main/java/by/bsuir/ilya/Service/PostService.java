package by.bsuir.ilya.Service;


import by.bsuir.ilya.Entity.Post;
import by.bsuir.ilya.dto.*;
import by.bsuir.ilya.storage.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PostService implements IService<PostResponseTo, PostRequestTo> {
    @Autowired
    private PostRepository postRepository;

    public PostService(){

    }
    public List<PostResponseTo> getAll(){
        List<Post> postList = postRepository.findAll();
        List<PostResponseTo> resultList = new ArrayList<>();
        for(int i = 0;i<postList.size();i++)
        {
            resultList.add(PostMapper.INSTANCE.postToPostResponseTo(postList.get(i)));
        }
        return resultList;
    }
    public PostResponseTo update(PostRequestTo updatingpost){
        Post post = PostMapper.INSTANCE.postRequestToToPost(updatingpost);
        if(validatePost(post)) {
            boolean result = postRepository.update(post);
            PostResponseTo responseTo = result ? PostMapper.INSTANCE.postToPostResponseTo(post) : null;
            return  responseTo;
        }
        else return new PostResponseTo();
        //return responseTo;
    }
    public PostResponseTo getById(long id){
        return PostMapper.INSTANCE.postToPostResponseTo(postRepository.findById(id));
    }
    public PostResponseTo deleteById(long id)
    {
        return PostMapper.INSTANCE.postToPostResponseTo(postRepository.deleteById(id));
    }
    public PostResponseTo add(PostRequestTo postRequestTo)
    {
        Post post = PostMapper.INSTANCE.postRequestToToPost(postRequestTo);
        return PostMapper.INSTANCE.postToPostResponseTo(postRepository.insert(post));
    }
    private boolean validatePost(Post post)
    {
        String content = post.getContent();
        if(content.length()>=2 && content.length()<=2048) return  true;
        return false;
    }
}
