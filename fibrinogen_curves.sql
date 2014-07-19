DROP TABLE IF EXISTS "calibrations";
CREATE TABLE "calibrations" ("calibration_id_pk" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "name" TEXT);
INSERT INTO "calibrations" VALUES(1,'test1');
INSERT INTO "calibrations" VALUES(4,'third');
DROP TABLE IF EXISTS "learned_functions";
CREATE TABLE "learned_functions" ("lf_id_pk" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "theta0" DOUBLE, "theta1" DOUBLE, "theta2" DOUBLE, "theta3" DOUBLE, "calibration_id_fk" INTEGER);
DROP TABLE IF EXISTS "measurements";
CREATE TABLE "measurements" ("measurements_id_pk" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "time" DOUBLE, "fibrinogen" DOUBLE, "calibration_id_fk" INTEGER);
