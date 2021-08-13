package online.rkmhikai.ui.player;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import online.rkmhikai.ui.authentication.LoginFragment;
import online.rkmhikai.ui.player.nestedlist.ChapterLecture;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int numOfTabs){
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ChapterLecture();
            default:
                return new DescriptionFragment();
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
