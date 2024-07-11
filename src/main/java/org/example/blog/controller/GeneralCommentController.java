package org.example.blog.controller;

import org.example.blog.dto.CommentDto;
import org.example.blog.service.CommentService;
import org.example.blog.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class GeneralCommentController {

    @Autowired
    private CommentService commentService;

    @Operation(summary = "Get all comments", description = "Retrieve a list of all comments.")
    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments() {
        List<CommentDto> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Get a comment by ID", description = "Retrieve a specific comment by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(
            @Parameter(description = "ID of the comment to be retrieved") @PathVariable Long id) {
        try {
            CommentDto comment = commentService.getCommentById(id);
            return ResponseEntity.ok(comment);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Comment with id " + id + " not found.");
        }
    }
}
