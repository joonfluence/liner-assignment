package com.liner.feed.domain.entity.feedItem.highlight

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "feed_item_highlights")
class FeedItemHighlightEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val feedItemId: Long,
    val highlightId: Long,
)
