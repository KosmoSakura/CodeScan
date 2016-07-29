//package project.zero.codescan.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import com.windward.sharelibrary.BitmapUtils;
//
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import project.zero.codescan.R;
//import project.zero.codescan.bean.ChatBean;
//import project.zero.codescan.core.ScreenUtil;
//import project.zero.codescan.utils.TimeUtils;
//
//public class StewardCustonAdapter extends BaseAdapter {
//
//    private List<ChatBean> mData;
//
//    private Context mContext;
//    private StewardChartBean.UserEntity mUser;
//    private StewardChartBean.BankerEntity mBanker;
//
//    public StewardCustonAdapter(Context context, List<ChatBean> infos) {
//        this.mContext = context;
//        this.mData = infos;
//    }
//
//    public void addList(List<ChatBean> infos, boolean append) {
//        if (append) {
//            mData.addAll(0, infos);
//        } else {
//            mData.clear();
//            mData.addAll(infos);
//        }
//        notifyDataSetChanged();
//    }
//
//    public void addItem(ChatBean info) {
//        mData.add(info);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getCount() {
//        return mData.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return mData.get(i);
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return i;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        // 消息类型: 1=文字消息 2=图片消息 3=视频消息
//        // 4=管家的回复 5=管家分享的理财产品 6=沟通结束 7=用户评价管家
//        try {
//            return "123".contains(mData.get(position).getType()) ? 1 : ("6".contains(mData.get(position).getType()) ? 2 : 0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return 0;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 3;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        ViewHolder holder = null;
//        ViewHolderEvaluate evaluateHolder = null;
//        int type = getItemViewType(i);
//        if (view == null) {
//            if (type == 0) {
//                view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.feedback_comment_left_item_layout, null);
//                holder = new ViewHolder();
//                ButterKnife.bind(holder, view);
//                view.setTag(holder);
//            } else if (type == 1) {
//                view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.feedback_comment_right_item_layout, null);
//                holder = new ViewHolder();
//                ButterKnife.bind(holder, view);
//                view.setTag(holder);
//            } else {
//                view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.feedback_comment_center_item_layout, null);
//                evaluateHolder = new ViewHolderEvaluate();
//                ButterKnife.bind(evaluateHolder, view);
//                view.setTag(holder);
//            }
//
//        } else {
//            if (type == 2) {
//                evaluateHolder = (ViewHolderEvaluate) view.getTag();
//                if (evaluateHolder == null) {
//                    view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.feedback_comment_center_item_layout, null);
//                    evaluateHolder = new ViewHolderEvaluate();
//                    ButterKnife.bind(evaluateHolder, view);
//                    view.setTag(holder);
//                }
//            } else
//                holder = (ViewHolder) view.getTag();
//            if (holder == null) {
//                if (type == 0) {
//                    view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.feedback_comment_left_item_layout, null);
//                    holder = new ViewHolder();
//                    ButterKnife.bind(holder, view);
//                    view.setTag(holder);
//                } else if (type == 1) {
//                    view = ((Activity) mContext).getLayoutInflater().inflate(R.layout.feedback_comment_right_item_layout, null);
//                    holder = new ViewHolder();
//                    ButterKnife.bind(holder, view);
//                    view.setTag(holder);
//                }
//            }
//        }
//
//        final ChatBean bean = mData.get(i);
//        if (type == 2) { // 6=沟通结束 9=管家撤回消息
//            if ("6".equalsIgnoreCase(bean.getType())) {
//                evaluateHolder.evaluate.setVisibility(View.VISIBLE);
//                Drawable drawable = mContext.getResources().getDrawable(R.drawable.icon_plaint_xxh);
//                drawable.setBounds(0, 0,
//                        ScreenUtil.getScalePxValue(drawable.getMinimumWidth()),
//                        ScreenUtil.getScalePxValue(drawable.getMinimumHeight()));
//                evaluateHolder.replay.setCompoundDrawables(drawable, null, null, null);
//                evaluateHolder.line.setVisibility(View.VISIBLE);
//                if (!"0".equalsIgnoreCase(bean.getUser_banker_grade())) {
//                    evaluateHolder.evaluate.setText("已评价");
//                    evaluateHolder.evaluate.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            // TODO 评价管家
//                            ((StewardActivity) mContext).showEvaluateDialog(bean.getId(), bean.getUser_banker_grade());
//                        }
//                    });
//                } else {
//                    evaluateHolder.evaluate.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            // TODO 评价管家
//                            ((StewardActivity) mContext).showEvaluateDialog(bean.getId(), "0");
//                        }
//                    });
//                }
//            } else { // 9=管家撤回消息
//                evaluateHolder.evaluate.setVisibility(View.GONE);
//                evaluateHolder.replay.setText(bean.getMsg());
//                evaluateHolder.replay.setCompoundDrawables(null, null, null, null);
//                evaluateHolder.line.setVisibility(View.GONE);
//            }
//
//            return view;
//        }
//        holder.reply.setVisibility(View.VISIBLE);
//        holder.reply2.setVisibility(View.GONE);
//        holder.llNaviLayout.setVisibility(View.GONE);
//        holder.picture.setVisibility(View.GONE);
//
//        if (bean != null) {
//            if (type == 0 && !TextUtils.isEmpty(mBanker.getAvatar())) { // 1=客户发的文字消息 2=客户发的图片消息 3=客户发的视频消息
//                holder.time.setText(mBanker.getName() + "  " + TimeUtils.formatDate5(bean.getCreated()));
//                BitmapUtils.getInstance(mContext).loadRoundImage(mBanker.getAvatar(), holder.icon, 70);
//                switch (bean.getType()) {
//                    case "9": // 管家撤回消息
//                        holder.reply.setVisibility(View.GONE);
//                        holder.reply2.setText(bean.getMsg());
//                        holder.reply2.setVisibility(View.VISIBLE);
//                        break;
//                    case "8": // 管家无法回复
//                        holder.reply.setText(bean.getMsg());
//                        break;
//
//                    case "4":// 详情内容
//                        holder.llNaviLayout.setVisibility(View.VISIBLE);
//                        holder.reply.setVisibility(View.GONE);
//                        holder.tvTitle.setText(bean.getReply_content().getTitle());
//                        holder.tvContent.setText(bean.getReply_content().getContent());
//                        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(mContext, WebViewActivity.class);
//                                intent.putExtra("title_text",
//                                        mContext.getResources().getString(R.string.title_product_details));
//                                intent.putExtra("url", bean.getReply_content().getDetail_link());
//                                ((BaseActivity) mContext).startActivityForResult(intent, RequestCode.REQUEST_CODE);
//
////                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bean.getReply_content().getDetail_link()));
////                                ((BaseActivity) mContext).startActivity(intent);
//                            }
//                        });
//                        break;
//                }
//            } else if (type == 1 && !TextUtils.isEmpty(mUser.getAvatar())) { // 4=管家的回复 5=管家分享的理财产品 7=用户评价管家 8=管家无法回复
//                holder.time.setText(TimeUtils.formatDate5(bean.getCreated()));
//                holder.reply.setText(bean.getMsg());
//                BitmapUtils.getInstance(mContext).loadRoundImage(mUser.getAvatar(), holder.icon, 70);
//                if ("2".equalsIgnoreCase(bean.getType())) {
//                    BitmapUtils.getInstance(mContext).loadSampleImage(bean.getImg().getSrc(), holder.picture);
//                    holder.picture.setVisibility(View.VISIBLE);
//                    holder.reply.setVisibility(View.GONE);
//                    final ImageView image = holder.picture;
//                    holder.picture.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            // 预览大图
//                            showPopPreview(image, bean.getImg().getSrc());
//                        }
//                    });
//                } else {
//                    holder.picture.setVisibility(View.GONE);
//                    holder.reply.setVisibility(View.VISIBLE);
//                }
//            }
//        }
//
//        return view;
//    }
//
//    // 动画过度显示大图
//    private void showPopPreview(ImageView image, String src) {
//        LinearLayout view = new LinearLayout(mContext);
//        view.setOrientation(LinearLayout.VERTICAL);
//        view.setGravity(Gravity.CENTER);
//        view.setBackgroundColor(mContext.getResources().getColor(R.color.transparent_h));
//
//        ImageView pic = new ImageView(mContext);
//        pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        pic.setClickable(false);
//
//        BitmapUtils.getInstance(mContext).loadSampleImage(src, pic);
//
//        view.addView(pic);
//
//        final PopupWindow popup = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        popup.setBackgroundDrawable(new ColorDrawable(0));
//        popup.setFocusable(true);
//        popup.setOutsideTouchable(true);
//
//        popup.setAnimationStyle(R.style.AnimPreview);
//
//        popup.showAtLocation(image, 0, 0, 0);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popup.dismiss();
//            }
//        });
//
//    }
//
//    public void setUserInfo(StewardChartBean.UserEntity userEntity) {
//        this.mUser = userEntity;
//    }
//
//    public void setBankerInfo(StewardChartBean.BankerEntity bankerEntity) {
//        this.mBanker = bankerEntity;
//    }
//
//    static class ViewHolder {
//        // 账单标题
//        @Bind(R.id.tv_reply)
//        TextView reply;
//        @Bind(R.id.tv_reply2)
//        TextView reply2;
//        @Bind(R.id.tv_time)
//        TextView time;
//        @Bind(R.id.imv_icon)
//        ImageView icon;
//        @Bind(R.id.tv_title)
//        TextView tvTitle;
//        @Bind(R.id.tv_content)
//        TextView tvContent;
//        @Bind(R.id.tv_detail)
//        TextView tvDetail;
//        @Bind(R.id.ll_navi_layout)
//        LinearLayout llNaviLayout;
//        @Bind(R.id.imv_picture)
//        ImageView picture;
//    }
//
//    class ViewHolderEvaluate {
//        @Bind(R.id.tv_evaluate)
//        TextView evaluate;
//        @Bind(R.id.tv_reply)
//        TextView replay;
//        @Bind(R.id.imv_line)
//        ImageView line;
//    }
//}
