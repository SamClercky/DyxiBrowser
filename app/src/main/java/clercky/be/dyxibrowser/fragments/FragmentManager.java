package clercky.be.dyxibrowser.fragments;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Clercky on 30/09/2017.
 */

public class FragmentManager {
    AppCompatActivity ctx;
    ArrayList<Fragment> fragmentList = new ArrayList<>();
    android.support.v4.app.FragmentManager fragmentManager;
    int containerId;

    /**
     * Gives or if not exits creates a new fragment
     * @param fragment the fragment that has to be created
     * @return created fragment or null is not available
     */
    public Fragment GetInstanceOfFragment(FragmentChooser fragment) {
        Fragment f = null;
        Fragment temp;

        temp = checkForFragmentAndCreate(fragment, FragmentChooser.Browser);
        f = (temp == null) ? f : temp;

        return f;
    }

    private Fragment findFragment(FragmentChooser fragment) {
        if (fragment == FragmentChooser.Browser) {
            for (int i = 0; i < fragmentList.size(); i++) {
                if (fragmentList.get(i) instanceof MainBrowserFragment)
                    return fragmentList.get(i);
            }
        }
        return null;
    }

    private Fragment checkForFragmentAndCreate(FragmentChooser fragment, FragmentChooser checkFor) {
        Fragment f = null;
        if (fragment == checkFor) {
            Fragment fr = findFragment(fragment);
            if (fr == null) {
                // create new
                f = new MainBrowserFragment();
                fragmentList.add(f);
            } else {
                f = fr;
            }
        }
        return f;
    }

    public FragmentManager(AppCompatActivity ctx, int containerId) {
        this.ctx = ctx;
        fragmentManager = this.ctx.getSupportFragmentManager();
        this.containerId = containerId;
    }

    public void goTo(FragmentChooser fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment1 = GetInstanceOfFragment(fragment);
        transaction.add(containerId, fragment1);
        transaction.commit();
    }
}
