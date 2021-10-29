package edu.gmu.cs477.project2_fmahmud4.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import edu.gmu.cs477.project2_fmahmud4.R;

public class ExercisesFragment extends Fragment {
    //TODO USE RECYCLER VIEW

    //TODO IMPLEMENT NOTES

    //TODO MAKE SURE SCROLLING WORKS PROPERLY

    //TODO IMPLEMENT TYPE SETTING and GENERALS


    private WorkoutViewModel mViewModel;
    private SQLiteDatabase db = null;
    private ExerciseListDBHelper dbHelper = null;
    private LoadDB dbLoader = null;
    public static RecyclerView rv_elist;
    public static ListView lv_elist;
    SimpleCursorAdapter elistAdapter;
    Cursor mCursor;

    final static String[] all_columns = {
            ExerciseListDBHelper.ID,
            ExerciseListDBHelper.EXERCISE,
            ExerciseListDBHelper.SETS,
            ExerciseListDBHelper.REPS,
            ExerciseListDBHelper.WEIGHT
    };
    final static String[] print_columns = {
            ExerciseListDBHelper.EXERCISE,
            ExerciseListDBHelper.SETS,
            ExerciseListDBHelper.REPS,
            ExerciseListDBHelper.WEIGHT
    };

    public static String TAG = ExercisesFragment.class.getName();

    public static ExercisesFragment newInstance() {
        return new ExercisesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercises, container, false);
        //rv_elist = view.findViewById(R.id.rv_elist);
        lv_elist = view.findViewById(R.id.lv_elist);
        dbHelper = new ExerciseListDBHelper(getContext());


        return view;
    }
    public void onResume(){
        super.onResume();
        db = dbHelper.getWritableDatabase();

        mCursor = db.query(dbHelper.TABLE_NAME, all_columns, null, null, null, null,
                null);
        elistAdapter = new SimpleCursorAdapter(getContext(),
                R.layout.exercise_list,
                mCursor,
                print_columns,
                new int[] { R.id.exercise, R.id.sets, R.id.reps, R.id.weight},0);
        lv_elist.setAdapter(elistAdapter);


    }
    public void onPause() {
        super.onPause();
        if (db != null) db.close();
        mCursor.close();
    }

    @Override
    public void onDestroy() {
        db.close();
        dbHelper.deleteDatabase();
        super.onDestroy();
    }

    private final class LoadDB extends AsyncTask<String, Void, Cursor> {
        @Override protected void onPostExecute(Cursor data) {
            elistAdapter = new SimpleCursorAdapter(getContext(),
                    R.layout.exercise_list,
                    data,
                    print_columns,
                    new int[] { R.id.exercise, R.id.sets, R.id.reps, R.id.weight},0);
            mCursor = data;
            lv_elist.setAdapter(elistAdapter);
        }

        @Override
        protected Cursor doInBackground(String... strings) {
            db = dbHelper.getWritableDatabase();
            return db.query(dbHelper.TABLE_NAME, all_columns, null, null,
                    null, null, null);
        }
    }

}