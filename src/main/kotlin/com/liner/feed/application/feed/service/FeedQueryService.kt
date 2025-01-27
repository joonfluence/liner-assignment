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
    companion object {
        const val MINIMUM_HIGHLIGHTS = 3
    }

    fun getFeeds(dto: FeedItemSearchDto, pageable: Pageable): Page<FeedItemDto> {
        val feedItems = feedItemRepository.findFeedItems(dto, pageable)
        val content = feedItems.content
        val pageIds = content.map { feedItem -> feedItem.page.id }

        // pageIds 로 조회해온다
        val highlights = highlightRepository.findByPageIdInAndUserId(pageIds, dto.userId)
        val pageMap = highlights.groupBy { it.pageId }
        content.forEach { feedItem ->
            val entities = pageMap[feedItem.page.id]?.take(MINIMUM_HIGHLIGHTS) ?: emptyList()
            val dtos = entities.map { highlight ->
                HighlightDto.from(highlight)
            }
            feedItem.updateHighlights(dtos)
        }

        return PageImpl(
            content,
            feedItems.pageable,
            feedItems.totalElements
        )
    }
}
