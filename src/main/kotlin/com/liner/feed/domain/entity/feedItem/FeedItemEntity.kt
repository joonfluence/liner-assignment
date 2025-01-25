package com.liner.feed.domain.entity.feedItem

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "feed_items")
class FeedItemEntity(
  @Id
  @GeneratedValue
  val id: Long,
  val userId: Long,
  val pageId: Long,
  val createdAt: LocalDateTime,
  @Enumerated(EnumType.STRING)
  val visibility: VisibilityType,
)

enum class VisibilityType {
  PUBLIC,
  PRIVATE,
  MENTIONED
}