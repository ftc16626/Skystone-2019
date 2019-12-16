package org.firstinspires.ftc.teamcode.auto.deliverypaths;

import android.util.Log;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.ftc16626.missioncontrol.util.statemachine.State;
import com.ftc16626.missioncontrol.util.statemachine.StateMachine;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

public class SkyStoneDeliverFoundationMiddleBlue implements DeliveryPath {

  private DriveBaseMecanum driveBaseMecanum;
  private MainHardware mainHardware;

  private Trajectory trajectory1;

  public Telemetry telemetry = null;

  private StateMachine stateMachine = buildStateMachine();

  private boolean FIRSTCHECKPOINT = false;

  private double initialAngle = 0;

  public SkyStoneDeliverFoundationMiddleBlue(MainHardware mainHardware,
      DriveBaseMecanum driveBaseMecanum) {
    this.mainHardware = mainHardware;
    this.driveBaseMecanum = driveBaseMecanum;
  }

  @Override
  public void init() {
//    driveBaseMecanum.setPoseEstimate(new Pose2d(810, 215, Math.toRadians(90)));

    initialAngle = driveBaseMecanum.getPoseEstimate().getHeading();
    telemetry.addData("HEADING", initialAngle);
    telemetry.update();

    trajectory1 = driveBaseMecanum.trajectoryBuilder()
        .splineTo(new Pose2d(800, 0, Math.toRadians(330)))
        .addMarker(3, new Function0<Unit>() {
          @Override
          public Unit invoke() {
            mainHardware.intake.setPower(-.3);
            Log.i("TESTTEST", "YOYOY");
            telemetry.addData("TESTEST", "TEST");
            telemetry.update();

            return Unit.INSTANCE;
          }
        })
        .back(400)
        .strafeRight(-100)
        .splineTo(new Pose2d(-700, -1450, Math.toRadians(270)))
        .addMarker(12, new Function0<Unit>() {
          @Override
          public Unit invoke() {
            mainHardware.intake.open();

            telemetry.addData("TESTEST", "UHHHHHHHHHHHHH");
            telemetry.update();

            stateMachine.transition();

            return Unit.INSTANCE;
          }
        })
        .build();

    driveBaseMecanum.followTrajectory(trajectory1);
  }

  @Override
  public void update() {
    driveBaseMecanum.update();

    if(!FIRSTCHECKPOINT && driveBaseMecanum.getPoseEstimate().getY() < -1000) {
      telemetry.addData("TESTTEST", "YOYOYO");
      telemetry.update();

//      mainHardware.intake.open();

      FIRSTCHECKPOINT = true;
    }

    switch ((MyState) Objects.requireNonNull(stateMachine.getCurrentState())) {
      case DELIVERY1:
        telemetry.addData("STATE", "DELIY");
        break;
      case CORRECT_HEADING:
        telemetry.addData("STATE", "HEADING");
        driveBaseMecanum.turn(driveBaseMecanum.getPoseEstimate().getHeading() - initialAngle);
        stateMachine.transition();
        break;
      default:
        telemetry.addData("STATE", "TEST");
        break;
    }

    double x = driveBaseMecanum.getPoseEstimate().getX();
    double y = driveBaseMecanum.getPoseEstimate().getY();
    Log.i("POSTEST", x + ", " + y);
    telemetry.addData("POS", x + ", " + y);
    telemetry.update();
  }

  private StateMachine buildStateMachine() {
    StateMachine stateMachine = new StateMachine();

    stateMachine
        .state(new State(MyState.DELIVERY1))
        .state(new State(MyState.CORRECT_HEADING));

    return stateMachine;
  }

  enum MyState {
    DELIVERY1,
    CORRECT_HEADING,
    END
  }

  enum MyTransition {

  }
}
