package com.example.testservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.IBinder;
import android.os.ISerialSysService;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private ISerialSysService SERIALservice;
    private Button btnSerialRead;
    private Button btnSerialWrite;
    private EditText editTextRead;
    private EditText editTextWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] service = ServiceManager.listServices();
        for(int i=0; i < service.length; i++)
        {
            String s = service[i];
            Log.d("MainActivity", "Services " + s);
        }

        IBinder b = ServiceManager.getService("serialsys");
        if (b == null)
        {
            throw new RuntimeException("serialsys Service not available");
        }

        SERIALservice = ISerialSysService.Stub.asInterface(b);

        btnSerialRead   = (Button) findViewById(R.id.buttonRead);
        editTextRead    = (EditText) findViewById(R.id.editTextRead);
        btnSerialWrite  = (Button) findViewById(R.id.buttonWrite);
        editTextWrite    = (EditText) findViewById(R.id.editTextWrite);

        btnSerialRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String readstring ="";
                    try {
                        readstring = SERIALservice.read_timed(50,1);
                        Log.d("MainActivity", "readstring value " + readstring);
                        editTextRead.setText(readstring);
                    } catch (RemoteException ignore)
                    {
                        Log.e("MainActivity", "Flush Error", ignore);
                    }
                }
            }
        );

        btnSerialWrite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String writestring ="";
                    int value = 0;
                    try {
                        value = SERIALservice.write(writestring);
                        Log.d("MainActivity", "writestring value " + writestring);
                        editTextWrite.setText(Integer.toString(value));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        );
    }
}
