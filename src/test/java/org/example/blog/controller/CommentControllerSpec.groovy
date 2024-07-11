package org.example.blog.controller

import org.example.blog.dto.CommentDto
import org.example.blog.exception.ResourceNotFoundException
import org.example.blog.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class CommentControllerSpec extends Specification {

    CommentService commentService = Mock()
    CommentController commentController = new CommentController(commentService: commentService)

    def "getAllComments should return list of comments for a given post ID"() {
        given:
        Long postId = 1L
        List<CommentDto> commentList = [new CommentDto(content: "Test comment")]
        commentService.getAllCommentsByPostId(postId) >> commentList

        when:
        ResponseEntity<List<CommentDto>> response = commentController.getAllComments(postId)

        then:
        response.statusCode == HttpStatus.OK
        response.body == commentList
    }

    def "getAllComments should throw ResourceNotFoundException if post not found"() {
        given:
        Long postId = 1L
        commentService.getAllCommentsByPostId(postId) >> { throw new ResourceNotFoundException("Post with id $postId not found.") }

        when:
        commentController.getAllComments(postId)

        then:
        thrown(ResourceNotFoundException)
    }

    def "getCommentById should return a comment for a given post and comment ID"() {
        given:
        Long postId = 1L
        Long commentId = 1L
        CommentDto comment = new CommentDto(content: "Test comment")
        commentService.getCommentById(postId, commentId) >> comment

        when:
        ResponseEntity<CommentDto> response = commentController.getCommentById(postId, commentId)

        then:
        response.statusCode == HttpStatus.OK
        response.body == comment
    }

    def "getCommentById should throw ResourceNotFoundException if comment not found"() {
        given:
        Long postId = 1L
        Long commentId = 1L
        commentService.getCommentById(postId, commentId) >> { throw new ResourceNotFoundException("Comment with id $commentId not found.") }

        when:
        commentController.getCommentById(postId, commentId)

        then:
        thrown(ResourceNotFoundException)
    }

    def "createComment should create a new comment for a given post"() {
        given:
        Long postId = 1L
        CommentDto newComment = new CommentDto(content: "New comment")
        CommentDto createdComment = new CommentDto(content: "New comment")
        commentService.createComment(postId, newComment) >> createdComment

        when:
        ResponseEntity<CommentDto> response = commentController.createComment(postId, newComment)

        then:
        response.statusCode == HttpStatus.CREATED
        response.body == createdComment
    }

    def "updateComment should update an existing comment for a given post and comment ID"() {
        given:
        Long postId = 1L
        Long commentId = 1L
        CommentDto updateComment = new CommentDto(content: "Updated comment")
        CommentDto updatedComment = new CommentDto(content: "Updated comment")
        commentService.updateComment(postId, commentId, updateComment) >> updatedComment

        when:
        ResponseEntity<CommentDto> response = commentController.updateComment(postId, commentId, updateComment)

        then:
        response.statusCode == HttpStatus.OK
        response.body == updatedComment
    }

    def "updateComment should throw ResourceNotFoundException if comment not found"() {
        given:
        Long postId = 1L
        Long commentId = 1L
        CommentDto updateComment = new CommentDto(content: "Updated comment")
        commentService.updateComment(postId, commentId, updateComment) >> { throw new ResourceNotFoundException("Comment with id $commentId not found.") }

        when:
        commentController.updateComment(postId, commentId, updateComment)

        then:
        thrown(ResourceNotFoundException)
    }

    def "deleteComment should delete a comment for a given post and comment ID"() {
        given:
        Long postId = 1L
        Long commentId = 1L
        commentService.deleteComment(postId, commentId) >> {}

        when:
        ResponseEntity<Map<String, String>> response = commentController.deleteComment(postId, commentId)

        then:
        response.statusCode == HttpStatus.OK
        response.body == [message: "Comment deleted successfully."]
    }

    def "deleteComment should throw ResourceNotFoundException if comment not found"() {
        given:
        Long postId = 1L
        Long commentId = 1L
        commentService.deleteComment(postId, commentId) >> { throw new ResourceNotFoundException("Comment with id $commentId not found.") }

        when:
        commentController.deleteComment(postId, commentId)

        then:
        thrown(ResourceNotFoundException)
    }

    def "deleteAllComments should delete all comments for a given post"() {
        given:
        Long postId = 1L
        commentService.deleteAllComments(postId) >> {}

        when:
        ResponseEntity<Map<String, String>> response = commentController.deleteAllComments(postId)

        then:
        response.statusCode == HttpStatus.OK
        response.body == [message: "All comments deleted successfully."]
    }

    def "deleteAllComments should throw ResourceNotFoundException if post not found"() {
        given:
        Long postId = 1L
        commentService.deleteAllComments(postId) >> { throw new ResourceNotFoundException("Post with id $postId not found.") }

        when:
        commentController.deleteAllComments(postId)

        then:
        thrown(ResourceNotFoundException)
    }
}
