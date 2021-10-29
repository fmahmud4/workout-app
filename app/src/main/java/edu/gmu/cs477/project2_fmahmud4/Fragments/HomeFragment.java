package edu.gmu.cs477.project2_fmahmud4.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.gmu.cs477.project2_fmahmud4.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private Button btn_start, btn_elist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btn_start = view.findViewById(R.id.btn_start);
        btn_elist = view.findViewById(R.id.btn_elist);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(WorkoutFragment.TAG);
                transaction.replace(R.id.main_frame, new WorkoutFragment());
                transaction.commit();
            }
        });
        btn_elist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(ExercisesFragment.TAG);
                transaction.replace(R.id.main_frame, new ExercisesFragment());
                transaction.commit();
            }
        });
        return view;
    }
}