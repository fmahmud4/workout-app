package edu.gmu.cs477.project2_fmahmud4.Fragments;

import android.content.ContentValues;
        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

public class ExerciseListDBHelper extends SQLiteOpenHelper {
    final static String DBNAME = "EXERCISE_LIST.DB";
    final static String TABLE_NAME = "EXERCISES";
    final static String ID = "_id";
    final static String EXERCISE = "exercise";
    final static String SETS = "sets";
    final static String REPS = "reps";
    final static String WEIGHT = "weight";
    final static String NOTES = "notes";

    final private static String CREATE_CMD =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EXERCISE + " TEXT NOT NULL, " +
                    SETS + " TEXT NOT NULL, " +
                    REPS + " TEXT NOT NULL, " +
                    WEIGHT + " TEXT NOT NULL, " +
                    NOTES + "TEXT)";

    final private static Integer DBVERSION = 1;
    final private Context context;

    public ExerciseListDBHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CMD);
        ContentValues values = new ContentValues();

        values.put(this.EXERCISE, "Squat");
        values.put(this.SETS, "5");
        values.put(this.REPS, "5");
        values.put(this.WEIGHT, "135");
        db.insert(this.TABLE_NAME, null, values);
        values.put(this.EXERCISE, "Bench Press");
        values.put(this.SETS, "4");
        values.put(this.REPS, "8");
        values.put(this.WEIGHT, "95");
        db.insert(this.TABLE_NAME, null, values);
        values.put(this.EXERCISE, "Deadlift");
        values.put(this.SETS, "4");
        values.put(this.REPS, "3");
        values.put(this.WEIGHT, "185");
        db.insert(this.TABLE_NAME, null, values);



        //this.deleteDatabase();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void deleteDatabase() {
        context.deleteDatabase(DBNAME);
    }
}



