package com.liner.feed.application.feed.controller

import com.liner.feed.application.feed.dto.FeedItemSearchDto
import com.liner.feed.application.feed.response.FeedResponse
import com.liner.feed.application.feed.service.FeedQueryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/feeds")
class FeedItemQueryController(
    private val feedsQueryService: FeedQueryService,
) {
    @GetMapping
    fun getFeeds(
        dto: FeedItemSearchDto,
        pageable: Pageable,
    ): ResponseEntity<Page<FeedResponse>> {
        val feeds = feedsQueryService.getFeeds(dto, pageable)
        return ResponseEntity.ok(
            PageImpl(
                feeds.content.map { FeedResponse.from(it) },
                feeds.pageable,
                feeds.totalElements
            )
        )
    }
}
