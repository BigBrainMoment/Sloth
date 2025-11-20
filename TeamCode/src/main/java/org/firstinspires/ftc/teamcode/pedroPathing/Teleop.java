package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

//gamepad 1 is driver
//gamepad 2 is operator
@TeleOp
@Configurable
public class Teleop extends LinearOpMode {
    int count = 0;
    double setSpeed = 1;

    @Override
    public void runOpMode() throws InterruptedException {
// Declaring motors

        CRServo ls, rs;
        ls = hardwareMap.get(CRServo.class,"leftServo");
        rs = hardwareMap.get(CRServo.class,"rightServo");

        DcMotor FL, FR, BL, BR, fly;
        FL = hardwareMap.get(DcMotor.class, "frontLeft");
        FR = hardwareMap.get(DcMotor.class, "frontRight");
        BL = hardwareMap.get(DcMotor.class, "backLeft");
        BR = hardwareMap.get(DcMotor.class, "backRight");

        fly = hardwareMap.get(DcMotor.class,"flywheel");

        FR.setDirection(DcMotorSimple.Direction.REVERSE);
        BR.setDirection(DcMotorSimple.Direction.REVERSE);
        fly.setDirection(DcMotorSimple.Direction.REVERSE);
        ls.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        if (isStopRequested()) return;

//The Drivetrain Code
        while (opModeIsActive()) {

            double y = gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);


            double frontLeftPower = 0.5 * ((y + x + rx) / denominator);
            double backLeftPower = 0.5 * ((y - x + rx) / denominator);
            double frontRightPower = 0.5 * ((y - x - rx) / denominator);
            double backRightPower = 0.5 * ((y + x - rx) / denominator);

            //teleop goes on here  -- where you tell the robot exactly what to do during the program
            FL.setPower(frontLeftPower * setSpeed);
            BL.setPower(backLeftPower * setSpeed);
            FR.setPower(frontRightPower * setSpeed);
            BR.setPower(backRightPower * setSpeed);

            //speed settings
            if(gamepad1.dpad_up){
                setSpeed = 1;
            }
            if(gamepad1.dpad_right){
                setSpeed = 1.25;
            }
            if(gamepad1.dpad_down){
                setSpeed = 2;
            }
            if(gamepad1.dpad_left){
                setSpeed = 2.25;
            }
/*

// Flywheel/Launch code (made continuous so we don't have to hold down)
            //track how many times pressed
            if(gamepad2.leftBumperWasPressed()){
                count++;
            }

            //if pressed twice/divisible by 2 it will stop motor, pressed once makes it go, etc
            if(count % 2 != 0){
                fly.setPower(0);

            }else if(count == 0){
                fly.setPower(0);
            }else{
                fly.setPower(.70);
            }

            // preess

            if(gamepad2.leftBumperWasPressed()){
                fly.setPower(.61);
            }
            if(gamepad2.leftBumperWasReleased()){
                fly.setPower(0);
            }

*/




            //fly.setPower(.61);

            //Servo push into flywheel
            if(gamepad2.aWasPressed()){
                rs.setPower(.50);
                ls.setPower(.50);
            }
            if(gamepad2.aWasReleased()){
                rs.setPower(-.50);
                ls.setPower(-.50);
            }



        }
    }
}
