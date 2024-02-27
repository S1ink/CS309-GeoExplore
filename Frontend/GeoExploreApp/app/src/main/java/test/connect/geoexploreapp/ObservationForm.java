package test.connect.geoexploreapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ObservationForm extends AppCompatActivity {
    private EditText editTextTitle, editTextDescription, editTextLocation;
    private Button buttonCancel, buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_form);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonCreate = findViewById(R.id.buttonCreate);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(ObservationForm.this, MapsActivity.class);
//                startActivity(intent);

                finish();
            }
        });

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the create action
                createObservation();
            }
        });
    }

    private void createObservation() {
        // Logic to handle creation, e.g., validation and saving data
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
       // String location = editTextLocation.getText().toString();


    }
}
