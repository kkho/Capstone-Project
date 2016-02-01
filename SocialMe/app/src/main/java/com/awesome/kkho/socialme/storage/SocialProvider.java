package com.awesome.kkho.socialme.storage;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.awesome.kkho.socialme.util.SelectionBuilder;

import java.util.ArrayList;

/**
 * Created by kkho on 24.01.2016.
 */
public class SocialProvider extends ContentProvider {
    private SocialDatabase mOpenHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int EVENT = 100;
    private static final int EVENT_ID = 101;
    private static final int VENUE = 200;
    private static final int VENUE_ID = 201;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SocialContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "event", EVENT);
        matcher.addURI(authority, "event/*", EVENT_ID);
        matcher.addURI(authority, "venue", VENUE);
        matcher.addURI(authority, "venue/*", VENUE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new SocialDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            default: {
                // Most cases are handled with simple SelectionBuilder
                final SelectionBuilder builder = buildSimpleSelection(uri);
                return builder.where(selection, selectionArgs).query(db,
                        projection, sortOrder);
            }
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENT:
                return SocialContract.EventDB.CONTENT_TYPE;
            case EVENT_ID:
                return SocialContract.EventDB.CONTENT_ITEM_TYPE;
            case VENUE:
                return SocialContract.VenueDB.CONTENT_TYPE;
            case VENUE_ID:
                return SocialContract.VenueDB.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        ContentResolver contentResolver = getContext().getContentResolver();
        switch (match) {
            case EVENT: {
                db.insertOrThrow(SocialContract.TABLE_EVENT, null, values);
                contentResolver.notifyChange(uri, null);
                return SocialContract.EventDB.buildUri(
                        values.getAsString(SocialContract.EventDB._ID));
            }
            case VENUE: {
                db.insertOrThrow(SocialContract.TABLE_VENUE, null, values);
                contentResolver.notifyChange(uri, null);
                return SocialContract.VenueDB.buildUri(
                        values.getAsString(SocialContract.VenueDB._ID));
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uri == SocialContract.BASE_CONTENT_URI) {
            // Handle whole database deletes (e.g. when signing out)
            return 1;
        }
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int retVal = builder.where(selection, selectionArgs).delete(db);
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int retVal = builder.where(selection, selectionArgs).update(db, values);

        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

    /**
     * Apply the given set of {@link android.content.ContentProviderOperation}, executing inside
     * a {@link SQLiteDatabase} transaction. All changes will be rolled back if
     * any single one fails.
     */
    @Override
    public ContentProviderResult[] applyBatch(
            @NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case EVENT: {
                return builder.table(SocialContract.TABLE_EVENT);
            }
            case EVENT_ID: {

            }

            case VENUE: {
                return builder.table(SocialContract.TABLE_VENUE);
            }

            case VENUE_ID: {
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }
}
