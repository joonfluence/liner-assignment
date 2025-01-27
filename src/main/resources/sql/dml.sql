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
