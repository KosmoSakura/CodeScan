package project.zero.codescan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import project.zero.codescan.R;
import project.zero.codescan.bean.MenuBean;


public class MenuAdapter extends BaseAdapter {

    private Context c;
    public static ArrayList<MenuBean> ss;

    public MenuAdapter(Context context, ArrayList<MenuBean> str) {
        this.c = context;
        this.ss = str;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ss.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return ss.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(c, R.layout.item_menu, null);
            holder = new ViewHolder();
            holder.tv_menu = (TextView) convertView.findViewById(R.id.tv_menu);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MenuBean bean = ss.get(position);
        holder.tv_menu.setText(bean.getName());
        holder.icon.setImageResource(bean.getResId());

        return convertView;
    }

    class ViewHolder {
        TextView tv_menu;
        ImageView icon;
    }
}
