package org.academiadecodigo.thunderstructs.gameobjects;

import org.academiadecodigo.simplegraphics.pictures.BetterPicture;

public class Enemy extends GameObjects {


    private boolean movingLeft = false;


    public Enemy(Position position, String picturePath) {

        super(position, new BetterPicture(position.getPosX(), position.getPosY(), picturePath), ObjectType.ENEMY);
    }

    //last movement indicates the last movement through a boolean: true = left; false = right
    public void move() {

        if (!movingLeft) {
            if (isCollisionOnBottomRight()) {
                moveRight();
                return;
            }
            movingLeft = true;
        }

        if (isCollisionOnBottomLeft()) {
            moveLeft();
            return;
        }
        movingLeft = false;
    }
}
