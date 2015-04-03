package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;

public class LoginFragment extends Fragment {

    EditText mUsernameEdit;
    EditText mPasswordEdit;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.user_login_submit:
                    String username = mUsernameEdit.getText().toString();
                    String password = mPasswordEdit.getText().toString();
                    if(!username.isEmpty() && !password.isEmpty()) {
                        AccountHandler.getInstance(getActivity()).login(getActivity(), username, password);
                    }
                    break;
                case R.id.user_register:
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_holder, new RegisterFragment(), "register_fragment")
                            .addToBackStack("register")
                            .commit();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, group, false);
        ((Button) view.findViewById(R.id.user_login_submit)).setOnClickListener(mOnClickListener);
        ((Button) view.findViewById(R.id.user_register)).setOnClickListener(mOnClickListener);
        mUsernameEdit = (EditText) view.findViewById(R.id.user_email);
        mPasswordEdit = (EditText) view.findViewById(R.id.user_password);
        return view;
    }

}
