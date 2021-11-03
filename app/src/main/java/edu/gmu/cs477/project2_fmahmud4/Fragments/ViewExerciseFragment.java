package edu.gmu.cs477.project2_fmahmud4.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.gmu.cs477.project2_fmahmud4.R;


public class ViewExerciseFragment extends Fragment {

    long e_id;
    public static String TAG = ViewExerciseFragment.class.getName();

    private SQLiteDatabase db = null;
    private ExerciseListDBHelper dbHelper = null;
    private ViewExerciseFragment.LoadDB dbLoader = null;
    private TextView exercise, reps, sets, weight, notes;
    private String db_ex, db_exID, db_exREPS, db_exSETS, db_exWEIGHT, db_exNOTES;

    Cursor mCursor;

    final static String[] all_columns = {
            ExerciseListDBHelper.ID,
            ExerciseListDBHelper.EXERCISE,
            ExerciseListDBHelper.SETS,
            ExerciseListDBHelper.REPS,
            ExerciseListDBHelper.WEIGHT,
            ExerciseListDBHelper.NOTES
    };

    public ViewExerciseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            e_id = getArguments().getLong("_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_exercise, container, false);

        exercise = v.findViewById(R.id.e_name);
        reps = v.findViewById(R.id.reps);
        sets = v.findViewById(R.id.sets);
        weight = v.findViewById(R.id.weight);
        notes = v.findViewById(R.id.notes);

        dbHelper = new ExerciseListDBHelper(getContext());


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
        db_ex =  mCursor.getString(mCursor.getColumnIndexOrThrow("exercise"));
        db_exID = mCursor.getString(mCursor.getColumnIndexOrThrow("_id"));
        db_exREPS = mCursor.getString(mCursor.getColumnIndexOrThrow("reps"));
        db_exSETS = mCursor.getString(mCursor.getColumnIndexOrThrow("sets"));
        db_exWEIGHT = mCursor.getString(mCursor.getColumnIndexOrThrow("weight"));
        db_exNOTES = mCursor.getString(mCursor.getColumnIndexOrThrow("notes"));

        exercise.setText(db_ex);
        reps.setText(db_exREPS);
        sets.setText(db_exSETS);
        weight.setText(db_exWEIGHT);
        if (db_exNOTES.length() != 0) {
            notes.setText(db_exNOTES);
        }
    }

    private final class LoadDB extends AsyncTask<String, Void, Cursor> {
        @Override protected void onPostExecute(Cursor data) {

            try {
                db = dbHelper.getWritableDatabase();

                mCursor = db.query(ExerciseListDBHelper.TABLE_NAME, all_columns, null, null, null, null,
                        null);
                while (mCursor.moveToNext()) {
                    if (mCursor.getInt(mCursor.getColumnIndexOrThrow("_id")) == e_id) {
                        break;
                    }
                }
                db_ex = mCursor.getString(mCursor.getColumnIndexOrThrow("exercise"));
                db_exID = mCursor.getString(mCursor.getColumnIndexOrThrow("_id"));
                db_exREPS = mCursor.getString(mCursor.getColumnIndexOrThrow("reps"));
                db_exSETS = mCursor.getString(mCursor.getColumnIndexOrThrow("sets"));
                db_exWEIGHT = mCursor.getString(mCursor.getColumnIndexOrThrow("weight"));
                db_exNOTES = mCursor.getString(mCursor.getColumnIndexOrThrow("notes"));

                exercise.setText(db_ex);
                reps.setText(db_exREPS);
                sets.setText(db_exSETS);
                weight.setText(db_exWEIGHT);
                if (db_exNOTES.length() != 0) {
                    notes.setText(db_exNOTES);
                }


            } catch (Exception e) {
                Toast.makeText(getContext(),  "Failed to update: " + e , Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected Cursor doInBackground(String... strings) {
            db = dbHelper.getWritableDatabase();

            return db.query(dbHelper.TABLE_NAME, all_columns, null, null,
                    null, null, null);
        }
    }
}