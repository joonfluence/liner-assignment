CREATE DATABASE IF NOT EXISTS liner;

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
    id                 BigInt PRIMARY KEY,
    url                VARCHAR(2083) NOT NULL, -- URL 길이를 고려하여 2083자로 설정
    title              VARCHAR(255)  NOT NULL,
    user_id            BigInt        NOT NULL,
    first_highlighted_at TIMESTAMP     NULL,
    created_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by         VARCHAR(255),
    updated_at         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by         VARCHAR(255)
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
    id                BigInt    PRIMARY KEY AUTO_INCREMENT,
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

-- 샘플 데이터 추가 : 'users' table
INSERT INTO users (id, name, username, created_at, created_by, updated_at, updated_by)
VALUES (1, 'John Doe', 'johndoe', NOW(), 1, NOW(), 1),
       (2, 'Jane Smith', 'janesmith', NOW(), 2, NOW(), 2),
       (3, 'Alice Johnson', 'alicejohnson', NOW(), 3, NOW(), 3),
       (4, 'Bob Brown', 'bobbrown', NOW(), 4, NOW(), 4),
       (5, 'Charlie Davis', 'charliedavis', NOW(), 5, NOW(), 5);

-- 샘플 데이터 추가 : 'pages' table
INSERT INTO pages (id, url, title, user_id, created_at, created_by, updated_at, updated_by, first_highlighted_at) VALUES (1, 'https://example.com/page1', 'Page 1', 1, '2025-01-27 07:39:06', '1', '2025-01-27 11:12:23', '1', '2025-01-27 07:39:06.000000');
INSERT INTO pages (id, url, title, user_id, created_at, created_by, updated_at, updated_by, first_highlighted_at) VALUES (2, 'https://example.com/page2', 'Page 2', 1, '2025-01-27 07:39:06', '2', '2025-01-27 11:12:23', '2', '2025-01-27 08:39:06.000000');
INSERT INTO pages (id, url, title, user_id, created_at, created_by, updated_at, updated_by, first_highlighted_at) VALUES (3, 'https://example.com/page3', 'Page 3', 1, '2025-01-27 07:39:06', '3', '2025-01-27 11:12:23', '3', '2025-01-27 09:39:06.000000');
INSERT INTO pages (id, url, title, user_id, created_at, created_by, updated_at, updated_by, first_highlighted_at) VALUES (4, 'https://example.com/page4', 'Page 4', 1, '2025-01-27 07:39:06', '4', '2025-01-27 11:12:23', '4', '2025-01-27 10:39:06.000000');
INSERT INTO pages (id, url, title, user_id, created_at, created_by, updated_at, updated_by, first_highlighted_at) VALUES (5, 'https://example.com/page5', 'Page 5', 1, '2025-01-27 07:39:06', '5', '2025-01-27 11:12:23', '5', '2025-01-27 11:39:06.000000');

-- 샘플 데이터 추가 : 'highlights' table
INSERT INTO highlights (id, text, color, created_at, created_by, updated_at, updated_by, page_id, user_id)
VALUES (1, 'This is a highlighted text on Page 1', '#33333', NOW(), 1, NOW(), 1, 1, 1),
       (2, 'Another highlighted text on Page 1', '#33333', NOW(), 2, NOW(), 2, 1, 2),
       (3, 'Some important text on Page 2', '#33333', NOW(), 3, NOW(), 3, 2, 3),
       (4, 'Highlight for Page 3', '#33333', NOW(), 4, NOW(), 4, 3, 4),
       (5, 'Key text on Page 4', '#33333', NOW(), 5, NOW(), 5, 4, 5);

-- 샘플 데이터 추가 : 'feed_items' table
INSERT INTO feed_items (id, user_id, page_id, created_at, created_by, updated_at, updated_by, visibility)
VALUES (1, 1, 1, NOW(), 1, NOW(), 1, 'PUBLIC'),
       (2, 2, 2, NOW(), 2, NOW(), 2, 'MENTIONED'),
       (3, 3, 3, NOW(), 3, NOW(), 3, 'PRIVATE'),
       (4, 4, 4, NOW(), 4, NOW(), 4, 'PUBLIC'),
       (5, 5, 5, NOW(), 5, NOW(), 5, 'MENTIONED');

-- 샘플 데이터 추가 : 'feed_item_mentioned_users' table
INSERT INTO feed_item_mentioned_users (feed_item_id, mentioned_user_id, created_at, created_by, updated_at, updated_by)
VALUES (2, 1, NOW(), 1, NOW(), 1),
       (2, 3, NOW(), 3, NOW(), 3),
       (5, 2, NOW(), 2, NOW(), 2),
       (5, 4, NOW(), 4, NOW(), 4);
