package com.iyuile.caelum.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iyuile.caelum.R;
import com.iyuile.caelum.view.HeaderLayout;
import com.viewpagerindicator.UnderlinePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 订单
 *
 * @author WangYao
 */
public class OrderFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.common_actionbar_fragment_order)
    LinearLayout mHeaderLinearLayout;
    @Bind(R.id.v_status_bar)
    View statusBar;
    @Bind(R.id.common_actionbar)
    HeaderLayout mHeaderLayout;
    @Bind(R.id.common_actionbar_line)
    View line;
    @Bind(R.id.tv_label)
    View tvLabel;
    @Bind(R.id.vp_order)
    ViewPager mViewPager;
    @Bind(R.id.toolbar_ll_all)
    LinearLayout mAllLayout;
    @Bind(R.id.toolbar_ll_not_pay)
    LinearLayout mNotPayLayout;
    @Bind(R.id.toolbar_ll_not_shipped)
    LinearLayout mNotShippedLayout;
    @Bind(R.id.toolbar_ll_already_shipped)
    LinearLayout mAlreadyShippedLayout;
    @Bind(R.id.toolbar_ll_already_completed)
    LinearLayout mAlreadyCompletedLayout;
    @Bind(R.id.indicator)
    UnderlinePageIndicator mIndicator;

    public static OrderFragment mInstance;
    public static final int REQUEST_CODE_RESULT_REFRESHING = 0x00003011;//请求返回刷新
//    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mDatas;
//    private LinearLayout mAllLayout, mNotPayLayout, mNotShippedLayout, mAlreadyShippedLayout, mAlreadyCompletedLayout;
//    private PageIndicator mIndicator;
    private boolean scrollble = false;// 控制是否可以滑动
    private BaseFragment tab01, tab02, tab03, tab04, tab05;
    public static OrderFragment newInstance() {
        Bundle args = new Bundle();

        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_order, container,false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mInstance = this;
        initHeader();
        initView();
        initTabLine();
    }

    private void initHeader() {
//        LinearLayout mHeaderLinearLayout = (LinearLayout) getActivity().findViewById(R.id.common_actionbar_fragment_order);
//        View statusBar = mHeaderLinearLayout.findViewById(R.id.v_status_bar);
        setStatusBar(getActivity(), statusBar);
//        HeaderLayout mHeaderLayout = (HeaderLayout) mHeaderLinearLayout.findViewById(R.id.common_actionbar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.DEFAULT_TITLE);
        mHeaderLayout.setDefaultTitle(getString(R.string.main_toolbar_tv_order));
        mHeaderLayout.setTitleTypeface(mApplication.getWoodBodyStyleFont());
//        View line = mHeaderLinearLayout.findViewById(R.id.common_actionbar_line);
        line.setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));

//        final View tvLabel = mHeaderLinearLayout.findViewById(R.id.tv_label);
        final TextView tvTitle = mHeaderLayout.getTitleCenter();
        ViewTreeObserver vto = tvTitle.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                tvTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvLabel.getLayoutParams();
                layoutParams.width = tvTitle.getWidth();
                tvLabel.setLayoutParams(layoutParams);
            }
        });
    }

    private void initView() {
//        mViewPager = (ViewPager) getActivity().findViewById(R.id.vp_order);
//        mAllLayout = (LinearLayout) getActivity().findViewById(R.id.toolbar_ll_all);
//        mNotPayLayout = (LinearLayout) getActivity().findViewById(R.id.toolbar_ll_not_pay);
//        mNotShippedLayout = (LinearLayout) getActivity().findViewById(R.id.toolbar_ll_not_shipped);
//        mAlreadyShippedLayout = (LinearLayout) getActivity().findViewById(R.id.toolbar_ll_already_shipped);
//        mAlreadyCompletedLayout = (LinearLayout) getActivity().findViewById(R.id.toolbar_ll_already_completed);

        mAllLayout.setOnClickListener(this);
        mNotPayLayout.setOnClickListener(this);
        mNotShippedLayout.setOnClickListener(this);
        mAlreadyShippedLayout.setOnClickListener(this);
        mAlreadyCompletedLayout.setOnClickListener(this);

        mDatas = new ArrayList<Fragment>();

        tab01 = new OrderAllFragment();
        tab02 = new OrderNotPayFragment();
        tab03 = new OrderNotShippedFragment();
        tab04 = new OrderAlreadyShippedFragment();
        tab05 = new OrderAlreadyCompletedFragment();

        mDatas.add(tab01);
        mDatas.add(tab02);
        mDatas.add(tab03);
        mDatas.add(tab04);
        mDatas.add(tab05);

        /**
         * 根activity里fragment使用getActivity().getSupportFragmentManager()来获取FragmentManager对象
         *
         * 多层嵌套fragment里的fragment,使用getChildFragmentManager()来获取FragmentManager对象
         * 注:setRetainInstance(false);值为:false
         */
        mAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mDatas.get(arg0);
            }

        };
        mViewPager.setAdapter(mAdapter);

        //缓存这个2个页面
        mViewPager.setOffscreenPageLimit(1);

        //控制是否可以滑动(MyViewPager的方法,如果想取消滑动,把mViewpager对象类型换成MyViewPager)
//		mViewPager.setScrollble(scrollble);

        mViewPager.setCurrentItem(0);
    }

    private void initTabLine() {
//        mIndicator = (UnderlinePageIndicator) getActivity().findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RESULT_REFRESHING:
                if (resultCode == Activity.RESULT_OK) {
                    if (tab02 != null)
                        tab02.onActivityResult(requestCode, resultCode, data);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_ll_all:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.toolbar_ll_not_pay:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.toolbar_ll_not_shipped:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.toolbar_ll_already_shipped:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.toolbar_ll_already_completed:
                mViewPager.setCurrentItem(4);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        mInstance = null;
        super.onDestroy();
    }
}
