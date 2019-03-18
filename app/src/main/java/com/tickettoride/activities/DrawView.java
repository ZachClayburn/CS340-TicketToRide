package com.tickettoride.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.Route;

import java.util.ArrayList;

public class DrawView extends View {
    Paint paint = new Paint();
    private ArrayList<Route> routes = new ArrayList<>();

    private void init() {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(14);
    }

    public DrawView(Context context) {
        super(context);
        init();
    }
    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }
    public void drawRoutes(Canvas canvas) {
        for (Route route : routes) {
            route.drawRoute(canvas);
        }
    }
    
    public boolean clickRoute(int x, int y){//todo: actually make this work with the claimroute funtionallity
        for(Route route :routes){
            for(int i=x-1;i<x+2;i++) {
                for(int j=y;j<y+2;j++) {
                    if (route.contains(i, j)) {
                        if (route.getIsClaimed()) {
                            //it's already been claimed
                            Log.i("TAG", "got claimed one!");

                            return false;//mainly for testing
                        } else {
                            //do whatever is needed to claim it.
                            //go to the discard whatever
                            route.setLineColor(DataManager.getSINGLETON().getPlayer().getColor());//purely testing
                            route.setIsClaimed(true);//purely testing
                            return true;//mainly for testing
                        }
                    }
                }
            }
        }
        return false;//mainly for testing
    }
    
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    
    @Override
    public void onDraw(Canvas canvas) {
        //Straight lines
        //canvas.drawRect(150, 130, 150, 180, paint);
        drawRoutes(canvas);
        //canvas.drawLine(130, 130, 130, 180, paint);
        //canvas.drawLine(160, 108, 330, 88, paint);
        //canvas.drawLine(350, 98, 495, 273, paint);
        //canvas.drawLine(510, 268, 665, 118, paint);
        //canvas.drawLine(695, 118, 850, 268, paint);
        //canvas.drawLine(515, 288, 845, 283, paint);
        //canvas.drawLine(155, 215, 485, 293, paint);
        //canvas.drawLine(142, 213, 123, 260, paint);
        //canvas.drawLine(122, 208, 103, 255, paint);
        //canvas.drawLine(485, 308, 400, 450, paint);
        //canvas.drawLine(505, 308, 583, 500, paint);
        //canvas.drawLine(525, 305, 783, 410, paint);
        //canvas.drawLine(405, 467, 563, 500, paint);
        //canvas.drawLine(405, 487, 563, 520, paint);
        //canvas.drawLine(365, 473, 110, 557, paint);
        //canvas.drawLine(365, 493, 115, 577, paint);
        //canvas.drawLine(1040, 170, 705, 100, paint);
        //canvas.drawLine(1070, 180, 1180, 205, paint);
        //canvas.drawLine(1040, 190, 885, 255, paint);
        //canvas.drawLine(1200, 222, 875, 277, paint);
        //canvas.drawLine(1230, 227, 1235, 327, paint);
        //canvas.drawLine(1345, 272, 1250, 332, paint);
        //canvas.drawLine(1360, 287, 1265, 347, paint);
        //canvas.drawLine(1370, 302, 1375, 407, paint);
        //canvas.drawLine(1390, 302, 1395, 407, paint);
        //canvas.drawLine(1360, 262, 1330, 100, paint);
        //canvas.drawLine(1380, 267, 1435, 180, paint);
        //canvas.drawLine(1400, 277, 1455, 190, paint);
        //canvas.drawLine(1350, 97, 1435, 165, paint);
        //canvas.drawLine(1360, 82, 1445, 150, paint);
        //canvas.drawLine(1367, 427, 1300, 505, paint);
        //canvas.drawLine(1382, 442, 1315, 520, paint);
        //canvas.drawLine(1292, 538, 1213, 607, paint);
        //canvas.drawLine(1281, 524, 1202, 593, paint);
        //canvas.drawLine(1281, 494, 1258, 385, paint);
        //canvas.drawLine(1265, 365, 1365, 415, paint);
        //canvas.drawLine(1230, 365, 995, 502, paint);
        //canvas.drawLine(990, 524, 1090, 554, paint);
        //canvas.drawLine(975, 514, 947, 620, paint);
        //canvas.drawLine(937, 625, 832, 630, paint);
        //canvas.drawLine(837, 640, 850, 747, paint);
        //canvas.drawLine(817, 640, 830, 747, paint);
        //canvas.drawLine(808, 612, 837, 510, paint);
        //canvas.drawLine(828, 615, 857, 513, paint);
        //canvas.drawLine(857, 505, 959, 502, paint);
        //canvas.drawLine(857, 485, 959, 482, paint);
        //canvas.drawLine(964, 475, 1025, 382, paint);
        //canvas.drawLine(980, 485, 1040, 392, paint);
        //canvas.drawLine(937, 640, 867, 740, paint);
        //canvas.drawLine(863, 765, 897, 805, paint);
        //canvas.drawLine(848, 780, 882, 820, paint);
        //canvas.drawLine(917, 820, 1027, 805, paint);//marked
        //canvas.drawLine(1037, 795, 957, 650, paint);
        //canvas.drawLine(1130, 566, 1175, 599, paint);
        //canvas.drawLine(1215, 624, 1325, 630, paint);
        //canvas.drawLine(1200, 634, 1375, 855, paint);
        //canvas.drawLine(850, 475, 827, 430, paint);
        //canvas.drawLine(835, 485, 812, 440, paint);
        //canvas.drawLine(859, 303, 822, 400, paint);
        //canvas.drawLine(840, 300, 804, 398, paint);
        //canvas.drawLine(822, 772, 610, 805, paint);
        //canvas.drawLine(550, 792, 400, 747, paint);
        //canvas.drawLine(413, 729, 560, 664, paint);
        //canvas.drawLine(590, 662, 760, 641, paint);
        //canvas.drawLine(575, 647, 580, 540, paint);
        //canvas.drawLine(575, 677, 570, 784, paint);
        //Curved lines
        //canvas.drawLine(160, 195, 220, 195, paint);//start
        //canvas.drawLine(220, 195, 265, 190, paint);
        //canvas.drawLine(265, 190, 320, 150, paint);
        //canvas.drawLine(320, 150, 340, 105, paint);//end
        //canvas.drawLine(360, 85, 415, 62, paint);//start
        //canvas.drawLine(415, 62, 465, 53, paint);
        //canvas.drawLine(465, 53, 520, 50, paint);
        //canvas.drawLine(520, 50, 580, 53, paint);
        //canvas.drawLine(580, 53, 625, 65, paint);
        //canvas.drawLine(625, 65, 675, 90, paint);//end
        //canvas.drawLine(1065, 165, 1100, 132, paint);//start
        //canvas.drawLine(1100, 132, 1150, 107, paint);
        //canvas.drawLine(1150, 107, 1205, 87, paint);
        //canvas.drawLine(1205, 87, 1255, 77, paint);
        //canvas.drawLine(1255, 77, 1310, 72, paint);//end
        //canvas.drawLine(1325, 90, 1275, 110, paint);//start
        //canvas.drawLine(1275, 110, 1240, 145, paint);
        //canvas.drawLine(1240, 145, 1210, 190, paint);//end
        //canvas.drawLine(1210, 230, 1165, 265, paint);//start
        //canvas.drawLine(1165, 265, 1115, 275, paint);
        //canvas.drawLine(1115, 275, 1070, 310, paint);
        //canvas.drawLine(1070, 310, 1035, 350, paint);//end
        //canvas.drawLine(1055, 350, 1105, 337, paint);//start
        //canvas.drawLine(1105, 337, 1160, 330, paint);
        //canvas.drawLine(1160, 330, 1215, 330, paint);//end
        //canvas.drawLine(1060, 370, 1115, 355, paint);//start
        //canvas.drawLine(1115, 355, 1165, 350, paint);
        //canvas.drawLine(1165, 350, 1220, 350, paint);//end
        //canvas.drawLine(1025, 352, 965, 337, paint);//start
        //canvas.drawLine(965, 337, 915, 320, paint);
        //canvas.drawLine(915, 320, 865, 290, paint);//end
        //canvas.drawLine(1025, 375, 920, 355, paint);//start
        //canvas.drawLine(915, 360, 830, 415, paint);//end
        //canvas.drawLine(795, 425, 745, 435, paint);//start
        //canvas.drawLine(745, 435, 693, 450, paint);
        //canvas.drawLine(693, 450, 643, 470, paint);
        //canvas.drawLine(643, 470, 593, 510, paint);//end
        //canvas.drawLine(613, 520, 663, 525, paint);//start
        //canvas.drawLine(663, 525, 718, 525, paint);
        //canvas.drawLine(718, 525, 768, 515, paint);
        //canvas.drawLine(768, 515, 823, 495, paint);//end
        //canvas.drawLine(613, 542, 663, 545, paint);//start
        //canvas.drawLine(663, 545, 713, 545, paint);
        //canvas.drawLine(713, 545, 773, 535, paint);
        //canvas.drawLine(773, 535, 823, 515, paint);//end
        //canvas.drawLine(596, 550, 636, 590, paint);//start
        //canvas.drawLine(636, 590, 686, 613, paint);
        //canvas.drawLine(686, 613, 736, 618, paint);
        //canvas.drawLine(736, 618, 786, 623, paint);//end
        //canvas.drawLine(801, 638, 768, 678, paint);//start
        //canvas.drawLine(768, 678, 728, 718, paint);
        //canvas.drawLine(728, 718, 678, 750, paint);
        //canvas.drawLine(678, 750, 638, 770, paint);
        //canvas.drawLine(638, 770, 588, 790, paint);//end
        //canvas.drawLine(578, 815, 628, 840, paint);//start
        //canvas.drawLine(628, 840, 673, 855, paint);
        //canvas.drawLine(673, 855, 728, 863, paint);
        //canvas.drawLine(728, 863, 778, 865, paint);
        //canvas.drawLine(778, 865, 828, 860, paint);
        //canvas.drawLine(828, 860, 888, 835, paint);//end
        //canvas.drawLine(538, 802, 488, 815, paint);//start
        //canvas.drawLine(488, 815, 428, 820, paint);
        //canvas.drawLine(428, 820, 373, 812, paint);
        //canvas.drawLine(373, 812, 318, 800, paint);
        //canvas.drawLine(318, 800, 268, 780, paint);
        //canvas.drawLine(268, 780, 218, 740, paint);//end
        //canvas.drawLine(218, 725, 268, 717, paint);//start
        //canvas.drawLine(268, 717, 318, 717, paint);
        //canvas.drawLine(318, 717, 378, 727, paint);//end
        //canvas.drawLine(393, 727, 413, 677, paint);//start
        //canvas.drawLine(413, 677, 443, 627, paint);
        //canvas.drawLine(443, 627, 478, 577, paint);
        //canvas.drawLine(478, 577, 518, 547, paint);
        //canvas.drawLine(518, 547, 563, 537, paint);//end
        //canvas.drawLine(193, 720, 158, 678, paint);//start
        //canvas.drawLine(158, 678, 123, 628, paint);
        //canvas.drawLine(123, 628, 103, 578, paint);//end
        //canvas.drawLine(183, 735, 143, 695, paint);//start
        //canvas.drawLine(143, 695, 108, 640, paint);
        //canvas.drawLine(108, 640, 88, 590, paint);//end
        //canvas.drawLine(78, 550, 63, 500, paint);//start
        //canvas.drawLine(63, 500, 58, 450, paint);
        //canvas.drawLine(58, 450, 63, 395, paint);
        //canvas.drawLine(63, 395, 68, 340, paint);
        //canvas.drawLine(68, 340, 88, 290, paint);//end
        //canvas.drawLine(98, 555, 83, 505, paint);//start
        //canvas.drawLine(83, 505, 78, 455, paint);
        //canvas.drawLine(78, 455, 78, 400, paint);
        //canvas.drawLine(78, 400, 88, 350, paint);
        //canvas.drawLine(88, 350, 108, 295, paint);//end
        //canvas.drawLine(128, 275, 178, 285, paint);//start
        //canvas.drawLine(178, 285, 228, 305, paint);
        //canvas.drawLine(228, 305, 278, 335, paint);
        //canvas.drawLine(278, 335, 328, 380, paint);
        //canvas.drawLine(328, 380, 370, 430, paint);
        //canvas.drawLine(370, 430, 380, 460, paint);//end
        //canvas.drawLine(385, 495, 380, 545, paint);//start
        //canvas.drawLine(380, 545, 360, 600, paint);
        //canvas.drawLine(360, 600, 320, 640, paint);//end
        //canvas.drawLine(285, 645, 235, 655, paint);//start
        //canvas.drawLine(235, 655, 205, 705, paint);//end
        //canvas.drawLine(1080, 810, 1125, 785, paint);//start
        //canvas.drawLine(1125, 785, 1180, 768, paint);
        //canvas.drawLine(1180, 768, 1240, 767, paint);
        //canvas.drawLine(1240, 767, 1290, 797, paint);
        //canvas.drawLine(1290, 797, 1340, 840, paint);
        //canvas.drawLine(1340, 840, 1365, 870, paint);//end
        //canvas.drawLine(1065, 800, 1085, 750, paint);//start
        //canvas.drawLine(1085, 750, 1120, 700, paint);
        //canvas.drawLine(1120, 700, 1160, 653, paint);
        //canvas.drawLine(1160, 653, 1190, 628, paint);//end
        //canvas.drawLine(1050, 790, 1070, 740, paint);//start
        //canvas.drawLine(1070, 740, 1095, 695, paint);
        //canvas.drawLine(1095, 695, 1145, 640, paint);
        //canvas.drawLine(1145, 640, 1175, 615, paint);//end
        //canvas.drawLine(1390, 840, 1365, 790, paint);//start
        //canvas.drawLine(1365, 790, 1350, 740, paint);
        //canvas.drawLine(1350, 740, 1340, 690, paint);
        //canvas.drawLine(1340, 690, 1340, 635, paint);//end
        //canvas.drawLine(1340, 610, 1365, 565, paint);//start
        //canvas.drawLine(1355, 570, 1305, 535, paint);//end
        //canvas.drawLine(1280, 510, 1230, 500, paint);//start
        //canvas.drawLine(1230, 500, 1175, 515, paint);
        //canvas.drawLine(1175, 515, 1125, 545, paint);//end
        //canvas.drawLine(1100, 545, 1125, 495, paint);//start
        //canvas.drawLine(1125, 495, 1160, 455, paint);
        //canvas.drawLine(1160, 455, 1210, 420, paint);
        //canvas.drawLine(1210, 420, 1240, 380, paint);//end
        //canvas.drawLine(1110, 570, 1070, 605, paint);//start
        //canvas.drawLine(1070, 605, 1020, 625, paint);
        //canvas.drawLine(1020, 625, 965, 630, paint);
    }

}