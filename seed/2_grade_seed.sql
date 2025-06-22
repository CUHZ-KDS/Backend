-- 기존 Grade 데이터 삭제 및 AUTO_INCREMENT 초기화
DELETE FROM grade WHERE id > 0;
ALTER TABLE grade AUTO_INCREMENT = 1;

-- 공연 1 (뮤지컬) - 3개 등급
INSERT INTO grade (id, show_id, grade, price, created_at, modified_at) VALUES
(1, 1, 'VIP', 80000, NOW(), NOW()),
(2, 1, 'R', 60000, NOW(), NOW()),
(3, 1, 'S', 40000, NOW(), NOW());

-- 공연 2 (뮤지컬) - 3개 등급
INSERT INTO grade (id, show_id, grade, price, created_at, modified_at) VALUES
(4, 2, 'VIP', 75000, NOW(), NOW()),
(5, 2, 'R', 55000, NOW(), NOW()),
(6, 2, 'S', 35000, NOW(), NOW());

-- 공연 3 (콘서트) - 2개 등급
INSERT INTO grade (id, show_id, grade, price, created_at, modified_at) VALUES
(7, 3, 'R', 90000, NOW(), NOW()),
(8, 3, 'S', 70000, NOW(), NOW());

-- 공연 4 (콘서트) - 2개 등급
INSERT INTO grade (id, show_id, grade, price, created_at, modified_at) VALUES
(9, 4, 'R', 65000, NOW(), NOW()),
(10, 4, 'S', 45000, NOW(), NOW());

-- 공연 5 (콘서트) - 3개 등급
INSERT INTO grade (id, show_id, grade, price, created_at, modified_at) VALUES
(11, 5, 'VIP', 120000, NOW(), NOW()),
(12, 5, 'R', 95000, NOW(), NOW()),
(13, 5, 'S', 75000, NOW(), NOW());
