package taquin.dciss.miashs.taquinex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    public Button but1 ;

    public Button but2;

    public void init(){
        but1 = (Button) findViewById(R.id.jeu);
        but1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent toy = new Intent(FirstActivity.this,MainActivity.class);
                startActivity(toy);


            }
        });
    }

    public void init2(){
        but1 = (Button) findViewById(R.id.button3);
        but1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent toy2 = new Intent(FirstActivity.this,RegleActivity.class);
                startActivity(toy2);


            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        init();
        init2();
    }
}
