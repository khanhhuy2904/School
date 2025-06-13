CREATE TABLE teachers (
    teacher_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10),
    birth_date DATE,
    experience_years INT
);
CREATE TABLE ranks (
	rank_id INT AUTO_INCREMENT PRIMARY KEY,
    rank_name VARCHAR(50)
);

CREATE TABLE majors (
	major_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
);
CREATE TABLE teacher_rank(
	id INT AUTO_INCREMENT PRIMARY KEY,
	teacher_id BIGINT,
	rank_id INT,
	FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE,
	FOREIGN KEY (rank_id) REFERENCES ranks(rank_id)
);
CREATE TABLE teacher_major(
	id INT AUTO_INCREMENT PRIMARY KEY,
	teacher_id BIGINT,
	major_id INT,
	FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE,
	FOREIGN KEY (major_id) REFERENCES majors(major_id)
);


CREATE TABLE students (
    student_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10),
    birth_date DATE,
    major_id INT,
    FOREIGN KEY (major_id) REFERENCES majors(major_id)
);
CREATE TABLE subjects (
    subject_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    max_students INT NOT NULL,
    credit INT NOT NULL,
    teacher_id BIGINT,
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id)
);

CREATE TABLE student_subjects (
    student_subjects_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT,
    subject_id BIGINT,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id)
);








