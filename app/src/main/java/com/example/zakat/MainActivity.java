package com.example.zakat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerType;
    private EditText weightEditText, valueEditText;
    private TextView resultTextView, textViewMustPayZakat, textViewGoldValueZakatPayable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        weightEditText = findViewById(R.id.editTextWeight);
        valueEditText = findViewById(R.id.editTextValue);
        resultTextView = findViewById(R.id.textViewResult);
        textViewMustPayZakat = findViewById(R.id.textViewMustPayZakat);
        textViewGoldValueZakatPayable = findViewById(R.id.textViewGoldValueZakatPayable);

        spinnerType = findViewById(R.id.spinnerType);

        // Populating the spinner with options
        String[] types = {"Keep", "Wear"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        // Spinner item selection listener
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                // You can do something with the selectedType if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection if needed
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Please use my application -https://github.com/IkhwanRodzi/ZakatCalculator/tree/master");
            startActivity(Intent.createChooser(shareIntent, null));
            return true;
        } else if (item.getItemId() == R.id.item_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void calculateZakat(View view) {
        String weightStr = weightEditText.getText().toString();
        String valueStr = valueEditText.getText().toString();

        if (weightStr.isEmpty() || valueStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int weight = Integer.parseInt(weightStr);
        double value = Double.parseDouble(valueStr);

        String selectedType = spinnerType.getSelectedItem().toString();

        // Calculate total gold value by multiplying weight with value
        double totalGoldValue = weight * value;
        resultTextView.setText(String.format("Total Gold Value: RM %.2f", totalGoldValue));

        // Calculate total must-pay zakat and total gold value that is zakat payable
        int x = (selectedType.equals("Keep")) ? 85 : 200;
        int weightMinusX = weight - x;
        double totalGoldValueZakatPayable = Math.max(0, weightMinusX) * value;
        double totalMustPayZakat = 0.025 * totalGoldValueZakatPayable;

        // Display the calculated values in TextViews
        textViewMustPayZakat.setText(String.format("Total Must-Pay Zakat: RM %.2f", totalMustPayZakat));
        textViewGoldValueZakatPayable.setText(String.format("Total Gold Value Zakat Payable: RM %.2f", totalGoldValueZakatPayable));
    }

    public void openGitHubLink(View view) {
        Uri uri = Uri.parse("https://github.com/IkhwanRodzi/ZakatCalculator/tree/master");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
