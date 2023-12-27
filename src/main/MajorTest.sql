INSERT INTO major (check_major, major_name)
VALUES (true, 'Computer Science');

INSERT INTO major (check_major, major_name)
VALUES (true, 'Math');

INSERT INTO major (check_major, major_name)
VALUES (true, 'Real English');

INSERT INTO major (check_major, major_name)
VALUES (true, 'Real Music');

INSERT INTO major (check_major, major_name)
VALUES (true, 'Engineer');

##관리자 권한 변경

UPDATE member
SET role = 'ADMIN'
WHERE email = 'codhtjd8700@naver.com' AND role = 'STUDENT';

