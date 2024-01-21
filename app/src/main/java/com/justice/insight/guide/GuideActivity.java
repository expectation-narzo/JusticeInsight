package com.justice.insight.guide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.justice.insight.R;
import com.justice.insight.authenticate.LoginActivity;
import com.justice.insight.guide.pageviewer.PageIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GuideActivity extends AppCompatActivity {

    public PageIndicator wormDotsIndicator;

    private ArrayList<HashMap<String, Object>> number_of_frag = new ArrayList<>();

    public Intent login_opener;
    private LinearLayout linear3;
    private ViewPager viewpager1;
    private PageIndicator linear1;
    private LinearLayout linear5;
    private Button button1;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_guide);
        initialize(_savedInstanceState);
        askNotificationPermission();
        initializeLogic();
        blockss();
        login_opener = new Intent();
    }
    public void blockss()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
    }
    private void initialize(Bundle _savedInstanceState) {
        linear3 = findViewById(R.id.linear3);
        viewpager1 = findViewById(R.id.viewpager1);
        linear1 = findViewById(R.id.linear1);
        linear5 = findViewById(R.id.linear5);
        button1 = findViewById(R.id.button1);

        viewpager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int _position, float _positionOffset, int _positionOffsetPixels) {
                wormDotsIndicator.setCurrentPosition(_position, _positionOffset);
            }

            @Override
            public void onPageSelected(int _position) {
                if (_position == 3) {
                    button1.setVisibility(View.VISIBLE);
                }
                else {
                    button1.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int _scrollState) {

            }
        });
    }
    public void askNotificationPermission() {

    }
    private void initializeLogic() {
        getWindow().setStatusBarColor(0xFF171717);	getWindow().setNavigationBarColor(0xFF171717);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             login_opener.setClass(getApplicationContext(), LoginActivity.class);
            startActivity(login_opener);
            }
        });
        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("1", "1");
            number_of_frag.add(_item);
        }

        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("2", "1");
            number_of_frag.add(_item);
        }

        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("3", "1");
            number_of_frag.add(_item);
        }

        {
            HashMap<String, Object> _item = new HashMap<>();
            _item.put("4", "1");
            number_of_frag.add(_item);
        }

        viewpager1.setAdapter(new Viewpager1Adapter(number_of_frag));
        ((PagerAdapter)viewpager1.getAdapter()).notifyDataSetChanged();
        wormDotsIndicator = (PageIndicator) findViewById(R.id.linear1);  wormDotsIndicator.setCount(((PagerAdapter)viewpager1.getAdapter()).getCount());
        wormDotsIndicator.setCurrentPosition(0, 0);
    }

    public class Viewpager1Adapter extends PagerAdapter {

        Context _context;
        ArrayList<HashMap<String, Object>> _data;

        public Viewpager1Adapter(Context _ctx, ArrayList<HashMap<String, Object>> _arr) {
            _context = _ctx;
            _data = _arr;
        }

        public Viewpager1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _context = getApplicationContext();
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public boolean isViewFromObject(View _view, Object _object) {
            return _view == _object;
        }

        @Override
        public void destroyItem(ViewGroup _container, int _position, Object _object) {
            _container.removeView((View) _object);
        }

        @Override
        public int getItemPosition(Object _object) {
            return super.getItemPosition(_object);
        }

        @Override
        public CharSequence getPageTitle(int pos) {
            // Use the Activity Event (onTabLayoutNewTabAdded) in order to use this method
            return "page " + String.valueOf(pos);
        }

        @Override
        public Object instantiateItem(ViewGroup _container,  final int _position) {
            View _view = LayoutInflater.from(_context).inflate(R.layout.guide_frag, _container, false);

            final ImageView imageview1 = _view.findViewById(R.id.imageview1);
            final TextView textview1 = _view.findViewById(R.id.textview1);
            final TextView textview2 = _view.findViewById(R.id.textview2);

            textview1.setTextSize((int)32);
            textview2.setTextSize((int)13);


            if (_position == 0) {
                imageview1.setImageResource(R.drawable.guide_image_1);
                textview1.setText("Welcome to Games");
                textview2.setText("View all your games here and level up your gaming experience.");
            }
            if (_position == 1) {
                imageview1.setImageResource(R.drawable.guide_image_2);
                textview1.setText("Instantly enhance your gaming experience");
                textview2.setText("Game mode turns on automatically each time your launch a game in \"Games\". You can also switch to Games focus mode for a more immersive gaming experience.");
            }
            if (_position == 2) {
                imageview1.setImageResource(R.drawable.guide_image_3);
                textview1.setText("A handy-dandy Game Toolkit");
                textview2.setText("Dedicated gaming tools to help you beat your opponents and rank up!");
            }
            if (_position == 3) {
                imageview1.setImageResource(R.drawable.guide_image_4);
                textview1.setText("Game captures and reports");
                textview2.setText("Record and re-experience your gaming highlights and view gaming data.");
            }

            _container.addView(_view);
            return _view;
        }
    }
    @Override
    public void onBackPressed(){
        finishAffinity();
    }
    @Deprecated
    public void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    @Deprecated
    public int getLocationX(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[0];
    }

    @Deprecated
    public int getLocationY(View _v) {
        int _location[] = new int[2];
        _v.getLocationInWindow(_location);
        return _location[1];
    }

    @Deprecated
    public int getRandom(int _min, int _max) {
        Random random = new Random();
        return random.nextInt(_max - _min + 1) + _min;
    }

    @Deprecated
    public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
        ArrayList<Double> _result = new ArrayList<Double>();
        SparseBooleanArray _arr = _list.getCheckedItemPositions();
        for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
            if (_arr.valueAt(_iIdx))
                _result.add((double)_arr.keyAt(_iIdx));
        }
        return _result;
    }

    @Deprecated
    public float getDip(int _input) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
    }

    @Deprecated
    public int getDisplayWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Deprecated
    public int getDisplayHeightPixels() {
        return getResources().getDisplayMetrics().heightPixels;
    }
}