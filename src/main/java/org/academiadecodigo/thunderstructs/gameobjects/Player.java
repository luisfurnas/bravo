package org.academiadecodigo.thunderstructs.gameobjects;

import org.academiadecodigo.simplegraphics.pictures.BetterPicture;
import org.academiadecodigo.simplegraphics.pictures.Picture;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardEvent;
import org.academiadecodigo.thunderstructs.Game;

public class Player extends GameObjects {

    private Game game;
    private boolean playGame;
    private boolean quit;

    private boolean right;
    private boolean left;
    private boolean jumping;
    private int speed;

    private int currentJumpSpeed;
    private int jumpCounter;

    private int currentRightSpeed;
    private int rightMovementCounter;
    private int rightStoppingCounter;

    private int currentLeftSpeed;
    private int leftMovementCounter;
    private int leftStoppingCounter;

    private String direction; //TODO: implement shooting
    private int health;
    private Position position;
    private BetterPicture characterImage;


    public Player(Position position, String picture, Game game) {

        super(position, new BetterPicture(position.getPosX(), position.getPosY(), picture), ObjectType.PLAYER);
        this.position = position;
        this.characterImage = getObjectImage();
        this.speed = 3;
        this.currentJumpSpeed = 3;
        this.currentRightSpeed = 1;
        this.rightMovementCounter = 0;
        this.game = game;

    }

    @Override
    public void moveRight() {

        rightStoppingCounter = 0; //if > 0 (rightMovementCounter--)

        if (rightMovementCounter == 8) {
            currentRightSpeed++;
        }

        if (rightMovementCounter == 12) {
            currentRightSpeed++;
        }

        if (currentRightSpeed > speed) {
            currentRightSpeed = speed;
        }


        if (position.getPosX() < (Game.GAME_WIDTH / 2 + 200)) {

            super.getObjectImage().translate(currentRightSpeed, 0);
            position.setPosX(position.getPosX() + currentRightSpeed);

            rightMovementCounter++;
            return;
        }

        for (Object o : game.getWorldObjects()) {

            if (!(o instanceof GameObjects)) {
                return;
            }

            ((GameObjects) o).getObjectImage().translate(-currentRightSpeed, 0);
            ((GameObjects) o).getPosition().setPosX(((GameObjects) o).getPosition().getPosX() - currentRightSpeed);
        }

        rightMovementCounter++;

        //super.getObjectImage().translate(currentRightSpeed,0);
        //position.setPosX(position.getPosX() + currentRightSpeed);
    }

    public void stopRight() {

        rightMovementCounter = 0; //if > 0 (rightMovementCounter--)

        if (rightStoppingCounter == 8) {
            currentRightSpeed--;
        }

        if (rightStoppingCounter == 12) {
            currentRightSpeed--;
        }

        if (rightStoppingCounter == 16) {
            currentRightSpeed--;
        }

        if (currentRightSpeed < 0) {
            currentRightSpeed = 0;
        }

        if (position.getPosX() < (Game.GAME_WIDTH / 2 + 200)) {

            super.getObjectImage().translate(currentRightSpeed, 0);
            position.setPosX(position.getPosX() + currentRightSpeed);

            rightStoppingCounter++;
            return;
        }

        for (Object o : game.getWorldObjects()) {

            if (!(o instanceof GameObjects)) {
                return;
            }

            ((GameObjects) o).getObjectImage().translate(-currentRightSpeed, 0);
            ((GameObjects) o).getPosition().setPosX(((GameObjects) o).getPosition().getPosX() - currentRightSpeed);
        }

        rightStoppingCounter++;


        //super.getObjectImage().translate(currentRightSpeed, 0);
        //position.setPosX(position.getPosX() + currentRightSpeed);
    }

    @Override
    public void moveLeft() {

        if (position.getPosX() < 100) {
            return;
        }

        leftStoppingCounter = 0; //if > 0 (rightMovementCounter--)

        if (leftMovementCounter == 8) {
            currentLeftSpeed++;
        }

        if (leftMovementCounter == 12) {
            currentLeftSpeed++;
        }

        if (currentLeftSpeed > speed) {
            currentLeftSpeed = speed;
        }

        if (position.getPosX() > (Game.GAME_WIDTH / 2 - 200)) {

            super.getObjectImage().translate(-currentLeftSpeed, 0);
            position.setPosX(position.getPosX() - currentLeftSpeed);

            leftMovementCounter++;
            return;
        }

        for (Object o : game.getWorldObjects()) {

            if (!(o instanceof GameObjects)) {
                return;
            }

            ((GameObjects) o).getObjectImage().translate(+currentLeftSpeed, 0);
            ((GameObjects) o).getPosition().setPosX(((GameObjects) o).getPosition().getPosX() + currentLeftSpeed);
        }

        leftMovementCounter++;

        // leftMovementCounter++;
        // super.getObjectImage().translate(-currentLeftSpeed, 0);
        // position.setPosX(position.getPosX() - currentLeftSpeed);
    }

    public void stopLeft() {
        leftMovementCounter = 0; //if > 0 (rightMovementCounter--)

        if (leftStoppingCounter == 8) {
            currentLeftSpeed--;
        }

        if (leftStoppingCounter == 12) {
            currentLeftSpeed--;
        }

        if (leftStoppingCounter == 16) {
            currentLeftSpeed--;
        }

        if (currentLeftSpeed < 0) {
            currentLeftSpeed = 0;
        }

        if (position.getPosX() > (Game.GAME_WIDTH / 2 - 200)) {

            super.getObjectImage().translate(-currentLeftSpeed, 0);
            position.setPosX(position.getPosX() - currentLeftSpeed);

            leftStoppingCounter++;
            return;
        }

        for (Object o : game.getWorldObjects()) {

            if (!(o instanceof GameObjects)) {
                return;
            }

            ((GameObjects) o).getObjectImage().translate(+currentLeftSpeed, 0);
            ((GameObjects) o).getPosition().setPosX(((GameObjects) o).getPosition().getPosX() + currentLeftSpeed);
        }

        leftStoppingCounter++;


        //super.getObjectImage().translate(-currentLeftSpeed, 0);
        //position.setPosX(position.getPosX() - currentLeftSpeed);
    }

    public void jump() {

        setGravity(false);
        smoothJump();

        position.setPosY(getPosY() - currentJumpSpeed);
        characterImage.translate(0, -currentJumpSpeed);
        jumpCounter++;

        if (jumpCounter >= 50) {
            this.jumping = false;
            setGravity(true);
            jumpCounter = 0;
            currentJumpSpeed = speed;
        }
    }


    public void tick() {

        if (right && !isCollisionOnRight()) {
            moveRight();
        }

        if (!right) {

            if (isCollisionOnRight()) {
                currentRightSpeed = 0;
            }

            stopRight();
        }

        if (left && !isCollisionOnLeft()) {
            moveLeft();
        }

        if (!left) {

            if (isCollisionOnLeft()) {
                currentLeftSpeed = 0;
            }

            stopLeft();
        }

        if (jumping) {

            if (!isCollisionOnTop()) {
                jump();
                return;
            }

            setJumping(false);
            jumpCounter = 0;
            setGravity(true);
        }
    }

    public void smoothJump() {
        if (jumpCounter == 30) {
            currentJumpSpeed--;
        }

        if (jumpCounter == 36) {
            currentJumpSpeed--;
        }

        if (jumpCounter == 44) {
            currentJumpSpeed--;
        }
    }

    //TODO: Change KEY_SPACE with KEY_UP; Erase commented code; Improve jump smoothness (using speed...).
    @Override
    public void keyReleased(KeyboardEvent keyboardEvent) {
        switch (keyboardEvent.getKey()) {
            case KeyboardEvent.KEY_LEFT:
                setLeft(false);
                break;
            case KeyboardEvent.KEY_RIGHT:
                setRight(false);
                break;
        }
    }

    @Override
    public void keyPressed(KeyboardEvent keyboardEvent) {
        switch (keyboardEvent.getKey()) {
            case KeyboardEvent.KEY_LEFT:
                setLeft(true);
                break;
            case KeyboardEvent.KEY_RIGHT:
                setRight(true);
                break;
            //case KeyboardEvent.KEY_DOWN:
            // moveDown();
            //break;
            case KeyboardEvent.KEY_SPACE:

                if (isCollisionOnBottom()) {
                    setJumping(true);
                    break;
                }

                if (position.getPosY() < Game.GAME_HEIGHT - ObjectType.PLAYER.getHeigth()) { //TODO: Change 2nd value once Collisions are working properly
                    break; //Send condition to "jumping" to allow continuous jumping while pressing up.
                }

                setJumping(true);
                break;

            case KeyboardEvent.KEY_UP:
                break;

            case KeyboardEvent.KEY_R:
                this.playGame = true;
                break;

            case KeyboardEvent.KEY_Q:
                this.quit = true;
                break;
        }
    }


    public int getPosX() {
        return this.position.getPosX();
    }

    public int getPosY() {
        return this.position.getPosY();
    }

    public void setRight(boolean setStatus) {
        this.right = setStatus;
    }

    public void setLeft(boolean setStatus) {
        this.left = setStatus;
    }

    public void setJumping(boolean setStatus) {
        this.jumping = setStatus;
    }

    public boolean getPlayGame() {
        return this.playGame;
    }

    public boolean getQuit() {
        return this.quit;
    }

    public void setPlayGameFalse() {
        this.playGame = false;
    }
}
