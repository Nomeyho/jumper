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
        controlColors = new Vector3[10];
        controlColors[0] = new Vector3(192/255f,67/255f,32/255f);   //Orange
        controlColors[1] = new Vector3(165/255f,245/255f,169/255f); //Light green
        controlColors[2] = new Vector3(246/255f,36/255f,89/255f);   //Pink
        controlColors[3] = new Vector3(191/255f,85/255f,236/255f);  //Purple
        controlColors[4] = new Vector3(129/255f,207/255f,224/255f);  //Light blue
        controlColors[5] = new Vector3(46/255f,204/255f,113/255f);  //Green soft light
        controlColors[6] = new Vector3(249/255f,191/255f,59/255f);  //Yellow
        controlColors[7] = new Vector3(246/255f,71/255f,71/255f);  //Red
        controlColors[8] = new Vector3(228/255f,241/255f,254/255f);  //White and blue
        controlColors[9] = new Vector3(25/255f,181/255f,254/255f);  //Blue soft light

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
        myCatmull.valueAt(out, (y/70000f)%1);
      //  return new Color(out.x,out.y,out.z,1);
        return new Color(192/255f,67/255f,32/255f,1);
    }

}
