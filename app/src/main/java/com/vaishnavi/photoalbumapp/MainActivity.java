package com.vaishnavi.photoalbumapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.LoadState;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.vaishnavi.photoalbumapp.adapter.PhotoAdapter;
import com.vaishnavi.photoalbumapp.database.AppDatabase;
import com.vaishnavi.photoalbumapp.model.Photo;
import com.vaishnavi.photoalbumapp.network.ApiService;
import com.vaishnavi.photoalbumapp.network.RetrofitClient;
import com.vaishnavi.photoalbumapp.viewmodel.PhotoViewModel;
import com.vaishnavi.photoalbumapp.viewmodel.PhotoViewModelFactory;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PhotoViewModel photoViewModel;
    private PhotoAdapter adapter;
    private RecyclerView recyclerView;
    private AutoCompleteTextView searchAutoComplete;
    private List<String> searchSuggestions;
    private ArrayAdapter<String> autoCompleteAdapter;
    private String lastSearchQuery = ""; // Avoid redundant searches
    private boolean isSearchActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PhotoAdapter();
        recyclerView.setAdapter(adapter);



        searchAutoComplete = findViewById(R.id.searchAutoComplete);

        // Initialize search suggestions list
        searchSuggestions = new ArrayList<>();
        autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, searchSuggestions);
        searchAutoComplete.setAdapter(autoCompleteAdapter);

        // Initialize API service and Room database
        ApiService apiService = RetrofitClient.getInstance(this).create(ApiService.class);
        AppDatabase database = AppDatabase.getInstance(this);
        photoViewModel = new ViewModelProvider(this,
                new PhotoViewModelFactory(getApplication(), apiService, database.photoDao()))
                .get(PhotoViewModel.class);

        // Load default photos
        loadPhotos();

        // Set up search features
        setupAutoCompleteSearch();

        // Handle API errors and pagination failures
        setupLoadStateListener();
    }

    // Fetches photos from ViewModel and updates RecyclerView
    private void loadPhotos() {
        photoViewModel.getPhotos().observe(this, pagingData -> {
            if (pagingData == null || pagingData.equals(PagingData.empty())) {
                showError("No internet. Loading cached data.");
            } else {
                adapter.submitData(getLifecycle(), pagingData);
                extractSearchSuggestions(); // Extract search suggestions from dataset
            }
        });
    }

    // Extracts search suggestions from the currently loaded photos
    private void extractSearchSuggestions() {
        searchSuggestions.clear();
        for (Photo photo : adapter.snapshot().getItems()) {
            if (photo.getAuthor() != null) searchSuggestions.add(photo.getAuthor().toLowerCase());
            searchSuggestions.add(String.valueOf(photo.getId())); // Convert ID to String
        }
        autoCompleteAdapter.notifyDataSetChanged();
    }

    // Setup AutoComplete search functionality
    private void setupAutoCompleteSearch() {
        searchAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String query = (String) parent.getItemAtPosition(position);
            performSearch(query);
        });

        // Show suggestions dynamically as user types
        searchAutoComplete.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(v.getText().toString());
                return true;
            }
            return false;
        });

        // Show suggestions dynamically as user types
        searchAutoComplete.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().toLowerCase();
                List<String> filteredSuggestions = new ArrayList<>();
                for (String suggestion : searchSuggestions) {
                    if (suggestion.toLowerCase().contains(input)) {
                        filteredSuggestions.add(suggestion);
                    }
                }
                autoCompleteAdapter.clear();
                autoCompleteAdapter.addAll(filteredSuggestions);
                autoCompleteAdapter.notifyDataSetChanged();
                searchAutoComplete.showDropDown();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    // Perform search using ViewModel
    private void performSearch(String query) {
        String formattedQuery = query.toLowerCase();
        photoViewModel.searchPhotos(formattedQuery).observe(this, pagingData -> {
            if (pagingData == null || pagingData.equals(PagingData.empty())) {
                showError("No results found.");
            } else {
                adapter.submitData(getLifecycle(), pagingData);
                recyclerView.smoothScrollToPosition(0);
                extractSearchSuggestions();
            }
        });
    }

    //  Setup `LoadStateListener` to Handle Errors
    private void setupLoadStateListener() {
        adapter.addLoadStateListener(loadStates -> {
            LoadState refreshState = loadStates.getRefresh();
            LoadState appendState = loadStates.getAppend();

            //  Show error message if the first load fails
            if (refreshState instanceof LoadState.Error) {
                LoadState.Error errorState = (LoadState.Error) refreshState;
                showError(errorState.getError().getMessage());
            }

            // Show error message if loading next page fails
            if (appendState instanceof LoadState.Error) {
                LoadState.Error errorState = (LoadState.Error) appendState;
                showError(errorState.getError().getMessage());
            }

            return null;
        });
    }


    // Show error messages in a Snackbar
    private void showError(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.recyclerView), message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setDuration(20000); // Setting duration to 20 seconds
        snackbar.show();
    }




    @Override
    public void onBackPressed() {
        // If search bar is not empty, clear it and reload all photos
        if (!searchAutoComplete.getText().toString().isEmpty() || isSearchActive) {
            searchAutoComplete.setText("");  // Clear search text
            isSearchActive = false;  // Reset search state
            loadPhotos();  // Reload all images from API or cache
        } else {
            super.onBackPressed();  // Default back behavior (exit app)
        }
    }


}
