package com.example.practiseprojec1;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

public class question1part extends Activity {
    CardView cardView1,cardView2,cardView3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question1part);
        cardView1=findViewById(R.id.firstCard);
        cardView2=findViewById(R.id.second_card);
        cardView3=findViewById(R.id.third_card);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView1.setCardBackgroundColor(getResources().getColor(android.R.color.holo_orange_dark));
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }


}
