package com.example.fiebasephoneauth.Guardian.page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fiebasephoneauth.R;

/**
 *
 *
 */
public class GuardianMenuHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter viewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guardian_menu_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_home_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(viewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;

    }
}