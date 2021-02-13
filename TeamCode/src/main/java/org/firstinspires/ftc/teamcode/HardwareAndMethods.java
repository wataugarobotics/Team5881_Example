
package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class HardwareAndMethods {
    /* Variables Necessary for Computer Vision, they will be different each year */
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad"; //
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY = Secret.vKey; /*you'll have to get your own developer key,
     It should look like the one below, store it as a String */
    //"ATwu5Uf/////AAABmW6EkCcqK08LuKL127zX3owl9yKuVGftG+fZJh1x5DAia5zv6SAip+KJqd+P9DUA1NhLaqowDt2iraVhn4mt8C2ZJXuQd5XZiQ9Ihx6s1dvI0W+/bn0YBP2rry4keEQC2C2NVmfAtXsV+sqYVGfkHUkQ1n00L/ndbQKPE0JMWFo+sX5683ght4wyaTjjKbjSxL8gNVoCaP9ndedm+tsPdfKcSj8urqhgSOtNlAk4cTzVx1buSG33tNKy4a+JuULWYFbRngrEqPd/6MzIXFAxpMvfu0dcpCFal+VYPX/upaL4GkEEV2gfJ4xtSEqIBgnIFM8WtWwC51P2yhtGjDoqXChw0MvLDjcCTxqYT3MIjFE1";

    //vuforia is the variable we will use to store our instance of the Vuforia localization engine
    private VuforiaLocalizer vuforia;
    //tfod is the variable we will use to store our instance of the TensorFlow Object Detection Engine
    public TFObjectDetector tfod;

/*******************************************************************************************/
    /*
    * The following are variables that let you interface with the hardware of the robot
    * you can change this as you need, but this will work for a very simple robot with:
    * -4 Mecanum wheels
    * -1 Servo
    * -One imu sensor
    * -A forward and sideways encoder (see wiki)
    * -A distance sensor
    * -A color sensor
    * You can copy/paste, comment-out, or delete any of these to create new or get rid of unused
    * sensors or motors.
    *
    * Keep in mind that you have to map all of these to actual hardware by naming the hardware
    * on the phone app.
    */

    //Motors | DcMotor motorName = null;
    DcMotor leftFront = null;
    DcMotor leftBack = null;
    DcMotor rightFront = null;
    DcMotor rightBack = null;
    public float speedMod = 1f; //The speed multiplier for the motors

    //Servos | Servo servoName = null;
    Servo servo1 = null;

    //Sensors
    DcMotor forwardEncoder = null;
    DcMotor sidewaysEncoder = null;

    BNO055IMU imu; //https://learn.adafruit.com/adafruit-bno055-absolute-orientation-sensor/overview
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    DistanceSensor distance = null;

    ColorSensor colorSensor = null;
    float hsvValuesSensor[] = {0F, 0F, 0F}; //Color sensor Hue Saturation Value;; one for each ColorSensor
    final double COLOR_SCALE_FACTOR = 255;

    //HardwareMap hwMap = null;
    private ElapsedTime runtime = new ElapsedTime();

    public HardwareAndMethods(){
        //Set up parameters for imu
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
    }
    public void init(HardwareMap hwMap) {

        // Initialize the hardware variables | name = hwMap.get(Type.class "deviceName");

        //Motors; declared above; TODO:"Initialize variables here?"
        leftFront  = hwMap.get(DcMotor.class, "leftFront");
        leftBack = hwMap.get(DcMotor.class, "leftBack");
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        rightBack = hwMap.get(DcMotor.class, "rightBack");

        //Servos
        servo1 = hwMap.get(Servo.class, "servo1");

        //Sensors
        imu = hwMap.get(BNO055IMU.class, "imu");
        colorSensor = hwMap.get(ColorSensor.class, "colorSensor");
        distance = hwMap.get(DistanceSensor.class, "distance");
        forwardEncoder = hwMap.get(DcMotor.class, "forward");
        sidewaysEncoder = hwMap.get(DcMotor.class, "sideways");

        // Set motor directions
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);

        // Set motor modes
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //does this segment do anything?
        forwardEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sidewaysEncoder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Set imu parameters
        imu.initialize(parameters);
    }


    public void mecanum(float x, float y, float r){
        //TODO: Create wiki page for mecanum
        double leftFrontPower = Range.clip(y + x + r, -1.0, 1.0) / speedMod;
        double leftBackPower = Range.clip(y - x + r, -1.0, 1.0) / speedMod;
        double rightFrontPower = Range.clip(y - x - r, -1.0, 1.0) / speedMod;
        double rightBackPower = Range.clip(y + x - r, -1.0, 1.0) / speedMod;

        // Send calculated power to wheels
        leftFront.setPower(leftFrontPower);
        leftBack.setPower(leftBackPower);
        rightFront.setPower(rightFrontPower);
        rightBack.setPower(rightBackPower);
    }

    public float map(float input, float inputMin, float inputMax, float outputMin, float outputMax){
        /*
        * Simple method to translate a number from one range to another
        * Useful for situations using an analog input (perhaps a joystick or trigger) that returns
        * a value between -1 and 1, and translating that into movement on a larger scale.
        */
        return (input - inputMin) / (inputMax - inputMin) * (outputMax - outputMin) + outputMin;
    }

    public float getImuAngle(){
        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return orientation.firstAngle;
    }

    public String getOdometry(){
        /*
        For telemetry purposes, returns a String "x: ___, y: ___"
        where the value after "x: " is the distance that the sideways encoder has travelled, and
        the value after "y: " is the distance that the forward encoder has travelled.
        */
        return "x: " + sidewaysEncoder.getCurrentPosition() + ", y: " + forwardEncoder.getCurrentPosition();
    }

    public boolean wheelsAreBusy(){
        /*
        * Returns true if any of the wheels are spinning
        */
        return leftFront.isBusy() || leftBack.isBusy() || rightFront.isBusy() || rightBack.isBusy();
    }


    //TODO:CV Stuff, maybe move to another class?


}


