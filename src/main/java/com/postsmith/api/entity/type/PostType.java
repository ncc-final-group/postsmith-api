package com.postsmith.api.entity.type;

public enum PostType {
	POSTS("posts"), PAGE("page"), NOTICE("notice");

	private String type;

	PostType(String type) {
		this.type = type;
	}

	public static PostType fromString(String type) {
		for (PostType postType : PostType.values()) {
			if (postType.type.equalsIgnoreCase(type)) {
				return postType;
			}
		}
		throw new IllegalArgumentException("No matching PostType found for type: " + type);
	}
}
