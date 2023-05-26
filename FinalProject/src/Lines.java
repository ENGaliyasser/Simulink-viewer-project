import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Circle;

public class Lines {
    // Constants for XML tags
    private static final String LINE_START_TAG = "  <Line>";
    private static final String LINE_END_TAG = "  </Line>";
    private static final String SRC_TAG = "    <P Name=\"Src\">";
    private static final String DST_TAG = "    <P Name=\"Dst\">";
    private static final String BRANCH_TAG = "    <Branch>";

    // Method to draw lines on a JavaFX Pane
    public static void linesDrawer(String fileString, Pane pane, ArrayList<Block> blocks) {
        try (Scanner scanner = new Scanner(fileString)) {
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.equals(LINE_START_TAG)) {
                    int outID = 0;
                    ArrayList<Integer> inIDs = new ArrayList<>();
                    ArrayList<Integer> inPorts = new ArrayList<>();
                    boolean branched = false;
                    while (!line.equals(LINE_END_TAG)) {
                        if (line.contains(SRC_TAG)) {
                            // Extract the source ID from the Src tag
                            int startIndex = line.indexOf('>') + 1;
                            int endIndex = line.indexOf('#', startIndex);
                            String no = line.substring(startIndex, endIndex);
                            outID = Integer.parseInt(no);
                        }
                        if (line.contains(DST_TAG)) {
                            // Extract the destination ID and port from the Dst tag
                            int startIndex = line.indexOf('>') + 1;
                            int endIndex = line.indexOf('#', startIndex);
                            String no = line.substring(startIndex, endIndex);
                            inIDs.add(Integer.parseInt(no));
                            startIndex = line.indexOf(':', endIndex) + 1;
                            endIndex = line.indexOf('<', startIndex);
                            no = line.substring(startIndex, endIndex);
                            inPorts.add(Integer.parseInt(no));
                        }
                        if (line.contains(BRANCH_TAG)) {
                            // Set the branched flag if the Branch tag is present
                            branched = true;
                        }
                        line = scanner.nextLine();
                    }
                    Point2D startPoint = determineStartPoint(outID, blocks);

                    ArrayList<Point2D> endPoints = determineEndPoints(inIDs, inPorts, blocks);


                    if(branched){
                        // Draw branched lines
                        Point2D branchPoint = null;

                        if (startPoint != null) {

                            branchPoint = calcBranchedPoint(startPoint, endPoints);

                        }

                        BranchedLineDraw(pane, branchPoint,startPoint,endPoints);

                    }

                    else {
                        // Draw arrow lines
                        arrow(pane, startPoint, endPoints);

                    }

                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }
    // Method to draw arrow lines
    private static void arrow(Pane pane, Point2D startPoint, ArrayList<Point2D> endPoints) {

        Arrow a=new Arrow(startPoint, endPoints.get(0));

        a.draw(pane);

    }
    // Method to draw branched lines
    private static void BranchedLineDraw(Pane pane, Point2D branchPoint, Point2D startPoint, ArrayList<Point2D> endPoints) {

        Branch b =new Branch(branchPoint,startPoint,endPoints);

        b.draw(pane);

    }
    // Method to determine the end points of the lines
    private static ArrayList<Point2D> determineEndPoints(ArrayList<Integer> inIDs, ArrayList<Integer> inPorts, ArrayList<Block> blocks) {

        int inPortsCount = 0;

        ArrayList<Point2D> endPoints = new ArrayList<>();

        for (int inId : inIDs) {

            int inPort = inPorts.get(inPortsCount++);

            for (Block block : blocks) {

                if (block.getId() == inId) {

                    endPoints.add(block.portPosition(inPort));

                }

            }

        }

        return endPoints;

    }
    // Method to determine the start point of the lines
    private static Point2D determineStartPoint(int inId, ArrayList<Block> blocks) {

        for (Block block : blocks) {

            if (block.getId() == inId) {

                double x = block.getTopLeft().getX() + block.getWidth();

                double y = block.getTopLeft().getY() + block.getWidth() / 2;

                if(block.isRotated()){

                    x = block.getTopLeft().getX();
                    y = block.getTopLeft().getY() + block.getWidth() / 2;

                }

                return new Point2D(x, y);

            }

        }

        return null;

    }
    // Method to calculate the branched point
    private static Point2D calcBranchedPoint(Point2D startPoint, ArrayList<Point2D> endPoints) {

        double y = startPoint.getY();

        double x = getMaxX(endPoints) -15;

        return new Point2D(x, y);

    }
    // Method to calculate the maximum X coordinate from a list of points
    private static double getMaxX(ArrayList<Point2D> endPoints) {

        double maxX = Double.NEGATIVE_INFINITY;

        for (Point2D point : endPoints) {

            if (point.getX() > maxX) {

                maxX = point.getX();

            }

        }

        return maxX;

    }
}
// Class representing an arrow line
class Arrow {
    private final Point2D startPoint;
    private final Point2D endPoint;
    private Line line;
    private Line line2;
    private Line line3;
    private Polygon triangle;
    private int noOfLines;
    private boolean triangleRotate;

    Arrow(Point2D startPoint, Point2D endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    // Method to draw the arrow line
    public void draw(Pane pane) {
        lineCreator();
        createTriangle();
        styleLine(line);
        pane.getChildren().addAll(triangle, line);

        //draw line 2 if needed
        if (this.noOfLines >= 2) {
            styleLine(line2);
            pane.getChildren().add(line2);
        }
        //draw line 3 if needed
        if (this.noOfLines == 3) {
            styleLine(line3);
            pane.getChildren().add(line3);
        }
    }
    // Method to create the arrow lines
    private void lineCreator() {
        // example: start is in top or under the end
        if (-startPoint.getX() + endPoint.getX() < 11 && -startPoint.getX() + endPoint.getX() >= 0) {
            // line 3 : first H line to left
            line3 = new Line(startPoint.getX(), startPoint.getY(), startPoint.getX() - 15, startPoint.getY());

            // line 2 : V line to up or down
            line2 = new Line(startPoint.getX() - 15, startPoint.getY(), startPoint.getX() - 15, endPoint.getY());

            //line  : last line to destination
            line = new Line(startPoint.getX() - 15, endPoint.getY(), endPoint.getX(), endPoint.getY());

            this.noOfLines = 3;
            this.triangleRotate = false;
        }
        // example: connection to unit delay
        else if (endPoint.getY() != startPoint.getY() && endPoint.getX() < startPoint.getX()) {
            // line 2 : V line t oup or down
            line2 = new Line(startPoint.getX(), startPoint.getY(), startPoint.getX(), endPoint.getY());
            // line : last H line to destination
            line = new Line(startPoint.getX(), endPoint.getY(), endPoint.getX(), endPoint.getY());

            this.noOfLines = 2;
            this.triangleRotate = true;
        }
        // just in case if a straight k=line is not enough
        else if (endPoint.getY() != startPoint.getY() && endPoint.getX() > startPoint.getX()) {
            // line 3 : first H line to left
            line3 = new Line(startPoint.getX(), startPoint.getY(), endPoint.getX() - 15, startPoint.getY());
            // line 2 : V line to up or down
            line2 = new Line(endPoint.getX() - 15, startPoint.getY(), endPoint.getX() - 15, endPoint.getY());
            //line  : last line to destination
            line = new Line(endPoint.getX() - 15, endPoint.getY(), endPoint.getX(), endPoint.getY());

            this.noOfLines = 3;
            this.triangleRotate = false;
        }
        //direct line into the end point
        else {
            //line  : direct line to destination
            line = new Line(startPoint.getX(), endPoint.getY(), endPoint.getX(), endPoint.getY());

            this.noOfLines = 1;
            this.triangleRotate = false;
        }
    }

    private void styleLine(Line l) {
        l.setStroke(Color.BLACK);
        l.setStrokeWidth(1.5);
        l.setFill(Color.BLACK);
    }
    // Method to create the triangle shape
    private void createTriangle() {
        triangle = new Polygon();
        double endX = endPoint.getX();
        double endY = endPoint.getY();
        triangle.setFill(Color.BLACK);
        triangle.setStroke(Color.BLACK);

        if (this.triangleRotate) {
            triangle.getPoints().addAll(endX + 8, endY - 6, endX, endY, endX + 8, endY + 6);
            return;
        }

        triangle.getPoints().addAll(endX - 8, endY + 6, endX, endY, endX - 8, endY-6);
    }

}
// Class representing a branched line
class Branch {
    private final Point2D branchedPoint;
    private final Point2D startPoint;
    private final ArrayList<Point2D> endPoints;

    public Branch(Point2D branchedPoint, Point2D startPoint, ArrayList<Point2D> endPoints) {
        this.branchedPoint = branchedPoint;
        this.startPoint = startPoint;
        this.endPoints = endPoints;
    }
    // Method to draw the branched line
    public void draw(Pane pane) {
        //create a direct lone to branching point
        Line lineToBranchedPoint = new Line(startPoint.getX(), startPoint.getY(), branchedPoint.getX(), branchedPoint.getY());
        lineToBranchedPoint.setStroke(Color.BLACK);
        lineToBranchedPoint.setStrokeWidth(1.5);
        lineToBranchedPoint.setFill(Color.BLACK);

        //create arrow lines that are connected to the end and draw them
        for (Point2D endPoint : endPoints) {
            new Arrow(branchedPoint, endPoint).draw(pane);
        }

        //create a bold dot at the branch point
        Circle branchDot = new Circle(branchedPoint.getX(), branchedPoint.getY(), 2);
        branchDot.setStroke(Color.BLACK);
        branchDot.setFill(Color.BLACK);

        // add previous to pane
        pane.getChildren().addAll(lineToBranchedPoint, branchDot);
    }
}