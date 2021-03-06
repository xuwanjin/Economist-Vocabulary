package com.xuwanjin.inchoate.ui.floataction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.xuwanjin.inchoate.R;
import com.xuwanjin.inchoate.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matthew Xu
 */
public class FloatActionFragment extends BaseFragment<IssueCategoryAdapter, IssueCategoryItemDecoration, List<String>, LinearLayoutManager> {
    public static final String TAG = "FloatActionFragment";

    private List<String> mSectionList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView = view.findViewById(R.id.float_action_recyclerView);
        View mHeaderSectionView = LayoutInflater.from(getContext()).inflate(R.layout.float_action_header, mRecyclerView, false);
        mBaseAdapter = new IssueCategoryAdapter(getContext(), mSectionList);
        mBaseAdapter.setHeaderView(mHeaderSectionView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.addItemDecoration(new IssueCategoryItemDecoration(getContext(), mRecyclerView));
    }

    @Override
    protected void loadData() {
        if (getActivity() == null) {
            return;
        }
        ArrayList<String> sequenceArrayList = getActivity().getIntent().getStringArrayListExtra("issue_section");
        if (sequenceArrayList == null || sequenceArrayList.size() == 0) {
            return;
        }
        mSectionList.clear();
        mSectionList.addAll(sequenceArrayList);
        mSectionList.add(0, "This week");
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_float_action;
    }

    @Override
    protected List<String> fetchDataFromDBOrNetwork() {
        return null;
    }

    @Override
    protected List<String> initFakeData() {
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
