package com.almighty.data.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestData {
	@Schema(hidden = true)
	private String id;
	private String text;
	@Schema(hidden = true)
	private String parentId;
	@Schema(hidden = true)
	private Integer page;
	@Schema(hidden = true)
	private Integer pageSize;
}
