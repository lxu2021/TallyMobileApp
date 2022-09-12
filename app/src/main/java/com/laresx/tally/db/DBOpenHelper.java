package com.laresx.tally.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.laresx.tally.R;

public class DBOpenHelper extends SQLiteOpenHelper {

    public DBOpenHelper(@Nullable Context context) {
        super(context,"tally.db" , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table typetb(id integer primary key autoincrement,typename varchar(10),imageId integer,sImageId integer,kind integer)";
        db.execSQL(sql);
        insertType(db);
        // create tally form
        sql = "create table accounttb(id integer primary key autoincrement,typename varchar(10),sImageId integer,beizhu varchar(80),money float," +
                "time varchar(60),year integer,month integer,day integer,kind integer)";
        db.execSQL(sql);
    }

    private void insertType(SQLiteDatabase db) {
        //insert element to typedb
        String sql = "insert into typetb(typename, imageId, sImageId, kind) values(?, ?, ?, ?)";
        db.execSQL(sql, new Object[]{"Others", R.mipmap.ic_qita, R.mipmap.ic_qita_fs, 0});
        db.execSQL(sql, new Object[]{"Restaurant", R.mipmap.ic_canyin, R.mipmap.ic_canyin_fs, 0});
        db.execSQL(sql, new Object[]{"Traffic", R.mipmap.ic_jiaotong, R.mipmap.ic_jiaotong_fs, 0});
        db.execSQL(sql, new Object[]{"Shopping", R.mipmap.ic_gouwu, R.mipmap.ic_gouwu_fs, 0});
        db.execSQL(sql, new Object[]{"Clothes", R.mipmap.ic_fushi, R.mipmap.ic_fushi_fs, 0});
        db.execSQL(sql, new Object[]{"Daily", R.mipmap.ic_riyongpin, R.mipmap.ic_riyongpin_fs, 0});
        db.execSQL(sql, new Object[]{"Recreation", R.mipmap.ic_yule, R.mipmap.ic_yule_fs, 0});
        db.execSQL(sql, new Object[]{"snack ", R.mipmap.ic_lingshi, R.mipmap.ic_lingshi_fs, 0});
        db.execSQL(sql, new Object[]{"alcohol", R.mipmap.ic_yanjiu, R.mipmap.ic_yanjiu_fs, 0});
        db.execSQL(sql, new Object[]{"Study", R.mipmap.ic_xuexi, R.mipmap.ic_xuexi_fs, 0});
        db.execSQL(sql, new Object[]{"Medical", R.mipmap.ic_yiliao, R.mipmap.ic_yiliao_fs, 0});
        db.execSQL(sql, new Object[]{"Rent", R.mipmap.ic_zhufang, R.mipmap.ic_zhufang_fs, 0});
        db.execSQL(sql, new Object[]{"Utility ", R.mipmap.ic_shuidianfei, R.mipmap.ic_shuidianfei_fs, 0});
        db.execSQL(sql, new Object[]{"Mobile", R.mipmap.ic_tongxun, R.mipmap.ic_tongxun_fs, 0});
        db.execSQL(sql, new Object[]{"Gift", R.mipmap.ic_renqingwanglai, R.mipmap.ic_renqingwanglai_fs, 0});

        db.execSQL(sql, new Object[]{"Others", R.mipmap.in_qt, R.mipmap.in_qt_fs, 1});
        db.execSQL(sql, new Object[]{"Wage", R.mipmap.in_xinzi, R.mipmap.in_xinzi_fs, 1});
        db.execSQL(sql, new Object[]{"Bonus", R.mipmap.in_jiangjin, R.mipmap.in_jiangjin_fs, 1});
        db.execSQL(sql, new Object[]{"Loan", R.mipmap.in_jieru, R.mipmap.in_jieru_fs, 1});
        db.execSQL(sql, new Object[]{"Collection", R.mipmap.in_shouzhai, R.mipmap.in_shouzhai_fs, 1});
        db.execSQL(sql, new Object[]{"Interest", R.mipmap.in_lixifuji, R.mipmap.in_lixifuji_fs, 1});
        db.execSQL(sql, new Object[]{"Invest", R.mipmap.in_touzi, R.mipmap.in_touzi_fs, 1});
        db.execSQL(sql, new Object[]{"Resale", R.mipmap.in_ershoushebei, R.mipmap.in_ershoushebei_fs, 1});
        db.execSQL(sql, new Object[]{"Surprise", R.mipmap.in_yiwai, R.mipmap.in_yiwai_fs, 1});

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
