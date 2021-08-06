package com.example.lancalearn;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

public class module_viewHolder extends RecyclerView.ViewHolder {
     MaterialTextView name, code;
     AppCompatImageView img;
     MaterialCardView cv;




    public module_viewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name_input);
        code =  itemView.findViewById(R.id.code_input);
        img = itemView.findViewById(R.id.icon);
        cv=itemView.findViewById(R.id.cv);

    }


}
