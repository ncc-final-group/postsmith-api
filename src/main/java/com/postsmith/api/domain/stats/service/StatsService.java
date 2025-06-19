package com.postsmith.api.domain.stats.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.postsmith.api.repository.ContentViewsRepository;
import com.postsmith.api.repository.ContentVisitsRepository;
import com.postsmith.api.domain.stats.dto.ViewStatsDto;
import com.postsmith.api.domain.stats.dto.VisitDto;
import com.postsmith.api.domain.stats.dto.ViewDto;
import com.postsmith.api.domain.stats.dto.VisitStatsDto;

@Service
public class StatsService {

	private final ContentViewsRepository contentViewsRepository;
	private final ContentVisitsRepository contentVisitsRepository;

	public StatsService(ContentViewsRepository contentViewsRepository, ContentVisitsRepository contentVisitsRepository) {
		this.contentViewsRepository = contentViewsRepository;
		this.contentVisitsRepository = contentVisitsRepository;
	}

	public ViewStatsDto getViewStatsByBlogId(Integer blogId) {
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);

		Integer todayViews = contentViewsRepository.viewsByBlogIdAndCreatedOn(blogId, today);
		Integer yesterdayViews = contentViewsRepository.viewsByBlogIdAndCreatedOn(blogId, yesterday);
		Integer totalViews = contentViewsRepository.totalViewsByBlogId(blogId);

		ViewStatsDto dto = new ViewStatsDto();
		dto.setBlogId(blogId);
		dto.setTodayViewCount(todayViews != null ? todayViews : 0);
		dto.setYesterdayViewCount(yesterdayViews != null ? yesterdayViews : 0);
		dto.setTotalViewCount(totalViews != null ? totalViews : 0);
		dto.setToday(today);

		return dto;
	}

	public VisitStatsDto getVisitStatsByBlogId(Integer blogId) {
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);

		Integer todayCount = contentVisitsRepository.countVisitorsByBlogIdAndCreatedOn(blogId, today);
		Integer yesterdayCount = contentVisitsRepository.countVisitorsByBlogIdAndCreatedOn(blogId, yesterday);
		Integer totalCount = contentVisitsRepository.countTotalVisitorsByBlogId(blogId);

		VisitStatsDto dto = new VisitStatsDto();
		dto.setBlogId(blogId);
		dto.setTodayVisitCount(todayCount != null ? todayCount : 0);
		dto.setYesterdayVisitCount(yesterdayCount != null ? yesterdayCount : 0);
		dto.setTotalVisitCount(totalCount != null ? totalCount : 0);
		dto.setToday(today);

		return dto;
	}

	public ViewStatsDto getViewStatsByContentId(Integer contentId) {
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);

		Integer todayViews = contentViewsRepository.viewsByContentIdAndCreatedOn(contentId, today);
		Integer yesterdayViews = contentViewsRepository.viewsByContentIdAndCreatedOn(contentId, yesterday);
		Integer totalViews = contentViewsRepository.totalViewsByContentId(contentId);

		ViewStatsDto dto = new ViewStatsDto();
		dto.setBlogId(contentId);
		dto.setTodayViewCount(todayViews != null ? todayViews : 0);
		dto.setYesterdayViewCount(yesterdayViews != null ? yesterdayViews : 0);
		dto.setTotalViewCount(totalViews != null ? totalViews : 0);
		dto.setToday(today);

		return dto;
	}

	public VisitStatsDto getVisitStatsByContentId(Integer contentId) {
		LocalDate today = LocalDate.now();
		LocalDate yesterday = today.minusDays(1);

		Integer todayCount = contentVisitsRepository.countVisitorsByContentIdAndCreatedOn(contentId, today);
		Integer yesterdayCount = contentVisitsRepository.countVisitorsByContentIdAndCreatedOn(contentId, yesterday);
		Integer totalCount = contentVisitsRepository.countTotalVisitorsByContentId(contentId);

		VisitStatsDto dto = new VisitStatsDto();
		dto.setBlogId(contentId);
		dto.setTodayVisitCount(todayCount != null ? todayCount : 0);
		dto.setYesterdayVisitCount(yesterdayCount != null ? yesterdayCount : 0);
		dto.setTotalVisitCount(totalCount != null ? totalCount : 0);
		dto.setToday(today);

		return dto;
	}

	public List<ViewDto> getDailyViewsByBlogId(Integer blogId) {
		return contentViewsRepository.findViewsByBlogIdGroupByDate(blogId);
	}

	public List<VisitDto> getDailyVisitorsByBlogId(Integer blogId) {
		return contentVisitsRepository.findVisitorsByBlogIdGroupByDate(blogId);
	}

	public List<ViewDto> getDailyEachViewsByBlogId(Integer contentId) {
		return contentViewsRepository.findViewsByContentIdGroupByDate(contentId);
	}

	public List<VisitDto> getDailyEachVisitorsByBlogId(Integer contentId) {
		return contentVisitsRepository.findVisitorsByContentIdGroupByDate(contentId);
	}

}