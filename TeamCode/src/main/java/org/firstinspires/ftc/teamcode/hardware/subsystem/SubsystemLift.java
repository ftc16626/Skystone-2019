package org.firstinspires.ftc.teamcode.hardware.subsystem;

import android.util.Log;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.ftc16626.missioncontrol.math.PIDController;
import com.ftc16626.missioncontrol.util.statemachine.State;
import com.ftc16626.missioncontrol.util.statemachine.StateMachine;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import java.util.Objects;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.util.DcMotorCached;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;

@Config
public class SubsystemLift extends HardwareSubsystem {

  private StateMachine<MyState, Transition> stateMachine = buildStateMachine();

  public static int MAX_HEIGHT = 1600;

  public static double kP = 0.015;
  public static double kI = 0;
  public static double kD = 0;
  public static double kG = 0;

  private int zeroPos = 0;
  private final double resetSpeed = 0.3;

  private PIDController controller = new PIDController(kP, kI, kD, kG);

  public static int targetPos = 0;

  private final DcMotorCached motorBottom, motorTop;
  private final DigitalChannel switchRight;

  private final String[] motorIds = new String[]{
      "motorLiftBottom", "motorLiftTopAndEncoderMiddle"
  };
  private final String[] liftSwitchIds = new String[]{"liftSensorRight"};

  public SubsystemLift(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    motorBottom = new DcMotorCached(robot.hwMap.get(ExpansionHubMotor.class, motorIds[0]));
    motorTop = new DcMotorCached(robot.hwMap.get(ExpansionHubMotor.class, motorIds[1]));
    motorBottom.getMotor().setDirection(Direction.REVERSE);

    switchRight = robot.hwMap.digitalChannel.get(liftSwitchIds[0]);

    controller.setBounds(-1, 1);
  }

  @Override
  public void update() {
    RevBulkData bulkData = getRobot().getBulkDataOne();

    if (bulkData == null) {
      return;
    }

    int currentPosRaw = bulkData
        .getMotorCurrentPosition(motorBottom.getMotor());

    int currentPos = currentPosRaw - zeroPos;

    controller.setKP(kP);
    controller.setKD(kD);
    controller.setKG(kGFunction(kG, currentPos / MAX_HEIGHT));

    double power = controller.update(targetPos - currentPos);

    switch (Objects.requireNonNull(stateMachine.getCurrentState())) {
      case IDLE:
        if ((!bulkData.getDigitalInputState(switchRight)) && targetPos - currentPos < 0) {
          motorBottom.setPower(0);
          motorTop.setPower(0);

          zeroPos = currentPosRaw;
        } else {
          motorBottom.setPower(power);
          motorTop.setPower(-power);
        }
        break;
      case RESETTING:
        if (bulkData.getDigitalInputState(switchRight)) {
          motorBottom.setPower(0);
          zeroPos = currentPos;
          stateMachine.transition(Transition.IDLE);
        } else {
          motorBottom.setPower(-resetSpeed);
        }
        break;
    }

    TelemetryPacket telemetryPacket = new TelemetryPacket();
    telemetryPacket.put("currentHeight", currentPos);
    telemetryPacket.put("targetPos", targetPos);
    telemetryPacket.put("motorPower", power);

    FtcDashboard.getInstance().sendTelemetryPacket(telemetryPacket);
  }

  @Override
  public void onMount() {
    RevBulkData bulkData = getRobot().getBulkDataOne();

    if (bulkData == null) {
      return;
    }

    zeroPos = bulkData.getMotorCurrentPosition(motorBottom.getMotor());
  }

  public void setPosition(int position) {
    this.targetPos = position;
  }

  public void adjustZeroOffset(int offset) {
    zeroPos += offset;
  }

  private double kGFunction(double kG, double percent) {
    return lerp(0, kG, percent);
  }

  private double lerp(double start, double end, double t) {
    return start * (1 - t) + end * t;
  }

  private StateMachine<MyState, Transition> buildStateMachine() {
    StateMachine<MyState, Transition> stateMachine = new StateMachine<>();

    stateMachine
        .state(
            new State<MyState, Transition>(MyState.IDLE)
                .on(Transition.RESET, MyState.RESETTING)
        )
        .state(
            new State<MyState, Transition>(MyState.RESETTING)
                .on(Transition.IDLE, MyState.IDLE)
        );

    return stateMachine;
  }

  enum MyState {
    IDLE,
    RESETTING
  }

  enum Transition {
    RESET,
    IDLE
  }
}
