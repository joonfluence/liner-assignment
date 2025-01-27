package com.liner.feed.global.exception

enum class ErrorCodes(val code: String, val message: String) {
    GLOBAL_ERROR("GLOBAL_001", "요청 처리 중 오류가 발생했습니다. 페이지를 새로 고치거나 잠시 후 다시 시도해 주세요."),
    INVALID_VISIBILITY_TYPE("FEED_001", "유효하지 않은 피드 공개범위 타입입니다."),
}
