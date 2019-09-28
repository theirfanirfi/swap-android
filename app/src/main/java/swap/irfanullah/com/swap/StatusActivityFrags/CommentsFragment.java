package swap.irfanullah.com.swap.StatusActivityFrags;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import swap.irfanullah.com.swap.CustomComponents.CommentDialog;
import swap.irfanullah.com.swap.Libraries.RetroLib;
import swap.irfanullah.com.swap.Models.Comment;
import swap.irfanullah.com.swap.Models.RMsg;
import swap.irfanullah.com.swap.Models.User;
import swap.irfanullah.com.swap.R;
import swap.irfanullah.com.swap.Storage.PrefStorage;

public class CommentsFragment extends Fragment implements CommentDialog.CommentCallBack, CommentsAdapter.CommentDelete {

    private RecyclerView sRV;
    private CommentsAdapter commentsAdapter;
    private ArrayList<Comment> comments;
    private Context context;
    private ProgressBar progressBar;
    private Bundle bundle;
    private String STATUS_ID = "";
    private FloatingActionButton commentFloatingActionButton;
    CommentDialog commentDialog;
    User user;
    public CommentsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_status_comments, container, false);
        sRV = rootView.findViewById(R.id.chatRV);
        progressBar = rootView.findViewById(R.id.statusLoadingProgressbar);
        commentFloatingActionButton = rootView.findViewById(R.id.floatingActionButton);

        context = getContext();
        user = PrefStorage.getUser(context);
        bundle = getArguments();
        STATUS_ID = bundle.getString("status_id");
        makeRequest();
        commentsAdapter= new CommentsAdapter(getActivity(),comments);
        commentsAdapter.setCommentDeleteCallback(this);
        sRV.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        sRV.setLayoutManager(layoutManager);
        sRV.setAdapter(commentsAdapter);
        commentBtn();
         return rootView;
    }

    private void makeRequest() {
        comments = new ArrayList<>();
        RetroLib.geApiService().getStatusComments(user.getTOKEN(), this.STATUS_ID).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(response.isSuccessful()) {

                    Comment comment = response.body();
                    if(comment.isError() || !comment.isAuthenticated()){
                        RMsg.toastHere(context,comment.getMESSAGE());
                    }else if(comment.isFound()){
                        progressBar.setVisibility(View.GONE);
                        comments = comment.getCOMMENTS();
                        notifyAdapter(comments);

                    }else {
                        progressBar.setVisibility(View.GONE);
                        RMsg.toastHere(context,comment.getMESSAGE());
                    }
                }
                else {
                    Log.i("STATUES: ","NOT SUCCESSFULL "+response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(getContext(),"ERROR: "+t.toString(),Toast.LENGTH_LONG).show();
                Log.i("STATUES: ","NOT SUCCESSFULL "+t.toString());

            }
        });
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
          //  context = getContext();
            // /   makeRequest();

//            context = getActivity();
//            user = PrefStorage.getUser(context);
//            makeRequest();
        }
    }

    public void notifyAdapter(ArrayList<Comment> comments){
        commentsAdapter.notifyAdapter(comments);
    }

    private void commentBtn(){
        commentDialog = new CommentDialog();
        commentDialog.setOnCommentClickListener(this);
        commentFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//                dialog.setTitle("Post a comment");

                Bundle bundle = new Bundle();
                bundle.putString("status_id",STATUS_ID);
                commentDialog.setArguments(bundle);
                commentDialog.setCancelable(false);
                commentDialog.show(getActivity().getSupportFragmentManager(),"comment_on_post");
            }
        });
    }

    @Override
    public void onCommented() {
        //RMsg.toastHere(context,"fragment commented.");
        makeRequest();
    }

    @Override
    public void onCommentFail() {

    }

    @Override
    public void callBack() {
        makeRequest();
    }
}
