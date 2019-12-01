package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@Config
@Autonomous(name = "Auto - Block Auto Blue", group = "testing")
public class BlockAutoBlue extends LinearOpMode {

  /**
   * AUTO PLAN:
   *
   * POINT 1
   *    - Go forward by distance of *step1*
   *    - Lower servo
   * POINT 2
   *    - Go back by distance of *step2*
   *    - Strafe left by distance of *step3*
   *    - Raise servo slightly so it fits under bridge
   *    - drive.turnSync() to account for drifting
   * POINT 3
   *    - Strafe left back to the next block by distance of *step4*
   *    - Go forward towards block by distance of *step5*
   *    - lower servo to grab next block
   * POINT 4
   *    - go back by distance *step2* (since it should go back same distance as before)
   *    - Strafe right by distance *step6* to go past bridge
   *    - Raise servo slightly so it fits under bridge
   *    - drive.turnSync() to account for drifting
   * POINT 5
   *    - Strafe left by distance *step 7* to go to next block
   *    - go forward towards block by distance of *step 6*
   *    - lower servo to grab next block
   * POINT 6
   *    - Strafe left to prepare for turn
   *    - turn 90 degrees
   * POINT 7
   *    - Move forward by distance *step 9* to deliver last stone
   *    - Raise servo slightly so it fits under bridge
   * POINT 8
   *    - Go back to go over tape
   * FINISH
   */

  public static double step1 = 650;
  public static double step2 = 150;
  public static double step3 = 1600;
  public static double step4 = step3 + 190 - 150;
  public static double step5 = step2 + 110;
  public static double step6 = step3 + 220;
  //  public static double step7 = 250;
  public static double step7 = 1120 + 80;
  public static double step8 = 280 - 50;
  public static double step9 = 1500;

  //  public static double turn1 = Math.toRadians(-90);
  MainHardware robot;

  ElapsedTime runtime = new ElapsedTime();

  @Override
  public void runOpMode() {
    robot = new MainHardware(hardwareMap);
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    Trajectory trajectory1 = drive.trajectoryBuilder()
        .back(step1).build();
    Trajectory trajectory2 = drive.trajectoryBuilder().forward(step2).strafeRight(step3).build();
    Trajectory trajectory3 = drive.trajectoryBuilder().strafeLeft(step4).back(step5).build();
    Trajectory trajectory4 = drive.trajectoryBuilder().forward(step2).strafeRight(step6).build();
//    Trajectory trajectory5 = drive.trajectoryBuilder().strafeRight(step7).build();
    Trajectory trajectory5 = drive.trajectoryBuilder().strafeLeft(step7).back(step8).build();
    Trajectory trajectory6 = drive.trajectoryBuilder().strafeLeft(step2).build();
    Trajectory trajectory7 = drive.trajectoryBuilder().back(step9).build();
    Trajectory trajectory8 = drive.trajectoryBuilder().forward(step9 - 780).build();

//    robot.backRightServo.setPosition(1);
//    robot.raiseBackRightServo();
    robot.backRightServo.setPosition(0.55);

    // Lock the slider
    robot.swingyServo.setPosition(1);

    // Get starting angle to calibrate for later
    double startingAngle = drive.getRawExternalHeading();

    waitForStart();

    if (isStopRequested()) {
      return;
    }

    robot.init();

    /** POINT 1 **/
    drive.followTrajectorySync(trajectory1);

//    robot.backRightServo.setPosition(0);
    // Lower servo
    robot.perpendicularBackRightServo();
    // Unlock and drop slider
    robot.swingyServo.setPosition(1);

    // Wait like .8 seconds to make sure servo goes down all the way
    runtime.reset();
    while (runtime.seconds() < 0.8) {
      ;
    }

    /** POINT 2 **/
    drive.followTrajectorySync(trajectory2);

//    robot.raiseBackRightServo();
    // Raise servo but not all the way b/c of the bridge
    robot.backRightServo.setPosition(0.55);

    // Turn to correct for any drift
    drive.turnSync(-(drive.getRawExternalHeading() - startingAngle));

    /** POINT 3 **/
    drive.followTrajectorySync(trajectory3);

    // Lower servo to grab block
    robot.perpendicularBackRightServo();

    // Wait 0.8 second to wait for servo to drop
    runtime.reset();
    while (runtime.seconds() < 0.8) {

    }

    /** Point 4 **/
    drive.followTrajectorySync(trajectory4);

//    robot.raiseBackRightServo();
    // Raise servo but not all the way b/c of the bridge
    robot.backRightServo.setPosition(0.55);

    //Turn to correct for drift
    drive.turnSync(-(drive.getRawExternalHeading() - startingAngle));

    // Wait for servo (should be able to remove)
    runtime.reset();
    while (runtime.seconds() < 0.8) {
    }

    /** Point 5 **/
    drive.followTrajectorySync(trajectory5);

    // lower servo to grab block
    robot.perpendicularBackRightServo();

    // wait for servo to grab
    runtime.reset();
    while (runtime.seconds() < 0.8) {
    }

    /** Point 6 **/
    drive.followTrajectorySync(trajectory6);

    // Turn to push block
    drive.turnSync(Math.toRadians(-90));

    /** Point 7 **/
    drive.followTrajectorySync(trajectory7);

    // Raise servo but not all the way b/c of bridge
    robot.backRightServo.setPosition(0.55);

    // Wait for servo to move
    runtime.reset();
    while (runtime.seconds() < 0.2) {
    }

    /** Point 8 **/
    drive.followTrajectorySync(trajectory8);

//    drive.turnSync(turn1);
//    drive.followTrajectorySync(trajectory2);
  }
}
