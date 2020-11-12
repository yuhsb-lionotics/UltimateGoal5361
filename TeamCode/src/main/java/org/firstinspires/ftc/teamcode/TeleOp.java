package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Driver Controlled")
public class TeleOp extends DriveTrain {

    DcMotor leftLauncherWheel, rightLauncherWheel, arm;
    CRServo conveyorBelt;

    @Override
    public void runOpMode() {
        setup();
        telemetry.addData("Status:","Initialized");
        telemetry.update();
        waitForStart();
        leftLauncherWheel.setPower(1);
        rightLauncherWheel.setPower(1);
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
        telemetry.addData("Status", "Initializing");
        telemetry.update();

        driveTrainSetup();
        leftLauncherWheel = hardwareMap.dcMotor.get("left launcher wheel");
        rightLauncherWheel = hardwareMap.dcMotor.get("right launcher wheel");
        conveyorBelt = hardwareMap.crservo.get("conveyor belt");
        arm = hardwareMap.dcMotor.get("arm");
        leftLauncherWheel.setDirection(DcMotor.Direction.FORWARD);
        rightLauncherWheel.setDirection(DcMotor.Direction.REVERSE);
        conveyorBelt.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setDirection(DcMotor.Direction.FORWARD);
    }
}
