package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.DogeCVDetector;
import com.disnodeteam.dogecv.filters.DogeCVColorFilter;
import com.disnodeteam.dogecv.filters.HSVColorFilter;
import com.disnodeteam.dogecv.filters.HSVRangeFilter;
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

// Created by Victo on 9/17/2018.

public class BlackAlignDetector extends DogeCVDetector {

    // Defining Mats to be used.
    private Mat displayMat = new Mat(); // Display debug info to the screen (this is what is returned)
    private Mat workingMat = new Mat(); // Used for preprocessing and working with (blurring as an example)
    private Mat maskBlack = new Mat(); // Black Mask returned by color filter
    private Mat hierarchy  = new Mat(); // hierarchy used by coutnours

    // Results of the detector
    private boolean found    = false; // Is the black mineral found
    private boolean aligned  = false; // Is the black mineral aligned
    private double  blackXPos = 0;     // X Position (in pixels) of the black element

    // Detector settings
    public boolean debugAlignment = true; // Show debug lines to show alignment settings
    public double alignPosOffset  = 0;    // How far from center frame is aligned
    public double alignSize       = 100;  // How wide is the margin of error for alignment

    public DogeCV.AreaScoringMethod areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Setting to decide to use MaxAreaScorer or PerfectAreaScorer


    //Create the default filters and scorers
    public DogeCVColorFilter blackFilter = new HSVColorFilter(new Scalar(0,0,0), new Scalar(30,30,30)); // Black is (0,0,0); Range is (,,)
//    public DogeCVColorFilter blackFilter = new HSVRangeFilter(new Scalar(0,0,0), new Scalar(30,30,30)); // Lower is (0,0,0); Upper is (,,)

    public RatioScorer       ratioScorer       = new RatioScorer(1.0, 3);          // Used to find perfect squares
    public MaxAreaScorer     maxAreaScorer     = new MaxAreaScorer( 0.01);                    // Used to find largest objects
    public PerfectAreaScorer perfectAreaScorer = new PerfectAreaScorer(5000,0.05); // Used to find objects near a tuned area value

    /**
     * Simple constructor
     */
    public BlackAlignDetector() {
        super();
        detectorName = "Black Align Detector"; // Set the detector name
    }


    @Override
    public Mat process(Mat input) {

        // Copy the input mat to our working mats, then release it for memory
        input.copyTo(displayMat);
        input.copyTo(workingMat);
        input.release();


        //Preprocess the working Mat (blur it then apply a black filter)
        Imgproc.GaussianBlur(workingMat,workingMat,new Size(5,5),0);
        blackFilter.process(workingMat.clone(),maskBlack);

        //Find contours of the black mask and draw them to the display mat for viewing

        List<MatOfPoint> contoursBlack = new ArrayList<>();
        Imgproc.findContours(maskBlack, contoursBlack, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(displayMat,contoursBlack,-1,new Scalar(230,70,70),2);

        // Current result
        Rect bestRect = null;
        double bestDiffrence = Double.MAX_VALUE; // MAX_VALUE since less diffrence = better

        // Loop through the contours and score them, searching for the best result
        for(MatOfPoint cont : contoursBlack){
            double score = calculateScore(cont); // Get the diffrence score using the scoring API

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(cont);
            Imgproc.rectangle(displayMat, rect.tl(), rect.br(), new Scalar(0,0,300),2); // Draw rect

            // If the result is better then the previously tracked one, set this rect as the new best
            if(score < bestDiffrence){
                bestDiffrence = score;
                bestRect = rect;
            }
        }

        // Vars to calculate the alignment logic.
        double alignX    = (getAdjustedSize().width / 2) + alignPosOffset; // Center point in X Pixels
        double alignXMin = alignX - (alignSize / 2); // Min X Pos in pixels
        double alignXMax = alignX +(alignSize / 2); // Max X pos in pixels
        double xPos; // Current Black X Pos

        if(bestRect != null){
            // Show chosen result
            Imgproc.rectangle(displayMat, bestRect.tl(), bestRect.br(), new Scalar(300,0,0),4);
            Imgproc.putText(displayMat, "Chosen", bestRect.tl(),0,1,new Scalar(300,300,300));

            // Set align X pos
            xPos = bestRect.x + (bestRect.width / 2);
            blackXPos = xPos;

            // Draw center point
            Imgproc.circle(displayMat, new Point( xPos, bestRect.y + (bestRect.height / 2)), 5, new Scalar(0,300,0),2);

            // Check if the mineral is aligned
            if(xPos < alignXMax && xPos > alignXMin){
                aligned = true;
            }else{
                aligned = false;
            }

            // Draw Current X
            Imgproc.putText(displayMat,"Current X: " + bestRect.x,new Point(10,getAdjustedSize().height - 10),0,0.5, new Scalar(300,300,300),1);
            found = true;
        }else{
            found = false;
            aligned = false;
        }
        if(debugAlignment){

            //Draw debug alignment info
            if(isFound()){
                Imgproc.line(displayMat,new Point(blackXPos, getAdjustedSize().height), new Point(blackXPos, getAdjustedSize().height - 30),new Scalar(300,300,0), 2);
            }

            Imgproc.line(displayMat,new Point(alignXMin, getAdjustedSize().height), new Point(alignXMin, getAdjustedSize().height - 40),new Scalar(0,300,0), 2);
            Imgproc.line(displayMat,new Point(alignXMax, getAdjustedSize().height), new Point(alignXMax,getAdjustedSize().height - 40),new Scalar(0,300,0), 2);
        }

        //Print result
        Imgproc.putText(displayMat,"Result: " + aligned,new Point(10,getAdjustedSize().height - 30),0,1, new Scalar(300,300,0),1);


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
     * Set the alignment settings for BlackAlign
     * @param offset - How far from center frame (in pixels)
     * @param width - How wide the margin is (in pixels, on each side of offset)
     */
    public void setAlignSettings(int offset, int width){
        alignPosOffset = offset;
        alignSize = width;
    }

    /**
     * Returns if the black element is aligned
     * @return if the black element is alined
     */
    public boolean getAligned(){
        return aligned;
    }

    /**
     * Returns black element last x-position
     * @return last x-position in screen pixels of black element
     */
    public double getXPosition(){
        return blackXPos;
    }

    /**
     * Returns if a black mineral is being tracked/detected
     * @return if a black mineral is being tracked/detected
     */
    public boolean isFound() {
        return found;
    }
}
