package com.almighty.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.almighty.data.enums.Status;
import com.almighty.data.request.CommentRequestData;
import com.almighty.data.response.CommentData;
import com.almighty.data.response.CommentResponseData;
import com.almighty.data.response.CommentsResponseData;
import com.almighty.data.response.ResponseData;
import com.almighty.domain.Comments;
import com.almighty.repository.CommentsRepository;
import com.almighty.service.CommentsService;

import lombok.RequiredArgsConstructor;

import static com.almighty.constants.CommentMessages.*;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {

	private final CommentsRepository repository;

	@Override
	public ResponseData getComment(String id) {
		Comments comment = repository.findById(id).orElse(null);
		if (comment == null) {
			return ResponseData.builder().status(Status.FAILURE).message(COMMENT_ID_INVALID).build();
		}
		return CommentResponseData.builder().id(comment.getId()).text(comment.getText()).parentId(comment.getParentId())
				.postedAt(comment.getUpdatedAt()).status(Status.SUCCESS).message(COMMENT_FETCHED_SUCCESS).build();
	}

	@Override
	public ResponseData getComments(CommentRequestData requestData) {
		int page = requestData.getPage() != null && requestData.getPage() > 0 ? requestData.getPage() - 1 : 0;
		int pageSize = requestData.getPageSize() != null && requestData.getPageSize() > 0 ? requestData.getPageSize()
				: 5;

		var pageable = PageRequest.of(page, pageSize, Sort.by("updatedAt").descending());
		Page<Comments> commentsPage;
		if (StringUtils.hasText(requestData.getParentId())) {
			if (StringUtils.hasText(requestData.getText())) {
				commentsPage = repository.findAllByParentIdAndTextContainingIgnoreCase(requestData.getParentId(),
						requestData.getText(), pageable);
			} else {
				commentsPage = repository.findAllByParentId(requestData.getParentId(), pageable);
			}
		} else {
			if (StringUtils.hasText(requestData.getText())) {
				commentsPage = repository.findAllByParentIdIsNullAndTextContainingIgnoreCase(requestData.getText(),
						pageable);
			} else {
				commentsPage = repository.findAllByParentIdIsNull(pageable);
			}
		}
		List<CommentData> comments = commentsPage.getContent().stream()
				.map(comment -> CommentData.builder().id(comment.getId()).text(comment.getText())
						.parentId(comment.getParentId()).totalSubComments(comment.getTotalSubComments())
						.postedAt(comment.getUpdatedAt()).build())
				.collect(Collectors.toList());

		return CommentsResponseData.builder().totalComments(commentsPage.getTotalElements()).comments(comments)
				.status(Status.SUCCESS)
				.message(String.format(COMMENTS_FETCHED_SUCCESS, commentsPage.getTotalElements())).build();
	}

	@Override
	public ResponseData saveOrUpdateComment(CommentRequestData requestData) {
		Date now = new Date();
		Comments comment;

		if (StringUtils.hasText(requestData.getId())) {
			comment = repository.findById(requestData.getId()).orElse(null);

			if (comment == null) {
				return ResponseData.builder().status(Status.FAILURE).message(COMMENT_ID_INVALID).build();
			}

			comment.setText(requestData.getText());
			comment.setUpdatedAt(now);
			repository.save(comment);

			return CommentResponseData.builder().id(comment.getId()).text(comment.getText())
					.parentId(comment.getParentId()).postedAt(now).status(Status.SUCCESS)
					.message(COMMENT_UPDATED_SUCCESS).build();

		} else {
			comment = Comments.builder().text(requestData.getText()).createdAt(now).updatedAt(now).build();

			if (StringUtils.hasText(requestData.getParentId())) {
				Comments parent = repository.findById(requestData.getParentId()).orElse(null);
				if (parent == null) {
					return ResponseData.builder().status(Status.FAILURE).message(PARENT_COMMENT_ID_INVALID).build();
				}
				parent.setTotalSubComments(parent.getTotalSubComments() + 1);
				parent.setUpdatedAt(now);
				repository.save(parent);
				comment.setParentId(parent.getId());
			}

			comment = repository.save(comment);

			return CommentResponseData.builder().id(comment.getId()).text(comment.getText())
					.parentId(comment.getParentId()).postedAt(now).status(Status.SUCCESS).message(COMMENT_SAVED_SUCCESS)
					.build();
		}
	}

	@Override
	public ResponseData deleteComment(String id) {
		Comments comment = repository.findById(id).orElse(null);

		if (comment != null) {
			repository.delete(comment);
			return ResponseData.builder().status(Status.SUCCESS).message(COMMENT_DELETED_SUCCESS).build();
		} else {
			return ResponseData.builder().status(Status.FAILURE).message(COMMENT_ID_INVALID).build();
		}
	}
}
