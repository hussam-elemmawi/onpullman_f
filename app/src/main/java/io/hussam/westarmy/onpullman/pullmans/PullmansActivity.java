package io.hussam.westarmy.onpullman.pullmans;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import io.hussam.westarmy.onpullman.R;
import io.hussam.westarmy.onpullman.util.ActivityUtils;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class PullmansActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private DrawerLayout mDrawerLayout;

    private PullmansPresenter mPullmansPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pullmans_act);
        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        PullmansFragment pullmansFragment =
                (PullmansFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (pullmansFragment == null) {
            // Create the fragment
            pullmansFragment = PullmansFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), pullmansFragment, R.id.contentFrame);
        }

        // Create the presenter
        mPullmansPresenter = new PullmansPresenter(pullmansFragment);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.pullmans_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.about_navigation_menu_item:
                                startActivity(new Intent(PullmansActivity.this, AboutActivity.class));
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}
