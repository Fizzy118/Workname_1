package com.Piotrk_Kielak.Workname_1;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Fragment layoutu zawierający informacje na temat aplikacji oraz posiadający przycisk umożliwiający
 * kontakt z twórcą.
 */
// TODO: Skończyc opis aplikacji w fragment_frag_info
public class FragInfo extends Fragment {

    private Button button;
    public FragInfo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Funkcja kopiująca adres email twórcy do schowka.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //funkcja po nacisnieciu przyciusku kopiuje adres email autora do schowka.
        View v = inflater.inflate(R.layout.fragment_frag_info, container, false);
        button = v.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard =(ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("skopiowano", "piotr.kielak2.stud@pw.edu.pl");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Skopiowano Email do schowka.",Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}

