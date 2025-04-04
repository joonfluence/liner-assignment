CREATE TABLE users
(
    id         BigInt PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255)
);

CREATE TABLE pages
(
    id                   BigInt PRIMARY KEY,
    url                  VARCHAR(2083) NOT NULL, -- URL 길이를 고려하여 2083자로 설정
    title                VARCHAR(255)  NOT NULL,
    user_id              BigInt        NOT NULL,
    first_highlighted_at TIMESTAMP     NULL,
    created_at           TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by           VARCHAR(255),
    updated_at           TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by           VARCHAR(255)
);

CREATE INDEX idx_page_user_id ON pages (user_id);
CREATE INDEX idx_page_first_highlighted_at ON pages (first_highlighted_at);

CREATE TABLE highlights
(
    id         BigInt PRIMARY KEY,
    text       TEXT         NOT NULL,
    color      VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    page_id    BigInt       NOT NULL,
    user_id    BigInt       NOT NULL
);

CREATE INDEX idx_highlight_page_id ON highlights (page_id);
CREATE INDEX idx_highlight_user_id ON highlights (user_id);
CREATE INDEX idx_highlight_page_user_id ON highlights (page_id, user_id);

CREATE TABLE feed_items
(
    id         BigInt PRIMARY KEY,
    user_id    BigInt      NOT NULL,
    page_id    BigInt      NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),
    visibility VARCHAR(20) NOT NULL
);

CREATE INDEX idx_feed_item_user_id ON feed_items (user_id);
CREATE INDEX idx_feed_item_page_id ON feed_items (page_id);
CREATE INDEX idx_feed_item_visibility ON feed_items (visibility);
CREATE INDEX idx_feed_item_user_page_visibility ON feed_items (user_id, page_id, visibility);

CREATE TABLE feed_item_mentioned_users
(
    id                BigInt PRIMARY KEY AUTO_INCREMENT,
    feed_item_id      BigInt    NOT NULL,
    mentioned_user_id BigInt    NOT NULL,
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(255),
    updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by        VARCHAR(255)
);

CREATE INDEX idx_feed_item_feed_item_id ON feed_item_mentioned_users (feed_item_id);
CREATE INDEX idx_feed_item_mentioned_users ON feed_item_mentioned_users (mentioned_user_id);
CREATE INDEX idx_feed_item_feed_item_id_mentioned_users ON feed_item_mentioned_users (mentioned_user_id, feed_item_id);

CREATE TABLE feed_item_highlights
(
    id           BigInt PRIMARY KEY,
    feed_item_id BigInt NOT NULL,
    highlight_id BigInt NOT NULL
);

CREATE INDEX idx_feed_item_highlight_feed_item_id ON feed_item_highlights (feed_item_id);
CREATE INDEX idx_feed_item_highlight_highlight_id ON feed_item_highlights (highlight_id);