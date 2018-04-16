package tw.brad.mygetaccounts;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    0);

        }else{
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }

    }

    // 取得使用者帳號
    private void init(){
        accountManager = AccountManager.get(this);
    }

    public void test1(View view) {
        // 處理跨版本的程式差異

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 8(O) 之後的處理方式
            Intent googlePicker = AccountManager.newChooseAccountIntent(
                    null,
                    null,
                    new String[]{"com.google", "com.facebook.auth.login"},
                    true,
                    null,
                    null,
                    null,
                    null
            );
            startActivityForResult(googlePicker, 0);
        }else {
            Account[] accounts = accountManager.getAccounts();
            for (Account account : accounts){
                Log.v("brad", account.type + ":" + account.name);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK){
            String type = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
            String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            Log.v("brad", type + ":" + name);
        }

    }
}
