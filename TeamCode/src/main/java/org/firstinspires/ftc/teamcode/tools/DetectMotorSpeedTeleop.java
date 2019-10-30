package org.firstinspires.ftc.teamcode.tools;

import com.ftc16626.missioncontrol.MissionControl;
import com.ftc16626.missioncontrol.util.LogModel;
import com.ftc16626.missioncontrol.util.statemachine.State;
import com.ftc16626.missioncontrol.util.statemachine.StateMachine;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.ArrayList;
import java.util.Date;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@TeleOp(name = "Detect Motor Speed Teleop", group = "Testing")
public class DetectMotorSpeedTeleop extends OpMode {

  private MainHardware robot;

  private int lastEncoderFrontLeft = 0;
  private int lastEncoderFrontRight = 0;
  private int lastEncoderBackLeft = 0;
  private int lastEncoderBackRight = 0;

  private double waitingStartTime = 0;
  private double sampleStartTime = 0;

  private double increments = 0.05;
  private double waitTime = 3;
  private double sampleTime = 3;

  private double currentSpeed = 0;

  private ArrayList<ArrayList<Object[]>> samples = new ArrayList<>();

  private ArrayList<ArrayList<Integer>> currentSamples = new ArrayList<>();
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

    lastEncoderFrontLeft = robot.drive.motorFrontLeft.getCurrentPosition();
    lastEncoderFrontRight = robot.drive.motorFrontRight.getCurrentPosition();
    lastEncoderBackLeft = robot.drive.motorBackLeft.getCurrentPosition();
    lastEncoderBackRight = robot.drive.motorBackRight.getCurrentPosition();

    samples.add(new ArrayList<Object[]>());
    samples.add(new ArrayList<Object[]>());
    samples.add(new ArrayList<Object[]>());
    samples.add(new ArrayList<Object[]>());

    currentSamples.add(new ArrayList<Integer>());
    currentSamples.add(new ArrayList<Integer>());
    currentSamples.add(new ArrayList<Integer>());
    currentSamples.add(new ArrayList<Integer>());

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
          setLastEncoderValues();
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

  }

  @Override
  public void stop() {
    missionControl.stopLogging();
  }

  private void setLastEncoderValues() {
    lastEncoderFrontLeft = robot.drive.motorFrontLeft.getCurrentPosition();
    lastEncoderFrontRight = robot.drive.motorFrontRight.getCurrentPosition();
    lastEncoderBackLeft = robot.drive.motorBackLeft.getCurrentPosition();
    lastEncoderBackRight = robot.drive.motorBackRight.getCurrentPosition();
  }

  private void collectAndPushSamples() {
    double time = getRuntime();

    int deltaEncoderFrontLeft =
        robot.drive.motorFrontLeft.getCurrentPosition() - lastEncoderFrontLeft;
    int deltaEncoderFrontRight =
        robot.drive.motorFrontRight.getCurrentPosition() - lastEncoderFrontRight;
    int deltaEncoderBackLeft = robot.drive.motorBackLeft.getCurrentPosition() - lastEncoderBackLeft;
    int deltaEncoderBackRight =
        robot.drive.motorBackRight.getCurrentPosition() - lastEncoderBackRight;

    samples.get(0).add(new Object[]{time, deltaEncoderFrontLeft});
    samples.get(1).add(new Object[]{time, deltaEncoderFrontRight});
    samples.get(2).add(new Object[]{time, deltaEncoderBackLeft});
    samples.get(3).add(new Object[]{time, deltaEncoderBackRight});

    if (logTicks++ % 5 == 0) {
      missionControl.logAndBroadcast(
          new LogModel(Double.toString(deltaEncoderFrontLeft), "EncoderDeltaFrontLeft",
              new Date()));
      missionControl.logAndBroadcast(
          new LogModel(Double.toString(deltaEncoderFrontRight), "EncoderDeltaFrontRight",
              new Date()));
      missionControl.logAndBroadcast(
          new LogModel(Double.toString(deltaEncoderBackLeft), "EncoderDeltaBackLeft", new Date()));
      missionControl.logAndBroadcast(
          new LogModel(Double.toString(deltaEncoderBackRight), "EncoderDeltaBackRight",
              new Date()));
    }

    telemetry.addData("Power", currentSpeed);
    telemetry.addData("Front Left", deltaEncoderFrontLeft);
    telemetry.addData("Front Right", deltaEncoderFrontRight);
    telemetry.addData("Back Left", deltaEncoderBackLeft);
    telemetry.addData("Back Right", deltaEncoderBackRight);

    setLastEncoderValues();

    currentSamples.get(0).add(deltaEncoderFrontLeft);
    currentSamples.get(1).add(deltaEncoderFrontRight);
    currentSamples.get(2).add(deltaEncoderBackLeft);
    currentSamples.get(3).add(deltaEncoderBackRight);
  }

  private void setMotorSpeed(double speed) {
    robot.drive.motorFrontLeft.setPower(speed);
    robot.drive.motorFrontRight.setPower(speed);
    robot.drive.motorBackLeft.setPower(speed);
    robot.drive.motorBackRight.setPower(speed);
  }

  private void clearCurrentSamples() {
    for (ArrayList<Integer> sampleList : currentSamples) {
      sampleList.clear();
    }
  }

  private void averageAndPushCurrentSamples() {
    int currentPositionInList = 0;

    for (ArrayList<Integer> currentSampleList : currentSamples) {
      int sum = 0;
      for (int value : currentSampleList) {
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