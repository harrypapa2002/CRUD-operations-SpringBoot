package org.example.blog.service;

import org.example.blog.dto.CommentDto;
import org.example.blog.mapper.CommentMapper;
import org.example.blog.model.Comment;
import org.example.blog.model.Post;
import org.example.blog.repository.CommentRepository;
import org.example.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.blog.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public List<CommentDto> getAllComments() {
        return commentRepository.findAll().stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return CommentMapper.toDto(comment);
    }

    public CommentDto getCommentById(Long postId, Long id) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (!comment.getPost().getId().equals(post.getId())) {
            throw new ResourceNotFoundException("Comment does not belong to the specified post");
        }

        return CommentMapper.toDto(comment);
    }

    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        Comment comment = CommentMapper.toEntity(commentDto);
        comment.setPost(post);
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    public CommentDto updateComment(Long postId, Long id, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        comment.setContent(commentDto.getContent());
        comment.setPost(post);
        return CommentMapper.toDto(commentRepository.save(comment));
    }

    public void deleteComment(Long postId, Long id) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        if (!comment.getPost().getId().equals(post.getId())) {
            throw new ResourceNotFoundException("Comment does not belong to the specified post");
        }
        commentRepository.delete(comment);
    }

    public void deleteAllComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        List<Comment> comments = commentRepository.findByPost(post);
        commentRepository.deleteAll(comments);
    }

    public List<CommentDto> getAllCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return commentRepository.findByPost(post).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
}
