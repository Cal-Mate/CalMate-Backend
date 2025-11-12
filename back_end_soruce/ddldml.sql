-- 🔥 외래키 검사 비활성화
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS comment_like;
DROP TABLE IF EXISTS post_comment;
DROP TABLE IF EXISTS post_like;
DROP TABLE IF EXISTS post_file;
DROP TABLE IF EXISTS post_tag;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS upload_file;
DROP TABLE IF EXISTS goal;
DROP TABLE IF EXISTS ban;
DROP TABLE IF EXISTS member_status;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS member_rank;
DROP TABLE IF EXISTS member_authority;
DROP TABLE IF EXISTS authorites;
DROP TABLE IF EXISTS login_failure_history;
DROP TABLE IF EXISTS refresh_token;
DROP TABLE IF EXISTS login_history;
DROP TABLE IF EXISTS extend_file_path;
DROP TABLE IF EXISTS meal_food;
DROP TABLE IF EXISTS food_fileupload;
DROP TABLE IF EXISTS ai_diet;
DROP TABLE IF EXISTS food;
DROP TABLE IF EXISTS meal;
DROP TABLE IF EXISTS `report_fileupload`;
DROP TABLE IF EXISTS `report`;
DROP TABLE IF EXISTS `report_base`;
DROP TABLE IF EXISTS `exercise_fileupload`;
DROP TABLE IF EXISTS `exercise`;
DROP TABLE IF EXISTS `black_list`;
DROP TABLE IF EXISTS `diary`;
DROP TABLE IF EXISTS `qna`;
DROP TABLE IF EXISTS `calender`;
DROP TABLE IF EXISTS `diary_file`;
DROP TABLE IF EXISTS `qna_comment`;
DROP TABLE IF EXISTS gacha_draw_log;
DROP TABLE IF EXISTS gacha_reward_grant;
DROP TABLE IF EXISTS gacha_board_cell;
DROP TABLE IF EXISTS gacha_fileupload;
DROP TABLE IF EXISTS gacha_quantity;
DROP TABLE IF EXISTS gacha_prize;
DROP TABLE IF EXISTS gacha_event;

DROP TABLE IF EXISTS bingo_fileupload;
DROP TABLE IF EXISTS bingo_cell;
DROP TABLE IF EXISTS bingo_board;

DROP TABLE IF EXISTS point;

DROP TABLE IF EXISTS gacha_reset;

DROP TABLE IF EXISTS member_allergy;
DROP TABLE IF EXISTS allergy;
DROP TABLE IF EXISTS food_allergy;


-- ------------- DDL -------------- --
CREATE TABLE IF NOT EXISTS upload_file (
                                           id   bigint   NOT NULL auto_increment,
                                           mime_type   VARCHAR(255)   not NULL,
    file_path   VARCHAR(255)   NOT NULL,
    created_at   DATETIME   NULL default now(),
    State   VARCHAR(255)   NULL,
    original_file_name   VARCHAR(255)   NULL,
    re_file_name   VARCHAR(255)   NULL,
    member_id   bigint   NOT NULL,
    extend_file_path_id   BIGINT   NOT NULL,
    constraint pk_upload_file_id primary key(id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS goal (
                                    id   bigint   NOT NULL auto_increment,
                                    type   ENUM('WEIGHT','CALORIE','MACRO') NOT NULL,
    target_value   DECIMAL(10,2)   NULL,
    kcal_per_day   INT   NULL,
    protein_g   INT   NULL,
    fat_g   INT   NULL,
    carbs_g   INT   NULL,
    start_date   DATETIME   NOT NULL,
    end_date   DATETIME   NULL,
    created_at   DATETIME   NOT NULL default now(),
    member_id   bigint   NOT NULL,
    constraint pk_goal_id primary key(id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS ban (
                                   id   bigint   NOT NULL auto_increment,
                                   startDate   DATETIME   NOT NULL,
                                   endDate   DATETIME   NOT NULL,
                                   admin_id   bigint   NOT NULL,
                                   member_id   bigint   NOT NULL,
                                   report_no   bigint   NOT NULL,
                                   constraint pk_ban_id primary key(id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS member_status (
                                             id   bigint   NOT NULL auto_increment,
                                             status   varchar(255)   NULL,
    constraint pk_member_status_id primary key(id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS member (
                                      id   bigint   NOT NULL auto_increment,
                                      name   VARCHAR(255)   NULL,
    nickname   VARCHAR(255)   NULL,
    email   VARCHAR(255)   NOT NULL,
    pw   VARCHAR(255)   NOT NULL,
    phone   VARCHAR(255)   NULL,
    gender   varchar(1)   NULL,
    birth   VARCHAR(255)   NULL,
    height   DECIMAL(5,2)   NULL,
    weight   DECIMAL(5,2)   NULL,
    body_metric   INT   NULL   COMMENT '회원 가입 할때 defualt로 계산 값 입력',
    point   INT   NULL,
    created_at   DATETIME   NOT NULL default now(),
    login_failure_count   int   NULL default 0,
    login_lock_until   datetime   NULL   COMMENT '연속5회 비밀번호 오류시 15분 접속 제한',
    quit_date   datetime   NULL,
    status   bigint   NOT NULL   DEFAULT 1,
    level   bigint   NOT NULL default 1,
    constraint pk_member_id primary key(id),
    constraint ck_member_gender check(gender in('M','F'))
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS member_rank (
                                           id   bigint   NOT NULL auto_increment,
                                           name   varchar(255)   NULL,
    badge_count   int   NULL   COMMENT '뱃지 갯수',
    constraint pk_member_rank_id primary key(id)
    ) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS member_authority (
                                                member_id   bigint   NOT NULL ,
                                                authories_id   bigint   NOT NULL,
                                                constraint pk_member_authority_member_id_authories_id primary key(member_id,authories_id)
    ) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS authorites (
                                          id   bigint   NOT NULL auto_increment,
                                          authurity   VARCHAR(255)   NOT NULL,
    description   varchar(255)   NULL,
    constraint pk_authorites_id primary key(id)
    ) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS login_failure_history (
                                                     id   bigint   NOT NULL auto_increment,
                                                     failure_datetime   datetime   NOT NULL,
                                                     failure_ip   varchar(255)   NULL,
    failure_reasone   varchar(2000)   NULL,
    member_id   bigint   NOT NULL,
    constraint pk_login_failure_history_id primary key(id)
    ) ENGINE=InnoDB;



CREATE TABLE IF NOT EXISTS refresh_token (
                                             id   bigint   NOT NULL auto_increment,
                                             token_hash   varchar(128)   NOT NULL,
    jti   varchar(64)   NULL   COMMENT 'unique 제약조건',
    issued_at   datetime   NULL default now(),
    expires_at   datetime   NULL,
    revoked   tinyint   NULL   DEFAULT 0,
    revoked_at   datetime   NULL,
    device_fp   varchar(255)   NULL,
    ip   varchar(255)   NULL,
    last_used_at   datetime   NULL default now(),
    member_id   bigint   NOT NULL,
    constraint pk_refresh_token_id primary key(id),
    constraint uk_refresh_token_jti unique(jti)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS login_history (
                                             id   bigint   NOT NULL auto_increment,
                                             login_date   datetime   NOT NULL default now(),
    come_in_ip   varchar(255)   NULL,
    before_path   varchar(255)   NULL,
    member_id   bigint   NOT NULL,
    constraint pk_login_history_id primary key(id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS extend_file_path (
                                                id   BIGINT   NOT NULL auto_increment,
                                                url_path   VARCHAR(255) NULL,
    constraint pk_extend_file_path_id primary key(id)
    ) ENGINE=InnoDB;


-- 1) 태그
CREATE TABLE IF NOT EXISTS tag (
                                   id   INT   NOT NULL      AUTO_INCREMENT   ,
                                   name   VARCHAR(255)   NOT NULL   ,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB;


-- 2) 게시판 (post)
CREATE TABLE IF NOT EXISTS post (
                                    id   INT   NOT NULL    AUTO_INCREMENT   ,
                                    title   VARCHAR(255)   NOT NULL,
    content   VARCHAR(255)   NULL,
    visibility   TINYINT(1)   NULL   DEFAULT 0   ,
    created_at   DATETIME   NOT NULL   DEFAULT CURRENT_TIMESTAMP ,
    member_id   bigint   NOT NULL,
    tag_id   INT   NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB;

-- 3) 게시판 좋아요 (post_like)
CREATE TABLE IF NOT EXISTS post_like (
                                         id   bigint   NOT NULL    AUTO_INCREMENT   ,
                                         like_created   DATETIME   NOT NULL   DEFAULT CURRENT_TIMESTAMP ,
                                         member_id   bigint   NOT NULL,
                                         post_id   INT   NOT NULL,
                                         PRIMARY KEY (id)
    ) ENGINE=InnoDB;


-- 4) 게시판 댓글 (post_comment) + 대댓글 지원
CREATE TABLE IF NOT EXISTS post_comment (
                                            id   INT   NOT NULL    AUTO_INCREMENT   ,
                                            content   VARCHAR(255)   NOT NULL,
    create_at   DATETIME   NOT NULL   DEFAULT CURRENT_TIMESTAMP,
    post_id   INT   NOT NULL,
    member_id   BIGINT   NOT NULL,
    member_parent_comment_id   INT   NULL,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB;



-- 5) 댓글 좋아요 (comment_like)
CREATE TABLE IF NOT EXISTS comment_like (
                                            id   bigint   NOT NULL    AUTO_INCREMENT,
                                            create_at   DATETIME   NULL   DEFAULT CURRENT_TIMESTAMP ,
                                            member_id   bigint   NOT NULL,
                                            post_comment_id   INT   NOT NULL,
                                            PRIMARY KEY (id)
    ) ENGINE=InnoDB;

-- 6) 게시물 파일 업로드 (post_file)
CREATE TABLE IF NOT EXISTS post_file (
                                         id   INT   NOT NULL    AUTO_INCREMENT   ,
                                         name   VARCHAR(255)   NULL,
    url   VARCHAR(255)   NOT NULL,
    mime_type   VARCHAR(255)   NULL,
    path   VARCHAR(255)   NOT NULL,
    created_at   DATETIME   NULL   DEFAULT CURRENT_TIMESTAMP ,
    state   VARCHAR(255)   NULL,
    re_name   VARCHAR(255)   NULL,
    post_id   INT   NOT NULL,
    extend_file_path_id   INT   NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS post_tag (
                                        id   INT   NOT NULL    AUTO_INCREMENT   ,
                                        name   VARCHAR(255)   NULL,
    post_id   INT   NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS food (
                                    id   BIGINT   NOT NULL   AUTO_INCREMENT,
                                    name   VARCHAR(255)   NOT NULL,
    gram   INTEGER   NOT NULL,
    kcal   DECIMAL(8,2)   NOT NULL,
    carbo   DECIMAL(8,2)   NOT NULL,
    protein   DECIMAL(8,2)   NOT NULL,
    fat   DECIMAL(8,2)   NOT NULL,
    sodium   DECIMAL(10,2)   NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS meal (
                                    id   BIGINT   NOT NULL   AUTO_INCREMENT ,
                                    type   ENUM('BREAKFAST','LUNCH','DINNER','SNACK')   NOT NULL,
    date   DATE   NOT NULL,
    created_at   DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    member_id   BIGINT   NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS food_fileupload (
                                               id   INT   NOT NULL   AUTO_INCREMENT ,
                                               meal_id   BIGINT   NOT NULL   ,
                                               name   VARCHAR(255)   NOT NULL,
    type   VARCHAR(255)   NOT NULL,
    re_name   VARCHAR(255)   NOT NULL,
    path   VARCHAR(255)   NOT NULL,
    create_at   DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    upload_order   INT   NOT NULL,
    thumb_path   VARCHAR(255)   NOT NULL,
    extend_file_path_id   BIGINT   NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE=INNODB;


CREATE TABLE IF NOT EXISTS ai_diet (
                                       id   BIGINT   NOT NULL AUTO_INCREMENT   ,
                                       type   ENUM('BREAKFAST','LUNCH','DINNER','SNACK') NOT NULL,
    total_kcal   DECIMAL(8,2) NOT NULL,
    kcal   DECIMAL(8,2)   NOT NULL,
    total_protein   DECIMAL(8,2)   NOT NULL,
    total_fat   DECIMAL(8,2)   NOT NULL,
    created_at   DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name   VARCHAR(255)   NOT NULL,
    member_id   bigint   NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE=INNODB;


CREATE TABLE IF NOT EXISTS meal_food (
                                         meal_id   BIGINT   NOT NULL,
                                         food_id   BIGINT   NOT NULL,
                                         PRIMARY KEY (meal_id, food_id)
    ) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS report_base (
                                           id INT NOT NULL AUTO_INCREMENT,
                                           title VARCHAR(255) NOT NULL,
    count INT NOT NULL,
    day_of_ban INT DEFAULT 0,
    PRIMARY KEY (id)
    ) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS report (
                                      id BIGINT NOT NULL AUTO_INCREMENT,
                                      title VARCHAR(255) NOT NULL,
    contents VARCHAR(255) NOT NULL,
    yn BOOLEAN NOT NULL,
    date DATETIME NOT NULL,
    report_image_url VARCHAR(500),
    member_id2 BIGINT NOT NULL,
    post_id INT,
    comment_id INT,
    admin_id BIGINT,
    report_id INT NOT NULL,
    member_id BIGINT NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS report_fileupload (
                                                 id INT NOT NULL AUTO_INCREMENT,
                                                 report_id BIGINT NOT NULL,
                                                 name VARCHAR(255),
    type VARCHAR(255),
    `rename` VARCHAR(255),
    path VARCHAR(255),
    create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    thumb_path VARCHAR(255),
    upload_order INT DEFAULT 1,
    extend_file_path_id BIGINT,
    PRIMARY KEY (id)
    ) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS exercise (
                                        id INT NOT NULL AUTO_INCREMENT,
                                        date DATE NOT NULL,
                                        type VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    min INT NOT NULL,
    burned_kcal INT NOT NULL,
    create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    member_id BIGINT NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS exercise_fileupload (
                                                   id INT NOT NULL AUTO_INCREMENT,
                                                   name VARCHAR(255),
    type VARCHAR(255),
    re_name VARCHAR(255),
    path VARCHAR(255),
    thumb_path VARCHAR(255),
    upload_order INT DEFAULT 1,
    create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    exercise_id INT NOT NULL,
    extend_file_path_id BIGINT,
    PRIMARY KEY (id)
    ) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS black_list (
                                          member_id BIGINT NOT NULL,
                                          create_date DATETIME NOT NULL,
                                          reason VARCHAR(2000) NOT NULL,
    admin_id BIGINT,
    PRIMARY KEY (member_id)
    ) ENGINE=INNODB;

/* DIARY 테이블 */

CREATE TABLE `diary` (
                         `id` INT NOT NULL AUTO_INCREMENT,
                         `day` DATETIME NOT NULL,
                         `weight` INT NOT NULL,
                         `mood` ENUM('아주좋음', '좋음', '보통', '나쁨', '아주나쁨'),
                         `condition` VARCHAR(255) NOT NULL,
                         `memo` VARCHAR(500) NOT NULL,
                         `member_id` BIGINT NOT NULL,
                         CONSTRAINT `pk_diary` PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='일기';


/* DIARY_FILE 테이블 */

CREATE TABLE `diary_file` (
                              `id` INT NOT NULL AUTO_INCREMENT,
                              `mime` VARCHAR(255) NOT NULL,
                              `path` VARCHAR(255) NOT NULL,
                              `created_at` DATETIME NOT NULL,
                              `state` VARCHAR(255) NOT NULL,
                              `original_file` VARCHAR(255) NOT NULL,
                              `rename` INT NOT NULL,
                              `diary_id` INT NOT NULL,
                              `extend_file_path_id` BIGINT NOT NULL,
                              CONSTRAINT `pk_diary_file` PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='일기 파일 업로드';


/* QNA 테이블 */

CREATE TABLE `qna` (
                       `id` BIGINT NOT NULL AUTO_INCREMENT,
                       `title` VARCHAR(255) NOT NULL,
                       `contents` VARCHAR(500) NOT NULL,
                       `created_at` DATETIME NOT NULL,
                       `member_id` BIGINT NOT NULL,
                       CONSTRAINT `pk_qna` PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='문의사항 게시글';


/* QNA_COMMENT 테이블 */

CREATE TABLE `qna_comment` (
                               `id` BIGINT NOT NULL AUTO_INCREMENT,
                               `comment` VARCHAR(500) NOT NULL,
                               `created_at` DATETIME NOT NULL,
                               `qna_id` BIGINT NOT NULL,
                               `member_id` BIGINT NOT NULL,
                               `parent_comment_id` BIGINT NULL,
                               CONSTRAINT `pk_qna_comment` PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='문의사항 댓글';


/* CALENDER 테이블 */

CREATE TABLE `calender` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT,
                            `cal_day` DATETIME NOT NULL,
                            `badge_count` INT NOT NULL,
                            `exercise_status` INT NOT NULL DEFAULT 0,
                            `meal_status` INT NOT NULL DEFAULT 0,
                            `diary_status` INT NOT NULL DEFAULT 0,
                            `member_id` BIGINT NOT NULL DEFAULT 0,
                            CONSTRAINT `pk_calender` PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='캘린더';
ALTER TABLE diary_file add CONSTRAINT `fk_diary_to_diary_file_1` FOREIGN KEY (`diary_id`) REFERENCES `diary` (`id`);

/* BINGO_BOARD */
CREATE TABLE `bingo_board` (
                               `id` INT NOT NULL,
                               `title` VARCHAR(255) NOT NULL,
                               `size` INT NOT NULL,
                               `start_date` DATE NOT NULL,
                               `end_date` DATE NULL,
                               `created_at` DATETIME NOT NULL,
                               `member_id` BIGINT NOT NULL,
                               CONSTRAINT pk_bingo_board_id PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT '';

/* BINGO_CELL */
CREATE TABLE `bingo_cell` (
                              `id` INT NOT NULL,
                              `row` INT NOT NULL,
                              `col` INT NOT NULL,
                              `label` VARCHAR(255) NOT NULL,
                              `is_checked` TINYINT(1) NOT NULL,
                              `checked_at` DATETIME NULL,
                              `bingo_board_id` INT NOT NULL,
                              CONSTRAINT ck_bingo_cell_is_checked CHECK (`is_checked` IN (0,1)),
                              CONSTRAINT pk_bingo_cell_id PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/* BINGO_FILEUPLOAD */
CREATE TABLE `bingo_fileupload` (
                                    `id` INT NOT NULL,
                                    `name` VARCHAR(500) NOT NULL,
                                    `mime_type` VARCHAR(500) NOT NULL,
                                    `re_name` VARCHAR(255) NULL,
                                    `path` VARCHAR(255) NOT NULL,
                                    `created_at` DATETIME NULL,
                                    `bingo_cell_id` INT NOT NULL,
                                    `extend_file_path_id` INT NOT NULL,
                                    CONSTRAINT pk_bingo_fileupload_id PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/* GACHA_RESET (리셋 정책 마스터) */
CREATE TABLE `gacha_reset` (
                               `id` BIGINT NOT NULL,
                               `name` VARCHAR(255) NULL COMMENT '1: 1등뽑히면 리셋, 2: 모든 경품 뽑히면 리셋, 3: 기간 지나면 리셋',
                               `use_point` INT NULL COMMENT '사용포인트',
                               CONSTRAINT pk_gacha_reset_id PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/* GACHA_EVENT */
CREATE TABLE `gacha_event` (
                               `id` BIGINT NOT NULL,
                               `start_at` DATETIME NOT NULL COMMENT '시작일시',
                               `point` INT NULL DEFAULT 0,
                               `end_at` DATETIME NOT NULL COMMENT '종료일시',
                               `status` ENUM('DRAFT','ACTIVE','PAUSED','ENDED') NOT NULL DEFAULT 'DRAFT' COMMENT '상태(DRAFT:작성중, ACTIVE:진행중, PAUSED:일시중지, ENDED:종료)',
                               `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
                               `event_id2` BIGINT NOT NULL COMMENT '리셋 정책 ID',
                               CONSTRAINT pk_gacha_event_id PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/* GACHA_PRIZE */
CREATE TABLE `gacha_prize` (
                               `id` BIGINT NOT NULL COMMENT '경품ID',
                               `name` VARCHAR(100) NOT NULL COMMENT '경품명',
                               `payload_json` JSON NULL COMMENT '경품내용(JSON: 포인트금액/쿠폰코드/아이템식별 등)',
                               `prize_type` ENUM('POINT','COUPON','ITEM','NOTHING','ETC') NOT NULL COMMENT '경품종류(포인트/쿠폰/아이템/꽝/기타)',
                               `rank` INT NULL,
                               `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
                               `quantity` INT NULL DEFAULT 0,
                               `gacha_event_id` BIGINT NOT NULL,
                               CONSTRAINT pk_gacha_prize_id PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/* GACHA_QUANTITY (경품 재고 수량 - 경품ID 1:1) */
CREATE TABLE `gacha_quantity` (
                                  `id` BIGINT NOT NULL COMMENT '경품ID',
                                  `count` INT NULL,
                                  CONSTRAINT pk_gacha_quantity_id PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/* GACHA_BOARD_CELL (사용자별 보드 셀 상태) */
CREATE TABLE `gacha_board_cell` (
                                    `id` BIGINT NOT NULL AUTO_INCREMENT,
                                    `status` ENUM('COVERED','OPENED') NOT NULL DEFAULT 'COVERED',
                                    `opened_at` DATETIME NULL,
                                    `rows` INT NULL,
                                    `cols` INT NULL,
                                    `opened_count` INT NULL,
                                    `created_at` DATETIME NULL,
                                    `updated_at` DATETIME NULL,
                                    `gacha_prize_id` BIGINT NOT NULL COMMENT '경품ID',
                                    `gacha_event_id` BIGINT NOT NULL,
                                    `member_id` BIGINT NOT NULL COMMENT '해당 칸을 오픈 한 회원ID',
                                    CONSTRAINT pk_gacha_board_cell_id PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/* GACHA_DRAW_LOG (뽑기실행로그) */
CREATE TABLE `gacha_draw_log` (
                                  `id` INT NOT NULL AUTO_INCREMENT COMMENT '뽑기실행로그번호',
                                  `created_at` DATETIME NOT NULL COMMENT '뽑기실행로그생성일',
                                  `member_id` BIGINT NOT NULL COMMENT '회원번호',
                                  `gacha_board_cell_id` BIGINT NOT NULL COMMENT '뽑기보드칸번호',
                                  `gacha_prize_id` BIGINT NOT NULL COMMENT '뽑기경품번호',
                                  CONSTRAINT pk_gacha_draw_log_id PRIMARY KEY(`id`)
) ENGINE=InnoDB COMMENT '뽑기실행로그';

/* GACHA_REWARD_GRANT (지급 로그) */
CREATE TABLE `gacha_reward_grant` (
                                      `id` BIGINT NOT NULL,
                                      `grant_status` ENUM('QUEUED','GRANTED','FAILED') NOT NULL DEFAULT 'QUEUED' COMMENT '지급상태',
                                      `granted_at` DATETIME NULL COMMENT '지급일시',
                                      `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
                                      `gacha_board_cell_id` BIGINT NOT NULL,
                                      CONSTRAINT pk_gacha_reward_grant_id PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/* GACHA_FILEUPLOAD */
CREATE TABLE `gacha_fileupload` (
                                    `id` INT NOT NULL,
                                    `extend_file_path_id` BIGINT NOT NULL,
                                    `gacha_event_id` BIGINT NOT NULL,
                                    `name` VARCHAR(255) NOT NULL,
                                    `mime_type` VARCHAR(255) NOT NULL,
                                    `re_name` VARCHAR(255) NULL,
                                    `url` VARCHAR(255) NOT NULL,
                                    `create_at` DATETIME NULL,
                                    CONSTRAINT pk_gacha_fileupload_id PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


/* POINT (적립/사용 내역) */
CREATE TABLE `point` (
                         `point_id` BIGINT NOT NULL,
                         `point` INT NULL,
                         `distinction` ENUM('EARN','USE') NULL COMMENT '1: 획득(EARN), 2: 사용(USE)',
                         `member_id` BIGINT NOT NULL,
                         `diary_id` INT NOT NULL,
                         `calender_id` BIGINT NOT NULL,
                         `gacha_event_id` BIGINT NOT NULL,
                         `bingo_board_id` INT NOT NULL,
                         CONSTRAINT pk_point_point_id PRIMARY KEY (`point_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE IF NOT EXISTS member_allergy (
                                              id	INT	NOT NULL	AUTO_INCREMENT,
                                              member_id	BIGINT	NOT NULL,
                                              allergy_id	INT	NOT NULL,
                                              PRIMARY KEY (id)
    ) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS allergy (
                                       id	INT	NOT NULL	AUTO_INCREMENT ,
                                       name	VARCHAR(255)	NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS food_allergy (
                                            id	BIGINT	NOT NULL	AUTO_INCREMENT ,
                                            meal_id	BIGINT	NOT NULL,
                                            allergy_id	BIGINT	NOT NULL,
                                            PRIMARY KEY (id)
    ) ENGINE=INNODB;

-- ----- 제약조건 ----------
-- ✅ 외래키 검사 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;

ALTER TABLE post add CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES member(id);
ALTER TABLE post add CONSTRAINT fk_post_tag FOREIGN KEY (tag_id) REFERENCES tag(id);

ALTER TABLE post_like add CONSTRAINT fk_postlike_post FOREIGN KEY (post_id) REFERENCES post(id);
ALTER TABLE post_like add CONSTRAINT fk_postlike_member FOREIGN KEY (member_id) REFERENCES member(id);
ALTER TABLE post_like add CONSTRAINT uq_post_like UNIQUE (post_id, member_id);
ALTER TABLE post_like
DROP FOREIGN KEY fk_post_like_post_id;

ALTER TABLE post_like
    ADD CONSTRAINT fk_post_like_post_id
        FOREIGN KEY (post_id) REFERENCES post(id)
            ON DELETE CASCADE;


ALTER TABLE post_comment add CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES post(id);
ALTER TABLE post_comment add CONSTRAINT fk_comment_member FOREIGN KEY (member_id) REFERENCES member(id);
ALTER TABLE post_comment add CONSTRAINT fk_comment_parent FOREIGN KEY (member_parent_comment_id) REFERENCES post_comment(id);
ALTER TABLE post_comment
DROP FOREIGN KEY fk_post_comment_post_id;

ALTER TABLE post_comment
    ADD CONSTRAINT fk_post_comment_post_id
        FOREIGN KEY (post_id) REFERENCES post(id)
            ON DELETE CASCADE;

ALTER TABLE comment_like add CONSTRAINT fk_cmtlike_comment FOREIGN KEY (post_comment_id) REFERENCES post_comment(id);
ALTER TABLE comment_like add CONSTRAINT fk_cmtlike_member FOREIGN KEY (member_id) REFERENCES member(id);
ALTER TABLE comment_like add CONSTRAINT uq_comment_like UNIQUE (post_comment_id, member_id);

ALTER TABLE post_file add CONSTRAINT fk_postfile_post FOREIGN KEY (post_id) REFERENCES post(id);
ALTER TABLE post_file
DROP FOREIGN KEY fk_post_file_post_id;

ALTER TABLE post_file
    ADD CONSTRAINT fk_post_file_post_id
        FOREIGN KEY (post_id) REFERENCES post(id)
            ON DELETE CASCADE;

ALTER TABLE post_tag add CONSTRAINT fk_posttag_post FOREIGN KEY (post_id) REFERENCES post(id);

alter table upload_file add constraint fk_upload_file_member  foreign key(member_id)  references member(id);
alter table upload_file add constraint fk_upload_file_extend_file_path  foreign key(extend_file_path_id)  references extend_file_path(id);
alter table goal add constraint fk_goal_member   foreign key(member_id)  references member(id);
alter table ban add constraint fk_ban_member_member   foreign key(admin_id)  references member(id);
alter table ban add constraint fk_ban_member_admin   foreign key(member_id)  references member(id);
alter table ban add constraint fk_ban_report   foreign key(report_no)  references report(id);
alter table member add constraint fk_member_member_status   foreign key(status)  references member_status(id);
alter table member add constraint fk_member_member_rank   foreign key(level)  references member_rank(id);
alter table member_authority add constraint fk_member_authority_member   foreign key(member_id)  references member(id);
alter table member_authority add constraint fk_member_authority_authorites   foreign key(authories_id)  references authorites(id);
alter table login_failure_history add constraint fk_login_failure_history_member   foreign key(member_id)  references member(id) ;
alter table login_history add constraint fk_login_history_member   foreign key(member_id)  references member(id) ;
alter table refresh_token add constraint fk_refresh_token_member   foreign key(member_id)  references member(id);

ALTER TABLE meal_food ADD CONSTRAINT FK_meal_TO_meal_food_1 FOREIGN KEY (
                                                                         meal_id
    )
    REFERENCES meal (
                     id
        );

ALTER TABLE meal_food ADD CONSTRAINT FK_food_TO_meal_food_1 FOREIGN KEY (
                                                                         food_id
    )
    REFERENCES food (
                     id
        );

ALTER TABLE exercise_fileupload
    ADD CONSTRAINT fk_fileupload_exercise
        FOREIGN KEY (exercise_id) REFERENCES exercise(id)
            ON DELETE CASCADE;

ALTER TABLE report
    ADD CONSTRAINT fk_report_report_base
        FOREIGN KEY (report_id) REFERENCES report_base(id);

ALTER TABLE report_fileupload
    ADD CONSTRAINT fk_report_fileupload_report
        FOREIGN KEY (report_id) REFERENCES report(id)
            ON DELETE CASCADE;

ALTER TABLE black_list
    ADD CONSTRAINT fk_blacklist_admin
        FOREIGN KEY (admin_id) REFERENCES member(id);

ALTER TABLE diary_file add CONSTRAINT `fk_extend_file_path_to_diary_file_1` FOREIGN KEY (`extend_file_path_id`) REFERENCES `extend_file_path` (`id`);




ALTER TABLE qna_comment add CONSTRAINT `fk_qna_to_qna_comment_1` FOREIGN KEY (`qna_id`) REFERENCES `qna` (`id`);

ALTER TABLE qna_comment add CONSTRAINT `fk_member_to_qna_comment_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`);

ALTER TABLE qna_comment add CONSTRAINT `fk_qna_comment_to_qna_comment_1` FOREIGN KEY (`parent_comment_id`) REFERENCES `qna_comment` (`id`);



ALTER TABLE calender add CONSTRAINT `fk_member_to_calender_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`);



ALTER TABLE qna add CONSTRAINT `fk_member_to_qna_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`);



ALTER TABLE diary add CONSTRAINT `fk_member_to_diary_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`);

/* bingo_cell → bingo_board */
ALTER TABLE `bingo_cell`
    ADD CONSTRAINT `fk_bingo_cell_bingo_board`
        FOREIGN KEY (`bingo_board_id`) REFERENCES `bingo_board`(`id`)
            ON DELETE CASCADE ON UPDATE CASCADE;

/* bingo_board → member (이름도 의미있게 변경) */
ALTER TABLE `bingo_board`
    ADD CONSTRAINT `fk_bingo_board_member`
        FOREIGN KEY (`member_id`) REFERENCES `member`(`id`)
            ON DELETE RESTRICT ON UPDATE CASCADE;

/* bingo_fileupload → bingo_cell (원래 보드가 아니라 ‘칸’을 참조해야 자연스러움) */
ALTER TABLE `bingo_fileupload`
    ADD CONSTRAINT `fk_bingo_fileupload_bingo_cell_id`
        FOREIGN KEY (`bingo_cell_id`) REFERENCES `bingo_cell`(`id`)
            ON DELETE CASCADE ON UPDATE CASCADE;

/* gacha_event → gacha_reset */
ALTER TABLE `gacha_event`
    ADD CONSTRAINT `fk_gacha_event_gacha_reset`
        FOREIGN KEY (`event_id2`) REFERENCES `gacha_reset`(`id`)
            ON DELETE RESTRICT ON UPDATE CASCADE;

/* gacha_prize → gacha_event */
ALTER TABLE `gacha_prize`
    ADD CONSTRAINT `fk_gacha_prize_gacha_event`
        FOREIGN KEY (`gacha_event_id`) REFERENCES `gacha_event`(`id`)
            ON DELETE CASCADE ON UPDATE CASCADE;

/* gacha_quantity(PK=ID) → gacha_prize(ID) */
ALTER TABLE `gacha_quantity`
    ADD CONSTRAINT `fk_gacha_quantity_gacha_prize`
        FOREIGN KEY (`id`) REFERENCES `gacha_prize`(`id`)
            ON DELETE CASCADE ON UPDATE CASCADE;

/* gacha_board_cell → gacha_prize / gacha_event / member */
ALTER TABLE `gacha_board_cell`
    ADD CONSTRAINT `fk_gacha_board_cell_gacha_prize`
        FOREIGN KEY (`gacha_prize_id`) REFERENCES `gacha_prize`(`id`)
            ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `gacha_board_cell`
    ADD CONSTRAINT `fk_gacha_board_cell_gacha_event`
        FOREIGN KEY (`gacha_event_id`) REFERENCES `gacha_event`(`id`)
            ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `gacha_board_cell`
    ADD CONSTRAINT `fk_gacha_board_cell_member`
        FOREIGN KEY (`member_id`) REFERENCES `member`(`id`)
            ON DELETE RESTRICT ON UPDATE CASCADE;

/* gacha_reward_grant → gacha_board_cell */
ALTER TABLE `gacha_reward_grant`
    ADD CONSTRAINT `fk_gacha_reward_grant_gacha_board_cell`
        FOREIGN KEY (`gacha_board_cell_id`) REFERENCES `gacha_board_cell`(`id`)
            ON DELETE CASCADE ON UPDATE CASCADE;

/* gacha_fileupload → gacha_event (이름 고유하게 다시 추가) */
ALTER TABLE `gacha_fileupload`
    ADD CONSTRAINT `fk_gacha_fileupload_gacha_event_id`
        FOREIGN KEY (`gacha_event_id`) REFERENCES `gacha_event`(`id`)
            ON DELETE CASCADE ON UPDATE CASCADE;

/* point → member / diary / calender (각각 고유 이름) */
ALTER TABLE `point`
    ADD CONSTRAINT `fk_point_member_id`
        FOREIGN KEY (`member_id`) REFERENCES `member`(`id`)
            ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `point`
    ADD CONSTRAINT `fk_point_diary_id`
        FOREIGN KEY (`diary_id`) REFERENCES `diary`(`id`)
            ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE `point`
    ADD CONSTRAINT `fk_point_calender_id`
        FOREIGN KEY (`calender_id`) REFERENCES `calender`(`id`)
            ON DELETE RESTRICT ON UPDATE CASCADE;

ALTER TABLE point
    ADD CONSTRAINT fk_point_gacha_event_id
        FOREIGN KEY (gacha_event_id) REFERENCES gacha_event(id);
ALTER TABLE point
    ADD CONSTRAINT fk_point_bingo_board_id
        FOREIGN KEY (bingo_board_id) REFERENCES bingo_board(id);

-- ------------ DML --------- --
-- '전체'는 필터조건없이 불러오면되고, '내글'은 자신의 회원번호와 게시물번호로 불러오면 됨.
INSERT INTO tag (name) VALUES
                           ('운동'),
                           ('식단'),
                           ('Before&After'),
                           ('자유게시판');


INSERT INTO authorites (
    authurity, description
)
VALUES
    ('ROLE_ADMIN', '시스템 관리 권한 — 모든 회원 관리 및 설정 변경 가능'),
    ('ROLE_MEMBER', '일반 사용자 권한 — 서비스 이용 및 기본 기능 접근 가능');


INSERT INTO member_status (
    STATUS
)
VALUES
    ('정상'),
    ('탈퇴'),
    ('정지'),
    ('휴먼상태'),
    ('블랙리스트');

INSERT INTO member_rank(
    NAME , badge_count )
VALUES
    ('새싹이',0),
    ('튼튼이',20),
    ('헬린이',50);


INSERT INTO member (
    name, nickname, email, pw, phone, gender, birth, height, weight, body_metric, point,
    created_at, login_failure_count, login_lock_until, quit_date, status, level
)
VALUES
    ('세종대왕', '훈민정음', 'sejong@gmail.com', 'pw1234!', '010-1111-1111', 'M', '1397-05-15', 175.00, 70.50, 1, 95, NOW(), 0, NULL, NULL, 1, 1),
    ('이순신', '충무공', 'leesoonshin@gmail.com', 'pw1234!', '010-2222-2222', 'M', '1545-04-28', 178.20, 75.30, 1, 92, NOW(), 0, NULL, NULL, 1, 1),
    ('신사임당', '사임당', 'shinsaimdang@gmail.com', 'pw1234!', '010-3333-3333', 'F', '1504-10-29', 160.40, 55.20, 1, 88, NOW(), 0, NULL, NULL, 1, 1),
    ('장영실', '과학자', 'jangyoungsil@gmail.com', 'pw1234!', '010-4444-4444', 'M', '1390-03-20', 170.80, 68.10, 1, 83, NOW(), 0, NULL, NULL, 1, 1),
    ('김유신', '삼국통일', 'kimyushin@gmail.com', 'pw1234!', '010-5555-5555', 'M', '595-10-01', 176.50, 72.00, 1, 90, NOW(), 0, NULL, NULL, 1, 1),
    ('유관순', '만세소녀', 'yugwansoon@gmail.com', 'pw1234!', '010-6666-6666', 'F', '1902-12-16', 158.00, 50.30, 1, 85, NOW(), 0, NULL, NULL, 1, 1),
    ('정약용', '목민심서', 'jeongyakyong@gmail.com', 'pw1234!', '010-7777-7777', 'M', '1762-08-05', 172.00, 68.50, 1, 80, NOW(), 0, NULL, NULL, 1, 1),
    ('윤봉길', '의사', 'yunbonggil@gmail.com', 'pw1234!', '010-8888-8888', 'M', '1908-06-21', 174.50, 69.20, 1, 87, NOW(), 0, NULL, NULL, 1, 1),
    ('안중근', '의사', 'ahnjoonggeun@gmail.com', 'pw1234!', '010-9999-9999', 'M', '1879-09-02', 175.20, 71.80, 1, 93, NOW(), 0, NULL, NULL, 1, 1),
    ('허준', '동의보감', 'heojun@gmail.com', 'pw1234!', '010-1010-1010', 'M', '1539-03-05', 168.00, 65.00, 1, 76, NOW(), 0, NULL, NULL, 1, 1),
    ('이황', '퇴계', 'toegye@gmail.com', 'pw1234!', '010-1111-1212', 'M', '1501-11-25', 172.50, 66.80, 1, 70, NOW(), 0, NULL, NULL, 1, 1),
    ('이이', '율곡', 'yulgok@gmail.com', 'pw1234!', '010-1212-1313', 'M', '1536-12-26', 173.00, 67.50, 1, 74, NOW(), 0, NULL, NULL, 1, 1),
    ('김홍도', '단원', 'kimhongdo@gmail.com', 'pw1234!', '010-1313-1414', 'M', '1745-09-10', 171.60, 69.10, 1, 78, NOW(), 0, NULL, NULL, 1, 1),
    ('심사임당', '화가', 'simsaimdang@gmail.com', 'pw1234!', '010-1414-1515', 'F', '1504-10-29', 159.00, 54.00, 1, 82, NOW(), 0, NULL, NULL, 1, 1),
    ('박지원', '연암', 'parkjiwon@gmail.com', 'pw1234!', '010-1515-1616', 'M', '1737-02-05', 170.00, 66.00, 1, 68, NOW(), 0, NULL, NULL, 1, 1),
    ('김구', '백범', 'kimkoo@gmail.com', 'pw1234!', '010-1616-1717', 'M', '1876-08-29', 176.00, 73.00, 1, 99, NOW(), 0, NULL, NULL, 1, 1),
    ('안창호', '도산', 'ahndosan@gmail.com', 'pw1234!', '010-1717-1818', 'M', '1878-11-09', 177.00, 74.00, 1, 84, NOW(), 0, NULL, NULL, 1, 1),
    ('유성룡', '징비록', 'yuseongryong@gmail.com', 'pw1234!', '010-1818-1919', 'M', '1542-01-01', 173.50, 67.00, 1, 77, NOW(), 0, NULL, NULL, 1, 1),
    ('이방원', '태종', 'leebangwon@gmail.com', 'pw1234!', '010-1919-2020', 'M', '1367-06-13', 175.00, 72.00, 1, 81, NOW(), 0, NULL, NULL, 1, 1),
    ('신채호', '단재', 'shinchaeho@gmail.com', 'pw1234!', '010-2020-2121', 'M', '1880-12-08', 174.00, 70.00, 1, 79, NOW(), 0, NULL, NULL, 1, 1);



INSERT INTO post (title, content, member_id, tag_id) VALUES
                                                         ('오늘 하체 찢고 왔다', '레그데이 난이도 상🔥', 1, 1),
                                                         ('단백질 샐러드 추천', '닭가슴살+아보카도 조합 미쳤다', 2, 2),
                                                         ('다이어트 2주차 변화', '턱선 생김 ㄹㅇ', 3, 3),
                                                         ('운동 자극 사진 모음', '같이 자극받고 가요 💪', 4, 1),
                                                         ('식단 슬럼프 왔다ㅠ', '이제 먹을 게 없다', 5, 2),
                                                         ('나 오늘 PT 시작함', '지켜봐주세요!!', 1, 1),
                                                         ('저녁 식단 공유', '고구마+계란+샐러드', 2, 2),
                                                         ('다이어트 실패함..', '다시 처음부터 간다', 3, 3),
                                                         ('오늘 헬스장 사람 미쳤음', '기다리다가 운동 못함', 4, 1),
                                                         ('잡담) 날씨 너무 좋음', '뛰기 딱 좋다', 5, 4);

-- 수정해야 할수도 있음
INSERT INTO post_tag (name, post_id) VALUES
                                         ('#레그데이', 1),
                                         ('#샐러드맛집', 2),
                                         ('#다이어트중', 3),
                                         ('#운동자극', 4),
                                         ('#식단고민', 5),
                                         ('#PT일지', 6),
                                         ('#저녁식dm_room단', 7),
                                         ('#멘탈회복', 8),
                                         ('#헬스장지옥', 9),
                                         ('#일상', 10);

INSERT INTO post_like (post_id, member_id) VALUES
                                               (1, 2),
                                               (1, 3),
                                               (2, 1),
                                               (3, 4),
                                               (4, 2),
                                               (5, 3),
                                               (6, 5),
                                               (7, 4),
                                               (8, 1),
                                               (9, 2);


-- 일반 댓글 (1~6)
INSERT INTO post_comment (content, post_id, member_id) VALUES
                                                           ('자극받고 갑니다🔥', 1, 4),   -- comment_id = 1
                                                           ('진짜 맛있어 보이네요', 2, 5), -- 2
                                                           ('대단합니다...', 3, 1),       -- 3
                                                           ('사진 공유좀요!', 4, 2),     -- 4
                                                           ('식단 너무 공감...', 5, 3),   -- 5
                                                           ('화이팅 해요!', 6, 4);       -- 6

-- 대댓글 (7~10)
INSERT INTO post_comment (content, post_id, member_id, member_parent_comment_id) VALUES
                                                                                     ('감사합니다 🙏', 1, 1, 1),   -- 7
                                                                                     ('저도 해볼게요!', 2, 2, 2), -- 8
                                                                                     ('저도 같은 상황입니다', 3, 5, 3), -- 9
                                                                                     ('같이 힘냅시다 💪', 6, 3, 6); -- 10


INSERT INTO comment_like (post_comment_id, member_id) VALUES
                                                          (1, 2),
                                                          (1, 3),
                                                          (2, 1),
                                                          (3, 4),
                                                          (4, 5),
                                                          (5, 1),
                                                          (6, 2),
                                                          (7, 5),
                                                          (8, 4),
                                                          (9, 3);


INSERT INTO post_file (name, url, mime_type, path, state, re_name, post_id,extend_file_path_id)
VALUES
    ('post1.jpg', '/upload/post1.jpg', 'image/jpeg', '/var/upload/post1.jpg', 'ACTIVE', 'p1.jpg', 1,5),
    ('post2.jpg', '/upload/post2.jpg', 'image/jpeg', '/var/upload/post2.jpg', 'ACTIVE', 'p2.jpg', 2,6),
    ('post3.jpg', '/upload/post3.jpg', 'image/jpeg', '/var/upload/post3.jpg', 'ACTIVE', 'p3.jpg', 3,7),
    ('post4.jpg', '/upload/post4.jpg', 'image/jpeg', '/var/upload/post4.jpg', 'ACTIVE', 'p4.jpg', 4,8),
    ('post5.jpg', '/upload/post5.jpg', 'image/jpeg', '/var/upload/post5.jpg', 'ACTIVE', 'p5.jpg', 5,9),
    ('post6.jpg', '/upload/post6.jpg', 'image/jpeg', '/var/upload/post6.jpg', 'ACTIVE', 'p6.jpg', 6,11),
    ('post7.jpg', '/upload/post7.jpg', 'image/jpeg', '/var/upload/post7.jpg', 'ACTIVE', 'p7.jpg', 7,12),
    ('post8.jpg', '/upload/post8.jpg', 'image/jpeg', '/var/upload/post8.jpg', 'ACTIVE', 'p8.jpg', 8,16),
    ('post9.jpg', '/upload/post9.jpg', 'image/jpeg', '/var/upload/post9.jpg', 'ACTIVE', 'p9.jpg', 9,17),
    ('post10.jpg', '/upload/post10.jpg', 'image/jpeg', '/var/upload/post10.jpg', 'ACTIVE', 'p10.jpg', 10,18);



INSERT INTO extend_file_path (url_path) VALUES
                                            ('http://192.168.0.1:8080/upload/1'),
                                            ('http://192.168.0.1:8080/upload/2'),
                                            ('http://192.168.0.1:8080/upload/3'),
                                            ('http://192.168.0.1:8080/upload/4'),
                                            ('http://192.168.0.1:8080/upload/5'),
                                            ('http://192.168.0.1:8080/upload/6'),
                                            ('http://192.168.0.1:8080/upload/7'),
                                            ('http://192.168.0.1:8080/upload/8'),
                                            ('http://192.168.0.1:8080/upload/9'),
                                            ('http://192.168.0.1:8080/upload/10'),
                                            ('http://192.168.0.1:8080/upload/11'),
                                            ('http://192.168.0.1:8080/upload/12'),
                                            ('http://192.168.0.1:8080/upload/13'),
                                            ('http://192.168.0.1:8080/upload/14'),
                                            ('http://192.168.0.1:8080/upload/15'),
                                            ('http://192.168.0.1:8080/upload/16'),
                                            ('http://192.168.0.1:8080/upload/17'),
                                            ('http://192.168.0.1:8080/upload/18'),
                                            ('http://192.168.0.1:8080/upload/19'),
                                            ('http://192.168.0.1:8080/upload/20'),
                                            ('http://192.168.0.1:8080/upload/21'),
                                            ('http://192.168.0.1:8080/upload/22'),
                                            ('http://192.168.0.1:8080/upload/23'),
                                            ('http://192.168.0.1:8080/upload/24'),
                                            ('http://192.168.0.1:8080/upload/25'),
                                            ('http://192.168.0.1:8080/upload/26'),
                                            ('http://192.168.0.1:8080/upload/27'),
                                            ('http://192.168.0.1:8080/upload/28'),
                                            ('http://192.168.0.1:8080/upload/29');



INSERT INTO upload_file (
    mime_type, file_path, created_at, State,
    original_file_name, re_file_name, member_id, extend_file_path_id
)
VALUES
    ('image/jpeg', '/uploads/2025/11/01/profile1.jpg', '2025-11-01 10:15:23', 'ACTIVE', 'profile1.jpg', 'f1a2b3c4d5.jpg', 1, 10),
    ('image/png', '/uploads/2025/11/01/profile2.png', '2025-11-01 11:42:10', 'ACTIVE', 'profile2.png', 'a2b3c4d5e6.png', 2, 11),
    ('application/pdf', '/uploads/2025/11/01/doc1.pdf', '2025-11-01 13:21:55', 'INACTIVE', 'resume.pdf', 'x9y8z7w6v5.pdf', 3, 12),
    ('image/jpeg', '/uploads/2025/11/02/mountain1.jpg', '2025-11-02 09:32:10', 'ACTIVE', 'mountain1.jpg', 'j1k2l3m4n5.jpg', 1, 13),
    ('image/png', '/uploads/2025/11/02/mountain2.png', '2025-11-02 14:12:59', 'ACTIVE', 'mountain2.png', 'k2l3m4n5o6.png', 4, 14),
    ('application/pdf', '/uploads/2025/11/02/report.pdf', '2025-11-02 16:44:03', 'INACTIVE', 'report.pdf', 'r5t6y7u8i9.pdf', 5, 15),
    ('image/jpeg', '/uploads/2025/11/03/avatar1.jpg', '2025-11-03 08:01:44', 'ACTIVE', 'avatar1.jpg', 'q1w2e3r4t5.jpg', 2, 16),
    ('image/png', '/uploads/2025/11/03/avatar2.png', '2025-11-03 11:27:18', 'ACTIVE', 'avatar2.png', 'w2e3r4t5y6.png', 3, 17),
    ('image/jpeg', '/uploads/2025/11/03/avatar3.jpg', '2025-11-03 12:48:30', 'INACTIVE', 'avatar3.jpg', 'e3r4t5y6u7.jpg', 4, 18),
    ('image/jpeg', '/uploads/2025/11/04/photo1.jpg', '2025-11-04 09:50:10', 'ACTIVE', 'photo1.jpg', 'z1x2c3v4b5.jpg', 1, 19),
    ('image/png', '/uploads/2025/11/04/photo2.png', '2025-11-04 10:33:55', 'ACTIVE', 'photo2.png', 'x2c3v4b5n6.png', 2, 20),
    ('application/pdf', '/uploads/2025/11/04/manual.pdf', '2025-11-04 13:21:22', 'INACTIVE', 'manual.pdf', 'p9o8i7u6y5.pdf', 5, 21),
    ('image/jpeg', '/uploads/2025/11/05/pic1.jpg', '2025-11-05 08:12:14', 'ACTIVE', 'pic1.jpg', 'a1s2d3f4g5.jpg', 1, 22),
    ('image/jpeg', '/uploads/2025/11/05/pic2.jpg', '2025-11-05 09:33:41', 'ACTIVE', 'pic2.jpg', 's2d3f4g5h6.jpg', 2, 23),
    ('image/png', '/uploads/2025/11/05/pic3.png', '2025-11-05 11:19:50', 'INACTIVE', 'pic3.png', 'd3f4g5h6j7.png', 3, 24),
    ('image/jpeg', '/uploads/2025/11/06/user1.jpg', '2025-11-06 09:00:00', 'ACTIVE', 'user1.jpg', 'u1i2o3p4q5.jpg', 4, 25),
    ('image/png', '/uploads/2025/11/06/user2.png', '2025-11-06 09:15:25', 'ACTIVE', 'user2.png', 'i2o3p4q5r6.png', 5, 26),
    ('image/jpeg', '/uploads/2025/11/06/user3.jpg', '2025-11-06 10:42:38', 'ACTIVE', 'user3.jpg', 'o3p4q5r6s7.jpg', 2, 27),
    ('application/pdf', '/uploads/2025/11/06/doc2.pdf', '2025-11-06 11:58:00', 'INACTIVE', 'document.pdf', 'r4s5t6y7u8.pdf', 3, 28),
    ('image/jpeg', '/uploads/2025/11/06/banner.jpg', '2025-11-06 12:30:12', 'ACTIVE', 'banner.jpg', 'y6u7i8o9p0.jpg', 1, 29);




INSERT INTO goal (
    type, target_value, kcal_per_day, protein_g, fat_g, carbs_g,
    start_date, end_date, created_at, member_id
)
VALUES
    ('WEIGHT', 70.50, NULL, NULL, NULL, NULL, '2025-11-01 00:00:00', '2026-01-01 00:00:00', NOW(), 1),
    ('CALORIE', NULL, 2200, 130, 70, 260, '2025-11-02 00:00:00', '2026-01-15 00:00:00', NOW(), 2),
    ('MACRO', NULL, 2000, 120, 60, 250, '2025-11-03 00:00:00', '2026-02-01 00:00:00', NOW(), 3),
    ('WEIGHT', 65.20, NULL, NULL, NULL, NULL, '2025-11-04 00:00:00', '2026-01-31 00:00:00', NOW(), 4),
    ('CALORIE', NULL, 1800, 100, 50, 200, '2025-11-05 00:00:00', '2026-02-15 00:00:00', NOW(), 5),
    ('MACRO', NULL, 2100, 140, 60, 270, '2025-11-06 00:00:00', '2026-01-10 00:00:00', NOW(), 6),
    ('WEIGHT', 75.00, NULL, NULL, NULL, NULL, '2025-11-07 00:00:00', '2026-02-20 00:00:00', NOW(), 7),
    ('CALORIE', NULL, 2500, 160, 80, 300, '2025-11-08 00:00:00', '2026-03-01 00:00:00', NOW(), 8),
    ('MACRO', NULL, 1900, 110, 50, 220, '2025-11-09 00:00:00', '2026-03-10 00:00:00', NOW(), 9),
    ('WEIGHT', 68.00, NULL, NULL, NULL, NULL, '2025-11-10 00:00:00', '2026-02-01 00:00:00', NOW(), 10),
    ('CALORIE', NULL, 2000, 120, 60, 250, '2025-11-11 00:00:00', '2026-02-20 00:00:00', NOW(), 11),
    ('MACRO', NULL, 2300, 150, 70, 280, '2025-11-12 00:00:00', '2026-03-15 00:00:00', NOW(), 12),
    ('WEIGHT', 72.30, NULL, NULL, NULL, NULL, '2025-11-13 00:00:00', '2026-02-28 00:00:00', NOW(), 13),
    ('CALORIE', NULL, 1900, 110, 50, 220, '2025-11-14 00:00:00', '2026-03-20 00:00:00', NOW(), 14),
    ('MACRO', NULL, 2100, 130, 60, 260, '2025-11-15 00:00:00', '2026-03-25 00:00:00', NOW(), 15),
    ('WEIGHT', 63.00, NULL, NULL, NULL, NULL, '2025-11-16 00:00:00', '2026-03-30 00:00:00', NOW(), 16),
    ('CALORIE', NULL, 2400, 150, 70, 300, '2025-11-17 00:00:00', '2026-04-01 00:00:00', NOW(), 17),
    ('MACRO', NULL, 2000, 125, 55, 250, '2025-11-18 00:00:00', '2026-04-10 00:00:00', NOW(), 18),
    ('WEIGHT', 69.50, NULL, NULL, NULL, NULL, '2025-11-19 00:00:00', '2026-04-15 00:00:00', NOW(), 19),
    ('CALORIE', NULL, 2100, 130, 60, 270, '2025-11-20 00:00:00', '2026-04-20 00:00:00', NOW(), 20);

-- 신고 구분 코드
INSERT INTO report_base (title, count, day_of_ban) VALUES
                                                       ('욕설', 3, 7),
                                                       ('도배', 5, 3),
                                                       ('사기', 1, 30),
                                                       ('음란물', 1, 15),
                                                       ('허위사실', 2, 10),
                                                       ('스팸', 5, 5),
                                                       ('괴롭힘', 2, 14),
                                                       ('기타', 5, 1),
                                                       ('명예훼손', 2, 14),
                                                       ('불법 광고', 3, 10);

-- 신고 데이터
INSERT INTO report (title, contents, yn, date, report_image_url, member_id2, post_id, comment_id, admin_id, report_id, member_id) VALUES
                                                                                                                                      ('욕설 신고', '댓글에서 욕설 사용', TRUE, NOW(), '/img/report1.png', 2, 11, 1001, NULL, 1, 1),
                                                                                                                                      ('도배 신고', '같은 글을 반복 게시', TRUE, NOW(), '/img/report2.png', 3, 12, NULL, NULL, 2, 2),
                                                                                                                                      ('사기 신고', '물품 거래 사기 발생', TRUE, NOW(), '/img/report3.png', 4, NULL, NULL, NULL, 3, 3),
                                                                                                                                      ('음란물 신고', '부적절한 사진 업로드', TRUE, NOW(), '/img/report4.png', 5, 14, NULL, NULL, 4, 4),
                                                                                                                                      ('허위사실 신고', '허위 정보 유포', TRUE, NOW(), '/img/report5.png', 6, 15, NULL, NULL, 5, 5);

-- 신고 파일 업로드
INSERT INTO report_fileupload (report_id, name, type, `rename`, path, thumb_path, upload_order) VALUES
                                                                                                    (1, 'report1.jpg', 'image/jpeg', 'report1_001.jpg', '/upload/report1.jpg', '/upload/thumb_report1.jpg', 1),
                                                                                                    (2, 'report2.jpg', 'image/jpeg', 'report2_001.jpg', '/upload/report2.jpg', '/upload/thumb_report2.jpg', 1),
                                                                                                    (3, 'report3.png', 'image/png', 'report3_001.png', '/upload/report3.png', '/upload/thumb_report3.png', 1);


INSERT INTO ban (
    startDate, endDate, admin_id, member_id, report_no
)
VALUES
    ('2025-11-01 09:00:00', '2025-11-15 09:00:00', 1, 3, 1),
    ('2025-11-02 12:00:00', '2025-11-10 12:00:00', 2, 7, 2),
    ('2025-11-03 08:30:00', '2025-11-17 08:30:00', 3, 9, 3),
    ('2025-11-04 10:15:00', '2025-11-11 10:15:00', 4, 12, 4),
    ('2025-11-05 14:00:00', '2025-11-20 14:00:00', 5, 18, 5);






INSERT INTO member_authority (
    member_id, authories_id
)
VALUES
-- 관리자 권한 (ROLE_ADMIN → id=1)
(1, 1),
(2, 1),

-- 일반 회원 권한 (ROLE_MEMBER → id=2)
(3, 2),
(4, 2),
(5, 2),
(6, 2),
(7, 2),
(8, 2),
(9, 2),
(10, 2),
(11, 2),
(12, 2),
(13, 2),
(14, 2),
(15, 2),
(16, 2),
(17, 2),
(18, 2),
(19, 2),
(20, 2);



INSERT INTO login_failure_history (
    failure_datetime, failure_ip, failure_reasone, member_id
)
VALUES
    ('2025-11-01 08:15:12', '192.168.0.11', '비밀번호 불일치', 1),
    ('2025-11-01 08:17:45', '192.168.0.11', '비밀번호 불일치', 1),
    ('2025-11-01 09:32:10', '192.168.0.23', '존재하지 않는 이메일', 3),
    ('2025-11-01 10:25:44', '192.168.0.25', '비밀번호 5회 연속 오류로 계정 잠금', 3),
    ('2025-11-01 11:12:30', '10.0.0.12', '세션 만료 후 재로그인 실패', 4),
    ('2025-11-02 07:42:00', '192.168.0.30', '비밀번호 불일치', 5),
    ('2025-11-02 08:50:23', '192.168.0.31', '비밀번호 불일치', 5),
    ('2025-11-02 09:03:40', '192.168.0.31', '존재하지 않는 이메일', 6),
    ('2025-11-02 09:45:12', '172.16.0.5', '계정 비활성화 상태', 6),
    ('2025-11-02 10:00:58', '172.16.0.8', '비밀번호 불일치', 7),
    ('2025-11-02 10:12:44', '192.168.1.55', '2단계 인증 실패', 8),
    ('2025-11-02 10:14:31', '192.168.1.55', '비밀번호 불일치', 8),
    ('2025-11-03 08:11:10', '10.0.0.10', '비밀번호 불일치', 9),
    ('2025-11-03 08:13:10', '10.0.0.10', '비밀번호 불일치', 9),
    ('2025-11-03 08:15:10', '10.0.0.10', '비밀번호 불일치', 9),
    ('2025-11-03 09:25:20', '10.0.0.20', '존재하지 않는 이메일', 10),
    ('2025-11-03 10:15:44', '10.0.0.30', '비밀번호 불일치', 10),
    ('2025-11-04 07:30:12', '192.168.10.10', '비밀번호 불일치', 11),
    ('2025-11-04 08:00:45', '192.168.10.11', '비밀번호 불일치', 12),
    ('2025-11-04 08:02:12', '192.168.10.12', '비밀번호 불일치', 12),
    ('2025-11-04 09:11:55', '192.168.10.13', '계정 잠금 해제 전 로그인 시도', 13),
    ('2025-11-05 10:22:18', '10.1.1.1', '비밀번호 불일치', 14),
    ('2025-11-05 10:25:30', '10.1.1.1', '비밀번호 불일치', 14),
    ('2025-11-05 11:03:12', '10.1.1.2', '존재하지 않는 이메일', 15),
    ('2025-11-05 11:22:44', '10.1.1.2', '비밀번호 불일치', 15),
    ('2025-11-05 11:30:18', '10.1.1.3', '세션 만료 후 재로그인 실패', 16),
    ('2025-11-05 11:45:25', '10.1.1.4', '비밀번호 불일치', 17),
    ('2025-11-05 12:00:44', '10.1.1.5', '비밀번호 불일치', 18),
    ('2025-11-05 12:10:30', '10.1.1.6', '비밀번호 불일치', 19),
    ('2025-11-05 12:20:15', '10.1.1.7', '계정 비활성화 상태', 20);



INSERT INTO refresh_token (
    token_hash, jti, issued_at, expires_at, revoked, revoked_at, device_fp, ip, last_used_at, member_id
)
VALUES
    ('a1b2c3d4e5f6g7h8i9j0aabbccddeeff00112233445566778899aabbccddeeff', 'JTI001', '2025-11-01 08:00:00', '2025-12-01 08:00:00', 0, NULL, 'FP-001', '192.168.0.11', '2025-11-01 09:00:00', 1),
    ('b2c3d4e5f6g7h8i9j0aabbccddeeff00112233445566778899aabbccddeeffaa', 'JTI002', '2025-11-01 09:00:00', '2025-12-01 09:00:00', 0, NULL, 'FP-002', '192.168.0.12', '2025-11-01 09:10:00', 2),
    ('c3d4e5f6g7h8i9j0aabbccddeeff00112233445566778899aabbccddeeffaabb', 'JTI003', '2025-11-01 10:00:00', '2025-12-01 10:00:00', 0, NULL, 'FP-003', '192.168.0.13', '2025-11-01 10:05:00', 3),
    ('d4e5f6g7h8i9j0aabbccddeeff00112233445566778899aabbccddeeffaabbcc', 'JTI004', '2025-11-01 10:15:00', '2025-12-01 10:15:00', 0, NULL, 'FP-004', '192.168.0.14', '2025-11-01 10:30:00', 4),
    ('e5f6g7h8i9j0aabbccddeeff00112233445566778899aabbccddeeffaabbccdd', 'JTI005', '2025-11-01 11:00:00', '2025-12-01 11:00:00', 0, NULL, 'FP-005', '192.168.0.15', '2025-11-01 11:10:00', 5),
    ('f6g7h8i9j0aabbccddeeff00112233445566778899aabbccddeeffaabbccdde0', 'JTI006', '2025-11-01 12:00:00', '2025-12-01 12:00:00', 1, '2025-11-15 12:00:00', 'FP-006', '192.168.0.16', '2025-11-01 12:15:00', 6),
    ('g7h8i9j0aabbccddeeff00112233445566778899aabbccddeeffaabbccdde011', 'JTI007', '2025-11-01 12:30:00', '2025-12-01 12:30:00', 0, NULL, 'FP-007', '192.168.0.17', '2025-11-01 12:35:00', 7),
    ('h8i9j0aabbccddeeff00112233445566778899aabbccddeeffaabbccdde01122', 'JTI008', '2025-11-01 13:00:00', '2025-12-01 13:00:00', 0, NULL, 'FP-008', '192.168.0.18', '2025-11-01 13:10:00', 8),
    ('i9j0aabbccddeeff00112233445566778899aabbccddeeffaabbccdde0112233', 'JTI009', '2025-11-01 13:30:00', '2025-12-01 13:30:00', 0, NULL, 'FP-009', '192.168.0.19', '2025-11-01 13:35:00', 9),
    ('j0aabbccddeeff00112233445566778899aabbccddeeffaabbccdde011223344', 'JTI010', '2025-11-01 14:00:00', '2025-12-01 14:00:00', 0, NULL, 'FP-010', '192.168.0.20', '2025-11-01 14:05:00', 10),
    ('aabbccddeeff00112233445566778899aabbccddeeffaabbccdde01122334455', 'JTI011', '2025-11-01 15:00:00', '2025-12-01 15:00:00', 0, NULL, 'FP-011', '192.168.0.21', '2025-11-01 15:10:00', 11),
    ('bbccddeeff00112233445566778899aabbccddeeffaabbccdde0112233445566', 'JTI012', '2025-11-01 15:30:00', '2025-12-01 15:30:00', 0, NULL, 'FP-012', '192.168.0.22', '2025-11-01 15:35:00', 12),
    ('ccddeeff00112233445566778899aabbccddeeffaabbccdde011223344556677', 'JTI013', '2025-11-01 16:00:00', '2025-12-01 16:00:00', 0, NULL, 'FP-013', '192.168.0.23', '2025-11-01 16:05:00', 13),
    ('ddeeff00112233445566778899aabbccddeeffaabbccdde01122334455667788', 'JTI014', '2025-11-01 16:30:00', '2025-12-01 16:30:00', 0, NULL, 'FP-014', '192.168.0.24', '2025-11-01 16:35:00', 14),
    ('eeff00112233445566778899aabbccddeeffaabbccdde0112233445566778899', 'JTI015', '2025-11-01 17:00:00', '2025-12-01 17:00:00', 0, NULL, 'FP-015', '192.168.0.25', '2025-11-01 17:05:00', 15),
    ('ff00112233445566778899aabbccddeeffaabbccdde011223344556677889900', 'JTI016', '2025-11-01 17:30:00', '2025-12-01 17:30:00', 1, '2025-11-10 17:30:00', 'FP-016', '192.168.0.26', '2025-11-01 17:40:00', 16),
    ('00112233445566778899aabbccddeeffaabbccdde01122334455667788990011', 'JTI017', '2025-11-01 18:00:00', '2025-12-01 18:00:00', 0, NULL, 'FP-017', '192.168.0.27', '2025-11-01 18:10:00', 17),
    ('112233445566778899aabbccddeeffaabbccdde0112233445566778899001122', 'JTI018', '2025-11-01 18:30:00', '2025-12-01 18:30:00', 0, NULL, 'FP-018', '192.168.0.28', '2025-11-01 18:35:00', 18),
    ('2233445566778899aabbccddeeffaabbccdde011223344556677889900112233', 'JTI019', '2025-11-01 19:00:00', '2025-12-01 19:00:00', 0, NULL, 'FP-019', '192.168.0.29', '2025-11-01 19:05:00', 19),
    ('33445566778899aabbccddeeffaabbccdde01122334455667788990011223344', 'JTI020', '2025-11-01 19:30:00', '2025-12-01 19:30:00', 0, NULL, 'FP-020', '192.168.0.30', '2025-11-01 19:40:00', 20);


INSERT INTO login_history (
    login_date, come_in_ip, before_path, member_id
)
VALUES
    ('2025-11-01 08:10:00', '192.168.0.11', '/login', 1),
    ('2025-11-01 09:00:00', '192.168.0.12', '/main', 2),
    ('2025-11-01 09:30:00', '192.168.0.13', '/profile', 3),
    ('2025-11-01 10:15:00', '192.168.0.14', '/home', 4),
    ('2025-11-01 10:45:00', '192.168.0.15', '/login', 5),
    ('2025-11-01 11:10:00', '192.168.0.16', '/dashboard', 6),
    ('2025-11-01 11:40:00', '192.168.0.17', '/profile', 7),
    ('2025-11-01 12:05:00', '192.168.0.18', '/main', 8),
    ('2025-11-01 12:25:00', '192.168.0.19', '/home', 9),
    ('2025-11-01 13:00:00', '192.168.0.20', '/login', 10),
    ('2025-11-01 13:15:00', '192.168.0.21', '/profile', 11),
    ('2025-11-01 13:45:00', '192.168.0.22', '/main', 12),
    ('2025-11-01 14:00:00', '192.168.0.23', '/home', 13),
    ('2025-11-01 14:30:00', '192.168.0.24', '/dashboard', 14),
    ('2025-11-01 15:00:00', '192.168.0.25', '/login', 15),
    ('2025-11-01 15:30:00', '192.168.0.26', '/profile', 16),
    ('2025-11-01 16:00:00', '192.168.0.27', '/main', 17),
    ('2025-11-01 16:20:00', '192.168.0.28', '/home', 18),
    ('2025-11-01 16:45:00', '192.168.0.29', '/dashboard', 19),
    ('2025-11-01 17:10:00', '192.168.0.30', '/login', 20);




-- ----------------------------
-- 1. food (음식 정보) 더미 데이터
-- ----------------------------
INSERT INTO food (name, gram, kcal, carbo, protein, fat, sodium) VALUES
                                                                     ('닭가슴살', 100, 165.00, 0.00, 31.00, 3.60, 74.00),
                                                                     ('쌀밥', 210, 310.00, 68.00, 5.00, 1.00, 10.00),
                                                                     ('삶은 계란', 50, 77.00, 0.60, 6.30, 5.30, 62.00),
                                                                     ('바나나', 100, 93.00, 24.00, 1.00, 0.30, 1.00),
                                                                     ('아몬드', 30, 180.00, 6.00, 6.00, 15.00, 1.00);

-- ----------------------------
-- 2. meal (회원이 기록한 식사) 더미 데이터
-- ----------------------------
INSERT INTO meal (type, date, member_id) VALUES
                                             ('BREAKFAST', '2025-11-05', 1),
                                             ('LUNCH', '2025-11-05', 1),
                                             ('DINNER', '2025-11-05', 1),
                                             ('SNACK', '2025-11-05', 1),
                                             ('BREAKFAST', '2025-11-06', 1);

-- ----------------------------
-- 3. ai_diet (AI 추천 식단) 더미 데이터
-- ----------------------------
INSERT INTO ai_diet (type, total_kcal, kcal, total_protein, total_fat, name, member_id) VALUES
                                                                                            ('BREAKFAST', 450.50, 450.50, 30.5, 10.0, '든든한 아침 세트 (AI)', 1),
                                                                                            ('LUNCH', 620.00, 620.00, 40.0, 15.5, '고단백 점심 (AI)', 1),
                                                                                            ('DINNER', 510.00, 510.00, 35.0, 12.0, '클린 저녁 식단 (AI)', 1),
                                                                                            ('SNACK', 180.00, 180.00, 6.0, 15.0, '오후 간식 (AI)', 1),
                                                                                            ('BREAKFAST', 480.00, 480.00, 33.0, 11.0, '활기찬 아침 (AI)', 2);

-- ----------------------------
-- 4. meal_food (매핑 테이블) 더미 데이터
-- (meal ID 1~5와 food ID 1~5를 조합)
-- ----------------------------
INSERT INTO meal_food (meal_id, food_id) VALUES
                                             (1, 3), -- 1번 아침(BREAKFAST)에 3번 음식(삶은 계란)
                                             (1, 4), -- 1번 아침(BREAKFAST)에 4번 음식(바나나)
                                             (2, 1), -- 2번 점심(LUNCH)에 1번 음식(닭가슴살)
                                             (2, 2), -- 2번 점심(LUNCH)에 2번 음식(쌀밥)
                                             (3, 1); -- 3번 저녁(DINNER)에 1번 음식(닭가슴살)

-- ----------------------------
-- 5. food_fileupload (업로드된 식사 사진) 더미 데이터
-- (meal ID 1~5에 연결)
-- ----------------------------
INSERT INTO food_fileupload (meal_id, name, type, re_name, path, upload_order, thumb_path, extend_file_path_id) VALUES
                                                                                                                    (1, 'breakfast_photo.jpg', 'image/jpeg', 'uuid-fake-001.jpg', '/uploads/2025/11/uuid-fake-001.jpg', 1, '/uploads/thumbs/thumb_001.jpg', 1),
                                                                                                                    (2, 'my_lunch.png', 'image/png', 'uuid-fake-002.png', '/uploads/2025/11/uuid-fake-002.png', 1, '/uploads/thumbs/thumb_002.png', 1),
                                                                                                                    (2, 'side_dish.jpg', 'image/jpeg', 'uuid-fake-003.jpg', '/uploads/2025/11/uuid-fake-003.jpg', 2, '/uploads/thumbs/thumb_003.jpg', 1),
                                                                                                                    (3, 'dinner_salad.jpg', 'image/jpeg', 'uuid-fake-004.jpg', '/uploads/2025/11/uuid-fake-004.jpg', 1, '/uploads/thumbs/thumb_004.jpg', 1),
                                                                                                                    (5, 'morning_meal.jpg', 'image/jpeg', 'uuid-fake-005.jpg', '/uploads/2025/11/uuid-fake-005.jpg', 1, '/uploads/thumbs/thumb_005.jpg', 1);

-- 운동 데이터
INSERT INTO exercise (member_id, date, type, category, min, burned_kcal) VALUES
                                                                             (1, '2025-11-01', '조깅', '유산소', 30, 210),
                                                                             (1, '2025-11-02', '자전거 타기', '유산소', 45, 340),
                                                                             (1, '2025-11-03', '스쿼트', '무산소', 20, 160),
                                                                             (1, '2025-11-04', '요가', '스트레칭', 40, 120),
                                                                             (2, '2025-11-01', '수영', '유산소', 60, 430),
                                                                             (2, '2025-11-02', '팔굽혀펴기', '무산소', 15, 90),
                                                                             (2, '2025-11-03', '줄넘기', '유산소', 25, 210),
                                                                             (2, '2025-11-04', '복근운동', '무산소', 30, 180),
                                                                             (3, '2025-11-01', '런닝머신', '유산소', 35, 260),
                                                                             (3, '2025-11-02', '등운동', '무산소', 25, 190),
                                                                             (3, '2025-11-03', '요가', '스트레칭', 30, 100),
                                                                             (3, '2025-11-04', '사이클', '유산소', 40, 300),
                                                                             (4, '2025-11-01', '스트레칭', '스트레칭', 15, 50),
                                                                             (4, '2025-11-02', '플랭크', '무산소', 10, 70),
                                                                             (4, '2025-11-03', '필라테스', '스트레칭', 40, 140),
                                                                             (4, '2025-11-04', '걷기', '유산소', 50, 180),
                                                                             (5, '2025-11-01', '조깅', '유산소', 20, 150),
                                                                             (5, '2025-11-02', '사이클', '유산소', 30, 240),
                                                                             (5, '2025-11-03', '복근운동', '무산소', 25, 160),
                                                                             (5, '2025-11-04', '요가', '스트레칭', 30, 100),
                                                                             (6, '2025-11-01', '벤치프레스', '무산소', 20, 190),
                                                                             (6, '2025-11-02', '사이클', '유산소', 40, 290),
                                                                             (6, '2025-11-03', '달리기', '유산소', 35, 260),
                                                                             (7, '2025-11-01', '스쿼트', '무산소', 15, 130),
                                                                             (7, '2025-11-02', '줄넘기', '유산소', 20, 200),
                                                                             (7, '2025-11-03', '걷기', '유산소', 30, 130),
                                                                             (8, '2025-11-01', '자전거 타기', '유산소', 40, 330),
                                                                             (8, '2025-11-02', '플랭크', '무산소', 15, 90),
                                                                             (8, '2025-11-03', '스트레칭', '스트레칭', 20, 60),
                                                                             (8, '2025-11-04', '팔굽혀펴기', '무산소', 15, 80);

-- 운동 파일 업로드
INSERT INTO exercise_fileupload (exercise_id, name, type, re_name, path, thumb_path, upload_order) VALUES
                                                                                                       (1, 'jogging1.jpg', 'image/jpeg', 'jogging_001.jpg', '/uploads/exercise/jogging_001.jpg', '/uploads/exercise/thumb_jogging_001.jpg', 1),
                                                                                                       (2, 'bike1.jpg', 'image/jpeg', 'bike_001.jpg', '/uploads/exercise/bike_001.jpg', '/uploads/exercise/thumb_bike_001.jpg', 1),
                                                                                                       (3, 'squat1.jpg', 'image/jpeg', 'squat_001.jpg', '/uploads/exercise/squat_001.jpg', '/uploads/exercise/thumb_squat_001.jpg', 1),
                                                                                                       (4, 'yoga1.jpg', 'image/jpeg', 'yoga_001.jpg', '/uploads/exercise/yoga_001.jpg', '/uploads/exercise/thumb_yoga_001.jpg', 1);


-- 블랙리스트
INSERT INTO black_list (member_id, create_date, reason, admin_id) VALUES
                                                                      (1, NOW(), '욕설 3회 누적', 10),
                                                                      (3, NOW(), '사기 행위 적발', 10);


INSERT INTO `diary` (`day`, `weight`, `mood`, `condition`, `memo`, `member_id`) VALUES
                                                                                    ('2025-11-01', 70, '좋음', '컨디션 양호', '오늘은 아침 일찍 일어나서 산책을 다녀왔다. 공기가 차가웠지만 상쾌해서 하루를 기분 좋게 시작할 수 있었다. 점심에는 가벼운 샐러드를 먹고 오후에는 책을 읽으며 여유로운 시간을 보냈다.', 1),

                                                                                    ('2025-11-02', 69, '보통', '피곤함', '주말이라 늦잠을 잤다. 전날 늦게까지 영화를 봐서인지 하루 종일 조금 피곤했다. 오후에는 커피를 마시며 잠을 쫓았고, 저녁에는 간단히 파스타를 해먹었다. 특별한 일은 없지만 평범한 하루였다.', 1),

                                                                                    ('2025-11-03', 68, '아주좋음', '최상', '출근길에 하늘이 유난히 맑았다. 일도 잘 풀리고 팀원들과의 회의도 순조로웠다. 저녁에는 오랜만에 친구를 만나 즐겁게 수다를 떨었다. 모든 게 잘 흘러가는 하루라 행복하다.', 2),

                                                                                    ('2025-11-04', 70, '나쁨', '두통', '아침부터 머리가 아팠다. 잠을 충분히 못 잔 탓인 것 같다. 커피를 마셔도 별로 나아지지 않았다. 일을 하다가 집중이 잘 안 돼서 조퇴하고 집에 돌아와 푹 쉬었다.', 3),

                                                                                    ('2025-11-05', 71, '보통', '보통', '오늘은 특별한 감정 없이 그냥 평범한 하루였다. 회사에서 일하고 점심에는 동료들과 식당에 다녀왔다. 저녁에는 집에서 드라마를 보며 시간을 보냈다.', 2),

                                                                                    ('2025-11-06', 69, '좋음', '활기참', '아침에 일어나자마자 운동을 했다. 땀을 흘리니 기분이 상쾌했다. 점심엔 샐러드와 닭가슴살을 먹었고, 오후에는 프로젝트 진행 상황을 점검했다. 하루를 잘 마무리했다.', 4),

                                                                                    ('2025-11-07', 70, '아주좋음', '매우 좋음', '오늘은 기다리던 여행을 다녀왔다. 바다를 보며 걷는 동안 마음이 한결 편안해졌다. 날씨도 완벽했고, 사진도 많이 찍었다. 행복한 기억으로 남을 하루였다.', 5),

                                                                                    ('2025-11-08', 68, '보통', '괜찮음', '주말이라 집에서 푹 쉬었다. 밀린 빨래를 하고, 방 청소도 했다. 저녁에는 따뜻한 차를 마시며 독서를 했다. 조용하지만 만족스러운 하루였다.', 3),

                                                                                    ('2025-11-09', 67, '좋음', '건강함', '오늘은 간헐적 단식을 유지하며 가벼운 요가를 했다. 몸이 훨씬 가벼워진 느낌이다. 저녁에는 가족들과 함께 식사하면서 이런저런 이야기를 나눴다.', 6),

                                                                                    ('2025-11-10', 70, '아주나쁨', '매우 피곤', '야근이 길어져서 집에 돌아온 게 새벽이었다. 피곤해서 저녁도 거르고 바로 잠들었다. 몸이 무겁고 정신도 흐릿하다. 내일은 꼭 일찍 퇴근해야겠다.', 1);


INSERT INTO `diary_file` (`mime`, `path`, `created_at`, `state`, `original_file`, `rename`, `diary_id`, `extend_file_path_id`) VALUES
                                                                                                                                   ('image/png', '/uploads/diary/1.png', '2025-11-01 10:00:00', 'active', '1.png', 1001, 1, 1),
                                                                                                                                   ('image/jpg', '/uploads/diary/2.jpg', '2025-11-02 09:00:00', 'active', '2.jpg', 1002, 2, 1),
                                                                                                                                   ('image/png', '/uploads/diary/3.png', '2025-11-03 08:30:00', 'active', '3.png', 1003, 3, 2),
                                                                                                                                   ('image/jpg', '/uploads/diary/4.jpg', '2025-11-04 11:00:00', 'inactive', '4.jpg', 1004, 4, 3),
                                                                                                                                   ('image/png', '/uploads/diary/5.png', '2025-11-05 10:30:00', 'active', '5.png', 1005, 5, 2),
                                                                                                                                   ('image/jpg', '/uploads/diary/6.jpg', '2025-11-06 09:10:00', 'deleted', '6.jpg', 1006, 6, 3),
                                                                                                                                   ('image/png', '/uploads/diary/7.png', '2025-11-07 13:00:00', 'active', '7.png', 1007, 7, 1),
                                                                                                                                   ('image/png', '/uploads/diary/8.png', '2025-11-08 15:45:00', 'active', '8.png', 1008, 8, 1),
                                                                                                                                   ('image/jpg', '/uploads/diary/9.jpg', '2025-11-09 09:25:00', 'active', '9.jpg', 1009, 9, 3),
                                                                                                                                   ('image/png', '/uploads/diary/10.png', '2025-11-10 22:15:00', 'active', '10.png', 1010, 10, 2);


INSERT INTO `qna` (`title`, `contents`, `created_at`, `member_id`) VALUES
                                                                       ('운동 루틴 추천', '체지방 감량에 좋은 루틴이 있을까요?', '2025-11-01 10:00:00', 1),
                                                                       ('단백질 섭취량', '운동 후 단백질 섭취량은 어느 정도가 적당한가요?', '2025-11-02 09:00:00', 2),
                                                                       ('다이어트 정체기', '체중이 줄지 않을 때 어떻게 해야 할까요?', '2025-11-03 08:30:00', 3),
                                                                       ('헬스장 추천', '서울 강남 근처 좋은 헬스장 추천 부탁드려요.', '2025-11-04 12:00:00', 4),
                                                                       ('아침 식사 중요성', '아침을 꼭 먹어야 하나요?', '2025-11-05 07:45:00', 5),
                                                                       ('근육통 완화', '운동 후 근육통 줄이는 법 알려주세요.', '2025-11-06 11:10:00', 2),
                                                                       ('유산소 시간', '하루 유산소는 몇 분이 좋을까요?', '2025-11-07 14:20:00', 3),
                                                                       ('체중계 정확도', '체중계가 자꾸 다르게 나와요.', '2025-11-08 15:00:00', 6),
                                                                       ('식단 관리 앱 추천', '좋은 식단 관리 앱 있을까요?', '2025-11-09 09:45:00', 4),
                                                                       ('수면과 다이어트', '수면 부족이 체중 감량에 영향이 있나요?', '2025-11-10 23:30:00', 5);


INSERT INTO `qna_comment` (`comment`, `created_at`, `qna_id`, `member_id`, `parent_comment_id`) VALUES
                                                                                                    ('좋은 질문이에요! 저도 궁금했어요.', '2025-11-01 11:00:00', 1, 2, NULL),
                                                                                                    ('단백질은 체중×1.6g 정도 추천드려요.', '2025-11-02 10:00:00', 2, 3, NULL),
                                                                                                    ('저는 아침을 꼭 챙겨먹어요!', '2025-11-05 08:00:00', 5, 4, NULL),
                                                                                                    ('운동 후 스트레칭 꼭 하세요.', '2025-11-06 12:00:00', 6, 5, NULL),
                                                                                                    ('체중계 브랜드마다 조금 달라요.', '2025-11-08 15:30:00', 8, 1, NULL),
                                                                                                    ('정체기면 식단을 바꿔보세요.', '2025-11-03 09:00:00', 3, 2, NULL),
                                                                                                    ('좋은 헬스장 많아요! PM헬스 추천', '2025-11-04 13:00:00', 4, 3, NULL),
                                                                                                    ('저도 같은 고민이에요 ㅠㅠ', '2025-11-09 10:00:00', 9, 4, NULL),
                                                                                                    ('잠 부족하면 코르티솔 올라갑니다.', '2025-11-10 23:45:00', 10, 5, NULL),
                                                                                                    ('위 댓글에 동의합니다!', '2025-11-02 10:30:00', 2, 6, 2);


INSERT INTO `calender` (`cal_day`, `badge_count`, `exercise_status`, `meal_status`, `diary_status`, `member_id`) VALUES
                                                                                                                     ('2025-11-01', 3, 1, 1, 1, 1),
                                                                                                                     ('2025-11-02', 2, 0, 1, 1, 1),
                                                                                                                     ('2025-11-03', 4, 1, 1, 1, 2),
                                                                                                                     ('2025-11-04', 1, 0, 1, 0, 3),
                                                                                                                     ('2025-11-05', 3, 1, 1, 1, 4),
                                                                                                                     ('2025-11-06', 2, 1, 0, 1, 2),
                                                                                                                     ('2025-11-07', 5, 1, 1, 1, 5),
                                                                                                                     ('2025-11-08', 1, 0, 0, 1, 3),
                                                                                                                     ('2025-11-09', 3, 1, 1, 0, 6),
                                                                                                                     ('2025-11-10', 4, 1, 1, 1, 1);



/* 2) 가챠 리셋 정책 */
INSERT INTO gacha_reset (id, name, use_point) VALUES
                                                  (1, '1등 뽑히면 리셋', 100),
                                                  (2, '모든 경품 소진 시 리셋', 150),
                                                  (3, '기간 만료 시 리셋', 50);

/* 3) 가챠 이벤트 */
INSERT INTO gacha_event (id, start_at, point, end_at, status, created_at, event_id2) VALUES
    (1, '2025-11-01 00:00:00', 10, '2025-11-30 23:59:59', 'ACTIVE', NOW(), 1);

/* 4) 경품 */
INSERT INTO gacha_prize (id, name, payload_json, prize_type, rank, created_at, quantity, gacha_event_id) VALUES
                                                                                                             (1, '1등-1000포인트', JSON_OBJECT('point',1000), 'POINT', 1, NOW(), 1, 1),
                                                                                                             (2, '2등-쿠폰A',      JSON_OBJECT('coupon','A-COUPON'), 'COUPON', 2, NOW(), 5, 1),
                                                                                                             (3, '꽝',             JSON_OBJECT('result','nothing'), 'NOTHING', 3, NOW(), 999, 1);

/* 5) 경품 재고 */
INSERT INTO gacha_quantity (id, count) VALUES
                                           (1, 1), (2, 5), (3, 999);

/* 6) 빙고 보드 */
INSERT INTO bingo_board (id, title, size, start_date, end_date, created_at, member_id) VALUES
    (1, '11월 건강 빙고', 5, '2025-11-01', '2025-11-30', NOW(), 1);

/* 7) 빙고 칸 5x5 (25개) */
INSERT INTO bingo_cell (id, `row`, `col`, label, is_checked, checked_at, bingo_board_id) VALUES
-- row1(전부 체크)
(1,1,1,'물 2L 마시기',1,NOW(),1),(2,1,2,'스트레칭 10분',1,NOW(),1),(3,1,3,'만보 걷기',1,NOW(),1),
(4,1,4,'야식 금지',1,NOW(),1),(5,1,5,'야채 5종',1,NOW(),1),
-- row2
(6,2,1,'조깅 20분',0,NULL,1),(7,2,2,'디지털 디톡스',0,NULL,1),(8,2,3,'비타민',0,NULL,1),
(9,2,4,'저염식',0,NULL,1),(10,2,5,'커피 1잔 이하',0,NULL,1),
-- row3
(11,3,1,'금주 하루',0,NULL,1),(12,3,2,'8시간 수면',0,NULL,1),(13,3,3,'샐러드',0,NULL,1),
(14,3,4,'계단 이용',0,NULL,1),(15,3,5,'간식 줄이기',0,NULL,1),
-- row4
(16,4,1,'물 알람',0,NULL,1),(17,4,2,'정리정돈',0,NULL,1),(18,4,3,'가벼운 스트레칭',0,NULL,1),
(19,4,4,'소식',0,NULL,1),(20,4,5,'비타민C',0,NULL,1),
-- row5
(21,5,1,'물 1L+',0,NULL,1),(22,5,2,'공원 산책',0,NULL,1),(23,5,3,'감사일기',0,NULL,1),
(24,5,4,'명상 10분',0,NULL,1),(25,5,5,'긍정적 하루',0,NULL,1);

/* 8) 빙고 파일 (셀을 참조) */
INSERT INTO bingo_fileupload
(id, name, mime_type, re_name, `path`, created_at, bingo_cell_id, extend_file_path_id) VALUES
                                                                                           (1,'bingo_img1.png','image/png','20251105_1.png','/upload/bingo/',NOW(),1,1),
                                                                                           (2,'bingo_img2.png','image/png','20251105_2.png','/upload/bingo/',NOW(),2,1);

/* 9) 가챠 보드 칸 (샘플 20칸: 1~10 OPENED, 11~20 COVERED) */
INSERT INTO gacha_board_cell
(id, status, opened_at, `rows`, `cols`, opened_count, created_at, updated_at, gacha_prize_id, gacha_event_id, member_id)
VALUES
    (1,'OPENED',NOW(),1,1,1,NOW(),NOW(),1,1,1),
    (2,'OPENED',NOW(),1,2,1,NOW(),NOW(),2,1,1),
    (3,'OPENED',NOW(),1,3,1,NOW(),NOW(),3,1,1),
    (4,'OPENED',NOW(),1,4,1,NOW(),NOW(),1,1,1),
    (5,'OPENED',NOW(),1,5,1,NOW(),NOW(),2,1,1),
    (6,'OPENED',NOW(),1,6,1,NOW(),NOW(),3,1,1),
    (7,'OPENED',NOW(),1,7,1,NOW(),NOW(),1,1,1),
    (8,'OPENED',NOW(),1,8,1,NOW(),NOW(),2,1,1),
    (9,'OPENED',NOW(),1,9,1,NOW(),NOW(),3,1,1),
    (10,'OPENED',NOW(),1,10,1,NOW(),NOW(),1,1,1),
    (11,'COVERED',NULL,2,1,0,NOW(),NULL,1,1,1),
    (12,'COVERED',NULL,2,2,0,NOW(),NULL,2,1,1),
    (13,'COVERED',NULL,2,3,0,NOW(),NULL,3,1,1),
    (14,'COVERED',NULL,2,4,0,NOW(),NULL,1,1,1),
    (15,'COVERED',NULL,2,5,0,NOW(),NULL,2,1,1),
    (16,'COVERED',NULL,2,6,0,NOW(),NULL,3,1,1),
    (17,'COVERED',NULL,2,7,0,NOW(),NULL,1,1,1),
    (18,'COVERED',NULL,2,8,0,NOW(),NULL,2,1,1),
    (19,'COVERED',NULL,2,9,0,NOW(),NULL,3,1,1),
    (20,'COVERED',NULL,2,10,0,NOW(),NULL,1,1,1);

/* 10) 뽑기 실행 로그 */
-- (gacha_draw_log 테이블이 없는 스크립트 버전이면 이 블록은 건너뛰세요)

/* 11) 지급 로그 */
INSERT INTO gacha_reward_grant (id, grant_status, granted_at, created_at, gacha_board_cell_id) VALUES
                                                                                                   (1,'GRANTED',NOW(),NOW(),1),
                                                                                                   (2,'QUEUED',NULL,NOW(),2);

/* 12) 가챠 파일 (이벤트 참조) */
INSERT INTO gacha_fileupload
(id, extend_file_path_id, gacha_event_id, name, mime_type, re_name, url, create_at)
VALUES
    (3, 1, 1, 'event_banner.png', 'image/png', 'banner_20251105.png', '/upload/bingo/20251105_1.png', NOW());

/* 14) 포인트 내역 */
INSERT INTO point (point_id, point, distinction, member_id, diary_id, calender_id, gacha_event_id, bingo_board_id) VALUES
                                                                                                                       (1, 100, 'EARN', 1, 1, 1, 1, 1),
                                                                                                                       (2,  50,  'USE', 1, 1, 1, 1, 1);
-- ----------------------------
-- 1. allergy (알러지 마스터 목록) 더미 데이터
-- (ID가 1, 2, 3으로 자동 생성됨)
-- ----------------------------
INSERT INTO allergy (name) VALUES
                               ('땅콩'),
                               ('우유'),
                               ('갑각류');

-- ----------------------------
-- 2. member_allergy (회원의 알러지 정보) 더미 데이터
-- ----------------------------
INSERT INTO member_allergy (member_id, allergy_id) VALUES
                                                       (1, 1), -- 1번 회원: '땅콩' 알러지
                                                       (1, 2), -- 1번 회원: '우유' 알러지
                                                       (2, 3); -- 2번 회원: '갑각류' 알러지

-- ----------------------------
-- 3. food_allergy (음식(식사)의 알러지 유발 정보) 더미 데이터
-- ----------------------------
INSERT INTO food_allergy (meal_id, allergy_id) VALUES
                                                   (1, 1), -- 1번 식사: '땅콩' 포함
                                                   (2, 2), -- 2번 식사: '우유' 포함
                                                   (3, 3); -- 3번 식사: '갑각류' 포함