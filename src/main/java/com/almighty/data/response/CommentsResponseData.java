package com.almighty.data.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CommentsResponseData extends ResponseData {

	private long totalComments;
	private List<CommentData> comments;
}
