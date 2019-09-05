package com.restaurent.swaad;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.shaohui.bottomdialog.BottomDialog;

public class AccountFragment extends Fragment {
    private TextView editButton;
    private TextView name,email,number;
    private RelativeLayout logout;
    private EditText editName,editNumber,editEmail;
    private Button updateDetails;
    private UserAccount userAccount;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        editButton=view.findViewById(R.id.EDIT);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        number = view.findViewById(R.id.number);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
       setData();
        editButton.setOnClickListener(v->{
                    BottomDialog.create(getActivity().getSupportFragmentManager())
                            .setViewListener(view1->{
                                editName = view1.findViewById(R.id.editName);
                                editNumber = view1.findViewById(R.id.editNumber);
                                editEmail = view1.findViewById(R.id.editEmail);
                                editName.setText(name.getText());
                                editNumber.setText(number.getText());
                                editEmail.setText(email.getText());
                            updateDetails = view1.findViewById(R.id.update);
                            updateDetails.setOnClickListener(update->{
                                if(TextUtils.isEmpty(editName.getText().toString().trim())) {
                                    Toast.makeText(getActivity(),"Name cannot be empty",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else if(TextUtils.isEmpty(editNumber.getText().toString().trim())||editNumber.getText().length()<10){
                                    Toast.makeText(getActivity(),"Number cannot be empty",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else if(TextUtils.isEmpty(editEmail.getText().toString().trim())){
                                    Toast.makeText(getActivity(),"Email cannot be empty",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else {
                                    UserAccount userAccount = new UserAccount(editEmail.getText().toString().trim(), editNumber.getText().toString().trim(), editName.getText().toString().trim());
                                    FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
                                    DatabaseReference databaseReference1 = firebaseDatabase1.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    databaseReference1.setValue(userAccount);
                                    Toast.makeText(getActivity(),"Account Updated",Toast.LENGTH_SHORT).show();
                                    setData();
                                    return;
                                }
                            });
                            })
                            .setLayoutRes(R.layout.edit_account)
                            .setDimAmount(0.5f)            // Dialog window dim amount(can change window background color）, range：0 to 1，default is : 0.2f
                            .setCancelOutside(true)     // click the external area whether is closed, default is : true
                            .setTag("EDIT DETAILS")// setting the DialogFragment tag
                            .show();
                }
        );
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(v->signOut());
        return view;
    }
    private void signOut() {
       FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                task ->{
                    startActivity(new Intent(getActivity(),MainActivity.class));
                    getActivity().finish();
                }
        );
    }
    private void setData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Users").child(FirebaseAuth.getInstance().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userAccount = dataSnapshot.getValue(UserAccount.class);
                if(userAccount==null){
                    name.setText("");
                    email.setText("");
                    number.setText("");
                }
                else {
                    name.setText(userAccount.getName());
                    email.setText(userAccount.getEmail());
                    number.setText(userAccount.getNumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}