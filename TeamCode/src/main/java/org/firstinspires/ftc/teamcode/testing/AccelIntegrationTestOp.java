//package org.firstinspires.ftc.teamcode.testing;
//
//import com.ftc16626.missioncontrol.MissionControl;
//import com.ftc16626.missioncontrol.util.LogModel;
//import com.ftc16626.missioncontrol.util.statemachine.State;
//import com.ftc16626.missioncontrol.util.statemachine.StateMachine;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.util.ElapsedTime;
//import java.util.Date;
//import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
//import org.firstinspires.ftc.teamcode.hardware.MainHardware;
//
//@TeleOp(name = "Accel Integration Test OP", group = "Testing")
//@Disabled
//public class AccelIntegrationTestOp extends OpMode {
//
//  private MainHardware robot;
//  private ElapsedTime runtime = new ElapsedTime();
//
//  private MissionControl missionControl;
//
//  private StateMachine stateMachine = buildStateMachine();
//
//  private int currentStep = 0;
//
//  @Override
//  public void init() {
//    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;
//
//    robot = new MainHardware(hardwareMap);
//    robot.init();
//
//    telemetry.addData("IMU Status", !robot.imu.isCalibrated() ? "Calibrating..." : "Calibrated");
//
//    missionControl.sendInitPacket(new String[] {"status"});
//
//    robot.imu.calibrateSensorBias(10000);
//  }
//
//  @Override
//  public void init_loop() {
//    telemetry.addData("IMU Sensor Bias Calibrating", robot.imu.isCalibrating());
//    telemetry.addData("IMU Calibration", robot.imu.getCalibrationStatus());
//    telemetry.addData("IMU Accel Calibrated", !robot.imu.getImu().isAccelerometerCalibrated() ? "Calibrating..." : "Calibrated");
//    telemetry.addData("IMU Gyro Calibrated", !robot.imu.getImu().isGyroCalibrated() ? "Calibrating..." : "Calibrated");
//    telemetry.addData("IMU Magn Calibrated", !robot.imu.getImu().isMagnetometerCalibrated() ? "Calibrating..." : "Calibrated");
//    telemetry.addData("IMU System Calibrated", !robot.imu.getImu().isSystemCalibrated() ? "Calibrating..." : "Calibrated");
//
//    missionControl.broadcast(new LogModel("Calibrating"
//        + "", "status", new Date()));
//
//    robot.update();
//  }
//
//  @Override
//  public void loop() {
//    telemetry.addData("State", stateMachine.getCurrentState().toString());
//
//    telemetry.addData("Accel X", robot.imu.getAcceleration().xAccel);
//    telemetry.addData("Accel Y", robot.imu.getAcceleration().yAccel);
//    telemetry.addData("Vel X", robot.imu.getVelocity().getX());
//    telemetry.addData("Vel Y", robot.imu.getVelocity().getY());
//    telemetry.addData("Pos X", robot.imu.getPosition().getX());
//    telemetry.addData("Pos Y", robot.imu.getPosition().getY());
//
//    switch((MyState) stateMachine.getCurrentState()) {
//      case INIT:
//        stateMachine.transition();
//        break;
//      case SET_ANGLE:
//        if(currentStep == 0) robot.drive.setAngle(0);
//        else if(currentStep == 1) robot.drive.setAngle(Math.toRadians(90));
//
//        runtime.reset();
//        stateMachine.transition();
//        break;
//      case WAITING:
//        if(runtime.seconds() > 2) {
//          currentStep++;
//
//          if (currentStep > 1) {
//            stateMachine.transition(Transition.FINISH);
//          } else {
//            stateMachine.transition();
//          }
//        }
//        break;
//      case DONE:
//        robot.drive.stopMotors();
//        break;
//    }
//
//    robot.update();
//  }
//
//  private StateMachine buildStateMachine() {
//    StateMachine machine = new StateMachine();
//
//    machine
//        .state(new State(MyState.INIT))
//        .state(new State(MyState.SET_ANGLE))
//        .state(
//            new State(MyState.WAITING)
//              .on(Transition.NEW_ANGLE, MyState.SET_ANGLE)
//              .on(Transition.FINISH, MyState.DONE)
//        )
//        .state(new State(MyState.DONE));
//
//    return machine;
//  }
//
//  enum MyState {
//    INIT,
//    SET_ANGLE,
//    WAITING,
//    DONE
//  }
//
//  enum Transition {
//    NEW_ANGLE,
//    FINISH
//  }
//}