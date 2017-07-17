package com.missionbit.game.states;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.missionbit.game.GameTutorial;
import com.missionbit.game.sprites.animals.Bunny;
import com.missionbit.game.sprites.Farmer;
import com.missionbit.game.sprites.obstacles.Poop;
import com.missionbit.game.sprites.animals.Sheep;
import com.missionbit.game.sprites.obstacles.Obstacle;

import java.util.Random;

/**
 * Created by missionbit on 6/28/17.
 */

public class Level4 extends State {
    private Texture sky;
    private Sheep sheep;
    private Farmer farmer;
    private Texture cars;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Vector2 skyPos, skyPos2;
    private Vector2 carsPos, carsPos2, carsPos3;
    private static final int POOP_WIDTH = 30;
    //OBSTACLES
    private Texture greenCarTexture;
    private Obstacle greenCar;
    private Texture appleTexture;
    private Obstacle apple;
    private boolean appleIsTouched;
    private Poop poop;
    long startTime;
    private static final int CAR_WIDTH = 270;
    private static final int SKY_WIDTH = 800;
    private static final int GROUND_WIDTH = 790;

    public Level4(GameStateManager gsm) {
        super(gsm);
        poop = new Poop(100, 60);
        sheep = new Sheep(150, 60);
        sky = new Texture("sunCloudsForHighway.png");
        farmer = new Farmer(-50, 60);
        cars = new Texture("carsForHighway.png");
        ground = new Texture("highwayBackground.png");
        cam.setToOrtho(false, GameTutorial.WIDTH / 2, GameTutorial.HEIGHT / 2);
        groundPos1 = new Vector2(cam.position.x - cam.viewportWidth / 2, 0);
        groundPos2 = new Vector2(ground.getWidth() + groundPos1.x, 0);
        skyPos = new Vector2(cam.position.x - cam.viewportWidth / 2, 0);
        skyPos2 = new Vector2(sky.getWidth() + skyPos.x, 0);
        carsPos = new Vector2(cam.position.x - cam.viewportWidth / 2, 0);
        carsPos2 = new Vector2(cars.getWidth() + carsPos.x, 0);
        carsPos3 = new Vector2(cars.getWidth() + carsPos2.x, 0);
        //OBSTACLES
        greenCarTexture = new Texture("CarGreen.png");
        greenCar = new Obstacle(greenCarTexture, 1000, 20, 1, 0.5f);
        appleTexture = new Texture("Apple2.png");
        apple = new Obstacle(appleTexture, 700, 55, 2, 0.5f);
        appleIsTouched = false;
        startTime = System.currentTimeMillis();

    }

    @Override
    protected void handleInput() {
        if (sheep.getPosition().y == 60) {
            if (Gdx.input.justTouched()) {
                sheep.jump();
            }
        }
    }

    @Override
    public void create() {
    }

    @Override
    public void update(float dt) {
        handleInput();
        sheep.update(dt);
        cam.position.x = sheep.getPosition().x + 80;
        farmer.update(dt);
        updateGround();
        updateSky();
        updateCars();
        timerCheck(dt);
        apple.update(dt);
        updateApple();
        updatePoops();
        collisionCheck();
        changeLevels();
        updateGreen();
        cam.update();
//        if(((System.currentTimeMillis() - startTime) > 30000 & farmer.collides(sheep.getBounds1()) == false)) {
//            gsm.set(new Level5(gsm));
//    }

    }


    public void updateGreen() {
        if (cam.position.x - cam.viewportWidth / 2 > greenCar.getPosObs().x + greenCar.getWidth()) {
            Random rand = new Random();
            float fluctuation = rand.nextFloat();
            float distance = (fluctuation * 400) + GameTutorial.WIDTH;
            greenCar.reposition(greenCar.getPosObs().x + distance, 35);
        }
    }

    public void updatePoops(){
        if (poop.getPosPoop().x + POOP_WIDTH <= cam.position.x-cam.viewportWidth/2){
            Random rand = new Random();
            float fluctuation = rand.nextFloat();
            float distance = (fluctuation * 600) + GameTutorial.WIDTH;
            poop.reposition(poop.getPosPoop().x+ distance, 58);
        }
    }

    public void updateApple() {
        if (cam.position.x - cam.viewportWidth / 2 > apple.getPosObs().x + apple.getWidth()) {
            Random rand = new Random();
            float fluctuation = rand.nextFloat();
            float distance = (fluctuation * 800) + GameTutorial.WIDTH;
            apple.reposition(apple.getPosObs().x + distance, 58);
            appleIsTouched = false;
        }
    }


    public void updateGround(){
        if(groundPos1.x+ground.getWidth() <= cam.position.x-cam.viewportWidth/2){
            groundPos1.add(2*ground.getWidth(),0);
        }
        if(groundPos2.x+ground.getWidth() <= cam.position.x-cam.viewportWidth/2){
            groundPos2.add(2*ground.getWidth(),0);
        }

    }

    public void updateSky(){
        if(skyPos.x+sky.getWidth() <= cam.position.x-cam.viewportWidth/2){
            skyPos.add(2*sky.getWidth(),0);
        }
        if(skyPos2.x+sky.getWidth() <= cam.position.x-cam.viewportWidth/2){
            skyPos2.add(2*sky.getWidth(),0);
        }

    }

    public void updateCars(){
        if(carsPos.x+cars.getWidth() <= cam.position.x-cam.viewportWidth/2){
            carsPos.add(3*cars.getWidth(),0);
        }
        if(carsPos2.x+cars.getWidth() <= cam.position.x-cam.viewportWidth/2){
            carsPos2.add(3*cars.getWidth(),0);
        }

        if(carsPos3.x+cars.getWidth()<= cam.position.x-cam.viewportWidth/2){
            carsPos3.add(3*cars.getWidth(),0);
        }

    }

    public void collisionCheck() {
        if (farmer.collides(sheep.getBounds1())) {
            sheep.getSheepDead();
            sheep.sheepDied();
            farmer.killedSheep();
        }
        if (poop.collides(sheep.getBounds1())){
            sheep.reduceSpd();
            sheep.startTimer();
        }
        if (apple.collides(sheep.getBounds1())) {
            appleIsTouched = true;
            sheep.increaseSpd();
            sheep.startTimer();
            System.out.println("Apple Touched");
        }
        if (greenCar.collides(sheep.getBounds1())) {
            System.out.println("Car touched");
            sheep.reduceSpd();
            sheep.startTimer();
        }
    }

    public void changeLevels(){
//        if (sheep.getPosition().x > 3000){
//            gsm.set(new Level5(gsm));
//        }
    }

    public void timerCheck(float timePassed) {
        sheep.updateTimer(timePassed);
        if (sheep.isTimerDone()) {
            sheep.resetSpd();
        }
        if (poop.collides(sheep.getBounds1())){
            sheep.reduceSpd();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.setProjectionMatrix(cam.combined);
        sb.draw(sky,skyPos.x,0,SKY_WIDTH,400);
        sb.draw(sky,skyPos2.x,0,SKY_WIDTH,400);
        sb.draw(ground,groundPos1.x,0,GROUND_WIDTH,350);
        sb.draw(ground,groundPos2.x,0,GROUND_WIDTH,350);
        sb.draw(cars,carsPos.x,20,CAR_WIDTH,300);
        sb.draw(cars,carsPos2.x,20,CAR_WIDTH,300);
        sb.draw(cars,carsPos3.x,20,CAR_WIDTH,300);
        sb.draw(poop.getPoop(),poop.getPosPoop().x,poop.getPosPoop().y,30,30);
        sb.draw(greenCar.getObstacle(), greenCar.getPosObs().x, greenCar.getPosObs().y);
        if (appleIsTouched == false) {
            sb.draw(apple.getObsAnimation(), apple.getPosObs().x, apple.getPosObs().y);
        }
        if (farmer.collides(sheep.getBounds1())) {
            sb.draw(sheep.getSheepDead(), sheep.getPosition().x, sheep.getPosition().y,70,45);
        } else {
            sb.draw(sheep.getSheep(), sheep.getPosition().x, sheep.getPosition().y, 70, 45);
        }
        sb.draw(farmer.getFarmer(),farmer.getPosition().x,farmer.getPosition().y,120,110);
        sb.end();
    }

    @Override
    public void dispose() {
        sky.dispose();
        sheep.dispose();
        farmer.dispose();
        cars.dispose();
        ground.dispose();
        poop.dispose();
        greenCarTexture.dispose();
        greenCar.dispose();
        appleTexture.dispose();
        apple.dispose();
    }
}
