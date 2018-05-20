package com.noscale.bos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.noscale.bos.controllers.MainController;
import com.noscale.bos.controllers.MessageController;

public class MainActivity extends AppCompatActivity {

    private MainController mainController;
    private MessageController messageController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setup () {
        mainController = new MainController(this, MainController.TAG);
        messageController = new MessageController(this, MessageController.TAG);
    }

    public MainController getMainController () {
        return mainController;
    }

    public MessageController getMessageController () {
        return messageController;
    }

}
