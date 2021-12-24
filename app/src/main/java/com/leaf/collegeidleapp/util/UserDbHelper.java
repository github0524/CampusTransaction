package com.leaf.collegeidleapp.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leaf.collegeidleapp.bean.User;

import java.util.LinkedList;

/**
 * 用户数据库连接类
 *
 */
public class UserDbHelper extends SQLiteOpenHelper {

    //定义数据库表名
    public static final String DB_NAME = "tb_user";
    /** 创建用户信息表，这里定义表的内容，下面onCreate方法调用**/
    private static final String CREATE_USER_DB = "create table tb_user (" +
            "id integer primary key autoincrement," +
            "uuid text," +
            "username text," +
            "password text )";

     /*构造方法*/
    public UserDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    /*两个重写的抽象方法，可实现创建，升级数据库的逻辑*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_DB);
    }
    /*升级数据库用的*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 注册时添加用户信息，传入一个user的实例，转成表中记录
     * @param user 学生用户
     */
    public void addUser(User user) {
        /*getWritableDatabase()方法创建或打开一个现有的数据库，并返回一个可对数据库读写的对象*/
        SQLiteDatabase db = this.getWritableDatabase();
        /*ContentValues类和Hashtable比较类似，它也是负责存储一些名值对，但是它存储的名值对当中的名是一个String类型，而值都是基本类型。*/
        ContentValues values = new ContentValues();
        values.put("uuid",user.getUuid());
        values.put("username",user.getUsername());
        values.put("password",user.getPassword());
        /*插入成功返回id，否则返回-1；nullColumnHack涉及values为空时的一些底层语句合法性问题*/
        db.insert(DB_NAME,null,values);
        values.clear();
    }

    /**
     * 登陆时查询用户信息
     * @return users 查询到的用户
     */
    public LinkedList<User> readUsers() {
        /*LinkedList双向链表，有头尾指针*/
        LinkedList<User> users = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        /*Cursor  存放所有查询到的行的集合
        和它的getString方法*/
        /*rawQuery*/
        Cursor cursor = db.rawQuery("select * from tb_user",null);
        /*moveToFirst()和cursor.moveToNext()反正是从头到尾的意思，将全部人都提取出来传到login.activity处理*/
        if(cursor.moveToFirst()) {
            do{
                /*将改游标所在的那条记录的各列读取出来传进一个新建的user，在添加到一个双向链表回传到login.activity*/
                String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                User user = new User();
                user.setUuid(uuid);
                user.setUsername(username);
                user.setPassword(password);
                users.add(user);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    /**
     * 修改密码功能
     * @param username 用户名
     * @param password 密码
     * @return 是否修改好
     */
    public boolean updateUser(String username,String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        /*execSQL方法中第一个参数为SQL语句，第二个参数为SQL语句中占位符参数的值，参数值在数组中的顺序要和占位符的位置对应。*/
        String sql = "update tb_user set password=? where username=?";
        String[] obj = new String[]{password,username};
        db.execSQL(sql,obj);
        return true;
    }

}
