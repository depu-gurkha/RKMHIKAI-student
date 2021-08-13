package online.rkmhikai.ui.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import online.rkmhikai.R;


public class

DescriptionFragment extends Fragment {



    public DescriptionFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_description, container, false);
        WebView wview =view.findViewById(R.id.textContent);
        String text;
        text = "<html><body>";
        text+= "<h3>Computer</h3>\n" +
                "<p align=\"justify\">Upskill yourself in the world of technology. Be industry-ready with our basic and advanced courses in computer applications..\n" +
                "<br><br>\n" +
                "<h3>Art</h3>\n" +
                "<p align=\"justify\">Explore your talent in Art and Craft. We welcome you to the worderful world of colours through our online class. Learn to draw, paint and make things creatively\n" +
                "<br><br>\n" +
                "<h3>Spoken English</h3>\n" +
                "<p align=\"justify\">Do you want to speak English fluently or communicate effectively in different situations? It's your opportunity to enhance your skills in English with our Spoken English Course.\n" +
                "<br><br>\n" ;
        text+= "</p></body></html>";
        wview.loadData(text, "text/html", "utf-8");
        return view;

    }
}