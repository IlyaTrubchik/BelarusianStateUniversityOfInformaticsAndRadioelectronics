using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using Lab1Wpf;
namespace Lab3Net
{
    internal class TBN
    {
        Vector3 T;
        Vector3 B;
        Vector3 N;
        public float[,] tbnMatrix = new float[3,3];
        public TBN()
        {

        }

        public void setNormal(Vector3 n)
        {
            //this.N = Vector3.Cross(this.T, this.B);
            this.N = Vector3.Normalize(n);
        }

        public void countTB(Vector3 edge1,Vector3 edge2,Vector3 deltaUV1,Vector3 deltaUV2)
        {
            float coef = (float)1 / (deltaUV1.X * deltaUV2.Y - deltaUV2.X * deltaUV1.Y);

            float tangentX = coef * (deltaUV2.Y * edge1.X - deltaUV1.Y * edge2.X);
            float tangentY = coef * (deltaUV2.Y * edge1.Y - deltaUV1.Y * edge2.Y);
            float tangentZ = coef * (deltaUV2.Y * edge1.Z - deltaUV1.Y * edge2.Z);
            T = Vector3.Normalize(new Vector3(tangentX, tangentY, tangentZ));

            //float bitangentX = coef * (-deltaUV2.X * edge1.X + deltaUV1.X * edge2.X);
            //float bitangentY = coef * (-deltaUV2.X * edge1.Y + deltaUV1.X * edge2.Y);
            //float bitangentZ = coef * (-deltaUV2.X * edge1.Z + deltaUV1.X * edge2.Z);

             //B = Vector3.Normalize(new Vector3(bitangentX, bitangentY, bitangentZ));
            B = Vector3.Normalize(Vector3.Cross(N, T));
        }

        public void formTBN()
        {
          /*  Matrix4x4 matrix = new Matrix4x4(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
            matrix.M11 = scale.X;
            matrix.M22 = scale.Y;
            matrix.M33 = scale.Z;
            matrix.M44 = 1;
            MatrixTransformations.multiply3(matrix, ref T);
            MatrixTransformations.multiply3(matrix, ref B);
            MatrixTransformations.multiply3(matrix, ref N);
          */
            Vector3 normalizedTangent = Vector3.Normalize(T);
            Vector3 normalizedBiTangent = Vector3.Normalize(B);
            Vector3 normalizedNormal = Vector3.Normalize(N);
            tbnMatrix[0,0] = normalizedTangent.X;
            tbnMatrix[0, 1] = normalizedTangent.Y;
            tbnMatrix[0, 2] = normalizedTangent.Z;
            tbnMatrix[1, 0] = normalizedBiTangent.X;
            tbnMatrix[1, 1] = normalizedBiTangent.Y;
            tbnMatrix[1, 2] = normalizedBiTangent.Z;
            tbnMatrix[2, 0] = normalizedNormal.X;
            tbnMatrix[2, 1] = normalizedNormal.Y;
            tbnMatrix[2, 2] = normalizedNormal.Z;


            //In Columns
            //tbnMatrix[0, 0] = normalizedTangent.X;
            //tbnMatrix[1, 0] = normalizedTangent.Y;
            //tbnMatrix[2, 0] = normalizedTangent.Z;
            //tbnMatrix[0, 1] = normalizedBiTangent.X;
            //tbnMatrix[1, 1] = normalizedBiTangent.Y;
            //tbnMatrix[2, 1] = normalizedBiTangent.Z;
           // tbnMatrix[0, 2] = normalizedNormal.X;
            //tbnMatrix[1, 2] = normalizedNormal.Y;
            //tbnMatrix[2, 2] = normalizedNormal.Z;
            checkTbn();

        }
        private void checkTbn()
        {
            float TB = Vector3.Dot(T, B);
            float BN = Vector3.Dot(B, N);
            float TN = Vector3.Dot(T, N);
            float x = tbnMatrix[0, 0] * N.X + tbnMatrix[1, 0] * N.Y + tbnMatrix[2, 0] * N.Z;
            float y = tbnMatrix[0, 1] * N.X + tbnMatrix[1, 1] * N.Y + tbnMatrix[2, 1] * N.Z;
            float z = tbnMatrix[0, 2] * N.X + tbnMatrix[1, 2] * N.Y + tbnMatrix[2, 2] * N.Z;

            float xT = tbnMatrix[0, 0] * T.X + tbnMatrix[1, 0] * T.Y + tbnMatrix[2, 0] * T.Z;
            float yT = tbnMatrix[0, 1] * T.X + tbnMatrix[1, 1] * T.Y + tbnMatrix[2, 1] * T.Z;
            float zT = tbnMatrix[0, 2] * T.X + tbnMatrix[1, 2] * T.Y + tbnMatrix[2, 2] * T.Z;

            float xB = tbnMatrix[0, 0] * B.X + tbnMatrix[1, 0] * B.Y + tbnMatrix[2, 0] * B.Z;
            float yB = tbnMatrix[0, 1] * B.X + tbnMatrix[1, 1] * B.Y + tbnMatrix[2, 1] * B.Z;
            float zB = tbnMatrix[0, 2] * B.X + tbnMatrix[1, 2] * B.Y + tbnMatrix[2, 2] * B.Z;

            Console.WriteLine("gg");
        }

        public void reort()
        {
            T = Vector3.Normalize(T - Vector3.Dot(T, N) * N);
            B = Vector3.Cross(N, T);
            formTBN();
        }
    }
}
