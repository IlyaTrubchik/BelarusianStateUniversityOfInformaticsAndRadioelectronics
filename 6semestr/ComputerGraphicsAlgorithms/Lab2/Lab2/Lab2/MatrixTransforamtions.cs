﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Numerics;


namespace Lab1Wpf
{
    public class MatrixTransformations
    {
        public static float[,] projectionMatrix;
        public static float[,] cameraMatrix;
        public static float[,] viewPortMatrix;
        public static Vector4[] worldModel;
        public static Vector4[] projectionPoints;
        public MatrixTransformations()
        {
        }
        public static void multiply(float[,] matrix,ref Vector4 vector)
        {
            List<float> numbers = new List<float>();
            Vector4 oldVector = new Vector4(vector.X, vector.Y, vector.Z, vector.W);
            vector.X = matrix[0, 0] * oldVector.X + matrix[0, 1] * oldVector.Y + matrix[0, 2] * oldVector.Z + matrix[0, 3] * vector.W;
            vector.Y = matrix[1, 0] * oldVector.X + matrix[1, 1] * oldVector.Y + matrix[1, 2] * oldVector.Z + matrix[1, 3] * vector.W;
            vector.Z = matrix[2, 0] * oldVector.X + matrix[2, 1] * oldVector.Y + matrix[2, 2] * oldVector.Z + matrix[2, 3] * vector.W;
            vector.W = matrix[3, 0] * oldVector.X + matrix[3, 1] * oldVector.Y + matrix[3, 2] * oldVector.Z + matrix[3, 3] * vector.W;
       
        }
        public static void scale(Vector4 scaleVector,ref  Vector4 vector)
        {
            float[,] matrix = new float[4, 4] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
            matrix[0, 0] = scaleVector.X;
            matrix[1, 1] = scaleVector.Y;
            matrix[2, 2] = scaleVector.Z;
            matrix[3, 3] = 1;
            multiply(matrix,ref vector);
        }   
        public static void toWorld(Vector4 scale,Vector4[] points)
        {
            Parallel.For(0, points.Length, (i) =>
                {
                    MatrixTransformations.scale(scale,ref points[i]);
                }
            );
        }
        public static void toCamera(Vector4[] points)
        {
            Parallel.For(0, points.Length, (i) =>
            {
                MatrixTransformations.multiply(MatrixTransformations.cameraMatrix, ref points[i]);
            }
           );   
        }
        public static void toProjection(Vector4[] points)
        {
            Parallel.For(0, points.Length, (i) =>
            {
                MatrixTransformations.multiply(MatrixTransformations.projectionMatrix, ref points[i]);
            }
           );
        }
        public static void cut(Vector4[] points)
        {
            Parallel.For(0, points.Length, (i) =>
            {
                if (Math.Abs(points[i].X) <= Math.Abs(points[i].W) && Math.Abs(points[i].X) >= 0
             && Math.Abs(points[i].Y) <= Math.Abs(points[i].W) && Math.Abs(points[i].Y) >= 0
             && points[i].Z >= 0 && points[i].Z <= points[i].W
              )
                {
                    points[i].Z = points[i].Z / points[i].W;
                    points[i].X = points[i].X / points[i].W;
                    points[i].Y = points[i].Y / points[i].W;
                    points[i].W = 1;
                }
                else
                {
                    points[i].W = 0;
                }
            }
         );    
        }
        public static void toScreen(Vector4[] points)
        {
            Parallel.For(0, points.Length, (i) =>
            {
                MatrixTransformations.multiply(MatrixTransformations.viewPortMatrix, ref points[i]);
            });
        }
        public static void rotateX(float angle,ref Vector4 vector)
        {
            float[,] matrix = new float[4, 4] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
            matrix[0, 0] = 1;
            matrix[1, 1] = (float)Math.Cos(angle);
            matrix[1, 2] = (float)-Math.Sin(angle);
            matrix[2, 1] = (float)Math.Sin(angle);
            matrix[2, 2] = (float)Math.Cos(angle);
            matrix[3, 3] = 1;
            multiply(matrix,ref vector);
        }
        public static void rotateY(float angle,ref Vector4 vector)
        {
            float[,] matrix = new float[4, 4] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
            matrix[0, 0] = (float)Math.Cos(angle);
            matrix[0, 2] = (float)Math.Sin(angle);
            matrix[1, 1] = 1;
            matrix[2, 0] = (float)-Math.Sin(angle);
            matrix[2, 2] = (float)Math.Cos(angle);
            matrix[3, 3] = 1;
            multiply(matrix,ref vector);
        }
        public static void rotateZ(float angle,ref Vector4 vector)
        {
            float[,] matrix = new float[4, 4] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
            matrix[0, 0] = (float)Math.Cos(angle);
            matrix[0, 1] = (float)-Math.Sin(angle);
            matrix[1, 0] = (float)Math.Sin(angle);
            matrix[1, 1] = (float)Math.Cos(angle);
            matrix[2, 2] = 1;
            matrix[3, 3] = 1;
            multiply(matrix,ref vector);
        }
        public static void move(Vector4 translationVector,ref Vector4 vector)
        {
            float[,] matrix = new float[4, 4] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
            matrix[0, 0] = 1;
            matrix[1, 1] = 1;
            matrix[2, 2] = 1;
            matrix[3, 3] = 1;
            matrix[0, 3] = translationVector.X;
            matrix[1, 3] = translationVector.Y;
            matrix[2, 3] = translationVector.Z;
            multiply(matrix,ref vector);
        }
        public static void getCameraMatrix(Vector4 eye, Vector4 up, Vector4 target)
        {
            Vector4 ZAxis;
            Vector4 XAxis;
            Vector4 YAxis;
            float[,] matrix = new float[4, 4] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
            ZAxis = minus(eye, target);
            ZAxis = ZAxis.normalize();
            XAxis = vectorMultiply(up, ZAxis);
            XAxis = XAxis.normalize();
            YAxis = vectorMultiply(ZAxis, XAxis);
            YAxis = YAxis.normalize();
            //YAxis = up;
            matrix[0, 0] = XAxis.X;//XAxis.X;
            matrix[0, 1] = XAxis.Y;
            matrix[0, 2] = XAxis.Z;
            matrix[1, 0] = YAxis.X;
            matrix[1, 1] = YAxis.Y;//YAxis.Y;
            matrix[1, 2] = YAxis.Z;
            matrix[2, 0] = ZAxis.X;
            matrix[2, 1] = ZAxis.Y;
            matrix[2, 2] = ZAxis.Z;//ZAxis.Z;
            matrix[2, 3] = (float)-vectorSMultiply(ZAxis, eye);
            matrix[1, 3] = (float)-vectorSMultiply(YAxis, eye);
            matrix[0, 3] = (float)-vectorSMultiply(XAxis, eye);
            matrix[3, 3] = 1;
            MatrixTransformations.cameraMatrix =  matrix;
        }
        public static Vector4 normalize(Vector4 vector)
        {
            Vector4 resultVector = new Vector4();
            float length = (float)Math.Sqrt(vector.X * vector.X + vector.Y * vector.Y + vector.Z * vector.Z);
            resultVector.X = vector.X / length;
            resultVector.Y = vector.Y / length;
            resultVector.Z = vector.Z / length;
            return resultVector;
        }
        public static Vector4 minus(Vector4 vectorA, Vector4 vectorB)
        {
            Vector4 resultVector = new Vector4();
            resultVector.X = vectorA.X - vectorB.X;
            resultVector.Y = vectorA.Y - vectorB.Y;
            resultVector.Z = vectorA.Z - vectorB.Z;
            return resultVector;
        }
        public static float vectorSMultiply(Vector4 vectorA, Vector4 vectorB)
        {
            return vectorA.X * vectorB.X + vectorA.Y * vectorB.Y + vectorA.Z * vectorB.Z;
        }
        public static Vector4 vectorMultiply(Vector4 vectorA, Vector4 vectorB)
        {
            Vector4 resultVector = new Vector4();
            resultVector.X = vectorA.Y * vectorB.Z - vectorA.Z * vectorB.Y;
            resultVector.Y = vectorA.Z * vectorB.X - vectorA.X * vectorB.Z;
            resultVector.Z = vectorA.X * vectorB.Y - vectorA.Y * vectorB.X;
            return resultVector;
        }
        public static void getProjectionMatrix(float width, float height, float zNear, float zFar)
        {
            float fov = 2;
            float[,] matrix = new float[4, 4] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
            matrix[0, 0] = (float)1 / ((width / height)*(float)Math.Tan(fov / 2));//Math.PI/4.5);
            matrix[1, 1] = 1 / (float)Math.Tan(fov / 2);//(Math.PI/4.5);
            matrix[2, 2] = zFar / (zNear - zFar);
            matrix[2, 3] = (zFar * zNear) / (zNear - zFar);
            matrix[3, 2] = -1;//0.5625/
            MatrixTransformations.projectionMatrix = matrix;
        }
        public static void getViewPortMatrix(float width, float height, float xMin, float yMin)
        {
            float[,] matrix = new float[4, 4] { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
            matrix[0, 0] = width / 2;
            matrix[0, 3] = xMin + width / 2;
            matrix[1, 1] = -height / 2;
            matrix[1, 3] = yMin + height / 2;
            matrix[2, 2] = 1;
            matrix[3, 3] = 1;
            MatrixTransformations.viewPortMatrix = matrix;
        }
    }
}
