package org.firstinspires.ftc.teamcode.pedroPathing;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous (name="PreTest", group="Linear OpMode")
public class PreTest extends LinearOpMode {
    DcMotor FL;
    DcMotor FR;
    DcMotor BL;
    DcMotor BR;




    private static ElapsedTime timer = new ElapsedTime();
    public void drive(double milliseconds, float speed) {
        timer.reset();
        FL.setPower(speed);
        FR.setPower(speed);
        BL.setPower(speed);
        BR.setPower(speed);

        while (timer.milliseconds() <= milliseconds){

        }

        FL.setPower(0);
        FR.setPower(0);
        BL.setPower(0);
        BR.setPower(0);

    }

    @Override
    public void runOpMode() throws InterruptedException {
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

        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);





        waitForStart();
        if (isStopRequested()) return;

        ElapsedTime myTimer = new ElapsedTime();
        if (opModeIsActive()) {
            fly.setPower(.57);

//        Run

            drive(1500.0, 0.5F);



            // start counting
            timer.reset();

            for (int i = 0; i <= 3; i++) {
                while (timer.milliseconds() <= 1000) {

                }

                ls.setPower(0.5);
                rs.setPower(0.5);

                timer.reset();
                while (timer.milliseconds() <= 400) {

                }

                // if you got lost we still inside the for loop
                // and we are now turing off the motors
                // and recounting the time
                ls.setPower(0);
                rs.setPower(0);
                // reset timer for the next shot
                drive(500,-0.2F);
            }


            timer.reset();
            FL.setPower(-0.5);
            FR.setPower(0.5);
            BL.setPower(0.5);
            BR.setPower(-0.5);

            while (timer.milliseconds() <= 1500){

            }

            FL.setPower(0);
            FR.setPower(0);
            BL.setPower(0);
            BR.setPower(0);

        }


    }




}