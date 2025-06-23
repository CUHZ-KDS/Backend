-- 기존 show_seat_mapping 데이터 안전 삭제
DELETE FROM show_seat_mapping WHERE id > 0;

-- AUTO_INCREMENT 초기화
ALTER TABLE show_seat_mapping AUTO_INCREMENT = 1;

-- 공연 1의 모든 스케줄에 대한 좌석 매핑 생성
-- 공연 1: VIP(1), R(2), S(3) 등급
-- Zone A (좌석 1-9): VIP 등급
-- Zone B (좌석 10-18): R 등급 (일부는 S 등급)

-- 공연 1 스케줄 1 (2025-07-01)
INSERT INTO show_seat_mapping (show_schedule_id, seat_id, grade_id, status, created_at, modified_at) VALUES
-- Zone A - VIP 등급
(1, 1, 1, 'AVAILABLE', NOW(), NOW()),
(1, 2, 1, 'AVAILABLE', NOW(), NOW()),
(1, 3, 1, 'AVAILABLE', NOW(), NOW()),
(1, 4, 1, 'AVAILABLE', NOW(), NOW()),
(1, 5, 1, 'AVAILABLE', NOW(), NOW()),
(1, 6, 1, 'AVAILABLE', NOW(), NOW()),
(1, 7, 1, 'AVAILABLE', NOW(), NOW()),
(1, 8, 1, 'AVAILABLE', NOW(), NOW()),
(1, 9, 1, 'AVAILABLE', NOW(), NOW()),
-- Zone B - R석과 S석 혼합
(1, 10, 2, 'AVAILABLE', NOW(), NOW()),
(1, 11, 2, 'AVAILABLE', NOW(), NOW()),
(1, 12, 2, 'AVAILABLE', NOW(), NOW()),
(1, 13, 2, 'AVAILABLE', NOW(), NOW()),
(1, 14, 2, 'AVAILABLE', NOW(), NOW()),
(1, 15, 2, 'AVAILABLE', NOW(), NOW()),
(1, 16, 3, 'AVAILABLE', NOW(), NOW()),
(1, 17, 3, 'AVAILABLE', NOW(), NOW()),
(1, 18, 3, 'AVAILABLE', NOW(), NOW());

-- 공연 1 스케줄 2 (2025-07-02) - 일부 좌석 예약됨
INSERT INTO show_seat_mapping (show_schedule_id, seat_id, grade_id, status, created_at, modified_at) VALUES
-- Zone A - VIP 등급 (일부 예약됨)
(2, 1, 1, 'RESERVED', NOW(), NOW()),
(2, 2, 1, 'AVAILABLE', NOW(), NOW()),
(2, 3, 1, 'AVAILABLE', NOW(), NOW()),
(2, 4, 1, 'RESERVED', NOW(), NOW()),
(2, 5, 1, 'AVAILABLE', NOW(), NOW()),
(2, 6, 1, 'AVAILABLE', NOW(), NOW()),
(2, 7, 1, 'AVAILABLE', NOW(), NOW()),
(2, 8, 1, 'RESERVED', NOW(), NOW()),
(2, 9, 1, 'AVAILABLE', NOW(), NOW()),
-- Zone B - R석과 S석 혼합 (일부 예약됨)
(2, 10, 2, 'AVAILABLE', NOW(), NOW()),
(2, 11, 2, 'RESERVED', NOW(), NOW()),
(2, 12, 2, 'AVAILABLE', NOW(), NOW()),
(2, 13, 2, 'AVAILABLE', NOW(), NOW()),
(2, 14, 2, 'RESERVED', NOW(), NOW()),
(2, 15, 2, 'AVAILABLE', NOW(), NOW()),
(2, 16, 3, 'AVAILABLE', NOW(), NOW()),
(2, 17, 3, 'AVAILABLE', NOW(), NOW()),
(2, 18, 3, 'RESERVED', NOW(), NOW());

-- 공연 1의 나머지 스케줄들 (3-10) - 모든 좌석 AVAILABLE
INSERT INTO show_seat_mapping (show_schedule_id, seat_id, grade_id, status, created_at, modified_at)
SELECT
    schedule_id,
    seat_id,
    CASE
        WHEN seat_id BETWEEN 1 AND 9 THEN 1  -- VIP
        WHEN seat_id BETWEEN 10 AND 15 THEN 2  -- R
        ELSE 3  -- S
    END as grade_id,
    'AVAILABLE' as status,
    NOW() as created_at,
    NOW() as modified_at
FROM
    (SELECT 3 as schedule_id UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) schedules
CROSS JOIN
    (SELECT 1 as seat_id UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18) seats;

-- 공연 2의 모든 스케줄에 대한 좌석 매핑 생성
-- 공연 2: VIP(4), R(5), S(6) 등급
INSERT INTO show_seat_mapping (show_schedule_id, seat_id, grade_id, status, created_at, modified_at)
SELECT
    schedule_id,
    seat_id,
    CASE
        WHEN seat_id BETWEEN 1 AND 9 THEN 4  -- VIP
        WHEN seat_id BETWEEN 10 AND 15 THEN 5  -- R
        ELSE 6  -- S
    END as grade_id,
    'AVAILABLE' as status,
    NOW() as created_at,
    NOW() as modified_at
FROM
    (SELECT 11 as schedule_id UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20) schedules
CROSS JOIN
    (SELECT 1 as seat_id UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18) seats;

-- 공연 3의 모든 스케줄에 대한 좌석 매핑 생성
-- 공연 3: R(7), S(8) 등급 (VIP 없음)
INSERT INTO show_seat_mapping (show_schedule_id, seat_id, grade_id, status, created_at, modified_at)
SELECT
    schedule_id,
    seat_id,
    CASE
        WHEN seat_id BETWEEN 1 AND 12 THEN 7  -- R
        ELSE 8  -- S
    END as grade_id,
    'AVAILABLE' as status,
    NOW() as created_at,
    NOW() as modified_at
FROM
    (SELECT 21 as schedule_id UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25 UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29 UNION SELECT 30) schedules
CROSS JOIN
    (SELECT 1 as seat_id UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18) seats;

-- 공연 4의 모든 스케줄에 대한 좌석 매핑 생성
-- 공연 4: R(9), S(10) 등급 (VIP 없음)
INSERT INTO show_seat_mapping (show_schedule_id, seat_id, grade_id, status, created_at, modified_at)
SELECT
    schedule_id,
    seat_id,
    CASE
        WHEN seat_id BETWEEN 1 AND 12 THEN 9  -- R
        ELSE 10  -- S
    END as grade_id,
    'AVAILABLE' as status,
    NOW() as created_at,
    NOW() as modified_at
FROM
    (SELECT 31 as schedule_id UNION SELECT 32 UNION SELECT 33 UNION SELECT 34 UNION SELECT 35 UNION SELECT 36 UNION SELECT 37 UNION SELECT 38 UNION SELECT 39 UNION SELECT 40) schedules
CROSS JOIN
    (SELECT 1 as seat_id UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18) seats;

-- 공연 5의 모든 스케줄에 대한 좌석 매핑 생성
-- 공연 5: VIP(11), R(12), S(13) 등급
INSERT INTO show_seat_mapping (show_schedule_id, seat_id, grade_id, status, created_at, modified_at)
SELECT
    schedule_id,
    seat_id,
    CASE
        WHEN seat_id BETWEEN 1 AND 6 THEN 11  -- VIP
        WHEN seat_id BETWEEN 7 AND 12 THEN 12  -- R
        ELSE 13  -- S
    END as grade_id,
    'AVAILABLE' as status,
    NOW() as created_at,
    NOW() as modified_at
FROM
    (SELECT 41 as schedule_id UNION SELECT 42 UNION SELECT 43 UNION SELECT 44 UNION SELECT 45 UNION SELECT 46 UNION SELECT 47 UNION SELECT 48 UNION SELECT 49 UNION SELECT 50) schedules
CROSS JOIN
    (SELECT 1 as seat_id UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18) seats;
