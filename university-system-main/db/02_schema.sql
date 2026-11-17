-- OPCIONAL: la app ya crea estas tablas sola (ddl-auto=update).
-- Úsalo si prefieres crear el esquema a mano (y pon ddl-auto=validate o none).
USE UniversitySystem;
GO

CREATE TABLE user_account (
    id        BIGINT IDENTITY(1,1) PRIMARY KEY,
    username  VARCHAR(100) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    role      VARCHAR(20)  NOT NULL CHECK (role IN ('STUDENT','TEACHER','ADMIN')),
    active    BIT NOT NULL DEFAULT 1,
    CONSTRAINT UQ_user_account_username UNIQUE (username)
);

CREATE TABLE student (
    id               BIGINT IDENTITY(1,1) PRIMARY KEY,
    first_name       VARCHAR(50)  NOT NULL,
    last_name        VARCHAR(50)  NOT NULL,
    email            VARCHAR(100) NOT NULL UNIQUE,
    enrollment_code  VARCHAR(15)  NOT NULL UNIQUE,
    phone            VARCHAR(15)  NULL,
    document_number  VARCHAR(20)  NOT NULL UNIQUE,
    birth_date       DATE NOT NULL,
    enrollment_date  DATE NOT NULL,
    active           BIT NOT NULL DEFAULT 1,
    user_account_id  BIGINT NOT NULL UNIQUE REFERENCES user_account(id)
);

CREATE TABLE teacher (
    id               BIGINT IDENTITY(1,1) PRIMARY KEY,
    first_name       VARCHAR(50)  NOT NULL,
    last_name        VARCHAR(50)  NOT NULL,
    email            VARCHAR(100) NOT NULL UNIQUE,
    teacher_code     VARCHAR(20)  NOT NULL UNIQUE,
    specialty        VARCHAR(100) NULL,
    phone            VARCHAR(15)  NULL,
    document_number  VARCHAR(20)  NULL,
    hire_date        DATE NOT NULL,
    academic_degree  VARCHAR(50)  NULL,
    active           BIT NOT NULL DEFAULT 1,
    user_account_id  BIGINT NOT NULL UNIQUE REFERENCES user_account(id)
);
-- UNIQUE que permite varios NULL (SQL Server solo admite un NULL en UNIQUE normal)
CREATE UNIQUE INDEX UQ_teacher_document_number ON teacher(document_number) WHERE document_number IS NOT NULL;

CREATE TABLE course (
    id           BIGINT IDENTITY(1,1) PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    course_code  VARCHAR(20)  NOT NULL UNIQUE,
    credits      INT NOT NULL
);

CREATE TABLE module (
    id             BIGINT IDENTITY(1,1) PRIMARY KEY,
    module_number  INT NOT NULL,
    course_id      BIGINT NOT NULL REFERENCES course(id)
);

CREATE TABLE topic (
    id         BIGINT IDENTITY(1,1) PRIMARY KEY,
    name       VARCHAR(150) NOT NULL,
    module_id  BIGINT NOT NULL REFERENCES module(id)
);

CREATE TABLE evaluation (
    id               BIGINT IDENTITY(1,1) PRIMARY KEY,
    title            VARCHAR(100) NOT NULL,
    weight           FLOAT NOT NULL,
    evaluation_type  VARCHAR(20) NOT NULL CHECK (evaluation_type IN ('PC_1','PC_2','PARCIAL_EXAM','FINAL_EXAM')),
    module_id        BIGINT NOT NULL REFERENCES module(id)
);

CREATE TABLE section (
    id            BIGINT IDENTITY(1,1) PRIMARY KEY,
    section_name  VARCHAR(50) NOT NULL,
    max_capacity  INT NOT NULL DEFAULT 30,
    course_id     BIGINT NOT NULL REFERENCES course(id),
    teacher_id    BIGINT NOT NULL REFERENCES teacher(id)
);

CREATE TABLE enrollment (
    id               BIGINT IDENTITY(1,1) PRIMARY KEY,
    student_id       BIGINT NOT NULL REFERENCES student(id),
    section_id       BIGINT NOT NULL REFERENCES section(id),
    academic_term    VARCHAR(15) NOT NULL,
    enrollment_date  DATE NOT NULL,
    CONSTRAINT UQ_enrollment_student_section UNIQUE (student_id, section_id)
);

CREATE TABLE grade (
    id             BIGINT IDENTITY(1,1) PRIMARY KEY,
    value          FLOAT NOT NULL,
    enrollment_id  BIGINT NOT NULL REFERENCES enrollment(id),
    evaluation_id  BIGINT NOT NULL REFERENCES evaluation(id),
    CONSTRAINT UQ_grade_enrollment_evaluation UNIQUE (enrollment_id, evaluation_id)
);

CREATE TABLE ai_settings (
    id                      BIGINT PRIMARY KEY,
    enabled                 BIT NOT NULL,
    provider                VARCHAR(50) NOT NULL,
    model_name              VARCHAR(50) NOT NULL,
    temperature             FLOAT NOT NULL,
    minimum_grade_threshold FLOAT NOT NULL,
    max_output_tokens       INT NOT NULL,
    system_prompt           NVARCHAR(MAX) NOT NULL
);

CREATE TABLE ai_recommendation (
    id                      BIGINT IDENTITY(1,1) PRIMARY KEY,
    enrollment_id           BIGINT NOT NULL REFERENCES enrollment(id),
    grade_obtained          FLOAT NOT NULL,
    evaluation_title        VARCHAR(100) NOT NULL,
    model_name              VARCHAR(50)  NOT NULL,
    prompt                  NVARCHAR(MAX) NULL,
    recommendation_content  NVARCHAR(MAX) NULL,
    status                  VARCHAR(20) NOT NULL CHECK (status IN ('PENDING_REVIEW','APPROVED','REJECTED','FAILED')),
    read_by_student         BIT NOT NULL DEFAULT 0,
    generated_at            DATETIME2 NOT NULL
);
GO
-- Los usuarios iniciales (admin j4v / admin123) y la config de IA los crea la app al arrancar (AdminSeeder / AISettingsSeeder).
