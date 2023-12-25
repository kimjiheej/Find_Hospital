package ddwu.com.mobile.naverretrofittest

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import ddwu.com.mobile.naverretrofittest.data.Item

class HospitalDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME,null,1) { // 4가지 매개변수를 만들어주는것이 필요하다
    // context 정보를 어딘가에서 받아와서 넣어주어라
    // this 넣어주면 된다
    // DB_NAME 은 파일 이름이 된다. 상수로 지정하면 된다

    val TAG = "FoodDBHelper"

    companion object {
        const val DB_NAME = "hospital_db"
        const val TABLE_NAME = "hospital_table"
        const val COL_NAME = "hospital"
        const val COL_TYPE = "type"
    }

    override fun onCreate(db: SQLiteDatabase?){ // 테이블을 만드는 역할이다
        val CREATE_TABLE =
            "CREATE TABLE ${TABLE_NAME} (${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${COL_NAME} TEXT, ${COL_TYPE} TEXT)"
        Log.d(TAG,CREATE_TABLE)
        db?.execSQL(CREATE_TABLE) // create table 문장을 실행해주는 것이다.

        db?.execSQL("INSERT INTO ${TABLE_NAME} values (null,'제육볶음','대한민국')") // 예문을 넣는 것이다.
    }


    //항상 primarykey 는 밑줄을 치게 되어있음.

    override fun onUpgrade(db: SQLiteDatabase?, oldVer: Int, newVer: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS ${TABLE_NAME}"
        db?.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun insertData(name: String?, type: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_NAME, name)
        contentValues.put(COL_TYPE, type)

        val result = db.insert(TABLE_NAME, null, contentValues)
        return result != -1L
    }

    @SuppressLint("Range")
    fun getAllData(): ArrayList<PartialItem> {
        val itemList = ArrayList<PartialItem>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                val name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                val type = cursor.getString(cursor.getColumnIndex(COL_TYPE))
                // Add data to the list using the PartialItem class constructor
                itemList.add(PartialItem(id, name, type))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return itemList
    }

    @SuppressLint("Range")
    fun getAllPartialData(): ArrayList<PartialItem> {
        val partialItemList = ArrayList<PartialItem>()
        val db = readableDatabase
        val query = "SELECT ${BaseColumns._ID}, $COL_NAME, $COL_TYPE FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
                val name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                val type = cursor.getString(cursor.getColumnIndex(COL_TYPE))

                // PartialItem을 생성하여 리스트에 추가
                partialItemList.add(PartialItem(id, name, type))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return partialItemList
    }

    data class PartialItem(
        val id: Int,
        val name: String,
        val type: String
    )
}