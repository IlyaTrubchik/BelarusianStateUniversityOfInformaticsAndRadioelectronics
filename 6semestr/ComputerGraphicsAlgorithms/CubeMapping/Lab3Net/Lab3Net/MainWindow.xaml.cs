using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Numerics;
using System.IO;
using Lab3Net;
using System.Windows.Controls;

namespace Lab1Wpf
{
    /// <summary>
    /// Логика взаимодействия для MainWindow.xaml
    /// </summary>
    /// 
   
    public partial class MainWindow : Window
    {
        static object lockObject = new object();
        float yPrevMouse = 0;
        float xPrevMouse = 0;
        int imageWidth = 0;
        int imageHeight = 0;
        Camera camera;
        Model model;
        WriteableBitmap writeableBitmap;
        Vector4 currentScale = new Vector4();
        Vector4[] skyboxProjection = new Vector4[8];
        /*      Cube
      1)(-1,-1,1)
      2)(1,-1,1)
      3)(-1,-1,-1);
      4)(1,-1,-1)
      5)(-1,1,1)
      6)(1,1,1)
      7)(-1,1,-1)
      8)(1,1,-1) */
        Vector4[] skyboxVertexes = new Vector4[]
        {
            new Vector4(-5,-5,5,1),
            new Vector4(5,-5,5,1),
            new Vector4(-5,-5,-5,1),
            new Vector4(5,-5,-5,1),
            new Vector4(-5,5,5,1),
            new Vector4(5,5,5,1),
            new Vector4(-5,5,-5,1),
            new Vector4(5,5,-5,1)
        };

      /*  
         Vector4[] skyboxVertexes = new Vector4[]
        {
            new Vector4(9,-6,11,0),
            new Vector4(11,-6,11,0),
            new Vector4(9,-6,9,0),
            new Vector4(11,-6,9,0),
            new Vector4(9,-4,11,0),
            new Vector4(11,-4,11,0),
            new Vector4(9,-4,9,0),
            new Vector4(11,-4,11,0)
        };
        */
        /*
        Triangles
        (1,2,6)
(1,6,5)
(2,6,4)
(4,6,8)
(1,3,5)
(5,3,7)
(3,7,8)
(3,8,4)
(5,7,6)
(7,6,8)
(1,2,3)
(2,3,4)
*/
        Vector3[] skyboxTriangles = new Vector3[]
        {
            new Vector3(0,1,5),
            new Vector3(0,5,4),
            new Vector3(1,5,3),
            new Vector3(3,5,7),
            new Vector3(0,2,4),
            new Vector3(4,2,6),
            new Vector3(2,6,7),
            new Vector3(2,7,3),
            new Vector3(4,6,5),
            new Vector3(6,5,7),
            new Vector3(0,1,2),
            new Vector3(1,2,3)
        };
        String[] skyboxpathes = new string[]
        {
            "D:\\3dmodels\\Skybox\\back.jpg",
            "D:\\3dmodels\\Skybox\\front.jpg",
            "D:\\3dmodels\\Skybox\\right.jpg",
            "D:\\3dmodels\\Skybox\\left.jpg",
            "D:\\3dmodels\\Skybox\\top.jpg",
            "D:\\3dmodels\\Skybox\\bottom.jpg",
        };
        JPGImage backImage;
        JPGImage frontImage;
        JPGImage leftImage;
        JPGImage rightImage;
        JPGImage topImage;
        JPGImage bottomImage;
        JPGImage diffuseMap;
        JPGImage normalMap;
        JPGImage specularMap;


        bool isFromFile = true;

        bool isPressed = false;
        int pixelSize;
        int stride;
        byte[] pixels;
        Vector3 lightPos;
        float[] zBuffer;
        bool wireFrameMode;
        
        float specularStrength = 0.8f;
        bool followMode = false;


        TgaImage tgaDiffuseMap;


        float ambientStrength = 0.1f;
        Vector3 specColor = new Vector3(255, 255, 255);

        int lightMode = 0;// LightMode - 0 -Alllights, 1-diffuse+specular,2-specular,3-specular+ambient,4-ambient,5-diffuse
        
        
        Vector3 lightColor = new Vector3(255, 255, 255);
        Vector3 objColor = new Vector3(255, 255, 255);
        Vector4[] skyboxScreen = new Vector4[8];
        public MainWindow()
        {
            InitializeComponent();
            Vector3 target = new Vector3(0,0,-1);
            Vector3 position = new Vector3(5f,5f,5f);
            camera = new Camera(target, position);
            currentScale.X = 1;
            currentScale.Y = 1;
            currentScale.Z = 1;
            MatrixTransformations.getProjectionMatrix((float)drawingImage.Width, (float)drawingImage.Height, 1, 1000);
            MatrixTransformations.getViewPortMatrix((float)drawingImage.Width, (float)drawingImage.Height, 0, 0);
            lightPos = camera.getPosition();
            skyboxVertexes.CopyTo(skyboxScreen,0);
            MatrixTransformations.toProjection(skyboxScreen);

            MatrixTransformations.projectionMatrix[0, 0] = 1/(float)((Width / Height) * (float)Math.Tan(1.5f / 2));//Math.PI/4.5);
            MatrixTransformations.projectionMatrix[1, 1] = 1/(float)Math.Tan(1.5f / 2);//(Math.PI/4.5);
        
            skyboxScreen.CopyTo(skyboxProjection, 0);
            for (int i = 0; i < skyboxScreen.Length; i++)
            {
                
                skyboxScreen[i].Z = skyboxScreen[i].Z / Math.Abs(skyboxScreen[i].W);
                skyboxScreen[i].X = skyboxScreen[i].X / Math.Abs(skyboxScreen[i].W);
                skyboxScreen[i].Y = skyboxScreen[i].Y / Math.Abs(skyboxScreen[i].W);
                if (Math.Abs(skyboxScreen[i].X) <= Math.Abs(skyboxScreen[i].W) && Math.Abs(skyboxScreen[i].X) >= 0
                        && Math.Abs(skyboxScreen[i].Y) <= Math.Abs(skyboxScreen[i].W) && Math.Abs(skyboxScreen[i].Y) >= 0
                    && skyboxScreen[i].Z >= 0 && skyboxScreen[i].Z <= skyboxScreen[i].W
               )
                {
                    skyboxScreen[i].W = 1;
                }
                else
                {
                    skyboxScreen[i].W = 0;
                }
            }
            MatrixTransformations.toScreen(skyboxScreen);

            wireFrameMode = false;
        }
        void testTBN()
        {
            Vector3 pos1 = new Vector3(-1,1,0);
            Vector3 pos2 = new Vector3(-1, -1, 0);
            Vector3 pos3 = new Vector3(1, -1, 0);

            Vector3 uv1 = new Vector3(0, 1,0);
            Vector3 uv2 = new Vector3(0, 0,0);
            Vector3 uv3 = new Vector3(1, 0,0);

            Vector3 N = new Vector3(0, 0, 1);

            Vector3 edge1 = pos2 - pos1;
            Vector3 edge2 = pos3 - pos1;

            Vector3 deltaUv1 = uv2 - uv1;
            Vector3 deltaUv2 = uv3 - uv1;
            TBN tbn = new TBN();
            tbn.setNormal(N);
     //       tbn.countTB(edge1,edge2,deltaUv1,deltaUv2);
        }

        void render()
        {
            imageWidth = (int)drawingImage.Width;
            imageHeight = (int)drawingImage.Height;
            if(followMode )
            {
                lightPos = camera.getPosition();
            }
            Vector4[] points = new Vector4[model.GetPointCount()];
            Vector4[] skyboxPoints = new Vector4[8];
         
            skyboxScreen.CopyTo(skyboxPoints, 0);
     
            Vector3[] pointsN = new Vector3[model.PointsN.Length];
             MatrixTransformations.getCameraMatrix(camera.getPosition(), camera.getUp(), camera.getTarget());
            //MatrixTransformations.GetCameraMatrix(camera.getPosition(), new Vector3(0,1,0), camera.getTarget());
            model.GetPoints().CopyTo(points,0);
            model.PointsN.CopyTo(pointsN,0);
            //Преобразование координат
            MatrixTransformations.toWorld(currentScale, points,pointsN);

            points.CopyTo(MatrixTransformations.worldModel,0);
   
            

            MatrixTransformations.toCamera(points);
            MatrixTransformations.toProjection(points);
            points.CopyTo(MatrixTransformations.projectionPoints, 0);
            MatrixTransformations.cut(points);
            MatrixTransformations.toScreen(points);

 
            
           
            // Объявление массива для пикселей
            pixelSize = 4; // Каждый пиксель занимает 4 байта в формате Bgra32
            stride = imageWidth * pixelSize; // Длина одной строки пикселей
            int bufferSize = imageHeight * stride; // Общий размер буфера пикселей
            pixels = new byte[bufferSize];

            //List<int>[] poly = checkPolygons(points).ToArray();

            List<int>[] poly = model.getPolygons();

            if (!wireFrameMode)
            {
                zBuffer = Enumerable.Repeat(float.MaxValue, imageWidth * imageHeight).ToArray();

                Parallel.For(0, skyboxTriangles.Length, (i) => { 
                //for (int i = 0; i < skyboxTriangles.Length; i++)
               // {
                    Vector3 face = skyboxTriangles[i];
                    if (skyboxPoints[(int)face.X].W != 0 && skyboxPoints[(int)face.Y].W != 0 && skyboxPoints[(int)face.Z].W != 0) 
                        drawSkyBox(skyboxPoints, (int)face.X, (int)face.Y, (int)face.Z);

               
                 });
                //}
                Parallel.For(0, poly.Length, (i) =>
                //for (int i = 0; i < poly.Length; i++)
                {
                    List<int> face = poly[i];
                    if (model.PoligonsN[i].Count != 0 && model.PoligonsT[i].Count != 0)
                    {
                        for (int j = 2; j < face.Count; j++)
                        {
                            Vector3 n1 = pointsN[model.PoligonsN[i][0]];
                            Vector3 n2 = pointsN[model.PoligonsN[i][j - 1]];
                            Vector3 n3 = pointsN[model.PoligonsN[i][j]];

                            Vector3 textureCord1 = model.PointsT[model.PoligonsT[i][0]];
                            Vector3 textureCord2 = model.PointsT[model.PoligonsT[i][j - 1]];
                            Vector3 textureCord3 = model.PointsT[model.PoligonsT[i][j]];


                            Vector4 edge1 = (MatrixTransformations.worldModel[face[j - 1]] - MatrixTransformations.worldModel[face[0]]);
                            Vector4 edge2 = (MatrixTransformations.worldModel[face[j]] - MatrixTransformations.worldModel[face[0]]);

                            //textureCord1.Y = 1 - textureCord1.Y;
                            // textureCord2.Y = 1 - textureCord2.Y;
                            // textureCord3.Y = 1 - textureCord3.Y;

                            Vector3 UV1 = (textureCord2 - textureCord1);
                            Vector3 UV2 = (textureCord3 - textureCord1);

                            //textureCord1.Y = 1 - textureCord1.Y;
                            //textureCord2.Y = 1 - textureCord2.Y;
                            //textureCord3.Y = 1 - textureCord3.Y;


                            textureCord1.X = textureCord1.X * (diffuseMap.width);
                            textureCord1.Y = textureCord1.Y * (diffuseMap.height);
                            textureCord2.X = textureCord2.X * (diffuseMap.width);
                            textureCord2.Y = textureCord2.Y * (diffuseMap.height);
                            textureCord3.X = textureCord3.X * (diffuseMap.width);
                            textureCord3.Y = textureCord3.Y * (diffuseMap.height);


                            TBN tbn = new TBN();

                            Vector3 normal = Vector3.Cross(new Vector3(edge1.X, edge1.Y, edge1.Z), new Vector3(edge2.X, edge2.Y, edge2.Z));

                            tbn.setNormal(normal);
                            //tbn.setNormal(normalFromFile);
                            tbn.countTB(new Vector3(edge1.X, edge1.Y, edge1.Z), new Vector3(edge2.X, edge2.Y, edge2.Z), UV1, UV2);
                            tbn.formTBN();
                            tbn.reort();

                            if (points[face[0]].W == 0 || points[face[j - 1]].W == 0 || points[face[j]].W == 0)
                                continue;

                            //  Vector3 n1 = new Vector3(Model.pointsNormals[face[0]].X, Model.pointsNormals[face[0]].Y, Model.pointsNormals[face[0]].Z);
                            // Vector3 n2 = new Vector3(Model.pointsNormals[face[j - 1]].X, Model.pointsNormals[face[j - 1]].Y, Model.pointsNormals[face[j - 1]].Z);
                            // Vector3 n3 = new Vector3(Model.pointsNormals[face[j]].X, Model.pointsNormals[face[j]].Y, Model.pointsNormals[face[j]].Z);
                            
                            newDrawTriangle(points, face[0], face[j - 1], face[j], tbn, n1, n2, n3, textureCord1, textureCord2, textureCord3);
                        }
                    }
                });
            }
            else
            {
                drawLines(points,poly);
            }
      
            //запись пикселей в битмап
            writeableBitmap = new WriteableBitmap((int)drawingImage.Width, (int)drawingImage.Height, 96, 96, PixelFormats.Bgr32, null);
            writeableBitmap.WritePixels(new Int32Rect(0, 0, (int)drawingImage.Width, (int)drawingImage.Height), pixels, stride, 0);
            drawingImage.Source = writeableBitmap;
        }
        
        private List<List<int>> checkPolygons( Vector4[] points)
        {
            List<List<int>> result = new List<List<int>>();
            List<int>[] allPolygons = model.getPolygons();
            for(int  i = 0;i<allPolygons.Length;i++)
            {
                List<int> face = allPolygons[i];
                bool flag = true;
                for(int j = 0;j<face.Count;j++)
                {
                    if (points[face[j]].W == 0)
                    {
                        flag = false;
                        break;
                    }
                }
                if(flag)
                {
                    result.Add(face);
                }
            }
            return result;
        }
        private Vector4 getPointNormal(Vector4 normalA, Vector4 normalB, Vector4 normalC, Vector4 a, Vector4 b, Vector4 c, Vector4 p)
        {
            // u = ((B - P) × (C - P)) · normalA
            // v = ((C - P) × (A - P)) · normalB
            //w = ((A - P) × (B - P)) · normalC
            float u = Vector4.Dot(Vector4.Multiply(Vector4.Subtract(b, p), Vector4.Subtract(c, p)), normalA);
            float v = Vector4.Dot(Vector4.Multiply(Vector4.Subtract(c, p), Vector4.Subtract(a, p)), normalB);
            float w = Vector4.Dot(Vector4.Multiply(Vector4.Subtract(a, p), Vector4.Subtract(b, p)), normalC);
            //normalP = (u * normalA + v * normalB + w * normalC).Normalized()
            Vector4 normal = Vector4.Add(Vector4.Add(Vector4.Multiply(u, normalA), Vector4.Multiply(v, normalB)), Vector4.Multiply(w, normalC)).normalize();
            return normal;
        }
      
        private float getIntensity(Vector3 normal,Vector3 fragPos)
        { 
            Vector3 viewDir = Vector3.Subtract(camera.getPosition(), fragPos);
            double a = Vector3.Dot(Vector3.Normalize(viewDir), normal);
            Vector3 lightDir = Vector3.Normalize(lightPos - fragPos);
            float intensity = 0;
            if (a > 0) 
            {
                intensity = (Math.Max(Vector3.Dot(normal, lightDir),0) );
            }
            return intensity;
        }

        private Vector3 specularLightColor(Vector3 testVector)
        {
            testVector = Vector3.Normalize(testVector);
            float X = Math.Abs(testVector.X);
            float Y = Math.Abs(testVector.Y);
            float Z = Math.Abs(testVector.Z);

            float maxComponent = Math.Max(X, Math.Max(Y, Z));
            float u = 0;
            float v = 0;
            byte[] colorFromFile = new byte[3];

            //if (maxComponent == X && worldPoint.X>0)
            if (maxComponent == X && testVector.X > 0)
            {
                u = ((testVector.Z / X) + 1) / 2;
                v = ((testVector.Y / X) + 1) / 2;
                //u = ((worldPoint.Z / X) + 1) / 2;
                //v = ((worldPoint.Y / X) + 1) / 2;
                int iu = Math.Max(0, Math.Min((int)(u * frontImage.width), frontImage.width - 1));
                int iv = Math.Max(0, Math.Min((int)(v * frontImage.height), frontImage.height - 1));
                colorFromFile = rightImage.GetPixelColor(iu, iv);
            }
            //else if(maxComponent == X && worldPoint.X < 0)
            else if (maxComponent == X && testVector.X < 0)
            {
                u = ((-testVector.Z / X) + 1) / 2;
                v = ((testVector.Y / X) + 1) / 2;
                // u = ((-worldPoint.Z / X) + 1) / 2;
                //  v = ((worldPoint.Y / X) + 1) / 2;
                int iu = Math.Max(0, Math.Min((int)(u * frontImage.width), frontImage.width - 1));
                int iv = Math.Max(0, Math.Min((int)(v * frontImage.height), frontImage.height - 1));
                colorFromFile = leftImage.GetPixelColor(iu, iv);
            }
            //else if (maxComponent == Y && worldPoint.Y > 0)
            else if (maxComponent == Y && testVector.Y > 0)
            {
                u = ((testVector.X / Y) + 1) / 2;
                v = ((testVector.Z / Y) + 1) / 2;
                // u = ((worldPoint.X / Y) + 1) / 2;
                // v = ((worldPoint.Z / Y) + 1) / 2;
                int iu = Math.Max(0, Math.Min((int)(u * frontImage.width), frontImage.width - 1));
                int iv = Math.Max(0, Math.Min((int)(v * frontImage.height), frontImage.height - 1));

                colorFromFile = bottomImage.GetPixelColor(iu, iv);
            }
            //else if (maxComponent == Y && worldPoint.Y < 0)
            else if (maxComponent == Y && testVector.Y < 0)
            {
                u = ((testVector.X / Y) + 1) / 2;
                v = ((-testVector.Z / Y) + 1) / 2;
                //u = ((worldPoint.X / Y) + 1) / 2;
                // v = ((-worldPoint.Z / Y) + 1) / 2;
                int iu = Math.Max(0, Math.Min((int)(u * frontImage.width), frontImage.width - 1));
                int iv = Math.Max(0, Math.Min((int)(v * frontImage.height), frontImage.height - 1));
                colorFromFile = topImage.GetPixelColor(iu, iv);
            }
            //else if (maxComponent == Z && worldPoint.Z > 0)
            else if (maxComponent == Z && testVector.Z > 0)
            {
                u = ((-testVector.X / Z) + 1) / 2;
                v = ((testVector.Y / Z) + 1) / 2;
                //u = ((-worldPoint.X / Z) + 1) / 2;
                // v = ((worldPoint.Y / Z) + 1) / 2;
                int iu = Math.Max(0, Math.Min((int)(u * frontImage.width), frontImage.width - 1));
                int iv = Math.Max(0, Math.Min((int)(v * frontImage.height), frontImage.height - 1));
                colorFromFile = backImage.GetPixelColor(iu, iv);
            }
            //else if (maxComponent == Z && worldPoint.Z < 0)
            else if (maxComponent == Z && testVector.Z < 0)
            {
                u = ((testVector.X / Z) + 1) / 2;
                v = ((testVector.Y / Z) + 1) / 2;
                // u = ((worldPoint.X / Z) + 1) / 2;
                //v = ((worldPoint.Y / Z) + 1) / 2;
                int iu = Math.Max(0, Math.Min((int)(u * frontImage.width), frontImage.width - 1));
                int iv = Math.Max(0, Math.Min((int)(v * frontImage.height), frontImage.height - 1));
                colorFromFile = frontImage.GetPixelColor(iu, iv);

            }
            if (colorFromFile[0] == 0 && colorFromFile[1] == 0 && colorFromFile[2] == 0) Console.WriteLine();
            return new Vector3(colorFromFile[0], colorFromFile[1], colorFromFile[2]);
        }
        private float getSpecularLight(Vector3 normal,Vector3 fragPos)
        {
            Vector3 viewDir = Vector3.Normalize(Vector3.Subtract(camera.getPosition(),fragPos ));
            Vector3 lightDir = Vector3.Normalize(lightPos-fragPos);
            Vector3 reflectDir = Reflect(-lightDir, normal);

            float spec = (float)Math.Pow(Math.Max(Vector3.Dot(viewDir, reflectDir), 0.0f), 40);
            return spec;
        }

        private Vector3 Reflect(Vector3 vector, Vector3 normal)
        {
            // Вычисляем отраженный вектор

            float dotProduct = Vector3.Dot(vector, normal);
            Vector3 reflectedVector = vector - 2 * dotProduct * normal;

            return reflectedVector;
        }

        private void drawPoint(int x ,int y,byte colorA,byte colorB,byte colorC)
        {
            if (x <= imageWidth && y <= imageHeight && x > 0 && y > 0)
            {
                int pixelOffset = ((int)y-1) * stride + ((int)x-1) * pixelSize;
                pixels[pixelOffset + 0] = colorA; // Синий
                pixels[pixelOffset + 1] = colorB; // Зеленый
                pixels[pixelOffset + 2] = colorC; // Красный
                pixels[pixelOffset + 3] = 255; // Альфа-канал
            }
        }
        private void drawSkyBox(Vector4[] points,int i0,int i1,int i2)
        {
            Vector4[] screenTriangle = new Vector4[3];
            screenTriangle[0] = points[i0];
            screenTriangle[0].X = (float)Math.Round(screenTriangle[0].X);
            screenTriangle[0].Y = (float)Math.Round(screenTriangle[0].Y);
            screenTriangle[0].Z = skyboxProjection[i0].Z;



            screenTriangle[1] = points[i1];
            screenTriangle[1].X = (float)Math.Round(screenTriangle[1].X);
            screenTriangle[1].Y = (float)Math.Round(screenTriangle[1].Y);
            screenTriangle[1].Z = skyboxProjection[i1].Z;


            screenTriangle[2] = points[i2];
            screenTriangle[2].X = (float)Math.Round(screenTriangle[2].X);
            screenTriangle[2].Y = (float)Math.Round(screenTriangle[2].Y);
            screenTriangle[2].Z = skyboxProjection[i2].Z;


            Vector4[] worldTriangle = new Vector4[3];
            worldTriangle[0] = skyboxVertexes[i0];
            worldTriangle[1] = skyboxVertexes[i1];
            worldTriangle[2] = skyboxVertexes[i2];


            //  t0 minY , next t1, next t2
            if (screenTriangle[0].Y == screenTriangle[1].Y && screenTriangle[0].Y == screenTriangle[2].Y)
            {
                return;
            }
            if (screenTriangle[0].Y > screenTriangle[1].Y)
            {
                (screenTriangle[0], screenTriangle[1]) = (screenTriangle[1], screenTriangle[0]);
                (worldTriangle[0], worldTriangle[1]) = (worldTriangle[1], worldTriangle[0]);
 
            }
            if (screenTriangle[0].Y > screenTriangle[2].Y)
            {
                (screenTriangle[0], screenTriangle[2]) = (screenTriangle[2], screenTriangle[0]);
                (worldTriangle[0], worldTriangle[2]) = (worldTriangle[2], worldTriangle[0]);
 
            }
            if (screenTriangle[1].Y > screenTriangle[2].Y)
            {
                (screenTriangle[1], screenTriangle[2]) = (screenTriangle[2], screenTriangle[1]);
                (worldTriangle[1], worldTriangle[2]) = (worldTriangle[2], worldTriangle[1]);

            }

            int minY = Math.Max((int)Math.Ceiling(screenTriangle[0].Y), 0);
            int maxY = Math.Min((int)Math.Ceiling(screenTriangle[2].Y), imageHeight - 1);


            // Скорость изменения координат мировых координат между вершинами треугольника относитель изменения высоты в пикселях
            Vector4[] speedWorld = new Vector4[3];
            speedWorld[0] = (worldTriangle[1] - worldTriangle[0]) / (screenTriangle[1].Y - screenTriangle[0].Y);// между 1-0
            speedWorld[1] = (worldTriangle[2] - worldTriangle[0]) / (screenTriangle[2].Y - screenTriangle[0].Y);// между 2-0
            speedWorld[2] = (worldTriangle[2] - worldTriangle[1]) / (screenTriangle[2].Y - screenTriangle[1].Y);// между 2-1

          

            // Скорость изменения координат экрана между вершинами треугольника относитель изменения высоты в пикселях
            Vector4[] speedScreen = new Vector4[3];
            speedScreen[0] = (screenTriangle[1] - screenTriangle[0]) / (screenTriangle[1].Y - screenTriangle[0].Y);// между 1-0
            speedScreen[1] = (screenTriangle[2] - screenTriangle[0]) / (screenTriangle[2].Y - screenTriangle[0].Y);// между 2-0
            speedScreen[2] = (screenTriangle[2] - screenTriangle[1]) / (screenTriangle[2].Y - screenTriangle[1].Y);// между 2-1

      
            // Надо идти сначала от 0-1, потом идти от 1-2, то есть сначала идем по линиям 02,01; потом по линиям 02,12.

            for (int y = minY; y < maxY; y++)
            {
                Vector4 leftScreenPoint;
                Vector4 leftWorldPoint;
         
                Vector2 leftTexturePoint;
                float leftDepth;

                Vector4 rightScreenPoint;
                Vector4 rightWorldPoint;
                Vector3 rightNormalPoint;
                Vector2 rightTexturePoint;
                float rightDepth;

                if (y < screenTriangle[1].Y)
                {
                    leftScreenPoint = screenTriangle[0] + (y - screenTriangle[0].Y) * speedScreen[0];
                    leftWorldPoint = worldTriangle[0] + (y - screenTriangle[0].Y) * speedWorld[0];
               
        


                    rightScreenPoint = screenTriangle[0] + (y - screenTriangle[0].Y) * speedScreen[1];
                    rightWorldPoint = worldTriangle[0] + (y - screenTriangle[0].Y) * speedWorld[1];
  
                }
                else
                {
                    leftScreenPoint = screenTriangle[0] + (y - screenTriangle[0].Y) * speedScreen[1];
                    leftWorldPoint = worldTriangle[0] + (y - screenTriangle[0].Y) * speedWorld[1];



                    rightScreenPoint = screenTriangle[1] + (y - screenTriangle[1].Y) * speedScreen[2];
                    rightWorldPoint = worldTriangle[1] + (y - screenTriangle[1].Y) * speedWorld[2];

                }

                if (leftScreenPoint.X > rightScreenPoint.X)
                {
                    (leftScreenPoint, rightScreenPoint) = (rightScreenPoint, leftScreenPoint);
                    (leftWorldPoint, rightWorldPoint) = (rightWorldPoint, leftWorldPoint);

                }

                int minX = Math.Max((int)Math.Ceiling(leftScreenPoint.X), 0);
                int maxX = Math.Min((int)Math.Ceiling(rightScreenPoint.X), imageWidth - 1);
                if (maxX == imageWidth - 1)
                {
                    maxX = imageWidth - 1;
                }
  
                Vector4 screenSpeed = (rightScreenPoint - leftScreenPoint) / (rightScreenPoint.X - leftScreenPoint.X);
                Vector4 worldSpeed = (rightWorldPoint - leftWorldPoint) / (rightScreenPoint.X - leftScreenPoint.X);

                for (int x = minX; x < maxX; x++)
                {
                    Vector4 screenPoint = leftScreenPoint + (x - leftScreenPoint.X) * screenSpeed;
                    Vector4 worldPoint = leftWorldPoint + (x - leftScreenPoint.X) * worldSpeed;
           

                    //if (zBuffer[x + (y) * imageWidth] > screenPoint.Z && screenPoint.Z>0)
                    //{

                        
                        byte[] colorFromFile =  new byte[] { 255,255,255};
                        float u = 0;
                        float v = 0;
                       // MatrixTransformations.multiply(MatrixTransformations.cameraMatrix, ref worldPoint);
                        Vector3 testVector = Vector3.Normalize(Vector3.Subtract(new Vector3(0,0,0), new Vector3(worldPoint.X, worldPoint.Y, worldPoint.Z)));
                        //Vector4 testVector = new Vector4(a.X, a.Y, a.Z, 1);
                        //MatrixTransformations.multiply3(MatrixTransformations.cameraMatrix, ref testVector);
                        //testVector = Vector3.Normalize(testVector);
                        //float X = Math.Abs(worldPoint.X);
                        //float Y = Math.Abs(worldPoint.Y);
                        //float Z = Math.Abs(worldPoint.Z);
                       // zBuffer[x + (y) * imageWidth] = screenPoint.Z;
                        float X = Math.Abs(testVector.X);
                        float Y = Math.Abs(testVector.Y);
                        float Z = Math.Abs(testVector.Z);
                        float maxComponent = Math.Max(X, Math.Max(Y, Z));

                    //colorFromFile = diffuseMap.GetPixelColor((int)Math.Round(texturePoint.X), (int)Math.Round(texturePoint.Y));
                       //if (maxComponent == X && worldPoint.X>0)
                       if (maxComponent == X && testVector.X > 0)
                       {
                            u = ((testVector.Z/X)+1)/2;
                            v = ((testVector.Y/X)+1)/2;
                            //u = ((worldPoint.Z / X) + 1) / 2;
                            //v = ((worldPoint.Y / X) + 1) / 2;
                            int iu = Math.Max(0, Math.Min((int)(u*frontImage.width), frontImage.width - 1));
                            int iv = Math.Max(0, Math.Min((int)(v*frontImage.height), frontImage.height - 1));
                            colorFromFile = rightImage.GetPixelColor(iu, iv);
                        }
                       //else if(maxComponent == X && worldPoint.X < 0)
                        else if (maxComponent == X && testVector.X < 0)
                        {
                            u = ((-testVector.Z / X) + 1) / 2;
                            v = ((testVector.Y / X) + 1) / 2;
                           // u = ((-worldPoint.Z / X) + 1) / 2;
                          //  v = ((worldPoint.Y / X) + 1) / 2;
                            int iu = Math.Max(0, Math.Min((int)(u * frontImage.width), frontImage.width - 1));
                            int iv = Math.Max(0, Math.Min((int)(v * frontImage.height), frontImage.height - 1));
                            colorFromFile = leftImage.GetPixelColor(iu, iv);
                        }
                        //else if (maxComponent == Y && worldPoint.Y > 0)
                        else if (maxComponent == Y && testVector.Y > 0)
                        {
                            u = ((testVector.X / Y) + 1) / 2;
                            v = ((testVector.Z / Y) + 1) / 2;
                           // u = ((worldPoint.X / Y) + 1) / 2;
                           // v = ((worldPoint.Z / Y) + 1) / 2;
                            int iu = Math.Max(0, Math.Min((int)(u * frontImage.width), frontImage.width - 1));
                            int iv = Math.Max(0, Math.Min((int)(v * frontImage.height), frontImage.height - 1));
   
                            colorFromFile = bottomImage.GetPixelColor(iu, iv);
                        }
                        //else if (maxComponent == Y && worldPoint.Y < 0)
                        else if (maxComponent == Y && testVector.Y < 0)
                        {
                            u = ((testVector.X / Y) + 1) / 2;
                            v = ((-testVector.Z / Y) + 1) / 2;
                            //u = ((worldPoint.X / Y) + 1) / 2;
                           // v = ((-worldPoint.Z / Y) + 1) / 2;
                            int iu = Math.Max(0, Math.Min((int)(u * frontImage.width), frontImage.width - 1));
                            int iv = Math.Max(0, Math.Min((int)(v * frontImage.height), frontImage.height - 1));
                            colorFromFile = topImage.GetPixelColor(iu, iv);
                        }
                        //else if (maxComponent == Z && worldPoint.Z > 0)
                        else if (maxComponent == Z && testVector.Z > 0)
                        { 
                            u = ((-testVector.X / Z) + 1) / 2;
                            v = ((testVector.Y / Z) + 1) / 2;
                            //u = ((-worldPoint.X / Z) + 1) / 2;
                           // v = ((worldPoint.Y / Z) + 1) / 2;
                            int iu = Math.Max(0, Math.Min((int)(u * frontImage.width), frontImage.width - 1));
                            int iv = Math.Max(0, Math.Min((int)(v * frontImage.height), frontImage.height - 1));
                            colorFromFile =backImage.GetPixelColor(iu, iv);
                        }
                        //else if (maxComponent == Z && worldPoint.Z < 0)
                        else if (maxComponent == Z && testVector.Z < 0)
                        {
                            u = ((testVector.X /Z) + 1) / 2;
                            v = ((testVector.Y / Z) + 1) / 2;
                           // u = ((worldPoint.X / Z) + 1) / 2;
                            //v = ((worldPoint.Y / Z) + 1) / 2;
                            int iu = Math.Max(0, Math.Min((int)(u * frontImage.width), frontImage.width - 1));
                            int iv = Math.Max(0, Math.Min((int)(v * frontImage.height), frontImage.height - 1));
                            colorFromFile =frontImage.GetPixelColor(iu,iv);

                        }
                        drawPoint(x, y, Math.Min(colorFromFile[0],(byte)255), Math.Min(colorFromFile[1], (byte)255), Math.Min(colorFromFile[2], (byte)255));

                  // }

                }
            }
        }
        private void newDrawTriangle(Vector4[] points, int i0, int i1, int i2, TBN tbn,Vector3 n1,Vector3 n2,Vector3 n3,Vector3 t1, Vector3 t2,Vector3 t3)
        {
            Vector4[] screenTriangle = new Vector4[3];
            screenTriangle[0] = points[i0];
          //  screenTriangle[0].X =(float) Math.Round(screenTriangle[0].X);
          //  screenTriangle[0].Y =(float) Math.Round(screenTriangle[0].Y);
            screenTriangle[0].Z = MatrixTransformations.projectionPoints[i0].Z;

            float[] depthTriangle = new float[3];
            depthTriangle[0] = 1/MatrixTransformations.projectionPoints[i0].Z;
            depthTriangle[1] = 1 / MatrixTransformations.projectionPoints[i1].Z;
            depthTriangle[2] = 1 / MatrixTransformations.projectionPoints[i2].Z;

            Vector2[] textureTriangle = new Vector2[3];
            textureTriangle[0] = new Vector2(t1.X, t1.Y) ;
            textureTriangle[1] = new Vector2(t2.X, t2.Y);
            textureTriangle[2] = new Vector2(t3.X, t3.Y);

            screenTriangle[1] = points[i1];
           // screenTriangle[1].X = (float)Math.Round(screenTriangle[1].X);
           // screenTriangle[1].Y = (float)Math.Round(screenTriangle[1].Y);
            screenTriangle[1].Z = MatrixTransformations.projectionPoints[i1].Z;

          
            screenTriangle[2] = points[i2];
            //screenTriangle[2].X = (float)Math.Round(screenTriangle[2].X);
            //screenTriangle[2].Y = (float)Math.Round(screenTriangle[2].Y);
            screenTriangle[2].Z = MatrixTransformations.projectionPoints[i2].Z;


            Vector4[] worldTriangle = new Vector4[3];
            worldTriangle[0] = MatrixTransformations.worldModel[i0];
            worldTriangle[1] = MatrixTransformations.worldModel[i1];
            worldTriangle[2] = MatrixTransformations.worldModel[i2];

            Vector3[] normalTriangle = new Vector3[3];
            normalTriangle[0] = n1;
            normalTriangle[1] = n2;
            normalTriangle[2] = n3;
           //  t0 minY , next t1, next t2
            if (screenTriangle[0].Y == screenTriangle[1].Y && screenTriangle[0].Y == screenTriangle[2].Y)
            {
                return;
            }
            if (screenTriangle[0].Y > screenTriangle[1].Y)
            {
                (screenTriangle[0], screenTriangle[1]) = (screenTriangle[1], screenTriangle[0]);
                (worldTriangle[0], worldTriangle[1]) = (worldTriangle[1], worldTriangle[0]);
                (normalTriangle[0], normalTriangle[1]) = (normalTriangle[1], normalTriangle[0]);
                (textureTriangle[0], textureTriangle[1]) = (textureTriangle[1], textureTriangle[0]);
                (depthTriangle[0], depthTriangle[1]) = (depthTriangle[1], depthTriangle[0]);
            }
            if (screenTriangle[0].Y > screenTriangle[2].Y)
            {
                (screenTriangle[0], screenTriangle[2]) = (screenTriangle[2], screenTriangle[0]);
                (worldTriangle[0], worldTriangle[2]) = (worldTriangle[2], worldTriangle[0]);
                (normalTriangle[0], normalTriangle[2]) = (normalTriangle[2], normalTriangle[0]);
                (textureTriangle[0], textureTriangle[2]) = (textureTriangle[2], textureTriangle[0]);
                (depthTriangle[0], depthTriangle[2]) = (depthTriangle[2], depthTriangle[0]);
            }
            if (screenTriangle[1].Y > screenTriangle[2].Y)
            {
                (screenTriangle[1], screenTriangle[2]) = (screenTriangle[2], screenTriangle[1]);
                (worldTriangle[1], worldTriangle[2]) = (worldTriangle[2], worldTriangle[1]);
                (normalTriangle[1], normalTriangle[2]) = (normalTriangle[2], normalTriangle[1]);
                (textureTriangle[1], textureTriangle[2]) = (textureTriangle[2], textureTriangle[1]);
                (depthTriangle[1], depthTriangle[2]) = (depthTriangle[2], depthTriangle[1]);
            }
            //float backFaceCulling = (worldTriangle[2] - worldTriangle[1]).X * (worldTriangle[2] - worldTriangle[0]).Y - (worldTriangle[2] - worldTriangle[1]).Y * (worldTriangle[2] - worldTriangle[0]).X;

            //if (backFaceCulling < 0) return;

            int minY = Math.Max((int)Math.Ceiling(screenTriangle[0].Y), 0);
            int maxY = Math.Min((int)Math.Ceiling(screenTriangle[2].Y), imageHeight-1);


            // Скорость изменения координат мировых координат между вершинами треугольника относитель изменения высоты в пикселях
           // Vector4[] speedWorld = new Vector4[3];
           // speedWorld[0] = (worldTriangle[1] - worldTriangle[0]) / (screenTriangle[1].Y - screenTriangle[0].Y);// между 1-0
           // speedWorld[1] = (worldTriangle[2] - worldTriangle[0]) / (screenTriangle[2].Y - screenTriangle[0].Y);// между 2-0
           // speedWorld[2] = (worldTriangle[2] - worldTriangle[1]) / (screenTriangle[2].Y - screenTriangle[1].Y);// между 2-1
            
            // С коррекцией
            Vector4[] speedWorld = new Vector4[3];
            speedWorld[0] = (worldTriangle[1] / screenTriangle[1].Z - worldTriangle[0] / screenTriangle[0].Z) / (screenTriangle[1].Y - screenTriangle[0].Y);// между 1-0
            speedWorld[1] = (worldTriangle[2] / screenTriangle[2].Z - worldTriangle[0] / screenTriangle[0].Z) / (screenTriangle[2].Y - screenTriangle[0].Y);// между 2-0
            speedWorld[2] = (worldTriangle[2] / screenTriangle[2].Z - worldTriangle[1] / screenTriangle[1].Z) / (screenTriangle[2].Y - screenTriangle[1].Y);// между 2-1

            // Скорость изменения координат нормалей между вершинами треугольника относитель изменения высоты в пикселях
            // Vector3[] speedNormal = new Vector3[3];
            // speedNormal[0] = (normalTriangle[1] - normalTriangle[0]) / (screenTriangle[1].Y - screenTriangle[0].Y);// между 1-0
            // speedNormal[1] = (normalTriangle[2] - normalTriangle[0]) / (screenTriangle[2].Y - screenTriangle[0].Y);// между 2-0
            //speedNormal[2] = (normalTriangle[2] - normalTriangle[1]) / (screenTriangle[2].Y - screenTriangle[1].Y);// между 2-1

            //C кореекцией
            Vector3[] speedNormal = new Vector3[3];
            speedNormal[0] = (normalTriangle[1] / screenTriangle[1].Z - normalTriangle[0] / screenTriangle[0].Z) / (screenTriangle[1].Y - screenTriangle[0].Y);// между 1-0
            speedNormal[1] = (normalTriangle[2] / screenTriangle[2].Z - normalTriangle[0] / screenTriangle[0].Z) / (screenTriangle[2].Y - screenTriangle[0].Y);// между 2-0
            speedNormal[2] = (normalTriangle[2] / screenTriangle[2].Z - normalTriangle[1] / screenTriangle[1].Z) / (screenTriangle[2].Y - screenTriangle[1].Y);// между 2-1

            // Скорость изменения координат экрана между вершинами треугольника относитель изменения высоты в пикселях
            Vector4[] speedScreen = new Vector4[3];
            speedScreen[0] = (screenTriangle[1] - screenTriangle[0]) / (screenTriangle[1].Y - screenTriangle[0].Y);// между 1-0
            speedScreen[1] = (screenTriangle[2] - screenTriangle[0]) / (screenTriangle[2].Y - screenTriangle[0].Y);// между 2-0
            speedScreen[2] = (screenTriangle[2] - screenTriangle[1]) / (screenTriangle[2].Y - screenTriangle[1].Y);// между 2-1

            // Скорость изменения текстурных корд между вершинами относительно изменения высоты в пикселях
            Vector2[] speedTexture = new Vector2[3];
            speedTexture[0] = (textureTriangle[1] / screenTriangle[1].Z - textureTriangle[0] / screenTriangle[0].Z) / (screenTriangle[1].Y - screenTriangle[0].Y);// между 1-0
            speedTexture[1] = (textureTriangle[2] / screenTriangle[2].Z - textureTriangle[0] / screenTriangle[0].Z) / (screenTriangle[2].Y - screenTriangle[0].Y);// между 2-0
            speedTexture[2] = (textureTriangle[2] / screenTriangle[2].Z - textureTriangle[1] / screenTriangle[1].Z) / (screenTriangle[2].Y - screenTriangle[1].Y);// между 2-1

            //speedTexture[0] = (textureTriangle[1]  - textureTriangle[0] ) / (screenTriangle[1].Y - screenTriangle[0].Y);// между 1-0
            //speedTexture[1] = (textureTriangle[2]  - textureTriangle[0] ) / (screenTriangle[2].Y - screenTriangle[0].Y);// между 2-0
            //speedTexture[2] = (textureTriangle[2]  - textureTriangle[1] ) / (screenTriangle[2].Y - screenTriangle[1].Y);// между 2-1



            float[] speedDepth = new float[3];
            speedDepth[0] = (depthTriangle[1] - depthTriangle[0]) / (screenTriangle[1].Y - screenTriangle[0].Y);//между 1-0
            speedDepth[1] = (depthTriangle[2] - depthTriangle[0]) / (screenTriangle[2].Y - screenTriangle[0].Y);
            speedDepth[2] = (depthTriangle[2] - depthTriangle[1]) / (screenTriangle[2].Y - screenTriangle[1].Y);

            // Надо идти сначала от 0-1, потом идти от 1-2, то есть сначала идем по линиям 02,01; потом по линиям 02,12.

            for (int y = minY; y < maxY; y++)
            {
                Vector4 leftScreenPoint;
                Vector4 leftWorldPoint;
                Vector3 leftNormalPoint;
                Vector2 leftTexturePoint;
                float leftDepth;

                Vector4 rightScreenPoint;
                Vector4 rightWorldPoint;
                Vector3 rightNormalPoint;
                Vector2 rightTexturePoint;
                float rightDepth;

                if (y < screenTriangle[1].Y)
                {
                    leftScreenPoint = screenTriangle[0] + (y - screenTriangle[0].Y) * speedScreen[0];
                    
                    //leftWorldPoint = worldTriangle[0] + (y - screenTriangle[0].Y) * speedWorld[0];
                    leftWorldPoint = worldTriangle[0] / screenTriangle[0].Z + (y - screenTriangle[0].Y) * speedWorld[0];

                    // leftNormalPoint = normalTriangle[0] + (y - screenTriangle[0].Y) * speedNormal[0];
                    leftNormalPoint = normalTriangle[0] / screenTriangle[0].Z + (y - screenTriangle[0].Y) * speedNormal[0];
                    
                    leftTexturePoint = textureTriangle[0] / screenTriangle[0].Z + (y - screenTriangle[0].Y) * speedTexture[0];
                    //leftTexturePoint = textureTriangle[0] + (y - screenTriangle[0].Y) * speedTexture[0];
                    
                    leftDepth = depthTriangle[0] + (y - screenTriangle[0].Y) * speedDepth[0];


                    rightScreenPoint = screenTriangle[0] + (y - screenTriangle[0].Y) * speedScreen[1];

                    //rightWorldPoint = worldTriangle[0] + (y - screenTriangle[0].Y) * speedWorld[1];
                    rightWorldPoint = worldTriangle[0] / screenTriangle[0].Z + (y - screenTriangle[0].Y) * speedWorld[1];

                    //rightNormalPoint = normalTriangle[0] + (y - screenTriangle[0].Y) * speedNormal[1];
                    rightNormalPoint = normalTriangle[0] / screenTriangle[0].Z + (y - screenTriangle[0].Y) * speedNormal[1];
                   
                    rightTexturePoint = textureTriangle[0] / screenTriangle[0].Z + (y - screenTriangle[0].Y) * speedTexture[1];
                    //rightTexturePoint = textureTriangle[0] + (y - screenTriangle[0].Y) * speedTexture[1];

                    rightDepth = depthTriangle[0] + (y - screenTriangle[0].Y) * speedDepth[1];
                }
                else
                {
                    leftScreenPoint = screenTriangle[0] + (y - screenTriangle[0].Y) * speedScreen[1];
                    leftWorldPoint = worldTriangle[0] / screenTriangle[0].Z + (y - screenTriangle[0].Y) * speedWorld[1];
                    leftNormalPoint = normalTriangle[0] / screenTriangle[0].Z + (y - screenTriangle[0].Y) * speedNormal[1];
                    leftTexturePoint = textureTriangle[0] / screenTriangle[0].Z + (y - screenTriangle[0].Y) * speedTexture[1];
                    leftDepth = depthTriangle[0] + (y - screenTriangle[0].Y) * speedDepth[1];
                    rightScreenPoint = screenTriangle[1] + (y - screenTriangle[1].Y) * speedScreen[2];
                    rightWorldPoint = worldTriangle[1] / screenTriangle[1].Z + (y - screenTriangle[1].Y) * speedWorld[2];
                    rightNormalPoint = normalTriangle[1] / screenTriangle[1].Z + (y - screenTriangle[1].Y) * speedNormal[2];                  
                    rightTexturePoint = textureTriangle[1] / screenTriangle[1].Z + (y - screenTriangle[1].Y) * speedTexture[2];
                    rightDepth = depthTriangle[1] + (y - screenTriangle[1].Y) * speedDepth[2];
                }
                
                if(leftScreenPoint.X>rightScreenPoint.X)
                {
                    (leftScreenPoint, rightScreenPoint) = (rightScreenPoint, leftScreenPoint);
                    (leftWorldPoint, rightWorldPoint) = (rightWorldPoint, leftWorldPoint);
                    (leftNormalPoint, rightNormalPoint) = (rightNormalPoint, leftNormalPoint);
                    (leftTexturePoint, rightTexturePoint) = (rightTexturePoint, leftTexturePoint);
                    (leftDepth, rightDepth) = (rightDepth, leftDepth);
                }

                int minX = Math.Max((int)Math.Ceiling(leftScreenPoint.X), 0);
                int maxX = Math.Min((int)Math.Ceiling(rightScreenPoint.X), imageWidth-1);
                if(maxX == imageWidth-1)
                {
                    maxX = imageWidth - 1;
                }
                Vector3 normalSpeed = (rightNormalPoint - leftNormalPoint) / (rightScreenPoint.X - leftScreenPoint.X);
                Vector4 screenSpeed = (rightScreenPoint - leftScreenPoint) / (rightScreenPoint.X - leftScreenPoint.X);
                Vector4 worldSpeed = (rightWorldPoint - leftWorldPoint) / (rightScreenPoint.X - leftScreenPoint.X);
                Vector2 textureSpeed = (rightTexturePoint - leftTexturePoint) / (rightScreenPoint.X - leftScreenPoint.X);
                float depthSpeed = (rightDepth - leftDepth) / (rightScreenPoint.X - leftScreenPoint.X);

                for (int x = minX; x < maxX; x++)
                {
                    Vector4 screenPoint = leftScreenPoint + (x - leftScreenPoint.X) * screenSpeed;
                    Vector4 worldPoint = leftWorldPoint + (x - leftScreenPoint.X) * worldSpeed;
                    Vector3 normalPoint = leftNormalPoint + (x - leftScreenPoint.X) * normalSpeed;

                   if (zBuffer[x + (y) * imageWidth] > screenPoint.Z)
                   {
                        Vector2 texturePoint = leftTexturePoint + (x - leftScreenPoint.X) * textureSpeed;
                        float depth = leftDepth + (x - leftScreenPoint.X) * depthSpeed;
                        texturePoint = texturePoint / depth;
                        normalPoint = normalPoint / depth;
                        worldPoint = worldPoint / depth;

                        byte[] normalBytes = normalMap.GetPixelColor((int)texturePoint.X, normalMap.height-(int)texturePoint.Y);
                        byte[] specularBytes = specularMap.GetPixelColor((int)texturePoint.X, specularMap.height-(int)texturePoint.Y);
                        Vector3 normalFromFile = Vector3.Normalize(new Vector3(((float)normalBytes[2]/255) * 2 - 1, (((float)normalBytes[1]/255) * 2 - 1 ), ((float)normalBytes[0]/255) * 2 - 1));
                        normalPoint = Vector3.Normalize(normalPoint);
                        tbn.setNormal(normalPoint);
                        tbn.reort();
                        MatrixTransformations.multiply3(tbn.tbnMatrix, ref normalFromFile);
                        normalFromFile = Vector3.Normalize(normalFromFile);

                        float spec = 0;
                        float intensity = 0;
                      
                        float cosWithinNormals = Vector3.Dot(normalFromFile, normalPoint);
                        Vector3 viewDir = Vector3.Subtract(camera.getPosition(), new Vector3(worldPoint.X, worldPoint.Y, worldPoint.Z));
                    
                     
                        if (isFromFile)
                        {
                            intensity = getIntensity(normalFromFile, new Vector3(worldPoint.X, worldPoint.Y, worldPoint.Z));
                            spec = getSpecularLight(normalFromFile, new Vector3(worldPoint.X, worldPoint.Y, worldPoint.Z));
                        }
                        else {
                            intensity = getIntensity(normalPoint, new Vector3(worldPoint.X, worldPoint.Y, worldPoint.Z));
                            spec = getSpecularLight(normalPoint, new Vector3(worldPoint.X, worldPoint.Y, worldPoint.Z));
                        }
                       
                      
                        byte[] colorFromFile;
                     
                        colorFromFile = diffuseMap.GetPixelColor((int)Math.Round(texturePoint.X),diffuseMap.height- (int)Math.Round(texturePoint.Y));

              
                        Vector3 testReflect = Reflect(Vector3.Normalize(viewDir), normalFromFile);
                        //Vector3 testColor = specularLightColor(new Vector3(worldPoint.X,worldPoint.Y,worldPoint.Z));
                        Vector3 testColor = specularLightColor(new Vector3(testReflect.X, testReflect.Y, testReflect.Z));
                        specColor.X = testColor.X;
                        specColor.Y = testColor.Y;
                        specColor.Z = testColor.Z;
                        zBuffer[x + (y) * imageWidth] = screenPoint.Z;
                            switch (lightMode)
                            {
                                case 0:
                                    {

                                    // drawPoint((int)x, (int)y, (byte)Math.Min((colorFromFile[0] * intensity + colorFromFile[0] * ambientStrength + specColor.X * specularBytes[0] / 255), (byte)255), (byte)Math.Min((colorFromFile[1] * intensity + colorFromFile[1] * ambientStrength + specColor.Y * specularBytes[1] / 255), (byte)255), (byte)Math.Min((colorFromFile[2] * intensity + colorFromFile[2] * ambientStrength + specColor.Z  * specularBytes[2] / 255), (byte)255));
                                    //drawPoint((int)x, (int)y, (byte)Math.Min((specColor.X), (byte)255), (byte)Math.Min((specColor.Y), (byte)255), (byte)Math.Min((specColor.Z), (byte)255));
                               
                                    float cA = specColor.X / 255;
                                    float cB = specColor.Y / 255;
                                    float cC = specColor.Z / 255;
                                    float dA = (float)colorFromFile[0] / 255;
                                    float dB = (float)colorFromFile[1] / 255;
                                    float dC = (float)colorFromFile[2] / 255;
                                    byte R = (byte)Math.Min((dA * cA * intensity + dA  * cA * ambientStrength + cA * spec * specularBytes[0]/255) *255, (byte)255);
                                    byte G = (byte)Math.Min((dB * cB * intensity + cB * ambientStrength * dB + cB * spec * specularBytes[1]/255)*255, (byte)255);
                                    byte B = (byte)Math.Min((cC * intensity * dC+ cC * ambientStrength * dC+ cC * spec * specularBytes[2]/255)*255, (byte)255);
                                    drawPoint((int)x, (int)y, R,G , B);
                                    //drawPoint((int)x, (int)y, (byte)Math.Min((colorFromFile[0] * specColor.X * intensity + colorFromFile[0] * specColor.X * ambientStrength ), (byte)255), (byte)Math.Min((colorFromFile[1] * specColor.Y * intensity + specColor.Y * ambientStrength * colorFromFile[1] ), (byte)255), (byte)Math.Min((specColor.Z * intensity * colorFromFile[2] + specColor.Z * ambientStrength * colorFromFile[2] ), (byte)255));
                                    //drawPoint((int)x, (int)y, (byte)Math.Min((colorFromFile[0] * intensity + colorFromFile[0] * ambientStrength + specColor.X  * specularStrength), (byte)255), (byte)Math.Min((colorFromFile[1] * intensity + colorFromFile[1] * ambientStrength + specColor.Y  * specularStrength), (byte)255), (byte)Math.Min((colorFromFile[2] * intensity + colorFromFile[2] * ambientStrength + specColor.Z * specularStrength), (byte)255));
                                    break;
                                    }
                                case 1:
                                    {
                                        //drawPoint((int)x, (int)y, (byte)Math.Min((colorFromFile[0] * intensity + specColor.X  * specularBytes[0] / 255), (byte)255), (byte)Math.Min((colorFromFile[1] * intensity + specColor.Y * specularBytes[1] / 255), (byte)255), (byte)Math.Min((colorFromFile[2] * intensity + specColor.Z * specularBytes[2] / 255), (byte)255));
                                        drawPoint((int)x, (int)y, (byte)Math.Min((colorFromFile[0] * intensity + specColor.X *spec * specularStrength), (byte)255), (byte)Math.Min((colorFromFile[1] * intensity + specColor.Y *spec * specularStrength), (byte)255), (byte)Math.Min((colorFromFile[2] * intensity + specColor.Z * spec * specularStrength), (byte)255));
                                        break;
                                    }
                                case 2:
                                    {
                                        drawPoint((int)x, (int)y, (byte)Math.Min((specularStrength*specColor.X), (byte)255), (byte)Math.Min((specularStrength * specColor.Y), (byte)255), (byte)Math.Min((specularStrength * specColor.Z), (byte)255));
                                        // drawPoint((int)x, (int)y, (byte)Math.Min((specColor.X * spec * specularBytes[0] / 255), (byte)255), (byte)Math.Min((specColor.Y * spec * specularBytes[1] / 255), (byte)255), (byte)Math.Min((specColor.Z * spec * specularBytes[2] / 255), (byte)255));
                                        //drawPoint((int)x, (int)y, (byte)Math.Min((specColor.X * spec * specularStrength), (byte)255), (byte)Math.Min((specColor.Y * spec * specularStrength), (byte)255), (byte)Math.Min((specColor.Z * spec * specularStrength), (byte)255));
                                        break;
                                    }
                                case 3:
                                    {
                                        drawPoint((int)x, (int)y, (byte)Math.Min((colorFromFile[0] * ambientStrength + specColor.X * specularBytes[0] / 255), (byte)255), (byte)Math.Min((colorFromFile[1] * ambientStrength + specColor.Y  * specularBytes[1] / 255), (byte)255), (byte)Math.Min((colorFromFile[2] * ambientStrength + specColor.Z * specularBytes[2] / 255), (byte)255));
                                        //drawPoint((int)x, (int)y, (byte)Math.Min((colorFromFile[0] * ambientStrength + specColor.X * spec * specularStrength), (byte)255), (byte)Math.Min((colorFromFile[1] * ambientStrength + specColor.Y * spec * specularStrength), (byte)255), (byte)Math.Min((colorFromFile[2] * ambientStrength + specColor.Z * spec * specularStrength), (byte)255));
                                        break;
                                    }
                                case 4:
                                    {
                                        drawPoint((int)x, (int)y, (byte)Math.Min((colorFromFile[0] * ambientStrength), (byte)255), (byte)Math.Min((colorFromFile[1] * ambientStrength), (byte)255), (byte)Math.Min((colorFromFile[2] * ambientStrength), (byte)255));
                                        break;
                                    }
                                case 5:
                                    {
                                        drawPoint((int)x, (int)y, (byte)Math.Min((colorFromFile[0] * intensity), (byte)255), (byte)Math.Min((colorFromFile[1] * intensity), (byte)255), (byte)Math.Min((colorFromFile[2] * intensity), (byte)255));
                                        break;
                                    }
                            }
                        
                      
                   }

                }
            }
        }
        private void drawLines(Vector4[] points, List<int>[] poly)
        {
            Parallel.For(0, poly.Length, (i) =>
            {
                List<int> face = poly[i];
                bool isOutside = false;
                foreach (int point in face)
                {
                    if (points[point].W == 0)
                    {
                        isOutside = true;
                    }
                }
                if (!isOutside)
                {
                    for (int j = 0; j < face.Count; j++)
                    {
                        Vector4 v0 = points[face[j]];
                        Vector4 v1 = points[(face[((j + 1) % face.Count)])];
                        int x0 = (int)v0.X;
                        int y0 = (int)v0.Y;
                        int x1 = (int)v1.X;
                        int y1 = (int)v1.Y;
                        BresenhamLine(x0, y0, x1, y1, points);
                    }
                }
            
            });
              
        }
        private void onLeftMouseDown(object sender, MouseButtonEventArgs e)
        {
            Point p = e.GetPosition(this);
            yPrevMouse = (float)p.Y;
            xPrevMouse = (float)p.X;
            isPressed = true;
        }
        private void onLeftMouseUp(object sender, MouseButtonEventArgs e)
        {
            isPressed = false;
        }
        private void Button_Click(object sender, RoutedEventArgs e)
        {
            Microsoft.Win32.OpenFileDialog openFileDialog = new Microsoft.Win32.OpenFileDialog();
            openFileDialog.Filter = "All Files (*.*)|*.*";

            if (openFileDialog.ShowDialog() == true)
            {
                string filePath = openFileDialog.FileName;

                try
                {
                    string fileContent = File.ReadAllText(filePath);
                    String fileData = fileContent;
                    model = Parser.Parse(fileData);
                    MatrixTransformations.worldModel = new Vector4[model.GetPointCount()];
                    MatrixTransformations.projectionPoints = new Vector4[model.GetPointCount()];
                    // model.structPoligons();
                    // model.calculatePolyNormals();
                    // model.calculatePointsNormals();
                    diffuseMap = new JPGImage("D:\\3dmodels\\Cat\\textures\\M_Cat_Statue_albedo.jpg");
                    //diffuseMap = new JPGImage("D:\\3dmodels\\Cat\\textures\\M_Cat_Statue_metallic.jpg");

                    normalMap = new JPGImage("D:\\3dmodels\\Cat\\textures\\M_Cat_Statue_normal.png");
                    //diffuseMap = new JPGImage("D:\\3dmodels\\of\\main_body_blinn_albedo.jpeg");
                    //diffuseMap = new JPGImage("D:\\3dmodels\\Lich\\KelThuzad_default_color.png");

                    //Woman
                    //diffuseMap = new JPGImage("D:\\3dmodels\\Woman\\ruby_final.png");
                    //normalMap = new JPGImage("D:\\3dmodels\\of\\main_body_blinn_normal.png");

                    //Floor
                    // diffuseMap = new JPGImage("D:\\3dmodels\\FloorOpenGL\\floor.jpg");
                    // normalMap = new JPGImage("D:\\3dmodels\\FloorOpenGL\\floor_normal.png");

                    //Cube
                    //diffuseMap = new JPGImage("D:\\3dmodels\\Cube\\cubeD.bmp");
                    //normalMap = new JPGImage("D:\\3dmodels\\Cube\\NormalMap.jpg");


                    //Shovel

                   // diffuseMap = new JPGImage("D:\\3dmodels\\Shovel\\diffuse.png");
                   // normalMap = new JPGImage("D:\\3dmodels\\Shovel\\normal.png");
                    specularMap = new JPGImage("D:\\3dmodels\\Shovel\\specular.png");
                 

                    // African
                   // diffuseMap = new JPGImage("D:\\3dmodels\\african\\african_head_diffuse.jpg");
                   //normalMap = new JPGImage("D:\\3dmodels\\african\\african_head_nm_tangent.jpg");
                    //normalMap = new JPGImage("D:\\3dmodels\\Cube\\NormalMap.jpg");

                    leftImage = new JPGImage(skyboxpathes[3]);
                    rightImage = new JPGImage(skyboxpathes[2]);
                    backImage = new JPGImage(skyboxpathes[0]);
                    frontImage = new JPGImage(skyboxpathes[1]);
                    topImage = new JPGImage(skyboxpathes[4]);
                    bottomImage = new JPGImage(skyboxpathes[5]);

                    //normalMap = new TgaImage("D:\\3dmodels\\african_head_nm_tangent.tga");
                    //tgaDiffuseMap = new TgaImage("D:\\3dmodels\\african_head_diffuse.tga");
                    // model.GetPoints().CopyTo(MatrixTransformations.worldModel,0);
                    render();
                }
                catch (IOException ex)
                {
                    MessageBox.Show($"Ошибка чтения файла: {ex.Message}");
                }
                
            }
        }
        
        private void PlusScaleClick(object sender, RoutedEventArgs e)
        {
            currentScale.X += 0.01f;
            currentScale.Y += 0.01f;
            currentScale.Z += 0.01f;
            render();
        }
        private void MinusScaleClick(object sender, RoutedEventArgs e)
        {
            currentScale.X -= 0.01f;
            currentScale.Y -= 0.01f;
            currentScale.Z -= 0.01f;
            render();
        }
        void Swap<T>(ref T x, ref T y)
        {
            T temp = x;
            x = y;
            y = temp;
        }

        private void Image_PreviewMouseWheel(object sender, MouseWheelEventArgs e)
        {
            int delta = e.Delta; 
            currentScale.X += ((float)delta / 1000);
            currentScale.Y += ((float)delta / 1000);
            currentScale.Z += ((float)delta / 1000);
            render(); 
        }
        private void BresenhamLine(int x0, int y0, int x1, int y1,Vector4[] points)
        {
            var steep = Math.Abs(y1 - y0) > Math.Abs(x1 - x0); // Проверяем рост отрезка по оси икс и по оси игрек
                                                               // Отражаем линию по диагонали, если угол наклона слишком большой
            if (steep)
            {
                Swap(ref x0, ref y0); // Перетасовка координат вынесена в отдельную функцию для красоты
                Swap(ref x1, ref y1);
            }
            // Если линия растёт не слева направо, то меняем начало и конец отрезка местами
            if (x0 > x1)
            {
                Swap(ref x0, ref x1);
                Swap(ref y0, ref y1);
            }
            int dx = x1 - x0;
            int dy = Math.Abs(y1 - y0);
            int error = dx; // Здесь используется оптимизация с умножением на dx, чтобы избавиться от лишних дробей
            int ystep = (y0 < y1) ? 1 : -1; // Выбираем направление роста координаты y
            int y = y0;
            for (int x = x0; x <= x1; x++)
            {
                drawPoint(steep ? y : x, steep ? x : y,255,255,255); // Не забываем вернуть координаты на место
                error -= dy*2;
                if (error < 0)
                {
                    y += ystep;
                    error += dx*2;
                }
            }
        }
        private void changeMode(object sender, RoutedEventArgs e)
        {
            wireFrameMode = !wireFrameMode;
            render();
        }
        /// <summary>
        /// Calculates the coefficients for interpolating screen coordinates based on the provided triangle vertices.
        /// </summary>
        /// <param name="vertices">The screen coordinates of the triangle vertices.</param>
        /// <returns>An array of vectors representing the coefficients for interpolating screen coordinates.</returns>


        private void i_MouseMove(object sender, MouseEventArgs e)
        {
            if (isPressed)
            {

                 Point p = e.GetPosition(this);
                 float xMouseDelta = (float)p.X - xPrevMouse;
                 float yMouseDelta = (float)p.Y - yPrevMouse;
                 xPrevMouse = (float)p.X;
                 yPrevMouse = (float)p.Y;
                 camera.rotateY(yMouseDelta,(float) drawingImage.Height);
                 camera.rotateZ(xMouseDelta, (float)drawingImage.Width);
                 xPrevMouse =(float) p.X;
                 yPrevMouse =(float) p.Y;
                 for(int i = 0;i<skyboxVertexes.Length;i++)
                 {
                    MatrixTransformations.rotateY((float)((xMouseDelta / Width) * 2 * Math.PI), ref skyboxVertexes[i]);
                    MatrixTransformations.rotateX((float)(-(yMouseDelta / Height) * 2 * Math.PI), ref skyboxVertexes[i]);
                }
                 render();
            }
        }

        private void changeNormals(object sender, RoutedEventArgs e)
        {
            isFromFile = !isFromFile;
            render();
        }

        private void addXLight(object sender, RoutedEventArgs e)
        {
            lightPos.X += 1;
            camera.addX(0.5f);
            render();
        }
        private void subtractXLight(object sender, RoutedEventArgs e)
        {
            lightPos.X -= 1;
            camera.addX(-0.5f);
            render();
        }

        private void addYLight(object sender, RoutedEventArgs e)
        {
            lightPos.Y += 1;
            camera.addY(0.5f);
            render();
        }
        private void subtractYLight(object sender, RoutedEventArgs e)
        {
            lightPos.Y -= 1;
            camera.addY(-0.5f);
            render();
        }
        private void addZLight(object sender, RoutedEventArgs e)
        {
            lightPos.Z += 1;
            camera.addZ(0.5f);
            render();
        }
        private void subtractZLight(object sender, RoutedEventArgs e)
        {
            lightPos.Z -= 1;
            camera.addZ(-0.5f);
            render();
        }

        private void ComboBox_SelectionChanged(object sender, System.Windows.Controls.SelectionChangedEventArgs e)
        {
           //    < ComboBoxItem Content = "All" />
           // < ComboBoxItem Content = "Diffuse+Spec" />
           // < ComboBoxItem Content = "Specular" />
           // < ComboBoxItem Content = "Spec+ambient" />
           // < ComboBoxItem Content = "ambient" />
          //  < ComboBoxItem Content = "diffuse" />
            ComboBoxItem selectedItem = (ComboBoxItem)modeBox.SelectedItem;

            // Выполнить действия, основанные на выбранном элементе
            string selectedOption = selectedItem.Content.ToString();
            switch (selectedOption)
            {
                case "All":
                    {
                        lightMode = 0;
                        break;
                    }
                case "Diffuse+Spec":
                    {
                        lightMode = 1;
                        break;
                    }
                case "Specular":
                    {
                        lightMode = 2;
                        break;
                    }
                case "Spec+ambient":
                    {
                        lightMode = 3;
                        break;
                    }
                case "ambient":
                    {
                        lightMode = 4;
                        break;
                    }
                case "diffuse":
                    {
                        lightMode = 5;
                        break;
                    }
            }
            render();
        }

        private void Button_Click_1(object sender, RoutedEventArgs e)
        {
            followMode = !followMode;
            render();
        }

        private void rBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            string text = rBox.Text;
            try
            {
                specColor.Z = int.Parse(rBox.Text);
            }catch(Exception ex)
            {
                specColor.Z = 255;
            }
            if (model != null)
            {
                render();
            }
        }

        private void gBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            try
            {
                specColor.Y = int.Parse(gBox.Text);
            }
            catch (Exception ex)
            {
                specColor.Y = 255;
            }
            if (model != null)
            {
                render();
            }
        }

        private void bBox_TextChanged(object sender, TextChangedEventArgs e)
        {
            try
            {
                specColor.X = int.Parse(bBox.Text);
            }
            catch (Exception ex)
            {
                specColor.X = 255;
            }
            if (model != null)
            {
                render();
            }
        }
    }
}
