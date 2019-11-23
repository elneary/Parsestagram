package com.example.emma_instagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.emma_instagram.LoginActivity;
import com.example.emma_instagram.Post;
import com.example.emma_instagram.PostsAdapter;
import com.example.emma_instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {

    public final String TAG = "PostsFragment";
    protected RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected ArrayList<Post> mPosts;
    private SwipeRefreshLayout swipeContainer;
    protected Button btnLogout;
    protected RelativeLayout profileContainer;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        //create data source
        mPosts = new ArrayList<>();
        //create the adapter
        adapter = new PostsAdapter(getContext(), mPosts);
        //set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        //set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        profileContainer = view.findViewById(R.id.profileContainer);

        queryPosts();

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                queryPosts();
            }
        });

        btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "User attempted to log out");
                ParseUser.logOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
                Log.i(TAG, "User logged out");
            }
        });
        setLogoutButton(view);
    }

    public void setLogoutButton(View view){
        profileContainer.setVisibility(View.GONE);
    };

    protected void queryPosts() {
        ParseQuery<Post> postQuery = new ParseQuery<Post>(Post.class);
        postQuery.include(Post.KEY_USER);
        postQuery.setLimit(20);
        postQuery.addDescendingOrder(Post.KEY_CREATED_AT);
        postQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Error with query");
                    e.printStackTrace();
                    return;
                }

                adapter.clear();
                adapter.addAll(posts);
                swipeContainer.setRefreshing(false);

                for (int i = 0; i < posts.size(); i++) {
                    Log.d(TAG, "Post: " + posts.get(i).getDescription() + " username: "
                            + posts.get(i).getUser().getUsername());
                }
            }
        });
    }
}
