package com.example.appchat;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CreateGroupBottomSheetDialog  extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(),R.layout.layout_bottom_sheet_create_group, null);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height =getFullScreen();
        linearLayout.setLayoutParams(params);

        bottomSheetDialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from( (View) view.getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        view.findViewById(R.id.ImageButton_Create_Group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });


        return bottomSheetDialog;
    }

    public static int getFullScreen(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
