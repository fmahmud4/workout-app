package edu.gmu.cs477.project2_fmahmud4.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.gmu.cs477.project2_fmahmud4.R;


public class AddExerciseFragment extends Fragment {


    public static String TAG = AddExerciseFragment.class.getName();

    private SQLiteDatabase db = null;
    private ExerciseListDBHelper dbHelper = null;
    private AddExerciseFragment.LoadDB dbLoader = null;
    private EditText exercise, reps, sets, weight, notes;
    private String db_ex, db_exID, db_exREPS, db_exSETS, db_exWEIGHT, db_exNOTES;
    private Button btn_commit, btn_cancel;
    Cursor mCursor;

    final static String[] all_columns = {
            ExerciseListDBHelper.ID,
            ExerciseListDBHelper.EXERCISE,
            ExerciseListDBHelper.SETS,
            ExerciseListDBHelper.REPS,
            ExerciseListDBHelper.WEIGHT
    };


    public AddExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_exercise, container, false);
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
                try {
                    if (!isEmpty(exercise)) {
                        cv.put(ExerciseListDBHelper.EXERCISE, exercise.getText().toString());
                        /*Cursor c = db.rawQuery("SELECT * FROM " +
                                dbHelper.TABLE_NAME + " WHERE "+
                                dbHelper.EXERCISE +" = "+ exercise.getText(), null);

                         */
                        Cursor c = db.query(dbHelper.TABLE_NAME, all_columns, dbHelper.EXERCISE + "=?",
                                new  String[] {String.valueOf(exercise.getText())}, null, null, null);
                        if (c.moveToFirst()) {
                            throw new java.lang.InstantiationException("Exercise already exists");
                        }

                    } else {
                        //cv.put(ExerciseListDBHelper.EXERCISE, db_ex);
                        throw new Exception("You must have an exercise name");
                    }
                    if (!isEmpty(reps)) {
                        cv.put(ExerciseListDBHelper.REPS, reps.getText().toString());
                    } else {
                        //cv.put(ExerciseListDBHelper.REPS, db_exREPS);
                        throw new Exception("You must have a number of reps");
                    }
                    if (!isEmpty(sets)) {
                        cv.put(ExerciseListDBHelper.SETS, sets.getText().toString());
                    } else {
                        //cv.put(ExerciseListDBHelper.SETS, db_exSETS);
                        throw new Exception("You must have an number of reps");
                    }
                    if (!isEmpty(weight)) {
                        cv.put(ExerciseListDBHelper.WEIGHT, weight.getText().toString());
                    } else {
                        //cv.put(ExerciseListDBHelper.WEIGHT, db_exWEIGHT);
                        throw new Exception("You must have an initial weight");
                    }
                    db.insert(ExerciseListDBHelper.TABLE_NAME, null, cv);

                } catch (Exception e){
                    Toast.makeText(getContext(),  "Failed to add: " + e , Toast.LENGTH_LONG).show();

                }


                FragmentManager fm = getActivity().getSupportFragmentManager();
                if(fm.getBackStackEntryCount() != 0) {
                    fm.popBackStack();
                }


/*
                dbLoader = new AddExerciseFragment.LoadDB();
                dbLoader.execute();
                dbLoader.onPostExecute(mCursor);

 */

            }
        });

        return v;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private final class LoadDB extends AsyncTask<String, Void, Cursor> {
        @Override protected void onPostExecute(Cursor data) {
            db = dbHelper.getWritableDatabase();

            mCursor = db.query(ExerciseListDBHelper.TABLE_NAME, all_columns, null, null, null, null,
                    null);
            while (mCursor.moveToNext()) {

            }
            db_ex =  mCursor.getString(mCursor.getColumnIndexOrThrow("exercise"));
            db_exID = mCursor.getString(mCursor.getColumnIndexOrThrow("_id"));
            db_exREPS = mCursor.getString(mCursor.getColumnIndexOrThrow("reps"));
            db_exSETS = mCursor.getString(mCursor.getColumnIndexOrThrow("sets"));
            db_exWEIGHT = mCursor.getString(mCursor.getColumnIndexOrThrow("weight"));

        }

        @Override
        protected Cursor doInBackground(String... strings) {
            db = dbHelper.getWritableDatabase();

            return db.query(dbHelper.TABLE_NAME, all_columns, null, null,
                    null, null, null);
        }
    }
}