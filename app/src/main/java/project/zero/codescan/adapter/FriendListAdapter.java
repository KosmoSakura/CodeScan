package project.zero.codescan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.zero.codescan.R;
import project.zero.codescan.utils.debug.Debug;

/**
 * Created by KosMos on 2016/7/22 0022 23:39
 */
public class FriendListAdapter extends BaseAdapter {
    private List<String> usernames;
    private Context context;
    private LayoutInflater inflater;

    public FriendListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        usernames = new ArrayList<>();
    }

    public void setList(List<String> list) {
        if (usernames.size() > 0) {
            usernames.clear();
        }
        usernames.addAll(list);

        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return usernames.size();
    }

    @Override
    public Object getItem(int position) {
        return usernames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.friend_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvL.setText(usernames.get(position));
        Debug.d(usernames.get(position)+"+++");
        return convertView;
    }

    static class ViewHolder {
        TextView tvL;

        ViewHolder(View view) {
            tvL = (TextView) view.findViewById(R.id.tv_l);
        }
    }
}
