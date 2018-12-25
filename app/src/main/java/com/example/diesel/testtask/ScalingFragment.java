package com.example.diesel.testtask;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.diesel.testtask.Common.REQUEST_CAMERA;
import static com.example.diesel.testtask.Common.REQUEST_GALLERY;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScalingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScalingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScalingFragment extends Fragment
        implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Uri mOutputImageUri;

    private OnFragmentInteractionListener mListener;

    public ScalingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScalingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScalingFragment newInstance(String param1, String param2) {
        ScalingFragment fragment = new ScalingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_browse_image:
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY);
                break;
            case R.id.button_make_photo:
                File file = null;
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    file = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if (file != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            mOutputImageUri);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mOutputImageUri = Uri.fromFile(image);
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    showPicture(selectedImageUri);
                    break;
                }
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = mOutputImageUri;
                    showPicture(selectedImageUri);
                    break;
                }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_scaling, container, false);
        Button buttonBrowseImage = layout.findViewById(R.id.button_browse_image);
        buttonBrowseImage.setOnClickListener(this);
        Button buttonMakePhoto = layout.findViewById(R.id.button_make_photo);
        buttonMakePhoto.setOnClickListener(this);

        return layout;
    }

    void showPicture(Uri pictureUri) {

        try {
            ImageFragment fragment = ImageFragment.newInstance(pictureUri);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment)
                    .addToBackStack(null).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
