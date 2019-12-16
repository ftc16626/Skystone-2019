package org.firstinspires.ftc.teamcode.auto.deliverypaths;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

public class SkyStoneDeliverFoundationLastRed implements DeliveryPath {
  private DriveBaseMecanum driveBaseMecanum;
  private MainHardware mainHardware;

  private Trajectory trajectory1;

  public SkyStoneDeliverFoundationLastRed(MainHardware mainHardware, DriveBaseMecanum driveBaseMecanum) {
    this.mainHardware = mainHardware;
    this.driveBaseMecanum = driveBaseMecanum;
  }

  @Override
  public void init() {
    trajectory1 = driveBaseMecanum.trajectoryBuilder()
        .splineTo(new Pose2d(800, 0, Math.toRadians(330)))
//        .addMarker(new Function0<Unit>() {
//          @Override
//          public Unit invoke() {
//            return null;
//          }
//        })
        .back(400)
        .strafeRight(100)
        .splineTo(new Pose2d(700 -1450, Math.toRadians(270)))
        .addMarker(new Function0<Unit>() {
          @Override
          public Unit invoke() {
            mainHardware.lift.setTargetHeight(10);
            mainHardware.intake.setPower(-0.4);

            return Unit.INSTANCE;
          }
        })
        .splineTo(new Pose2d(1400, -2300, Math.toRadians(0))).build();
  }

  @Override
  public void update() {

  }
}

