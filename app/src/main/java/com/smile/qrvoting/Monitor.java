package com.smile.qrvoting;

import android.content.Context;
import android.content.SharedPreferences;

public class Monitor {

    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    public static Context context;
    public Monitor(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("Voter", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static void addVoterDetails(String voterid, String votername){
        editor.putString("Voterid", voterid).apply();
        editor.putString("Votername", votername).apply();
    }

    public static void addWardNo(String wardno){
        editor.putString("Ward", wardno).apply();
    }

    public static String getVotername(){
        return pref.getString("Votername", null);
    }

    public static String getVoterid(){
        return pref.getString("Voterid", null);
    }

    public static String getWard(){
        return pref.getString("Ward", null);
    }
}
