package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Go forward")
public class GoForward extends DriveTrain {
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        driveTrainSetup();

        telemetry.addData("Status:","Initialized");
        telemetry.update();
        waitForStart();

        telemetry.addData("Status:", "Running");
        telemetry.update();
        encoderDriveForward(0.7,36,10);

    }
}
