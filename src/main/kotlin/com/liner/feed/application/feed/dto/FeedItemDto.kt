package com.liner.feed.application.feed.dto

import com.liner.feed.domain.entity.highlight.HighlightEntity
import com.liner.feed.domain.enums.VisibilityType
import java.time.LocalDateTime

data class FeedItemDto(
    val id: Long,
    val user: UserDto,
    val page: PageDto,
    val createdAt: LocalDateTime,
    val visibility: VisibilityType,
) {
    var highlights: List<HighlightDto> = emptyList()

    fun updateHighlights(highlights: List<HighlightDto>) {
        this.highlights = highlights
    }
}

data class UserDto(
    val id: Long,
    val name: String,
)

data class PageDto(
    val id: Long,
    val url: String,
    val title: String,
)

data class HighlightDto(
    val id: Long,
    val pageId: Long,
    val text: String,
    val color: String,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(
            entity: HighlightEntity
        ): HighlightDto {
            return HighlightDto(
                id = entity.id,
                pageId = entity.pageId,
                text = entity.text,
                color = entity.color,
                createdAt = entity.createdAt,
            )
        }
    }
}
