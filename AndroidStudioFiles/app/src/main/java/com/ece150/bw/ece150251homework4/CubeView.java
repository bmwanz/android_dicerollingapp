package com.ece150.bw.ece150251homework4;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class CubeView extends GLSurfaceView {

    public Rendering cubeRender;

    public CubeView(Context context){
        super(context);
        //opengl es context
        setEGLContextClientVersion(2);

        cubeRender = new Rendering(context);
        setRenderer(cubeRender);
    }

    public void rollDie(){
        cubeRender.beginning = false;
        cubeRender.rollout = 1 + (int)(Math.random()*6);
    }
}
