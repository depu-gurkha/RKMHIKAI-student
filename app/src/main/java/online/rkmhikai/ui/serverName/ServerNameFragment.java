package online.rkmhikai.ui.serverName;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import online.rkmhikai.R;
import online.rkmhikai.config.SharedPrefManager;
import online.rkmhikai.config.URLs;
import online.rkmhikai.ui.authentication.LoginFragment;

public class ServerNameFragment extends Fragment {

    EditText etServerName;
    Button btSaveAddress;

    FragmentManager fragmentManager;

    private ServerNameViewModel mViewModel;

    public static ServerNameFragment newInstance() {
        return new ServerNameFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.server_name_fragment, container, false);
        etServerName = v.findViewById(R.id.et_server_name);
        btSaveAddress = v.findViewById(R.id.bt_save_address);

        Bundle bundle = this.getArguments();

        if (bundle != null){
            String fragmentName = bundle.getString("fragment_key");
            Toast.makeText(getContext(), "Coming From: "+fragmentName, Toast.LENGTH_SHORT).show();
        }

        etServerName.setText(SharedPrefManager.getInstance(getActivity().getApplicationContext()).getServerAddress());
        btSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getActivity().getApplicationContext()).setServerAddress(etServerName.getText().toString());
                //Toast.makeText(getContext(), "Server Name changed to: "+etServerName.getText().toString(), Toast.LENGTH_SHORT).show();

                if(bundle != null) {
                    fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    LoginFragment loginFragment = new LoginFragment();
                    fragmentTransaction.replace(R.id.fragment_container, loginFragment, null);
                    fragmentTransaction.commit();
                }
                Toast.makeText(getContext(), "Server Address Changed to: "+SharedPrefManager.getInstance(getContext()).getServerAddress(), Toast.LENGTH_SHORT).show();

            }
        });
        return  v;
    }

    @Override 
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ServerNameViewModel.class);
        // TODO: Use the ViewModel
    }

}