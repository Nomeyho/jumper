package io.github.nomeyho.jumper.utils;


public enum DirectionEnum {
    LEFT(-1),
    RIGHT(+1);

    private int sign;

    DirectionEnum(int sign){
        this.sign = sign;
    }

    public int getSign(){
        return this.sign;
    }

    public static DirectionEnum toDirection(double expression){
        if(expression >= 0)
            return RIGHT;
        else
            return LEFT;
    }
}
