package com.example.admin.vkreader.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.vkreader.R;
import com.example.admin.vkreader.adapters.CustomAdapter;
import com.example.admin.vkreader.asyncTask.LoadImageFromNetwork;
import com.example.admin.vkreader.asyncTask.ParseTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MyListFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    public onSomeEventListener someEventListener;
    private TextView textView;
    private ImageView imageView;
    private ListView listView;
    private Fragment fragment2;
    private HashMap<String, String> map;
    private LoadImageFromNetwork ld;
    private ParseTask parseTask;
    private SimpleDateFormat sdf;
    private FrameLayout frameLayout;
    private LinearLayout linearLayout;
    private CustomAdapter arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public interface onSomeEventListener {
        public void someEvent(Integer i, ArrayList arr);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_list, container, false);
        imageView = (ImageView) getActivity().findViewById(R.id.imageT);
        textView = (TextView) getActivity().findViewById(R.id.textF);
        frameLayout = (FrameLayout) getActivity().findViewById(R.id.frm);
        fragment2 = getActivity().getSupportFragmentManager().findFragmentById(R.id.details_frag);
        if (fragment2 != null) {
            linearLayout = (LinearLayout) getActivity().findViewById(R.id.fragment2);
            linearLayout.setOnClickListener(this);
            textView.setOnClickListener(this);
        }
        listView = (ListView) v.findViewById(R.id.myList);
        parseTask = new ParseTask(getResources().getString(R.string.url));
        parseTask.execute();
        try {
            arrayAdapter = new CustomAdapter(getActivity(), R.layout.row, parseTask.get());
            listView.setAdapter(arrayAdapter);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (NullPointerException e) {
        }
        listView.setOnItemClickListener(this);
        arrayAdapter.notifyDataSetChanged();
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        someEventListener.someEvent(position, parseTask.getArr());
        if (fragment2 != null) {
            frameLayout.setVisibility(View.GONE);
            map = (HashMap<String, String>) parseTask.getArr().get(position);
            imageView.setVisibility(View.INVISIBLE);
            ld = new LoadImageFromNetwork(imageView, getActivity());
            ld.execute(map.get("imageContent"));
            sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            textView.setText(map.get("textContent") + "\n\n" +
                    sdf.format(Integer.parseInt(map.get("textDate"))));
        }
    }

    @Override
    public void onClick(View v) {
        frameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            textView.setText(map.get("textContent") + "\n\n" +
                    sdf.format(Integer.parseInt(map.get("textDate"))));
            imageView.setImageBitmap(ld.get());
        } catch (NullPointerException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}