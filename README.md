CalibrationCurves
=================

A simple program whose goal is to make it easier for lab technicians to make calibration curves when making fibrinogen calibrations - several points are entered, and the program needs to "learn" the function and plot the results. 

The learning part is done using polynomial regression (3rd order, independent variable - time, and dependent variable - fibrinogen).

Libraries used:
- JAMA for matrices
- xerial jdbc-sqlite
- JFreeChart and JCommon
