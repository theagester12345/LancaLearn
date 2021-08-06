package com.example.lancalearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.shrikanthravi.chatview.data.Message;
import com.shrikanthravi.chatview.widget.ChatView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;

public class chat_app extends AppCompatActivity {
    private ChatView chatView;
    private Boolean switchbool=true;
    private final int galleryRequestCode = 200;
    private final int videoRequestCode = 300;
    private final int fileRequestCode = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_app);
        getSupportActionBar().setTitle("Chats");
        chatView = (ChatView) findViewById(R.id.chatView);
//
//        //sample code to add message to right
//        Message message = new Message();
////        message.setBody(messageET.getText().toString().trim()); //message body
//        message.setType(Message.RightSimpleMessage); //message type
////        message.setTime(getTime()); //message time (String)
//        message.setUserName("Groot"); //sender name
//        //sender icon
//        message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
//        chatView.addMessage(message);
//
////sample code to add message to left
//        Message message1 = new Message();
////        message1.setBody(messageET.getText().toString().trim());
//        message1.setType(Message.LeftSimpleMessage);
////        message1.setTime(getTime());
//        message1.setUserName("Hodor");
//        message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
//        chatView.addMessage(message);
//
////to clear messages
//        chatView.clearMessages();

//to remove message
//        chatView.remove(position) //or  chatview.remove(message)


        //Send button click listerer
        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
            @Override
            public void onSendButtonClick(String body) {
                if (switchbool) {
                    Message message = new Message();
                    message.setBody(body);
                    message.setType(Message.RightSimpleMessage);
//                    message.setTime(getTime());
                    message.setUserName("you");
//                    message.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/groot"));
                    chatView.addMessage(message);

                    switchbool = false;
                } else {
                    Message message1 = new Message();
                    message1.setBody(body);
                    message1.setType(Message.LeftSimpleMessage);
//                    message1.setTime(getTime());
                    message1.setUserName("Participant");
//                    message1.setUserIcon(Uri.parse("android.resource://com.shrikanthravi.chatviewlibrary/drawable/hodor"));
                    chatView.addMessage(message1);

                    switchbool = true;
                }
            }
        });

        //Gallery button click listener
        chatView.setOnClickGalleryButtonListener(new ChatView.OnClickGalleryButtonListener() {
            @Override
            public void onGalleryButtonClick() {
                Matisse.from(chat_app.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(9)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new PicassoEngine())
                        .forResult(galleryRequestCode);
            }
        });

        //Video button click listener
        chatView.setOnClickVideoButtonListener(new ChatView.OnClickVideoButtonListener() {
            @Override
            public void onVideoButtonClick() {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                i.setType("video/*");
                //
                startActivityForResult(i, 300);
            }
        });

        //Camera button click listener
        chatView.setOnClickCameraButtonListener(new ChatView.OnClickCameraButtonListener() {
            @Override
            public void onCameraButtonClicked() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
                file.delete();
                File file1 = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");

                Uri uri = FileProvider.getUriForFile(chat_app.this, getApplicationContext().getPackageName() + ".provider", file1);
                cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(cameraIntent, fileRequestCode);
            }
        });


    }
}