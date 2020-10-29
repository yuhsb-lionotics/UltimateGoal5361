package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Red Auto")
public class RedAuto extends Auto {
    @Override
    public boolean getIsBlueAlliance() {
        return false;
    }
}
