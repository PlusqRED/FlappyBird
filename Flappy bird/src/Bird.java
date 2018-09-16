import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bird {
    private Rectangle birdShape;
    private float velocity = 0.4f;
    private float fallingSpeed = 0;
    private int windowWIDTH, windowHEIGHT;
    public Bird(int WIDTH, int HEIGHT) {
        windowWIDTH = WIDTH;
        windowHEIGHT = HEIGHT;
        birdShape = new Rectangle(40, 20);
        birdShape.setTranslateX(windowWIDTH/10);
        birdShape.setTranslateY(windowHEIGHT/2);
        birdShape.setFill(Color.BLACK);
    }

    public void jump() {
        fallingSpeed = -8;
        birdShape.setRotate(-30);
    }

    public void fall() {
        if(birdShape.getTranslateY() >= 0)
            fallingSpeed += velocity;
        else if(birdShape.getTranslateY() <= 0) {
            fallingSpeed = 0;
            birdShape.setTranslateY(0);
        }
        birdShape.setTranslateY(birdShape.getTranslateY() + fallingSpeed);
    }
    public Rectangle getBirdShape() {
        return birdShape;
    }

    public float getFallingSpeed() {
        return fallingSpeed;
    }

    public void setFallingSpeed(float fallingSpeed) {
        this.fallingSpeed = fallingSpeed;
    }

    public void setTranslateX(float x) {
        this.birdShape.setTranslateX(x);
    }

    public void setTranslateY(float y) {
        this.birdShape.setTranslateY(y);
    }

    public float getTranslateX() {
        return (float)this.birdShape.getTranslateX();
    }

    public float getTranslateY() {
        return (float)this.birdShape.getTranslateY();
    }


}
