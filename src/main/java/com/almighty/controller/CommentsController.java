package com.almighty.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.almighty.data.request.CommentRequestData;
import com.almighty.data.response.ResponseData;
import com.almighty.service.CommentsService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comments Controller")
public class CommentsController {

	private final CommentsService service;

	@GetMapping
	public ResponseEntity<ResponseData> getAllComments(@RequestParam(required = false) String parentId,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int pageSize) {
		return ResponseEntity.ok(service
				.allComments(CommentRequestData.builder().parentId(parentId).page(page).pageSize(pageSize).build()));
	}

	@PostMapping
	public ResponseEntity<ResponseData> saveComment(@RequestBody CommentRequestData requestData,
			@RequestParam(required = false) String parentId) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.saveOrUpdateComment(
				CommentRequestData.builder().parentId(parentId).text(requestData.getText()).build()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseData> updateComment(@PathVariable String id,
			@RequestBody CommentRequestData requestData) {
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(service.saveOrUpdateComment(CommentRequestData.builder().id(id).text(requestData.getText()).build()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseData> deleteComment(@PathVariable String id) {
		return ResponseEntity.ok(service.deleteComment(id));
	}
}
