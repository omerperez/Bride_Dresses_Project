package com.example.bride_dresses_project.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bride_dresses_project.R;
import com.example.bride_dresses_project.model.Dress;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEditDressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditDressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText mPriceEditText;
    private Button mSaveEditButton;
    private ImageView mDressImage;
    private TextView mHeaderTextView;
    private ProgressBar mProgressBar;
    private final FirebaseFirestore mDB = FirebaseFirestore.getInstance();

    private Dress mEditItem;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddEditDressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEditDressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEditDressFragment newInstance(String param1, String param2) {
        AddEditDressFragment fragment = new AddEditDressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_dress, container, false);
    }
}