package com.example.casitapartyrentalsadmin.common.utils;

import android.content.Context;
import android.widget.Toast;
import com.example.casitapartyrentalsadmin.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CommonUtils {

    public static boolean validateMueble(Context context, TextInputEditText etName,
                                         TextInputEditText etPrice, TextInputEditText etQuantity,
                                         TextInputEditText etDescription, String URL,
    TextInputLayout tilName, TextInputLayout tilPrice,TextInputLayout tilQuantity, TextInputLayout tilDescription ){
        boolean isValid=true;

        if (etDescription.getText().toString().isEmpty()){
            tilDescription.setError(context.getText(R.string.common_validate_field_required));
            etDescription.requestFocus();
            isValid=false;
        }else {
            tilDescription.setError(null);
        }
        if (etPrice.getText().toString().trim().isEmpty()){
            tilPrice.setError(context.getString(R.string.common_validate_field_required));
            etPrice.requestFocus();
            isValid=false;
        }else if (Float.parseFloat(etPrice.getText().toString().trim())<= 0){
            tilPrice.setError(context.getString(R.string.common_validate_min_quantity));
            etPrice.requestFocus();
            isValid=false;
        }else{
            tilPrice.setError("");
        }

        if (etQuantity.getText().toString().trim().isEmpty()){
            tilQuantity.setError(context.getString(R.string.common_validate_field_required));
            etQuantity.requestFocus();
            isValid=false;
        }else if (Integer.parseInt(etQuantity.getText().toString().trim())<=0){
            tilQuantity.setError(context.getString(R.string.common_validate_min_quantity));
            etQuantity.requestFocus();
            isValid=false;
        }else {
            tilQuantity.setError(null);
        }
        if (etName.getText().toString().isEmpty()){
            tilName.setError(context.getText(R.string.common_validate_field_required));
            etName.requestFocus();
            isValid=false;
        }else{
            tilName.setError(null);
        }
        if (URL==null){
            Toast.makeText(context, R.string.common_validate_URL, Toast.LENGTH_SHORT).show();
            isValid=false;
        }
        return isValid;
    }
}
