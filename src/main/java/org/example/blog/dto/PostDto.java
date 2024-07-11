package org.example.blog.dto;

import lombok.Data;
import java.util.List;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private List<CommentDto> comments;
}
