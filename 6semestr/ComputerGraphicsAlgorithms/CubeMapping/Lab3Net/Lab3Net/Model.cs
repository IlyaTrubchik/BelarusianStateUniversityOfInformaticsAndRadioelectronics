using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Numerics;
using System.Windows.Media.Imaging;

namespace Lab1Wpf
{
    internal class Model
    {
        Vector4[] Points;
        public Vector3[] PointsN;
        List<int>[] Poligons;
        public List<int>[] PoligonsN;
        int pointCount;
        List<Vector4> polyNormals = new List<Vector4>();
        public static List<Vector4> pointsNormals = new List<Vector4>();

        public List<int>[] PoligonsT;
        public Vector3[] PointsT;
        Matrix4x4 WordTras;

        public void SetPoligonsT(List<List<int>> Pl)
        {
            PoligonsT = Pl.ToArray();
        }

        public void SetPointsT(List<Vector3> Ver)
        {
            PointsT = Ver.ToArray();
        }
        public List<Vector4> getPolyNormals()
        {
            return this.polyNormals;
        }

        public void SetPoints(List<Vector4> Ver)
        {
            Points = Ver.ToArray();
            pointCount = Ver.Count;
        }

        public void SetPointsN(List<Vector3> Ver)
        {
            PointsN = Ver.ToArray();
        }

        public void SetPoints(Vector4[] points)
        {
            this.Points = points;
        }
        public List<int>[] getPolygons()
        {
            return this.Poligons;
        }
        public Vector4 getPoint(int i)
        {
            return Points[i];
        }
        public void SetPoligons(List<List<int>> Pl)
        {
            Poligons = Pl.ToArray();
        }

        public void SetPoligonsN(List<List<int>> Pl)
        {
            PoligonsN = Pl.ToArray();
        }

        public Vector4[] GetPoints()
        {
            return this.Points;
        }

        public void calculatePolyNormals()
        {
            for (int i = 0; i < Poligons.Length; i++)
            {
                List<int> face = Poligons[i];
                for (int j = 2; j < face.Count; j++)
                {
                    int p1 = face[0];
                    int p2 = face[j - 1];
                    int p3 = face[j];
                    Vector4 normal = MatrixTransformations.vectorMultiply(Vector4.Subtract(Points[p2], Points[p1]),
                    Vector4.Subtract(Points[p3], Points[p1]));
                    normal = normal.normalize();
                    polyNormals.Add(normal);
                }
            }
        }
        public void structPoligons()
        {
            List<List<int>> newPoligons = new List<List<int>>();
            for (int i = 0; i < Poligons.Length; i++)
            {
                List<int> face = Poligons[i];
                if (face.Count == 3)
                {
                    newPoligons.Add(face);
                }
                else
                {
                    for (int j = 2; j < face.Count; j++)
                    {
                        List<int> newFace = new List<int>();
                        newFace.Add(face[0]);
                        newFace.Add(face[j - 1]);
                        newFace.Add(face[j]);
                        newPoligons.Add(newFace);
                    }
                }
            }
            Poligons = newPoligons.ToArray();
        }
        public void calculatePointsNormals()
        {
            Vector4 normal = new Vector4(0, 0, 0, 0);
            for (int i = 0; i < Points.Length; i++)
            {
                for (int j = 0; j < Poligons.Length; j++)
                {
                    List<int> face = Poligons[j];
                    if (face.Contains(i))
                    {
                        normal = Vector4.Add(normal, polyNormals[j]);
                    }
                }
                pointsNormals.Add(normal.normalize());
            }

        }

        public int GetPointCount()
        {
            return this.pointCount;
        }
        /* public void render(WriteableBitmap writeableBitmap,float width,float height,Vector4 eye)
         {
             foreach(Vector4 point in Points)
             {
                 float[,] matrix = MatrixTransformations.getCameraMatrix(, up, points[i]);
                 points[i] = (MatrixTransformations.multiply(matrix, points[i]));
                 matrix = MatrixTransformations.getProjectionMatrix(1800, 900, 5, 1000);
                 points[i] = (MatrixTransformations.multiply(matrix, points[i]));
                 matrix = MatrixTransformations.getViewPortMatrix(1800, 900, 0, 0);
                 points[i] = (MatrixTransformations.multiply(matrix, points[i]));
             }
         }
        */
    }
}
