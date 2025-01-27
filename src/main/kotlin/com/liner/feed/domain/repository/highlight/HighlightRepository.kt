package com.liner.feed.domain.repository.highlight

import com.liner.feed.domain.entity.highlight.HighlightEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HighlightRepository : JpaRepository<HighlightEntity, Long> {
    fun findByPageIdInAndUserId(pageIds: List<Long>, userId: Long): List<HighlightEntity>
}
