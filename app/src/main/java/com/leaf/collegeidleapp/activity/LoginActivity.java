package com.leaf.collegeidleapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leaf.collegeidleapp.MainActivity;
import com.leaf.collegeidleapp.R;
import com.leaf.collegeidleapp.bean.User;
import com.leaf.collegeidleapp.util.UserDbHelper;

import java.util.LinkedList;

/**
 * 登录界面活动类
 */
public class LoginActivity extends AppCompatActivity {
//两输入框
    EditText EtStuNumber,EtStuPwd;
    private String username;
//一个存储User的双向链表，有头尾指针，较灵活
    LinkedList<User> users = new LinkedList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//新用户注册的textview
        TextView tvRegister = findViewById(R.id.tv_register);
        //设置监听，跳转到注册界面
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

//绑定账号密码框
        EtStuNumber = findViewById(R.id.et_username);
        EtStuPwd = findViewById(R.id.et_password);
//绑定登录按钮
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            //点击事件
            public void onClick(View v) {
                boolean flag = false;
                //查空
                if(CheckInput()) {
                    //
                    //传入的前两个参数一个是context是activity的父类，一个是UserDbHelper.DB_NAME是一个静态属性，不可更改的，且是public公开的
                    UserDbHelper dbHelper = new UserDbHelper(getApplicationContext(),UserDbHelper.DB_NAME,null,1);
                    //一个存储User的双向链表，有头尾指针，较灵活，读出全部的用户信息
                    users = dbHelper.readUsers();
                    //将其中双向链表中的User循环迭代到user中与输入框的对比

                    for(User user : users) {
                        //如果可以找到,则输出登录成功,并跳转到主界面  JDK1.5
                        if(user.getUsername().equals(EtStuNumber.getText().toString()) && user.getPassword().equals(EtStuPwd.getText().toString()) )
                        {
                            flag = true;
                            Toast.makeText(LoginActivity.this,"恭喜你登录成功!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            //这里利用Bundle传递了登录的用户名
                            Bundle bundle = new Bundle();
                            username = EtStuNumber.getText().toString();
                            bundle.putString("username",username);
                            intent.putExtras(bundle);

                            startActivity(intent);
                        }
                    }
                    //否则提示登录失败,需要重新输入
                    if (!flag) {
                        Toast.makeText(LoginActivity.this,"学号或密码输入错误!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //检查输入是否符合要求，检查学号和密码的空
    //trim()函数是用于去除字符串头尾的
    public boolean CheckInput() {
        String StuNumber = EtStuNumber.getText().toString();
        String StuPwd = EtStuPwd.getText().toString();
        if(StuNumber.trim().equals("")) {
            Toast.makeText(LoginActivity.this,"学号不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(StuPwd.trim().equals("")) {
            Toast.makeText(LoginActivity.this,"密码不能为空!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
