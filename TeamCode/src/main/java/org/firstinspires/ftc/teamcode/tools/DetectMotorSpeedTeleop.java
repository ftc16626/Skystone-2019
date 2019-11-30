package org.firstinspires.ftc.teamcode.tools;

import com.ftc16626.missioncontrol.MissionControl;
import com.ftc16626.missioncontrol.util.LogModel;
import com.ftc16626.missioncontrol.util.statemachine.State;
import com.ftc16626.missioncontrol.util.statemachine.StateMachine;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.ArrayList;
import java.util.Date;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@TeleOp(name = "Detect Motor Speed Teleop", group = "Tools")
@Disabled
public class DetectMotorSpeedTeleop extends OpMode {

  private MainHardware robot;

  private double waitingStartTime = 0;
  private double sampleStartTime = 0;

  private double increments = 0.05;
  private double waitTime = 3;
  private double sampleTime = 3;

  private double currentSpeed = 0;

  private ArrayList<ArrayList<Object[]>> samples = new ArrayList<>();

  private ArrayList<ArrayList<Double>> currentSamples = new ArrayList<>();
  private int[][] averages = new int[4][(int) (1 / increments) + 1];
  private int currentAvgPosition = 0;

  private StateMachine stateMachine = buildStateMachine();

  private MissionControl missionControl;

  private double logTicks = 0;

  @Override
  public void init() {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;
    missionControl.startLogging();

    robot = new MainHardware(hardwareMap);
    robot.drive.stopMotors();
    robot.init();

    samples.add(new ArrayList<Object[]>());
    samples.add(new ArrayList<Object[]>());
    samples.add(new ArrayList<Object[]>());
    samples.add(new ArrayList<Object[]>());

    currentSamples.add(new ArrayList<Double>());
    currentSamples.add(new ArrayList<Double>());
    currentSamples.add(new ArrayList<Double>());
    currentSamples.add(new ArrayList<Double>());

    missionControl.sendInitPacket(
        new String[]{
            "EncoderDeltaFrontLeft", "EncoderDeltaFrontRight",
            "EncoderDeltaBackLeft", "EncoderDeltaBackRight"
        });

    telemetry.addData("Msg:", "Press Start to Continue");
  }

  @Override
  public void loop() {
    telemetry.addData("State", stateMachine.getCurrentState().toString());

    switch ((MyState) stateMachine.getCurrentState()) {
      case INIT:
        stateMachine.transition();
        break;
      case SET_MOTOR_SPEED:
        setMotorSpeed(currentSpeed);
        clearCurrentSamples();

        waitingStartTime = getRuntime();
        stateMachine.transition();
        break;
      case WAITING:
        if (getRuntime() - waitingStartTime >= waitTime) {
          sampleStartTime = getRuntime();
          stateMachine.transition();
        }
        break;
      case SAMPLING:
        collectAndPushSamples();

        if (getRuntime() - sampleStartTime >= sampleTime) {

          if (currentSpeed < 1) {
            currentSpeed += increments;
            averageAndPushCurrentSamples();
            stateMachine.transition(Transition.RESTART_SAMPLING);
          } else {
            stateMachine.transition(Transition.FINISH);
          }
        }
        break;
      case DONE:
        robot.drive.stopMotors();
        break;
    }

    robot.update();
  }

  @Override
  public void stop() {
    missionControl.stopLogging();
  }

  private void collectAndPushSamples() {
    double time = getRuntime();

    Date now = new Date();

    samples.get(0).add(new Object[]{time, robot.drive.motorVelFrontLeft});
    samples.get(1).add(new Object[]{time, robot.drive.motorVelFrontRight});
    samples.get(2).add(new Object[]{time, robot.drive.motorVelBackLeft});
    samples.get(3).add(new Object[]{time, robot.drive.motorVelBackRight});

    if (logTicks++ % 5 == 0) {
      missionControl.logAndBroadcast(
          new LogModel(Double.toString(robot.drive.motorVelFrontLeft), "EncoderDeltaFrontLeft", now));
      missionControl.logAndBroadcast(
          new LogModel(Double.toString(robot.drive.motorVelFrontRight), "EncoderDeltaFrontRight", now));
      missionControl.logAndBroadcast(
          new LogModel(Double.toString(robot.drive.motorVelBackLeft), "EncoderDeltaBackLeft", now));
      missionControl.logAndBroadcast(
          new LogModel(Double.toString(robot.drive.motorVelBackRight), "EncoderDeltaBackRight", now));
    }

    telemetry.addData("Power", currentSpeed);
    telemetry.addData("Front Left", robot.drive.motorVelFrontLeft);
    telemetry.addData("Front Right", robot.drive.motorVelFrontRight);
    telemetry.addData("Back Left", robot.drive.motorVelBackLeft);
    telemetry.addData("Back Right", robot.drive.motorVelBackRight);


    currentSamples.get(0).add(robot.drive.motorVelFrontLeft);
    currentSamples.get(1).add(robot.drive.motorVelFrontRight);
    currentSamples.get(2).add(robot.drive.motorVelBackLeft);
    currentSamples.get(3).add(robot.drive.motorVelBackRight);
  }

  private void setMotorSpeed(double speed) {
    robot.drive.motorFrontLeft.setPower(speed);
    robot.drive.motorFrontRight.setPower(speed);
    robot.drive.motorBackLeft.setPower(speed);
    robot.drive.motorBackRight.setPower(speed);
  }

  private void clearCurrentSamples() {
    for (ArrayList<Double> sampleList : currentSamples) {
      sampleList.clear();
    }
  }

  private void averageAndPushCurrentSamples() {
    int currentPositionInList = 0;

    for (ArrayList<Double> currentSampleList : currentSamples) {
      int sum = 0;
      for (double value : currentSampleList) {
        sum += value;
      }

      int average = sum / currentSampleList.size();
      averages[currentPositionInList++][currentAvgPosition] = average;
    }

    currentAvgPosition++;
  }

  private StateMachine buildStateMachine() {
    StateMachine machine = new StateMachine();

    machine
        .state(new State(MyState.INIT))
        .state(new State(MyState.SET_MOTOR_SPEED))
        .state(new State(MyState.WAITING))
        .state(
            new State(MyState.SAMPLING)
                .on(Transition.RESTART_SAMPLING, MyState.SET_MOTOR_SPEED)
                .on(Transition.FINISH, MyState.DONE)
        )
        .state(new State(MyState.DONE));

    return machine;
  }

  enum MyState {
    INIT,
    SET_MOTOR_SPEED,
    WAITING,
    SAMPLING,
    DONE
  }

  enum Transition {
    RESTART_SAMPLING,
    FINISH
  }
}