package org.firstinspires.ftc.teamcode.pedroPathing; // make sure this aligns with class location
import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.drawOnlyCurrent;
import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;
import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.telemetryM;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "NOOOOOOO" ,group = "Examples")

public class FarLaunchBlue extends OpMode {
    CRServo ls, rs;

    /// M   E   T   H   O   D   S ///
    private static ElapsedTime timer = new ElapsedTime();
    public void push(double milliseconds){
        // 400 = 1 push
        rs.setPower(.67);
        ls.setPower(.67);
        while (timer.milliseconds() <= milliseconds){}
        rs.setPower(0);
        ls.setPower(0);
    }

    public void shootThree(){
        push(400);
        push(400);
        push(400);
    }


    ///     ///     ///     ///     ///     ///     ///     ///     ///
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;



    private final Pose startPoint = new Pose(88,8,Math.toDegrees(90));
    private final Pose Path1 = new Pose(88,21.7,Math.toDegrees(123));
    private final Pose Path2 = new Pose(100,35.5,Math.toDegrees(0));
    private final Pose Path3 = new Pose(135,35.5,Math.toDegrees(0));
    private final Pose Path4 = new Pose( 99.9,42.1,Math.toDegrees(138));
    private final Pose Path5 = new Pose(100,59.9,Math.toDegrees(0));
    private final Pose Path6 = new Pose(128,59.7,Math.toDegrees(0));
    private final Pose Path7 = new Pose(99,75,Math.toDegrees(150));
    private final Pose Path8 = new Pose(105.5,84,Math.toDegrees(0));
    private final Pose Path9 = new Pose(131,84,Math.toDegrees(0));
    private final Pose Path10 = new Pose(120,90,Math.toDegrees(156));
    private final Pose Path11 = new Pose(87,40,Math.toDegrees(90));

    private final Pose startPose = new Pose(120.2, 125, Math.toDegrees(45));
    // Start Pose of robot, this is red goal, 4back until touching white line, parellel to deposit (36.5 Degrees).
    private final Pose scorePose = new Pose(74, 83, Math.toDegrees(43));
    // Scoring Pose of our robot. It is facing the goal at a 43 degree angle.5
    private final Pose linePointPose = new Pose(37, 121, Math.toDegrees(0));
    //the parking of the Red score place
    ///     ///     ///     ///     ///     ///     ///     ///     ///
    private Path score, park;
    public void buildPaths() {
        score = new Path(new BezierLine(startPose, scorePose));
        score.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        park = new Path(new BezierLine(scorePose, linePointPose));
        park.setLinearHeadingInterpolation(scorePose.getHeading(), linePointPose.getHeading());
    }

    ///     ///     ///     ///     ///     ///     ///     ///     ///
    public void autonomousPathUpdate() {
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
        fly.setPower(.67);

        ///     ///     ///     ///     ///     ///     ///     ///     ///


        switch (pathState) {
            case 0:
                follower.followPath(score, true);
                if (!follower.isBusy()) {

                    push(1600);

                }
                setPathState(1);

                break;
            case 1:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {
                    /* Score Preload */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(park, true);
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {
                    /* Set the state to a Case we won't use or define, so it just stops running an new paths */
                    setPathState(-1);
                }
                break;
        }
    }











    /** These change the states of the paths and actions. It will also reset the timers of the individual switches **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
        ///
    }
    /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /** This method is called once at the init of the OpMode. **/
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();


        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {}

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);

        follower.activateAllPIDFs(); ///////////THIS NOT HERE; I PUT IT

    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {}
}
