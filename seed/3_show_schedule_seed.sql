-- 기존 show_schedule 데이터 안전 삭제
DELETE FROM show_schedule WHERE id > 0;

-- AUTO_INCREMENT 초기화
ALTER TABLE show_schedule AUTO_INCREMENT = 1;

-- 공연 1 (2025-07-01 ~ 2025-07-10) - 총 10회 공연
INSERT INTO show_schedule (id, show_id, show_date, created_at, modified_at) VALUES
(1, 1, '2025-07-01', NOW(), NOW()),
(2, 1, '2025-07-02', NOW(), NOW()),
(3, 1, '2025-07-03', NOW(), NOW()),
(4, 1, '2025-07-04', NOW(), NOW()),
(5, 1, '2025-07-05', NOW(), NOW()),
(6, 1, '2025-07-06', NOW(), NOW()),
(7, 1, '2025-07-07', NOW(), NOW()),
(8, 1, '2025-07-08', NOW(), NOW()),
(9, 1, '2025-07-09', NOW(), NOW()),
(10, 1, '2025-07-10', NOW(), NOW());

-- 공연 2 (2025-07-11 ~ 2025-07-20) - 총 10회 공연
INSERT INTO show_schedule (id, show_id, show_date, created_at, modified_at) VALUES
(11, 2, '2025-07-11', NOW(), NOW()),
(12, 2, '2025-07-12', NOW(), NOW()),
(13, 2, '2025-07-13', NOW(), NOW()),
(14, 2, '2025-07-14', NOW(), NOW()),
(15, 2, '2025-07-15', NOW(), NOW()),
(16, 2, '2025-07-16', NOW(), NOW()),
(17, 2, '2025-07-17', NOW(), NOW()),
(18, 2, '2025-07-18', NOW(), NOW()),
(19, 2, '2025-07-19', NOW(), NOW()),
(20, 2, '2025-07-20', NOW(), NOW());

-- 공연 3 (2025-07-21 ~ 2025-07-30) - 총 10회 공연
INSERT INTO show_schedule (id, show_id, show_date, created_at, modified_at) VALUES
(21, 3, '2025-07-21', NOW(), NOW()),
(22, 3, '2025-07-22', NOW(), NOW()),
(23, 3, '2025-07-23', NOW(), NOW()),
(24, 3, '2025-07-24', NOW(), NOW()),
(25, 3, '2025-07-25', NOW(), NOW()),
(26, 3, '2025-07-26', NOW(), NOW()),
(27, 3, '2025-07-27', NOW(), NOW()),
(28, 3, '2025-07-28', NOW(), NOW()),
(29, 3, '2025-07-29', NOW(), NOW()),
(30, 3, '2025-07-30', NOW(), NOW());

-- 공연 4 (2025-08-01 ~ 2025-08-10) - 총 10회 공연
INSERT INTO show_schedule (id, show_id, show_date, created_at, modified_at) VALUES
(31, 4, '2025-08-01', NOW(), NOW()),
(32, 4, '2025-08-02', NOW(), NOW()),
(33, 4, '2025-08-03', NOW(), NOW()),
(34, 4, '2025-08-04', NOW(), NOW()),
(35, 4, '2025-08-05', NOW(), NOW()),
(36, 4, '2025-08-06', NOW(), NOW()),
(37, 4, '2025-08-07', NOW(), NOW()),
(38, 4, '2025-08-08', NOW(), NOW()),
(39, 4, '2025-08-09', NOW(), NOW()),
(40, 4, '2025-08-10', NOW(), NOW());

-- 공연 5 (2025-08-11 ~ 2025-08-20) - 총 10회 공연
INSERT INTO show_schedule (id, show_id, show_date, created_at, modified_at) VALUES
(41, 5, '2025-08-11', NOW(), NOW()),
(42, 5, '2025-08-12', NOW(), NOW()),
(43, 5, '2025-08-13', NOW(), NOW()),
(44, 5, '2025-08-14', NOW(), NOW()),
(45, 5, '2025-08-15', NOW(), NOW()),
(46, 5, '2025-08-16', NOW(), NOW()),
(47, 5, '2025-08-17', NOW(), NOW()),
(48, 5, '2025-08-18', NOW(), NOW()),
(49, 5, '2025-08-19', NOW(), NOW()),
(50, 5, '2025-08-20', NOW(), NOW());
