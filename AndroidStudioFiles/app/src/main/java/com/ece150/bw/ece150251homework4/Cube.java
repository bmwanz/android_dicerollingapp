package com.ece150.bw.ece150251homework4;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Cube {

    private final FloatBuffer vertexBuffer, textureCBuffer;
    private final ShortBuffer drawOrderBuffer;
    private final int mProgram;
    private int positionHandle, textureCHandle, MVPmHandle, textureHandle, uniTextureHandle;

    private static final String vertexShaderStr =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec4 a_color;" +
                    "attribute vec2 tCoordinate;" +
                    "varying vec2 v_tCoordinate;" +
                    "varying vec4 v_Color;" +
                    "void main() {" +
                    "  gl_Position = vPosition * uMVPMatrix;" +
                    "	v_tCoordinate = tCoordinate;" +
                    "	v_Color = a_color;" +
                    "}";

    final String fragmShader =
            "precision mediump float;" +
                    "varying vec4 v_Color;" +
                    "varying vec2 v_tCoordinate;" +
                    "uniform sampler2D s_texture;" +
                    "void main() {" +
                    "	vec4 texColor = texture2D(s_texture, v_tCoordinate); " +
                    "  gl_FragColor = v_Color*0.5 + texColor*0.5;" +
                    "}";

    private static final float cubeVertex[] = {
            0.5f,  0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,  //rear

            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f,  0.5f, 0.5f,   //left

            -0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, 0.5f,    //bottom

            0.5f,  0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,   //right

            -0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, 0.5f,
            0.5f,  0.5f, 0.5f,
            0.5f,  0.5f, -0.5f,   //top

            -0.5f,  0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f,  0.5f, 0.5f,   //front
    };

    final short drawOrder[] = {
            /*
            0, 1, 3, 3, 1, 2, // Front
            0, 1, 4, 4, 5, 1, // Bottom
            1, 2, 5, 5, 6, 2, // Right
            2, 3, 6, 6, 7, 3, // Top
            3, 7, 4, 4, 3, 0, // Left
            4, 5, 7, 7, 6, 5, // Rear
            */

            0, 1, 2, 0, 2, 3,       //rear
            4, 5, 6, 4, 6, 7,       //left
            8, 9, 10, 8, 10, 11,    //bottom
            12, 13, 14, 12, 14, 15, //right
            16, 17, 18, 16, 18, 19, //top
            20, 21, 22, 20, 22, 23  //front
    };

    private static final int coordPvertex = 3; //coordinates per vertex
    private static final int vertSizeB = coordPvertex * 4;  //vertex size in bytes

    private static final int coordPtexture = 2;
    private final int texSizeB = coordPtexture * 4; // 4 bytes per texture coord
    static float textureCoord[] = {
            1f, 0.5f,
            1f, 0.667f,
            0f, 0.667f,
            0f, 0.5f,       //rear

            0f, 0.167f,
            0f, 0.333f,
            1f, 0.333f,
            1f, 0.167f,     //left

            1f, 0.833f,
            1f, 1f,
            0f, 1f,
            0f, 0.833f,     //bottom

            1f, 0.667f,
            1f, 0.833f,
            0f, 0.833f,
            0f, 0.667f,     //right

            1.0f, 0.0f,
            1.0f, 0.167f,
            0.0f, 0.167f,
            0.0f, 0.0f,     //top

            0f, 0.333f,
            0f, 0.5f,
            1f, 0.5f,
            1f, 0.333f      //front
    };

    private static int loadShader(int type, String shaderC) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderC);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public static int loadTexture(Context context, int tID) {
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        if (textureHandle[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;

            //get picture
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), tID, options);

            //bind to open gl
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            //filter
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            //load picture into bind texture
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            //recycle; data loaded into open gl
            bitmap.recycle();
        }

        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    public Cube(Context context) {
        //initialize buffer for cubeVertex
        ByteBuffer bbuffer = ByteBuffer.allocateDirect(cubeVertex.length * 4);
        bbuffer.order(ByteOrder.nativeOrder());

        //initialize buffer for drawOrder
        ByteBuffer doBuffer = ByteBuffer.allocateDirect(drawOrder.length * 2);
        doBuffer.order(ByteOrder.nativeOrder());
        drawOrderBuffer = doBuffer.asShortBuffer();
        drawOrderBuffer.put(drawOrder);
        drawOrderBuffer.position(0);

        vertexBuffer = bbuffer.asFloatBuffer();
        vertexBuffer.put(cubeVertex);
        vertexBuffer.position(0);

        //initialize buffer for textures
        ByteBuffer textureBuffer = ByteBuffer.allocateDirect(textureCoord.length * 4);
        textureBuffer.order(ByteOrder.nativeOrder());
        textureCBuffer = textureBuffer.asFloatBuffer();
        textureCBuffer.put(textureCoord);
        textureCBuffer.position(0);

        //load texture and create program for shaders
        textureHandle = loadTexture(context, R.drawable.dice);
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderStr));
        GLES20.glAttachShader(mProgram, loadShader(GLES20.GL_FRAGMENT_SHADER, fragmShader));
        GLES20.glLinkProgram(mProgram);

        /*
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        MVPmHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        uniTextureHandle = GLES20.glGetUniformLocation(mProgram, "s_texture");
        */
    }

    public void draw(float[] mvpMatrix) {

        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        //get cube coordinates
        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glVertexAttribPointer(positionHandle, coordPvertex, GLES20.GL_FLOAT, false, vertSizeB, vertexBuffer);

        //get texture coordinates
        textureCHandle = GLES20.glGetAttribLocation(mProgram, "tCoordinate");
        GLES20.glEnableVertexAttribArray(textureCHandle);
        GLES20.glVertexAttribPointer(textureCHandle, coordPtexture, GLES20.GL_FLOAT, false, texSizeB, textureCBuffer);

        // Apply projection and view transformation
        MVPmHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(MVPmHandle, 1, false, mvpMatrix, 0);

        // initialize texture
        uniTextureHandle = GLES20.glGetUniformLocation(mProgram, "s_texture");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        // Bind the texture to handler to 0 for use and apply
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);
        GLES20.glUniform1i(uniTextureHandle, 0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawOrderBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }
}
