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

@Autonomous(name = "Auto" ,group = "Examples")
@Disabled
public class Auto extends OpMode {
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;


    private final Pose startPose = new Pose(118.4, 129.6, Math.toDegrees(0));
    // Start Pose of robot, this is red goal, 4back until touching white line, parellel to deposit (36.5 Degrees).
    private final Pose scorePose = new Pose(74, 83, Math.toDegrees(0));
    // Scoring Pose of our robot. It is facing the goal at a 43 degree angle.
    private final Pose parkPose = new Pose(37, 121, Math.toDegrees(0));
    //the parking of the Red score place

    private Path direct, park;


    public void buildPaths() {
        direct = new Path(new BezierLine(startPose, scorePose));
        direct.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

        park = new Path(new BezierLine(scorePose, parkPose));
        park.setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading());
    }

/*
    @Override
    public void init() {
        CRServo ls, rs;
        ls = hardwareMap.get(CRServo.class, "leftServo");
        rs = hardwareMap.get(CRServo.class, "rightServo");

        DcMotor fly;


        fly = hardwareMap.get(DcMotor.class, "flywheel");

        fly.setDirection(DcMotorSimple.Direction.REVERSE);



       `Archive oj = new Archive();
        oj.push(-.25);
        oj.fly(68);

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        follower.followPath(direct);

        getRuntime();
        oj.push(.25);
        follower.followPath(park);

    }

*/
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:

                follower.followPath(direct);
                setPathState(1);
                break;
            case 1:

            /* You could check for
            - Follower State: "if(!follower.isBusy()) {}"
            - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
            - Robot Position: "if(follower.getPose().getX() > 36) {}"
            */

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
        telemetryM.update(telemetry);

    }

    /** This method is called continuously after Init while waiting for "play". **/
    @Override
    public void init_loop() {
        telemetryM.update(telemetry);
        follower.update();
        drawOnlyCurrent();
    }

    /** This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);

        follower.activateAllPIDFs();

    }

    /** We do not use this because everything should automatically disable **/
    @Override
    public void stop() {}
}
