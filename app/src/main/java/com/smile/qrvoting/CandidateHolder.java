package com.smile.qrvoting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CandidateHolder extends RecyclerView.Adapter<CandidateHolder.ViewHolderAdapter> {


    Context c;

    ArrayList<String> namlist = new ArrayList<>();
    ArrayList<String> pinlist = new ArrayList<>();
    ArrayList<String> catlist = new ArrayList<>();
    ArrayList<String> wrdlist = new ArrayList<>();
    ArrayList<String> flagurllist = new ArrayList<>();

    public CandidateHolder(Context c, ArrayList<String> namlist, ArrayList<String> pinlist, ArrayList<String> catlist, ArrayList<String> wrdlist, ArrayList<String> flagurllist) {
        this.c = c;
        this.namlist = namlist;
        this.pinlist = pinlist;
        this.catlist = catlist;
        this.wrdlist = wrdlist;
        this.flagurllist = flagurllist;
    }

    @NonNull
    @Override
    public ViewHolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater ly = LayoutInflater.from(parent.getContext());
        View v = ly.inflate(R.layout.row_candidate , parent , false);

        return new ViewHolderAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAdapter holder, final int position) {

        holder.name.setText(namlist.get(position)+" "+catlist.get(position));

        holder.vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVoteCount(pinlist.get(position), Monitor.getVoterid());
                updateVotingDetails(Monitor.getVoterid(), Monitor.getVotername(), Monitor.getWard(), pinlist.get(position), namlist.get(position));
                //c.startActivity(new Intent(c, VoteResult.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return namlist.size();
    }

    public class ViewHolderAdapter extends RecyclerView.ViewHolder
    {
        CircleImageView curl,furl;
        TextView name;
        Button vote;

        public ViewHolderAdapter(@NonNull View itemView) {
            super(itemView);

            curl = itemView.findViewById(R.id.row_curl);
            furl = itemView.findViewById(R.id.row_furl);
            name = itemView.findViewById(R.id.row_cname);
            vote = itemView.findViewById(R.id.row_vote);

        }
    }

    public void updateVoteCount(String cid, String vid){
        StringRequest request = new StringRequest(Request.Method.GET, "https://sanjaytask.000webhostapp.com/QR%20Voting/updateVoteCount.php?CandidId="+cid+"&Voterid="+vid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Updated")){
                    c.startActivity(new Intent(c, VoteResult.class));
                }else {
                    Toast.makeText(c, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(c);
        queue.add(request);
    }

    public void updateVotingDetails(final String vid, final String vname, final String ward, final String cid, final String cname){
        StringRequest request = new StringRequest(Request.Method.POST, "https://sanjaytask.000webhostapp.com/QR%20Voting/votingDetails.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Updated")){
                    c.startActivity(new Intent(c, VoteResult.class));
                }else {
                    Toast.makeText(c, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("vid", vid);
                params.put("vname", vname);
                params.put("ward", ward);
                params.put("cid", cid);
                params.put("cname", cname);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(c);
        queue.add(request);
    }

}


