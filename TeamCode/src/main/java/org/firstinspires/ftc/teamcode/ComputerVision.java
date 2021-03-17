/*
    This file is part of Team5881_Example.

    Team5881_Example is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Team5881_Example is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 */

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
     It should look somewhat like the one below, store it as a String */
    //"ATwu5Uf/////ACABmW6ElCcqK0luk8L127zR1owl9yKuVGftG+fZJh1x5DAiaV5z6SAiP+LJQd+P9DcA1NhLaSowDt2iraVhn4mt8C2ZJXuQk5XxiQ9Ihx6s1dvI0W+/bn0YBP2rry4keEQC2C2NVmfAtXsV+sqYVGfkHUkQ1n00L/ndbQKPE0JMWFo+sX5683ght4wyaTjjKbjSxL8gNVoCaP9ndedm+tsPdfKcSj8urqhgSOtNlAk4cTzVx1buSG33tNKy4a+JuULWYFbRngrEqPd/6MzIXFAxpMvfu0dcpCFal+VYPX/upaL4GkEEV2gfJ4xtSEqIBgnIFM8WtWwC51P2yhtGjDoqXCSEqIBgnIFM8WtsdfsMvLDjcCT1qYT3MIjFE1";

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

    /* Useful properties of tfod.getUpdatedRecognitions() TODO:move to wiki perhaps?
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
