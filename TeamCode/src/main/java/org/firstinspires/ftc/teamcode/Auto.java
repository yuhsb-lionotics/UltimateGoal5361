package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import androidx.annotation.NonNull;

@Autonomous(name = "Autonomous")
public class Auto extends DriveTrain {
    
    // 3 boxes, 24 inches each = 72 inches
    // + 6 inches from robot to end of the square = 78 inches
    // + 12 inches (half of a square, to get wobble goal all the way in) = 90 inches
    private static final int DISTANCE_TO_WOBBLE_GOAL_DESTINATION = 90;
    private static final double MAX_POWER = 1.0;
    private static final double DEFAULT_POWER = 1.0;
    private DcMotor goalArm;

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

        //turnArm(0.5, 6);
        while (opModeIsActive()) {
            telemetry.addData("Position", goalArm.getCurrentPosition());
            telemetry.update();
            idle();
        }

        telemetryUpdateStatus("Done");
        sleep(2000);
    }


    @Override
    public void driveTrainSetup() {
        telemetryUpdateStatus("Initializing");

        super.driveTrainSetup();
        goalArm = hardwareMap.dcMotor.get("arm");
        goalArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        goalArm.setPower(0);

        telemetryUpdateStatus("Initialized");
    }

    private void turnArm(double circleFraction, int timeoutS) {
        ElapsedTime turningTime = new ElapsedTime();
        // The motor on the arm is a neverest 40, which has 1120 ticks per revolution
        int encoderTarget = goalArm.getCurrentPosition() + (int) (circleFraction * 1120 / 2);
        telemetry.addData("Current position", goalArm.getCurrentPosition());
        telemetry.addData("Target",encoderTarget);
        telemetry.update();
        sleep(1000);

        goalArm.setTargetPosition(encoderTarget);
        goalArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turningTime.reset();
        goalArm.setPower(0.3);

        while (opModeIsActive() && turningTime.seconds() < timeoutS && goalArm.isBusy()) {
            idle();
        }

        goalArm.setPower(0);
        goalArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Encoder Arm", "Finished in %.2f s/%f", turningTime.seconds(), timeoutS);
        telemetry.update();


    }
}


