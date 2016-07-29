package project.zero.codescan.utils;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Zull on 2016/1/12.
 */
public abstract class MyTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //text改变之前的状态
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //让TextView一直跟随EditText输入的内容同步显示
    }

    @Override
    public abstract void afterTextChanged(Editable s);
}
