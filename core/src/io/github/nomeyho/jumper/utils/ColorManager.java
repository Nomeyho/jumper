package io.github.nomeyho.jumper.utils;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class ColorManager {

    private final static ColorManager INSTANCE = new ColorManager();
    private Vector3[] controlColors;
    private Vector3 out = new Vector3();
    private CatmullRomSpline<Vector3> myCatmull;

    private ColorManager(){
        controlColors = new Vector3[4];
        controlColors[0] = new Vector3(192f/255,67/255f,32/255f);
        controlColors[1] = new Vector3(165/255f,245/255f,169/255f);
        controlColors[2] = new Vector3(237/255f,186/255f,44/255f);
        controlColors[3] = new Vector3(150/255f,91/255f,119/255f);

        myCatmull = new CatmullRomSpline<Vector3>(controlColors, true);
    }

    public static ColorManager get(){
        return INSTANCE;
    }

    public void shuffle(){
        Array<Vector3> colors = new Array<Vector3>(this.controlColors);
        colors.shuffle();
        controlColors = colors.items;
        myCatmull = new CatmullRomSpline<Vector3>(controlColors, true);
    }

    public Color getColor(float y){
        myCatmull.valueAt(out, (y/20000f)%1);
        return new Color(out.x,out.y,out.z,1);
    }

}
