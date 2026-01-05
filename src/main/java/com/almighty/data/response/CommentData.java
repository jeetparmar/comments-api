package com.almighty.data.response;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentData {
    private String id;
    private String text;
    private String parentId;
    private Integer totalSubComments;
    private Date postedAt;
}
