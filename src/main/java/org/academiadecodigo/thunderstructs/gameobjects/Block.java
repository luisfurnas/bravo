package org.academiadecodigo.thunderstructs.gameobjects;

import org.academiadecodigo.simplegraphics.pictures.BetterPicture;

public abstract class Block extends GameObjects {


    public Block(Position position, String picturePath, ObjectType objectType) {

        super(position, new BetterPicture(position.getPosX(), position.getPosY(), picturePath), objectType);
    }
}
