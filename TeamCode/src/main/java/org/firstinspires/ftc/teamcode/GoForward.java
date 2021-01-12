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
        telemetry.addData("FL Stop mode", fl.getZeroPowerBehavior());
        telemetry.addData("FR Stop mode", fr.getZeroPowerBehavior());
        telemetry.update();
        waitForStart();

        telemetry.addData("Status:", "Running");
        telemetry.update();
        encoderDriveForward(0.7,42,10);
        //encoderDrive(1,1,1,1,16*Math.PI, 15);

    }
}
