import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.animation.RotateTransition;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Block {
    private int id;
    private String type;
    private int ports;
    private int left;
    private int top;
    private int right;
    private int bottom;
    private Rectangle frame;
    private Rectangle shadow;
    private Text text;
    private Rectangle icon;
    private boolean rotated;
    private Image image;
    private ImageView imageView;

    public Block() {
        frame = new Rectangle();
        shadow = new Rectangle();
        text = new Text();
        icon = new Rectangle();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPorts(int ports) {
        this.ports = ports;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Rectangle getFrame() {
        return frame;
    }

    public Rectangle getShadow() {
        return shadow;
    }

    public Text getText() {
        return text;
    }

    public Rectangle getIcon() {
        return icon;
    }
    public boolean isRotated() {
        return rotated;
    }
    public void setRotated(boolean rotated) {
        this.rotated = rotated;
    }
    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public ImageView getImageView() {
        return imageView;
    }
    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setBlock() {
        // Calculate block dimensions and position based on the provided values

        // Set frame properties
        frame.setX(left);
        frame.setY(top);
        frame.setWidth(right - left);
        frame.setHeight(bottom - top);
        frame.setFill(Color.WHITE);
        frame.setStroke(Color.BLACK);
        if(this.isRotated()){
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), frame);
            rotateTransition.setAxis(Rotate.Y_AXIS); // Rotate around the y-axis
            rotateTransition.setByAngle(180); // Rotate by 180 degrees
            rotateTransition.setCycleCount(1); // Play animation only once
            rotateTransition.setAutoReverse(false); // Do not reverse the animation
            rotateTransition.play();
        }
        if(this.getId()==1){
            image=new Image("file:lib\\Saturate.png");
            ImageView imageView2 = new ImageView(image);
            // Set the size and position of the ImageView to match the rectangle
            imageView2.setFitWidth(frame.getWidth());
            imageView2.setFitHeight(frame.getHeight());
            imageView2.setX(frame.getX());
            imageView2.setY(frame.getY());
            setImageView(imageView2);
        } else if (this.getId()==5) {
            image=new Image("file:lib\\Constant.png");
            ImageView imageView2 = new ImageView(image);
            // Set the size and position of the ImageView to match the rectangle
            imageView2.setFitWidth(frame.getWidth());
            imageView2.setFitHeight(frame.getHeight());
            imageView2.setX(frame.getX());
            imageView2.setY(frame.getY());
            setImageView(imageView2);

        }else if (this.getId()==4) {
            image=new Image("file:lib\\UnitDelay.png");
            ImageView imageView2 = new ImageView(image);
            // Set the size and position of the ImageView to match the rectangle
            imageView2.setFitWidth(frame.getWidth());
            imageView2.setFitHeight(frame.getHeight());
            imageView2.setX(frame.getX());
            imageView2.setY(frame.getY());
            setImageView(imageView2);
        }else if (this.getId()==7) {
            image=new Image("file:lib\\Scope.png");
            ImageView imageView2 = new ImageView(image);
            // Set the size and position of the ImageView to match the rectangle
            imageView2.setFitWidth(frame.getWidth());
            imageView2.setFitHeight(frame.getHeight());
            imageView2.setX(frame.getX());
            imageView2.setY(frame.getY());
            setImageView(imageView2);

        }else if (this.getId()==3) {
            image=new Image("file:lib\\Add.png");
            ImageView imageView2 = new ImageView(image);
            // Set the size and position of the ImageView to match the rectangle
            imageView2.setFitWidth(frame.getWidth());
            imageView2.setFitHeight(frame.getHeight());
            imageView2.setX(frame.getX());
            imageView2.setY(frame.getY());
            setImageView(imageView2);
        }

        // Set shadow properties
        shadow.setX(left -1);
        shadow.setY(top -1);
        shadow.setWidth((frame.getWidth())+2);
        shadow.setHeight((frame.getHeight())+2);
        shadow.setFill(Color.TRANSPARENT);
        shadow.setStroke(Color.BLUE);
        shadow.setStrokeWidth(1);

        // Set text properties
        text.setText(type);
        text.setX(left -5);
        text.setY(top + 45);
        text.setFont(Font.font("Arial", 12));

        // Set icon properties
        icon.setX(left + 20);
        icon.setY(top + 40);
        icon.setWidth(10);
        icon.setHeight(10);
        icon.setFill(Color.BLUE);
    }

    public Point2D portPosition(int portNumber) {
        // Calculate the position of the port based on the block's dimensions and port number
        if(this.isRotated()){
            double x =right;
            double y = top + (portNumber +1) * frame.getHeight() / (ports + 4);

            return new Point2D(x, y);
        }else {
            double x = left;//+ frame.getWidth() / 2;
            double y = top + (portNumber + 1) * frame.getHeight() / (ports + 4);

            return new Point2D(x, y);
        }
    }

    public String getName() {
        return type;
    }

    public double getWidth() {
        return frame.getWidth();
    }

    public Point2D getTopLeft() {
        return new Point2D(left,top);
    }
}
