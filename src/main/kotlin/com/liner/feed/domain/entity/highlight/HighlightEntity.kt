package com.liner.feed.domain.entity.highlight

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "highlights")
class HighlightEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val pageId: Long,
    val userId: Long,
    val text: String,
    val color: String,
    val createdAt: LocalDateTime,
)
