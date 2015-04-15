package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import javax.inject.Inject;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import seniorproject.caretakers.caretakersapp.CaretakersApplication;
import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.presenters.LoginPresenter;
import seniorproject.caretakers.caretakersapp.ui.actvities.MainActivity;
import seniorproject.caretakers.caretakersapp.views.LoginView;

public class LoginFragment extends Fragment implements LoginView {

    @Inject
    LoginPresenter presenter;

    EditText mUsernameEdit;
    EditText mPasswordEdit;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.user_login_submit:
                    String username = "sgherri2@illinois.edu";//mUsernameEdit.getText().toString();
                    String password = "W3lcome1";//mPasswordEdit.getText().toString();
                    presenter.login(username, password);

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
        CaretakersApplication app = (CaretakersApplication) this.getActivity().getApplication();
        app.inject(this);
        presenter.setView(this);
        mUsernameEdit = (EditText) view.findViewById(R.id.user_email);
        mPasswordEdit = (EditText) view.findViewById(R.id.user_password);
        view.findViewById(R.id.user_login_submit).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.user_register).setOnClickListener(mOnClickListener);
        return view;
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }

    @Override
    public void onLoginFailed() {
        Crouton.makeText(this.getActivity(), "Login Failed", Style.ALERT).show();
    }
}
