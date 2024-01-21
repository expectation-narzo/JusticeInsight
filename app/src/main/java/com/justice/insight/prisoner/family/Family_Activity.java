package com.justice.insight.prisoner.family;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.mediacodec.DefaultMediaCodecAdapterFactory;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.session.MediaSession;
import androidx.media3.ui.PlayerView;
import com.bumptech.glide.Glide;
import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaSession2;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.justice.insight.R;
import com.justice.insight.authenticate.LoginActivity;
import com.justice.insight.video_call.respository.MainRepository;
import com.justice.insight.video_call.ui.CallActivity;
import com.permissionx.guolindev.PermissionX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Family_Activity extends AppCompatActivity {

    public ImageView profile_photo;

    public PlayerView playerview;
    public ExoPlayer player;
    public PlayerView playerview1;
    public ExoPlayer player1;
    public ListView ipc_list;
    public ListView related_vedios_list;
    public static final String STREAM_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    public ArrayList<HashMap<String, Object>> list_lawyer = new ArrayList<>();
    public ArrayList<HashMap<String, Object>> list_video_call = new ArrayList<>();

    public ArrayList<HashMap<String, Object>> list_cases = new ArrayList<>();
    public ArrayList<HashMap<String, Object>> list_vedios = new ArrayList<>();

    public ArrayList<HashMap<String, Object>> list_session = new ArrayList<>();

    public ArrayList<HashMap<String, Object>> list_ipc = new ArrayList<>();

    public int open_num = 0;
    public int medical_open = 0;
    public Intent sign_in_opener = new Intent();
    public BottomSheetDialog cases_bottom_sheet_dialog;
    public BottomSheetDialog lawyer_bottom_sheet_dialog;
    public BottomSheetDialog medical_bottom_sheet_dialog;
    public BottomSheetDialog ipc_sheet_dialog;
    public BottomSheetDialog videos_bottom_sheet_dialog;

    public BottomSheetDialog session_bottom_sheet_dialog;


    public View bottom_sheet_layout;
    public View medical_bottom_sheet_layout;
    public View ipc_bottom_sheet_layout;
    private MainRepository mainRepository;
    public ListView listview1;
    public ListView video_calling_list;
    public View lawyer_bottom_sheet_layout;
    public View videos_bottom_sheet_layout;

    public View session_bottom_sheet_layout;

    public static String target_video_caller;
    public ListView session_list;
    private HashMap<String, Object> map = new HashMap<>();
    private FirebaseDatabase _firebase = FirebaseDatabase.getInstance();
    private DatabaseReference login1 = _firebase.getReference("login_saved/username/cases");
    private ChildEventListener _login1_child_listener;
    private DatabaseReference video_calling_list_reference = _firebase.getReference("login_saved/username/cases");
    private ChildEventListener _video_calling_child_listener;
    private DatabaseReference lawyer_list_reference = _firebase.getReference("lawyers");
    private ChildEventListener _lawyer_child_listener;

    private DatabaseReference session_list_reference = _firebase.getReference("sessions");
    private ChildEventListener _session_child_listener;

    private DatabaseReference videos_list_reference = _firebase.getReference("videos");
    private ChildEventListener _videos_child_listener;
    private DatabaseReference ipc_list_reference = _firebase.getReference("ipc");
    private ChildEventListener _ipc_child_listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prisoner_all);
        initizalise_profile();
        tablayout_working();
        FirebaseApp.initializeApp(this);
        initizalise_lawyer(this);
        initialize_cases_list(this);
        blockss();
        initialize_medical_bottom(this);
        initialize_more(this);
        intialize_session();
    }
    public void blockss()
    {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
    }
    public void tablayout_working() {
        LinearLayout session_page_base = (LinearLayout) findViewById(R.id.session_page_base);
        LinearLayout more_page_base = (LinearLayout) findViewById(R.id.more_page_base);
        LinearLayout chat_page_base = (LinearLayout) findViewById(R.id.chat_page_base);
        LinearLayout profile_page_base = (LinearLayout) findViewById(R.id.profile_page_base);

        LinearLayout session_base = (LinearLayout) findViewById(R.id.session_base);
        LinearLayout more_base = (LinearLayout) findViewById(R.id.more_base);
        LinearLayout chat_base = (LinearLayout) findViewById(R.id.chat_base);
        LinearLayout profile_base = (LinearLayout) findViewById(R.id.profile_base);

        ImageView session_base_image = (ImageView) findViewById(R.id.session_base_image);
        ImageView more_base_image = (ImageView) findViewById(R.id.more_base_image);
        ImageView chat_base_image = (ImageView) findViewById(R.id.chat_base_image);
        ImageView profile_base_image = (ImageView) findViewById(R.id.profile_base_image);

        TextView session_base_text = (TextView) findViewById(R.id.session_base_text);
        TextView more_base_text = (TextView) findViewById(R.id.more_base_text);
        TextView chat_base_text = (TextView) findViewById(R.id.chat_base_text);
        TextView profile_base_text = (TextView) findViewById(R.id.profile_base_text);


        session_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_page_base.setVisibility(View.VISIBLE);
                more_page_base.setVisibility(View.GONE);
                chat_page_base.setVisibility(View.GONE);
                profile_page_base.setVisibility(View.GONE);
                session_base_image.setColorFilter(0xFFFFFFFF);
                more_base_image.setColorFilter(0xFF5C5C5C);
                chat_base_image.setColorFilter(0xFF5C5C5C);
                profile_base_image.setColorFilter(0xFF5C5C5C);
                session_base_text.setTextColor(0xFFFFFFFF);
                more_base_text.setTextColor(0xFF5C5C5C);
                chat_base_text.setTextColor(0xFF5C5C5C);
                profile_base_text.setTextColor(0xFF5C5C5C);

            }
        });

        more_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_page_base.setVisibility(View.GONE);
                more_page_base.setVisibility(View.VISIBLE);
                chat_page_base.setVisibility(View.GONE);
                profile_page_base.setVisibility(View.GONE);
                session_base_image.setColorFilter(0xFF5C5C5C);
                more_base_image.setColorFilter(0xFFFFFFFF);
                chat_base_image.setColorFilter(0xFF5C5C5C);
                profile_base_image.setColorFilter(0xFF5C5C5C);
                session_base_text.setTextColor(0xFF5C5C5C);
                more_base_text.setTextColor(0xFFFFFFFF);
                chat_base_text.setTextColor(0xFF5C5C5C);
                profile_base_text.setTextColor(0xFF5C5C5C);

            }
        });

        chat_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_page_base.setVisibility(View.GONE);
                more_page_base.setVisibility(View.GONE);
                chat_page_base.setVisibility(View.VISIBLE);
                profile_page_base.setVisibility(View.GONE);
                session_base_image.setColorFilter(0xFF5C5C5C);
                more_base_image.setColorFilter(0xFF5C5C5C);
                chat_base_image.setColorFilter(0xFFFFFFFF);
                profile_base_image.setColorFilter(0xFF5C5C5C);
                session_base_text.setTextColor(0xFF5C5C5C);
                more_base_text.setTextColor(0xFF5C5C5C);
                chat_base_text.setTextColor(0xFFFFFFFF);
                profile_base_text.setTextColor(0xFF5C5C5C);
            }
        });
        profile_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_page_base.setVisibility(View.GONE);
                more_page_base.setVisibility(View.GONE);
                chat_page_base.setVisibility(View.GONE);
                profile_page_base.setVisibility(View.VISIBLE);
                session_base_image.setColorFilter(0xFF5C5C5C);
                more_base_image.setColorFilter(0xFF5C5C5C);
                chat_base_image.setColorFilter(0xFF5C5C5C);
                profile_base_image.setColorFilter(0xFFFFFFFF);

                session_base_text.setTextColor(0xFF5C5C5C);
                more_base_text.setTextColor(0xFF5C5C5C);
                chat_base_text.setTextColor(0xFF5C5C5C);
                profile_base_text.setTextColor(0xFFFFFFFF);
            }
        });
    }

    public void initizalise_profile() {


        CardView cases_btn = (CardView) findViewById(R.id.cases_btn);
        cases_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cases_bottom_sheet_dialog.show();
            }
        });
        CardView medical_btn = (CardView) findViewById(R.id.medical_btn);
        medical_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medical_bottom_sheet_dialog.show();
            }
        });

        Button sign_out = (Button) findViewById(R.id.sign_out);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_in_opener.setClass(getApplicationContext(), LoginActivity.class);
                startActivity(sign_in_opener);
                finish();
            }
        });


    }

    public void initialize_more(Context context) {
        CardView related_vedios_btn = (CardView) findViewById(R.id.related_vedios_btn);
        CardView ipc_btn = (CardView) findViewById(R.id.ipc_btn);
        CardView suspend_btn = (CardView) findViewById(R.id.suspend_btn);
        CardView health_btn = (CardView) findViewById(R.id.health_btn);

        LinearLayout related_vedios_base = (LinearLayout) findViewById(R.id.related_vedios_base);
        LinearLayout ipc_base = (LinearLayout) findViewById(R.id.ipc_base);
        LinearLayout suspend_base = (LinearLayout) findViewById(R.id.suspend_base);
        LinearLayout health_base = (LinearLayout) findViewById(R.id.health_base);

        related_vedios_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                related_vedios_btn.setCardBackgroundColor(Color.parseColor("#2ea555"));
                ipc_btn.setCardBackgroundColor(Color.parseColor("#212121"));
                suspend_btn.setCardBackgroundColor(Color.parseColor("#212121"));
                health_btn.setCardBackgroundColor(Color.parseColor("#212121"));

                related_vedios_base.setVisibility(View.VISIBLE);
                ipc_base.setVisibility(View.GONE);
                suspend_base.setVisibility(View.GONE);
                health_base.setVisibility(View.GONE);


            }
        });
        ipc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                related_vedios_btn.setCardBackgroundColor(Color.parseColor("#212121"));
                ipc_btn.setCardBackgroundColor(Color.parseColor("#2ea555"));
                suspend_btn.setCardBackgroundColor(Color.parseColor("#212121"));
                health_btn.setCardBackgroundColor(Color.parseColor("#212121"));

                related_vedios_base.setVisibility(View.GONE);
                ipc_base.setVisibility(View.VISIBLE);
                suspend_base.setVisibility(View.GONE);
                health_base.setVisibility(View.GONE);


            }
        });
        suspend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                related_vedios_btn.setCardBackgroundColor(Color.parseColor("#212121"));
                ipc_btn.setCardBackgroundColor(Color.parseColor("#212121"));
                suspend_btn.setCardBackgroundColor(Color.parseColor("#2ea555"));
                health_btn.setCardBackgroundColor(Color.parseColor("#212121"));
                related_vedios_base.setVisibility(View.GONE);
                ipc_base.setVisibility(View.GONE);
                suspend_base.setVisibility(View.VISIBLE);
                health_base.setVisibility(View.GONE);

            }
        });
        health_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                related_vedios_btn.setCardBackgroundColor(Color.parseColor("#212121"));
                ipc_btn.setCardBackgroundColor(Color.parseColor("#212121"));
                suspend_btn.setCardBackgroundColor(Color.parseColor("#212121"));
                health_btn.setCardBackgroundColor(Color.parseColor("#2ea555"));

                related_vedios_base.setVisibility(View.GONE);
                ipc_base.setVisibility(View.GONE);
                suspend_base.setVisibility(View.GONE);
                health_base.setVisibility(View.VISIBLE);

            }
        });

        related_vedios_list = findViewById(R.id.related_vedios_list);
        related_vedios_list.setDivider(null);
        _videos_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                videos_list_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        list_vedios = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                list_vedios.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        related_vedios_list.setAdapter(new Listview3Adapter(list_vedios));
                        ((BaseAdapter) related_vedios_list.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                videos_list_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        list_vedios = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                list_vedios.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        related_vedios_list.setAdapter(new Listview3Adapter(list_vedios));
                        ((BaseAdapter) related_vedios_list.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        videos_list_reference.addChildEventListener(_videos_child_listener);

        ipc_list = findViewById(R.id.ipc_list);
        ipc_list.setDivider(null);
        _ipc_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                ipc_list_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        list_ipc = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                list_ipc.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        ipc_list.setAdapter(new Listview4Adapter(list_ipc));
                        ((BaseAdapter) ipc_list.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                ipc_list_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        list_ipc = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                list_ipc.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        ipc_list.setAdapter(new Listview4Adapter(list_ipc));
                        ((BaseAdapter) ipc_list.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        ipc_list_reference.addChildEventListener(_ipc_child_listener);


        ipc_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ipc_sheet_dialog.show();
                TextView section_bottom_title = ipc_bottom_sheet_layout.findViewById(R.id.section_bottom_title);
                section_bottom_title.setText(list_ipc.get(position).get("section_title").toString());

                TextView section_bottom_number = ipc_bottom_sheet_layout.findViewById(R.id.section_bottom_number);
                section_bottom_number.setText(list_ipc.get(position).get("section_number").toString());

                TextView section_bottom_description = ipc_bottom_sheet_layout.findViewById(R.id.section_bottom_description);
                section_bottom_description.setText(list_ipc.get(position).get("section_bottom_description").toString());
            }
        });


        ipc_sheet_dialog = new BottomSheetDialog(context);
        ipc_bottom_sheet_layout = getLayoutInflater().inflate(R.layout.ipc_bottom_sheet, null);
        ipc_sheet_dialog.setContentView(ipc_bottom_sheet_layout);
        ipc_sheet_dialog.setCancelable(false);
        final ImageView ipc_bottom_sheet_close_btn = (ImageView) ipc_bottom_sheet_layout.findViewById(R.id.ipc_bottom_sheet_close_btn);
        ipc_bottom_sheet_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipc_sheet_dialog.dismiss();

            }
        });


        videos_bottom_sheet_dialog = new BottomSheetDialog(context);
        videos_bottom_sheet_layout = getLayoutInflater().inflate(R.layout.videos_bottom_sheet, null);
        videos_bottom_sheet_dialog.setContentView(videos_bottom_sheet_layout);
        videos_bottom_sheet_dialog.setCancelable(false);
        final ImageView videos_bottom_sheet_close_btn = (ImageView) videos_bottom_sheet_layout.findViewById(R.id.videos_bottom_sheet_close_btn);
        videos_bottom_sheet_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videos_bottom_sheet_dialog.dismiss();
            }
        });


        playerview = videos_bottom_sheet_layout.findViewById(R.id.player_view);
        player = new ExoPlayer.Builder(context).build();
        playerview.setPlayer(player);

        related_vedios_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videos_bottom_sheet_dialog.show();
                TextView video_bottom_title = videos_bottom_sheet_layout.findViewById(R.id.video_bottom_title);
                video_bottom_title.setText(list_vedios.get(position).get("related_videos_title").toString());
                TextView related_videos_bottom_description = videos_bottom_sheet_layout.findViewById(R.id.related_videos_bottom_description);
                related_videos_bottom_description.setText(list_vedios.get(position).get("related_videos_bottom_description").toString());

                MediaItem mediaitem = new MediaItem.Builder()
                        .setUri(list_vedios.get(position).get("related_video_bottom_video").toString())
                        .build();
                player.setMediaItem(mediaitem);
                player.prepare();
                player.setPlayWhenReady(true);
            }
        });

    }

    public void initialize_medical_bottom(Context context) {
        medical_bottom_sheet_dialog = new BottomSheetDialog(context);
        medical_bottom_sheet_layout = getLayoutInflater().inflate(R.layout.medical_bottom_sheet, null);
        medical_bottom_sheet_dialog.setContentView(medical_bottom_sheet_layout);
        medical_bottom_sheet_dialog.setCancelable(false);
        final ImageView medical_bottom_sheet_close_btn = (ImageView) medical_bottom_sheet_layout.findViewById(R.id.medical_bottom_sheet_close_btn);
        medical_bottom_sheet_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                medical_bottom_sheet_dialog.dismiss();
            }
        });
        CardView medical_small = (CardView) medical_bottom_sheet_layout.findViewById(R.id.medical_small);
        CardView medical_details_expand = (CardView) medical_bottom_sheet_layout.findViewById(R.id.medical_details_expand);
        medical_small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medical_open == 0) {
                    medical_open++;
                    medical_details_expand.setVisibility(View.VISIBLE);
                } else {
                    medical_open = 0;
                    medical_details_expand.setVisibility(View.GONE);

                }
            }
        });
    }

    public void initialize_cases_list(Context context) {

        // casess bottom sheet
        cases_bottom_sheet_dialog = new BottomSheetDialog(context);
        bottom_sheet_layout = getLayoutInflater().inflate(R.layout.cases_bottom_sheet, null);
        cases_bottom_sheet_dialog.setContentView(bottom_sheet_layout);
        cases_bottom_sheet_dialog.setCancelable(false);
        ListView cases_list_view = (ListView) bottom_sheet_layout.findViewById(R.id.cases_list_view);
        final ImageView case_bottom_sheet_close_btn = (ImageView) bottom_sheet_layout.findViewById(R.id.case_bottom_sheet_close_btn);
        case_bottom_sheet_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cases_bottom_sheet_dialog.dismiss();
            }
        });



        _login1_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);

                if (LoginActivity.uid.equals(_childKey)) {
                    list_cases.add(_childValue);
                    TextView username_txt = findViewById(R.id.username_txt);
                    username_txt.setText(_childValue.get("username").toString());
                    TextView age_txt = findViewById(R.id.age_txt);
                    age_txt.setText(_childValue.get("age").toString());
                    TextView hometown_txt = findViewById(R.id.hometown_txt);
                    hometown_txt.setText(_childValue.get("hometown").toString());
                    TextView gender_txt = findViewById(R.id.gender_txt);
                    gender_txt.setText(_childValue.get("gender").toString());
                    TextView height_txt = findViewById(R.id.height_txt);
                    height_txt.setText(_childValue.get("height").toString());
                    TextView weight_txt = findViewById(R.id.weight_txt);
                    weight_txt.setText(_childValue.get("weight").toString());
                    TextView dob_txt = findViewById(R.id.dob_txt);
                    dob_txt.setText(_childValue.get("dob").toString());
                    ImageView profile_photo = findViewById(R.id.profile_photo);
                    Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("profile_photo").toString())).into(profile_photo);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = snapshot.getKey();
                final HashMap<String, Object> _childValue = snapshot.getValue(_ind);
                if (LoginActivity.uid.equals(_childKey)) {
                    list_cases.add(_childValue);
                    TextView username_txt = findViewById(R.id.username_txt);
                    username_txt.setText(_childValue.get("username").toString());
                    TextView age_txt = findViewById(R.id.age_txt);
                    age_txt.setText(_childValue.get("age").toString());
                    TextView hometown_txt = findViewById(R.id.hometown_txt);
                    hometown_txt.setText(_childValue.get("hometown").toString());
                    TextView gender_txt = findViewById(R.id.gender_txt);
                    gender_txt.setText(_childValue.get("gender").toString());
                    TextView height_txt = findViewById(R.id.height_txt);
                    height_txt.setText(_childValue.get("height").toString());
                    TextView weight_txt = findViewById(R.id.weight_txt);
                    weight_txt.setText(_childValue.get("weight").toString());
                    TextView dob_txt = findViewById(R.id.dob_txt);
                    dob_txt.setText(_childValue.get("dob").toString());
                    ImageView profile_photo = findViewById(R.id.profile_photo);
                    Glide.with(getApplicationContext()).load(Uri.parse(_childValue.get("profile_photo").toString())).into(profile_photo);

                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        login1.addChildEventListener(_login1_child_listener);
        cases_list_view.setAdapter(new Listview2Adapter(list_cases));
        ((BaseAdapter) cases_list_view.getAdapter()).notifyDataSetChanged();
    }

    public void intialize_session() {
        session_bottom_sheet_dialog = new BottomSheetDialog(this);
        session_bottom_sheet_layout = getLayoutInflater().inflate(R.layout.session_bottom_sheet, null);
        session_bottom_sheet_dialog.setContentView(session_bottom_sheet_layout);
        session_bottom_sheet_dialog.setCancelable(false);
        final ImageView session_bottom_sheet_close_btn = (ImageView) session_bottom_sheet_layout.findViewById(R.id.session_bottom_sheet_close_btn);
        session_bottom_sheet_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session_bottom_sheet_dialog.dismiss();
            }
        });
        session_list = (ListView) findViewById(R.id.session_list);
        session_list.setDivider(null);
        _session_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                session_list_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        list_session = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                list_session.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        session_list.setAdapter(new Listview5Adapter(list_session));
                        ((BaseAdapter) session_list.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                session_list_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        list_session = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                list_session.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        session_list.setAdapter(new Listview5Adapter(list_session));
                        ((BaseAdapter) session_list.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        session_list_reference.addChildEventListener(_session_child_listener);

        playerview1 = session_bottom_sheet_layout.findViewById(R.id.player_view);
        player1 = new ExoPlayer.Builder(this).build();
        playerview1.setPlayer(player1);

        session_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                session_bottom_sheet_dialog.show();
                MediaItem mediaitem1 = new MediaItem.Builder()
                        .setUri(list_session.get(position).get("session_video").toString())
                        .build();
                player1.setMediaItem(mediaitem1);
                player1.prepare();
                player1.setPlayWhenReady(true);
                TextView session_title_bottom_sheet = session_bottom_sheet_layout.findViewById(R.id.session_title_bottom_sheet);
                session_title_bottom_sheet.setText(list_session.get(position).get("session_title").toString());
                TextView session_description = session_bottom_sheet_layout.findViewById(R.id.session_description);
                session_description.setText(list_session.get(position).get("session_description").toString());
                TextView session_number = session_bottom_sheet_layout.findViewById(R.id.session_number);
                session_number.setText(String.valueOf(position));
            }
        });

    }

    public void initizalise_lawyer(Context context) {

        lawyer_bottom_sheet_dialog = new BottomSheetDialog(context);
        lawyer_bottom_sheet_layout = getLayoutInflater().inflate(R.layout.lawyer_bottom_sheet, null);
        lawyer_bottom_sheet_dialog.setContentView(lawyer_bottom_sheet_layout);
        lawyer_bottom_sheet_dialog.setCancelable(false);
        final ImageView lawyer_bottom_sheet_close_btn = (ImageView) lawyer_bottom_sheet_layout.findViewById(R.id.lawyer_bottom_sheet_close_btn);
        lawyer_bottom_sheet_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lawyer_bottom_sheet_dialog.dismiss();
            }
        });


        listview1 = findViewById(R.id.lawyer_list);
        listview1.setDivider(null);
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lawyer_bottom_sheet_dialog.show();
                TextView username_lawyer_bottom_sheet = lawyer_bottom_sheet_layout.findViewById(R.id.username_lawyer_bottom_sheet);
                username_lawyer_bottom_sheet.setText(list_lawyer.get((int) position).get("username").toString());
                TextView specialization_bottom_sheet = lawyer_bottom_sheet_layout.findViewById(R.id.specialization_bottom_sheet);
                specialization_bottom_sheet.setText(list_lawyer.get((int) position).get("specialization").toString());
                ImageView profile_photo = lawyer_bottom_sheet_layout.findViewById(R.id.profile_photo);
                Glide.with(getApplicationContext()).load(Uri.parse(list_lawyer.get((int) position).get("profile_pic").toString())).into(profile_photo);
                TextView case_filed = lawyer_bottom_sheet_layout.findViewById(R.id.case_filed);
                case_filed.setText(list_lawyer.get((int) position).get("case_filed").toString());
                TextView case_failed = lawyer_bottom_sheet_layout.findViewById(R.id.case_failed);
                case_failed.setText(list_lawyer.get((int) position).get("case_failed").toString());
                TextView case_successfull = lawyer_bottom_sheet_layout.findViewById(R.id.case_successfull);
                case_successfull.setText(list_lawyer.get((int) position).get("case_successfull").toString());
                TextView lawyer_discription = lawyer_bottom_sheet_layout.findViewById(R.id.lawyer_discription);
                lawyer_discription.setText(list_lawyer.get((int) position).get("description").toString());
            }
        });
        _lawyer_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                lawyer_list_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        list_lawyer = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                list_lawyer.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        listview1.setAdapter(new Listview1Adapter(list_lawyer));
                        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                lawyer_list_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        list_lawyer = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                list_lawyer.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        listview1.setAdapter(new Listview1Adapter(list_lawyer));
                        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        lawyer_list_reference.addChildEventListener(_lawyer_child_listener);

        video_calling_list = (ListView) findViewById(R.id.video_calling_list);
        video_calling_list.setDivider(null);
        _video_calling_child_listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                video_calling_list_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        list_video_call = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                list_video_call.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        video_calling_list.setAdapter(new Listview6Adapter(list_video_call));
                        ((BaseAdapter) video_calling_list.getAdapter()).notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }
            @Override
            public void onChildChanged(DataSnapshot _param1, String _param2) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);
                video_calling_list_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot _dataSnapshot) {
                        list_video_call = new ArrayList<>();
                        try {
                            GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                            };
                            for (DataSnapshot _data : _dataSnapshot.getChildren()) {
                                HashMap<String, Object> _map = _data.getValue(_ind);
                                list_video_call.add(_map);
                            }
                        } catch (Exception _e) {
                            _e.printStackTrace();
                        }
                        video_calling_list.setAdapter(new Listview6Adapter(list_video_call));
                        ((BaseAdapter) video_calling_list.getAdapter()).notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError _databaseError) {
                    }
                });
            }

            @Override
            public void onChildMoved(DataSnapshot _param1, String _param2) {

            }

            @Override
            public void onChildRemoved(DataSnapshot _param1) {
                GenericTypeIndicator<HashMap<String, Object>> _ind = new GenericTypeIndicator<HashMap<String, Object>>() {
                };
                final String _childKey = _param1.getKey();
                final HashMap<String, Object> _childValue = _param1.getValue(_ind);

            }

            @Override
            public void onCancelled(DatabaseError _param1) {
                final int _errorCode = _param1.getCode();
                final String _errorMessage = _param1.getMessage();

            }
        };
        video_calling_list_reference.addChildEventListener(_video_calling_child_listener);

        video_calling_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                target_video_caller = list_video_call.get((int) position).get("uid").toString();
                video_calll();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public class Listview1Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.lawyer_list_item_layout, null);
            }


            TextView username_lawyer = _view.findViewById(R.id.username_lawyer);
            ImageView lawyer_pic = _view.findViewById(R.id.lawyer_pic);
            ImageView first_star = _view.findViewById(R.id.first_star);
            ImageView second_star = _view.findViewById(R.id.second_star);
            ImageView third_star = _view.findViewById(R.id.third_star);
            ImageView four_star = _view.findViewById(R.id.four_star);
            ImageView five_star = _view.findViewById(R.id.five_star);

            if (list_lawyer.get((int) _position).get("rating").toString().equals("0.5")) {
                first_star.setImageResource(R.drawable.baseline_star_half_24);
                second_star.setImageResource(R.drawable.baseline_star_border_24);
                third_star.setImageResource(R.drawable.baseline_star_border_24);
                four_star.setImageResource(R.drawable.baseline_star_border_24);
                five_star.setImageResource(R.drawable.baseline_star_border_24);

            } else if (list_lawyer.get((int) _position).get("rating").toString().equals("1")) {
                first_star.setImageResource(R.drawable.baseline_star_rate_24);
                second_star.setImageResource(R.drawable.baseline_star_border_24);
                third_star.setImageResource(R.drawable.baseline_star_border_24);
                four_star.setImageResource(R.drawable.baseline_star_border_24);
                five_star.setImageResource(R.drawable.baseline_star_border_24);

            } else if (list_lawyer.get((int) _position).get("rating").toString().equals("1.5")) {

                first_star.setImageResource(R.drawable.baseline_star_rate_24);
                second_star.setImageResource(R.drawable.baseline_star_half_24);
                third_star.setImageResource(R.drawable.baseline_star_border_24);
                four_star.setImageResource(R.drawable.baseline_star_border_24);
                five_star.setImageResource(R.drawable.baseline_star_border_24);


            } else if (list_lawyer.get((int) _position).get("rating").toString().equals("2")) {

                first_star.setImageResource(R.drawable.baseline_star_rate_24);
                second_star.setImageResource(R.drawable.baseline_star_rate_24);
                third_star.setImageResource(R.drawable.baseline_star_border_24);
                four_star.setImageResource(R.drawable.baseline_star_border_24);
                five_star.setImageResource(R.drawable.baseline_star_border_24);


            } else if (list_lawyer.get((int) _position).get("rating").toString().equals("2.5")) {


                first_star.setImageResource(R.drawable.baseline_star_rate_24);
                second_star.setImageResource(R.drawable.baseline_star_rate_24);
                third_star.setImageResource(R.drawable.baseline_star_half_24);
                four_star.setImageResource(R.drawable.baseline_star_border_24);
                five_star.setImageResource(R.drawable.baseline_star_border_24);


            } else if (list_lawyer.get((int) _position).get("rating").toString().equals("3")) {
                first_star.setImageResource(R.drawable.baseline_star_rate_24);
                second_star.setImageResource(R.drawable.baseline_star_rate_24);
                third_star.setImageResource(R.drawable.baseline_star_rate_24);
                four_star.setImageResource(R.drawable.baseline_star_border_24);
                five_star.setImageResource(R.drawable.baseline_star_border_24);

            } else if (list_lawyer.get((int) _position).get("rating").toString().equals("3.5")) {

                first_star.setImageResource(R.drawable.baseline_star_rate_24);
                second_star.setImageResource(R.drawable.baseline_star_rate_24);
                third_star.setImageResource(R.drawable.baseline_star_rate_24);
                four_star.setImageResource(R.drawable.baseline_star_half_24);
                five_star.setImageResource(R.drawable.baseline_star_border_24);

            } else if (list_lawyer.get((int) _position).get("rating").toString().equals("4")) {

                first_star.setImageResource(R.drawable.baseline_star_rate_24);
                second_star.setImageResource(R.drawable.baseline_star_rate_24);
                third_star.setImageResource(R.drawable.baseline_star_rate_24);
                four_star.setImageResource(R.drawable.baseline_star_rate_24);
                five_star.setImageResource(R.drawable.baseline_star_border_24);

            } else if (list_lawyer.get((int) _position).get("rating").toString().equals("4.5")) {
                first_star.setImageResource(R.drawable.baseline_star_rate_24);
                second_star.setImageResource(R.drawable.baseline_star_rate_24);
                third_star.setImageResource(R.drawable.baseline_star_rate_24);
                four_star.setImageResource(R.drawable.baseline_star_rate_24);
                five_star.setImageResource(R.drawable.baseline_star_half_24);

            } else if (list_lawyer.get((int) _position).get("rating").toString().equals("5")) {
                first_star.setImageResource(R.drawable.baseline_star_rate_24);
                second_star.setImageResource(R.drawable.baseline_star_rate_24);
                third_star.setImageResource(R.drawable.baseline_star_rate_24);
                four_star.setImageResource(R.drawable.baseline_star_rate_24);
                five_star.setImageResource(R.drawable.baseline_star_rate_24);
            }
            TextView specialization = _view.findViewById(R.id.specialization);
            Glide.with(getApplicationContext()).load(Uri.parse(list_lawyer.get((int) _position).get("profile_pic").toString())).into(lawyer_pic);
            //  lawyer_pic.setImageURI(Uri.parse(list_cases.get((int)_position).get("profile_pic").toString()));
            username_lawyer.setText(list_lawyer.get((int) _position).get("username").toString());
            specialization.setText(list_lawyer.get((int) _position).get("specialization").toString());

            return _view;
        }
    }

    public class Listview2Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Listview2Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.cases_list_item_layout, null);
            }


            final CardView cases_expand = _view.findViewById(R.id.cases_expand);
            final CardView cases_small = _view.findViewById(R.id.cases_small);

            TextView case_number = _view.findViewById(R.id.case_number);
            TextView case_date = _view.findViewById(R.id.case_date);
            TextView case_type = _view.findViewById(R.id.case_type);

            case_number.setText(list_cases.get(_position).get("case_number").toString());
            case_date.setText(list_cases.get(_position).get("case_date").toString());
            case_type.setText(list_cases.get(_position).get("case_type").toString());


            cases_small.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (open_num == 0) {
                        cases_expand.setVisibility(View.VISIBLE);
                        open_num++;
                    } else {
                        if (open_num == 1) {
                            cases_expand.setVisibility(View.GONE);
                            open_num = 0;
                        }
                    }
                }
            });

            return _view;
        }

    }


    public class Listview3Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Listview3Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.related_video_item_list, null);
            }

            TextView related_vidoes = _view.findViewById(R.id.related_videos_title);
            related_vidoes.setText(list_vedios.get(_position).get("related_videos_title").toString());
            TextView related_videos_description = _view.findViewById(R.id.related_videos_description);
            related_videos_description.setText(list_vedios.get(_position).get("related_videos_description").toString());
            ImageView related_videos_thumbnail = _view.findViewById(R.id.related_videos_thumbnail);
            Glide.with(getApplicationContext()).load(Uri.parse(list_vedios.get((int) _position).get("related_videos_thumbnail").toString())).into(related_videos_thumbnail);

            return _view;
        }


    }

    public class Listview4Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Listview4Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.ipc_list_item_layout, null);
            }

            TextView section_title = _view.findViewById(R.id.section_title);
            section_title.setText(list_ipc.get(_position).get("section_title").toString());

            TextView section_number = _view.findViewById(R.id.section_number);
            section_number.setText(list_ipc.get(_position).get("section_number").toString());

            return _view;
        }

    }



        public class Listview5Adapter extends BaseAdapter {

            ArrayList<HashMap<String, Object>> _data;

            public Listview5Adapter(ArrayList<HashMap<String, Object>> _arr) {
                _data = _arr;
            }

            @Override
            public int getCount() {
                return _data.size();
            }

            @Override
            public HashMap<String, Object> getItem(int _index) {
                return _data.get(_index);
            }

            @Override
            public long getItemId(int _index) {
                return _index;
            }

            @Override
            public View getView(final int _position, View _v, ViewGroup _container) {
                LayoutInflater _inflater = getLayoutInflater();
                View _view = _v;
                if (_view == null) {
                    _view = _inflater.inflate(R.layout.session_item_list, null);
                }

                TextView session_title = _view.findViewById(R.id.session_title);
                session_title.setText(list_session.get(_position).get("session_title").toString());
                TextView session_date = _view.findViewById(R.id.session_date);
                session_date.setText(list_session.get(_position).get("session_date").toString());
                TextView session_time = _view.findViewById(R.id.session_time);
                session_time.setText(list_session.get(_position).get("session_time").toString());
                ImageView session_thumbnail = _view.findViewById(R.id.session_thumbnail);
                Glide.with(getApplicationContext()).load(Uri.parse(list_session.get((int) _position).get("session_thumbnail").toString())).into(session_thumbnail);


                return _view;
            }

        @Deprecated
        public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
            ArrayList<Double> _result = new ArrayList<Double>();
            SparseBooleanArray _arr = _list.getCheckedItemPositions();
            for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
                if (_arr.valueAt(_iIdx))
                    _result.add((double) _arr.keyAt(_iIdx));
            }
            return _result;
        }
    }

    public class Listview6Adapter extends BaseAdapter {

        ArrayList<HashMap<String, Object>> _data;

        public Listview6Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int _position, View _v, ViewGroup _container) {
            LayoutInflater _inflater = getLayoutInflater();
            View _view = _v;
            if (_view == null) {
                _view = _inflater.inflate(R.layout.video_calling_list_item_layout, null);
            }


            TextView username_lawyer = _view.findViewById(R.id.username_lawyer);
            ImageView lawyer_pic = _view.findViewById(R.id.lawyer_pic);

            TextView specialization = _view.findViewById(R.id.specialization);
          //  Glide.with(getApplicationContext()).load(Uri.parse(list_lawyer.get((int) _position).get("profile_pic").toString())).into(lawyer_pic);
            //  lawyer_pic.setImageURI(Uri.parse(list_cases.get((int)_position).get("profile_pic").toString()));
            username_lawyer.setText(list_video_call.get((int) _position).get("username").toString());
          //  specialization.setText(list_lawyer.get((int) _position).get("specialization").toString());

            return _view;
        }
    }

    public void video_calll() {
        mainRepository = MainRepository.getInstance();
        mainRepository.login(
                LoginActivity.uid, getApplicationContext(), () -> {
                    startActivity(new Intent(Family_Activity.this, CallActivity.class));
                });
    }


}
