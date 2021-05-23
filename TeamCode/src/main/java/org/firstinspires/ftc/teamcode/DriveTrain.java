package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//In future years, this class should be instead called Hardware, and include all hardware
//components.
public class DriveTrain extends LinearOpMode {
    protected DcMotor frontLeft, backLeft, frontRight, backRight;
    private ElapsedTime runtime = new ElapsedTime();

    public boolean isBlueAlliance() { return true; } //Set to false if red alliance

    protected static final double COUNTS_PER_MOTOR_REV = 1680;    // eg: TETRIX Motor Encoder
    private static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    private static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    private static final double COUNTS_PER_INCH = COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION /
            (WHEEL_DIAMETER_INCHES * Math.PI);

    @Override
    public void runOpMode() { }

    public void driveTrainSetup() {
        //Initialize motors and set directions
        if (isBlueAlliance()) {
            frontLeft = hardwareMap.dcMotor.get("Fl");
            backLeft = hardwareMap.dcMotor.get("Bl");
            frontRight = hardwareMap.dcMotor.get("Fr");
            backRight = hardwareMap.dcMotor.get("Br");

            frontLeft.setDirection(DcMotor.Direction.REVERSE);
            backLeft.setDirection(DcMotor.Direction.REVERSE);
            frontRight.setDirection(DcMotor.Direction.FORWARD);
            backRight.setDirection(DcMotor.Direction.FORWARD);
        } else { //Mirror image for red alliance
            frontRight = hardwareMap.dcMotor.get("Fl");
            backRight = hardwareMap.dcMotor.get("Bl");
            frontLeft = hardwareMap.dcMotor.get("Fr");
            backLeft = hardwareMap.dcMotor.get("Br");

            frontRight.setDirection(DcMotor.Direction.REVERSE);
            backRight.setDirection(DcMotor.Direction.REVERSE);
            frontLeft.setDirection(DcMotor.Direction.FORWARD);
            backLeft.setDirection(DcMotor.Direction.FORWARD);
        }
        //Set motors to brake whenever they are stopped
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void rotateClockwise(double power) {
        frontLeft.setPower(power);
        backLeft.setPower(power);
        frontRight.setPower(-power);
        backRight.setPower(-power);
    }

    public void strafe(double forwardLeftPower, double forwardRightPower) {
        frontLeft.setPower(forwardRightPower);
        backLeft.setPower(forwardLeftPower);
        frontRight.setPower(forwardLeftPower);
        backRight.setPower(forwardRightPower);
    }
    public void driveForward(final double power) {
        strafe(power, power);
    }
    public void strafeRight(final double power) {
        strafe(-power, power);
    }

    public void tankControl(double maxPower) { // 0 < maxPower <= 1
        double leftPower = -gamepad1.left_stick_y * maxPower;
        double rightPower = -gamepad1.right_stick_y * maxPower;
        double strafePower = gamepad1.right_stick_x * maxPower;
        //double strafePower = (gamepad1.right_trigger - gamepad1.left_trigger) * maxPower; //positive is to the right
        
        double strafePowerLimit = Math.min(1 - Math.abs(rightPower) , 1 - Math.abs(leftPower));
        strafePower = Range.clip(strafePower, -strafePowerLimit, strafePowerLimit);

        // This will set each motor to a power between -1 and +1 such that the equation for
        // holonomic wheels works.
        frontLeft.setPower(leftPower  + strafePower);
        backLeft.setPower(leftPower  - strafePower);
        frontRight.setPower(rightPower - strafePower);
        backRight.setPower(rightPower + strafePower);
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

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            final int newFRTarget = frontRight.getCurrentPosition() + (int) (frInches * COUNTS_PER_INCH);
            final int newFLTarget = frontLeft.getCurrentPosition() + (int) (flInches * COUNTS_PER_INCH);
            final int newBLTarget = backLeft.getCurrentPosition() + (int) (blInches * COUNTS_PER_INCH);
            final int newBRTarget = backRight.getCurrentPosition() + (int) (brInches * COUNTS_PER_INCH);

            frontRight.setTargetPosition(newFRTarget);
            frontLeft.setTargetPosition(newFLTarget);
            backLeft.setTargetPosition(newBLTarget);
            backRight.setTargetPosition(newBRTarget);

            // Turn On RUN_TO_POSITION
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            //Determine wheel powers
            //Power for each wheel is proportional to the maximum power and distance travelled
            final double maxInches = Math.max( Math.max(Math.abs(frInches), Math.abs(flInches)),
                                         Math.max(Math.abs(brInches), Math.abs(blInches)) );
            final double powerFR = maxPower * frInches / maxInches;
            final double powerFL = maxPower * flInches / maxInches;
            final double powerBR = maxPower * brInches / maxInches;
            final double powerBL = maxPower * blInches / maxInches;

            // reset the timeout time and start motion.
            runtime.reset();

            frontRight.setPower(Math.abs(powerFR));
            frontLeft.setPower(Math.abs(powerFL));
            backLeft.setPower(Math.abs(powerBL));
            backRight.setPower(Math.abs(powerBR));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER/ANY motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH/ALL motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() && runtime.seconds() < timeoutS) {
                if (!(frontRight.isBusy() && frontLeft.isBusy() && backLeft.isBusy() && backRight.isBusy())) {
                    break;
                }
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
            frontRight.setPower(0);
            frontLeft.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(0);

            // Turn off RUN_TO_POSITION
            frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }
    
}
