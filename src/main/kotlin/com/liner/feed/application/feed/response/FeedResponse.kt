package com.liner.feed.application.feed.response

import com.liner.feed.application.feed.dto.FeedItemDto
import com.liner.feed.application.feed.dto.HighlightDto
import com.liner.feed.application.feed.dto.PageDto
import com.liner.feed.application.feed.dto.UserDto
import java.time.LocalDateTime

data class FeedResponse(
    val user: UserResponse,
    val highlights: List<HighlightResponse>,
    val page: PageResponse,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(
            dto: FeedItemDto,
        ): FeedResponse {
            return FeedResponse(
                UserResponse.from(dto.user),
                dto.highlights.map { HighlightResponse.from(it) },
                PageResponse.from(dto.page),
                dto.createdAt,
            )
        }
    }
}

data class UserResponse(val id: Long, val name: String) {
    companion object {
        fun from(dto: UserDto): UserResponse {
            return UserResponse(dto.id, dto.name)
        }
    }
}

data class HighlightResponse(val id: Long, val color: String, val text: String) {
    companion object {
        fun from(dto: HighlightDto): HighlightResponse {
            return HighlightResponse(dto.id, dto.color, dto.text)
        }
    }
}

data class PageResponse(val id: Long, val url: String, val title: String) {
    companion object {
        fun from(dto: PageDto): PageResponse {
            return PageResponse(dto.id, dto.url, dto.title)
        }
    }
}
