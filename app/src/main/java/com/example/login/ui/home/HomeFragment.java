package com.example.login.ui.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.Manifest;
import com.example.login.ConnectingActivity;
import com.example.login.MainActivity;
import com.example.login.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private int requestCode = 1;
    String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button btn = binding.logOutBtn;
        btn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeFragment.this.getActivity(), MainActivity.class));
        });

        Button callButton = binding.callBtn;
        callButton.setOnClickListener(view -> {
            isPermissionsGranted();
            startActivity(new Intent(HomeFragment.this.getActivity(), ConnectingActivity.class));
        });

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;

    }

    private void isPermissionsGranted() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(HomeFragment.this.getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeFragment.this.getActivity(), new String[]{permission}, requestCode);
                //Toast.makeText(HomeFragment.this.getActivity(), "Permission Not Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}