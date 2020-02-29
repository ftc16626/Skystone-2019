package org.firstinspires.ftc.teamcode.hardware.roadrunner;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;
import com.qualcomm.hardware.motors.GoBILDA5202Series;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
//import org.firstinspires.ftc.teamcode.hardware.util.GoBILDA5202Series435;

@Config
public class DriveConstants {

  private static final MotorConfigurationType MOTOR_CONFIG = MotorConfigurationType.getMotorType(
      GoBILDA5202Series.class);

  private static final double TICKS_PER_REV = 386.3;
  private static final double MAX_RPM = 435;

  public static final boolean RUN_USING_ENCODER = true;

  public static double VELO_KP = 28.5; // 38, 47, 45
  public static double VELO_KI = 0.2; // 0.5, 0.3
  public static double VELO_KD = 9.1; // 18, 18, 17

  public static final PIDCoefficients MOTOR_VELO_PID = new PIDCoefficients(VELO_KP, VELO_KI,
      VELO_KD);

//  public static final PIDCoefficients HEADING_PID = new PIDCoefficients(0, 0, 0);
  public static final PIDCoefficients HEADING_PID = new PIDCoefficients(0, 0, 0);
  public static final PIDCoefficients AXIAL_PID = new PIDCoefficients(0,  0, 0);
//  public static final PIDCoefficients AXIAL_PID = new PIDCoefficients(9,  0, 0);
  public static final PIDCoefficients LATERAL_PID = new PIDCoefficients(0, 0, 0);
//  public static final PIDCoefficients LATERAL_PID = new PIDCoefficients(9, 0, 0);

  public static double WHEEL_RADIUS = 1.9685 * 0.972 * 1.006944;
  // output (wheel) speed / input (motor) speed
  public static double GEAR_RATIO = 0.5;
  public static double TRACK_WIDTH = 15.06;
  public static double WHEEL_BASE = 13.5;

  public static double kV = 1.0 / rpmToVelocity(getMaxRpm());
  public static double kA = 0;
  public static double kStatic = 0;

  public static DriveConstraints BASE_CONSTRAINTS = new DriveConstraints(
      37, 38, 0.0,
      Math.toRadians(180.0), Math.toRadians(180.0), 0.0
  );

  public static double encoderTicksToInches(double ticks) {
    return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
//    return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks /  MOTOR_CONFIG.getTicksPerRev();
  }

  public static double rpmToVelocity(double rpm) {
    return rpm * GEAR_RATIO * 2 * Math.PI * WHEEL_RADIUS / 60.0;
  }

  public static double getMaxRpm() {
//    return MOTOR_CONFIG.getMaxRPM() *
//        (RUN_USING_ENCODER ? MOTOR_CONFIG.getAchieveableMaxRPMFraction() : 1.0);
    return MAX_RPM *
        (RUN_USING_ENCODER ? MOTOR_CONFIG.getAchieveableMaxRPMFraction() : 1.0);
  }

  public static double getTicksPerSec() {
    // note: MotorConfigurationType#getAchieveableMaxTicksPerSecond() isn't quite what we want
    return (MAX_RPM * TICKS_PER_REV / 60.0);
//    return (MOTOR_CONFIG.getMaxRPM() * MOTOR_CONFIG.getTicksPerRev() / 60.0);
  }

  public static double getMotorVelocityF() {
    // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
    return 32767 / getTicksPerSec();
  }
}
