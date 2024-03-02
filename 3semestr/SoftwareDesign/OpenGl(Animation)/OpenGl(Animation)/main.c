#include <GLFW/glfw3.h>

void drawbus(float px);
void drawwall();
void drawsun();
void drawsun(void) {
    glEnable(GL_POINT_SMOOTH);
    glPointSize(40);
    glColor3f(0.95, 0.9, 0.274);
    glBegin(GL_POINTS);
    glVertex3f(-0.75, 0.75, 0);
    glEnd();
}
void draw(float x)
{
    glClear(GL_COLOR_BUFFER_BIT);
    /// <summary>
    /// Green
    /// </summary>
    glBegin(GL_TRIANGLES);
    glColor3f(0.01, 0.45, 0.19);
    glVertex3f(-1.0, -1.0, 0);
    glVertex3f(-1.0, -0.6875, 0);
    glVertex3f(1.0, -0.6875, 0);
    glEnd();
    glBegin(GL_TRIANGLES);
    glColor3f(0.01, 0.45, 0.19);
    glVertex3f(-1, -1, 0);
    glVertex3f(1, -0.6875, 0);
    glVertex3f(1, -1, 0);
    glEnd();
    /// <summary>
    ///Road
    /// </summary>
    glBegin(GL_TRIANGLES);
    glColor3f(0.08, 0.115, 0.094);
    glVertex3f(-1, -0.6875, 0);
    glVertex3f(-1, -0.1875, 0);
    glVertex3f(1, -0.1875, 0);
    glEnd();
    glBegin(GL_TRIANGLES);
    glColor3f(0.08, 0.115, 0.094);
    glVertex3f(-1, -0.6875, 0);
    glVertex3f(1, -0.6875, 0);
    glVertex3f(1, -0.1875, 0);
    glEnd();
    /// <summary>
    /// Green
    /// </summary>
    glBegin(GL_TRIANGLES);
    glColor3f(0.01, 0.45, 0.19);
    glVertex3f(-1, -0.1875, 0);
    glVertex3f(1, 0, 0);
    glVertex3f(-1, 0, 0);
    glEnd();
    glBegin(GL_TRIANGLES);
    glColor3f(0.01, 0.45, 0.19);
    glVertex3f(-1, -0.1875, 0);
    glVertex3f(1, -0.1875, 0);
    glVertex3f(1, 0, 0);
    glEnd();

    //Rasmetka
    float startx = -1;
    float endx = -0.875;
    glLineWidth(8);
    glBegin(GL_LINES);
    glColor3f(0.87, 0.61, 0.086);
    for (int i = 0;i < 8;i++)
    {
        glVertex3f(startx, -0.4375, 0);
        glVertex3f(endx, -0.4375, 0);
        startx += 0.25;
        endx += 0.25;
    }
    glEnd();
    ////
    drawwall();
   
    drawsun();

    drawbus(x);
   
}
void drawwall()
{
    glColor3f(0.807, 0.541, 0.082);
    glBegin(GL_TRIANGLES);
    glVertex3f(-1, 0, 0);
    glVertex3f(-1, 0.5, 0);
    glVertex3f(1, 0.5, 0);
    glVertex3f(-1, 0, 0);
    glVertex3f(1, 0, 0);
    glVertex3f(1, 0.5, 0);
    glEnd();
    GLfloat diffy = 0.0625;
    GLfloat liney = 0.5;
    glColor3f(0.043, 0.043, 0.043);
    for (int i = 0;i < 8;i++)
    {
        liney = liney - diffy;
        glLineWidth(3);
        glBegin(GL_LINES);
        glVertex2f(-1, liney);
        glVertex2f(1, liney);
        glEnd;
    }
    GLfloat diffx = 0.125;
    GLfloat linex = -1;
    for (int i = 0;i < 15;i++)
    {
        linex = linex + diffx;
        glLineWidth(3);
        glBegin(GL_LINES);
        glVertex2f(linex, 0.5);
        glVertex2f(linex, 0);
        glEnd();
    }
}
void drawbus(float px)
{
    GLfloat xs = px / 320;
    glColor3f(0.75, 0.07, 0.207);
    glBegin(GL_TRIANGLES);
    glVertex3f(-0.375+xs, -0.4375, 0);
    glVertex3f(-0.375+xs, 0.03125, 0);
    glVertex3f(0.48125+xs, 0.03125, 0);
    glVertex3f(-0.375+xs, -0.4375, 0);
    glVertex3f(0.48125+xs, 0.03125, 0);
    glVertex3f(0.48125+xs, -0.4375, 0);
    //
    //
    glVertex3f(-0.375+xs, 0.03125, 0);
    glVertex3f(-0.28125+xs, 0.09375, 0);
    glVertex3f(0.575+xs, 0.09375, 0);
    glVertex3f(-0.375+xs, 0.03125, 0);
    glVertex3f(0.575+xs, 0.09375, 0);
    glVertex3f(0.48125+xs, 0.03125, 0);
    //
    //
    glVertex3f(0.48125+xs, -0.4375, 0);
    glVertex3f(0.575+xs, -0.375, 0);
    glVertex3f(0.575+xs, 0.09375, 0);
    glVertex3f(0.48125+xs, -0.4375, 0);
    glVertex3f(0.48125+xs, 0.03125, 0);
    glVertex3f(0.575+xs, 0.09375, 0);
    //
    //
    glColor3f(0.137, 0.2, 0.737);
    glVertex3f(0.48125+xs, -0.125, 0);
    glVertex3f(0.575+xs, -0.0625, 0);
    glVertex3f(0.575+xs, 0.09375, 0);
    glVertex3f(0.48125+xs, -0.125, 0);
    glVertex3f(0.48125+xs, 0.03125, 0);
    glVertex3f(0.575+xs, 0.09375, 0);
    glEnd();

    //Circles
    glEnable(GL_POINT_SMOOTH);
    glPointSize(40);
    glColor3f(0.227, 0.227, 0.227);
    glBegin(GL_POINTS);
    glVertex2f(-0.1875+xs, -0.4375);
    glVertex2f(0.29375+xs, -0.4375);
    glEnd();
    glDisable(GL_POINT_SMOOTH);
    //Windows
    glColor3f(0.137, 0.2, 0.737);
    glBegin(GL_TRIANGLES);
    glVertex3f(0.48125+xs, 0.03125, 0);
    glVertex3f(-0.375+xs, 0.03125, 0);
    glVertex3f(-0.375+xs, -0.125, 0);
    glVertex3f(-0.375+xs, -0.125, 0);
    glVertex3f(0.48125+xs, 0.03125, 0);
    glVertex3f(0.48125+xs, -0.125, 0);
    glEnd();

    glColor3f(0.87, 0.61, 0.086);

    glBegin(GL_TRIANGLES);
    glVertex3f(0.496875+xs, -0.34375, 0);
    glVertex3f(0.496875+xs, -0.3125, 0);
    glVertex3f(0.528125+xs, -0.296875, 0);

    glVertex3f(0.496875+xs, -0.34375, 0);
    glVertex3f(0.528125+xs, -0.328125, 0);
    glVertex3f(0.528125+xs, -0.296875, 0);

    glVertex3f(0.5400+xs, -0.328, 0);
    glVertex3f(0.5400+xs, -0.296, 0);
    glVertex3f(0.573+xs, -0.281, 0);

    glVertex3f(0.5400+xs, -0.328, 0);
    glVertex3f(0.573+xs, -0.31, 0);
    glVertex3f(0.573+xs, -0.28, 0);
    glEnd();

}

int main(void)
{
    GLFWwindow* window;

    /* Initialize the library */
    if (!glfwInit())
        return -1;

    /* Create a windowed mode window and its OpenGL context */
    window = glfwCreateWindow(640, 640, "Hello World", NULL, NULL);
    if (!window)
    {
        glfwTerminate();
        return -1;
    }

    /* Make the window's context current */
    glfwMakeContextCurrent(window);

    glClearColor(0.333, 0.753, 0.965, 1.0);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);




    /* Loop until the user closes the window */
    float x = -480;
    while (!glfwWindowShouldClose(window))
    {
        /* Render here */
        //glClear(GL_COLOR_BUFFER_BIT);
        draw(x);
        /* Swap front and back buffers */
        glfwSwapBuffers(window);

        /* Poll for and process events */
        glfwPollEvents();
        x += 2;
        if (x >= 480)
        {
            x = -480;
        }
    }

    glfwTerminate();
    return 0;
}
