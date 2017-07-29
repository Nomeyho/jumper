package io.github.nomeyho.jumper.utils;


import com.badlogic.gdx.graphics.Color;

public class ColorManager {

    private final static ColorManager INSTANCE = new ColorManager();

    private ColorManager(){}

    public static ColorManager get(){
        return INSTANCE;
    }

    public Color getColor(float y){
        return Color.RED;
    }

}
