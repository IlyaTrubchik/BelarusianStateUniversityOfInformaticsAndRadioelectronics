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
        List<int>[] Poligons;
        int pointCount;

        Matrix4x4 WordTras;

        public void SetPoints(List<Vector4> Ver)
        {
            Points = Ver.ToArray();
            pointCount = Ver.Count;
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
        public Vector4[] GetPoints()
        {
            return this.Points;
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
