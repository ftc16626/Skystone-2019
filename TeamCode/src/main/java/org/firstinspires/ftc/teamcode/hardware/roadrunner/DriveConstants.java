package org.firstinspires.ftc.teamcode.hardware.roadrunner;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import org.firstinspires.ftc.teamcode.hardware.util.GoBILDA5202Series435;

@Config
public class DriveConstants {

  //  private static final MotorConfigurationType MOTOR_CONFIG = MotorConfigurationType.getMotorType(
//      GoBILDA5202Series.class);
  private static final MotorConfigurationType MOTOR_CONFIG = MotorConfigurationType.getMotorType(
      GoBILDA5202Series435.class);

  public static final boolean RUN_USING_ENCODER = true;

  public static double kP = 20;
  public static double kI = 4;
  public static double kD = 12;

  //  public static final PIDCoefficients MOTOR_VELO_PID = new PIDCoefficients(getMotorVelocityF() * .1, getMotorVelocityF() * .1 * .1,0);
  public static final PIDCoefficients MOTOR_VELO_PID = new PIDCoefficients(kP, kI, kD);

  public static double WHEEL_RADIUS = 50;
  // output (wheel) speed / input (motor) speed
  // 99.5 is the assumed gear ration but 26.1 is the gobilda one. adjust so
//  public static double GEAR_RATIO = 1 / ((99.5 / 26.1) / 2);
//  public static double GEAR_RATIO = 1;
  public static double GEAR_RATIO = 0.5;
  //  public static double TRACK_WIDTH = 423.33; //371.93,392.55,394.71,395.51,396.71,397.77,423.33
  public static double TRACK_WIDTH = 384; //371.93,392.55,394.71,395.51,396.71,397.77,423.33
//  public static double TRACK_WIDTH = 381; //371.93,392.55,394.71,395.51,396.71,397.77,423.33
//    public static double TRACK_WIDTH = 360;

  public static double kV = 1.0 / rpmToVelocity(getMaxRpmGoBilda435());
  //  public static double kV = 0.00993;
  public static double kA = 0;
  public static double kStatic = 0;
//  public static double kStatic = 0.02752;

  public static double ENCODER_COUNTS_PER_REV = 386.3;

  public static DriveConstraints BASE_CONSTRAINTS = new DriveConstraints(
      850, 762.0, 0.0,
      Math.toRadians(180.0), Math.toRadians(180.0), 0.0
  );

  public static double encoderTicksToInches(double ticks) {
    return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / ENCODER_COUNTS_PER_REV;
  }

  public static double rpmToVelocity(double rpm) {
    return rpm * GEAR_RATIO * 2 * Math.PI * WHEEL_RADIUS / 60.0;
  }

  public static double getMaxRpm() {
    return MOTOR_CONFIG.getMaxRPM() *
        (RUN_USING_ENCODER ? MOTOR_CONFIG.getAchieveableMaxRPMFraction() : 1.0);
  }

  public static double getMaxRpmGoBilda435() {
    return 435 * (RUN_USING_ENCODER ? MOTOR_CONFIG.getAchieveableMaxRPMFraction() : 1.0);
  }

  public static double getTicksPerSec() {
    // note: MotorConfigurationType#getAchieveableMaxTicksPerSecond() isn't quite what we want
    return (getMaxRpmGoBilda435() * ENCODER_COUNTS_PER_REV / 60.0);
//    return (MOTOR_CONFIG.getMaxRPM() * MOTOR_CONFIG.getTicksPerRev() / 60.0);
  }

  public static double getMotorVelocityF() {
    // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
    return 32767 / getTicksPerSec();
  }
}
