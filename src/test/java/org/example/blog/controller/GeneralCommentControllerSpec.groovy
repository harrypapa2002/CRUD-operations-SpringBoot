package org.example.blog.controller

import org.example.blog.dto.CommentDto
import org.example.blog.exception.ResourceNotFoundException
import org.example.blog.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class GeneralCommentControllerSpec extends Specification {

    CommentService commentService = Mock()
    GeneralCommentController generalCommentController = new GeneralCommentController(commentService: commentService)

    def "getAllComments should return list of all comments"() {
        given:
        List<CommentDto> commentList = [new CommentDto(content: "Test comment")]
        commentService.getAllComments() >> commentList

        when:
        ResponseEntity<List<CommentDto>> response = generalCommentController.getAllComments()

        then:
        response.statusCode == HttpStatus.OK
        response.body == commentList
    }

    def "getCommentById should return a comment by its ID"() {
        given:
        Long commentId = 1L
        CommentDto comment = new CommentDto(content: "Test comment")
        commentService.getCommentById(commentId) >> comment

        when:
        ResponseEntity<CommentDto> response = generalCommentController.getCommentById(commentId)

        then:
        response.statusCode == HttpStatus.OK
        response.body == comment
    }

    def "getCommentById should throw ResourceNotFoundException if comment not found"() {
        given:
        Long commentId = 1L
        commentService.getCommentById(commentId) >> { throw new ResourceNotFoundException("Comment with id $commentId not found.") }

        when:
        generalCommentController.getCommentById(commentId)

        then:
        thrown(ResourceNotFoundException)
    }
}
