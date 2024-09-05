package ru.morozovit.recipeviewer.mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.morozovit.recipeviewer.mobile.databinding.RecipeEditBinding;

public class RecipeDataEditActivity extends AppCompatActivity {
    public RecipeEditBinding binding;

    /**
     * @noinspection DataFlowIssue */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RecipeEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Load recipe data
        Bundle b = getIntent().getExtras();
        if (b != null) {
            binding.recipeDataEditName.getEditText().setText(b.getString("name"));
            binding.recipeDataEditDesc.getEditText().setText(b.getString("desc"));
            binding.recipeDataEditSave.setOnClickListener(v -> {
                Intent data = new Intent();
                data.putExtra("name", binding.recipeDataEditName.getEditText().getText().toString());
                data.putExtra("desc", binding.recipeDataEditDesc.getEditText().getText().toString());
                setResult(RESULT_OK, data);
                finish();
            });
            binding.recipeDataEditBack.setOnClickListener(v -> onBackPressed());
        }
    }

    /** @noinspection deprecation*/
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
