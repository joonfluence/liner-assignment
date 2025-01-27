package com.liner.feed.domain.repository.feed

import com.liner.feed.application.feed.dto.FeedItemDto
import com.liner.feed.application.feed.dto.FeedItemSearchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FeedItemRepositoryCustom {
    fun findFeedItems(dto: FeedItemSearchDto, pageable: Pageable): Page<FeedItemDto>
}
