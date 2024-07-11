package org.example.blog.controller

import org.example.blog.dto.PostDto
import org.example.blog.exception.ResourceNotFoundException
import org.example.blog.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class PostControllerSpec extends Specification {

    PostService postService = Mock()
    PostController postController = new PostController(postService: postService)

    def "getAllPosts should return list of all posts"() {
        given:
        List<PostDto> postList = [new PostDto(title: "Test post", content: "Content")]
        postService.getAllPosts() >> postList

        when:
        ResponseEntity<List<PostDto>> response = postController.getAllPosts()

        then:
        response.statusCode == HttpStatus.OK
        response.body == postList
    }

    def "getPostById should return a post by its ID"() {
        given:
        Long postId = 1L
        PostDto post = new PostDto(title: "Test post", content: "Content")
        postService.getPostById(postId) >> post

        when:
        ResponseEntity<PostDto> response = postController.getPostById(postId)

        then:
        response.statusCode == HttpStatus.OK
        response.body == post
    }

    def "getPostById should throw ResourceNotFoundException if post not found"() {
        given:
        Long postId = 1L
        postService.getPostById(postId) >> { throw new ResourceNotFoundException("Post with id $postId not found.") }

        when:
        postController.getPostById(postId)

        then:
        thrown(ResourceNotFoundException)
    }

    def "createPost should create a new post"() {
        given:
        PostDto newPost = new PostDto(title: "New post", content: "Content")
        PostDto createdPost = new PostDto(title: "New post", content: "Content")
        postService.createPost(newPost) >> createdPost

        when:
        ResponseEntity<PostDto> response = postController.createPost(newPost)

        then:
        response.statusCode == HttpStatus.CREATED
        response.body == createdPost
    }

    def "updatePost should update an existing post by its ID"() {
        given:
        Long postId = 1L
        PostDto updatePost = new PostDto(title: "Updated post", content: "Updated content")
        PostDto updatedPost = new PostDto(title: "Updated post", content: "Updated content")
        postService.updatePost(postId, updatePost) >> updatedPost

        when:
        ResponseEntity<PostDto> response = postController.updatePost(postId, updatePost)

        then:
        response.statusCode == HttpStatus.OK
        response.body == updatedPost
    }

    def "updatePost should throw ResourceNotFoundException if post not found"() {
        given:
        Long postId = 1L
        PostDto updatePost = new PostDto(title: "Updated post", content: "Updated content")
        postService.updatePost(postId, updatePost) >> { throw new ResourceNotFoundException("Post with id $postId not found.") }

        when:
        postController.updatePost(postId, updatePost)

        then:
        thrown(ResourceNotFoundException)
    }

    def "deletePost should delete a post by its ID"() {
        given:
        Long postId = 1L
        postService.deletePost(postId) >> {}

        when:
        ResponseEntity<Map<String, String>> response = postController.deletePost(postId)

        then:
        response.statusCode == HttpStatus.OK
        response.body == [message: "Post deleted successfully."]
    }

    def "deletePost should throw ResourceNotFoundException if post not found"() {
        given:
        Long postId = 1L
        postService.deletePost(postId) >> { throw new ResourceNotFoundException("Post with id $postId not found.") }

        when:
        postController.deletePost(postId)

        then:
        thrown(ResourceNotFoundException)
    }
}
