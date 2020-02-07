package org.firstinspires.ftc.teamcode.hardware.subsystem;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.RadicalTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

public class SubsystemOdometry extends HardwareSubsystem {

  final RadicalTrackingWheelLocalizer localizer;

  public SubsystemOdometry(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    localizer = new RadicalTrackingWheelLocalizer(robot);
  }

  @Override
  public void update() {
    localizer.update();
  }

  public Pose2d getPoseEstimate() {
    return localizer.getPoseEstimate();
  }
}
