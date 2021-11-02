package edu.gmu.cs477.project2_fmahmud4.Fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.text.TextRunShaper;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditAddExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditAddExerciseFragment extends Fragment {

    long e_id;
    public static String TAG = EditAddExerciseFragment.class.getName();

    private SQLiteDatabase db = null;
    private ExerciseListDBHelper dbHelper = null;
    private LoadDB dbLoader = null;
    private EditText exercise, reps, sets, weight, notes;
    private TextView txt_update;
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
            e_id = getArguments().getLong("_id");
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


                //db = dbHelper.getWritableDatabase();

                ContentValues cv = new ContentValues();



                if (!isEmpty(exercise)) {
                    cv.put(ExerciseListDBHelper.EXERCISE, exercise.getText().toString());
                } else {
                    cv.put(ExerciseListDBHelper.EXERCISE, db_ex);
                }
                if (!isEmpty(reps)) {
                    cv.put(ExerciseListDBHelper.REPS, reps.getText().toString());
                } else {
                    cv.put(ExerciseListDBHelper.REPS, db_exREPS);
                }
                if (!isEmpty(sets)) {
                    cv.put(ExerciseListDBHelper.SETS, sets.getText().toString());
                } else {
                    cv.put(ExerciseListDBHelper.SETS, db_exSETS);
                }
                if (!isEmpty(weight)) {
                    cv.put(ExerciseListDBHelper.WEIGHT, weight.getText().toString());
                } else {
                    cv.put(ExerciseListDBHelper.WEIGHT, db_exWEIGHT);
                }


                String id = e_id + "";
                String db_id = getItemFromDbById(e_id);
                db.update(dbHelper.TABLE_NAME, cv, ExerciseListDBHelper.ID + " = ? ",
                        new String[] { id });

                cv.get(ExerciseListDBHelper.REPS);


                dbLoader = new LoadDB();
                dbLoader.execute();
                dbLoader.onPostExecute(mCursor);




            }
        });




        return v;
    }

    private String getItemFromDbById(long id) {
        String _id = String.valueOf(id);
        Cursor c = db.query(dbHelper.TABLE_NAME, all_columns,dbHelper.ID + "=?",
                new String[] { _id }, null, null ,null );
        if (c != null) c.moveToFirst();
        return c.getString(1);
    }

    // if true etText is empty
    // else etText is not empty
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
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

        txt_update.setText("Update values for " + db_ex);
        exercise.setHint(db_ex);
        reps.setHint(db_exREPS);
        sets.setHint(db_exSETS);



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
        //db.close();
        //dbHelper.deleteDatabase();
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
            db_ex =  mCursor.getString(mCursor.getColumnIndexOrThrow("exercise"));
            db_exID = mCursor.getString(mCursor.getColumnIndexOrThrow("_id"));
            db_exREPS = mCursor.getString(mCursor.getColumnIndexOrThrow("reps"));
            db_exSETS = mCursor.getString(mCursor.getColumnIndexOrThrow("sets"));
            db_exWEIGHT = mCursor.getString(mCursor.getColumnIndexOrThrow("weight"));

            //Toast.makeText(getContext(), db_exREPS +"" , Toast.LENGTH_SHORT).show();


            txt_update.setText("Update values for " + db_ex);
            exercise.setHint(db_ex);
            reps.setHint(db_exREPS);
            sets.setHint(db_exSETS);
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