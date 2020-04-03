package com.example.post_pc_sam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.skyfishjy.library.RippleBackground;

public class MainActivity extends AppCompatActivity {
    public boolean animation_on = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animation_on){
                    rippleBackground.stopRippleAnimation();
                }
                else{
                    rippleBackground.startRippleAnimation();
                }
                animation_on = !animation_on;
            }
        });


    }
}
