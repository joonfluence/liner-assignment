package com.liner.feed.domain.repository.feed

import com.liner.feed.application.feed.dto.FeedItemDto
import com.liner.feed.application.feed.dto.FeedItemSearchDto
import com.liner.feed.application.feed.dto.PageDto
import com.liner.feed.application.feed.dto.UserDto
import com.liner.feed.domain.entity.feedItem.FeedItemEntity
import com.liner.feed.domain.entity.feedItem.QFeedItemEntity
import com.liner.feed.domain.entity.feedItem.mentioned.QMentionedUserEntity
import com.liner.feed.domain.entity.page.QPageEntity
import com.liner.feed.domain.entity.user.QUserEntity
import com.liner.feed.domain.enums.VisibilityType
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class FeedItemRepositoryCustomImpl : FeedItemRepositoryCustom,
    QuerydslRepositorySupport(FeedItemEntity::class.java) {

    val feedItem = QFeedItemEntity.feedItemEntity
    val user = QUserEntity.userEntity
    val page = QPageEntity.pageEntity
    val mentionedUser = QMentionedUserEntity.mentionedUserEntity

    override fun findFeedItems(dto: FeedItemSearchDto, pageable: Pageable): Page<FeedItemDto> {
        val query =
            from(feedItem)
                .select(
                    Projections.constructor(
                        FeedItemDto::class.java,
                        feedItem.id,
                        Projections.constructor(
                            UserDto::class.java,
                            user.id,
                            user.name,
                            user.userName,
                        ),
                        Projections.constructor(
                            PageDto::class.java,
                            page.id,
                            page.url,
                            page.title,
                        ),
                        feedItem.createdAt,
                        feedItem.visibility
                    )
                )
                .innerJoin(user).on(feedItem.userId.eq(user.id))
                .innerJoin(page).on(feedItem.pageId.eq(page.id))

        // 공개 범위 조건에 따른 필터링
        val condition = BooleanBuilder().apply {
            or(feedItem.visibility.eq(VisibilityType.PUBLIC))
            or(feedItem.visibility.eq(VisibilityType.PRIVATE).and(feedItem.userId.eq(dto.userId)))
            or(
                feedItem.visibility.eq(VisibilityType.MENTIONED)
                    .and(
                        feedItem.id.`in`(
                            from(mentionedUser)
                                .select(mentionedUser.feedItemId)
                                .where(mentionedUser.userId.eq(dto.userId))
                        )
                    )
            )
        }

        val content = query
            .where(condition)
            .orderBy(feedItem.firstHighlightedAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = from(feedItem)
            .select(
                feedItem.id
            )
            .innerJoin(user).on(feedItem.userId.eq(user.id))
            .innerJoin(page).on(feedItem.pageId.eq(page.id))
            .where(condition)
            .fetchCount()

        return PageImpl(content, pageable, count)
    }
}
