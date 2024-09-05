package ru.morozovit.recipeviewer.mobile;

import static android.app.Activity.RESULT_OK;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.BaseProgressIndicator;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import ru.morozovit.recipeviewer.mobile.databinding.LibraryFragmentBinding;
import ru.morozovit.util.android.BetterActivityResult;

public class LibraryFragment extends Fragment {
    private LibraryFragmentBinding binding;
    public ItemAdapter adapter;
    protected BetterActivityResult<Intent, ActivityResult> activityLauncher = null;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = LibraryFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity activity = MainActivity.getInstance();
        activityLauncher = BetterActivityResult.registerActivityForResult(this);

        binding.libCreate.setOnClickListener(v -> {
            Intent intent = new Intent(activity, CreateActivity.class);
            startActivity(intent);
        });

        // Set up list
        binding.listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        List<Item> items = new ArrayList<>();
        adapter = new ItemAdapter(items, binding.listView, this);
        binding.listView.setAdapter(adapter);

        binding.libSwiperefresh.setOnRefreshListener(this::loadRecipes);
        loadRecipes();
    }

    public void loadRecipes() {
        RecipeDatabase db = RecipeDatabase.getInstance();
        try {
            binding.libSwiperefresh.setRefreshing(true);
            adapter.clear();
            ArrayList<JSONObject> recipes = db.getAllRecipes();
            for (JSONObject recipe : recipes) {
                String recipeName = recipe.getString("name");
                StringBuilder contentBuilder = new StringBuilder();
                JSONArray ingredients = recipe.getJSONObject("body").getJSONArray("ingredients");
                for (int i = 0; i < ingredients.length(); i++) {
                    JSONObject ingredient = ingredients.getJSONObject(i);
                    String name = ingredient.getString("name");
                    double count = ingredient.getDouble("count");
                    String unit = ingredient.getString("count2");
                    contentBuilder.append(name).append(" x ").append(count).append(" ").append(unit);
                    if (i != ingredients.length() - 1) {
                        contentBuilder.append(", ");
                    }
                }
                String content = contentBuilder.toString();
                adapter.addItem(new Item(recipeName, content, v -> {
                    ModalBottomSheet bottomSheet = new ModalBottomSheet(recipe, this);
                    //noinspection DataFlowIssue
                    bottomSheet.show(getActivity().getSupportFragmentManager(), ModalBottomSheet.TAG);
                }));
            }
            binding.libSwiperefresh.setRefreshing(false);
        } catch (Exception e) {
            Snackbar.make(
                            binding.libMainLayout,
                            R.string.error_loading_recipes,
                            Snackbar.LENGTH_SHORT)
                    .setAnchorView(binding.libCreate)
                    .show();
        }
    }

    public static class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
        private final List<Item> items;
        private final List<ViewHolder> selectedItems = new ArrayList<>();
        private boolean selectMode = false;
        public final RecyclerView recyclerView;
        public final LibraryFragment fragment;
        public final View.OnLongClickListener ENTER_SELECT_MODE = v -> {
            CheckBox checkbox = v.findViewById(R.id.item_checkbox);
            enterSelectMode(checkbox);
            return true;
        };
        @Nullable
        private ActionMode actionMode = null;

        public ItemAdapter(List<Item> items, RecyclerView recyclerView, LibraryFragment fragment) {
            this.items = items;
            this.recyclerView = recyclerView;
            this.fragment = fragment;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView headline;
            private final TextView content;
            private final CheckBox checkBox;
            private boolean selected = false;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                headline = itemView.findViewById(R.id.item_headline);
                content = itemView.findViewById(R.id.item_content);
                checkBox = itemView.findViewById(R.id.item_checkbox);
            }

            public void toggleSelection() {
                setSelected(!selected);
            }

            public void setSelected(boolean selected) {
                this.selected = selected;
                checkBox.setChecked(selected);
            }

            public boolean isSelected() {
                return selected;
            }
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Item item = items.get(position);
            holder.headline.setText(item.getHeadline());
            holder.content.setText(item.getContent());
            holder.checkBox.setVisibility(selectMode ? View.VISIBLE : View.GONE);
            holder.checkBox.setChecked(holder.selected);
            holder.itemView.setOnClickListener(item.getOnClick());
            holder.itemView.setOnLongClickListener(ENTER_SELECT_MODE);
        }

        /** @noinspection DataFlowIssue*/
        public void enterSelectMode(CheckBox startItem) {
            ActionMode.Callback callback = new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    mode.getMenuInflater().inflate(R.menu.lib_selectmode, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    if (item.getItemId() == R.id.lib_selectmode_delete) {
                        new MaterialAlertDialogBuilder(fragment.getActivity())
                                .setTitle(R.string.delete_recipes)
                                .setMessage(R.string.delete_recipes_desc)
                                .setNegativeButton(R.string.no, null)
                                .setPositiveButton(R.string.yes, (dialog, which) -> {
                                    SwipeRefreshLayout swipeRefresh = fragment.getActivity().findViewById(R.id.lib_swiperefresh);
                                    for (ViewHolder selectedItem : selectedItems) {
                                        String recipeName = String.valueOf(selectedItem.headline.getText());
                                        RecipeDatabase db = RecipeDatabase.getInstance();
                                        try {
                                            db.deleteRecipe(recipeName);
                                        } catch (Exception e) {
                                            Snackbar.make(
                                                            fragment.getActivity().findViewById(R.id.libMainLayout),
                                                            R.string.error_deleting_recipe,
                                                            Snackbar.LENGTH_SHORT)
                                                    .setAnchorView(fragment.getActivity().findViewById(R.id.lib_create))
                                                    .show();
                                            break;
                                        }
                                    }
                                    swipeRefresh.post(fragment::loadRecipes);
                                })
                                .show();
                        return true;
                    } else if (item.getItemId() == R.id.lib_selectmode_export) {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        fragment.activityLauncher.launch(intent, result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                Uri treeUri = result.getData().getData();
                                ContentResolver resolver = fragment.getActivity().getContentResolver();
                                for (int i = 0; i < getItemCount(); i++) {
                                    ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                                    if (holder != null && holder.isSelected()) {
                                        String recipeName = String.valueOf(holder.headline.getText());
                                        JSONObject recipe;
                                        try {
                                            RecipeDatabase db = RecipeDatabase.getInstance();
                                            recipe = db.getRecipe(recipeName);

                                            String fileName = recipe.getString("name") + ".json";
                                            String data = recipe.getJSONObject("body").toString(2);
                                            Uri newFileUri = DocumentFile.fromTreeUri(fragment.getContext(), treeUri).createFile("application/json", fileName).getUri();
                                            try (OutputStream outputStream = resolver.openOutputStream(newFileUri)) {
                                                if (outputStream != null) {
                                                    outputStream.write(data.getBytes());
                                                }
                                            }
                                        } catch (Exception e) {}
                                    }
                                }
                            }
                        });
                    } else if (item.getItemId() == R.id.lib_selectmode_selectall) {
                        for (int i = 0; i < getItemCount(); i++) {
                            ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                            if (holder != null) {
                                holder.setSelected(true);
                            }
                        }
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    exitSelectMode();
                    actionMode = null;
                }
            };
            actionMode = ((AppCompatActivity) fragment.getActivity()).startSupportActionMode(callback);
            actionMode.setTitle("0 selected");
            for (int i = 0; i < getItemCount(); i++) {
                ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                if (holder != null) {
                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.checkBox.setOnCheckedChangeListener((v, s) -> {
                        holder.selected = s;
                        if (s) {
                            selectedItems.add(holder);
                        } else {
                            selectedItems.remove(holder);
                        }
                        actionMode.setTitle(selectedItems.size() + " selected");
                        if (selectedItems.isEmpty()) exitSelectMode();
                    });
                    holder.itemView.setOnClickListener(v -> holder.toggleSelection());
                    holder.itemView.setOnLongClickListener(null);
                }
            }
            startItem.setChecked(true);
            selectMode = true;
        }

        public void exitSelectMode() {
            try {
                //noinspection DataFlowIssue
                actionMode.finish();
            } catch (Exception e) {}

            for (int i = 0; i < getItemCount(); i++) {
                ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                if (holder != null) {
                    holder.checkBox.setVisibility(View.GONE);
                    holder.checkBox.setOnCheckedChangeListener(null);
                    holder.checkBox.setChecked(false);
                    holder.selected = false;
                    holder.itemView.setOnClickListener(items.get(i).getOnClick());
                    holder.itemView.setOnLongClickListener(ENTER_SELECT_MODE);
                }
            }
            selectedItems.clear();
            selectMode = false;
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItem(Item item) {
            items.add(item);
            notifyItemInserted(items.size() - 1);
        }
        
        /** @noinspection unused*/
        public void deleteItem(Item item) {
            int position = items.indexOf(item);
            if (position != -1) {
                items.remove(position);
                notifyItemRemoved(position);
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        public void clear() {
            exitSelectMode();
            items.clear();
            notifyDataSetChanged();
        }
    }

    /** */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (adapter != null)
            if (adapter.actionMode != null)
                adapter.actionMode.finish();
    }

    public static class ModalBottomSheet extends BottomSheetDialogFragment {
        public static final String TAG = "ModalBottomSheet";
        private final JSONObject recipe;
        private final LibraryFragment fragment;
        private CoordinatorLayout view;
        private BottomSheetBehavior<LinearLayout> behavior;

        public ModalBottomSheet(JSONObject recipe, LibraryFragment fragment) {
            this.recipe = recipe;
            this.fragment = fragment;
        }

        @Override
        public View onCreateView(
                @NonNull LayoutInflater inflater,
                ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.recipe_detail, container, false);
        }

        /** @noinspection deprecation*/
        @SuppressLint("SetTextI18n")
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            behavior = BottomSheetBehavior.from((LinearLayout) ((CoordinatorLayout) view).getChildAt(0));
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            this.view = (CoordinatorLayout) view;
            CircularProgressIndicator spinner = view.findViewById(R.id.recipeDetail_spinner);

            LoadTask task = new LoadTask();
            Runnable check_progress = () -> {
                if (spinner.getProgress() > 98) {
                    task.cancel(true);
                    spinner.post(() -> {
                        spinner.setHideAnimationBehavior(BaseProgressIndicator.HIDE_NONE);
                        spinner.setVisibilityAfterHide(View.GONE);
                        spinner.hide();
                        spinner.setVisibility(View.GONE);
                    });
                    LinearLayout mainLayout = view.findViewById(R.id.recipeDetail_mainLayout);
                    mainLayout.setVisibility(View.VISIBLE);
                }
            };
            task.execute();
            for (int i = 100; i <= 500; i += 100) {
                view.postDelayed(check_progress, i);
            }
        }

        /** @noinspection deprecation*/
        @SuppressLint("StaticFieldLeak")
        private class LoadTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                behavior.setHideable(false);
                behavior.setDraggable(false);
                update();
                behavior.setHideable(true);
                behavior.setDraggable(true);
                return null;
            }
        }

        /** @noinspection DataFlowIssue */
        @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
        public void update() {
            try {
                // Set up spinner
                LinearLayout mainLayout = view.findViewById(R.id.recipeDetail_mainLayout);
                LinearLayout topLayout = (LinearLayout) mainLayout.getParent();
                CircularProgressIndicator spinner = view.findViewById(R.id.recipeDetail_spinner);
                spinner.post(() -> {
                    spinner.show();
                    spinner.setHideAnimationBehavior(BaseProgressIndicator.HIDE_OUTWARD);
                });

                // Variables
                TextView name = view.findViewById(R.id.recipeDetail_name);
                TextView desc = view.findViewById(R.id.recipeDetail_desc);
                Button edit = view.findViewById(R.id.recipeDetail_editRecipe);
                Button editInfo = view.findViewById(R.id.recipeDetail_edit);
                Button remove = view.findViewById(R.id.recipeDetail_remove);
                LinearLayout ingredients = view.findViewById(R.id.recipeDetail_ingredients);
                MaterialCardView ingredientsCard = view.findViewById(R.id.recipeDetail_ingredientsCard);
                Button share = view.findViewById(R.id.recipeDetail_share);
                Button export = view.findViewById(R.id.recipeDetail_export);
                Button viewButton = view.findViewById(R.id.recipeDetail_view);
                HorizontalScrollView actionsScroll = view.findViewById(R.id.recipeDetail_actionsScroll);
                LinearLayout actions = (LinearLayout) actionsScroll.getChildAt(0);

                // Name & description
                name.setText(recipe.getString("name"));
                try {
                    String desc1 = recipe.getString("desc");
                    if (desc1.isEmpty()) throw new Exception();
                    desc.setText(desc1);
                } catch (Exception ignored) {}

                // Buttons
                edit.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), CreateActivity.class);
                    Bundle args = new Bundle();
                    args.putString("recipe", recipe.toString());
                    intent.putExtras(args);
                    this.fragment.activityLauncher.launch(intent, result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            update();
                            SwipeRefreshLayout swipeRefresh = getActivity().findViewById(R.id.lib_swiperefresh);
                            swipeRefresh.post(fragment::loadRecipes);
                        }
                    });
                });
                editInfo.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(getActivity(), RecipeDataEditActivity.class);
                        Bundle args = new Bundle();
                        args.putString("name", recipe.getString("name"));
                        try {
                            args.putString("desc", recipe.getString("desc"));
                        } catch (JSONException e) {
                            args.putString("desc", "");
                        }
                        intent.putExtras(args);
                        this.fragment.activityLauncher.launch(intent, result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                try {
                                    String oldName = recipe.getString("name");
                                    Intent data = result.getData();
                                    String newName = data.getStringExtra("name");
                                    String newDesc = data.getStringExtra("desc");
                                    if (!(oldName.equals(newName))) {
                                        recipe.put("name", newName);
                                    }
                                    if (!(newDesc.equals(recipe.getString("desc")))) {
                                        recipe.put("desc", newDesc);
                                    }
                                    update();
                                    RecipeDatabase db = RecipeDatabase.getInstance();
                                    db.deleteRecipe(oldName);
                                    db.saveRecipe(recipe);
                                    SwipeRefreshLayout swipeRefresh = getActivity().findViewById(R.id.lib_swiperefresh);
                                    swipeRefresh.post(fragment::loadRecipes);
                                } catch (Exception e) {
                                    dismiss();
                                    Snackbar.make(
                                                    getActivity().findViewById(R.id.libMainLayout),
                                                    R.string.error_saving_recipe_details,
                                                    Snackbar.LENGTH_SHORT)
                                            .setAnchorView(getActivity().findViewById(R.id.lib_create))
                                            .show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        dismiss();
                        Snackbar.make(
                                fragment.getView().findViewById(R.id.libMainLayout),
                                R.string.error_loading_recipe,
                                Snackbar.LENGTH_SHORT)
                                .setAnchorView(fragment.getView().findViewById(R.id.lib_create))
                                .show();
                    }
                });
                share.setOnClickListener(v -> {
                    try {
                        String recipeString = recipe.getJSONObject("body").toString(2);
                        String recipeName = recipe.getString("name") + ".json";

                        // Cache recipe file
                        File cacheDir = getActivity().getCacheDir();
                        File file = new File(cacheDir, recipeName);
                        FileOutputStream outputStream = new FileOutputStream(file);
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                        bufferedWriter.write(recipeString);
                        bufferedWriter.close();

                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        emailIntent.setType("application/json");
                        // emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"Share File"});
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, recipeName);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, recipeString);
                        emailIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                                getActivity(),
                                getActivity().getPackageName() + ".provider",
                                file
                        ));
                        startActivity(Intent.createChooser(emailIntent, "Share recipe"));
                    } catch (Exception e) {}
                });
                export.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("application/json");
                        intent.putExtra(Intent.EXTRA_TITLE, recipe.getString("name") + ".json");
                        fragment.activityLauncher.launch(intent, result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                Uri uri = result.getData().getData();
                                try (OutputStream outputStream = getActivity().getContentResolver().openOutputStream(uri)) {
                                    outputStream.write(recipe.getJSONObject("body").toString(2).getBytes());
                                    outputStream.flush();
                                } catch (Exception e) {}
                            }
                        });
                    } catch (Exception e) {}
                });
                remove.setOnClickListener(v -> {
                    //noinspection DataFlowIssue
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle(R.string.delete_recipe)
                            .setMessage(R.string.delete_recipe_desc)
                            .setPositiveButton(R.string.yes, (dialog, which) -> {
                                RecipeDatabase db = RecipeDatabase.getInstance();
                                try {
                                    db.deleteRecipe(recipe.getString("name"));
                                } catch (Exception ex) {
                                    Snackbar.make(
                                                    getActivity().findViewById(R.id.libMainLayout),
                                                    R.string.error_deleting_recipe,
                                                    Snackbar.LENGTH_SHORT)
                                            .setAnchorView(getActivity().findViewById(R.id.lib_create))
                                            .show();
                                }
                                dismiss();
                                SwipeRefreshLayout swipeRefresh = getActivity().findViewById(R.id.lib_swiperefresh);
                                swipeRefresh.post(fragment::loadRecipes);
                            })
                            .setNegativeButton(R.string.no, null)
                            .show();
                });
                viewButton.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(getActivity(), ViewActivity.class);
                        Bundle args = new Bundle();
                        args.putString("name", recipe.getString("name"));
                        ArrayList<IngredientData> ingredientsList = new ArrayList<>();
                        JSONArray ingredients2 = recipe.getJSONObject("body").getJSONArray("ingredients");
                        for (int i = 0; i < ingredients2.length(); i++) {
                            JSONObject ing = ingredients2.getJSONObject(i);
                            ingredientsList.add(
                                    new IngredientData(
                                            ing.getString("name"),
                                            ing.getDouble("count"),
                                            ing.getString("count2")
                                    )
                            );
                        }
                        args.putSerializable("ingredients", ingredientsList);
                        ArrayList<String> stepsList = new ArrayList<>();
                        JSONArray array = recipe.getJSONObject("body").getJSONArray("steps");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject step = array.getJSONObject(i);
                            String stepString = step.getString("step"+(i+1));
                            stepsList.add(stepString);
                        }
                        args.putSerializable("steps", stepsList);
                        intent.putExtras(args);
                        this.fragment.activityLauncher.launch(intent, result -> {});
                    } catch (Exception e) {
                        dismiss();
                        Snackbar.make(
                                fragment.getView().findViewById(R.id.libMainLayout),
                                R.string.error_loading_recipe,
                                Snackbar.LENGTH_SHORT)
                                .setAnchorView(fragment.getView().findViewById(R.id.lib_create))
                                .show();
                    }
                });
                actionsScroll.post(() -> view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        actions.setMinimumWidth(actionsScroll.getWidth());
                    }
                }));

                // Ingredients preview
                ingredients.removeAllViews();
                JSONArray ingredientsArray = recipe.getJSONObject("body").getJSONArray("ingredients");
                int progressAdd = 100 / ingredientsArray.length();
                for (int i = 0; i < ingredientsArray.length(); i++) {
                    JSONObject ingredient = ingredientsArray.getJSONObject(i);
                    String ingName = ingredient.getString("name");
                    String ingCount = ingredient.getString("count");
                    String ingUnit = ingredient.getString("count2");

                    LinearLayout ingView = (LinearLayout)
                            LayoutInflater.from(getContext())
                                    .inflate(R.layout.ingredient, ingredients, false);
                    ingView.removeView(ingView.getChildAt(4));
                    TextView ingNumber = (TextView) ingView.getChildAt(0);
                    ingNumber.setText("#"+(i+1));
                    TextInputLayout ingNameView = (TextInputLayout) ingView.getChildAt(1);
                    ingNameView.post(() -> {
                        ingNameView.getEditText().setText(ingName);
                        ingNameView.getEditText().setEnabled(false);
                    });

                    TextInputLayout ingCountView = (TextInputLayout) ingView.getChildAt(2);
                    ingCountView.post(() -> {
                        ingCountView.getEditText().setText(ingCount);
                        ingCountView.getEditText().setEnabled(false);
                    });

                    TextInputLayout ingUnitView = (TextInputLayout) ingView.getChildAt(3);
                    ingUnitView.post(() -> {
                        ingUnitView.getEditText().setText(ingUnit);
                        ingUnitView.getEditText().setEnabled(false);
                    });
                    ingredients.post(() -> ingredients.addView(ingView));
                    if (i != ingredientsArray.length() - 1) {
                        ((LinearLayout.LayoutParams) ingView.getLayoutParams()).bottomMargin = getResources().getDimensionPixelSize(R.dimen.padding_medium);
                    }
                    int finalI = i;
                    spinner.post(() -> spinner.setProgress(progressAdd * (finalI + 1), true));
                }

                // Show more ingredients
                ingredientsCard.setOnClickListener(v -> ingredientsCard.post(() -> {
                    Intent intent = new Intent(getActivity(), IngredientsActivity.class);
                    Bundle args = new Bundle();
                    args.putString("recipe", recipe.toString());
                    intent.putExtras(args);
                    ingredients.setTransitionName("ingredients");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                            getActivity(),
                            ingredients,
                            "ingredients");
                    getActivity().startActivity(intent, options.toBundle());
                }));

                // Remove the spinner and show the content
                topLayout.post(() -> {
                    spinner.setProgress(100, true);
                    spinner.setVisibilityAfterHide(View.INVISIBLE);
                    spinner.hide();
                    mainLayout.postDelayed(() -> {
                        mainLayout.setLayoutTransition(new LayoutTransition());
                        mainLayout.setVisibility(View.VISIBLE);
                        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                spinner.setVisibility(View.GONE);
                                /* mainLayout.setTranslationY(mainLayout.getHeight());
                                mainLayout.setAlpha(0);
                                mainLayout
                                        .animate()
                                        .translationY(0)
                                        .translationX(0)
                                        .alpha(1)
                                        .setDuration(1000)
                                        .start(); */
                            }
                        });
                    }, 600);
                });
            } catch (Exception e) {
                dismiss();
                try {
                    Snackbar.make(
                                    getActivity().findViewById(R.id.libMainLayout),
                                    R.string.error_loading_recipe,
                                    Snackbar.LENGTH_SHORT)
                            .setAnchorView(getActivity().findViewById(R.id.lib_create))
                            .show();
                } catch (Exception ex) {}
            }
        }
    }
}