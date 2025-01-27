package com.liner.feed.application.feed.response

import com.liner.feed.application.feed.dto.UserDto

data class FeedUserResponse(val id: Long, val name: String, val userName: String) {
    companion object {
        fun from(dto: UserDto): FeedUserResponse {
            return FeedUserResponse(dto.id, dto.name, dto.userName)
        }
    }
}
