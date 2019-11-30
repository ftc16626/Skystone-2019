package org.firstinspires.ftc.teamcode.hardware;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;
import com.qualcomm.hardware.motors.GoBILDA5202Series;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class DriveConstants {

  private static final MotorConfigurationType MOTOR_CONFIG = MotorConfigurationType.getMotorType(
      GoBILDA5202Series.class);

  public static final boolean RUN_USING_ENCODER = true;
  public static final PIDCoefficients MOTOR_VELO_PID = new PIDCoefficients(20,4,12);

  public static double WHEEL_RADIUS = 50;
  // output (wheel) speed / input (motor) speed
  // 99.5 is the assumed gear ration but 26.1 is the gobilda one. adjust so
//  public static double GEAR_RATIO = 1 / ((99.5 / 26.1) / 2);
  public static double GEAR_RATIO = 0.5;
  public static double TRACK_WIDTH = 383.89;

  public static double kV = 1.0 / rpmToVelocity(getMaxRpmGoBilda435());
  public static double kA = 0;
  public static double kStatic = 0;

  public static DriveConstraints BASE_CONSTRAINTS = new DriveConstraints(
      762.0, 762.0, 0.0,
      Math.toRadians(180.0), Math.toRadians(180.0), 0.0
  );

  public static double encoderTicksToInches(double ticks) {
    return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / MOTOR_CONFIG.getTicksPerRev();
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
    return (MOTOR_CONFIG.getMaxRPM() * MOTOR_CONFIG.getTicksPerRev() / 60.0);
  }

  public static double getMotorVelocityF() {
    // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
    return 32767 / getTicksPerSec();
  }
}
