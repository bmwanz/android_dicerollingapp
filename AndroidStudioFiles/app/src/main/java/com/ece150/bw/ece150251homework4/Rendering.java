package com.ece150.bw.ece150251homework4;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;

public class Rendering implements GLSurfaceView.Renderer{

    private Context tcontext;
    private Cube die_cube;

    public volatile int rollout;
    public volatile boolean beginning = true;   //first time running

    private final float[] projectionMatrix = new float[16];
    private final float[] MVPMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] rotateMatrix = new float[16];

    private final float[] finalMatrix = new float[16];
    float[] tMatrix0 = new float[16];
    float[] tMatrix1 = new float[16];

    public Rendering(Context context){
        tcontext = context;
    } //Matrix.setLookAtM(viewMatrix, 0, 2f, 3f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        //initialize to black
        GLES20.glClearColor(0,0,0,1);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        //GLES20.glClearDepthf(1);
        //GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        //GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        die_cube = new Cube(tcontext);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0,0,width,height);
        float ratio = (float) width/height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        //Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); // | GLES20.GL_DEPTH_BUFFER_BIT)
        //set camera view
        Matrix.setLookAtM(viewMatrix, 0, 2f, 3f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        Matrix.setRotateM(rotateMatrix, 0, 1, 0, -1, 0);
        Matrix.multiplyMM(tMatrix0, 0, rotateMatrix, 0, MVPMatrix, 0);
        Matrix.setRotateM(rotateMatrix, 0, 1, 1, 0, 0);
        Matrix.multiplyMM(tMatrix1, 0, rotateMatrix, 0, tMatrix0, 0);

        //change die face
        if(rollout == 1){
            Matrix.setRotateM(rotateMatrix, 0, 90, 1, 0, 0);
            Matrix.multiplyMM(finalMatrix, 0, rotateMatrix, 0, tMatrix1, 0);
        }
        else if (rollout == 2){
            Matrix.setRotateM(rotateMatrix, 0, 90, 0, 1, 0);
            Matrix.multiplyMM(finalMatrix, 0, rotateMatrix, 0, tMatrix1, 0);
        }
        else if(rollout==3) {
            Matrix.setRotateM(rotateMatrix, 0, 180, 1, 0, 0);
            Matrix.multiplyMM(finalMatrix, 0, rotateMatrix, 0, tMatrix1, 0);
        }
        else if(rollout==4) {
            Matrix.setRotateM(rotateMatrix, 0, 0, 0, 1, 0);
            Matrix.multiplyMM(finalMatrix, 0, rotateMatrix, 0, tMatrix1, 0);
        }
        else if(rollout==5) {
            Matrix.setRotateM(rotateMatrix, 0, -90, 0, 1, 0);
            Matrix.multiplyMM(finalMatrix, 0, rotateMatrix, 0, tMatrix1, 0);
        }
        else if(rollout==6) {
            Matrix.setRotateM(rotateMatrix, 0, -90, 1, 0, 0);
            Matrix.multiplyMM(finalMatrix, 0, rotateMatrix, 0, tMatrix1, 0);
        }

        die_cube.draw(finalMatrix);

        if(beginning == true){
            die_cube.draw(tMatrix1);
        }
    }
}
