package com.example.lancalearn;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

public class user_viewHolder extends RecyclerView.ViewHolder {
    MaterialTextView name, email;
    MaterialButton call,message;
    AppCompatImageView icon;
    MaterialCardView cv ;
    public user_viewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name_input);
        email = itemView.findViewById(R.id.email_input);
        call = itemView.findViewById(R.id.call);
        message = itemView.findViewById(R.id.message_btn);
        icon = itemView.findViewById(R.id.icon_img);
        cv = itemView.findViewById(R.id.cv);


    }
}
