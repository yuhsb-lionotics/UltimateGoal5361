package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "autonomous")
public class Auto extends LinearOpMode {

    private DriveTrain driveTrain = new DriveTrain();

    @Override
    public void runOpMode() {

    }
}

    //public void frrotate(double power){
    //    fr.setPower(power/2);
    //    bl.setPower(power/2);
    //    br.setPower(-power);
    //    sleep(10000);
    //    fr.setPower(0);
    //    bl.setPower(0);
    //    br.setPower(0);
    //    }
