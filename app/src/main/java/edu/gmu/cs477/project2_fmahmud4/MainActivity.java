package edu.gmu.cs477.project2_fmahmud4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import edu.gmu.cs477.project2_fmahmud4.Fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private FrameLayout main_frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        main_frame = findViewById(R.id.main_frame);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, new HomeFragment());
        transaction.commit();

    }
}