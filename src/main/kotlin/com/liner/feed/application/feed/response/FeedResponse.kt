package com.liner.feed.application.feed.response

import com.liner.feed.application.feed.dto.FeedItemDto
import com.liner.feed.application.feed.dto.HighlightDto
import com.liner.feed.application.feed.dto.PageDto
import java.time.LocalDateTime

data class FeedResponse(
    val user: FeedUserResponse,
    val highlights: List<FeedHighlightResponse>,
    val page: FeedPageResponse,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(
            dto: FeedItemDto,
        ): FeedResponse {
            return FeedResponse(
                FeedUserResponse.from(dto.user),
                dto.highlights.map { FeedHighlightResponse.from(it) },
                FeedPageResponse.from(dto.page),
                dto.createdAt,
            )
        }
    }
}

data class FeedHighlightResponse(val id: Long, val color: String, val text: String) {
    companion object {
        fun from(dto: HighlightDto): FeedHighlightResponse {
            return FeedHighlightResponse(dto.id, dto.color, dto.text)
        }
    }
}

data class FeedPageResponse(val id: Long, val url: String, val title: String) {
    companion object {
        fun from(dto: PageDto): FeedPageResponse {
            return FeedPageResponse(dto.id, dto.url, dto.title)
        }
    }
}
