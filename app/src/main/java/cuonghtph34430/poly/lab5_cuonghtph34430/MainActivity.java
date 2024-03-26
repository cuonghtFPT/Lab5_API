package cuonghtph34430.poly.lab5_cuonghtph34430;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import cuonghtph34430.poly.lab5_cuonghtph34430.API.APIService;
import cuonghtph34430.poly.lab5_cuonghtph34430.Adapter.AdapterShoe;
import cuonghtph34430.poly.lab5_cuonghtph34430.DTO.ShoeDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    static List<ShoeDTO> list = new ArrayList<>();
    static AdapterShoe adapterShoe;
    static RecyclerView recyclerView;
    FloatingActionButton floaAdd;
    EditText edtSearch;
    Button btnSeacrh;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rcvList);
        floaAdd = findViewById(R.id.floatAdd);
        edtSearch=findViewById(R.id.edtSearch);
        btnSeacrh=findViewById(R.id.btnSearch);
        //Connect
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Call Api Retrofit
        CallAPI(retrofit);


        //setOnclick add
        floaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewCreateAndAddActivity.class);
                intent.putExtra("titleAdd", "Create shoe");
                intent.putExtra("titleBtnAdd", "Create");
                startActivity(intent);
                finish();
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Perform search whenever text changes
                filter(s.toString().trim());
            }
        });
    }

    public static void CallAPI(Retrofit retrofit) {

        //Khai báo API Service
        APIService apiService = retrofit.create(APIService.class);

        Call<List<ShoeDTO>> call = apiService.getShoe();

        call.enqueue(new Callback<List<ShoeDTO>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<List<ShoeDTO>> call, @NonNull Response<List<ShoeDTO>> response) {
                if (response.isSuccessful()) {
                    list = response.body();
                    adapterShoe = new AdapterShoe(recyclerView.getContext(), list);
                    LinearLayoutManager linearLayoutManager =
                            new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(adapterShoe);
                    adapterShoe.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ShoeDTO>> call, @NonNull Throwable t) {
                Log.e("zzzz", "onFailure: " + t.getMessage());
            }
        });
    }
    private void filter(String text) {
        ArrayList<ShoeDTO> filteredList = new ArrayList<>();
        for (ShoeDTO item : list) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapterShoe.updateData(filteredList);
    }


}