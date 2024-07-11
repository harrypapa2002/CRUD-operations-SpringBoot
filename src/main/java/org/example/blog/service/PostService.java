package org.example.blog.service;

import org.example.blog.dto.PostDto;
import org.example.blog.mapper.PostMapper;
import org.example.blog.model.Post;
import org.example.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.blog.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostMapper::toDto)
                .collect(Collectors.toList());
    }

    public PostDto getPostById(Long id) {
        return postRepository.findById(id)
                .map(PostMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    public PostDto createPost(PostDto postDto) {
        Post post = PostMapper.toEntity(postDto);
        Post savedPost = postRepository.save(post);
        return PostMapper.toDto(savedPost);
    }

    public PostDto updatePost(Long id, PostDto postDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return PostMapper.toDto(updatedPost);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        postRepository.delete(post);
    }
}
