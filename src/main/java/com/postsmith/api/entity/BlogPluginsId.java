package com.postsmith.api.entity;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogPluginsId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "blog_id", nullable = false)
	private Integer blogId; // FK > blogs.id

	@Column(name = "plugin_id", nullable = false)
	private Integer pluginId; // FK > plugins.id

	@Builder
	public BlogPluginsId(Integer blogId, Integer pluginId) {
		this.blogId = blogId;
		this.pluginId = pluginId;
	}
}
