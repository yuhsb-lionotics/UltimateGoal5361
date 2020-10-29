package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "blue autonomous")
public class Auto extends DriveTrain {

    @Override
    public void runOpMode() {
        setup();
        waitForStart();

        //driving commands here
    }
    private void setup(){
        driveTrainSetup();
    }
}
