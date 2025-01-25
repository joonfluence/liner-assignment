package com.liner.feed.domain.entity.highlight

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "highlights")
class HighlightEntity (
  @Id
  @GeneratedValue
  val id: Long = 0,
  val highlightedText: String,
  val colorBar: String,
  val createdAt: LocalDateTime = LocalDateTime.now(),
  val pageId: Long,
  val userId: Long,
)