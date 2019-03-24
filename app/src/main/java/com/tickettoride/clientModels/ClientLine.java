package com.tickettoride.clientModels;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Region;

import com.tickettoride.models.Line;

import java.util.Map;

public class ClientLine extends Line {
    private static double scale=1.0;//set at runtime in mapFragment, passed into the routehelper
    private static final double imgscale=2.42;//how much bigger the lines were than original 2.42 DO NOT CHANGE! 
    private Region region;
    private Path path = new Path();
    private int finalStartX;
    private int finalEndX;
    private int finalStartY;
    private int finalEndY;
    
    public static void setScale(double newscale){
        scale=newscale;
    }

    public ClientLine(Map<String, Object> lineHash) { super(lineHash); }

    public void resetLine() {
        if (region == null) {
            this.finalStartX = (int) ((startX / imgscale) * scale);
            this.finalStartY = (int) ((startY / imgscale) * scale);
            this.finalEndX = (int) ((endX / imgscale) * scale);
            this.finalEndY = (int) ((endY / imgscale) * scale);
            setPath();
            setRegion();
        }
    }

    private void setPath() {
        double slope = 0;
        int deltaY=finalStartY - finalEndY;
        int deltaX=finalStartX - finalEndX;
        int distance = 8;
        double x;
        double y;
        if(deltaY!=0 && deltaX!=0) {
            slope = -1 * (((double) (deltaX)) / (deltaY));
            x = distance * (1.0/Math.sqrt(1 + (slope * slope)));
            y = distance * (slope/Math.sqrt(1 + (slope * slope)));
        }else if(deltaX==0) {
            x=distance-1;
            y=0;
        }else{
            x=0;
            y=distance-1;
        }
        Point point1 = new Point(finalStartX + (int)x, finalStartY + (int)y);
        Point point2 = new Point(finalStartX - (int)x, finalStartY - (int)y);
        Point point3 = new Point(finalEndX - (int)x, finalEndY - (int)y);
        Point point4 = new Point(finalEndX + (int)x, finalEndY + (int)y);
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
}
