package org.example.blog.mapper;

import org.example.blog.dto.PostDto;
import org.example.blog.model.Post;
import org.example.blog.model.Comment;
import org.example.blog.dto.CommentDto;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {

    public static PostDto toDto(Post post) {
        if (post == null) {
            return null;
        }

        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setComments(toCommentDtoList(post.getComments()));

        return postDto;
    }

    public static Post toEntity(PostDto postDto) {
        if (postDto == null) {
            return null;
        }

        Post post = new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setComments(toCommentEntityList(postDto.getComments(), post));

        return post;
    }

    private static List<CommentDto> toCommentDtoList(List<Comment> comments) {
        if (comments == null) {
            return null;
        }

        return comments.stream()
                .map(comment -> {
                    CommentDto dto = new CommentDto();
                    dto.setId(comment.getId());
                    dto.setContent(comment.getContent());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private static List<Comment> toCommentEntityList(List<CommentDto> commentDtos, Post post) {
        if (commentDtos == null) {
            return null;
        }

        return commentDtos.stream()
                .map(dto -> {
                    Comment comment = new Comment();
                    comment.setId(dto.getId());
                    comment.setContent(dto.getContent());
                    comment.setPost(post);
                    return comment;
                })
                .collect(Collectors.toList());
    }
}
