package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

public class ComputerVision {
    /* Variables Necessary for Computer Vision, they will be different each year */
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad"; //
    private static final String LABEL_SECOND_ELEMENT = "Single";
    private static final String VUFORIA_KEY = Secret.vKey; /* you'll have to get your own developer key,
     It should look like the one below, store it as a String */
    //"ATwu5Uf/////AAABmW6EkCcqK08LuKL127zX3owl9yKuVGftG+fZJh1x5DAia5zv6SAip+KJqd+P9DUA1NhLaqowDt2iraVhn4mt8C2ZJXuQd5XZiQ9Ihx6s1dvI0W+/bn0YBP2rry4keEQC2C2NVmfAtXsV+sqYVGfkHUkQ1n00L/ndbQKPE0JMWFo+sX5683ght4wyaTjjKbjSxL8gNVoCaP9ndedm+tsPdfKcSj8urqhgSOtNlAk4cTzVx1buSG33tNKy4a+JuULWYFbRngrEqPd/6MzIXFAxpMvfu0dcpCFal+VYPX/upaL4GkEEV2gfJ4xtSEqIBgnIFM8WtWwC51P2yhtGjDoqXChw0MvLDjcCTxqYT3MIjFE1";

    //vuforia is the variable we will use to store our instance of the Vuforia localization engine
    private VuforiaLocalizer vuforia;
    //tfod is the variable we will use to store our instance of the TensorFlow Object Detection Engine
    public TFObjectDetector tfod;


    public ComputerVision(){

    }
    public void initCompuVis(HardwareMap hwMap) {
        /*
         *  A combination of the initVuforia() and initTfod() methods from the Tensorflow tutorial
         *  TODO: include link
         *  TODO: verify that it... works
         */
        /* ** initVuforia() ** */
        VuforiaLocalizer.Parameters vuforiaParameters = new VuforiaLocalizer.Parameters();
        vuforiaParameters.vuforiaLicenseKey = VUFORIA_KEY;
        vuforiaParameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        //Change cameraDirection if you use the 'selfie' camera
        vuforia = ClassFactory.getInstance().createVuforia(vuforiaParameters); //Instantiate the Vuforia engine
        /* **************** */

        /* ** initTfod() ** */
        int tfodMonitorViewId = hwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
        /* **************** */
    }

    /* Useful properties of tfod.getUpdatedRecognitions()

    * String label = getLabel();
    * float left = getLeft();
    * float top = getTop();
    * float right = getRight();
    * float bottom = getBottom();
    * float confidence = getConfidence();
    */

    public List<Recognition> tfodList(){
        return tfod.getUpdatedRecognitions();
    }
}
