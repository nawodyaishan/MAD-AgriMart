package lk.sltc.agrimarket;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import io.paperdb.Paper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LogoutFragment extends Fragment {
    private Button Logout;
    private Button cancelLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View View = inflater.inflate(R.layout.fragment_logout, container, false);
        Logout = (Button) View.findViewById(R.id.btn_logout);
        cancelLogout = (Button) View.findViewById(R.id.btn_cancel_logout);


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Paper.book().destroy();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        cancelLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });


        return View;
    }
}