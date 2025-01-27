package com.liner.feed.domain.repository.feed

import com.liner.feed.domain.entity.feedItem.FeedItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedItemRepository : JpaRepository<FeedItemEntity, Long>, FeedItemRepositoryCustom
