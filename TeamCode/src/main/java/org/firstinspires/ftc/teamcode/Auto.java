package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "autonomous")
public class Auto extends DriveTrain {

    @Override
    public void runOpMode() {
        setup();
        waitForStart();
    }
    private void setup(){
        driveTrainSetup();
    }
}
