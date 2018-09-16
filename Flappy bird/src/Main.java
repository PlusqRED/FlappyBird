import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Main extends Application {
    private Bird bird;
    private int WIDTH = 800;
    private int HEIGHT = 400;
    private Pipes pipes;
    private Pipes pipes1;
    private float gameSpeed = 4;
    private int i;
    private Text scoreText;
    private Text gameOverText = new Text();
    private int score = 0;
    private int upLevelScore = 5;
    private boolean gameIsOver = false;
    private Text levelText;
    private int level = 1;
    private AnimationTimer timer;
    private ImageView skyView;
    private boolean cheatMode = false;
    private Text cheatModeText = new Text("CHEAT MODE");
    private float deltaOpacity = 0.05f;
    private EventHandler<KeyEvent> keyPressed = event -> {
        if(event.getCode() == KeyCode.SPACE) {
            bird.jump();
        }
        if(event.getCode() == KeyCode.P) {
            gameIsOver = false;
            gameOverText.setText("");
            bird.getBirdShape().setOpacity(1);
            levelText.setText("Level " + level);
            scoreText.setText("Score " + score);
            skyView.setTranslateX(0);
        }
        if(event.getCode() == KeyCode.C) {
            cheatMode = !cheatMode;
            if(cheatMode)
                cheatModeText.setVisible(true);
            else cheatModeText.setVisible(false);
        }

    };
    private EventHandler<KeyEvent> keyReleased = event -> {
        if(event.getCode() == KeyCode.SPACE) {
            bird.getBirdShape().setRotate(0);
        }
    };

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage stage = primaryStage;
        Scene scene = new Scene(createContent());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getScene().setOnKeyPressed(keyPressed);
        stage.getScene().setOnKeyReleased(keyReleased);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Pane createContent() {
        score = 0;

        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        bird = new Bird(WIDTH, HEIGHT);
        pane.getChildren().add(bird.getBirdShape());

        pipes = new Pipes(WIDTH, HEIGHT);
        for(Rectangle element : pipes.getPipes())
            pane.getChildren().add(element);
        pipes.generateSpace(WIDTH);

        pipes1 = new Pipes(WIDTH, HEIGHT);
        for(Rectangle element : pipes1.getPipes())
            pane.getChildren().add(element);
        pipes1.generateSpace(WIDTH + WIDTH/2);

        scoreText = new Text();
        scoreText.setText(String.format("Score %d", score));
        scoreText.setTranslateX(50);
        scoreText.setTranslateY(50);
        scoreText.setFont(Font.font(30));
        pane.getChildren().add(scoreText);

        gameOverText.setTranslateX(WIDTH/2 - 180);
        gameOverText.setTranslateY(HEIGHT/2 - 40);
        gameOverText.setFont(Font.font(50));
        pane.getChildren().add(gameOverText);

        levelText = new Text();
        levelText.setTranslateX(WIDTH/2 - 70);
        levelText.setTranslateY(50);
        levelText.setFont(Font.font(30));
        levelText.setText("Level " + level);
        pane.getChildren().add(levelText);

         skyView = new ImageView("img\\sky.png");
        skyView.setFitHeight(HEIGHT*3);
        skyView.setFitWidth(WIDTH*3);
        pane.getChildren().add(0, skyView);

        cheatModeText.setFont(Font.font(40));
        cheatModeText.setFill(Color.YELLOW);
        cheatModeText.setTranslateX(WIDTH/2 - 150);
        cheatModeText.setTranslateY(HEIGHT/2);
        cheatModeText.setVisible(false);
        pane.getChildren().add(cheatModeText);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        pane.setBackground(new Background(new BackgroundFill(Color.rgb(63,191,255), new CornerRadii(0), new Insets(0,0,0,0))));
        return pane;
    }

    private void onUpdate() {
        if(!gameIsOver) {
            if(pipes.getPipes().get(0).getTranslateX() + pipes.getPipes().get(0).getWidth() < 0) {
                pipes.generateSpace(WIDTH);
                scoreText.setText(String.format("Score %d", ++score));
            }


            if(pipes1.getPipes().get(0).getTranslateX() + pipes1.getPipes().get(0).getWidth() < 0) {
                pipes1.generateSpace(WIDTH);
                scoreText.setText(String.format("Score %d", ++score));
            }

            for(i = 0; i < 2; i++) {
                pipes.getPipes().get(i).setTranslateX(pipes.getPipes().get(i).getTranslateX() - gameSpeed);
                pipes1.getPipes().get(i).setTranslateX(pipes1.getPipes().get(i).getTranslateX() - gameSpeed);
            }

            skyView.setTranslateX(skyView.getTranslateX() - gameSpeed);

            if(pipes.checkIntersects(bird.getBirdShape()) || pipes1.checkIntersects(bird.getBirdShape()) || bird.getBirdShape().getTranslateY() > HEIGHT) {
                if(!cheatMode) {
                    bird.getBirdShape().setTranslateY(HEIGHT / 2);
                    bird.setFallingSpeed(0);
                    gameSpeed = 0;
                    gameIsOver = true;
                }
            }
            else bird.fall();

            if(cheatMode) {
                if(cheatModeText.getOpacity() < 0) {
                    deltaOpacity *= -1;
                    cheatModeText.setOpacity(0);
                }
                else if(cheatModeText.getOpacity() > 1) {
                    deltaOpacity *= -1;
                    cheatModeText.setOpacity(1);
                }

                cheatModeText.setOpacity(cheatModeText.getOpacity() - deltaOpacity);
            }

            if(score > upLevelScore) {
                pipes.upLevel((int)bird.getBirdShape().getHeight());
                pipes1.upLevel((int)bird.getBirdShape().getHeight());
                levelText.setText("Level " + (++level));
                upLevelScore+=5;
            }

            if(gameSpeed == 0) {
                gameOverText.setText(String.format("Score: %d\nPress P to restart", score));
                cheatModeText.setVisible(false);
                scoreText.setText("");
                levelText.setText("");
                upLevelScore = 5;
                level = 1;
                bird.getBirdShape().setOpacity(0);
                score = 0;
                pipes.generateSpace(WIDTH);
                pipes1.generateSpace(WIDTH + WIDTH/2);
                pipes.resetLevel();
                pipes1.resetLevel();
                gameSpeed = 4;
            }
        }

    }
}
