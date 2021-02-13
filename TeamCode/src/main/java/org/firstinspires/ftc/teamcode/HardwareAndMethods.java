
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

//@Disabled
public class HardwareAndMethods {
    //CV stuff copy/pasted from ConceptTensorFlowObjectDetectionEngine
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite"; //TODO: Replace with each year's new file & names
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY =
            "ATwu5Uf/////AAABmW6EkCcqK08LuKL127zX3owl9yKuVGftG+fZJh1x5DAia5zv6SAip+KJqd+P9DUA1NhLaqowDt2iraVhn4mt8C2ZJXuQd5XZiQ9Ihx6s1dvI0W+/bn0YBP2rry4keEQC2C2NVmfAtXsV+sqYVGfkHUkQ1n00L/ndbQKPE0JMWFo+sX5683ght4wyaTjjKbjSxL8gNVoCaP9ndedm+tsPdfKcSj8urqhgSOtNlAk4cTzVx1buSG33tNKy4a+JuULWYFbRngrEqPd/6MzIXFAxpMvfu0dcpCFal+VYPX/upaL4GkEEV2gfJ4xtSEqIBgnIFM8WtWwC51P2yhtGjDoqXChw0MvLDjcCTxqYT3MIjFE1";

    //vuforia is the variable we will use to store our instance of the Vuforia localization engine
    private VuforiaLocalizer vuforia;

    //tfod is the variable we will use to store our instance of the TensorFlow Object Detection Engine
    public TFObjectDetector tfod;

    //Declares variables
    //Motors, just the wheels
    //DcMotor motorName = null;
    DcMotor leftFront = null;
    DcMotor leftBack = null;
    DcMotor rightFront = null;
    DcMotor rightBack = null;
    //The speed at which the motors will turn
    public float speedMod = 1f;

    //Servos
    //Servo servoName = null;

    //Sensors
    DcMotor forwardEncoder = null;
    DcMotor sidewaysEncoder = null;
    BNO055IMU imu; // https://learn.adafruit.com/adafruit-bno055-absolute-orientation-sensor/overview ??
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters(); //leaving uncommented because I'm not sure why it's here
    DistanceSensor distance = null;
    //ColorSensor colorSensorName = null;
    //float hsvValuesSensor[] = {0F, 0F, 0F}; //Color sensor Hue Saturation Value;; one for each ColorSensor
    //final double COLOR_SCALE_FACTOR = 255;

  HardwareMap hwMap = null;

    private ElapsedTime runtime = new ElapsedTime();
    private boolean firstMove = true;
    public HardwareAndMethods(){
        //Set up parameters for imu
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
//        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
    }
    public void init(HardwareMap ahwMap) {
//        hwMap = ahwMap;

        // Initialize the hardware variables
        //name = hwMap.get(Type.class "deviceName");

        //Motors; declared above;
        leftFront  = hwMap.get(DcMotor.class, "leftFront");
        leftBack = hwMap.get(DcMotor.class, "leftBack");
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        rightBack = hwMap.get(DcMotor.class, "rightBack");


        //Servos
        //exampleName = hwMap.get(Servo.class, "exampleName");


        //Sensors
        imu = hwMap.get(BNO055IMU.class, "imu");
        //colorSensor = hwMap.get(ColorSensor.class, "colorSensor");
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

    public float getImuAngle(){
        Orientation orientation = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return orientation.firstAngle;
    }

    public String getOdometry(){ //for telemetry
        return "x: " + sidewaysEncoder.getCurrentPosition() + ", y: " + forwardEncoder.getCurrentPosition();
    }

    public boolean wheelsAreBusy(){
        return leftFront.isBusy() || leftBack.isBusy() || rightFront.isBusy() || rightBack.isBusy();
    }


    //CV Stuff, maybe move to another class?
    public void initCompuVis() {
        //initVuforia()
        VuforiaLocalizer.Parameters vuforiaParameters = new VuforiaLocalizer.Parameters();

        vuforiaParameters.vuforiaLicenseKey = VUFORIA_KEY;
        vuforiaParameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(vuforiaParameters);

        //initTfod()
        int tfodMonitorViewId = hwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);

    }

    public void runTfod(){
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {

            // step through the list of recognitions and display boundary info.
            //int i = 0;
            /*for (Recognition recognition : updatedRecognitions) {
                String label = recognition.getLabel();
                float left = recognition.getLeft();
                float top = recognition.getTop();
                float right = recognition.getRight();
                float bottom = recognition.getBottom();
                float confidence = recognition.getConfidence();
            }*/
        }
    }

}


