package edu.gmu.cs477.project2_fmahmud4.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import edu.gmu.cs477.project2_fmahmud4.R;

public class ExercisesFragment extends Fragment {

    //TODO IMPLEMENT NOTES

    //TODO Implement Delete
    //TODO Implement Workout screen



    private WorkoutViewModel mViewModel;
    private SQLiteDatabase db = null;
    private ExerciseListDBHelper dbHelper = null;
    private LoadDB dbLoader = null;
    private Button btn_add;
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
            ExerciseListDBHelper.EXERCISE
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
        btn_add = view.findViewById(R.id.btn_add);
        dbHelper = new ExerciseListDBHelper(getContext());

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddExerciseFragment addDB =  new AddExerciseFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(AddExerciseFragment.TAG);
                transaction.replace(R.id.main_frame, addDB);
                transaction.commit();
            }
        });

        lv_elist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Bundle dbInfo = new Bundle();
                dbInfo.putLong("_id", id);
                EditExerciseFragment editDB =  new EditExerciseFragment();
                editDB.setArguments(dbInfo);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(EditExerciseFragment.TAG);
                transaction.replace(R.id.main_frame, editDB);
                transaction.commit();
            }
        });

        lv_elist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {

                db.delete(dbHelper.TABLE_NAME,dbHelper.ID + " =?",
                        new String[] { id+"" });
                dbLoader = new LoadDB();
                dbLoader.execute();
                dbLoader.onPostExecute(mCursor);
                return false;
            }
        });


        return view;
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
    public void onPause() {
        super.onPause();
        if (db != null) db.close();
        mCursor.close();
    }

    @Override
    public void onDestroy() {
        db.close();
        //dbHelper.deleteDatabase();
        super.onDestroy();
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

}