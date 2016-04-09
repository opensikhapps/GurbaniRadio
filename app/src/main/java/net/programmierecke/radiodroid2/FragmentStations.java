package net.programmierecke.radiodroid2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class FragmentStations extends FragmentBase {
    private ProgressDialog itsProgressLoading;
    private ItemAdapterStation itsArrayAdapter = null;
    private ListView lv;
    private String url;
    private DataRadioStation[] data = new DataRadioStation[0];

    public FragmentStations() {
    }

    @Override
    protected void InitArrayAdapter(){
        itsArrayAdapter = new ItemAdapterStation(getActivity(), R.layout.list_item_station);
    }

    void ClickOnItem(DataRadioStation theStation) {
        MainActivity a = (MainActivity)getActivity();
        PlayerService thisService = new PlayerService();
        thisService.unbindSafely( a, a.getSvc() );

        Intent anIntent = new Intent(getActivity().getBaseContext(), RadioDroidStationDetail.class);
        anIntent.putExtra("stationid", theStation.ID);
        startActivity(anIntent);
    }

    @Override
    protected void RefreshListGui(){
        data = DataRadioStation.DecodeJson(getUrlResult());
        itsArrayAdapter.clear();
        for (DataRadioStation aStation : data) {
            itsArrayAdapter.add(aStation);
        }
        if (lv != null) {
            lv.invalidate();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stations, container, false);

        lv = (ListView) view.findViewById(R.id.listViewStations);
        lv.setAdapter(itsArrayAdapter);
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object anObject = parent.getItemAtPosition(position);
                if (anObject instanceof DataRadioStation) {
                    ClickOnItem((DataRadioStation) anObject);
                }
            }
        });

        RefreshListGui();

        return view;
    }
}