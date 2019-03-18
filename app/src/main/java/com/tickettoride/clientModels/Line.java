package com.tickettoride.clientModels;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;

public class Line {
    private static double scale=1.0;//set at runtime in mapFragment, passed into the routehelper
    private static final double imgscale=2.42;//how much bigger the lines were than original 2.42 DO NOT CHANGE! 
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private Region region;
    private Path path = new Path();
    
    public static void setScale(double newscale){
        scale=newscale;
    }

    public Line(int startX, int startY, int endX, int endY) {
        this.startX = (int)((startX/imgscale) * scale);
        this.startY = (int)((startY/imgscale) * scale);
        this.endX = (int)((endX/imgscale) * scale);
        this.endY = (int)((endY/imgscale) * scale);
        setPath();
        setRegion();
    }
    private void setPath() {
        double slope = 0;
        slope = -1 * (((double)(startX - endX)) / (startY - endY));
        int distance = 8;
        double x = distance * (1.0/Math.sqrt(1 + (slope * slope)));
        double y = distance * (slope/Math.sqrt(1 + (slope * slope)));
        Point point1 = new Point(startX + (int)x, startY + (int)y);
        Point point2 = new Point(startX - (int)x, startY - (int)y);
        Point point3 = new Point(endX - (int)x, endY - (int)y);
        Point point4 = new Point(endX + (int)x, endY + (int)y);
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(point1.x, point1.y);
        path.lineTo(point2.x, point2.y);
        path.lineTo(point3.x, point3.y);
        path.lineTo(point4.x, point4.y);
        path.lineTo(point1.x, point1.y);
        path.close();
    }
    public void drawPath(Canvas canvas, Paint paint) {
//        canvas.drawLine(startX, startY, endX, endY, paint);
        canvas.drawPath(path, paint);
    }
    public boolean contains(int x, int y) {
        return region.contains(x,y);
    }
    private void setRegion() {
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        region = new Region();
        region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
    }
    //private Color lineColor;
}
