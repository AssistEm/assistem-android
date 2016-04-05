package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;

public class RegisterFragment extends Fragment {

    EditText mEmailEdit;
    EditText mPasswordEdit;
    EditText mConfirmPasswordEdit;
    EditText mFirstNameEdit;
    EditText mLastNameEdit;
    EditText mPhoneEdit;
    EditText mCommunityNameEdit;
    EditText mCommunityQueryEdit;
    String mCurrentPhotoPath;

    RadioButton mCaretakerRadio;
    RadioButton mPatientRadio;

    Button mTakePictureButton;

    ImageView mImageView;

    TextView mCommunityText;

    boolean mCaretaker = true;

    static final int REQUEST_TAKE_PHOTO = 1;

    View.OnClickListener mRadioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.button_caretaker:
                    mCaretaker = true;
                    break;
                case R.id.button_patient:
                    mCaretaker = false;
                    break;
            }
            if(mCaretaker) {
                mCommunityText.setText(getResources().getString(R.string.find_a_community));
                mCommunityNameEdit.setVisibility(View.GONE);
                mCommunityQueryEdit.setVisibility(View.VISIBLE);
            } else {
                mCommunityText.setText(getResources().getString(R.string.create_a_community));
                mCommunityQueryEdit.setVisibility(View.GONE);
                mCommunityNameEdit.setVisibility(View.VISIBLE);
            }
        }
    };

    private View.OnClickListener mOnClickSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = mEmailEdit.getText().toString();
            String password = mPasswordEdit.getText().toString();
            String confirmPassword = mConfirmPasswordEdit.getText().toString();
            String firstName = mFirstNameEdit.getText().toString();
            String lastName = mLastNameEdit.getText().toString();
            String phone = mPhoneEdit.getText().toString();
            if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                    || firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
                return;
            } else if(!password.equals(confirmPassword)) {
                Toast.makeText(getActivity(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }
            String community;
            if(mCaretaker) {
                community = mCommunityQueryEdit.getText().toString();
            } else {
                community = mCommunityNameEdit.getText().toString();
            }
            if(community.isEmpty()) {
                return;
            }
            if(mCaretaker) {
                AccountHandler.getInstance(getActivity())
                        .caretakerRegister(getActivity(), email, password, firstName, lastName,
                                phone, community);
            } else {
                AccountHandler.getInstance(getActivity())
                        .patientRegister(getActivity(), email, password, firstName, lastName,
                                phone, community, false);
            }
        }
    };

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + "/picupload";
        File dir = new File(storageDir);
        if (!dir.exists())
            dir.mkdir();

        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("upload", "photo path = " + mCurrentPhotoPath);
        return image;
    };

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor << 1;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);

        Matrix mtx = new Matrix();
        mtx.postRotate(90);
        // Rotating Bitmap
        Bitmap rotatedBMP = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mtx, true);

        if (rotatedBMP != bitmap)
            bitmap.recycle();

        mImageView.setImageBitmap(rotatedBMP);
    }

    private View.OnClickListener mOnClickTakePictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photoFile = createImageFile();

            if (photoFile != null) {
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(camera_intent, REQUEST_TAKE_PHOTO);
            }

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, group, false);
        ((Button) view.findViewById(R.id.user_register_submit)).setOnClickListener(mOnClickSubmitListener);
        mEmailEdit = (EditText) view.findViewById(R.id.user_email);
        mPasswordEdit = (EditText) view.findViewById(R.id.user_password);
        mConfirmPasswordEdit = (EditText) view.findViewById(R.id.user_confirm_password);
        mFirstNameEdit = (EditText) view.findViewById(R.id.user_first_name);
        mLastNameEdit = (EditText) view.findViewById(R.id.user_last_name);
        mPhoneEdit = (EditText) view.findViewById(R.id.user_phone);
        mCommunityNameEdit = (EditText) view.findViewById(R.id.create_community_name);
        mCommunityQueryEdit = (EditText) view.findViewById(R.id.find_community_query);
        mCaretakerRadio = (RadioButton) view.findViewById(R.id.button_caretaker);
        mCaretakerRadio.setOnClickListener(mRadioButtonClickListener);
        mPatientRadio = (RadioButton) view.findViewById(R.id.button_patient);
        mPatientRadio.setOnClickListener(mRadioButtonClickListener);
        mCommunityText = (TextView) view.findViewById(R.id.community_title);
        mTakePictureButton = (Button) view.findViewById(R.id.take_picture);
        mTakePictureButton.setOnClickListener(mOnClickTakePictureListener);

        mImageView = (ImageView) view.findViewById(R.id.image_preview);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Log.i("upload", "onActivityResult: " + this);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            setPic();

        }
    }
}
