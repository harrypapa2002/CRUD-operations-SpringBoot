package org.example.blog.controller;

import org.example.blog.dto.PostDto;
import org.example.blog.service.PostService;
import org.example.blog.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Operation(summary = "Get all posts", description = "Retrieve a list of all posts.")
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Get a post by ID", description = "Retrieve a specific post by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(
            @Parameter(description = "ID of the post to be retrieved") @PathVariable Long id) {
        try {
            PostDto post = postService.getPostById(id);
            return ResponseEntity.ok(post);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Post with id " + id + " not found.");
        }
    }

    @Operation(summary = "Create a new post", description = "Create a new post. Note: Comments should not be included in the request body.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Post data to create. Comments should not be included.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "CreatePostExample",
                                    summary = "Example post without comments",
                                    value = "{ \"title\": \"Sample Post Title\", \"content\": \"This is the content of the post.\" }"
                            )
                    )
            )
    )
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        PostDto createdPost = postService.createPost(postDto);
        return ResponseEntity.status(201).body(createdPost);
    }

    @Operation(summary = "Update a post", description = "Update a post by ID. Note: Comments should not be included in the request body.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Post data to update. Comments should not be included.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "UpdatePostExample",
                                    summary = "Example post without comments",
                                    value = "{ \"title\": \"Updated Post Title\", \"content\": \"This is the updated content of the post.\" }"
                            )
                    )
            )
    )
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @Parameter(description = "ID of the post to be updated") @PathVariable Long id,
            @RequestBody PostDto postDto) {
        try {
            PostDto updatedPost = postService.updatePost(id, postDto);
            return ResponseEntity.ok(updatedPost);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Post with id " + id + " not found.");
        }
    }

    @Operation(summary = "Delete a post", description = "Delete a post by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePost(
            @Parameter(description = "ID of the post to be deleted") @PathVariable Long id) {
        try {
            postService.deletePost(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Post deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Post with id " + id + " not found.");
        }
    }
}
