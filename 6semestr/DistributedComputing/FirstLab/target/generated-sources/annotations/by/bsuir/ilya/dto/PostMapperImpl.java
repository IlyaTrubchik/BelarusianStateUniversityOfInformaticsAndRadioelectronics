package by.bsuir.ilya.dto;

import by.bsuir.ilya.Entity.Post;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-28T20:43:09+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
public class PostMapperImpl implements PostMapper {

    @Override
    public Post postResponseToToPost(PostResponseTo responseTo) {
        if ( responseTo == null ) {
            return null;
        }

        Post post = new Post();

        post.setId( responseTo.getId() );
        post.setContent( responseTo.getContent() );
        post.setIssueId( responseTo.getIssueId() );

        return post;
    }

    @Override
    public Post postRequestToToPost(PostRequestTo requestTo) {
        if ( requestTo == null ) {
            return null;
        }

        Post post = new Post();

        post.setId( requestTo.getId() );
        post.setContent( requestTo.getContent() );
        post.setIssueId( requestTo.getIssueId() );

        return post;
    }

    @Override
    public PostRequestTo postToPostRequestTo(Post post) {
        if ( post == null ) {
            return null;
        }

        PostRequestTo postRequestTo = new PostRequestTo();

        postRequestTo.setIssueId( post.getIssueId() );
        postRequestTo.setContent( post.getContent() );
        postRequestTo.setId( post.getId() );

        return postRequestTo;
    }

    @Override
    public PostResponseTo postToPostResponseTo(Post post) {
        if ( post == null ) {
            return null;
        }

        PostResponseTo postResponseTo = new PostResponseTo();

        postResponseTo.setId( post.getId() );
        postResponseTo.setContent( post.getContent() );
        postResponseTo.setIssueId( post.getIssueId() );

        return postResponseTo;
    }
}
