package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "blue autonomous")
public class Auto extends DriveTrain {

    @Override
    public void runOpMode() {
        setup();
        telemetry.addData("Status:","Initialized");
        telemetry.update();
        waitForStart();
        telemetry.addData("Status:", "Running");
        telemetry.update();
        //driving commands here
    }
    private void setup(){
        driveTrainSetup();
    }
}
