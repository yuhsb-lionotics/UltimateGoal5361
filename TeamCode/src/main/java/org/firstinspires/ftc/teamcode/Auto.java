package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import androidx.annotation.NonNull;

@Autonomous(name = "Autonomous")
public class Auto extends DriveTrain {
    // 3 boxes, 24 inches each = 72 inches
    // + 6 inches from robot to end of the square = 78 inches
    // + 12 inches (half of a square, to get wobble goal all the way in) = 90 inches
    private static final int DISTANCE_TO_WOBBLE_GOAL_DESTINATION = 90;
    private static final double MAX_POWER = 1.0;
    private static final double DEFAULT_POWER = 1.0;

    private void telemetryUpdateStatus(final @NonNull String status) {
        telemetry.addData("Status:", status);
        telemetry.update();
    }

    private void moveWobbleGoal() {
        encoderDriveForward(DEFAULT_POWER, DISTANCE_TO_WOBBLE_GOAL_DESTINATION,30);
        encoderDriveForward(
                MAX_POWER,
                -9, // to make robot back up and touch blue line
                10
        );
    }

    @Override
    public void runOpMode() {
        driveTrainSetup();
        waitForStart();

        telemetryUpdateStatus("Running");

        moveWobbleGoal();
    }


    @Override
    public void driveTrainSetup() {
        telemetryUpdateStatus("Initializing");

        super.driveTrainSetup();

        telemetryUpdateStatus("Initialized");
    }
}


