package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class DriveTrain extends LinearOpMode {
    protected DcMotor fl,bl,fr,br;
    private ElapsedTime runtime = new ElapsedTime();

    public boolean getIsBlueAlliance() {return true;} //Set to false if red alliance

    private static final double COUNTS_PER_MOTOR_REV = 1680;    // eg: TETRIX Motor Encoder
    private static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    private static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * Math.PI);

    @Override
    public void runOpMode() {

    }

    public void driveTrainSetup(){
        //Initialize motors and set directions
        if(getIsBlueAlliance()) {
            fl = hardwareMap.dcMotor.get("Fl");
            bl = hardwareMap.dcMotor.get("Bl");
            fr = hardwareMap.dcMotor.get("Fr");
            br = hardwareMap.dcMotor.get("Br");

            fl.setDirection(DcMotor.Direction.REVERSE);
            bl.setDirection(DcMotor.Direction.REVERSE);
            fr.setDirection(DcMotor.Direction.FORWARD);
            br.setDirection(DcMotor.Direction.FORWARD);
        } else { //Mirror image for red alliance
            fr = hardwareMap.dcMotor.get("Fl");
            br = hardwareMap.dcMotor.get("Bl");
            fl = hardwareMap.dcMotor.get("Fr");
            bl = hardwareMap.dcMotor.get("Br");

            fr.setDirection(DcMotor.Direction.REVERSE);
            br.setDirection(DcMotor.Direction.REVERSE);
            fl.setDirection(DcMotor.Direction.FORWARD);
            bl.setDirection(DcMotor.Direction.FORWARD);
        }
        //Set motors to brake whenever they are stopped
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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

    public void tankControl(double maxPower) { // 0 < maxPower <= 1
        double leftPower = -gamepad1.left_stick_y * maxPower;
        double rightPower = -gamepad1.right_stick_y * maxPower;
        double strafePower = (gamepad1.right_trigger - gamepad1.left_trigger) * maxPower; //positive is to the right
        
        double strafePowerLimit = Math.min(1 - Math.abs(rightPower) , 1 - Math.abs(leftPower));
        strafePower = Range.clip(strafePower, -strafePowerLimit, strafePowerLimit);

        // This will set each motor to a power between -1 and +1 such that the equation for
        // holonomic wheels works.
        fl.setPower(leftPower  + strafePower);
        bl.setPower(leftPower  - strafePower);
        fr.setPower(rightPower - strafePower);
        br.setPower(rightPower + strafePower);
    }
    public void encoderStrafe(double maxPower, //0 <= maxPower <= 1
                              double forwardLeftInches, double forwardRightInches, // + or -
                              double timeoutS) {
        encoderDrive(maxPower, forwardLeftInches, forwardRightInches, forwardRightInches, forwardLeftInches, timeoutS);
    }
    public void encoderDriveForward(double power, double inches, double timeoutS) {
        encoderStrafe(power, inches/Math.sqrt(2), inches/Math.sqrt(2), timeoutS);
    }

    //Set each motor to drive a certain distance.
    //maxPower is the greatest absolute value of the power for any of the motors.
    //frInches, etc. can be positive or negative, but not 0.
    //timeoutS is the maximum number of seconds to run the OpMode before a hard stop.
    //The point is to avoid an infinite loop.
    //It should be much higher than the actual length of time it should take, e.g. 10.
    protected void encoderDrive(double maxPower, // 0 < maxPower <= 1
                                double frInches, double flInches, double brInches, double blInches, // + or -
                                double timeoutS) {
        int newFRTarget;
        int newFLTarget;
        int newBLTarget;
        int newBRTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newFRTarget = fr.getCurrentPosition()     + (int) (frInches * COUNTS_PER_INCH);
            newFLTarget = fl.getCurrentPosition()     + (int) (flInches * COUNTS_PER_INCH);
            newBLTarget = bl.getCurrentPosition()     + (int) (blInches * COUNTS_PER_INCH);
            newBRTarget = br.getCurrentPosition()     + (int) (brInches * COUNTS_PER_INCH);

            fr.setTargetPosition(newFRTarget);
            fl.setTargetPosition(newFLTarget);
            bl.setTargetPosition(newBLTarget);
            br.setTargetPosition(newBRTarget);

            // Turn On RUN_TO_POSITION
            fr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            fl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            bl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            br.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //Determine wheel powers
            //Power for each wheel is proportional to the maximum power and distance travelled
            double maxInches = Math.max( Math.max(Math.abs(frInches), Math.abs(flInches)) ,
                                         Math.max(Math.abs(brInches), Math.abs(blInches)) );
            double powerFR = maxPower * frInches / maxInches;
            double powerFL = maxPower * flInches / maxInches;
            double powerBR = maxPower * brInches / maxInches;
            double powerBL = maxPower * blInches / maxInches;

            // reset the timeout time and start motion.
            runtime.reset();
            fr.setPower(Math.abs(powerFR));
            fl.setPower(Math.abs(powerFL));
            bl.setPower(Math.abs(powerBL));
            br.setPower(Math.abs(powerBR));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER/ANY motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH/ALL motors have finished their moves before the robot continues
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
            //Display the time elapsed
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
