import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Pipes {
    public Rectangle upRect;
    public Rectangle downRect;
    private Rectangle space;
    private float level = 100;
    private int pipeWIDTHS = 50;
    private int windowWIDTH, windowHEIGHT;
    private Color color;
    private ArrayList<Rectangle> pipes;

    public Pipes(int WIDTH, int HEIGHT) {
        color = Color.GREEN;
        windowWIDTH = WIDTH;
        windowHEIGHT = HEIGHT;

        space = new Rectangle();
        space.setFill(Color.WHITE);
        space.setWidth(pipeWIDTHS);
        space.setHeight(level);

        upRect = new Rectangle();
        upRect.setTranslateY(0);
        upRect.setWidth(pipeWIDTHS);
        upRect.setFill(color);
        upRect.setStrokeWidth(3);
        upRect.setStroke(Color.BLACK);

        downRect = new Rectangle();
        downRect.setWidth(pipeWIDTHS);
        downRect.setFill(color);
        downRect.setStroke(Color.BLACK);
        downRect.setStrokeWidth(3);

        space.setTranslateX(windowWIDTH);

        pipes = new ArrayList<>();

        pipes.add(upRect);
        pipes.add(downRect);
    }

    public void generateSpace(float spacePosX) {
        space.setHeight(level);
        space.setTranslateX(spacePosX);
        space.setTranslateY(new Random().nextInt(windowHEIGHT - (int)space.getHeight() - 50));

        upRect.setTranslateX(space.getTranslateX());
        upRect.setHeight(space.getTranslateY());

        downRect.setTranslateX(space.getTranslateX());
        downRect.setTranslateY(space.getTranslateY() + space.getHeight());
        downRect.setHeight(windowHEIGHT - space.getHeight());

        pipes.clear();

        pipes.add(upRect);
        pipes.add(downRect);
    }
    public void upLevel(int heroHEIGHT) {
        if(level/1.5 - 10 > heroHEIGHT) {
            if(upRect.getFill() == Color.RED) {
                upRect.setFill(Color.BLACK);
                downRect.setFill(Color.BLACK);
                upRect.setStroke(Color.WHITE);
                downRect.setStroke(Color.WHITE);
            }
            if(upRect.getFill() == Color.GREEN) {
                upRect.setFill(Color.RED);
                downRect.setFill(Color.RED);
                upRect.setStroke(Color.BLACK);
                downRect.setStroke(Color.BLACK);
            }
            level/=1.5;
        }

    }

    public void resetLevel() {
        level = 100;
        upRect.setFill(Color.GREEN);
        downRect.setFill(Color.GREEN);
    }

    public void setPipeWIDTHS(int pipeWIDTHS) {
        this.pipeWIDTHS = pipeWIDTHS;
    }

    public void setColor(Color color) {
        this.color = color;
        upRect.setFill(color);
        downRect.setFill(color);
    }

    public Color getColor() {
        return color;
    }

    public ArrayList<Rectangle> getPipes() {
        return pipes;
    }

    public boolean checkIntersects(Rectangle bird) {
        if(bird.getTranslateX() + bird.getWidth() > upRect.getTranslateX() && bird.getTranslateX() + bird.getWidth() < upRect.getTranslateX() + upRect.getWidth()) {
            if(bird.getTranslateY() + bird.getHeight()/2 > upRect.getHeight() && bird.getTranslateY() < downRect.getTranslateY())
                return false;
            else
                return true;
        }
        return false;
    }
}
