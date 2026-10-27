package app.gkai.ai.core.datastore.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @desc ${TODD}
 */
public class UserDataHelper extends SQLiteOpenHelper {

    public UserDataHelper(Context context) {
        super(context, "user.db", null, 1);
    }

    /**
     * 创建数据库表
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table user(" +
                "_id Integer primary key autoincrement" +
                ",username varchar(20),pwd varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
