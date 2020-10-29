package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Driver Controlled")
public class TeleOp extends DriveTrain {

    @Override
    public void runOpMode() {
        setup();
        waitForStart();
        while(opModeIsActive()){
             telemetry.addData("Joystick x",gamepad1.left_stick_x);
             telemetry.addData("Joystick y", gamepad1.left_stick_y);
             telemetry.update();
        }
    }
    public void setup(){
        driveTrainSetup();
    }
}
