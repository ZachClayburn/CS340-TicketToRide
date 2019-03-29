package com.tickettoride.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tickettoride.clientModels.ClientRoute;
import com.tickettoride.clientModels.DataManager;
import com.tickettoride.clientModels.helpers.RouteHelper;
import com.tickettoride.models.Route;

import java.util.ArrayList;
import java.util.List;

public class DrawView extends View {
    Paint paint = new Paint();

    private void init() {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(14);
    }

    public DrawView(Context context) {
        super(context);
        init();
    }


    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    public ClientRoute clickRoute(int x, int y){//todo: actually make this work with the claimroute funtionallity
        List<ClientRoute> clientRoutes = RouteHelper.getSingleton().playerFilteredRoutes();
        for(ClientRoute clientRoute : clientRoutes){
            for(int i=x-1;i<x+2;i++) {
                for(int j=y;j<y+2;j++) {
                    if (clientRoute.contains(i, j)) {
                        if (clientRoute.getIsClaimed()) {
                            //it's already been claimed
                            Log.i("TAG", "got claimed one!");
                            return null;//mainly for testing
                        } else { return clientRoute; }
                    }
                }
            }
        }
        return null;//mainly for testing
    }
    
    
    @Override
    public void onDraw(Canvas canvas) {
        List<ClientRoute> clientRoutes = DataManager.getSINGLETON().getClientRoutes();
        for (ClientRoute clientRoute : clientRoutes) { clientRoute.drawRoute(canvas); }
    }

}