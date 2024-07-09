package com.example.shirtsalesapp.activity.product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shirtsalesapp.R;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateProductActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editTextTitle, editTextProductName, editTextProductDescription,
            editTextProductPrice, editTextProductQuantity, editTextCategoryId;
    private Button buttonSelectImage, buttonSubmit;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextProductName = findViewById(R.id.editTextProductName);
        editTextProductDescription = findViewById(R.id.editTextProductDescription);
        editTextProductPrice = findViewById(R.id.editTextProductPrice);
        editTextProductQuantity = findViewById(R.id.editTextProductQuantity);
        editTextCategoryId = findViewById(R.id.editTextCategoryId);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createProduct();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
        }
    }

    private void createProduct() throws IOException {
        String title = editTextTitle.getText().toString().trim();
        String productName = editTextProductName.getText().toString().trim();
        String productDescription = editTextProductDescription.getText().toString().trim();
        String productPrice = editTextProductPrice.getText().toString().trim();
        String productQuantity = editTextProductQuantity.getText().toString().trim();
        String categoryId = editTextCategoryId.getText().toString().trim();

        if (title.isEmpty() || productName.isEmpty() || productDescription.isEmpty()
                || productPrice.isEmpty() || productQuantity.isEmpty() || categoryId.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Title", title)
                .addFormDataPart("ProductName", productName)
                .addFormDataPart("Description", productDescription)
                .addFormDataPart("Price", productPrice)
                .addFormDataPart("Quantity", productQuantity)
                .addFormDataPart("CategoryId", categoryId)
                .addFormDataPart("Image", "image.jpg",
                        RequestBody.create(MediaType.parse("image/jpeg"),
                                getContentResolver().openInputStream(imageUri).toString()))
                .build();

        Request request = new Request.Builder()
                .url("https://realityprint.somee.com/api/Product/CreateProduct")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(CreateProductActivity.this, "Failed to create product", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(CreateProductActivity.this, "Product created successfully", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(CreateProductActivity.this, "Failed to create product", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
