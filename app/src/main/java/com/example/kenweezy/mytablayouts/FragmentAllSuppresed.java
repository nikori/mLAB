package com.example.kenweezy.mytablayouts;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by KENWEEZY on 2017-01-12.
 */

public class FragmentAllSuppresed extends Fragment {


    private static FragmentAllSuppresed inst;
    ArrayList<String> smsMessagesList = new ArrayList<String>();
    ListView smsListView;
    ArrayAdapter arrayAdapter;
    int counter=0;
    String smsMessage = "";
    public static FragmentAllSuppresed instance() {
        return (new FragmentAllSuppresed());
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    ArrayAdapter<String> myadapter;
    ListView lv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragmentallsuppressed, container, false);
        TextView tv2=(TextView) v.findViewById(R.id.mytext2);
        lv=(ListView) v.findViewById(R.id.lvallsuppresed);
        myadapter=new ArrayAdapter<String>(getActivity(),R.layout.listview_row,R.id.mytext,smsMessagesList);
        lv.setAdapter(myadapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {

                    TextView tvmine=(TextView) view.findViewById(R.id.mytext);
                    Toast.makeText(getActivity(), "clicked on"+tvmine.getText(), Toast.LENGTH_SHORT).show();

                    final String[] smsMessages = smsMessagesList.get(position).split("\n");
                    final String address = smsMessages[0];

                    AlertDialog.Builder mydialog= new AlertDialog.Builder(view.getContext());




                            // .setNeutralButton("Share",null)
                            // .setPositiveButton("Print",null)
                            mydialog.setNeutralButton("Print", new DialogInterface.OnClickListener()

                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent sendIntent = new Intent();
                                    sendIntent.setAction(Intent.ACTION_SEND);
                                    sendIntent.putExtra(Intent.EXTRA_TEXT, address);
                                    sendIntent.setType("text/plain");
                                    startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share)));


                                }
                            });
                            mydialog.setMessage(address);
                            mydialog.setNegativeButton("Close", null);
                            mydialog.show();
                    final AlertDialog dialog = mydialog.create();
                    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.colorPrimary));











                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        refreshSmsInbox();




        return v;
    }




    public void refreshSmsInbox() {
        try {
            ContentResolver contentResolver = getActivity().getContentResolver();
            Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, "address='40147'", null, null);
            int indexBody = smsInboxCursor.getColumnIndex("body");
            int indexAddress = smsInboxCursor.getColumnIndex("address");

            if (indexBody < 0 || !smsInboxCursor.moveToFirst())
                return;
            myadapter.clear();


            do {
                String str = smsInboxCursor.getString(indexBody);
                String mystrbdy = smsInboxCursor.getString(indexBody);
                String stw = new String(mystrbdy);
                String msgaddr = smsInboxCursor.getString(indexAddress);

                if (stw.contains("LDL")) {
                    counter += 1;

                    myadapter.add(mystrbdy);

                }


            } while (smsInboxCursor.moveToNext());
        }
        catch(Exception e){

        }


    }

    public void updateList(final String smsMessage) {
        try {
            myadapter.insert(smsMessage, 0);
            myadapter.notifyDataSetChanged();
        }
        catch(Exception e){

        }
    }
}
