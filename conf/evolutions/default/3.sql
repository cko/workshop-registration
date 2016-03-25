# --- !Ups

INSERT INTO "users" VALUES('8c4ffe26-9c9a-488a-9c57-77794d7edc24','firstName','lastName','test@example.com');
INSERT INTO "logininfo" VALUES(1,'credentials','test@example.com');
INSERT INTO "userlogininfo" VALUES('8c4ffe26-9c9a-488a-9c57-77794d7edc24',1);
INSERT INTO "passwordinfo" VALUES('bcrypt','$2a$10$qB2wm8pYsAq5rEzzvZ8agO4HPnit1SzrcXBEgltR858ucgx4q0aIW',NULL,1);

# --- !Downs
