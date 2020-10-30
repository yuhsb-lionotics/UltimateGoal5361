package org.firstinspires.ftc.teamcode;

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

             //Rotate coordinates by a 45 degree angle
             double forwardRightPower
                     = ( gamepad1.right_stick_x - gamepad1.left_stick_y)/Math.sqrt(2);
             double forwardLeftPower
                     = (-gamepad1.right_stick_x - gamepad1.left_stick_y)/Math.sqrt(2);
             //Strafe in the direction of the left joystick
            strafe(forwardLeftPower, forwardRightPower);
        }
    }
    public void setup(){
        driveTrainSetup();
    }
}
