package com.tickettoride.activities;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.tickettoride.R;

public class ViewMapFragment extends Fragment {
    private ImageView map;
    private OnReturnToDestListener destListener;
    private Button returnDest;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_map, container, false);
        map = (ImageView) v.findViewById(R.id.map);
        Bitmap bitmap = ((GameRoomActivity) getActivity()).getMapFragment().getBitmap();
        map.setImageBitmap(bitmap);
        returnDest = (Button) v.findViewById(R.id.return_dest);
        returnDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destListener = (OnReturnToDestListener) getActivity();
                destListener.moveToDest();
            }
        });
        return v;
    }

}
