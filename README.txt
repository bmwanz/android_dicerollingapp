Brian Wan

I have my code split into 4 different classes: MainActivity, CubeView, Rendering, and Cube. The main activity takes care
of the button press and the phone shake to roll the die. Because I implemented my own GLSurfaceView, I also created the
button itself and positioined it within the onCreate. "CubeView" is my implementation of of the GLSurfaceView. It simply
renders the die and has the function that would roll the die. "Rendering" is where I create the cube and display it on
the screen. I make the background black and tell OpenGL to draw ever face of the cube, except the back face. This class
also handles the cube rotations depending on which die face is on top. Lastly, "Cube" is where I make the cube. It holds
the information for each vertex of the cube, order to draw the vertexes, and texture for the cube. I decided to use the
texture template provided and wrapped the die faces around the cube only once.

Note: To position the "Rolling" button, I used setX(#) in MainActivity. Depending on the screen size and resolution, it may or may not
	  appear on the screen. The # value needs to be changed if it does not show on screen.

Link to the video: 
	http://1drv.ms/1vftfwh

Sources:
https://github.com/googleglass/gdk-apidemo-sample/blob/master/app/src/main/java/com/google/android/glass/sample/apidemo/opengl/Cube.java
http://www.360doc.com/content/11/0411/13/4873696_108810181.shtml
http://developer.android.com/training/graphics/opengl/environment.html
http://developer.android.com/reference/android/opengl/GLSurfaceView.html
http://developer.android.com/training/graphics/opengl/index.html
http://www.learnopengles.com/android-lesson-four-introducing-basic-texturing/
http://stackoverflow.com/questions/15566851/opengl-es-2-translation-after-rotation
http://www.clingmarks.com/how-to-detect-shake-motion-on-android-phone/25
http://stackoverflow.com/questions/25911584/colouring-and-texturing-3d-shapes-with-opengl-es
http://stackoverflow.com/questions/6535648/how-can-i-dynamically-set-the-position-of-view-in-android