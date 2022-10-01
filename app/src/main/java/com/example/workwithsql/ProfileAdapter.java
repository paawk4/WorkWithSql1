package com.example.workwithsql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileAdapter extends BaseAdapter {
    private final Context ctx;
    private final ArrayList<Profile> profiles;

    public ProfileAdapter(Context context, ArrayList<Profile> profiles) {
        this.ctx = context;
        this.profiles = profiles;

    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    @Override
    public Profile getItem(int i) {
        return profiles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_template, parent, false);
        }

        Profile profile = (Profile) getItem(i);

        TextView tvName = (TextView) convertView.findViewById(R.id.Name);
        TextView tvJob = (TextView) convertView.findViewById(R.id.Job);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.Email);
        ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.list_itemAvatar);
        tvName.setText(profile.name);
        tvJob.setText(profile.job);
        tvEmail.setText(profile.email);
        ivAvatar.setImageBitmap(profile.image);
        return convertView;
    }
}
