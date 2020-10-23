package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveTrain {
    public DcMotor fl,bl,fr,br;

    public DriveTrain(){}
    public void setup(HardwareMap ahwMap){
        HardwareMap hwMap = ahwMap;
        fl = hwMap.dcMotor.get("Fl");
        bl = hwMap.dcMotor.get("Bl");
        fr = hwMap.dcMotor.get("Fr");
        br = hwMap.dcMotor.get("Br");

        //fl.setDirection(DcMotor.Direction.FORWARD);
        //bl.setDirection(DcMotor.Direction.FORWARD);
        //fr.setDirection(DcMotor.Direction.FORWARD);
        //br.setDirection(DcMotor.Direction.FORWARD);
    }

    public void rotateClockwise(double power) {
        fl.setPower(power);
        bl.setPower(power);
        fr.setPower(-power);
        br.setPower(-power);
    }

    public void strafe(double forwardLeftPower, double forwardRightPower) {
        fl.setPower(forwardRightPower);
        bl.setPower(forwardLeftPower);
        fr.setPower(forwardLeftPower);
        br.setPower(forwardRightPower);
    }
    public void driveForward(double power) {
        strafe(power, power);
    }
    public void strafeRight(double power) {
        strafe(-power, power);
    }
    
}
