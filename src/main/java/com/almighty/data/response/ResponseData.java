package com.almighty.data.response;

import com.almighty.data.enums.Status;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ResponseData {
	private Status status;
	private String message;
}
