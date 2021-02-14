
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

        /* Initialize the hardware variables | name = hwMap.get(Type.class "deviceName"); */ //TODO:Wiki-fy

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
        return (input - inputMin) / (inputMax - inputMin) * (outputMax - outputMin) + outputMin;
    }


    /* For documentation on these methods, see:
     * https://github.com/wataugarobotics/Team5881_Example/wiki/HardwareAndMethods-Class#getter-methods
     */
    public float getImuAngle(){
        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return orientation.firstAngle;
    }

    public String getOdometryString(){
        return "x: " + sidewaysEncoder.getCurrentPosition() + ", y: " + forwardEncoder.getCurrentPosition();
    }
    public int getOdometryX(){
        return sidewaysEncoder.getCurrentPosition();
    }
    public int getOdometryY(){
        return forwardEncoder.getCurrentPosition();
    }

    public boolean getWheelsAreBusy(){
        return leftFront.isBusy() || leftBack.isBusy() || rightFront.isBusy() || rightBack.isBusy();
    }


}


