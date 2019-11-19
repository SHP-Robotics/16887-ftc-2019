package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.filters.DogeCVColorFilter;
import com.disnodeteam.dogecv.filters.LeviColorFilter;
import com.disnodeteam.dogecv.scoring.MaxAreaScorer;
import com.disnodeteam.dogecv.scoring.PerfectAreaScorer;
import com.disnodeteam.dogecv.scoring.RatioScorer;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

// Created by Victo on 9/10/2018.

public class BlueDetector extends DogeCVDetector {

    // Defining Mats to be used.
    private Mat displayMat = new Mat(); // Display debug info to the screen (this is what is returned)
    private Mat workingMat = new Mat(); // Used for preprocessing and working with (blurring as an example)
    private Mat maskBlue = new Mat(); // Blue Mask returned by color filter
    private Mat hierarchy  = new Mat(); // hierarchy used by coutnours

    // Results of the detector
    private boolean found    = false; // Is the blue mineral found
    private Point   screenPosition = new Point(); // Screen position of the mineral
    private Rect    foundRect = new Rect(); // Found rect

    public DogeCV.AreaScoringMethod areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Setting to decide to use MaxAreaScorer or PerfectAreaScorer

    //Create the default filters and scorers
    public DogeCVColorFilter blueFilter      = new LeviColorFilter(LeviColorFilter.ColorPreset.BLUE); //Default Blue filter

    public RatioScorer       ratioScorer       = new RatioScorer(1.0, 3);          // Used to find perfect squares
    public MaxAreaScorer     maxAreaScorer     = new MaxAreaScorer( 0.01);                    // Used to find largest objects
    public PerfectAreaScorer perfectAreaScorer = new PerfectAreaScorer(5000,0.05); // Used to find objects near a tuned area value

    /**
     * Simple constructor
     */
    public BlueDetector() {
        super();
        detectorName = "Blue Detector"; // Set the detector name
    }


    @Override
    public Mat process(Mat input) {

        // Copy the input mat to our working mats, then release it for memory
        input.copyTo(displayMat);
        input.copyTo(workingMat);
        input.release();


        //Preprocess the working Mat (blur it then apply a blue filter)
        Imgproc.GaussianBlur(workingMat,workingMat,new Size(5,5),0);
        blueFilter.process(workingMat.clone(),maskBlue);

        //Find contours of the bl mask and draw them to the display mat for viewing

        List<MatOfPoint> contoursBlue = new ArrayList<>();
        Imgproc.findContours(maskBlue, contoursBlue, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(displayMat,contoursBlue,-1,new Scalar(230,70,70),2);

        // Current result
        Rect bestRect = null;
        double bestDiffrence = Double.MAX_VALUE; // MAX_VALUE since less diffrence = better

        // Loop through the contours and score them, searching for the best result
        for(MatOfPoint cont : contoursBlue){
            double score = calculateScore(cont); // Get the diffrence score using the scoring API

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(cont);
            Imgproc.rectangle(displayMat, rect.tl(), rect.br(), new Scalar(0,0,255),2); // Draw rect

            // If the result is better then the previously tracked one, set this rect as the new best
            if(score < bestDiffrence){
                bestDiffrence = score;
                bestRect = rect;
            }
        }

        if(bestRect != null){
            // Show chosen result
            Imgproc.rectangle(displayMat, bestRect.tl(), bestRect.br(), new Scalar(255,0,0),4);
            Imgproc.putText(displayMat, "Chosen", bestRect.tl(),0,1,new Scalar(255,255,255));

            screenPosition = new Point(bestRect.x, bestRect.y);
            foundRect = bestRect;
            found = true;
        }else{
            found = false;
        }


        //Print result
        Imgproc.putText(displayMat,"Result: " + screenPosition.x +"/"+screenPosition.y,new Point(10,getAdjustedSize().height - 30),0,1, new Scalar(255,255,0),1);


        return displayMat;

    }

    @Override
    public void useDefaults() {
        addScorer(ratioScorer);

        // Add diffrent scoreres depending on the selected mode
        if(areaScoringMethod == DogeCV.AreaScoringMethod.MAX_AREA){
            addScorer(maxAreaScorer);
        }

        if (areaScoringMethod == DogeCV.AreaScoringMethod.PERFECT_AREA){
            addScorer(perfectAreaScorer);
        }

    }
    /**
     * Returns the blue element's last position in screen pixels
     * @return position in screen pixels
     */
    public Point getScreenPosition(){
        return screenPosition;
    }

    /**
     * Returns the blue element's found rectangle
     * @return blue element rect
     */
    public Rect getFoundRect() {
        return foundRect;
    }

    /**
     * Returns if a blue mineral is being tracked/detected
     * @return if a blue mineral is being tracked/detected
     */
    public boolean isFound() {
        return found;
    }
}
