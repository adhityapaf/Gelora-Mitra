package com.gelora.mitra.model;

import java.util.ArrayList;

public class WaktuLapangan {
    private ArrayList<String> jamArray = new ArrayList<>();

    public WaktuLapangan(ArrayList<String> jamArray) {
        this.jamArray = jamArray;
    }

    public ArrayList<String> getJamArray() {
        return jamArray;
    }

    public void setJamArray(ArrayList<String> jamArray) {
        this.jamArray = jamArray;
    }
}
