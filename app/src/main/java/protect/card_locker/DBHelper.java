package protect.card_locker;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "Catima.db";
    public static final int ORIGINAL_DATABASE_VERSION = 1;
    public static final int DATABASE_VERSION = 4;

    static class LoyaltyCardDbGroups
    {
        public static final String TABLE = "groups";
        public static final String ID = "_id";
        public static final String NAME = "name";
    }

    static class LoyaltyCardDbIds
    {
        public static final String TABLE = "cards";
        public static final String ID = "_id";
        public static final String STORE = "store";
        public static final String NOTE = "note";
        public static final String HEADER_COLOR = "headercolor";
        public static final String HEADER_TEXT_COLOR = "headertextcolor";
        public static final String CARD_ID = "cardid";
        public static final String BARCODE_TYPE = "barcodetype";
    }

    static class LoyaltyCardDbIdsGroups
    {
        public static final String TABLE = "cardsGroups";
        public static final String cardID = "cardId";
        public static final String groupID = "groupID";
    }

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        // create table for card groups
        db.execSQL("create table " + LoyaltyCardDbGroups.TABLE + "(" +
                LoyaltyCardDbGroups.ID + " INTEGER primary key autoincrement," +
                LoyaltyCardDbGroups.NAME + " TEXT not null)");

        // create table for cards
        db.execSQL("create table " + LoyaltyCardDbIds.TABLE + "(" +
                LoyaltyCardDbIds.ID + " INTEGER primary key autoincrement," +
                LoyaltyCardDbIds.STORE + " TEXT not null," +
                LoyaltyCardDbIds.NOTE + " TEXT not null," +
                LoyaltyCardDbIds.HEADER_COLOR + " INTEGER," +
                LoyaltyCardDbIds.HEADER_TEXT_COLOR + " INTEGER," +
                LoyaltyCardDbIds.CARD_ID + " TEXT not null," +
                LoyaltyCardDbIds.BARCODE_TYPE + " TEXT not null)");

        // create associative table for cards in groups
        db.execSQL("create table " + LoyaltyCardDbIdsGroups.TABLE + "(" +
                LoyaltyCardDbIdsGroups.cardID + " INTEGER," +
                LoyaltyCardDbIdsGroups.groupID + " INTEGER," +
                "primary key (" + LoyaltyCardDbIdsGroups.cardID + "," + LoyaltyCardDbIdsGroups.groupID +"))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Upgrade from version 1 to version 2
        if(oldVersion < 2 && newVersion >= 2)
        {
            db.execSQL("ALTER TABLE " + LoyaltyCardDbIds.TABLE
                    + " ADD COLUMN " + LoyaltyCardDbIds.NOTE + " TEXT not null default ''");
        }

        // Upgrade from version 2 to version 3
        if(oldVersion < 3 && newVersion >= 3)
        {
            db.execSQL("ALTER TABLE " + LoyaltyCardDbIds.TABLE
                    + " ADD COLUMN " + LoyaltyCardDbIds.HEADER_COLOR + " INTEGER");
            db.execSQL("ALTER TABLE " + LoyaltyCardDbIds.TABLE
                    + " ADD COLUMN " + LoyaltyCardDbIds.HEADER_TEXT_COLOR + " INTEGER");
        }

        // Upgrade from version 3 to 4
        if(oldVersion < 4 && newVersion >= 4)
        {
            db.execSQL("create table " + LoyaltyCardDbGroups.TABLE + "(" +
                    LoyaltyCardDbGroups.ID + " INTEGER primary key autoincrement," +
                    LoyaltyCardDbGroups.NAME + " TEXT not null)");

            db.execSQL("create table " + LoyaltyCardDbIdsGroups.TABLE + "(" +
                    LoyaltyCardDbIdsGroups.cardID + " INTEGER," +
                    LoyaltyCardDbIdsGroups.groupID + " INTEGER," +
                    "primary key (" + LoyaltyCardDbIdsGroups.cardID + "," + LoyaltyCardDbIdsGroups.groupID +"))");;
        }
    }

    public long insertLoyaltyCard(final String store, final String note, final String cardId,
                                  final String barcodeType, final Integer headerColor,
                                  final Integer headerTextColor)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LoyaltyCardDbIds.STORE, store);
        contentValues.put(LoyaltyCardDbIds.NOTE, note);
        contentValues.put(LoyaltyCardDbIds.CARD_ID, cardId);
        contentValues.put(LoyaltyCardDbIds.BARCODE_TYPE, barcodeType);
        contentValues.put(LoyaltyCardDbIds.HEADER_COLOR, headerColor);
        contentValues.put(LoyaltyCardDbIds.HEADER_TEXT_COLOR, headerTextColor);
        final long newId = db.insert(LoyaltyCardDbIds.TABLE, null, contentValues);
        return newId;
    }

    public boolean insertLoyaltyCard(final SQLiteDatabase db, final int id, final String store,
                                     final String note, final String cardId,
                                     final String barcodeType, final Integer headerColor,
                                     final Integer headerTextColor)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LoyaltyCardDbIds.ID, id);
        contentValues.put(LoyaltyCardDbIds.STORE, store);
        contentValues.put(LoyaltyCardDbIds.NOTE, note);
        contentValues.put(LoyaltyCardDbIds.CARD_ID, cardId);
        contentValues.put(LoyaltyCardDbIds.BARCODE_TYPE, barcodeType);
        contentValues.put(LoyaltyCardDbIds.HEADER_COLOR, headerColor);
        contentValues.put(LoyaltyCardDbIds.HEADER_TEXT_COLOR, headerTextColor);
        final long newId = db.insert(LoyaltyCardDbIds.TABLE, null, contentValues);
        return (newId != -1);
    }

    public boolean updateLoyaltyCard(final int id, final String store, final String note,
                                     final String cardId, final String barcodeType,
                                     final Integer headerColor, final Integer headerTextColor)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LoyaltyCardDbIds.STORE, store);
        contentValues.put(LoyaltyCardDbIds.NOTE, note);
        contentValues.put(LoyaltyCardDbIds.CARD_ID, cardId);
        contentValues.put(LoyaltyCardDbIds.BARCODE_TYPE, barcodeType);
        contentValues.put(LoyaltyCardDbIds.HEADER_COLOR, headerColor);
        contentValues.put(LoyaltyCardDbIds.HEADER_TEXT_COLOR, headerTextColor);
        int rowsUpdated = db.update(LoyaltyCardDbIds.TABLE, contentValues,
                LoyaltyCardDbIds.ID + "=?",
                new String[]{Integer.toString(id)});
        return (rowsUpdated == 1);
    }

    public LoyaltyCard getLoyaltyCard(final int id, boolean withGroups)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data = db.rawQuery("select * from " + LoyaltyCardDbIds.TABLE +
                " where " + LoyaltyCardDbIds.ID + "=?", new String[]{String.format("%d", id)});

        LoyaltyCard card = null;

        if(data.getCount() == 1)
        {
            data.moveToFirst();

            List<Group> groups = new ArrayList<>();
            if (withGroups) {
                groups = getLoyaltyCardGroups(id);
            }

            card = LoyaltyCard.toLoyaltyCard(data, groups);
        }

        data.close();

        return card;
    }

    public List<Group> getLoyaltyCardGroups(final int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data = db.rawQuery("select * from " + LoyaltyCardDbGroups.TABLE + " g " +
                " LEFT JOIN " + LoyaltyCardDbIdsGroups.TABLE + " ig ON ig." + LoyaltyCardDbIdsGroups.groupID + " = g." + LoyaltyCardDbGroups.ID +
                " where " + LoyaltyCardDbIdsGroups.cardID + "=?", new String[]{String.format("%d", id)});

        List<Group> groups = new ArrayList<>();

        Group tempTag = new Group(0, "Test");

        groups.add(tempTag);

        if (!data.moveToFirst()) {
            return groups;
        }

        groups.add(Group.toGroup(data));

        while (data.moveToNext())

        if(data.moveToFirst()) {
            groups.add(Group.toGroup(data));
        }

        return groups;
    }

    public boolean deleteLoyaltyCard (final int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        // Delete card
        int rowsDeleted = db.delete(LoyaltyCardDbIds.TABLE,
                LoyaltyCardDbIds.ID + " = ? ",
                new String[]{String.format("%d", id)});

        // And delete lookup table entries associated with this card
        db.delete(LoyaltyCardDbIdsGroups.TABLE,
                LoyaltyCardDbIdsGroups.cardID + " = ? ",
                new String[]{String.format("%d", id)});

        return (rowsDeleted == 1);
    }

    public Cursor getLoyaltyCardCursor()
    {
        // An empty string will match everything
        return getLoyaltyCardCursor("");
    }

    /**
     * Returns a cursor to all loyalty cards with the filter text in either the store or note.
     *
     * @param filter
     * @return Cursor
     */
    public Cursor getLoyaltyCardCursor(final String filter)
    {
        String actualFilter = String.format("%%%s%%", filter);
        String[] selectionArgs = { actualFilter, actualFilter };

        SQLiteDatabase db = getReadableDatabase();

        Cursor res = db.rawQuery("select * from " + LoyaltyCardDbIds.TABLE +
                " WHERE " + LoyaltyCardDbIds.STORE + "  LIKE ? " +
                " OR " + LoyaltyCardDbIds.NOTE + " LIKE ? " +
                " ORDER BY " + LoyaltyCardDbIds.STORE + " COLLATE NOCASE ASC", selectionArgs, null);
        return res;
    }

    public int getLoyaltyCardCount()
    {
        // An empty string will match everything
        return getLoyaltyCardCount("");
    }

    /**
     * Returns the amount of loyalty cards with the filter text in either the store or note.
     *
     * @param filter
     * @return Integer
     */
    public int getLoyaltyCardCount(String filter)
    {
        String actualFilter = String.format("%%%s%%", filter);
        String[] selectionArgs = { actualFilter, actualFilter };

        SQLiteDatabase db = getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT Count(*) FROM " + LoyaltyCardDbIds.TABLE +
                " WHERE " + LoyaltyCardDbIds.STORE + "  LIKE ? " +
                " OR " + LoyaltyCardDbIds.NOTE + " LIKE ? "
                , selectionArgs, null);

        int numItems = 0;

        if(data.getCount() == 1)
        {
            data.moveToFirst();
            numItems = data.getInt(0);
        }

        data.close();

        return numItems;
    }

    /**
     * Returns a cursor to all groups.
     *
     * @return Cursor
     */
    public Cursor getGroupCursor()
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor res = db.rawQuery("select * from " + LoyaltyCardDbGroups.TABLE +
                " ORDER BY " + LoyaltyCardDbGroups.NAME + " COLLATE NOCASE ASC", null, null);
        return res;
    }

    public Group getGroup(final int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data = db.rawQuery("select * from " + LoyaltyCardDbGroups.TABLE +
                " where " + LoyaltyCardDbGroups.ID + "=?", new String[]{String.format("%d", id)});

        Group group = null;

        if(data.getCount() == 1)
        {
            data.moveToFirst();

            group = Group.toGroup(data);
        }

        data.close();

        return group;
    }

    public int getGroupCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor data =  db.rawQuery("SELECT Count(*) FROM " + LoyaltyCardDbGroups.TABLE, null);

        int numItems = 0;

        if(data.getCount() == 1)
        {
            data.moveToFirst();
            numItems = data.getInt(0);
        }

        data.close();

        return numItems;
    }

    public long insertGroup(final String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LoyaltyCardDbGroups.NAME, name);
        final long newId = db.insert(LoyaltyCardDbGroups.TABLE, null, contentValues);
        return newId;
    }

    public boolean deleteGroup(final int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        // Delete group
        int rowsDeleted = db.delete(LoyaltyCardDbGroups.TABLE,
                LoyaltyCardDbGroups.ID + " = ? ",
                new String[]{String.format("%d", id)});

        // And delete lookup table entries associated with this group
        db.delete(LoyaltyCardDbIdsGroups.TABLE,
                LoyaltyCardDbIdsGroups.groupID + " = ? ",
                new String[]{String.format("%d", id)});

        return (rowsDeleted == 1);
    }

    public int getGroupCardCount(final int id)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor data =  db.rawQuery("SELECT Count(*) FROM " + LoyaltyCardDbIdsGroups.TABLE +
                " where " + LoyaltyCardDbIdsGroups.groupID + "=?",
                new String[]{String.format("%d", id)});

        int numItems = 0;

        if(data.getCount() == 1)
        {
            data.moveToFirst();
            numItems = data.getInt(0);
        }

        data.close();

        return numItems;
    }
}

