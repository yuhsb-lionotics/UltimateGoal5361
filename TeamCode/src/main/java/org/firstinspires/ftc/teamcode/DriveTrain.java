package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class DriveTrain {
    public DcMotor Fl,Bl,Fr,Br;

    public DriveTrain(){}
    public void setup(HardwareMap ahwMap){
        HardwareMap hwMap = ahwMap;
        Fl = hwMap.dcMotor.get("Fl");
        Bl = hwMap.dcMotor.get("Bl");
        Fr = hwMap.dcMotor.get("Fr");
        Br = hwMap.dcMotor.get("Br");
    }
}
