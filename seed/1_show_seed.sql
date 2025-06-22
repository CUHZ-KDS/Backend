-- 기존 공연 데이터 안전 삭제
DELETE FROM `show` WHERE id > 0;

-- 기존 장소 데이터 안전 삭제 (외래키 제약조건 순서 고려)
DELETE FROM seat WHERE id > 0;
DELETE FROM zone WHERE id > 0;
DELETE FROM place WHERE id > 0;

-- AUTO_INCREMENT 초기화
ALTER TABLE `show` AUTO_INCREMENT = 1;
ALTER TABLE seat AUTO_INCREMENT = 1;
ALTER TABLE zone AUTO_INCREMENT = 1;
ALTER TABLE place AUTO_INCREMENT = 1;

-- 장소 1개 생성
INSERT INTO place (id, name, address, seat_count, created_at, modified_at) VALUES
(1, 'Sample Place', '123 Sample Address', 18, NOW(), NOW());

-- 구역 2개 생성
INSERT INTO zone (id, place_id, name, created_at, modified_at) VALUES
(1, 1, 'Zone A', NOW(), NOW()),
(2, 1, 'Zone B', NOW(), NOW());

-- Zone A 좌석 생성 (3행 x 3열 = 9좌석)
INSERT INTO seat (id, zone_id, seat_row, seat_column, status, created_at, modified_at) VALUES
(1, 1, 1, 1, TRUE, NOW(), NOW()),
(2, 1, 1, 2, TRUE, NOW(), NOW()),
(3, 1, 1, 3, TRUE, NOW(), NOW()),
(4, 1, 2, 1, TRUE, NOW(), NOW()),
(5, 1, 2, 2, TRUE, NOW(), NOW()),
(6, 1, 2, 3, TRUE, NOW(), NOW()),
(7, 1, 3, 1, TRUE, NOW(), NOW()),
(8, 1, 3, 2, TRUE, NOW(), NOW()),
(9, 1, 3, 3, TRUE, NOW(), NOW());

-- Zone B 좌석 생성 (3행 x 3열 = 9좌석)
INSERT INTO seat (id, zone_id, seat_row, seat_column, status, created_at, modified_at) VALUES
(10, 2, 1, 1, TRUE, NOW(), NOW()),
(11, 2, 1, 2, TRUE, NOW(), NOW()),
(12, 2, 1, 3, TRUE, NOW(), NOW()),
(13, 2, 2, 1, TRUE, NOW(), NOW()),
(14, 2, 2, 2, TRUE, NOW(), NOW()),
(15, 2, 2, 3, TRUE, NOW(), NOW()),
(16, 2, 3, 1, TRUE, NOW(), NOW()),
(17, 2, 3, 2, TRUE, NOW(), NOW()),
(18, 2, 3, 3, TRUE, NOW(), NOW());

-- 공연 5개 생성 (모두 동일한 장소 사용) - 관리자 제거버전
INSERT INTO `show` (id, place_id, title, category, start_date, end_date, ticket_start_date_time, ticket_end_date_time, min_age, running_time_minute, intermission_time, show_img_url, created_at, modified_at) VALUES
(1, 1, '공연 1', 'MUSICAL', '2025-07-01 00:00:00', '2025-07-10 23:59:59', '2025-06-20 14:00:00', '2025-06-30 23:59:59', 15, 120, 15, '/img/show1.png', NOW(), NOW()),
(2, 1, '공연 2', 'MUSICAL', '2025-07-11 00:00:00', '2025-07-20 23:59:59', '2025-07-01 14:00:00', '2025-07-10 23:59:59', 12, 110, 10, '/img/show2.png', NOW(), NOW()),
(3, 1, '공연 3', 'CONCERT', '2025-07-21 00:00:00', '2025-07-30 23:59:59', '2025-07-11 14:00:00', '2025-07-20 23:59:59', 18, 130, 20, '/img/show3.png', NOW(), NOW()),
(4, 1, '공연 4', 'CONCERT', '2025-08-01 00:00:00', '2025-08-10 23:59:59', '2025-07-21 14:00:00', '2025-07-30 23:59:59', 10, 100, 5, '/img/show4.png', NOW(), NOW()),
(5, 1, '공연 5', 'CONCERT', '2025-08-11 00:00:00', '2025-08-20 23:59:59', '2025-08-01 14:00:00', '2025-08-10 23:59:59', 20, 140, 25, '/img/show5.png', NOW(), NOW());


-- -- 공연 5개 생성 (모두 동일한 장소 사용)
-- INSERT INTO show (id, created_by, place_id, title, category, start_date, end_date, ticket_start_date_time, ticket_end_date_time, min_age, running_time_minute, intermission_time, show_img, created_at, modified_at) VALUES
-- (1, 1, 1, '공연 1', 'MUSICAL', '2025-07-01 00:00:00', '2025-07-10 23:59:59', '2025-06-20 14:00:00', '2025-06-30 23:59:59', 15, 120, 15, '/img/show1.png', NOW(), NOW()),
-- (2, 1, 1, '공연 2', 'MUSICAL', '2025-07-11 00:00:00', '2025-07-20 23:59:59', '2025-07-01 14:00:00', '2025-07-10 23:59:59', 12, 110, 10, '/img/show2.png', NOW(), NOW()),
-- (3, 1, 1, '공연 3', 'CONCERT', '2025-07-21 00:00:00', '2025-07-30 23:59:59', '2025-07-11 14:00:00', '2025-07-20 23:59:59', 18, 130, 20, '/img/show3.png', NOW(), NOW()),
-- (4, 1, 1, '공연 4', 'CONCERT', '2025-08-01 00:00:00', '2025-08-10 23:59:59', '2025-07-21 14:00:00', '2025-07-30 23:59:59', 10, 100, 5, '/img/show4.png', NOW(), NOW()),
-- (5, 1, 1, '공연 5', 'CONCERT', '2025-08-11 00:00:00', '2025-08-20 23:59:59', '2025-08-01 14:00:00', '2025-08-10 23:59:59', 20, 140, 25, '/img/show5.png', NOW(), NOW());
