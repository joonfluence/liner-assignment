package com.liner.feed.application.feed.service

import com.liner.feed.application.feed.dto.FeedItemDto
import com.liner.feed.application.feed.dto.FeedItemSearchDto
import com.liner.feed.application.feed.dto.HighlightDto
import com.liner.feed.domain.repository.feed.FeedItemRepository
import com.liner.feed.domain.repository.highlight.HighlightRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class FeedQueryService(
    private val feedItemRepository: FeedItemRepository,
    private val highlightRepository: HighlightRepository,
) {
    fun getFeeds(dto: FeedItemSearchDto, pageable: Pageable): Page<FeedItemDto> {
        val feedItems = feedItemRepository.findFeedItems(dto, pageable)
        val content = feedItems.content
        val pageIds = content.map { feedItem -> feedItem.page.id }

        // pageIds 로 조회해온다
        val highlights = highlightRepository.findByPageIdInAndUserId(pageIds, dto.userId)
        val pageMap = highlights.groupBy { it.pageId }
        content.forEach { feedItem ->
            val highlightsByPage = pageMap[feedItem.page.id]?.take(3) ?: emptyList()
            val highlightDtos = highlightsByPage.map { highlight ->
                HighlightDto.from(highlight)
            }
            feedItem.updateHighlights(highlightDtos)
        }

        return PageImpl(
            content,
            feedItems.pageable,
            feedItems.totalElements
        )
    }
}
