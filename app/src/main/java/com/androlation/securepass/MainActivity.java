package com.androlation.securepass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText inputText1, inputText2;
    Button encBtn, decBtn;
    TextView outputText;
    String outputString;
    String AES = "AES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputText1 = (EditText)findViewById(R.id.edit_text1);
        inputText2 = (EditText)findViewById(R.id.edit_text2);
        outputText = (TextView)findViewById(R.id.text_view);

        encBtn = (Button)findViewById(R.id.encBtn);
        decBtn = (Button)findViewById(R.id.decBtn);

        encBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    outputString = encrypt(inputText1.getText().toString(), inputText2.getText().toString());
                    outputText.setText("Encrypted Value: "+outputString);
                    //Toast.makeText(getApplicationContext(), "done",
                    //  Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

        decBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    outputString = decrypt(outputString, inputText2.getText().toString());

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Key does not match", Toast.LENGTH_SHORT).show();
                }

                outputText.setText("Decrypted Value: "+outputString);
            }
        });
    }



    private String decrypt(String outputString, String password)throws Exception{
        SecretKeySpec Key = generateKey(password);
        Cipher sakib = Cipher.getInstance(AES);
        sakib.init(Cipher.DECRYPT_MODE,Key);
        byte[] decodedValue = Base64.decode(outputString, Base64.DEFAULT);
        byte[] decVal = sakib.doFinal(decodedValue);
        String decryptedValue = new String(decVal);
        return decryptedValue;
    }



    private String encrypt(String Data, String password) throws Exception{
        SecretKeySpec Key = generateKey(password);
        Cipher sakib = Cipher.getInstance(AES);
        sakib.init(Cipher.ENCRYPT_MODE,Key);
        byte[] encVal = sakib.doFinal(Data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }



    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] Key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(Key, "AES");
        return secretKeySpec;
    }

}
