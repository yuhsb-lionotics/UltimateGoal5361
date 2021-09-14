package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Driver Controlled")
public class TeleOp extends DriveTrain {

    DcMotor goalArm;
    //DcMotor leftLauncherWheel, rightLauncherWheel;
    //CRServo conveyorBelt;

    @Override
    public void runOpMode() {
        setup();
        telemetry.addData("Status:","Initialized");
        telemetry.update();
        waitForStart();
        //leftLauncherWheel.setPower(1);
        //rightLauncherWheel.setPower(1);
        while(opModeIsActive()){
             telemetry.addData("Joystick x",gamepad1.left_stick_x);
             telemetry.addData("Joystick y", gamepad1.left_stick_y);
             telemetry.update();

             //Drive wheels - the DPad is for approaching the goal to pick it up
             if(gamepad1.dpad_up){
                 driveForward(0.25);
             } else if (gamepad1.dpad_down) {
                 driveForward(-0.25);
             } else if (gamepad1.dpad_right) {
                 strafeRight(0.25);
             } else if (gamepad1.dpad_left) {
                 strafeRight(-0.25);
             } else { tankControl(0.5); }

             //Goal arm control
            if (gamepad1.left_bumper) {
                //Automatically pick up the wobble goal
                turnArm(-0.18,0.25,6);
            } else if (gamepad1.right_bumper) {
                turnArm(0.29,0.4,6);
            } else {
                //Manual control
                double armPower = 0.5 * (gamepad1.right_trigger - gamepad1.left_trigger);
                goalArm.setPower(armPower);
            }

             /*
             //Rotate coordinates by a 45 degree angle
             double forwardRightPower
                     = ( gamepad1.right_stick_x - gamepad1.right_stick_y)/Math.sqrt(2);
             double forwardLeftPower
                     = (-gamepad1.right_stick_x - gamepad1.right_stick_y)/Math.sqrt(2);
             //Strafe in the direction of the left joystick
            strafe(forwardLeftPower, forwardRightPower);*/
        }
    }
    public void setup(){
        telemetry.addData("Status", "Initializing");
        telemetry.update();

        driveTrainSetup();
        goalArm = hardwareMap.dcMotor.get("arm");
        goalArm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        goalArm.setPower(0);
        /*
        leftLauncherWheel = hardwareMap.dcMotor.get("left launcher wheel");
        rightLauncherWheel = hardwareMap.dcMotor.get("right launcher wheel");
        conveyorBelt = hardwareMap.crservo.get("conveyor belt");
        arm = hardwareMap.dcMotor.get("arm");
        leftLauncherWheel.setDirection(DcMotor.Direction.FORWARD);
        rightLauncherWheel.setDirection(DcMotor.Direction.REVERSE);
        conveyorBelt.setDirection(DcMotorSimple.Direction.FORWARD);
        arm.setDirection(DcMotor.Direction.FORWARD); */
    }

    private void turnArm(double circleFraction, double power, int timeoutS) {
        ElapsedTime turningTime = new ElapsedTime();
        // The motor on the arm is a neverest 40, which has 1120 ticks per revolution
        int encoderTarget = goalArm.getCurrentPosition() + (int) (circleFraction * 1120 * 2);
        telemetry.addData("Current position", goalArm.getCurrentPosition());
        telemetry.addData("Target",encoderTarget);
        telemetry.update();
        sleep(200);
        telemetry.addData("Status", "Turning");
        telemetry.update();

        goalArm.setTargetPosition(encoderTarget);
        goalArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turningTime.reset();
        goalArm.setPower(power);

        while (opModeIsActive() && turningTime.seconds() < timeoutS && goalArm.isBusy()) {
            idle();
        }

        goalArm.setPower(0);
        goalArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.addData("Encoder Arm", "Finished in %.2f s/%d", turningTime.seconds(), timeoutS);
        telemetry.update();

    }
}
