DROP TABLE IF EXISTS "calibrations";
CREATE TABLE "calibrations" ("calibration_id_pk" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "name" TEXT);
INSERT INTO "calibrations" VALUES(1,'test1');
INSERT INTO "calibrations" VALUES(2,'test2');
INSERT INTO "calibrations" VALUES(7,'asdf');
DROP TABLE IF EXISTS "learned_functions";
CREATE TABLE "learned_functions" ("lf_id_pk" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "theta0" DOUBLE, "theta1" DOUBLE, "theta2" DOUBLE, "theta3" DOUBLE, "calibration_id_fk" INTEGER, "mean1" DOUBLE, "mean2" DOUBLE, "mean3" DOUBLE, "range1" DOUBLE, "range2" DOUBLE, "range3" DOUBLE);
INSERT INTO "learned_functions" VALUES(50,1.0033333333333336,3.4621153583038526,-4.238709925229274,2.4841770780489503,1,0.8666666666666667,1.2666666666666666,2.136,2,4,8);
INSERT INTO "learned_functions" VALUES(54,1,1.85714285714285,0.42857142857143193,-0.2857142857142822,7,1,1.6666666666666667,3,2,4,8);
INSERT INTO "learned_functions" VALUES(58,2.5,4.7359741449326505,0.6898622066005506,-0.447759218195501,2,2.5,9.166666666666666,37.5,5,25,125);
DROP TABLE IF EXISTS "learned_points";
CREATE TABLE "learned_points" ("lp_id_pk" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "x" DOUBLE, "y" DOUBLE, "calibration_id_fk" INTEGER);
INSERT INTO "learned_points" VALUES(221,0,0.18206620788519756,1);
INSERT INTO "learned_points" VALUES(222,0.1111111111111111,0.36174946826146137,1);
INSERT INTO "learned_points" VALUES(223,0.2222222222222222,0.5178235902906814,1);
INSERT INTO "learned_points" VALUES(224,0.3333333333333333,0.6528443117074754,1);
INSERT INTO "learned_points" VALUES(225,0.4444444444444444,0.7693673702464622,1);
INSERT INTO "learned_points" VALUES(226,0.5555555555555556,0.8699485036422596,1);
INSERT INTO "learned_points" VALUES(227,0.6666666666666666,0.9571434496294864,1);
INSERT INTO "learned_points" VALUES(228,0.7777777777777777,1.0335079459427607,1);
INSERT INTO "learned_points" VALUES(229,0.8888888888888888,1.1015977303167006,1);
INSERT INTO "learned_points" VALUES(230,1,1.163968540485924,1);
INSERT INTO "learned_points" VALUES(231,1.1111111111111112,1.2231761141850503,1);
INSERT INTO "learned_points" VALUES(232,1.222222222222222,1.2817761891486965,1);
INSERT INTO "learned_points" VALUES(233,1.3333333333333333,1.3423245031114817,1);
INSERT INTO "learned_points" VALUES(234,1.4444444444444444,1.4073767938080233,1);
INSERT INTO "learned_points" VALUES(235,1.5555555555555554,1.4794887989729406,1);
INSERT INTO "learned_points" VALUES(236,1.6666666666666663,1.5612162563408511,1);
INSERT INTO "learned_points" VALUES(237,1.7777777777777777,1.6551149036463737,1);
INSERT INTO "learned_points" VALUES(238,1.8888888888888888,1.763740478624126,1);
INSERT INTO "learned_points" VALUES(239,2,1.889648719008727,1);
INSERT INTO "learned_points" VALUES(240,2.111111111111111,2.0353953625347936,1);
INSERT INTO "learned_points" VALUES(301,0,8.881784197001252e-16,7);
INSERT INTO "learned_points" VALUES(302,0.1111111111111111,0.10444836370762352,7);
INSERT INTO "learned_points" VALUES(303,0.2222222222222222,0.2112482853223595,7);
INSERT INTO "learned_points" VALUES(304,0.3333333333333333,0.3201058201058198,7);
INSERT INTO "learned_points" VALUES(305,0.4444444444444444,0.4307270233196153,7);
INSERT INTO "learned_points" VALUES(306,0.5555555555555556,0.542817950225357,7);
INSERT INTO "learned_points" VALUES(307,0.6666666666666666,0.6560846560846549,7);
INSERT INTO "learned_points" VALUES(308,0.7777777777777777,0.7702331961591208,7);
INSERT INTO "learned_points" VALUES(309,0.8888888888888888,0.8849696257103651,7);
INSERT INTO "learned_points" VALUES(310,1,0.9999999999999986,7);
INSERT INTO "learned_points" VALUES(311,1.1111111111111112,1.115030374289632,7);
INSERT INTO "learned_points" VALUES(312,1.222222222222222,1.2297668038408764,7);
INSERT INTO "learned_points" VALUES(313,1.3333333333333333,1.3439153439153424,7);
INSERT INTO "learned_points" VALUES(314,1.4444444444444444,1.457182049774641,7);
INSERT INTO "learned_points" VALUES(315,1.5555555555555554,1.5692729766803826,7);
INSERT INTO "learned_points" VALUES(316,1.6666666666666663,1.6798941798941789,7);
INSERT INTO "learned_points" VALUES(317,1.7777777777777777,1.78875171467764,7);
INSERT INTO "learned_points" VALUES(318,1.8888888888888888,1.8955516362923772,7);
INSERT INTO "learned_points" VALUES(319,2,2,7);
INSERT INTO "learned_points" VALUES(320,2.111111111111111,2.1018028610621218,7);
INSERT INTO "learned_points" VALUES(381,0,0.01339121723878986,2);
INSERT INTO "learned_points" VALUES(382,0.2777777777777778,0.27855332002664845,2);
INSERT INTO "learned_points" VALUES(383,0.5555555555555556,0.5475131738653387,2);
INSERT INTO "learned_points" VALUES(384,0.8333333333333334,0.8198101211229715,2);
INSERT INTO "learned_points" VALUES(385,1.1111111111111112,1.0949835041676583,2);
INSERT INTO "learned_points" VALUES(386,1.3888888888888888,1.3725726653675112,2);
INSERT INTO "learned_points" VALUES(387,1.6666666666666667,1.652116947090642,2);
INSERT INTO "learned_points" VALUES(388,1.9444444444444449,1.9331556917051615,2);
INSERT INTO "learned_points" VALUES(389,2.2222222222222223,2.215228241579182,2);
INSERT INTO "learned_points" VALUES(390,2.5,2.497873939080815,2);
INSERT INTO "learned_points" VALUES(391,2.7777777777777777,2.780632126578172,2);
INSERT INTO "learned_points" VALUES(392,3.055555555555556,3.0630421464393653,2);
INSERT INTO "learned_points" VALUES(393,3.3333333333333335,3.344643341032505,2);
INSERT INTO "learned_points" VALUES(394,3.611111111111111,3.624975052725704,2);
INSERT INTO "learned_points" VALUES(395,3.8888888888888897,3.9035766238870737,2);
INSERT INTO "learned_points" VALUES(396,4.166666666666667,4.179987396884726,2);
INSERT INTO "learned_points" VALUES(397,4.444444444444445,4.453746714086771,2);
INSERT INTO "learned_points" VALUES(398,4.722222222222222,4.724393917861322,2);
INSERT INTO "learned_points" VALUES(399,5,4.99146835057649,2);
INSERT INTO "learned_points" VALUES(400,5.277777777777778,5.254509354600387,2);
DROP TABLE IF EXISTS "measurements";
CREATE TABLE "measurements" ("measurements_id_pk" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "time" DOUBLE, "fibrinogen" DOUBLE, "calibration_id_fk" INTEGER);
INSERT INTO "measurements" VALUES(17,0,0,1);
INSERT INTO "measurements" VALUES(18,0.2,0.8,1);
INSERT INTO "measurements" VALUES(19,0.4,1,1);
INSERT INTO "measurements" VALUES(20,0.6,1.1,1);
INSERT INTO "measurements" VALUES(36,1,1,1);
INSERT INTO "measurements" VALUES(37,2,2,1);
INSERT INTO "measurements" VALUES(40,1.6,1.4,1);
INSERT INTO "measurements" VALUES(42,1.8,1.63,1);
INSERT INTO "measurements" VALUES(43,0,0,2);
INSERT INTO "measurements" VALUES(44,1,1,2);
INSERT INTO "measurements" VALUES(45,2,2,2);
INSERT INTO "measurements" VALUES(46,0.2,0.1,1);
INSERT INTO "measurements" VALUES(48,3,3,2);
INSERT INTO "measurements" VALUES(52,4,4,2);
INSERT INTO "measurements" VALUES(53,0,0,7);
INSERT INTO "measurements" VALUES(54,1,1,7);
INSERT INTO "measurements" VALUES(55,2,2,7);
INSERT INTO "measurements" VALUES(56,5,5,2);
CREATE TRIGGER "after_calibration_delete" AFTER DELETE ON "calibrations" FOR EACH ROW  BEGIN DELETE FROM measurements WHERE measurements.calibration_id_fk = old.calibration_id_pk;
DELETE FROM learned_functions WHERE learned_functions.calibration_id_fk = old.calibration_id_pk;
DELETE FROM learned_points WHERE learned_points.calibration_id_fk = old.calibration_id_pk; END;
CREATE TRIGGER "after_learned_functions_insert" AFTER INSERT ON "learned_functions" FOR EACH ROW  BEGIN DELETE FROM learned_points WHERE learned_points.calibration_id_fk = new.calibration_id_fk; END;
CREATE TRIGGER "after_measurement_delete" AFTER DELETE ON "measurements" FOR EACH ROW  BEGIN DELETE FROM learned_points WHERE learned_points.calibration_id_fk = old.calibration_id_fk; DELETE FROM learned_functions WHERE learned_functions.calibration_id_fk = old.calibration_id_fk; END;
CREATE TRIGGER "after_measurements_insert" AFTER INSERT ON "measurements" FOR EACH ROW  BEGIN DELETE FROM learned_points WHERE learned_points.calibration_id_fk = new.calibration_id_fk; DELETE FROM learned_functions WHERE learned_functions.calibration_id_fk = new.calibration_id_fk; END;
CREATE TRIGGER "before_learned_functions_insert" BEFORE INSERT ON "learned_functions" FOR EACH ROW  BEGIN DELETE FROM learned_functions WHERE learned_functions.calibration_id_fk = new.calibration_id_fk; END;
