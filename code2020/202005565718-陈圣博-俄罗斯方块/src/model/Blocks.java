package model;

import java.awt.Point;

public class Blocks {
    public Point[] points;
    Blocks(int [] xs, int [] ys){
        points = new Point[4];
        for(int i=0;i<4;i++)
        {
            points[i]=new Point(xs[i],ys[i]);
        }
    }

    Blocks(Blocks blocks){
        points = new Point[4];
        for(int i=0;i<4;i++)
        {
            points[i]=new Point(blocks.points[i].x,blocks.points[i].y);
        }
    }
}
