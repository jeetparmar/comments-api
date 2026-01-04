package com.almighty.data.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CommentResponseData extends ResponseData {
    private String id;
    private String text;
    private String parentId;
    private Date postedAt;
}
