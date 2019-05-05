package com.kdfly.sms2wechat.app.faq;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kdfly.sms2wechat.R;

import java.util.List;

/**
 * FAQ fragment
 */
public class FaqFragment extends Fragment {
    
    public FaqFragment() {
    }

    public static FaqFragment newInstance() {
        return new FaqFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            List<FaqItem> items = new FaqItemContainer(context).getFaqItems();
            recyclerView.setAdapter(new FaqItemAdapter(context, items));
        }
        return view;
    }
}
