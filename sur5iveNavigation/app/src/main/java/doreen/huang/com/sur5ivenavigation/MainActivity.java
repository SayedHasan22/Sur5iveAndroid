package doreen.huang.com.sur5ivenavigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get fragment manager
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft;

            switch (item.getItemId()) {
                case R.id.navigation_message:
                    // replace
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_view, new MessageFragment());
                    ft.commit();
                    mTextMessage.setText(R.string.title_message);
                    return true;
                case R.id.navigation_contacts:
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_view, new EmergencyFragment());
                    ft.commit();
                    mTextMessage.setText(R.string.title_contacts);
                    return true;
                case R.id.navigation_emergency:
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_view, new EmergencyFragment());
                    ft.commit();
                    mTextMessage.setText(R.string.title_emergency);
                    return true;
                case R.id.navigation_user:
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_view, new EmergencyFragment());
                    ft.commit();
                    mTextMessage.setText(R.string.title_user);
                    return true;
                case R.id.navigation_history:
                    ft = fm.beginTransaction();
                    ft.replace(R.id.content_view, new EmergencyFragment());
                    ft.commit();
                    mTextMessage.setText(R.string.title_history);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
