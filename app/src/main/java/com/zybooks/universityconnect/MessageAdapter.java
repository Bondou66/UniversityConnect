//package com.zybooks.universityconnect;
//
//import android.content.Context;
//import android.text.format.DateFormat;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.firestore.Query;
//import com.google.firebase.storage.StorageReference;
//
//public class MessageAdapter extends FirestoreRecyclerAdapter<Message, MessageAdapter.MessageHolder>{
//    private Context context;
//    private String userId;
//    private StorageReference storageReference;
//    private RequestOptions requestOptions = new RequestOptions();
//    private final int MESSAGE_IN_VIEW_TYPE  = 1;
//    private final int MESSAGE_OUT_VIEW_TYPE = 2;
//
//    public MessageAdapter(@NonNull Context context, Query query, String userID) {
//        /*
//        Configure recycler adapter options:
//        query defines the request made to Firestore
//        Message.class instructs the adapter to convert each DocumentSnapshot to a Message object
//        */
//        super(new FirestoreRecyclerOptions.Builder<Message>()
//                .setQuery(query, Message.class)
//                .build());
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        //if message userId matches current userid, set view type 1 else set view type 2
//        if(getItem(position).getMessageUserId().equals(userId)){
//            return MESSAGE_OUT_VIEW_TYPE;
//        }
//        return MESSAGE_IN_VIEW_TYPE;
//    }
//
//    @Override
//    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        /*
//        We're using two different layouts. One for messages from others and the other for user's messages
//         */
//        View view = null;
//        if(viewType==MESSAGE_IN_VIEW_TYPE){
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.mssg, parent, false);
//        }
//        else{
//            view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.mssg_out, parent, false);
//        }
//        return new MessageHolder(view);
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull MessageHolder holder, int position, @NonNull Message model) {
//        //Bind values from Message to the viewHolder
//
//        final TextView mText = holder.mText;
//        final TextView mUsername = holder.mUsername;
//        final TextView mTime = holder.mTime;
//        final TextView mLikesCount = holder.mLikesCount;
//        final CircleImageView imgProfile = holder.imgProfile;
//        final ImageView imgDropdown = holder.imgDropdown;
//        final ImageView imgLikes = holder.imgLikes;
//
//        mUsername.setText(model.getMessageUser());
//        mText.setText(model.getMessageText());
//        mTime.setText(DateFormat.format("dd MMM  (h:mm a)", model.getMessageTime()));
//        mLikesCount.setText(String.valueOf(model.getMessageLikesCount()));
//        if(model.getMessageLikes()!=null){
//            if(model.getMessageLikes().containsValue(userId)){
//                imgLikes.setImageResource(R.drawable.ic_favorite_red_24dp);
//            }
//            else{
//                imgLikes.setImageResource(R.drawable.ic_favorite_black_24dp);
//            }
//        }
//        GlideApp.with(context)
//                .setDefaultRequestOptions(requestOptions)
//                .load(storageReference.child(model.getMessageUserId()))
//                .into(imgProfile);
//
//    }
//
//
//    public class MessageHolder extends RecyclerView.ViewHolder {
//        TextView text;
//        TextView username;
//        TextView time;
//        public MessageHolder(View itemView) {
//            super(itemView);
//            text = itemView.findViewById(R.id.message_text);
//            username = itemView.findViewById(R.id.message_user);
//            time = itemView.findViewById(R.id.message_time);
//        }
//}
