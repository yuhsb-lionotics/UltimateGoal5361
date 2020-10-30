package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class DriveTrain extends LinearOpMode {
    protected DcMotor fl,bl,fr,br;
    private ElapsedTime runtime = new ElapsedTime();

    public boolean getIsBlueAlliance() {return true;} //Set to false if red alliance

    private static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    private static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    private static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    @Override
    public void runOpMode() {

    }

    public void driveTrainSetup(){
        if(getIsBlueAlliance()) {
            fl = hardwareMap.dcMotor.get("Fl");
            bl = hardwareMap.dcMotor.get("Bl");
            fr = hardwareMap.dcMotor.get("Fr");
            br = hardwareMap.dcMotor.get("Br");

            //fl.setDirection(DcMotor.Direction.FORWARD);
            //bl.setDirection(DcMotor.Direction.FORWARD);
            //fr.setDirection(DcMotor.Direction.FORWARD);
            //br.setDirection(DcMotor.Direction.FORWARD);
        } else {
            fr = hardwareMap.dcMotor.get("Fl");
            br = hardwareMap.dcMotor.get("Bl");
            fl = hardwareMap.dcMotor.get("Fr");
            bl = hardwareMap.dcMotor.get("Br");

            //fr.setDirection(DcMotor.Direction.FORWARD);
            //br.setDirection(DcMotor.Direction.FORWARD);
            //fl.setDirection(DcMotor.Direction.FORWARD);
            //bl.setDirection(DcMotor.Direction.FORWARD);
        }
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

    //Add two more parameters, so there's one for each wheel. Consider making private and allow
    //access only through a methods for strafing vertically, horizontally, or rotating about
    //the center of the robot.
    protected void encoderDrive(double speed,
                                double leftInches, double rightInches,
                                double timeoutS) { // middle inputs are how many inches to travel
        int newFRTarget;
        int newFLTarget;
        int newBLTarget;
        int newBRTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newFRTarget = fr.getCurrentPosition()     + (int) (rightInches   * COUNTS_PER_INCH);
            newFLTarget = fl.getCurrentPosition()     + (int) (leftInches  * COUNTS_PER_INCH);
            newBLTarget = bl.getCurrentPosition()     + (int) (leftInches   * COUNTS_PER_INCH);
            newBRTarget = br.getCurrentPosition()     + (int) (rightInches  * COUNTS_PER_INCH);

            fr.setTargetPosition(newFRTarget);
            fl.setTargetPosition(newFLTarget);
            bl.setTargetPosition(newBLTarget);
            br.setTargetPosition(newBRTarget);

            // Turn On RUN_TO_POSITION
            fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            fr.setPower(Math.abs(speed));
            fl.setPower(Math.abs(speed));
            bl.setPower(Math.abs(speed));
            br.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (fr.isBusy() && fl.isBusy() && bl.isBusy() && br.isBusy()) ) {

                // Display it for the driver.
                /* telemetry.addData("Path1", "Running to %7d :%7d :%7d :%7d :%7d",
                        newFRTarget, newFLTarget, newBLTarget, newFRTarget, newSMTarget);
                telemetry.addData("Path2", "Running at %7d :%7d :%7d :%7d :%7d",
                        motorFR.getCurrentPosition(),
                        motorFL.getCurrentPosition(),
                        motorBL.getCurrentPosition(),
                        motorBR.getCurrentPosition(),
						strafeMotor.getCurrentPosition()); */
            }

            telemetry.addData("Encoder Drive", "Finished in %.2f s/%f", runtime.seconds(), timeoutS);
            telemetry.update();

            // Stop all motion;
            fr.setPower(0);
            fl.setPower(0);
            bl.setPower(0);
            br.setPower(0);

            // Turn off RUN_TO_POSITION
            fr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            fl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            bl.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            br.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    
}
