package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Driver Controlled")
public class TeleOp extends LinearOpMode {

    private DriveTrain driveTrain = new DriveTrain();

    @Override
    public void runOpMode() {
        setup();
        waitForStart();
        while(opModeIsActive()){

        }
    }
    public void setup(){
        driveTrain.setup(hardwareMap);
    }
}
