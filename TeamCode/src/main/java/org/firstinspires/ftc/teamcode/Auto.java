package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Autonomous")
public class Auto extends DriveTrain {
    DcMotor leftLauncherWheel, rightLauncherWheel, arm;
    CRServo conveyorBelt;

    @Override
    public void runOpMode() {
        setup();
        telemetry.addData("Status:","Initialized");
        telemetry.update();
        waitForStart();
        telemetry.addData("Status:", "Running");
        telemetry.update();
        //driving commands here
        encoderDriveForward(0.5,78,14);
        encoderDriveForward(0.5,-7,10);
        /* encoderDriveForward(0.7,42,10);
        char targetSquare=detectRings();
        switch(targetSquare) {
            case 'A':
                break;
        }*/

    }
    private void setup(){
        telemetry.addData("Status", "Initializing");
        telemetry.update();
        driveTrainSetup();
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
    public void flRotate(double power){
        fr.setPower(-power/2);
        bl.setPower(power/2);
        br.setPower(-power);
        sleep(10000);
        fr.setPower(0);
        bl.setPower(0);
        br.setPower(0);
    }

    public char detectRings() {
        return 'A';
    }
}


