package edu.gmu.cs477.project2_fmahmud4.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import edu.gmu.cs477.project2_fmahmud4.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditAddExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAddExerciseFragment extends Fragment {

    int e_id;
    public static String TAG = EditAddExerciseFragment.class.getName();

    private SQLiteDatabase db = null;
    private ExerciseListDBHelper dbHelper = null;
    private LoadDB dbLoader = null;
    private TextView exercise, reps, sets, weight, notes, txt_update;
    private Button btn_commit, btn_cancel;
    Cursor mCursor;

    final static String[] all_columns = {
            ExerciseListDBHelper.ID,
            ExerciseListDBHelper.EXERCISE,
            ExerciseListDBHelper.SETS,
            ExerciseListDBHelper.REPS,
            ExerciseListDBHelper.WEIGHT
    };

    public EditAddExerciseFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditAddExerciseFragment newInstance(String param1, String param2) {
        EditAddExerciseFragment fragment = new EditAddExerciseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            e_id = getArguments().getInt("_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_add_exercise, container, false);
        txt_update = v.findViewById(R.id.txt_update);
        exercise = v.findViewById(R.id.e_name);
        reps = v.findViewById(R.id.reps);
        sets = v.findViewById(R.id.sets);
        weight = v.findViewById(R.id.weight);
        notes = v.findViewById(R.id.notes);

        btn_cancel = v.findViewById(R.id.btn_cancel);
        btn_commit = v.findViewById(R.id.btn_commit);

        dbHelper = new ExerciseListDBHelper(getContext());

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if(fm.getBackStackEntryCount() != 0) {
                    fm.popBackStack();
                }

            }
        });

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dbHelper.getWritableDatabase();

                ContentValues cv = new ContentValues();

                long _id = (long) e_id;
                if (exercise.getText() != null) {
                    cv.put(ExerciseListDBHelper.EXERCISE, exercise.getText().toString());
                }
                if (reps.getText() != null) {
                    cv.put(ExerciseListDBHelper.REPS, reps.getText().toString());
                }
                if (sets.getText() != null) {
                    cv.put(ExerciseListDBHelper.SETS, reps.getText().toString());
                }
                if (weight.getText() != null) {
                    cv.put(ExerciseListDBHelper.WEIGHT, weight.getText().toString());
                }
                int i = db.update(dbHelper.TABLE_NAME, cv, ExerciseListDBHelper.ID + " = " + _id, null);

                Toast.makeText(getContext(), weight.getText().toString() , Toast.LENGTH_SHORT).show();
                dbLoader = new LoadDB();
                dbLoader.execute();
                dbLoader.onPostExecute(mCursor);

            }
        });




        return v;
    }

    public void onResume(){
        super.onResume();
        db = dbHelper.getWritableDatabase();

        mCursor = db.query(ExerciseListDBHelper.TABLE_NAME, all_columns, null, null, null, null,
                null);

        while (mCursor.moveToNext()) {
            if (mCursor.getInt(mCursor.getColumnIndexOrThrow("_id")) == e_id) {
                break;
            }
        }
        String ex =  mCursor.getString(mCursor.getColumnIndexOrThrow("exercise"));
        String ex_id = mCursor.getString(mCursor.getColumnIndexOrThrow("_id"));
        String ex_reps = mCursor.getString(mCursor.getColumnIndexOrThrow("reps"));
        String ex_sets = mCursor.getString(mCursor.getColumnIndexOrThrow("sets"));
        String ex_weight = mCursor.getString(mCursor.getColumnIndexOrThrow("weight"));

        txt_update.setText("Update values for " + ex);
        exercise.setHint(ex);
        reps.setHint(ex_reps);
        sets.setHint(ex_sets);



        //String db_reps = mCursor.getString(mCursor.getColumnIndexOrThrow("reps"));
        //reps.setHint("7");
        //reps.setText("db_reps");
        /*
        elistAdapter = new SimpleCursorAdapter(getContext(),
                R.layout.exercise_list,
                mCursor,
                print_columns,
                new int[] { R.id.exercise},0);
        lv_elist.setAdapter(elistAdapter);

         */


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
            db = dbHelper.getWritableDatabase();

            mCursor = db.query(ExerciseListDBHelper.TABLE_NAME, all_columns, null, null, null, null,
                    null);
            while (mCursor.moveToNext()) {
                if (mCursor.getInt(mCursor.getColumnIndexOrThrow("_id")) == e_id) {
                    break;
                }
            }
            String ex =  mCursor.getString(mCursor.getColumnIndexOrThrow("exercise"));
            String ex_id = mCursor.getString(mCursor.getColumnIndexOrThrow("_id"));
            String ex_reps = mCursor.getString(mCursor.getColumnIndexOrThrow("reps"));
            String ex_sets = mCursor.getString(mCursor.getColumnIndexOrThrow("sets"));
            String ex_weight = mCursor.getString(mCursor.getColumnIndexOrThrow("weight"));

            txt_update.setText("Update values for " + ex);
            exercise.setHint(ex);
            reps.setHint(ex_reps);
            sets.setHint(ex_sets);
           // reps.setHint("7");
            //reps.setText("db_reps");
            /*
            elistAdapter = new SimpleCursorAdapter(getContext(),
                    R.layout.exercise_list,
                    data,
                    print_columns,
                    new int[] { R.id.exercise},0);
            mCursor = data;
            lv_elist.setAdapter(elistAdapter);

             */

        }

        @Override
        protected Cursor doInBackground(String... strings) {
            db = dbHelper.getWritableDatabase();
            String where = "_id = ?";
            String[] whereArg = new String[]{
                    e_id+""
            };
            return db.query(dbHelper.TABLE_NAME, all_columns, null, null,
                    null, null, null);
        }
    }
}