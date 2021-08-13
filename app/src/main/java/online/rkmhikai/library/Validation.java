package online.rkmhikai.library;

import android.util.Log;

import com.google.android.material.textfield.TextInputLayout;
import online.rkmhikai.R;

public class Validation {
    public static boolean  isEmpty(TextInputLayout textInputLayout, String message){
        String value = textInputLayout.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        } else {
            textInputLayout.setFocusable(true);
            textInputLayout.setError(null);
            textInputLayout.setErrorIconDrawable(null);
            return true;
        }
    }
    public static boolean isValidPassword(TextInputLayout textInputLayout, String message){
        String check="^(?=.[a-z])(?=.[A-Z])(?=.\\d)(?=.[@$!%?&])[A-Za-z\\d@$!%?&]{8,}$";
        String check1="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        String strpass=textInputLayout.getEditText().getText().toString().trim();
        if(strpass.isEmpty()){
            Log.d("PASS", "isEmpty True: ");
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }
        else if(!strpass.matches(check1)){
            Log.d("PASS", "not match: ");
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }else
        {
            Log.d("PASS", "Success: ");
            textInputLayout.setError(null);
            return true;
        }
    }
    public static boolean isValidEmail(TextInputLayout textInputLayout, String message){
        String check="^([a-zA-Z0-9.]+)@([a-zA-Z]+)\\.([a-zA-Z]+)$";
        String strpass=textInputLayout.getEditText().getText().toString().trim();
        if(strpass.isEmpty()){
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }
        else if(!strpass.matches(check)){
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }else
        {
            textInputLayout.setError(null);
            return true;
        }
    }

    public static boolean isValidText(TextInputLayout textInputLayout, String message){
        String check="^[a-zA-Z ]+$";
        String strpass=textInputLayout.getEditText().getText().toString().trim();
        if(strpass.isEmpty()){
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }
        else if(!strpass.matches(check)){
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }else
        {
            textInputLayout.setError(null);
            return true;
        }
    }
    public static boolean isValidDate(TextInputLayout textInputLayout, String message){
        String check="^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-[0-9]{4}$";
        String strpass=textInputLayout.getEditText().getText().toString().trim();
        if(strpass.isEmpty()){
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }
        else if(!strpass.matches(check)){
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }else
        {
            textInputLayout.setError(null);
            return true;
        }
    }
    public static boolean isValidPhone(TextInputLayout textInputLayout, String message){
        String check="^[0-9]{10}$";
        String strpass=textInputLayout.getEditText().getText().toString().trim();
        if(strpass.isEmpty()){
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }
        else if(!strpass.matches(check)){
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }else
        {
            textInputLayout.setError(null);
            return true;
        }
    }
    public static boolean isValidPincode(TextInputLayout textInputLayout, String message){
        String check="^[0-9]{6}$";
        String strpass=textInputLayout.getEditText().getText().toString().trim();
        if(strpass.isEmpty()){
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }
        else if(!strpass.matches(check)){
            textInputLayout.setErrorIconDrawable(R.drawable.ic_baseline_error_outline_24);
            textInputLayout.setError(message);
            return false;
        }else
        {
            textInputLayout.setError(null);
            return true;
        }
    }

}
