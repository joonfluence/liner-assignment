package com.liner.feed.domain.entity.feedItem.mentioned

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "feed_item_mentioned_users")
class MentionedUserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val feedItemId: Long,
    @Column(name = "mentioned_user_id")
    val userId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
)
