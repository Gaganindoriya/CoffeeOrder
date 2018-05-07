package com.example.android.coffeeorder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    int quantity = 1;
    LinearLayout linearLayout;
    CheckBox whippedCreamCheckBox;
    CheckBox chocolateCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.back);
        whippedCreamCheckBox = (CheckBox) findViewById(R.id.Whippedcream_checkbox);
        chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);

        whippedCreamCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (whippedCreamCheckBox.isChecked()) {
                    linearLayout.setBackgroundResource(R.drawable.whipped);
                } else {
                    if (chocolateCheckBox.isChecked()) {
                        linearLayout.setBackgroundResource(R.drawable.chocolate);
                    } else {
                        linearLayout.setBackgroundResource(R.drawable.simple);
                    }
                }
            }
        });
        chocolateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chocolateCheckBox.isChecked()) {
                    linearLayout.setBackgroundResource(R.drawable.chocolate);
                } else {
                    if (whippedCreamCheckBox.isChecked()) {
                        linearLayout.setBackgroundResource(R.drawable.whipped);
                    } else {
                        linearLayout.setBackgroundResource(R.drawable.simple);
                    }
                }
            }
        });

    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "You can not have more than 100 coffes", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Toast.makeText(this, "You acn not have less than 1 coffes", Toast.LENGTH_SHORT).show();
            return;
        }

        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Find the user name
        EditText txtname = (EditText) findViewById(R.id.name_field);
        String name = txtname.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show();
        } else {
            Log.v("MainActivity", "Name:" + name);
            // Figure out if the user wants whipped cream topping

            boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

            // Figure out if the user wants chocolate topping

            boolean hasChocolate = chocolateCheckBox.isChecked();

            // Calculate the price
            int price = calculatePrice(hasWhippedCream, hasChocolate);

            // Display the order summary on the screen
            String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate);


            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this

            intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for" + name);
            intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    /**
     * Calculates the price of the order.
     * addWhippedCream is whether or not the user wants whipped cream topping
     * addChocolate is whether or not the user wants chocolate topping
     *
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;
        if (addWhippedCream) {
            basePrice = basePrice + 1;
        }
        if (addChocolate) {
            basePrice = basePrice + 2;
        }
        return quantity * basePrice;
    }

    /**
     * Create summary of the order.
     *
     * @param price           of the order
     * @param addWhippedCream is whether or not to add whipped cream to the coffee
     * @param addChocolate    is whether or not to add chocolate to the coffee
     * @return text summary
     */
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {

        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage += "\nAdd whipped cream? " + addWhippedCream;
        priceMessage += "\nAdd chocolate? " + addChocolate;
        priceMessage += "\nQuantity: " + quantity;
        priceMessage += "\n" + getString(R.string.order_summary_price);
        priceMessage += NumberFormat.getCurrencyInstance().format(price);
        priceMessage += "\n" + getString(R.string.thank_you);
        return priceMessage;

    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity);
        quantityTextView.setText("" + numberOfCoffees);
    }

}