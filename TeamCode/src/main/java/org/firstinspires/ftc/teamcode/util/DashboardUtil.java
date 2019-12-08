package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.path.Path;

public class DashboardUtil {
  private static final double IN_TO_MM = 0.0394;
  private static final double DEFAULT_RESOLUTION = 2.0 * IN_TO_MM; // distance units; presumed inches
  private static final double ROBOT_RADIUS = 9; // in


  public static void drawSampledPath(Canvas canvas, Path path, double resolution) {
    int samples = (int) Math.ceil(path.length() / resolution);
    double[] xPoints = new double[samples];
    double[] yPoints = new double[samples];
    double dx = path.length() / (samples - 1);
    for (int i = 0; i < samples; i++) {
      double displacement = i * dx;
      Pose2d pose = path.get(displacement);
      xPoints[i] = pose.getX() * IN_TO_MM;
      yPoints[i] = pose.getY() * IN_TO_MM;
    }
    canvas.strokePolyline(xPoints, yPoints);
  }

  public static void drawSampledPath(Canvas canvas, Path path) {
    drawSampledPath(canvas, path, DEFAULT_RESOLUTION);
  }

  public static void drawRobot(Canvas canvas, Pose2d pose) {
    canvas.strokeCircle(pose.getX() * IN_TO_MM, pose.getY() * IN_TO_MM, ROBOT_RADIUS);
    Vector2d v = pose.headingVec().times(ROBOT_RADIUS);
    double x1 = (pose.getX()) + (v.getX() / 2);
    double y1 = (pose.getY()) + (v.getY() / 2);
    double x2 = (pose.getX()) + (v.getX());
    double y2 = (pose.getY()) + (v.getY());
    canvas.strokeLine(x1 * IN_TO_MM, y1 * IN_TO_MM, x2 * IN_TO_MM, y2 * IN_TO_MM);
  }
}