package Nhom2.com.example.doanmobile.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import Nhom2.com.example.doanmobile.R;

public class MainActivity extends AppCompatActivity {

    // Firestore instance
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firestore instance
        db = FirebaseFirestore.getInstance();
    }


}

