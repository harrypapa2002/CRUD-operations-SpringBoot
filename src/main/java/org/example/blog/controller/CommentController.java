package org.example.blog.controller;

import org.example.blog.dto.CommentDto;
import org.example.blog.service.CommentService;
import org.example.blog.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Operation(summary = "Get all comments for a post", description = "Retrieve a list of all comments for a specific post.")
    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments(@PathVariable Long postId) {
        try {
            List<CommentDto> comments = commentService.getAllCommentsByPostId(postId);
            return ResponseEntity.ok(comments);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Post with id " + postId + " not found.");
        }
    }

    @Operation(summary = "Get a comment by ID for a post", description = "Retrieve a specific comment by its ID for a specific post.")
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long postId, @PathVariable Long id) {
        try {
            CommentDto comment = commentService.getCommentById(postId, id);
            return ResponseEntity.ok(comment);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Comment with id " + id + " not found.");
        }
    }

    @Operation(summary = "Create a new comment for a post", description = "Create a new comment for a specific post.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Comment data to create.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "CreateCommentExample",
                                    summary = "Example comment without ID",
                                    value = "{ \"content\": \"This is a comment.\" }"
                            )
                    )
            )
    )
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto) {
        CommentDto createdComment = commentService.createComment(postId, commentDto);
        return ResponseEntity.status(201).body(createdComment);
    }

    @Operation(summary = "Update a comment for a post", description = "Update a comment for a specific post by comment ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Comment data to update.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "UpdateCommentExample",
                                    summary = "Example comment without ID",
                                    value = "{ \"content\": \"This is an updated comment.\" }"
                            )
                    )
            )
    )
    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long postId, @PathVariable Long id, @RequestBody CommentDto commentDto) {
        try {
            CommentDto updatedComment = commentService.updateComment(postId, id, commentDto);
            return ResponseEntity.ok(updatedComment);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Comment with id " + id + " not found.");
        }
    }

    @Operation(summary = "Delete a comment for a post", description = "Delete a comment for a specific post by comment ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable Long postId, @PathVariable Long id) {
        try {
            commentService.deleteComment(postId, id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Comment with id " + id + " not found.");
        }
    }

    @Operation(summary = "Delete all comments for a post", description = "Delete all comments for a specific post.")
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteAllComments(@PathVariable Long postId) {
        try {
            commentService.deleteAllComments(postId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "All comments deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Post with id " + postId + " not found.");
        }
    }
}
