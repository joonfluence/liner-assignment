package com.liner.feed.domain.entity.feedItem.mentioned

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "feed_item_mentioned_users")
data class FeedItemMentionedUserEntity(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  val id: UUID? = null,
  val feedItemId: Long,
  val mentionedUserId: Long,
)