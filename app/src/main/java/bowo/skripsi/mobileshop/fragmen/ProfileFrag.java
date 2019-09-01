package bowo.skripsi.mobileshop.fragmen;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import bowo.skripsi.mobileshop.HomeAct;
import bowo.skripsi.mobileshop.LoginAct;
import bowo.skripsi.mobileshop.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFrag extends Fragment {
    private SharedPreferences sharedpreferences;
    private String username,email;
    private Button logout;
    private TextView usernames, emails;
    public ProfileFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        logout = v.findViewById(R.id.logout);
        usernames = v.findViewById(R.id.username);
        emails = v.findViewById(R.id.etEmail);
        sharedpreferences = getActivity().getSharedPreferences(LoginAct.my_shared_preferences, Context.MODE_PRIVATE);
        username = sharedpreferences.getString("username",null);
        email = sharedpreferences.getString("email",null);

        emails.setText(email);
        usernames.setText(username);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean(LoginAct.session_status, false);
                editor.putString("username", null);
                editor.putString("email",null);
                editor.commit();

                Intent intent = new Intent(getActivity(), HomeAct.class);
                startActivity(intent);
            }
        });
        return v;
    }

}
