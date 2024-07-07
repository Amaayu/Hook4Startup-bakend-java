package com.cms.Dto;


import com.cms.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {
    @DBRef
    private Post postId;
    private  String  content;

}