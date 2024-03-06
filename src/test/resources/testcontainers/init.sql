INSERT INTO MajorsDomain (id, major) VALUES (1, '건설환경공학과');

INSERT INTO AbeekDomain (abeekType, major_id, note, year, minCredit) VALUES (2, 1, 'this is a test note', 19, 54);
INSERT INTO AbeekDomain (abeekType, major_id, note, year, minCredit) VALUES (1, 1, 'this is a test note', 19, 30);
INSERT INTO AbeekDomain (abeekType, major_id, note, year, minCredit) VALUES (3, 1, 'this is a test note', 19, 12);
INSERT INTO AbeekDomain (abeekType, major_id, note, year, minCredit) VALUES (0, 1, 'this is a test note', 19, 14);
INSERT INTO AbeekDomain (abeekType, major_id, note, year, minCredit) VALUES (4, 1, 'this is a test note', 19, 103);

INSERT INTO UserDomain (email, name, password, studentId, major_id) VALUES ('testEmail','홍지섭','qwer','19000001', 1);

INSERT INTO CoursesDomain (courseId, credit, name) VALUES (1234, 3, 'testCourse1');
INSERT INTO CoursesDomain (courseId, credit, name) VALUES (2345, 4, 'testCourse2');
INSERT INTO CoursesDomain (courseId, credit, name) VALUES (9000, 5, 'testCourse3');

INSERT INTO CompletedCoursesDomain (year, semester, course_id, user_id) VALUES (19, 1, 1234, 1);
INSERT INTO CompletedCoursesDomain (year, semester, course_id, user_id) VALUES (19, 1, 2345, 1);
INSERT INTO CompletedCoursesDomain (year, semester, course_id, user_id) VALUES (20, 1, 9000, 1);

INSERT INTO GonghakCoursesDomain (courseCategory, major_id, designCredit, course_id, passCategory, year)
VALUES ('전필', 1, 1.5, 1234, '인필', 19);
INSERT INTO GonghakCoursesDomain (courseCategory, major_id, designCredit, course_id, passCategory, year)
VALUES ('MSC', 1, 0.5, 2345, '인필', 19);
INSERT INTO GonghakCoursesDomain (courseCategory, major_id, designCredit, course_id, passCategory, year)
VALUES ('전선', 1, 1.0, 1234, '인선', 19);
