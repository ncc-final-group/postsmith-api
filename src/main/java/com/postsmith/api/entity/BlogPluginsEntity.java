package com.postsmith.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "blog_plugins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogPluginsEntity {
	@EmbeddedId
	private BlogPluginsId id; // PK > blog_plugins.blog_id, blog_plugins.plugin_id

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("blogId")
	@JoinColumn(name = "blog_id", referencedColumnName = "id")
	private BlogsEntity blog; // FK > users.id

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("pluginId")
	@JoinColumn(name = "plugin_id", referencedColumnName = "id")
	private PluginsEntity plugin; // FK > plugins.id

	@Column(name = "plugins_setting", columnDefinition = "TEXT")
	private String pluginsSetting; // 플러그인 설정(JavaScript)

	@Builder
	public BlogPluginsEntity(BlogsEntity blog, PluginsEntity plugin, String pluginsSetting) {
		this.id = BlogPluginsId.builder().blogId(blog.getId()).pluginId(plugin.getId()).build();
		this.blog = blog;
		this.plugin = plugin;
		this.pluginsSetting = pluginsSetting;
	}
}
