package com.wallpapers.abdev.util;

import android.graphics.Color;

import java.util.Random;


public class ColorUtil {
    public static int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(50, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
