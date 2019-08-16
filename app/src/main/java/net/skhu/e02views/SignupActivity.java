package net.skhu.e02views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import android.widget.TextView;
import android.util.Patterns;
import java.util.regex.Pattern;
import android.support.annotation.NonNull;

//////////////////////////////////////////////////////////////////// import 파이어베이스
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class SignupActivity extends AppCompatActivity {

    // 비밀번호 정규식
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$");
    private EditText editText_password;
    private EditText editText_password2;
    private String password = editText_password.getText().toString();
    private String password2 = editText_password2.getText().toString();
    private String msg1 = "회원가입 성공";
    private String msg2 = "회원가입 실패";
    private String msg3 = "로그인 성공";
    private String msg4 = "로그인 실패";
    private final DatabaseReference myData01 = FirebaseDatabase.getInstance().getReference("myData01");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        ValueEventListener listener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                TextView textView = (TextView) findViewById(R.id.loginId);
                textView.setText(value);
                Log.d("내태그", "받은 데이터: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("내태그", "서버 에러: ", error.toException());
            }
        };



        myData01.addValueEventListener(listener1);
        Button button = (Button) findViewById(R.id.button);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText_loginId = (EditText) findViewById(R.id.editText_loginId);
                String loginId = editText_loginId.getText().toString();
                if (isEmptyOrWhiteSpace(loginId))
                    editText_loginId.setError("로그인 아이디를 입력하세요");

                editText_password = (EditText) findViewById(R.id.editText_password);
           //   String password = editText_password.getText().toString();
                if (isEmptyOrWhiteSpace(password))
                    editText_password.setError("비밀번호를 입력하세요");

                editText_password2 = (EditText) findViewById(R.id.editText_password2);
            //    String password2 = editText_password2.getText().toString();
                if (password.equals(password2) == false)
                    editText_password2.setError("비밀번호가 일치하지 않습니다");

                EditText editText_email = (EditText) findViewById(R.id.editText_email);
                String email = editText_email.getText().toString();


                /********************회원 가입 데이터를 서버에 전송하는 코드를 구현해야 함.******************/




            /*
              String msg = "회원가입 성공: " + loginId + " " + email;
                Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_LONG).show();
                */
            }
        };
        button.setOnClickListener(listener);
    }

    // 비밀번호 유효성 검사
    private boolean isValidPasswd () {
        if (password.isEmpty()) {
            // 비밀번호 공백
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            // 비밀번호 형식 불일치
            return false;
        } else {
            return true;
        }
    }


    // 회원가입
    private void createUser(String password) {

        myData01.createUserWithEmailAndPassword(password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            Toast.makeText(SignupActivity.this, msg1, Toast.LENGTH_LONG).show();
                        } else {
                            // 회원가입 실패
                            Toast.makeText(SignupActivity.this, msg2, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // 로그인
    private void loginUser(String password)
    {
        myData01.signInWithEmailAndPassword(password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(SignupActivity.this, msg3, Toast.LENGTH_SHORT).show();
                        } else {
                            // 로그인 실패
                            Toast.makeText(SignupActivity.this, msg4, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    static boolean isEmptyOrWhiteSpace(String s) {
        if (s == null) return true;
        return s.trim().length() == 0;
    }
}
