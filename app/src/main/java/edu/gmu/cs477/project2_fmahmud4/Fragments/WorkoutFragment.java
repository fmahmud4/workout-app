package edu.gmu.cs477.project2_fmahmud4.Fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import edu.gmu.cs477.project2_fmahmud4.R;

public class WorkoutFragment extends Fragment {

    public static String TAG = WorkoutFragment.class.getName();

    private SQLiteDatabase db = null;
    private ExerciseListDBHelper dbHelper = null;
    private WorkoutFragment.LoadDB dbLoader = null;
    public static ListView lv_elist;
    private Button btn_end;
    SimpleCursorAdapter elistAdapter;
    Cursor mCursor;

    final static String[] all_columns = {
            ExerciseListDBHelper.ID,
            ExerciseListDBHelper.EXERCISE,
            ExerciseListDBHelper.SETS,
            ExerciseListDBHelper.REPS,
            ExerciseListDBHelper.WEIGHT,
            ExerciseListDBHelper.NOTES
    };
    final static String[] print_columns = {
            ExerciseListDBHelper.EXERCISE
    };

    public WorkoutFragment() {
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
        View v = inflater.inflate(R.layout.fragment_workout, container, false);

        lv_elist = v.findViewById(R.id.lv_elist);
        dbHelper = new ExerciseListDBHelper(getContext());
        btn_end = v.findViewById(R.id.btn_end);
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if(fm.getBackStackEntryCount() != 0) {
                    fm.popBackStack();
                }
            }
        });
        lv_elist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Bundle dbInfo = new Bundle();
                dbInfo.putLong("_id", id);
                ViewExerciseFragment editDB =  new ViewExerciseFragment();
                editDB.setArguments(dbInfo);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(ViewExerciseFragment.TAG);
                transaction.replace(R.id.main_frame, editDB);
                transaction.commit();
            }
        });

        lv_elist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                alertView("Completing exercise", id);
                return true;
            }
        });
        return v;
    }

    public void onResume(){
        super.onResume();
        db = dbHelper.getWritableDatabase();

        mCursor = db.query(dbHelper.TABLE_NAME, all_columns, null, null, null, null,
                null);
        //Toast.makeText(getContext(), mCursor.getString(1), Toast.LENGTH_SHORT).show();

        elistAdapter = new SimpleCursorAdapter(getContext(),
                R.layout.exercise_list,
                mCursor,
                print_columns,
                new int[] { R.id.exercise},0);
        lv_elist.setAdapter(elistAdapter);


    }

    private final class LoadDB extends AsyncTask<String, Void, Cursor> {
        @Override protected void onPostExecute(Cursor data) {
            elistAdapter = new SimpleCursorAdapter(getContext(),
                    R.layout.exercise_list,
                    data,
                    print_columns,
                    new int[] { R.id.exercise},0);
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


    private void alertView(String message ,final long id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle( message )
                .setIcon(R.drawable.bg_btn)
                .setMessage("Are you done with this exercise?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        db = dbHelper.getWritableDatabase();

                        mCursor = db.query(dbHelper.TABLE_NAME, all_columns, dbHelper.ID + "!=?",
                                new  String[] {String.valueOf(id)}, null, null, null);

                        elistAdapter = new SimpleCursorAdapter(getContext(),
                                R.layout.exercise_list,
                                mCursor,
                                print_columns,
                                new int[] { R.id.exercise},0);
                        lv_elist.setAdapter(elistAdapter);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.cancel();
                    }}).show();
    }

}